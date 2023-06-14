package com.cardboardcritic.feed.crawler;

import com.cardboardcritic.feed.DocumentFetcher;
import com.cardboardcritic.feed.ScrapeException;
import com.cardboardcritic.feed.scraper.DicebreakerScraper;
import jakarta.inject.Singleton;
import org.jboss.logging.Logger;

import java.util.List;

@Singleton
public class DicebreakerCrawler extends OutletCrawler {
    private static final String URL = "https://www.dicebreaker.com/archive/review";

    public DicebreakerCrawler(DocumentFetcher documentFetcher, DicebreakerScraper scraper, Logger log) {
        super("Dicebreaker", documentFetcher, scraper, log);
    }

    @Override
    public List<String> getArticleLinks() {
        try {
            return documentFetcher.fetch(URL)
                    .select("div[data-article-type=review] .details a")
                    .eachAttr("href");
        } catch (ScrapeException e) {
            throw new ScrapeException("Failed to fetch article links for outlet '%s' from url '%s'. Because: %s"
                    .formatted(getOutlet(), URL, e));
        }
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
