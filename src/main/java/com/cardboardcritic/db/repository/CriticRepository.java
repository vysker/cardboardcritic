package com.cardboardcritic.db.repository;

import com.cardboardcritic.db.entity.Critic;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CriticRepository implements PanacheRepository<Critic> {

    public Uni<Critic> findOrCreateByName(String name) {
        return find("name", name)
                .firstResult()
                .onFailure().recoverWithUni(() -> persistAndReturn(new Critic().setName(name)));
    }

    public Uni<Critic> persistAndReturn(Critic critic) {
        return persistAndFlush(critic).map(v -> critic);
    }
}
