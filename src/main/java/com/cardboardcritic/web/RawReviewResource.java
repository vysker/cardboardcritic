package com.cardboardcritic.web;

import com.cardboardcritic.data.RawReviewMapper;
import com.cardboardcritic.db.entity.*;
import com.cardboardcritic.db.repository.*;
import com.cardboardcritic.web.template.form.RawReviewEditForm;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import org.jboss.resteasy.annotations.Form;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
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
    public TemplateInstance index() {
        var rawReviews = rawReviewRepo.listAll();
        return Templates.list(rawReviews);
    }

    @Transactional
    @GET
    @Path("{id}")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance edit(@PathParam("id") long id) {
        RawReview rawReview = rawReviewRepo.findById(id);
        RawReviewEditForm rawReviewEditForm = rawReviewMapper.toTemplateData(rawReview);
        return Templates.edit(rawReviewEditForm);
    }

    @Transactional
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    public Response save(@Form RawReviewEditForm rawReview) {
        final Game game = gameRepo.findOrCreateByName(rawReview.getGame());
        final Critic critic = criticRepo.findOrCreateByName(rawReview.getCritic());
        final Outlet outlet = outletRepo.findOrCreateByName(rawReview.getOutlet());

        var review = new Review()
                .setGame(game)
                .setCritic(critic)
                .setOutlet(outlet)
                .setScore(rawReview.getScore())
                .setSummary(rawReview.getSummary())
                .setUrl(rawReview.getUrl())
                .setRecommended(rawReview.isRecommended());
        reviewRepo.persistAndFlush(review);
        rawReviewRepo.update("processed = true where id = ?1", rawReview.getId());

        return Response.status(302)
                .location(URI.create("/raw"))
                .build();
    }
}