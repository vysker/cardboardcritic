package com.cardboardcritic.db.entity

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import io.quarkus.hibernate.orm.panache.PanacheEntityBase

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@ToString(includeNames = true)
@EqualsAndHashCode
@Entity
@Table(name = 'raw_review')
class RawReview extends PanacheEntityBase {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id

    String game, critic, outlet, summary, url, title, content, date
    int score
    boolean recommended, processed
}
