package com.cardboardcritic.db.entity

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import io.quarkus.hibernate.orm.panache.PanacheEntityBase

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

@ToString(includeNames = true, excludes = ['game', 'critic', 'outlet'])
@EqualsAndHashCode
@Entity
class Review extends PanacheEntityBase {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id

    @ManyToOne
    @JoinColumn(name = 'game_id', referencedColumnName = 'id')
    Game game

    @ManyToOne
    @JoinColumn(name = 'critic_id', referencedColumnName = 'id')
    Critic critic

    @ManyToOne
    @JoinColumn(name = 'outlet_id', referencedColumnName = 'id')
    Outlet outlet

    int score
    String summary, url
    boolean recommended
}
