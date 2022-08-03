package com.cardboardcritic.feed;

import com.cardboardcritic.db.entity.RawReview;
import com.cardboardcritic.db.entity.Review;
import com.cardboardcritic.db.repository.RawReviewRepository;
import com.cardboardcritic.db.repository.ReviewRepository;
import com.cardboardcritic.feed.crawler.OutletCrawler;
import com.cardboardcritic.feed.crawler.RssOutletCrawler;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

public class CrawlerServiceTest {
    Logger log = Logger.getLogger(CrawlerServiceTest.class);
    RawReviewRepository rawReviewRepository;
    ReviewRepository reviewRepository;
    OutletCrawler crawler;
    CrawlerService service;

    @BeforeEach
    public void setUp() {
        crawler = Mockito.mock(RssOutletCrawler.class);
        rawReviewRepository = Mockito.mock(RawReviewRepository.class);
        reviewRepository = Mockito.mock(ReviewRepository.class);
        service = new CrawlerService(List.of(crawler), rawReviewRepository, reviewRepository, log);
    }

    @Test
    public void crawl() {
        when(crawler.getArticleLinks())
                .thenReturn(List.of("abc", "jkl", "tuv", "xyz"));
        when(crawler.getReview(anyString()))
                .thenReturn(new RawReview().setUrl("tuv"))
                .thenReturn(new RawReview().setUrl("xyz"));
        when(rawReviewRepository.visited(anyList()))
                .thenReturn(List.of(new RawReview().setUrl("abc"), new RawReview().setUrl("jkl")));
        when(reviewRepository.visited(anyList()))
                .thenReturn(List.of(new Review().setUrl("abc")));

        service.crawl();

        Mockito.verify(crawler, times(1)).getReview("tuv");
        Mockito.verify(crawler, times(1)).getReview("xyz");
        Mockito.verify(rawReviewRepository, times(1)).persist(new RawReview().setUrl("tuv"));
        Mockito.verify(rawReviewRepository, times(1)).persist(new RawReview().setUrl("xyz"));
    }

    @Test
    public void crawl_withTimeout() {
        final var reviewWithTimeout = new RawReview().setUrl("tuv");
        final var reviewThatPasses = new RawReview().setUrl("xyz");

        when(crawler.getArticleLinks())
                .thenReturn(List.of("abc", "jkl", "tuv", "xyz"));
        when(crawler.getReview(anyString()))
                .thenReturn(reviewWithTimeout)
                .thenReturn(reviewThatPasses);
        when(rawReviewRepository.visited(anyList()))
                .thenReturn(List.of(new RawReview().setUrl("abc"), new RawReview().setUrl("jkl")));
        when(reviewRepository.visited(anyList()))
                .thenReturn(List.of(new Review().setUrl("abc")));

        service.crawl();

        Mockito.verify(crawler, times(1)).getReview("tuv");
        Mockito.verify(crawler, times(1)).getReview("xyz");
        Mockito.verify(rawReviewRepository, times(1)).persist(reviewWithTimeout);
        Mockito.verify(rawReviewRepository, times(1)).persist(reviewThatPasses);
    }
}
