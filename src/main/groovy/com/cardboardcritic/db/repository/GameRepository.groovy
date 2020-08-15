package com.cardboardcritic.db.repository

import com.cardboardcritic.db.entity.Game
import io.quarkus.hibernate.orm.panache.PanacheRepository

import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class GameRepository implements PanacheRepository<Game> {
    Game findBySlug(String slug) {
        find('slug', slug).firstResult()
    }
}
