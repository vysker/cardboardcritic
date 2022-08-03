package com.cardboardcritic.feed;

import com.cardboardcritic.db.entity.RawReview;
import com.cardboardcritic.db.entity.Review;
import com.cardboardcritic.db.repository.RawReviewRepository;
import com.cardboardcritic.db.repository.ReviewRepository;
import com.cardboardcritic.feed.crawler.OutletCrawler;
import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Responsible for crawling news outlets for reviews.
 */
@ApplicationScoped
public record CrawlerService(List<OutletCrawler> outletCrawlers,
                             RawReviewRepository rawReviewRepository,
                             ReviewRepository reviewRepository,
                             Logger log) {

    public CrawlerService(@Named("outletCrawlers") List<OutletCrawler> outletCrawlers,
                          RawReviewRepository rawReviewRepository,
                          ReviewRepository reviewRepository,
                          Logger log) {
        this.outletCrawlers = outletCrawlers;
        this.rawReviewRepository = rawReviewRepository;
        this.reviewRepository = reviewRepository;
        this.log = log;
    }

    public Uni<Void> crawl() {
        return Multi.createFrom().iterable(outletCrawlers)
                // We tried doing a transformToUniAndMerge() here, but that messed with database connections and whatnot
                .onItem().transformToUniAndConcatenate(this::crawl)
                .onItem().ignoreAsUni();
    }

    @ReactiveTransactional
    public Uni<Void> crawl(OutletCrawler crawler) {
        log.infof("Crawling article links for '%s'", crawler.getOutlet());

        return crawler.getArticleLinks()
                .flatMap(linksFound -> {
                    final Uni<List<RawReview>> visitedRawReviews$ = rawReviewRepository.visited(linksFound);
                    final Uni<List<Review>> visitedReviews$ = reviewRepository.visited(linksFound);

                    return Uni.combine().all().unis(visitedRawReviews$, visitedReviews$)
                            .combinedWith((visitedRawReviews, visitedReviews) -> Stream.concat(
                                    visitedRawReviews.stream().map(RawReview::getUrl),
                                    visitedReviews.stream().map(Review::getUrl)).distinct().toList())
                            .map(visitedLinks -> linksFound.stream()
                                    .filter(link -> !visitedLinks.contains(link))
                                    .toList())
                            .invoke(newLinks -> log.infof("Found %d articles, %d of which are new",
                                    linksFound.size(), newLinks.size()));
                })
                .onItem().transformToMulti(links -> Multi.createFrom().iterable(links))
                .onItem().transformToUniAndMerge(link -> {
                    try {
                        log.infof("Visiting link: %s", link);
                        return crawler.getReview(link);
                    } catch (Exception e) {
                        log.warnf("Failed to scrape article '%s'. Reason: %s", link, e);
                        return Uni.createFrom().nullItem();
                    }
                })
                .filter(Objects::nonNull)
                .onItem().transformToUniAndConcatenate(x ->
                        // The deferred method makes sure we don't propagate failures to the upstream, so we can
                        // continue on failures
                        Uni.createFrom().deferred(() -> rawReviewRepository.persist(x))
                                .onFailure().recoverWithItem(new RawReview()))
                .onItem().ignoreAsUni();
    }
}
