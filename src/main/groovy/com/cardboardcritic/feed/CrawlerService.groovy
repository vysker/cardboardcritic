package com.cardboardcritic.feed

import com.cardboardcritic.db.entity.RawReview
import com.cardboardcritic.db.repository.ReviewRepository
import com.cardboardcritic.feed.crawler.OutletCrawler

import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
class CrawlerService {
    List<OutletCrawler> outletCrawlers

    @Inject
    ReviewRepository reviewRepository

    List<RawReview> getNewReviews() {
        List<List<RawReview>> reviews = outletCrawlers.collect { getNewReviews it }
        reviews.flatten() as List<RawReview>
    }

    List<RawReview> getNewReviews(OutletCrawler crawler) {
        List<String> articleLinks = crawler.articleLinks
        List<String> existing = reviewRepository.list('url in :urls', articleLinks)*.url
        List<String> newArticles = articleLinks - existing

        newArticles.collect { crawler.getReview it }
    }
}
