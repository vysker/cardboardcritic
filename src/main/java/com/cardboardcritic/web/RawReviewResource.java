package com.cardboardcritic.web;

import com.cardboardcritic.db.entity.Critic;
import com.cardboardcritic.db.entity.Game;
import com.cardboardcritic.db.entity.Outlet;
import com.cardboardcritic.db.entity.Review;
import com.cardboardcritic.db.repository.CriticRepository;
import com.cardboardcritic.db.repository.GameRepository;
import com.cardboardcritic.db.repository.OutletRepository;
import com.cardboardcritic.db.repository.RawReviewRepository;
import com.cardboardcritic.db.repository.ReviewRepository;
import com.cardboardcritic.web.template.data.RawReviewEditData;
import com.cardboardcritic.web.template.data.RawReviewListData;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import io.quarkus.qute.api.ResourcePath;
import org.jboss.resteasy.annotations.Form;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/raw")
public class RawReviewResource {
    private final RawReviewRepository rawReviewRepo;
    private final ReviewRepository reviewRepo;
    private final GameRepository gameRepo;
    private final CriticRepository criticRepo;
    private final OutletRepository outletRepo;

    @Inject
    @ResourcePath("raw-review-edit.html")
    Template rawReviewEdit;

    @Inject
    @ResourcePath("raw-review-list.html")
    Template rawReviewList;

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
        var data = new RawReviewListData(rawReviews);
        return rawReviewList.data(data);
    }

    @Transactional
    @GET
    @Path("/{id}")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance edit(@PathParam("id") long id) {
        return rawReviewEdit.data(rawReviewRepo.findById(id));
    }

    @Transactional
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance save(@Form RawReviewEditData rawReview) {
        Game game = gameRepo.findOrCreateByName(rawReview.getGame());
        Critic critic = criticRepo.findOrCreateByName(rawReview.getCritic());
        Outlet outlet = outletRepo.findOrCreateByName(rawReview.getOutlet());

        var review = Review.builder()
                .game(game)
                .critic(critic)
                .outlet(outlet)
                .score(rawReview.getScore())
                .summary(rawReview.getSummary())
                .url(rawReview.getUrl())
                .recommended(rawReview.isRecommended())
                .build();
        reviewRepo.persistAndFlush(review);
        rawReviewRepo.update("processed = true where id = ?", rawReview.getId());

        return rawReviewEdit.data(rawReview);
    }
}
