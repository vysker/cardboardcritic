package com.cardboardcritic.db.entity;

import com.cardboardcritic.db.entity.meta.HasName;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class Critic extends PanacheEntityBase implements HasName<Critic> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "critic")
    private List<Review> reviews;

    public Long getId() {
        return id;
    }

    public Critic setId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Critic setName(String name) {
        this.name = name;
        return this;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public Critic setReviews(List<Review> reviews) {
        this.reviews = reviews;
        return this;
    }
}
