package com.cardboardcritic.feed.scraper;

import com.cardboardcritic.db.entity.RawReview;
import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Fetches the contents of an article.
 */
public abstract class ArticleScraper {

    public abstract RawReview getReview(String articleUrl, Document document);

    @SneakyThrows // FIXME bad, bad code
    public Document fetch(String articleUrl) {
        return Jsoup.connect(articleUrl).get();
    }
}
