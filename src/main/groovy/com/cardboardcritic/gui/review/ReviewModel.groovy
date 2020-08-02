package com.cardboardcritic.gui.review

import com.cardboardcritic.db.entity.Outlet
import groovy.beans.Bindable

class ReviewModel {
    @Bindable String url, title, game, critic
    @Bindable Outlet outlet
    @Bindable String date
    @Bindable List<String> paragraphs // usually the full review content, but not always the case, i.e. when review is paginated
    @Bindable List<String> suggestedSummaries
    @Bindable int score
    @Bindable boolean recommended
    @Bindable int chosenSummary
}
