package com.cardboardcritic.db.repository;

import com.cardboardcritic.db.entity.Game;
import com.cardboardcritic.util.StringUtil;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
public class GameRepository implements PanacheRepository<Game> {

    public Uni<Game> findBySlug(String slug) {
        return find("slug", slug).firstResult();
    }

    public Uni<Game> findOrCreateByName(String name) {
        return find("name", name)
                .firstResult()
                .onItem().ifNull().switchTo(() -> find("slug", slugify(name)).firstResult())
                .onItem().ifNull().switchTo((() -> persist(new Game().setName(name).setSlug(slugify(name)))));
    }

    public Uni<Game> createNewOrFindExisting(String newGame, String existingGame) {
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

    public Uni<List<Game>> recent() {
        return findAll(Sort.by("releaseDate")).page(Page.ofSize(10)).list();
    }

    public Uni<List<Game>> topOfYear() {
        final LocalDate firstDayOfThisYear = LocalDate.of(LocalDate.now().getYear(), 1, 1);
        final Sort sortByAverage = Sort.by("average", Sort.Direction.Descending);
        return find("releaseDate >= ?1", sortByAverage, firstDayOfThisYear)
                .page(Page.ofSize(10))
                .list();
    }
}
