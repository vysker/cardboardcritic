package com.cardboardcritic.db.entity

import com.cardboardcritic.db.entity.meta.Column
import com.cardboardcritic.db.entity.meta.Entity
import com.cardboardcritic.db.entity.meta.HasName
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import java.time.LocalDate

@ToString(includeNames = true)
@EqualsAndHashCode
class Game extends Entity implements HasName {
    @Column int score, recommended
    @Column String shortDescription, description, designer
    @Column LocalDate releaseDate
}
