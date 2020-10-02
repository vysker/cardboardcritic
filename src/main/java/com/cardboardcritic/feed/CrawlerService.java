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
public class CrawlerService {
    private final List<OutletCrawler> outletCrawlers;
    private final ReviewRepository reviewRepository;

    public CrawlerService(@Named("outletCrawlers") List<OutletCrawler> outletCrawlers,
                          ReviewRepository reviewRepository) {
        this.outletCrawlers = outletCrawlers;
        this.reviewRepository = reviewRepository;
    }

    List<RawReview> getNewReviews() {
        return outletCrawlers.stream()
                .map(this::getNewReviews)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    List<RawReview> getNewReviews(OutletCrawler crawler) {
        List<String> articleLinks = crawler.getArticleLinks();
        List<String> existing = reviewRepository.list("url in ?1", articleLinks).stream()
                .map(Review::getUrl)
                .collect(Collectors.toList());
        articleLinks.removeAll(existing);

        return articleLinks.stream()
                .map(crawler::getReview)
                .collect(Collectors.toList());
    }
}
