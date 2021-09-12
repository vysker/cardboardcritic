package com.cardboardcritic.web.template.form;

import org.jboss.resteasy.annotations.jaxrs.FormParam;

public class ReviewEditForm {
    private long id;
    @FormParam private String game;
    @FormParam private String critic;
    @FormParam private String outlet;
    @FormParam private String summary;
    @FormParam private String url;
    @FormParam private int score;
    @FormParam private boolean recommended;

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
