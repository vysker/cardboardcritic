package com.cardboardcritic.db.repository

import com.cardboardcritic.db.entity.Review
import io.quarkus.hibernate.orm.panache.PanacheRepository

import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class ReviewRepository implements PanacheRepository<Review> {
}
