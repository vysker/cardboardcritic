package com.cardboardcritic.service;

import com.cardboardcritic.db.entity.Game;
import com.cardboardcritic.db.repository.GameRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;
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
}
