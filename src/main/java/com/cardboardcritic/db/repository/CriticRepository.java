package com.cardboardcritic.db.repository;

import com.cardboardcritic.db.entity.Critic;
import com.cardboardcritic.util.StringUtil;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CriticRepository implements PanacheRepository<Critic> {
    public Critic findOrCreateByName(String name) {
        return find("name", name).firstResultOptional()
                .orElseGet(() -> {
                    final Critic critic = new Critic().setName(name);
                    persist(critic);
                    return critic;
                });
    }

    public Critic createNewOrFindExisting(String newCritic, String existingCritic) {
        if (StringUtil.isNotEmpty(newCritic))
            return findOrCreateByName(newCritic);
        return find("name", existingCritic).firstResult();
    }
}
