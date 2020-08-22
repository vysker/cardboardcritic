package com.cardboardcritic.db.entity

import com.cardboardcritic.db.entity.meta.HasName
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import io.quarkus.hibernate.orm.panache.PanacheEntityBase

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany
import java.time.LocalDate

@ToString(includeNames = true)
@EqualsAndHashCode
@Entity
class Game extends PanacheEntityBase implements HasName {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id

    int score, recommended
    String name, shortDescription, description, designer, slug
    LocalDate releaseDate

    @OneToMany(mappedBy = 'game')
    List<Review> reviews
}
