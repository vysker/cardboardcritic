package com.cardboardcritic.db.repository;

import com.cardboardcritic.db.entity.Outlet;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class OutletRepository implements PanacheRepository<Outlet> {

    public Outlet findByName(String name) {
        return find("name", name).firstResult();
    }

    public Outlet findOrCreateByName(String name) {
        Outlet result = findByName(name);
        return result != null ? result : persistAndReturn(new Outlet().withName(name));
    }

    public Outlet persistAndReturn(Outlet outlet) {
        persistAndFlush(outlet);
        return outlet;
    }

}
