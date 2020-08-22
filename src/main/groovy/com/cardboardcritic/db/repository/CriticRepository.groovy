package com.cardboardcritic.db.repository

import com.cardboardcritic.db.entity.Critic
import io.quarkus.hibernate.orm.panache.PanacheRepository

import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class CriticRepository implements PanacheRepository<Critic> {
    Critic findByName(String name) {
        find('name', name).firstResult()
    }

    Critic findOrCreateByName(String name) {
        def result = findByName name
        result ?: persistAndReturn(new Critic(name: name))
    }

    Critic persistAndReturn(Critic critic) {
        persistAndFlush critic
        critic
    }
}
