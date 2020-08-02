package com.cardboardcritic.db.entity

import com.cardboardcritic.domain.HasName
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import java.time.LocalDate

@ToString(includeNames = true)
@EqualsAndHashCode
class Game extends Entity implements HasName {
    int id, score, recommended
    String shortDescription, description, designer
    LocalDate releaseDate
}
