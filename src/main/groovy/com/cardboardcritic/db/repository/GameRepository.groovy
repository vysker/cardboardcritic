package com.cardboardcritic.db.repository

import com.cardboardcritic.db.entity.Game
import io.quarkus.hibernate.orm.panache.PanacheRepository

import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class GameRepository implements PanacheRepository<Game> {
    Game findBySlug(String slug) {
        find('slug', slug).firstResult()
    }

    Game findByName(String name) {
        find('name', name).firstResult()
    }

    Game findOrCreateByName(String name) {
        def result = findByName name
        result ?: persistAndReturn(new Game(name: name))
    }

    Game persistAndReturn(Game game) {
        persistAndFlush game
        game
    }
}
