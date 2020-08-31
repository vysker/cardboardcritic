package com.cardboardcritic.feed.scraper

import com.cardboardcritic.db.entity.RawReview
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

abstract class ArticleScraper {

    abstract RawReview getReview(String articleUrl, Document document)

    Document fetch(String articleUrl) {
        Jsoup.connect(articleUrl).get()
    }
}
