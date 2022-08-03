package com.cardboardcritic.db.repository;

import com.cardboardcritic.db.entity.Game;
import com.cardboardcritic.util.StringUtil;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;

import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
public class GameRepository implements PanacheRepository<Game> {

    public Game findBySlug(String slug) {
        return find("slug", slug).firstResult();
    }

    public Game findOrCreateByName(String name) {
        return find("name", name).firstResultOptional()
                .or(() -> find("slug", slugify(name)).firstResultOptional())
                .orElseGet(() -> {
                    final Game game = new Game().setName(name).setSlug(slugify(name));
                    persist(game);
                    return game;
                });
    }

    public Game createNewOrFindExisting(String newGame, String existingGame) {
        if (StringUtil.isNotEmpty(newGame))
            return findOrCreateByName(newGame);
        return find("name", existingGame).firstResult();
    }

    public String slugify(String name) {
        return name.toLowerCase()
                .replaceAll("\\W", "-") // Remove non-word characters
                .replaceAll("-+", "-") // Remove sequences of multiple dashes, i.e. "--" -> "-"
                .replaceAll("(^-+|-+$)", ""); // Remove leading and trailing dashes
    }

    public List<Game> recent() {
        return findAll(Sort.by("releaseDate")).page(Page.ofSize(10)).list();
    }

    public List<Game> topOfYear() {
        final LocalDate firstDayOfThisYear = LocalDate.of(LocalDate.now().getYear(), 1, 1);
        final Sort sortByAverage = Sort.by("average", Sort.Direction.Descending);
        return find("releaseDate >= ?1", sortByAverage, firstDayOfThisYear)
                .page(Page.ofSize(10))
                .list();
    }
}
