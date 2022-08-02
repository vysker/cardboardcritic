package com.cardboardcritic.feed.crawler;

import com.cardboardcritic.feed.scraper.SusdScraper;

import javax.inject.Singleton;

/**
 * Shut Up & Sit Down
  */
@Singleton
public class SusdCrawler extends RssOutletCrawler {

    SusdCrawler(SusdScraper scraper) {
        super("Shut Up & Sit Down", scraper, "https://www.shutupandsitdown.com/feed/?post_type=games");
    }
}
