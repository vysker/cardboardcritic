package com.cardboardcritic.web;

import com.cardboardcritic.db.Pageable;
import com.cardboardcritic.db.entity.Game;
import com.cardboardcritic.db.repository.GameRepository;
import com.cardboardcritic.util.PagingUtil;
import com.cardboardcritic.util.StringUtil;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import jakarta.annotation.security.PermitAll;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Path("browse")
@PermitAll
public class BrowseResource {
    private static final String DEFAULT_SORT = "median";
    private static final List<String> ALLOWED_SORTS = List.of("median", "designer", "recommended", "name");

    private final GameRepository gameRepository;

    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance recent(List<Game> games);

        public static native TemplateInstance search(List<Game> games);

        public static native TemplateInstance browse(List<Game> games,
                                                     List<String> years,
                                                     List<String> designers,
                                                     List<String> publishers,
                                                     List<String> sorts,
                                                     Map<String, String> filters,
                                                     Pageable pageable);
    }

    public BrowseResource(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance browse(@QueryParam("year") Optional<String> yearFilter,
                                   @QueryParam("designer") Optional<String> designerFilter,
                                   @QueryParam("publisher") Optional<String> publisherFilter,
                                   @QueryParam("sort") Optional<String> sortFilter,
                                   @QueryParam("page") Optional<Integer> page,
                                   @QueryParam("page-action") Optional<String> pageActionString) {
        final String sort = sortFilter.filter(StringUtil::isNotEmpty)
                .filter(ALLOWED_SORTS::contains)
                .orElse(DEFAULT_SORT);
        final Sort.Direction sortDirection = sort.equals("designer") || sort.equals("name")
                ? Sort.Direction.Ascending
                : Sort.Direction.Descending;

        final PanacheQuery<Game> gameQuery = gameRepository.findAll(Sort.by(sort, sortDirection));
        // TODO: Refactor this so we don't fetch the entirety of our game table every time someone browses
        final List<Game> allGames = gameRepository.findAll().list();

        final List<String> years = allGames.stream()
                .map(Game::getReleaseDate)
                .filter(Objects::nonNull)
                .map(LocalDate::getYear)
                .distinct()
                .sorted(Comparator.comparingInt(year -> year))
                .map(String::valueOf)
                .toList();
        final List<String> designers = allGames.stream()
                .map(Game::getDesigner)
                .filter(Objects::nonNull)
                .distinct()
                .sorted(Comparator.comparing(designer -> designer, Comparator.naturalOrder()))
                .toList();
        final List<String> publishers = allGames.stream()
                .map(Game::getPublisher)
                .filter(Objects::nonNull)
                .distinct()
                .sorted(Comparator.comparing(publisher -> publisher, Comparator.naturalOrder()))
                .toList();

        yearFilter.filter(StringUtil::isNotEmpty)
                .ifPresent(year -> gameQuery.filter("Game.byYear", Parameters.with("year", Integer.valueOf(year))));
        designerFilter.filter(StringUtil::isNotEmpty)
                .ifPresent(designer -> gameQuery.filter("Game.byDesigner", Parameters.with("designer", designer)));
        publisherFilter.filter(StringUtil::isNotEmpty)
                .ifPresent(publisher -> gameQuery.filter("Game.byPublisher", Parameters.with("publisher", publisher)));

        final Map<String, String> filters = Map.of(
                "year", yearFilter.orElse(""),
                "designer", designerFilter.orElse(""),
                "publisher", publisherFilter.orElse(""),
                "sort", sortFilter.orElse(DEFAULT_SORT)
        );

        final int newPage = PagingUtil.getNewPageNumber(page, pageActionString);
        final List<Game> games = gameQuery.page(newPage, 20).list();
        final Pageable pageable = PagingUtil.pageable(gameQuery, newPage);

        return Templates.browse(games, years, designers, publishers, ALLOWED_SORTS, filters, pageable);
    }

    @GET
    @Path("recent")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance recent() {
        return Templates.recent(gameRepository.recent());
    }

    @GET
    @Path("search")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance recent(@QueryParam("q") String name) {
        final List<Game> matches = gameRepository.getEntityManager()
                .createNativeQuery("""
                        select *
                        from game
                        where to_tsvector('english', name) @@ plainto_tsquery('english', :name)
                        """.stripIndent(), Game.class)
                .setParameter("name", name)
                .getResultList();
        return Templates.search(matches);
    }
}
