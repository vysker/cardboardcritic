package com.cardboardcritic.db.entity;

import com.cardboardcritic.db.entity.meta.HasName;
import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;

import javax.persistence.*;
import java.util.List;

@Entity
public class Outlet extends PanacheEntityBase implements HasName<Outlet> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String website;

    @OneToMany(mappedBy = "outlet")
    private List<Review> reviews;

    public Long getId() {
        return id;
    }

    public Outlet setId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Outlet setName(String name) {
        this.name = name;
        return this;
    }

    public String getWebsite() {
        return website;
    }

    public Outlet setWebsite(String website) {
        this.website = website;
        return this;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public Outlet setReviews(List<Review> reviews) {
        this.reviews = reviews;
        return this;
    }
}
