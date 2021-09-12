package com.cardboardcritic.feed;

import com.cardboardcritic.db.entity.RawReview;
import com.cardboardcritic.db.repository.RawReviewRepository;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.List;

/**
 * Maintains the feed of reviews. Which is to say, orders all crawlers to find new reviews, and persists them.
 */
@ApplicationScoped
public record FeedService(CrawlerService crawlerService,
                          RawReviewRepository rawReviewRepository,
                          Logger log) {

    //    @Scheduled(every = "2S") // 1D = every day
    @Transactional
    public void refresh() {
        log.info("Starting new review feed");
        final List<RawReview> reviews = crawlerService.getNewReviews();
        rawReviewRepository.persist(reviews);
        log.infof("Finished feed. Scraped %d new reviews", reviews.size());
    }
}
