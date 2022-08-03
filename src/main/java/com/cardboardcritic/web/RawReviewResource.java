package com.cardboardcritic.web;

import com.cardboardcritic.config.GlobalTemplateExtensions;
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
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Path("raw")
@RolesAllowed("admin")
public class RawReviewResource {
    private static final Logger log = Logger.getLogger(RawReviewResource.class);

    private final RawReviewRepository rawReviewRepo;
    private final ReviewRepository reviewRepo;
    private final GameRepository gameRepo;
    private final CriticRepository criticRepo;
    private final OutletRepository outletRepo;

    @Inject
    RawReviewMapper rawReviewMapper;

    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance edit(RawReviewEditForm review,
                                                   List<Game> games,
                                                   List<Critic> critics,
                                                   List<Outlet> outlets);

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

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance index() {
        return Templates.index(rawReviewRepo.listAll());
    }

    @GET
    @Path("{id}/edit")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance edit(@PathParam long id) {
        final RawReview rawReview = rawReviewRepo.findById(id);
        final List<Game> games = gameRepo.listAll();
        final List<Critic> critics = criticRepo.listAll();
        final List<Outlet> outlets = outletRepo.listAll();

        return Templates.edit(rawReviewMapper.toForm(rawReview), games, critics, outlets);
    }

    @POST // Should be PATCH, but HTML forms only support POST
    @Path("{id}/edit")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    @Transactional
    public Response save(@PathParam long id, @BeanParam RawReviewEditForm form) {
        final Game game = gameRepo.createNewOrFindExisting(form.newGame, form.game);
        final Critic critic = criticRepo.createNewOrFindExisting(form.newCritic, form.critic);
        final Outlet outlet = outletRepo.createNewOrFindExisting(form.newOutlet, form.outlet);

        final List<String> errors = Stream.of(
                game == null ? "No game was provided. Please select or create a game" : null,
                critic == null ? "No critic was provided. Please select or create a critic" : null,
                outlet == null ? "No outlet was provided. Please select or create a outlet" : null
        ).filter(Objects::nonNull).toList();

        if (!errors.isEmpty()) {
            gameRepo.flush();
            criticRepo.flush();
            outletRepo.flush();

            final TemplateInstance template = edit(id);
            template.setAttribute(GlobalTemplateExtensions.ERRORS_ATTRIBUTE, errors);

            return Response.ok(template, MediaType.TEXT_HTML).build();
        }

        final var review = new Review()
                .setGame(game)
                .setCritic(critic)
                .setOutlet(outlet)
                .setScore(form.score)
                .setSummary(form.summary)
                .setUrl(form.url)
                .setRecommended(form.recommended);
        reviewRepo.persist(review);
        rawReviewRepo.update("processed = true where id = ?1", id);

        return Response.seeOther(URI.create("/raw")).build();
    }

    @POST
    @Path("{id}/deny")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    @Transactional
    public Response deny(@PathParam long id) {
        rawReviewRepo.update("processed = true where id = ?1", id);
        return Response.seeOther(URI.create("/raw")).build();
    }
}
