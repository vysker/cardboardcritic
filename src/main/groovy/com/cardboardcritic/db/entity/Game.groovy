package com.cardboardcritic.db.entity

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import java.time.LocalDate

@ToString(includeNames = true)
@EqualsAndHashCode
class Game extends Entity {
    int id, score, recommended
    String name, shortDescription, description, designer
    LocalDate releaseDate
}
