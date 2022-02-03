package com.cardboardcritic.web.template.form;

import org.jboss.resteasy.reactive.RestForm;

public class ReviewEditForm {
    private long id;
    @RestForm private String game;
    @RestForm private String critic;
    @RestForm private String outlet;
    @RestForm private String summary;
    @RestForm private String url;
    @RestForm private int score;
    @RestForm private boolean recommended;

    public long getId() {
        return id;
    }

    public ReviewEditForm setId(long id) {
        this.id = id;
        return this;
    }

    public String getGame() {
        return game;
    }

    public ReviewEditForm setGame(String game) {
        this.game = game;
        return this;
    }

    public String getCritic() {
        return critic;
    }

    public ReviewEditForm setCritic(String critic) {
        this.critic = critic;
        return this;
    }

    public String getOutlet() {
        return outlet;
    }

    public ReviewEditForm setOutlet(String outlet) {
        this.outlet = outlet;
        return this;
    }

    public String getSummary() {
        return summary;
    }

    public ReviewEditForm setSummary(String summary) {
        this.summary = summary;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public ReviewEditForm setUrl(String url) {
        this.url = url;
        return this;
    }

    public int getScore() {
        return score;
    }

    public ReviewEditForm setScore(int score) {
        this.score = score;
        return this;
    }

    public boolean isRecommended() {
        return recommended;
    }

    public ReviewEditForm setRecommended(boolean recommended) {
        this.recommended = recommended;
        return this;
    }
}
