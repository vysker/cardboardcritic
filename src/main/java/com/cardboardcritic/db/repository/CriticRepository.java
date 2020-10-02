package com.cardboardcritic.db.repository;

import com.cardboardcritic.db.entity.Critic;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CriticRepository implements PanacheRepository<Critic> {

    public Critic findByName(String name) {
        return find("name", name).firstResult();
    }

    public Critic findOrCreateByName(String name) {
        Critic result = findByName(name);
        return result != null ? result : persistAndReturn(new Critic().withName(name));
    }

    public Critic persistAndReturn(Critic critic) {
        persistAndFlush(critic);
        return critic;
    }

}
