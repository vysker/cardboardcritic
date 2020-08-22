package com.cardboardcritic.web

import com.cardboardcritic.db.entity.Critic
import com.cardboardcritic.db.entity.Game
import com.cardboardcritic.db.entity.Outlet
import com.cardboardcritic.db.entity.Review
import com.cardboardcritic.db.repository.CriticRepository
import com.cardboardcritic.db.repository.GameRepository
import com.cardboardcritic.db.repository.OutletRepository
import com.cardboardcritic.db.repository.ReviewRepository
import com.cardboardcritic.web.template.data.RawReviewEditData
import io.quarkus.qute.Template
import io.quarkus.qute.TemplateInstance
import io.quarkus.qute.api.ResourcePath
import org.jboss.resteasy.annotations.Form

import javax.inject.Inject
import javax.transaction.Transactional
import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path('/review')
class ReviewResource {

    @Inject
    @ResourcePath('raw-review-edit.html')
    Template rawReviewEdit

    @Inject
    ReviewRepository reviewRepo

    @Inject
    GameRepository gameRepo

    @Inject
    CriticRepository criticRepo

    @Inject
    OutletRepository outletRepo

    @Transactional
    @GET
    @Produces(MediaType.TEXT_HTML)
    TemplateInstance editRaw() {
        def data = new RawReviewEditData()
        rawReviewEdit.data data
    }

    @Transactional
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    TemplateInstance saveRaw(@Form RawReviewEditData rawReview) {
        Game game = gameRepo.findOrCreateByName rawReview.game
        Critic critic = criticRepo.findOrCreateByName rawReview.critic
        Outlet outlet = outletRepo.findOrCreateByName rawReview.outlet

        def review = new Review(
                game: game,
                critic: critic,
                outlet: outlet,
                score: rawReview.score,
                summary: rawReview.summary,
                link: rawReview.url,
                recommended: rawReview.recommended
        )

        reviewRepo.persistAndFlush review
        rawReviewEdit.data rawReview
    }
}
