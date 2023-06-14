package com.cardboardcritic.feed.crawler;

import com.cardboardcritic.feed.DocumentFetcher;
import com.cardboardcritic.feed.scraper.SusdScraper;

import jakarta.inject.Singleton;
import org.jboss.logging.Logger;

/**
 * Shut Up & Sit Down
  */
@Singleton
public class SusdCrawler extends RssOutletCrawler {

    SusdCrawler(DocumentFetcher documentFetcher, SusdScraper scraper, Logger log) {
        super("Shut Up & Sit Down", documentFetcher, scraper, log, "https://www.shutupandsitdown.com/feed/?post_type=videos");
    }
}
