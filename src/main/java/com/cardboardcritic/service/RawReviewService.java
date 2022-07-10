package com.cardboardcritic.service;

import com.cardboardcritic.db.entity.RawReview;
import com.cardboardcritic.db.repository.RawReviewRepository;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class RawReviewService {
    private final RawReviewRepository repo;

    public RawReviewService(RawReviewRepository repo) {
        this.repo = repo;
    }

    public Uni<List<RawReview>> visited(List<String> links) {
        return repo.list("url in ?1", links);
    }

    public Uni<RawReview> persist(RawReview rawReview) {
        return repo.persist(rawReview);
    }
}
