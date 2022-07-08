package com.cardboardcritic.service;

import com.cardboardcritic.db.entity.Game;
import com.cardboardcritic.db.repository.GameRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
public class GameService {
    private final GameRepository repo;

    public GameService(GameRepository repo) {
        this.repo = repo;
    }

    public Uni<List<Game>> recent() {
        return repo.findAll(Sort.by("releaseDate")).page(Page.ofSize(10)).list();
    }

    public Uni<List<Game>> topOfYear() {
        final LocalDate firstDayOfThisYear = LocalDate.of(LocalDate.now().getYear(), 1, 1);
        final Sort sortByScore = Sort.by("score", Sort.Direction.Descending);
        return repo.find("releaseDate >= ?1", sortByScore, firstDayOfThisYear)
                .page(Page.ofSize(10))
                .list();
    }
}
