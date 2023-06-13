package com.cardboardcritic.feed.crawler;

import com.cardboardcritic.feed.ScrapeException;
import com.cardboardcritic.feed.scraper.DiceTowerScraper;
import io.vertx.mutiny.ext.web.client.HttpResponse;
import io.vertx.mutiny.ext.web.client.WebClient;
import io.vertx.mutiny.ext.web.codec.BodyCodec;
import jakarta.inject.Singleton;
import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;

import java.util.List;

@Singleton
public class DiceTowerCrawler extends OutletCrawler {
    private final String url = "https://www.dicetower.com/dice-tower-reviews";
    private final WebClient webClient;

    public DiceTowerCrawler(WebClient webClient, DiceTowerScraper scraper) {
        super("Dice Tower", scraper);
        this.webClient = webClient;
    }

    @Override
    public List<String> getArticleLinks() {
        return webClient.getAbs(url).as(BodyCodec.string()).send()
                .map(HttpResponse::body)
                .map(body -> Jsoup.parse(body, "", Parser.xmlParser())
                        .select(".ReviewCards__item .ReviewCards__title h2.heading a").eachAttr("href").stream()
                        .map(path -> "https://www.dicetower.com" + path)
                        .toList())
                .onFailure().transform(e ->
                        new ScrapeException("Failed to fetch article links for outlet '%s' from url '%s'. Because: %s"
                                .formatted(getOutlet(), url, e)))
                .await().indefinitely();
    }
}
