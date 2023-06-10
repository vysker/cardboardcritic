package com.cardboardcritic.feed.crawler;

import com.cardboardcritic.feed.scraper.DiceTowerScraper;

import jakarta.inject.Singleton;

@Singleton
public class DiceTowerCrawler extends RssOutletCrawler {

    public DiceTowerCrawler(DiceTowerScraper scraper) {
        super("Dice Tower", scraper, "https://www.dicetower.com/feeds/videos.rss");
    }
}
