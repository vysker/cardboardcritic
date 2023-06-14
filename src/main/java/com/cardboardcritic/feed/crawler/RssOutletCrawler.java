package com.cardboardcritic.feed.crawler;

import com.cardboardcritic.feed.DocumentFetcher;
import com.cardboardcritic.feed.ScrapeException;
import com.cardboardcritic.feed.scraper.ArticleScraper;
import org.jboss.logging.Logger;

import java.util.List;

public class RssOutletCrawler extends OutletCrawler {
    private final String feedUrl;

    public RssOutletCrawler(String outlet,
                            DocumentFetcher documentFetcher,
                            ArticleScraper scraper,
                            Logger log,
                            String feedUrl) {
        super(outlet, documentFetcher, scraper, log);
        this.feedUrl = feedUrl;
    }

    @Override
    public List<String> getArticleLinks() {
        try {
            return documentFetcher.fetchXml(feedUrl).select("rss channel item link").eachText();
        } catch (ScrapeException e) {
            throw new ScrapeException("Failed to retrieve article links for outlet '%s' using feed url '%s', because: %s"
                    .formatted(getOutlet(), feedUrl, e));
        }
    }
}
