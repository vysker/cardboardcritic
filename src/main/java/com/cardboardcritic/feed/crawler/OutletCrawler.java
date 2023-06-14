package com.cardboardcritic.feed.crawler;

import com.cardboardcritic.db.entity.RawReview;
import com.cardboardcritic.feed.DocumentFetcher;
import com.cardboardcritic.feed.scraper.ArticleScraper;
import org.jboss.logging.Logger;
import org.jsoup.nodes.Document;

import java.util.List;

/**
 * A crawler finds all relevant (review) links on a website.
 */
public abstract class OutletCrawler {
    protected final String outlet;
    protected final DocumentFetcher documentFetcher;
    protected final ArticleScraper scraper;
    protected final Logger log;

    public OutletCrawler(String outlet, DocumentFetcher documentFetcher, ArticleScraper scraper, Logger log) {
        this.documentFetcher = documentFetcher;
        this.scraper = scraper;
        this.outlet = outlet;
        this.log = log;
    }

    public abstract List<String> getArticleLinks();

    /**
     * Scrape the review content.
     *
     * @param articleUrl Link to the review page to be scraped
     * @return The parsed raw review
     * @implNote The reason that, specifically, this class contains this method, is that there might be certain outlets
     * that require different scrapers depending on the kind of article. The crawler could then infer which scraper is
     * appropriate.
     */
    public RawReview getReview(String articleUrl) {
        try {
            final Document document = documentFetcher.fetch(articleUrl);
            return scraper.getReview(articleUrl, document);
        } catch (Exception e) {
            log.error("Failed to get review '%s'. Reason: %s", articleUrl, e);
            return null;
        }
    }

    public String getOutlet() {
        return outlet;
    }
}
