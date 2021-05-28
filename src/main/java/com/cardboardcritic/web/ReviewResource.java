package com.cardboardcritic.web;

import com.cardboardcritic.data.RawReviewMapper;
import com.cardboardcritic.data.ReviewMapper;
import com.cardboardcritic.db.entity.RawReview;
import com.cardboardcritic.db.entity.Review;
import com.cardboardcritic.db.repository.ReviewRepository;
import com.cardboardcritic.web.template.form.RawReviewEditForm;
import io.quarkus.qute.TemplateInstance;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("review")
public class ReviewResource {
    private final ReviewRepository reviewRepo;

    @Inject
    ReviewMapper reviewMapper;

    @Inject
    RawReviewMapper rawReviewMapper;

    public ReviewResource(ReviewRepository reviewRepo) {
        this.reviewRepo = reviewRepo;
    }

    @Transactional
    @GET
    @Path("{id}")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance edit(@PathParam long id) {
        Review review = reviewRepo.findById(id);

        // TODO create non-raw-review edit page
        RawReview rawReview = reviewMapper.toRawReview(review);
        RawReviewEditForm rawReviewEditForm = rawReviewMapper.toTemplateData(rawReview);
        return RawReviewResource.Templates.edit(rawReviewEditForm);
    }
}
