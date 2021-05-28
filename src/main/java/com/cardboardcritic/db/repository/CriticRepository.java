package com.cardboardcritic.db.repository;

import com.cardboardcritic.db.entity.Critic;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CriticRepository implements PanacheRepository<Critic> {

    public Critic findOrCreateByName(String name) {
        return find("name", name).firstResultOptional()
                .orElseGet(() -> persistAndReturn(new Critic().setName(name)));
    }

    public Critic persistAndReturn(Critic critic) {
        persistAndFlush(critic);
        return critic;
    }
}
