package com.cardboardcritic.db.entity

import com.cardboardcritic.db.entity.meta.Column
import com.cardboardcritic.db.entity.meta.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@ToString(includeNames = true)
@EqualsAndHashCode
class Review extends Entity {
    @Column int gameId, criticId, outletId
    @Column int score
    @Column String summary, link
    @Column boolean recommended
}
