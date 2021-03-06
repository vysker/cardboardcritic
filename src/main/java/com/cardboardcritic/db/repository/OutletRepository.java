package com.cardboardcritic.db.repository;

import com.cardboardcritic.db.entity.Outlet;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class OutletRepository implements PanacheRepository<Outlet> {

    public Uni<Outlet> findOrCreateByName(String name) {
        return find("name", name).firstResult()
                .onItem().ifNull().switchTo(() -> persistAndReturn(new Outlet().setName(name)));
    }

    public Uni<Outlet> persistAndReturn(Outlet outlet) {
        return persist(outlet).map(v -> outlet);
    }
}
