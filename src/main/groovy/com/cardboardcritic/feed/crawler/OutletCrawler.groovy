package com.cardboardcritic.feed.crawler

import com.cardboardcritic.db.entity.RawReview
import com.cardboardcritic.feed.scraper.ArticleScraper
import org.jsoup.nodes.Document

abstract class OutletCrawler {
    String outlet
    ArticleScraper scraper

    OutletCrawler(String outlet, ArticleScraper scraper) {
        this.scraper = scraper
        this.outlet = outlet
    }

    abstract List<String> getArticleLinks()

    RawReview getReview(String articleUrl) {
        Document document = scraper.fetch articleUrl
        RawReview review = scraper.getReview articleUrl, document
        review.outlet = outlet
        review.url = articleUrl
        review
    }
}
