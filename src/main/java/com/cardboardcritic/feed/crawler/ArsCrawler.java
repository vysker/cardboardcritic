package com.cardboardcritic.feed.crawler;

import com.cardboardcritic.feed.scraper.ArsArticleScraper;

import javax.inject.Singleton;

@Singleton
public class ArsCrawler extends RssOutletCrawler {

    public ArsCrawler(ArsArticleScraper scraper) {
        super("ars", scraper, "http://feeds.arstechnica.com/arstechnica/cardboard.rss");
    }
}
