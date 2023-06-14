package com.cardboardcritic.feed.crawler;

import com.cardboardcritic.feed.DocumentFetcher;
import com.cardboardcritic.feed.ScrapeException;
import com.cardboardcritic.feed.scraper.DiceTowerScraper;
import jakarta.inject.Singleton;
import org.jboss.logging.Logger;

import java.util.List;

@Singleton
public class DiceTowerCrawler extends OutletCrawler {
    private static final String URL = "https://www.dicetower.com/dice-tower-reviews";

    public DiceTowerCrawler(DocumentFetcher documentFetcher, DiceTowerScraper scraper, Logger log) {
        super("Dice Tower", documentFetcher, scraper, log);
    }

    @Override
    public List<String> getArticleLinks() {
        try {
            return documentFetcher.fetch(URL)
                    .select(".ReviewCards__item .ReviewCards__title h2.heading a").eachAttr("href").stream()
                    .map(path -> "https://www.dicetower.com" + path)
                    .toList();
        } catch (ScrapeException e) {
            throw new ScrapeException("Failed to fetch article links for outlet '%s' from url '%s'. Because: %s"
                    .formatted(getOutlet(), URL, e));
        }
    }
}
