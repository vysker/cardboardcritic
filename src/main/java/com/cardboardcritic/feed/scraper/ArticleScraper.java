package com.cardboardcritic.feed.scraper;

import com.cardboardcritic.db.entity.RawReview;
import com.cardboardcritic.feed.ScrapeException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Fetches the contents of an article.
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
