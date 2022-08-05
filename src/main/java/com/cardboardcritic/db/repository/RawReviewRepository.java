package com.cardboardcritic.db.repository;

import com.cardboardcritic.db.entity.RawReview;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class RawReviewRepository implements PanacheRepositoryBase<RawReview, Integer> {

    public List<RawReview> visited(List<String> links) {
        return list("url in ?1", links);
    }
}
