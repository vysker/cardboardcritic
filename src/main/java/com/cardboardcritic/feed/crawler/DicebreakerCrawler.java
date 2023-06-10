package com.cardboardcritic.feed.crawler;

import com.cardboardcritic.feed.ScrapeException;
import com.cardboardcritic.feed.scraper.DicebreakerScraper;
import io.vertx.mutiny.ext.web.client.HttpResponse;
import io.vertx.mutiny.ext.web.client.WebClient;
import io.vertx.mutiny.ext.web.codec.BodyCodec;
import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.util.List;

@Singleton
public class DicebreakerCrawler extends OutletCrawler {
    private final String url = "https://www.dicebreaker.com/archive/review";

    @Inject
    WebClient webClient;

    public DicebreakerCrawler(DicebreakerScraper scraper) {
        super("Dicebreaker", scraper);
    }

    @Override
    public List<String> getArticleLinks() {
        return webClient.getAbs(url).as(BodyCodec.string()).send()
                .map(HttpResponse::body)
                .map(body -> Jsoup.parse(body, "", Parser.xmlParser())
                        .select("article[data-article-type=review] a").eachAttr("href"))
                .onFailure().transform(e ->
                        new ScrapeException("Failed to fetch article links for outlet '%s' from url '%s'. Because: %s"
                                .formatted(getOutlet(), url, e)))
                .await().indefinitely();
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
