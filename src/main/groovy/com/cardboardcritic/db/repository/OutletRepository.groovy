package com.cardboardcritic.db.repository

import com.cardboardcritic.db.entity.Outlet
import io.quarkus.hibernate.orm.panache.PanacheRepository

import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class OutletRepository implements PanacheRepository<Outlet> {
    Outlet findByName(String name) {
        find('name', name).firstResult()
    }

    Outlet findOrCreateByName(String name) {
        def result = findByName name
        result ?: persistAndReturn(new Outlet(name: name))
    }

    Outlet persistAndReturn(Outlet outlet) {
        persistAndFlush outlet
        outlet
    }
}
