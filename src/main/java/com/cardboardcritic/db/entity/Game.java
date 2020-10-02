package com.cardboardcritic.db.entity;

import com.cardboardcritic.db.entity.meta.HasName;
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
import javax.persistence.OneToMany;
import java.time.LocalDate;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@With
@Builder
@Entity
public class Game extends PanacheEntityBase implements HasName {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int score;
    private int recommended;
    private String name;
    private String shortDescription;
    private String description;
    private String designer;
    private String slug;
    private LocalDate releaseDate;

    @OneToMany(mappedBy = "game")
    private List<Review> reviews;
}
