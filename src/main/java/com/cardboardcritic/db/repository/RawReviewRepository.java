package com.cardboardcritic.db.repository;

import com.cardboardcritic.db.entity.RawReview;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RawReviewRepository implements PanacheRepository<RawReview> {
}
