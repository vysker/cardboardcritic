package com.cardboardcritic.web;

import com.cardboardcritic.config.GlobalTemplateExtensions;
import com.cardboardcritic.data.ReviewMapper;
import com.cardboardcritic.db.entity.Critic;
import com.cardboardcritic.db.entity.Game;
import com.cardboardcritic.db.entity.Outlet;
import com.cardboardcritic.db.entity.Review;
import com.cardboardcritic.db.repository.CriticRepository;
import com.cardboardcritic.db.repository.GameRepository;
import com.cardboardcritic.db.repository.OutletRepository;
import com.cardboardcritic.db.repository.ReviewRepository;
import com.cardboardcritic.web.template.form.ReviewEditForm;
import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import io.smallrye.mutiny.Uni;
import org.jboss.resteasy.reactive.RestPath;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Path("review")
@RolesAllowed("admin")
public class ReviewResource {
    private final ReviewRepository reviewRepo;
    private final GameRepository gameRepo;
    private final CriticRepository criticRepo;
    private final OutletRepository outletRepo;

    @Inject
    ReviewMapper reviewMapper;

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
    }

    public ReviewResource(ReviewRepository reviewRepo,
                          GameRepository gameRepo,
                          CriticRepository criticRepo,
                          OutletRepository outletRepo) {
        this.reviewRepo = reviewRepo;
        this.gameRepo = gameRepo;
        this.criticRepo = criticRepo;
        this.outletRepo = outletRepo;
    }

    @GET
    @Path("{id}/edit")
    @Produces(MediaType.TEXT_HTML)
    @ReactiveTransactional
    public Uni<TemplateInstance> edit(@RestPath long id) {
        final Uni<List<Game>> games$ = gameRepo.listAll();
        final Uni<List<Critic>> critics$ = criticRepo.listAll();
        final Uni<List<Outlet>> outlets$ = outletRepo.listAll();
        final Uni<Review> review$ = reviewRepo.findById(id);

        return Uni.combine().all().unis(review$, games$, critics$, outlets$)
                .combinedWith((review, games, critics, outlets) ->
                        Templates.edit(reviewMapper.toForm(review), games, critics, outlets));
    }

    @POST // Should be PATCH, but HTML forms only support POST
    @Path("{id}/edit")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    @ReactiveTransactional
    public Uni<Response> save(@RestPath long id, @BeanParam ReviewEditForm form) {
        final Uni<Review> review$ = reviewRepo.findById(id);
        final Uni<Game> game$ = gameRepo.createNewOrFindExisting(form.newGame, form.game);
        final Uni<Critic> critic$ = criticRepo.createNewOrFindExisting(form.newCritic, form.critic);
        final Uni<Outlet> outlet$ = outletRepo.createNewOrFindExisting(form.newOutlet, form.outlet);

        return Uni.combine().all().unis(review$, game$, critic$, outlet$)
                .combinedWith((review, game, critic, outlet) -> {
                    final List<String> errors = Stream.of(
                            game == null ? "No game was provided. Please select or create a game" : null,
                            critic == null ? "No critic was provided. Please select or create a critic" : null,
                            outlet == null ? "No outlet was provided. Please select or create a outlet" : null
                    ).filter(Objects::nonNull).toList();

                    if (!errors.isEmpty()) {
                        return gameRepo.flush()
                                .flatMap(x -> criticRepo.flush())
                                .flatMap(x -> outletRepo.flush())
                                .flatMap(x -> edit(id))
                                .invoke(template ->
                                        template.setAttribute(GlobalTemplateExtensions.ERRORS_ATTRIBUTE, errors))
                                .map(template -> Response.ok(template, MediaType.TEXT_HTML).build());
                    }

                    review.setGame(game)
                            .setCritic(critic)
                            .setOutlet(outlet)
                            .setScore(form.score)
                            .setSummary(form.summary)
                            .setUrl(form.url)
                            .setRecommended(form.recommended);
                    return reviewRepo.persist(review)
                            .map(x -> Response.seeOther(getRedirectUri(review)).build());
                })
                .flatMap(response -> response);
    }

    private URI getRedirectUri(Review review) {
        return URI.create("/game/" + review.getGame().getSlug());
    }
}
