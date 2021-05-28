package com.cardboardcritic.feed.crawler;

import com.cardboardcritic.db.entity.RawReview;
import com.cardboardcritic.feed.scraper.ArticleScraper;
import org.jsoup.nodes.Document;

import java.util.List;

public abstract class OutletCrawler {
    private String outlet;
    private ArticleScraper scraper;

    public OutletCrawler(String outlet, ArticleScraper scraper) {
        this.scraper = scraper;
        this.outlet = outlet;
    }

    public abstract List<String> getArticleLinks();

    public RawReview getReview(String articleUrl) {
        final Document document = scraper.fetch(articleUrl);
        final RawReview review = scraper.getReview(articleUrl, document);
        review.setOutlet(outlet);
        review.setUrl(articleUrl);
        return review;
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
