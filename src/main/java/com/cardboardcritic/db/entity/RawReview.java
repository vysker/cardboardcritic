package com.cardboardcritic.db.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

/**
 * A raw review represents an unprocessed review article from a news outlet. After processing, it becomes available on
 * the website as a non-raw review.
 */
@Entity
@Table(name = "raw_review")
@FilterDef(name = "RawReview.byGame", defaultCondition = "game = :name", parameters = @ParamDef(name = "name", type = "string"))
@Filter(name = "RawReview.byGame")
@FilterDef(name = "RawReview.byCritic", defaultCondition = "critic = :name", parameters = @ParamDef(name = "name", type = "string"))
@Filter(name = "RawReview.byCritic")
@FilterDef(name = "RawReview.byOutlet", defaultCondition = "outlet = :name", parameters = @ParamDef(name = "name", type = "string"))
@Filter(name = "RawReview.byOutlet")
@FilterDef(name = "RawReview.byProcessed", defaultCondition = "processed = :value", parameters = @ParamDef(name = "value", type = "boolean"))
@Filter(name = "RawReview.byProcessed")
public class RawReview extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "INTEGER")
    private Integer id;

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

    public Integer getId() {
        return id;
    }

    public RawReview setId(Integer id) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RawReview rawReview = (RawReview) o;
        return score == rawReview.score && recommended == rawReview.recommended && processed == rawReview.processed && Objects.equals(id, rawReview.id) && Objects.equals(game, rawReview.game) && Objects.equals(critic, rawReview.critic) && Objects.equals(outlet, rawReview.outlet) && Objects.equals(summary, rawReview.summary) && Objects.equals(url, rawReview.url) && Objects.equals(title, rawReview.title) && Objects.equals(content, rawReview.content) && Objects.equals(date, rawReview.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, game, critic, outlet, summary, url, title, content, date, score, recommended, processed);
    }
}
