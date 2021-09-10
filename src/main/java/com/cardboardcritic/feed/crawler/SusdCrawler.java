package com.cardboardcritic.feed.crawler;

import com.cardboardcritic.feed.scraper.SusdScraper;

import javax.inject.Singleton;

/**
 * Shut Up and Sit Down
  */
@Singleton
public class SusdCrawler extends RssOutletCrawler {

    SusdCrawler(SusdScraper scraper) {
        super("susd", scraper, "https://www.shutupandsitdown.com/feed/?post_type=games");
    }
}
