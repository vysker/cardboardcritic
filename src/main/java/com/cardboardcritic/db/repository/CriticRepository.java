package com.cardboardcritic.db.repository;

import com.cardboardcritic.db.entity.Critic;
import com.cardboardcritic.util.StringUtil;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CriticRepository implements PanacheRepository<Critic> {

    public Uni<Critic> findOrCreateByName(String name) {
        return find("name", name).firstResult()
                .onItem().ifNull().switchTo(persist(new Critic().setName(name)));
    }

    public Uni<Critic> createNewOrFindExisting(String newCritic, String existingCritic) {
        if (StringUtil.isNotEmpty(newCritic))
            return findOrCreateByName(newCritic);
        return find("name", existingCritic).firstResult();
    }
}
