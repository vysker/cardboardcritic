package com.cardboardcritic.service;

import com.cardboardcritic.db.entity.Review;
import com.cardboardcritic.db.repository.ReviewRepository;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class ReviewService {
    private final ReviewRepository repo;

    public ReviewService(ReviewRepository repo) {
        this.repo = repo;
    }


    public Uni<List<Review>> visited(List<String> links) {
        return repo.list("url in ?1", links);
    }
}
