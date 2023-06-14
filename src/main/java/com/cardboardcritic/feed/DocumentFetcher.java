package com.cardboardcritic.feed;

import io.vertx.mutiny.ext.web.client.HttpResponse;
import io.vertx.mutiny.ext.web.client.WebClient;
import io.vertx.mutiny.ext.web.codec.BodyCodec;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.util.function.Function;

@ApplicationScoped
public class DocumentFetcher {
    private final WebClient webClient;
    private final Logger log;

    public DocumentFetcher(WebClient webClient, Logger log) {
        this.webClient = webClient;
        this.log = log;
    }

    public Document fetch(String articleUrl) throws ScrapeException {
        return fetch(articleUrl, Jsoup::parse);
    }

    public Document fetchXml(String articleUrl) throws ScrapeException {
        return fetch(articleUrl, (String html) -> Jsoup.parse(html, "", Parser.xmlParser()));
    }

    private Document fetch(String articleUrl, Function<String, Document> parseFunction) throws ScrapeException {
        try {
            log.infof("Fetching article content from url '%s'", articleUrl);
            return webClient.getAbs(articleUrl)
                    .as(BodyCodec.string())
                    .send()
                    .map(HttpResponse::body)
                    .map(parseFunction)
                    .await().indefinitely();
        } catch (Exception e) {
            throw new ScrapeException(
                    "Failed to fetch article content from url '%s'. Reason: %s".formatted(articleUrl, e));
        }
    }
}
