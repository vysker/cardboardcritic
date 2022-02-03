package com.cardboardcritic.web;

import com.cardboardcritic.data.ReviewMapper;
import com.cardboardcritic.db.entity.Review;
import com.cardboardcritic.db.repository.CriticRepository;
import com.cardboardcritic.db.repository.GameRepository;
import com.cardboardcritic.db.repository.OutletRepository;
import com.cardboardcritic.db.repository.ReviewRepository;
import com.cardboardcritic.web.template.form.ReviewEditForm;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import io.smallrye.mutiny.Uni;
import org.jboss.resteasy.reactive.RestPath;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
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
    public Uni<TemplateInstance> edit(@RestPath long id) {
        return reviewRepo.findById(id)
                .map(reviewMapper::toForm)
                .map(Templates::edit);
    }

    @Transactional
    @POST // html forms only support POST
    @Path("{id}/edit")
    @Produces(MediaType.TEXT_HTML)
    public Uni<Response> save(@RestPath long id, @BeanParam ReviewEditForm reviewForm) {
//        return Uni.createFrom().item(Response.status(302).location(URI.create("/")).build()).await().indefinitely();
        return reviewRepo.findById(id)
                .chain(review -> gameRepo.findOrCreateByName(reviewForm.getGame())
                        .chain(game -> criticRepo.findOrCreateByName(reviewForm.getCritic())
                                .chain(critic -> outletRepo.findOrCreateByName(reviewForm.getOutlet())
                                        .map(outlet -> review.setGame(game)
                                                .setCritic(critic)
                                                .setOutlet(outlet)
                                                .setScore(reviewForm.getScore())
                                                .setSummary(reviewForm.getSummary())
                                                .setUrl(reviewForm.getUrl())
                                                .setRecommended(reviewForm.isRecommended())))))
                .flatMap(reviewRepo::persistAndFlush)
                .map(review -> Response.seeOther(getRedirectUri(review)).build());
//        return Uni.combine().all()
//                .unis(gameRepo.findOrCreateByName(reviewForm.getGame()),
//                        criticRepo.findOrCreateByName(reviewForm.getCritic()),
//                        outletRepo.findOrCreateByName(reviewForm.getOutlet()),
//                        reviewRepo.findById(id))
//                .combinedWith((game, critic, outlet, review) ->
//                        review.setGame(game)
//                                .setCritic(critic)
//                                .setOutlet(outlet)
//                                .setScore(reviewForm.getScore())
//                                .setSummary(reviewForm.getSummary())
//                                .setUrl(reviewForm.getUrl())
//                                .setRecommended(reviewForm.isRecommended()))
//                .flatMap(r -> reviewRepo.getSession().flatMap(s -> s.merge(r)))
////                .flatMap(reviewRepo::persistAndFlush)
//                .map(review -> Response.seeOther(getRedirectUri(review)).build());
    }

    private URI getRedirectUri(Review review) {
//        return URI.create("/game/" + review.getGame().getSlug());
        return URI.create("/");
    }
}
