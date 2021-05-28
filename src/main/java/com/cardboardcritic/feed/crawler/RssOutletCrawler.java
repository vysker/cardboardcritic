package com.cardboardcritic.feed.crawler;

import com.cardboardcritic.feed.scraper.ArticleScraper;
import lombok.SneakyThrows;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.stream.IntStream;

public class RssOutletCrawler extends OutletCrawler {
    private final String feedUrl;

    RssOutletCrawler(String outlet, ArticleScraper scraper, String feedUrl) {
        super(outlet, scraper);
        this.feedUrl = feedUrl;
    }

    @SneakyThrows // FIXME bad, bad code
    @Override
    public List<String> getArticleLinks() {
        final HttpRequest request = HttpRequest.newBuilder().uri(URI.create(feedUrl)).build();
        final String rssRaw = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString()).body();

        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(true);
        factory.setIgnoringElementContentWhitespace(true);
        final DocumentBuilder parser = factory.newDocumentBuilder();
        final Document document = parser.parse(rssRaw);

        final NodeList links = document.getElementsByTagName("link");
        return IntStream.range(0, links.getLength())
                .mapToObj(links::item)
                .map(Node::getNodeValue)
                .toList();
    }
}
