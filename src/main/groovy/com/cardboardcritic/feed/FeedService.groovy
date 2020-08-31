package com.cardboardcritic.feed

import com.cardboardcritic.db.entity.RawReview
import com.cardboardcritic.db.repository.RawReviewRepository

import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.transaction.Transactional

@ApplicationScoped
class FeedService {

    @Inject
    CrawlerService crawlerService

    @Inject
    RawReviewRepository rawReviewRepository

    @Transactional
    void refresh() {
        List<RawReview> reviews = crawlerService.newReviews
        rawReviewRepository.persist reviews
    }
}
