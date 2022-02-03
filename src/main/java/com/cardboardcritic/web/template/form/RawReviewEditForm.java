package com.cardboardcritic.web.template.form;

import org.jboss.resteasy.reactive.RestForm;

public class RawReviewEditForm {
    private long id;
    @RestForm private String game;
    @RestForm private String critic;
    @RestForm private String outlet;
    @RestForm private String summary;
    @RestForm private String url;
    @RestForm private String title;
    @RestForm private String content;
    @RestForm private String date;
    @RestForm private int score;
    @RestForm private boolean recommended;

    public long getId() {
        return id;
    }

    public RawReviewEditForm setId(long id) {
        this.id = id;
        return this;
    }

    public String getGame() {
        return game;
    }

    public RawReviewEditForm setGame(String game) {
        this.game = game;
        return this;
    }

    public String getCritic() {
        return critic;
    }

    public RawReviewEditForm setCritic(String critic) {
        this.critic = critic;
        return this;
    }

    public String getOutlet() {
        return outlet;
    }

    public RawReviewEditForm setOutlet(String outlet) {
        this.outlet = outlet;
        return this;
    }

    public String getSummary() {
        return summary;
    }

    public RawReviewEditForm setSummary(String summary) {
        this.summary = summary;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public RawReviewEditForm setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public RawReviewEditForm setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getContent() {
        return content;
    }

    public RawReviewEditForm setContent(String content) {
        this.content = content;
        return this;
    }

    public String getDate() {
        return date;
    }

    public RawReviewEditForm setDate(String date) {
        this.date = date;
        return this;
    }

    public int getScore() {
        return score;
    }

    public RawReviewEditForm setScore(int score) {
        this.score = score;
        return this;
    }

    public boolean isRecommended() {
        return recommended;
    }

    public RawReviewEditForm setRecommended(boolean recommended) {
        this.recommended = recommended;
        return this;
    }
}
