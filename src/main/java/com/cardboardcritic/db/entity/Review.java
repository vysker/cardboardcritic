package com.cardboardcritic.db.entity;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;

import javax.persistence.*;

@Entity
public class Review extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "game_id", referencedColumnName = "id")
    private Game game;

    @ManyToOne
    @JoinColumn(name = "critic_id", referencedColumnName = "id")
    private Critic critic;

    @ManyToOne
    @JoinColumn(name = "outlet_id", referencedColumnName = "id")
    private Outlet outlet;

    private int score;

    // by default, hibernate assumes the 'text' data type is a varchar(255)
    @Column(length = 999999)
    private String summary;

    private String url;

    private boolean recommended;

    public Long getId() {
        return id;
    }

    public Review setId(Long id) {
        this.id = id;
        return this;
    }

    public Game getGame() {
        return game;
    }

    public Review setGame(Game game) {
        this.game = game;
        return this;
    }

    public Critic getCritic() {
        return critic;
    }

    public Review setCritic(Critic critic) {
        this.critic = critic;
        return this;
    }

    public Outlet getOutlet() {
        return outlet;
    }

    public Review setOutlet(Outlet outlet) {
        this.outlet = outlet;
        return this;
    }

    public int getScore() {
        return score;
    }

    public Review setScore(int score) {
        this.score = score;
        return this;
    }

    public String getSummary() {
        return summary;
    }

    public Review setSummary(String summary) {
        this.summary = summary;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public Review setUrl(String url) {
        this.url = url;
        return this;
    }

    public boolean isRecommended() {
        return recommended;
    }

    public Review setRecommended(boolean recommended) {
        this.recommended = recommended;
        return this;
    }
}
