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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@With
@Builder
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
    private String summary;
    private String url;
    private boolean recommended;
}
