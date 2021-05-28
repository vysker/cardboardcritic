package com.cardboardcritic.feed.scraper;

import com.cardboardcritic.db.entity.RawReview;
import org.jsoup.nodes.Document;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EurogamerScraper extends ArticleScraper {

    @Override
    public RawReview getReview(String articleUrl, Document document) {
        throw new UnsupportedOperationException("Not implemented, yet");
    }
}
