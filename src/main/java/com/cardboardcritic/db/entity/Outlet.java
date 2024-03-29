package com.cardboardcritic.db.entity;

import com.cardboardcritic.db.entity.meta.HasName;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;

@Entity
public class Outlet extends PanacheEntityBase implements HasName<Outlet> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "INTEGER")
    private Integer id;

    private String name;
    private String website;

    @OneToMany(mappedBy = "outlet")
    private List<Review> reviews;

    public Integer getId() {
        return id;
    }

    public Outlet setId(Integer id) {
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
