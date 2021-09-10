package com.cardboardcritic.feed.crawler;

import com.cardboardcritic.feed.ScrapeException;
import com.cardboardcritic.feed.scraper.BoardGameQuestScraper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import javax.inject.Singleton;
import java.io.IOException;
import java.util.List;

@Singleton
public class BoardGameQuestCrawler extends OutletCrawler {
    private final String url = "https://www.boardgamequest.com/reviews-alphabetically";

    public BoardGameQuestCrawler(BoardGameQuestScraper scraper) {
        super("boardgamequest", scraper);
    }

    @Override
    public List<String> getArticleLinks() {
        final Document document;
        try {
            document = Jsoup.connect(url).get();
        } catch (IOException e) {
            throw new ScrapeException("Failed to fetch article links for outlet '%s' from url '%s'"
                    .formatted(getOutlet(), url));
        }

        final Elements articles = document.select("div.letter-section ul li a");
        return articles.eachAttr("href");
    }
}
