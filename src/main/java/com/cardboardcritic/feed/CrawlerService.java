package com.cardboardcritic.feed;

import com.cardboardcritic.db.entity.RawReview;
import com.cardboardcritic.db.entity.Review;
import com.cardboardcritic.db.repository.ReviewRepository;
import com.cardboardcritic.feed.crawler.OutletCrawler;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Responsible for crawling news outlets for reviews.
 */
@ApplicationScoped
public record CrawlerService(List<OutletCrawler> outletCrawlers,
                             ReviewRepository reviewRepository) {

    public CrawlerService(@Named("outletCrawlers") List<OutletCrawler> outletCrawlers,
                          ReviewRepository reviewRepository) {
        this.outletCrawlers = outletCrawlers;
        this.reviewRepository = reviewRepository;
    }

    public List<RawReview> getNewReviews() {
        return outletCrawlers.stream()
                .map(this::getNewReviews)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public List<RawReview> getNewReviews(OutletCrawler crawler) {
        final List<String> articleLinks = crawler.getArticleLinks();
        final List<String> existing = reviewRepository.list("url in ?1", articleLinks).stream()
                .map(Review::getUrl)
                .collect(Collectors.toList());

        return articleLinks.stream()
                .filter(link -> !existing.contains(link))
                .map(crawler::getReview)
                .collect(Collectors.toList());
    }
}
