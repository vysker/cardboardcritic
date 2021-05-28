package com.cardboardcritic.db.repository;

import com.cardboardcritic.db.entity.Outlet;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class OutletRepository implements PanacheRepository<Outlet> {

    public Outlet findOrCreateByName(String name) {
        return find("name", name).firstResultOptional()
                .orElseGet(() -> persistAndReturn(new Outlet().setName(name)));
    }

    public Outlet persistAndReturn(Outlet outlet) {
        persistAndFlush(outlet);
        return outlet;
    }
}
