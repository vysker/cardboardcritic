package com.cardboardcritic.feed.crawler;

import com.cardboardcritic.db.entity.RawReview;
import com.cardboardcritic.feed.scraper.ArticleScraper;
import lombok.Data;
import org.jsoup.nodes.Document;

import java.util.List;

@Data
public abstract class OutletCrawler {
    private String outlet;
    private ArticleScraper scraper;

    public OutletCrawler(String outlet, ArticleScraper scraper) {
        this.scraper = scraper;
        this.outlet = outlet;
    }

    public abstract List<String> getArticleLinks();

    public RawReview getReview(String articleUrl) {
        Document document = scraper.fetch(articleUrl);
        RawReview review = scraper.getReview(articleUrl, document);
        review.setOutlet(outlet);
        review.setUrl(articleUrl);
        return review;
    }
}
