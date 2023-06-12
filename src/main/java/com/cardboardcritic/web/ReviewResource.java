package com.cardboardcritic.web;

import com.cardboardcritic.config.GlobalTemplateExtensions;
import com.cardboardcritic.data.ReviewMapper;
import com.cardboardcritic.db.Pageable;
import com.cardboardcritic.db.entity.Critic;
import com.cardboardcritic.db.entity.Game;
import com.cardboardcritic.db.entity.Outlet;
import com.cardboardcritic.db.entity.Review;
import com.cardboardcritic.db.repository.CriticRepository;
import com.cardboardcritic.db.repository.GameRepository;
import com.cardboardcritic.db.repository.OutletRepository;
import com.cardboardcritic.db.repository.ReviewRepository;
import com.cardboardcritic.util.PagingUtil;
import com.cardboardcritic.util.StringUtil;
import com.cardboardcritic.web.template.form.ReviewEditForm;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import jakarta.annotation.security.RolesAllowed;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Path("review")
@RolesAllowed("admin")
public class ReviewResource {
    private final ReviewRepository reviewRepo;
    private final GameRepository gameRepo;
    private final CriticRepository criticRepo;
    private final OutletRepository outletRepo;
    private final ReviewMapper reviewMapper;

    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance edit(ReviewEditForm review,
                                                   List<Game> games,
                                                   List<Critic> critics,
                                                   List<Outlet> outlets);

        public static native TemplateInstance formFields(ReviewEditForm review,
                                                         List<Game> games,
                                                         List<Critic> critics,
                                                         List<Outlet> outlets);

        public static native TemplateInstance list(List<Review> reviews,
                                                   List<Game> games,
                                                   List<Critic> critics,
                                                   List<Outlet> outlets,
                                                   Map<String, String> filters,
                                                   Pageable pageable);

        public static native TemplateInstance index(List<Review> reviews,
                                                    List<Game> games,
                                                    List<Critic> critics,
                                                    List<Outlet> outlets,
                                                    Map<String, String> filters,
                                                    Pageable pageable);
    }

    public ReviewResource(ReviewRepository reviewRepo,
                          GameRepository gameRepo,
                          CriticRepository criticRepo,
                          OutletRepository outletRepo,
                          ReviewMapper reviewMapper) {
        this.reviewRepo = reviewRepo;
        this.gameRepo = gameRepo;
        this.criticRepo = criticRepo;
        this.outletRepo = outletRepo;
        this.reviewMapper = reviewMapper;
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance index(@QueryParam("game") Optional<String> gameFilter,
                                  @QueryParam("outlet") Optional<String> outletFilter,
                                  @QueryParam("critic") Optional<String> criticFilter,
                                  @QueryParam("published") Optional<String> publishedFilter,
                                  @QueryParam("page") Optional<Integer> page,
                                  @QueryParam("page-action") Optional<String> pageActionString) {
        final PanacheQuery<Review> reviewQuery = reviewRepo.findAll();

        gameFilter.filter(StringUtil::isNotEmpty)
                .flatMap(name -> gameRepo.find("name", name).firstResultOptional())
                .map(game -> (Game) game)
                .ifPresent(game -> reviewQuery.filter("Review.byGameId", Parameters.with("id", game.getId())));
        criticFilter.filter(StringUtil::isNotEmpty)
                .flatMap(name -> criticRepo.find("name", name).firstResultOptional())
                .map(critic -> (Critic) critic)
                .ifPresent(critic -> reviewQuery.filter("Review.byCriticId", Parameters.with("id", critic.getId())));
        outletFilter.filter(StringUtil::isNotEmpty)
                .flatMap(name -> outletRepo.find("name", name).firstResultOptional())
                .map(outlet -> (Outlet) outlet)
                .ifPresent(outlet -> reviewQuery.filter("Review.byOutletId", Parameters.with("id", outlet.getId())));
        publishedFilter.filter(StringUtil::isNotEmpty)
                .ifPresentOrElse(status -> {
                    if ("true".equals(status))
                        reviewQuery.filter("Review.byPublished", Parameters.with("value", true));
                    if ("false".equals(status))
                        reviewQuery.filter("Review.byPublished", Parameters.with("value", false));
                }, () -> reviewQuery.filter("Review.byPublished", Parameters.with("value", true)));

        final Map<String, String> filters = Map.of(
                "game", gameFilter.orElse(""),
                "critic", criticFilter.orElse(""),
                "outlet", outletFilter.orElse(""),
                "published", publishedFilter.orElse("true")
        );

        final int newPage = PagingUtil.getNewPageNumber(page, pageActionString);
        final List<Review> reviews = reviewQuery.page(newPage, 20).list();
        final Pageable pageable = PagingUtil.pageable(reviewQuery, newPage);

        return Templates.index(reviews,
                gameRepo.listAll(Sort.by("name")),
                criticRepo.listAll(Sort.by("name")),
                outletRepo.listAll(Sort.by("name")),
                filters,
                pageable);
    }

    @GET
    @Path("{id}/edit")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance edit(@PathParam int id) {
        final List<Game> games = gameRepo.listAll();
        final List<Critic> critics = criticRepo.listAll();
        final List<Outlet> outlets = outletRepo.listAll();
        final Review review = reviewRepo.findById(id);

        return Templates.edit(reviewMapper.toForm(review), games, critics, outlets);
    }

    @POST // Should be PATCH, but HTML forms only support POST
    @Path("{id}/edit")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    @Transactional
    public Response save(@PathParam int id, @BeanParam ReviewEditForm form) {
        final Review review = reviewRepo.findById(id);
        final Game game = gameRepo.createNewOrFindExisting(form.newGame, form.game);
        final Critic critic = criticRepo.createNewOrFindExisting(form.newCritic, form.critic);
        final Outlet outlet = outletRepo.createNewOrFindExisting(form.newOutlet, form.outlet);

        final List<String> errors = Stream.of(
                game == null ? "No game was provided. Please select or create a game" : null,
                critic == null ? "No critic was provided. Please select or create a critic" : null,
                outlet == null ? "No outlet was provided. Please select or create a outlet" : null
        ).filter(Objects::nonNull).toList();

        if (!errors.isEmpty()) {
            gameRepo.flush();
            criticRepo.flush();
            outletRepo.flush();

            final TemplateInstance template = edit(id).setAttribute(GlobalTemplateExtensions.ERRORS_ATTRIBUTE, errors);
            return Response.ok(template, MediaType.TEXT_HTML).build();
        }

        review.setGame(game)
                .setCritic(critic)
                .setOutlet(outlet)
                .setScore(form.score)
                .setSummary(form.summary)
                .setUrl(form.url)
                .setRecommended(form.recommended);
        reviewRepo.persist(review);
        return Response.seeOther(getRedirectUri(review)).build();
    }

    private URI getRedirectUri(Review review) {
        return URI.create("/game/" + review.getGame().getSlug());
    }
}
