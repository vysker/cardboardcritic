package com.cardboardcritic.db.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.*;

@Entity
@Table(name = "raw_review")
public class RawReview extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String game;
    private String critic;
    private String outlet;
    private String summary;
    private String url;
    private String title;

    // by default, hibernate assumes the 'text' data type is a varchar(255)
    @Column(length = 999999)
    private String content;

    private String date;
    private int score;
    private boolean recommended;
    private boolean processed;

    public Long getId() {
        return id;
    }

    public RawReview setId(Long id) {
        this.id = id;
        return this;
    }

    public String getGame() {
        return game;
    }

    public RawReview setGame(String game) {
        this.game = game;
        return this;
    }

    public String getCritic() {
        return critic;
    }

    public RawReview setCritic(String critic) {
        this.critic = critic;
        return this;
    }

    public String getOutlet() {
        return outlet;
    }

    public RawReview setOutlet(String outlet) {
        this.outlet = outlet;
        return this;
    }

    public String getSummary() {
        return summary;
    }

    public RawReview setSummary(String summary) {
        this.summary = summary;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public RawReview setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public RawReview setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getContent() {
        return content;
    }

    public RawReview setContent(String content) {
        this.content = content;
        return this;
    }

    public String getDate() {
        return date;
    }

    public RawReview setDate(String date) {
        this.date = date;
        return this;
    }

    public int getScore() {
        return score;
    }

    public RawReview setScore(int score) {
        this.score = score;
        return this;
    }

    public boolean isRecommended() {
        return recommended;
    }

    public RawReview setRecommended(boolean recommended) {
        this.recommended = recommended;
        return this;
    }

    public boolean isProcessed() {
        return processed;
    }

    public RawReview setProcessed(boolean processed) {
        this.processed = processed;
        return this;
    }
}
