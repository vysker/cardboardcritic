package com.cardboardcritic.feed;

import com.cardboardcritic.db.entity.RawReview;
import com.cardboardcritic.db.entity.Review;
import com.cardboardcritic.feed.crawler.OutletCrawler;
import com.cardboardcritic.feed.crawler.RssOutletCrawler;
import com.cardboardcritic.service.RawReviewService;
import com.cardboardcritic.service.ReviewService;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
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
    RawReviewService rawReviewService;
    ReviewService reviewService;
    OutletCrawler crawler;
    CrawlerService service;

    @BeforeEach
    public void setUp() {
        crawler = Mockito.mock(RssOutletCrawler.class);
        rawReviewService = Mockito.mock(RawReviewService.class);
        reviewService = Mockito.mock(ReviewService.class);
        service = new CrawlerService(List.of(crawler), rawReviewService, reviewService, log);
    }

    @Test
    public void crawl() {
        when(crawler.getArticleLinks())
                .thenReturn(uni(List.of("abc", "jkl", "tuv", "xyz")));
        when(crawler.getReview(anyString()))
                .thenReturn(new RawReview().setUrl("tuv"))
                .thenReturn(new RawReview().setUrl("xyz"));
        when(rawReviewService.visited(anyList()))
                .thenReturn(uni(List.of(new RawReview().setUrl("abc"), new RawReview().setUrl("jkl"))));
        when(reviewService.visited(anyList()))
                .thenReturn(uni(List.of(new Review().setUrl("abc"))));

        service.crawl(crawler)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .assertCompleted()
                .getItem();

        Mockito.verify(crawler, times(1)).getReview("tuv");
        Mockito.verify(crawler, times(1)).getReview("xyz");
        Mockito.verify(rawReviewService, times(1)).persist(new RawReview().setUrl("tuv"));
        Mockito.verify(rawReviewService, times(1)).persist(new RawReview().setUrl("xyz"));
    }

    // Just a helper method
    <T> Uni<T> uni(T t) {
        return Uni.createFrom().item(t);
    }
}
