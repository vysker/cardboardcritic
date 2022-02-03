package com.cardboardcritic.feed;

import com.cardboardcritic.db.entity.RawReview;
import com.cardboardcritic.db.entity.Review;
import com.cardboardcritic.db.repository.RawReviewRepository;
import com.cardboardcritic.db.repository.ReviewRepository;
import com.cardboardcritic.feed.crawler.OutletCrawler;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.core.Vertx;
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
                             Vertx vertx,
                             Logger log) {

    public CrawlerService(@Named("outletCrawlers") List<OutletCrawler> outletCrawlers,
                          RawReviewRepository rawReviewRepository,
                          ReviewRepository reviewRepository,
                          Vertx vertx,
                          Logger log) {
        this.outletCrawlers = outletCrawlers;
        this.rawReviewRepository = rawReviewRepository;
        this.reviewRepository = reviewRepository;
        this.vertx = vertx;
        this.log = log;
    }

    public void crawl() {
        outletCrawlers.forEach(this::crawl);
    }

    public void crawl(OutletCrawler crawler) {
        log.infof("Crawling article links for '%s'", crawler.getOutlet());

        crawler.getArticleLinks()
                .flatMap(linksFound -> {
                    final Uni<List<RawReview>> visitedRawReviews = rawReviewRepository.list("url in ?1", linksFound);
                    final Uni<List<Review>> visitedReviews = reviewRepository.list("url in ?1", linksFound);

                    return Uni.combine().all().unis(visitedRawReviews, visitedReviews).asTuple()
                            .map(tuple -> Stream.concat(
                                    tuple.getItem1().stream().map(RawReview::getUrl),
                                    tuple.getItem2().stream().map(Review::getUrl)).toList())
                            .invoke(visitedLinks -> log.infof("Found %d articles, %d of which are new",
                                    linksFound.size(), linksFound.size() - visitedLinks.size()))
                            .map(visitedLinks -> linksFound.stream()
                                    .filter(link -> !visitedLinks.contains(link))
                                    .toList());
                })
                .onItem().transformToMulti(newLinks -> Multi.createFrom().iterable(newLinks))
                .map(link -> {
                    try {
                        return crawler.getReview(link);
                    } catch (Exception e) {
                        log.warnf("Failed to scrape article '%s'. Reason: %s", link, e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .invoke(rawReviewRepository::persist);
    }
}

//                .onItem().transformToMulti(tuple -> {
//                    Multi<String> rawReviewUrls = Multi.createFrom().iterable(tuple.getItem1()).map(RawReview::getUrl);
//                    Multi<String> reviewUrls = Multi.createFrom().iterable(tuple.getItem2()).map(Review::getUrl);
//                    return Multi.createBy().concatenating().streams(rawReviewUrls, reviewUrls);
//                })

//        final Multi<String> visitedRawReviewUrls = visitedRawReviews
//                .onItem().transformToMulti(reviews -> Multi.createFrom().iterable(reviews))
//                .onItem().transform(RawReview::getUrl);
//        final Multi<String> visitedReviewUrls = visitedReviews
//                .onItem().transformToMulti(reviews -> Multi.createFrom().iterable(reviews))
//                .onItem().transform(Review::getUrl);

//        Multi.createBy().concatenating()
//                .streams(visitedRawReviews, visitedReviews)
//                .onItem()
//        final List<String> visitedLinks = Stream.concat(visitedRawReviews, visitedReviews).toList();
