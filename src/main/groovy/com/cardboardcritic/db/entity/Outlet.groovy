package com.cardboardcritic.db.entity

import com.cardboardcritic.db.entity.meta.HasName
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import io.quarkus.hibernate.orm.panache.PanacheEntityBase

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToMany

@ToString(includeNames = true)
@EqualsAndHashCode
@Entity
class Outlet extends PanacheEntityBase implements HasName {
    @Id @GeneratedValue Long id

    String name, website

    @OneToMany(mappedBy = 'outlet')
    List<Review> reviews
}
