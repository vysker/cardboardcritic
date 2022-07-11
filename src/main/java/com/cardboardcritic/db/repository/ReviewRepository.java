package com.cardboardcritic.db.repository;

import com.cardboardcritic.db.entity.Review;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class ReviewRepository implements PanacheRepository<Review> {

    public Uni<List<Review>> visited(List<String> links) {
        return list("url in ?1", links);
    }
}
