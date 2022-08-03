package com.cardboardcritic.feed.crawler;

import com.cardboardcritic.feed.scraper.ArsArticleScraper;

import javax.inject.Singleton;

@Singleton
public class ArsCrawler extends RssOutletCrawler {

    public ArsCrawler(ArsArticleScraper scraper) {
        super("Ars Technica", scraper, "https://feeds.arstechnica.com/arstechnica/cardboard.rss");
    }
}
