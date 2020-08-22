package com.cardboardcritic.domain

import com.cardboardcritic.db.entity.Outlet
import groovy.transform.ToString

import java.time.LocalDateTime

@ToString(includeNames = true)
class RawReview {
    String url, title, game, critic
    Outlet outlet
    LocalDateTime date
    List<String> paragraphs // usually the full review content, but not always the case, i.e. when review is paginated
    List<String> suggestedSummaries
    int score
    boolean recommended
}
