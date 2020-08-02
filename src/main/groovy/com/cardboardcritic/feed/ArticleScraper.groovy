package com.cardboardcritic.feed

import com.cardboardcritic.domain.RawReview
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

abstract class ArticleScraper {
    protected String articleUrl

    abstract RawReview getReview(String articleUrl, Document document)

    RawReview getReview(String articleUrl) {
        getReview articleUrl, fetch(articleUrl)
    }

    RawReview getReview(Document document) {
        getReview null, document
    }

    Document fetch(String url) {
        println "Visiting: $url"
        Jsoup.connect(url).get()
    }
}
