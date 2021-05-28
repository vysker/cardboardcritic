package com.cardboardcritic.db.repository;

import com.cardboardcritic.db.entity.Game;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class GameRepository implements PanacheRepository<Game> {

    public Game findBySlug(String slug) {
        return find("slug", slug).firstResult();
    }

    public Game findOrCreateByName(String name) {
        return find("name", name).firstResultOptional()
                .or(() -> find("slug", slugify(name)).firstResultOptional())
                .orElseGet(() -> persistAndReturn(new Game()
                        .setName(name)
                        .setSlug(slugify(name))));
    }

    public Game persistAndReturn(Game game) {
        persistAndFlush(game);
        return game;
    }

    public String slugify(String name) {
        return name.toLowerCase().replaceAll(" ", "-");
    }
}
