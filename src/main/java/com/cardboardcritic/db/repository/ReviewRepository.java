package com.cardboardcritic.db.repository;

import com.cardboardcritic.db.entity.Review;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class ReviewRepository implements PanacheRepository<Review> {

    public List<Review> visited(List<String> links) {
        return list("url in ?1", links);
    }
}
