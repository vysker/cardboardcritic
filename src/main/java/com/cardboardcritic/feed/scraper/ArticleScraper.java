package com.cardboardcritic.feed.scraper;

import com.cardboardcritic.db.entity.RawReview;
import com.cardboardcritic.feed.ScrapeException;
import org.jboss.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.inject.Inject;

/**
 * A scraper fetches the contents of an article (review), then parses and converts that content to a "raw review".
 */
public abstract class ArticleScraper {

    @Inject
    Logger log;

    public abstract RawReview getReview(String articleUrl, Document document) throws ScrapeException;

    public Document fetch(String articleUrl) throws ScrapeException {
        try {
            log.infof("Fetching article content from url '%s'", articleUrl);
            return Jsoup.connect(articleUrl).get();
        } catch (Exception e) {
            throw new ScrapeException(
                    "Failed to fetch article content from url '%s'. Reason: %s".formatted(articleUrl, e));
        }
    }
}
