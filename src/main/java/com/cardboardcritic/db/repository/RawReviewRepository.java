package com.cardboardcritic.db.repository;

import com.cardboardcritic.db.entity.RawReview;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class RawReviewRepository implements PanacheRepository<RawReview> {

    public List<RawReview> visited(List<String> links) {
        return list("url in ?1", links);
    }
}
