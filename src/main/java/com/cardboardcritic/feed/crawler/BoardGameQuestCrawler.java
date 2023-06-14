package com.cardboardcritic.feed.crawler;

import com.cardboardcritic.feed.DocumentFetcher;
import com.cardboardcritic.feed.ScrapeException;
import com.cardboardcritic.feed.scraper.BoardGameQuestScraper;
import jakarta.inject.Singleton;
import org.jboss.logging.Logger;

import java.util.List;

@Singleton
public class BoardGameQuestCrawler extends OutletCrawler {
    private static final String URL = "https://www.boardgamequest.com/reviews-alphabetically";

    public BoardGameQuestCrawler(DocumentFetcher documentFetcher, BoardGameQuestScraper scraper, Logger log) {
        super("Board Game Quest", documentFetcher, scraper, log);
    }

    @Override
    public List<String> getArticleLinks() {
        try {
            return documentFetcher.fetch(URL)
                    .select("div.letter-section ul li a")
                    .eachAttr("href");
        } catch (ScrapeException e) {
            throw new ScrapeException("Failed to fetch article links for outlet '%s' from url '%s'. Because: %s"
                    .formatted(getOutlet(), URL, e));
        }
    }
}
