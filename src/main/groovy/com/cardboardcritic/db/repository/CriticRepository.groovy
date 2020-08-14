package com.cardboardcritic.db.repository

import com.cardboardcritic.db.entity.Critic
import io.quarkus.hibernate.orm.panache.PanacheRepository

import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class CriticRepository implements PanacheRepository<Critic> {
}
