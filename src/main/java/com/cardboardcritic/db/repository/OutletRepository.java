package com.cardboardcritic.db.repository;

import com.cardboardcritic.db.entity.Outlet;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class OutletRepository implements PanacheRepository<Outlet> {

    public Uni<Outlet> findOrCreateByName(String name) {
        return find("name", name).firstResult()
                .onFailure().recoverWithUni(() -> persistAndReturn(new Outlet().setName(name)));
    }

    public Uni<Outlet> persistAndReturn(Outlet outlet) {
        return persistAndFlush(outlet).map(v -> outlet);
    }
}
