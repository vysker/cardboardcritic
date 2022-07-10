package com.cardboardcritic.feed;

import com.cardboardcritic.db.entity.RawReview;
import com.cardboardcritic.db.entity.Review;
import com.cardboardcritic.feed.crawler.OutletCrawler;
import com.cardboardcritic.service.RawReviewService;
import com.cardboardcritic.service.ReviewService;
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
                             RawReviewService rawReviewService,
                             ReviewService reviewService,
                             Logger log) {

    public CrawlerService(@Named("outletCrawlers") List<OutletCrawler> outletCrawlers,
                          RawReviewService rawReviewService,
                          ReviewService reviewService,
                          Logger log) {
        this.outletCrawlers = outletCrawlers;
        this.rawReviewService = rawReviewService;
        this.reviewService = reviewService;
        this.log = log;
    }

    public Uni<Void> crawl() {
        return Multi.createFrom().iterable(outletCrawlers)
                .onItem().transformToUni(this::crawl)
                .concatenate()
                .toUni().replaceWithVoid();
    }

    public Uni<Void> crawl(OutletCrawler crawler) {
        log.infof("Crawling article links for '%s'", crawler.getOutlet());

        return crawler.getArticleLinks()
                .flatMap(linksFound -> {
                    final Uni<List<RawReview>> visitedRawReviews$ = rawReviewService.visited(linksFound);
                    final Uni<List<Review>> visitedReviews$ = reviewService.visited(linksFound);

                    return Uni.combine().all().unis(visitedRawReviews$, visitedReviews$)
                            .combinedWith((visitedRawReviews, visitedReviews) -> Stream.concat(
                                    visitedRawReviews.stream().map(RawReview::getUrl),
                                    visitedReviews.stream().map(Review::getUrl)).distinct().toList())
                            .map(visitedLinks -> linksFound.stream()
                                    .filter(link -> !visitedLinks.contains(link))
                                    .toList())
                            .invoke(visitedLinks -> log.infof("Found %d articles, %d of which are new",
                                    linksFound.size(), linksFound.size() - visitedLinks.size()));
                })
                .onItem().transformToMulti(links -> Multi.createFrom().iterable(links))
                .onItem().transformToUniAndMerge(link -> {
                    try {
                        log.infof("Visiting link: %s", link);
                        return Uni.createFrom().item(crawler.getReview(link));
                    } catch (Exception e) {
                        log.warnf("Failed to scrape article '%s'. Reason: %s", link, e);
                        return Uni.createFrom().nullItem();
                    }
                })
                .filter(Objects::nonNull)
                .call(rawReviewService::persist)
                .collect().asList()
                .replaceWithVoid();
    }
}
