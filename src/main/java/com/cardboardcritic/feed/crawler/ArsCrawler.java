package com.cardboardcritic.feed.crawler;

import com.cardboardcritic.feed.scraper.ArsArticleScraper;

import javax.inject.Singleton;

@Singleton
public class ArsCrawler extends RssOutletCrawler {

    public ArsCrawler(ArsArticleScraper scraper) {
        super("Ars Technica", scraper, "https://feeds.arstechnica.com/arstechnica/cardboard.rss");
    }

//    @Override
//    public List<String> getArticleLinks() {
//        return List.of(
//                "http://localhost:9090/a",
//                "http://localhost:9090/b",
//                "http://localhost:9090/c",
//                "http://localhost:9090/d",
//                "http://localhost:9090/e",
//                "http://localhost:9090/f",
//                "http://localhost:9090/g"
//        );
//    }
}
