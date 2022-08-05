package com.cardboardcritic.db.repository;

import com.cardboardcritic.db.entity.Outlet;
import com.cardboardcritic.util.StringUtil;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class OutletRepository implements PanacheRepositoryBase<Outlet, Integer> {

    public Outlet findOrCreateByName(String name) {
        return find("name", name).firstResultOptional()
                .orElseGet(() -> {
                    final Outlet outlet = new Outlet().setName(name);
                    persist(outlet);
                    return outlet;
                });
    }

    public Outlet createNewOrFindExisting(String newOutlet, String existingOutlet) {
        if (StringUtil.isNotEmpty(newOutlet))
            return findOrCreateByName(newOutlet);
        return find("name", existingOutlet).firstResult();
    }
}
