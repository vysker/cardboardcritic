package com.cardboardcritic.feed.crawler;

import com.cardboardcritic.feed.DocumentFetcher;
import com.cardboardcritic.feed.scraper.ArsArticleScraper;
import jakarta.inject.Singleton;
import org.jboss.logging.Logger;

@Singleton
public class ArsCrawler extends RssOutletCrawler {

    public ArsCrawler(DocumentFetcher documentFetcher, ArsArticleScraper scraper, Logger log) {
        super("Ars Technica", documentFetcher, scraper, log, "https://feeds.arstechnica.com/arstechnica/cardboard.rss");
    }
}
