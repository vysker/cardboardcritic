package com.cardboardcritic.db.repository

import com.cardboardcritic.db.entity.Review
import groovy.sql.Sql

class ReviewRepository extends Repository<Review> {
    ReviewRepository(Sql sql) {
        super(sql)
    }

    @Override
    protected Review entityInstance() {
        new Review()
    }
}
