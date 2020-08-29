package com.cardboardcritic.web.template.data

import com.cardboardcritic.db.entity.RawReview
import groovy.transform.ToString

@ToString(includeNames = true)
class RawReviewListData {
    List<RawReview> rawReviews
}
