package com.cardboardcritic.feed.crawler;

import com.cardboardcritic.db.entity.RawReview;
import com.cardboardcritic.feed.scraper.ArticleScraper;
import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;

import java.util.List;

/**
 * A crawler finds all relevant (review) links on a website.
 */
public abstract class OutletCrawler {
    private static final Logger log = Logger.getLogger(OutletCrawler.class);

    private String outlet;
    private ArticleScraper scraper;

    public OutletCrawler(String outlet, ArticleScraper scraper) {
        this.scraper = scraper;
        this.outlet = outlet;
    }

    public abstract Uni<List<String>> getArticleLinks();

    public Uni<RawReview> getReview(String articleUrl) {
        return scraper.fetch(articleUrl)
                .flatMap(document -> scraper.getReview(articleUrl, document))
                .onFailure().recoverWithUni(error -> {
                    log.error("Failed to get review '%s'. Reason: %s", articleUrl, error);
                    return Uni.createFrom().nullItem();
                })
                .onItem().ifNotNull().transform(review -> {
                    review.setOutlet(outlet);
                    review.setUrl(articleUrl);
                    return review;
                });
    }

    public String getOutlet() {
        return outlet;
    }

    public OutletCrawler setOutlet(String outlet) {
        this.outlet = outlet;
        return this;
    }

    public ArticleScraper getScraper() {
        return scraper;
    }

    public OutletCrawler setScraper(ArticleScraper scraper) {
        this.scraper = scraper;
        return this;
    }
}
