package com.cardboardcritic.db.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.With;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@With
@Builder
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
    private String content;
    private String date;
    private int score;
    private boolean recommended;
    private boolean processed;
}
