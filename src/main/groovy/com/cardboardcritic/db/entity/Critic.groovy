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

@ToString(includeNames = true)
@EqualsAndHashCode
@Entity
class Critic extends PanacheEntityBase implements HasName {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id

    String name

    @OneToMany(mappedBy = 'critic')
    List<Review> reviews
}
