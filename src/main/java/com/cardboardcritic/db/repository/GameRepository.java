package com.cardboardcritic.db.repository;

import com.cardboardcritic.db.entity.Game;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class GameRepository implements PanacheRepository<Game> {

    public Game findBySlug(String slug) {
        return find("slug", slug).firstResult();
    }

    public Game findByName(String name) {
        return find("name", name).firstResult();
    }

    public Game findOrCreateByName(String name) {
        Game result = findByName(name);
        return result != null ? result : persistAndReturn(new Game().withName(name));
    }

    public Game persistAndReturn(Game game) {
        persistAndFlush(game);
        return game;
    }

}
