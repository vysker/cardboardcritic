package com.cardboardcritic.web;

import com.cardboardcritic.db.entity.Review;
import com.cardboardcritic.db.repository.ReviewRepository;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import io.quarkus.qute.api.ResourcePath;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/review")
public class ReviewResource {
    private final ReviewRepository reviewRepo;

    @Inject
    @ResourcePath("review-edit.html")
    Template reviewEdit;

    public ReviewResource(ReviewRepository reviewRepo) {
        this.reviewRepo = reviewRepo;
    }

    @Transactional
    @GET
    @Path("/{id}")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance edit(@PathParam long id) {
        Review review = reviewRepo.findById(id);
        return reviewEdit.data(review);
    }
}
