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
