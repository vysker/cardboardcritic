package com.cardboardcritic.feed;

import com.cardboardcritic.db.repository.RawReviewRepository;
import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;

/**
 * Maintains the feed of reviews. Which is to say, orders all crawlers to find new reviews, and persists them.
 */
@ApplicationScoped
public record FeedService(CrawlerService crawlerService,
                          RawReviewRepository rawReviewRepository,
                          Logger log) {

//    @Scheduled(every = "2S") // 1D = every day
    @ReactiveTransactional
    public Uni<Void> refresh() {
        log.info("Starting new review feed");
        return crawlerService.crawl();
//        log.infof("Finished feed. Scraped %d new reviews", reviews.size());
    }
}
