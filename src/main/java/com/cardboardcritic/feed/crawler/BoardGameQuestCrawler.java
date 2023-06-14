package com.cardboardcritic.feed.crawler;

import com.cardboardcritic.feed.ScrapeException;
import com.cardboardcritic.feed.scraper.BoardGameQuestScraper;
import io.vertx.mutiny.ext.web.client.HttpResponse;
import io.vertx.mutiny.ext.web.client.WebClient;
import io.vertx.mutiny.ext.web.codec.BodyCodec;
import jakarta.inject.Singleton;
import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;

import java.util.List;

@Singleton
public class BoardGameQuestCrawler extends OutletCrawler {
    private final String url = "https://www.boardgamequest.com/reviews-alphabetically";
    private final WebClient webClient;

    public BoardGameQuestCrawler(WebClient webClient, BoardGameQuestScraper scraper) {
        super("Board Game Quest", scraper);
        this.webClient = webClient;
    }

    @Override
    public List<String> getArticleLinks() {
        return webClient.getAbs(url).as(BodyCodec.string()).send()
                .map(HttpResponse::body)
                .map(body -> Jsoup.parse(body, "", Parser.xmlParser())
                        .select("div.letter-section ul li a").eachAttr("href"))
                .onFailure().transform(e ->
                        new ScrapeException("Failed to fetch article links for outlet '%s' from url '%s'. Because: %s"
                                .formatted(getOutlet(), url, e)))
                .await().indefinitely();
    }
}
