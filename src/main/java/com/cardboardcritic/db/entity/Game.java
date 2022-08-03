package com.cardboardcritic.db.entity;

import com.cardboardcritic.db.entity.meta.HasName;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.time.LocalDate;
import java.util.List;

@Entity
public class Game extends PanacheEntityBase implements HasName<Game> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int average;
    private int median;
    private int recommended;
    private String name;
    private String shortDescription;
    private String description;
    private String designer;
    private String slug;
    private String image;
    private LocalDate releaseDate;

    @OneToMany(mappedBy = "game")
    private List<Review> reviews;

    public Long getId() {
        return id;
    }

    public Game setId(Long id) {
        this.id = id;
        return this;
    }

    public int getAverage() {
        return average;
    }

    public Game setAverage(int average) {
        this.average = average;
        return this;
    }

    public int getMedian() {
        return median;
    }

    public void setMedian(int median) {
        this.median = median;
    }

    public int getRecommended() {
        return recommended;
    }

    public Game setRecommended(int recommended) {
        this.recommended = recommended;
        return this;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Game setName(String name) {
        this.name = name;
        return this;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public Game setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Game setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getDesigner() {
        return designer;
    }

    public Game setDesigner(String designer) {
        this.designer = designer;
        return this;
    }

    public String getSlug() {
        return slug;
    }

    public Game setSlug(String slug) {
        this.slug = slug;
        return this;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public Game setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
        return this;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public Game setReviews(List<Review> reviews) {
        this.reviews = reviews;
        return this;
    }
}
