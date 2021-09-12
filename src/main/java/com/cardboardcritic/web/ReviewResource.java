package com.cardboardcritic.web;

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
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import org.jboss.resteasy.annotations.Form;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

@Path("review")
public class ReviewResource {
    private final ReviewRepository reviewRepo;
    private final GameRepository gameRepo;
    private final CriticRepository criticRepo;
    private final OutletRepository outletRepo;

    @Inject
    ReviewMapper reviewMapper;

    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance edit(ReviewEditForm review);

        public static native TemplateInstance formFields(ReviewEditForm review);
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

    @Transactional
    @GET
    @Path("{id}/edit")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance edit(@PathParam long id) {
        final Review review = reviewRepo.findById(id);
        final ReviewEditForm reviewEditForm = reviewMapper.toForm(review);
        return Templates.edit(reviewEditForm);
    }

    @Transactional
    @POST // html forms only support POST
    @Path("{id}/edit")
    @Produces(MediaType.TEXT_HTML)
    public Response save(@PathParam("id") long id, @Form ReviewEditForm reviewForm) {
        final Game game = gameRepo.findOrCreateByName(reviewForm.getGame());
        final Critic critic = criticRepo.findOrCreateByName(reviewForm.getCritic());
        final Outlet outlet = outletRepo.findOrCreateByName(reviewForm.getOutlet());

        final Review review = reviewRepo.findById(id);
        review.setGame(game)
                .setCritic(critic)
                .setOutlet(outlet)
                .setScore(reviewForm.getScore())
                .setSummary(reviewForm.getSummary())
                .setUrl(reviewForm.getUrl())
                .setRecommended(reviewForm.isRecommended());
        reviewRepo.persistAndFlush(review);

        return Response.status(302)
                .location(URI.create("/game/" + game.getSlug()))
                .build();
    }
}
