package com.cardboardcritic.feed.crawler;

import com.cardboardcritic.feed.ScrapeException;
import com.cardboardcritic.feed.scraper.ArticleScraper;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.ext.web.client.HttpResponse;
import io.vertx.mutiny.ext.web.client.WebClient;
import io.vertx.mutiny.ext.web.codec.BodyCodec;
import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;

import javax.inject.Inject;
import java.util.List;

public class RssOutletCrawler extends OutletCrawler {
    private final String feedUrl;

    @Inject
    WebClient webClient;

    RssOutletCrawler(String outlet, ArticleScraper scraper, String feedUrl) {
        super(outlet, scraper);
        this.feedUrl = feedUrl;
    }

    @Override
    public Uni<List<String>> getArticleLinks() {
        return webClient.getAbs(feedUrl).as(BodyCodec.string()).send()
                .map(HttpResponse::body)
                .invoke(x -> System.out.println("sup"))
                .map(body -> Jsoup.parse(body, "", Parser.xmlParser())
                        .select("rss channel item link").eachText())
//                .onItem().transformToMulti(urls -> Multi.createFrom().iterable(urls))
                .onFailure().transform(e ->
                        new ScrapeException("Failed to retrieve article links for outlet '%s' using feed url '%s', because: %s"
                                .formatted(getOutlet(), feedUrl, e)));
    }
}
