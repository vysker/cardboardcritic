package com.cardboardcritic.web;

import com.cardboardcritic.data.RawReviewMapper;
import com.cardboardcritic.db.entity.RawReview;
import com.cardboardcritic.db.repository.*;
import com.cardboardcritic.web.template.form.RawReviewEditForm;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import io.smallrye.mutiny.Uni;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
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
        return rawReviewRepo.listAll().map(Templates::list);
    }

//    @Transactional
//    @GET
//    @Path("{id}/edit")
//    @Produces(MediaType.TEXT_HTML)
//    public TemplateInstance edit(@PathParam("id") long id) {
//        final RawReview rawReview = rawReviewRepo.findById(id);
//        final RawReviewEditForm rawReviewEditForm = rawReviewMapper.toForm(rawReview);
//        return Templates.edit(rawReviewEditForm);
//    }
//
//    @Transactional
//    @POST
//    @Path("{id}")
//    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
//    @Produces(MediaType.TEXT_HTML)
//    public Response save(@PathParam("id") long id, @Form RawReviewEditForm rawReview) {
//        final Game game = gameRepo.findOrCreateByName(rawReview.getGame());
//        final Critic critic = criticRepo.findOrCreateByName(rawReview.getCritic());
//        final Outlet outlet = outletRepo.findOrCreateByName(rawReview.getOutlet());
//
//        final var review = new Review()
//                .setGame(game)
//                .setCritic(critic)
//                .setOutlet(outlet)
//                .setScore(rawReview.getScore())
//                .setSummary(rawReview.getSummary())
//                .setUrl(rawReview.getUrl())
//                .setRecommended(rawReview.isRecommended());
//        reviewRepo.persistAndFlush(review);
//        rawReviewRepo.update("processed = true where id = ?1", id);
//
//        return Response.status(302)
//                .location(URI.create("/raw"))
//                .build();
//    }
//
//    @Transactional
//    @POST
//    @Path("{id}/deny")
//    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
//    @Produces(MediaType.TEXT_HTML)
//    public Response deny(@PathParam("id") long id) {
//        rawReviewRepo.update("processed = true where id = ?1", id);
//
//        return Response.status(302)
//                .location(URI.create("/raw"))
//                .build();
//    }
}
