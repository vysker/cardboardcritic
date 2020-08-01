package com.cardboardcritic.db.entity

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@ToString(includeNames = true)
@EqualsAndHashCode
class Review extends Entity {
    int gameId, criticId, outletId
    int score
    String summary, link
    boolean recommended
}
