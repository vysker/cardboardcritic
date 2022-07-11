package com.cardboardcritic.db.repository;

import com.cardboardcritic.db.entity.RawReview;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class RawReviewRepository implements PanacheRepository<RawReview> {

    public Uni<List<RawReview>> visited(List<String> links) {
        return list("url in ?1", links);
    }
}
