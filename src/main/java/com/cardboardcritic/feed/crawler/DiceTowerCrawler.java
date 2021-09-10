package com.cardboardcritic.feed.crawler;

import com.cardboardcritic.feed.scraper.DiceTowerScraper;

import javax.inject.Singleton;

@Singleton
public class DiceTowerCrawler extends RssOutletCrawler {

    public DiceTowerCrawler(DiceTowerScraper scraper) {
        super("dicetower", scraper, "https://www.dicetower.com/feeds/videos.rss");
    }
}
