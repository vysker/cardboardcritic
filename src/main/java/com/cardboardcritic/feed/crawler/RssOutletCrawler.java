package com.cardboardcritic.feed.crawler;

import com.cardboardcritic.feed.scraper.ArticleScraper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class RssOutletCrawler extends OutletCrawler {
    private final String feedUrl;

    RssOutletCrawler(String outlet, ArticleScraper scraper, String feedUrl) {
        super(outlet, scraper);
        this.feedUrl = feedUrl;
    }

    @Override
    public List<String> getArticleLinks() {
        try {
            final URI feedUri = URI.create(feedUrl);
            final HttpRequest request = HttpRequest.newBuilder().uri(feedUri).build();
            final String rssRaw = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString()).body();

            final Document document = Jsoup.parse(rssRaw, "", Parser.xmlParser());
            return document.select("rss channel item link").eachText();
        } catch (Exception e) {
            final var message = "Failed to retrieve article links for outlet '%s' using feed url '%s', because: %s"
                    .formatted(getOutlet(), feedUrl, e.getMessage());
            throw new RuntimeException(message);
        }
    }
}
