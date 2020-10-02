package com.cardboardcritic.feed;

import com.cardboardcritic.db.entity.RawReview;
import com.cardboardcritic.db.repository.RawReviewRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

/**
 * Maintains the feed of reviews. Which is to say, orders all crawlers to find new reviews, and persists them.
 */
@ApplicationScoped
public class FeedService {

    @Inject
    CrawlerService crawlerService;

    @Inject
    RawReviewRepository rawReviewRepository;

    @Transactional
    public void refresh() {
        List<RawReview> reviews = crawlerService.getNewReviews();
        rawReviewRepository.persist(reviews);
    }
}
