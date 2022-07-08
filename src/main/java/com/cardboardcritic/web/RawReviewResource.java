package com.cardboardcritic.web;

import com.cardboardcritic.data.RawReviewMapper;
import com.cardboardcritic.db.entity.Critic;
import com.cardboardcritic.db.entity.Game;
import com.cardboardcritic.db.entity.Outlet;
import com.cardboardcritic.db.entity.RawReview;
import com.cardboardcritic.db.entity.Review;
import com.cardboardcritic.db.repository.CriticRepository;
import com.cardboardcritic.db.repository.GameRepository;
import com.cardboardcritic.db.repository.OutletRepository;
import com.cardboardcritic.db.repository.RawReviewRepository;
import com.cardboardcritic.db.repository.ReviewRepository;
import com.cardboardcritic.web.template.form.RawReviewEditForm;
import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import io.smallrye.mutiny.Uni;
import org.jboss.resteasy.reactive.RestPath;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;

@Path("raw")
public class RawReviewResource {
    private final RawReviewRepository rawReviewRepo;
    private final ReviewRepository reviewRepo;
    private final GameRepository gameRepo;
    private final CriticRepository criticRepo;
    private final OutletRepository outletRepo;

    @Inject
    RawReviewMapper rawReviewMapper;

    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance edit(RawReviewEditForm review);

        public static native TemplateInstance list(List<RawReview> reviews);


        public static native TemplateInstance index(List<RawReview> reviews);
    }

    public RawReviewResource(RawReviewRepository rawReviewRepo,
                             ReviewRepository reviewRepo,
                             GameRepository gameRepo,
                             CriticRepository criticRepo,
                             OutletRepository outletRepo) {
        this.rawReviewRepo = rawReviewRepo;
        this.reviewRepo = reviewRepo;
        this.gameRepo = gameRepo;
        this.criticRepo = criticRepo;
        this.outletRepo = outletRepo;
    }

    @Transactional
    @GET
    @Produces(MediaType.TEXT_HTML)
    public Uni<TemplateInstance> index() {
        return rawReviewRepo.listAll().map(Templates::index);
    }

    @Transactional
    @GET
    @Path("{id}/edit")
    @Produces(MediaType.TEXT_HTML)
    public Uni<TemplateInstance> edit(@RestPath long id) {
        return rawReviewRepo.findById(id)
                .map(rawReviewMapper::toForm)
                .map(Templates::edit);
    }

    @POST
    @Path("{id}/edit")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.TEXT_HTML)
    @ReactiveTransactional
    public Uni<Response> save(@RestPath long id, RawReviewEditForm rawReview) {
        final Uni<Game> game$ = gameRepo.findOrCreateByName(rawReview.game);
        final Uni<Critic> critic$ = criticRepo.findOrCreateByName(rawReview.critic);
        final Uni<Outlet> outlet$ = outletRepo.findOrCreateByName(rawReview.outlet);

        return Uni.combine().all().unis(game$, critic$, outlet$)
                .combinedWith((game, critic, outlet) ->
                        new Review()
                                .setGame(game)
                                .setCritic(critic)
                                .setOutlet(outlet)
                                .setScore(rawReview.score)
                                .setSummary(rawReview.summary)
                                .setUrl(rawReview.url)
                                .setRecommended(rawReview.recommended))
                .flatMap(reviewRepo::persist)
                .flatMap(x -> rawReviewRepo.update("processed = true where id = ?1", id))
                .map(x -> Response.status(302)
                        .location(URI.create("/raw"))
                        .build());
    }

    @POST
    @Path("{id}/deny")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.TEXT_HTML)
    @ReactiveTransactional
    public Uni<Response> deny(@RestPath long id) {
        return rawReviewRepo.update("processed = true where id = ?1", id)
                .map(x -> Response.status(302)
                        .location(URI.create("/raw"))
                        .build());
    }
}
