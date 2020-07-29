package com.cardboardcritic.feed

import com.cardboardcritic.domain.Review
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

abstract class ArticleScraper {
    protected String articleUrl

    abstract Review getReview(String articleUrl, Document document)

    Review getReview(String articleUrl) {
        return getReview(articleUrl, fetch(articleUrl))
    }

    Review getReview(Document document) {
        return getReview(null, document)
    }

    Document fetch(String url) {
        println "Visiting: $url"
        Jsoup.connect(url).get()
    }
}
