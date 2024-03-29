package com.cardboardcritic.feed;

import com.cardboardcritic.db.entity.RawReview;
import com.cardboardcritic.db.entity.Review;
import com.cardboardcritic.db.repository.RawReviewRepository;
import com.cardboardcritic.db.repository.ReviewRepository;
import com.cardboardcritic.feed.crawler.OutletCrawler;
import io.quarkus.scheduler.Scheduled;
import org.jboss.logging.Logger;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
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

    @Scheduled(cron = "{cbc.feed.schedule}")
    void crawlOnSchedule() {
        crawl();
    }

    public boolean crawl() {
        log.info("Starting new review feed");

        final AtomicInteger reviewCount = new AtomicInteger();

        outletCrawlers.stream()
                .parallel()
                .flatMap(crawler -> crawl(crawler).stream()
                        .parallel()
                        .map(link -> getReview(crawler, link)))
                .filter(Objects::nonNull)
                .forEach(review -> {
                    reviewCount.incrementAndGet();
                    persist(review);
                });

        log.infof("Finished new review feed. Scraped %d new reviews", reviewCount.get());
        return true;
    }

    // This method is merely here for the @Transactional annotation
    @Transactional
    public void persist(RawReview rawReview) {
        rawReviewRepository.persist(rawReview);
    }

    @Transactional
    public List<String> crawl(OutletCrawler crawler) {
        log.infof("Crawling article links for '%s'", crawler.getOutlet());

        final List<String> linksFound = crawler.getArticleLinks().stream().distinct().toList();
        final List<String> visitedRawReviews = rawReviewRepository.visited(linksFound).stream()
                .map(RawReview::getUrl).toList();
        final List<String> visitedReviews = reviewRepository.visited(linksFound).stream()
                .map(Review::getUrl).toList();

        final List<String> visitedLinks = Stream.concat(visitedRawReviews.stream(), visitedReviews.stream())
                .distinct().toList();
        final List<String> newLinks = linksFound.stream()
                .filter(link -> !visitedLinks.contains(link))
                .toList();

        log.infof("Found %d articles, %d of which are new", linksFound.size(), newLinks.size());
        return newLinks;
    }

    public RawReview getReview(OutletCrawler crawler, String link) {
        try {
            log.infof("Visiting link: %s", link);
            return crawler.getReview(link);
        } catch (Exception e) {
            log.warnf("Failed to scrape article '%s'. Reason: %s", link, e);
            return null;
        }
    }

    public Optional<OutletCrawler> getCrawlerByOutlet(String outlet) {
        return outletCrawlers.stream()
                .filter(crawler -> crawler.getOutlet().equals(outlet))
                .findFirst();
    }
}
