package com.cardboardcritic.feed.crawler;

import com.cardboardcritic.feed.scraper.EurogamerScraper;

import javax.inject.Singleton;

@Singleton
public class EurogamerCrawler extends RssOutletCrawler {

    public EurogamerCrawler(EurogamerScraper scraper) {
        super("Eurogamer", scraper, "https://www.eurogamer.net/?format=rss&platform=BOARDGAME");
    }
}
