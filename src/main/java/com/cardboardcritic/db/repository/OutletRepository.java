package com.cardboardcritic.db.repository;

import com.cardboardcritic.db.entity.Outlet;
import com.cardboardcritic.util.StringUtil;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class OutletRepository implements PanacheRepository<Outlet> {

    public Uni<Outlet> findOrCreateByName(String name) {
        return find("name", name).firstResult()
                .onItem().ifNull().switchTo(() -> persist(new Outlet().setName(name)));
    }

    public Uni<Outlet> createNewOrFindExisting(String newOutlet, String existingOutlet) {
        if (StringUtil.isNotEmpty(newOutlet))
            return findOrCreateByName(newOutlet);
        return find("name", existingOutlet).firstResult();
    }
}
