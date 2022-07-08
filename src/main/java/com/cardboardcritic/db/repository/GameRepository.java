package com.cardboardcritic.db.repository;

import com.cardboardcritic.db.entity.Game;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class GameRepository implements PanacheRepository<Game> {

    public Uni<Game> findBySlug(String slug) {
        return find("slug", slug).firstResult();
    }

    public Uni<Game> findOrCreateByName(String name) {
        return find("name", name)
                .firstResult()
                .onItem().ifNull().switchTo(() -> find("slug", slugify(name)).firstResult())
                .onItem().ifNull().switchTo((() -> persistAndReturn(new Game().setName(name).setSlug(slugify(name)))));
    }

    public Uni<Game> persistAndReturn(Game game) {
        return persist(game).map(v -> game);
    }

    public String slugify(String name) {
        return name.toLowerCase().replaceAll(" ", "-");
    }
}
