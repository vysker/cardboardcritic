package com.cardboardcritic.feed.crawler;

import com.cardboardcritic.feed.ScrapeException;
import com.cardboardcritic.feed.scraper.DicebreakerScraper;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.ext.web.client.HttpResponse;
import io.vertx.mutiny.ext.web.client.WebClient;
import io.vertx.mutiny.ext.web.codec.BodyCodec;
import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;

import javax.inject.Inject;
import javax.inject.Singleton;
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
    public Uni<List<String>> getArticleLinks() {
        return webClient.getAbs(url).as(BodyCodec.string()).send()
                .map(HttpResponse::body)
                .map(body -> Jsoup.parse(body, "", Parser.xmlParser())
                        .select("article[data-article-type=review] a").eachAttr("href"))
                .onFailure().transform(e ->
                        new ScrapeException("Failed to fetch article links for outlet '%s' from url '%s'. Because: %s"
                                .formatted(getOutlet(), url, e)));
    }
}
