package com.cardboardcritic.db.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

@Entity
@Filter(name = "Review.byGameId")
@Filter(name = "Review.byCriticId")
@Filter(name = "Review.byOutletId")
@Filter(name = "Review.byPublished")
@FilterDef(name = "Review.byGameId",
        defaultCondition = "game_id = :id",
        parameters = @ParamDef(name = "id", type = Integer.class))
@FilterDef(name = "Review.byCriticId",
        defaultCondition = "critic_id = :id",
        parameters = @ParamDef(name = "id", type = Integer.class))
@FilterDef(name = "Review.byOutletId",
        defaultCondition = "outlet_id = :id",
        parameters = @ParamDef(name = "id", type = Integer.class))
@FilterDef(name = "Review.byPublished",
        defaultCondition = "published = :value",
        parameters = @ParamDef(name = "value", type = Boolean.class))
public class Review extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "INTEGER")
    private Integer id;

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

    private boolean published;

    public Integer getId() {
        return id;
    }

    public Review setId(Integer id) {
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

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }
}
