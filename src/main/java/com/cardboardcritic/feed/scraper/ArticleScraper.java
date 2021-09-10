package com.cardboardcritic.feed.scraper;

import com.cardboardcritic.db.entity.RawReview;
import com.cardboardcritic.feed.ScrapeException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * A scraper fetches the contents of an article (review), then parses and converts that content to a "raw review".
 */
public abstract class ArticleScraper {

    public abstract RawReview getReview(String articleUrl, Document document) throws ScrapeException;

    public Document fetch(String articleUrl) throws ScrapeException {
        try {
            return Jsoup.connect(articleUrl).get();
        } catch (Exception e) {
            throw new ScrapeException("Failed to fetch article content from url '%s'".formatted(articleUrl));
        }
    }
}
