package com.cardboardcritic.db.repository;

import com.cardboardcritic.db.entity.Review;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class ReviewRepository implements PanacheRepositoryBase<Review, Integer> {

    public List<Review> visited(List<String> links) {
        return list("url in ?1", links);
    }
}
