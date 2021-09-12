package com.cardboardcritic.feed;

import com.cardboardcritic.db.entity.RawReview;
import com.cardboardcritic.db.entity.Review;
import com.cardboardcritic.db.repository.RawReviewRepository;
import com.cardboardcritic.db.repository.ReviewRepository;
import com.cardboardcritic.feed.crawler.OutletCrawler;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
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

    public List<RawReview> getNewReviews() {
        return outletCrawlers.stream()
                .map(this::getNewReviews)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public List<RawReview> getNewReviews(OutletCrawler crawler) {
        log.infof("Crawling article links for '%s'", crawler.getOutlet());

        final List<String> linksFound = crawler.getArticleLinks();

        final Stream<String> visitedRawReviews = rawReviewRepository.list("url in ?1", linksFound).stream()
                .map(RawReview::getUrl);
        final Stream<String> visitedReviews = reviewRepository.list("url in ?1", linksFound).stream()
                .map(Review::getUrl);
        final List<String> visitedLinks = Stream.concat(visitedRawReviews, visitedReviews).collect(Collectors.toList());

        log.infof("Found %d articles, %d of which are new", linksFound.size(), linksFound.size() - visitedLinks.size());

        return linksFound.stream()
                .filter(link -> !visitedLinks.contains(link))
                .map(link -> {
                    try {
                        return crawler.getReview(link);
                    } catch (Exception e) {
                        log.warnf("Failed to scrape article '%s'. Reason: %s", link, e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
