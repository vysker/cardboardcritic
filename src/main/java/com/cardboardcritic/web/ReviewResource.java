package com.cardboardcritic.web;

import com.cardboardcritic.data.ReviewMapper;
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

import javax.inject.Inject;
import javax.ws.rs.Consumes;
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

    @GET
    @Path("{id}/edit")
    @Produces(MediaType.TEXT_HTML)
    @ReactiveTransactional
    public Uni<TemplateInstance> edit(@RestPath long id) {
        return reviewRepo.findById(id)
                .map(reviewMapper::toForm)
                .map(Templates::edit);
    }

    @POST // Should be PATCH, but HTML forms only support POST
    @Path("{id}/edit")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.TEXT_HTML)
    @ReactiveTransactional
    public Uni<Response> save(@RestPath long id, ReviewEditForm reviewForm) {
        return reviewRepo.findById(id)
                .chain(review -> gameRepo.findOrCreateByName(reviewForm.game)
                        .chain(game -> criticRepo.findOrCreateByName(reviewForm.critic)
                                .chain(critic -> outletRepo.findOrCreateByName(reviewForm.outlet)
                                        .map(outlet -> review.setGame(game)
                                                .setCritic(critic)
                                                .setOutlet(outlet)
                                                .setScore(reviewForm.score)
                                                .setSummary(reviewForm.summary)
                                                .setUrl(reviewForm.url)
                                                .setRecommended(reviewForm.recommended)))))
                .flatMap(reviewRepo::persist)
                .map(review -> Response.seeOther(getRedirectUri(review)).build());
    }

    private URI getRedirectUri(Review review) {
        return URI.create("/game/" + review.getGame().getSlug());
    }
}
