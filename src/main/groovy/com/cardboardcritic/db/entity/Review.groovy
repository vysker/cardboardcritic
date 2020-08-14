package com.cardboardcritic.db.entity

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import io.quarkus.hibernate.orm.panache.PanacheEntityBase

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@ToString(includeNames = true)
@EqualsAndHashCode
@Entity
class Review extends PanacheEntityBase {
    @Id @GeneratedValue Long id
    int gameId, criticId, outletId
    int score
    String summary, link
    boolean recommended
}
