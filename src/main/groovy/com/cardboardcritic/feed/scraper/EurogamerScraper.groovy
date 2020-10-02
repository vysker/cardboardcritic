package com.cardboardcritic.feed.scraper

import com.cardboardcritic.db.entity.RawReview
import org.jsoup.nodes.Document

import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class EurogamerScraper extends ArticleScraper {

    @Override
    RawReview getReview(String articleUrl, Document document) {
        throw new UnsupportedOperationException('Not implemented, yet')
    }
}
