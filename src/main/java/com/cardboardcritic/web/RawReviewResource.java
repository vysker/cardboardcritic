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
import com.cardboardcritic.feed.CrawlerService;
import com.cardboardcritic.feed.crawler.OutletCrawler;
import com.cardboardcritic.util.StringUtil;
import com.cardboardcritic.web.template.form.RawReviewEditForm;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Path("raw-review")
@RolesAllowed("admin")
public class RawReviewResource {
    private final RawReviewRepository rawReviewRepo;
    private final ReviewRepository reviewRepo;
    private final GameRepository gameRepo;
    private final CriticRepository criticRepo;
    private final OutletRepository outletRepo;
    private final CrawlerService crawlerService;

    @Inject
    RawReviewMapper rawReviewMapper;

    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance edit(RawReviewEditForm review,
                                                   List<Game> games,
                                                   List<Critic> critics,
                                                   List<Outlet> outlets);

        public static native TemplateInstance list(List<RawReview> reviews,
                                                   List<Game> games,
                                                   List<Critic> critics,
                                                   List<Outlet> outlets,
                                                   Map<String, String> filters);


        public static native TemplateInstance index(List<RawReview> reviews,
                                                    List<Game> games,
                                                    List<Critic> critics,
                                                    List<Outlet> outlets,
                                                    Map<String, String> filters);
    }

    public RawReviewResource(RawReviewRepository rawReviewRepo,
                             ReviewRepository reviewRepo,
                             GameRepository gameRepo,
                             CriticRepository criticRepo,
                             OutletRepository outletRepo,
                             CrawlerService crawlerService) {
        this.rawReviewRepo = rawReviewRepo;
        this.reviewRepo = reviewRepo;
        this.gameRepo = gameRepo;
        this.criticRepo = criticRepo;
        this.outletRepo = outletRepo;
        this.crawlerService = crawlerService;
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance index(@QueryParam("game") Optional<String> gameFilter,
                                  @QueryParam("outlet") Optional<String> outletFilter,
                                  @QueryParam("critic") Optional<String> criticFilter,
                                  @QueryParam("status") Optional<String> statusFilter) {
        final PanacheQuery<RawReview> rawReviewQuery = rawReviewRepo.findAll();

        gameFilter.filter(StringUtil::isNotEmpty)
                .ifPresent(name -> rawReviewQuery.filter("RawReview.byGame", Parameters.with("name", name)));
        criticFilter.filter(StringUtil::isNotEmpty)
                .ifPresent(name -> rawReviewQuery.filter("RawReview.byCritic", Parameters.with("name", name)));
        outletFilter.filter(StringUtil::isNotEmpty)
                .ifPresent(name -> rawReviewQuery.filter("RawReview.byOutlet", Parameters.with("name", name)));
        statusFilter.filter(StringUtil::isNotEmpty)
                .ifPresent(status -> {
                    if ("done".equals(status))
                        rawReviewQuery.filter("RawReview.byProcessed", Parameters.with("value", true));
                    if ("todo".equals(status))
                        rawReviewQuery.filter("RawReview.byProcessed", Parameters.with("value", false));
                });

        final Map<String, String> filters = Map.of(
                "game", gameFilter.orElse(""),
                "critic", criticFilter.orElse(""),
                "outlet", outletFilter.orElse(""),
                "status", statusFilter.orElse("todo")
        );

        return Templates.index(rawReviewQuery.list(),
                gameRepo.listAll(Sort.by("name")),
                criticRepo.listAll(Sort.by("name")),
                outletRepo.listAll(Sort.by("name")),
                filters);
    }

    @GET
    @Path("{id}/edit")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance edit(@PathParam int id) {
        final RawReview rawReview = rawReviewRepo.findById(id);
        final List<Game> games = gameRepo.listAll(Sort.by("name"));
        final List<Critic> critics = criticRepo.listAll(Sort.by("name"));
        final List<Outlet> outlets = outletRepo.listAll(Sort.by("name"));

        return Templates.edit(rawReviewMapper.toForm(rawReview), games, critics, outlets);
    }

    @POST // Should be PATCH, but HTML forms only support POST
    @Path("{id}/edit")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    @Transactional
    public Response save(@PathParam int id, @BeanParam RawReviewEditForm form) {
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

            final TemplateInstance template = edit(id).setAttribute(GlobalTemplateExtensions.ERRORS_ATTRIBUTE, errors);
            return Response.ok(template, MediaType.TEXT_HTML).location(getEditLink(id)).build();
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

        return Response.seeOther(URI.create("/raw-review")).build();
    }

    @POST
    @Path("{id}/deny")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    @Transactional
    public Response deny(@PathParam int id) {
        rawReviewRepo.update("processed = true where id = ?1", id);
        return Response.seeOther(URI.create("/raw-review")).build();
    }

    @POST
    @Path("{id}/re-scrape")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    @Transactional
    public Response reScrape(@PathParam int id) {
        final RawReview review = rawReviewRepo.findById(id);
        final String outlet = review.getOutlet();

        if (StringUtil.isEmpty(outlet)) {
            final TemplateInstance template = edit(id).setAttribute(GlobalTemplateExtensions.ERRORS_ATTRIBUTE,
                    "Cannot re-scrape; this review has no outlet, so we cannot determine which scraper to use");
            return Response.ok(template, MediaType.TEXT_HTML).location(getEditLink(id)).build();
        }

        final Optional<OutletCrawler> crawler = crawlerService.getCrawlerByOutlet(outlet);
        if (crawler.isEmpty()) {
            final TemplateInstance template = edit(id).setAttribute(GlobalTemplateExtensions.ERRORS_ATTRIBUTE,
                    "Cannot re-scrape; no outlet crawler found for outlet with name '%s'".formatted(outlet));
            return Response.ok(template, MediaType.TEXT_HTML).location(getEditLink(id)).build();
        }

        final RawReview reScrapedReview = crawlerService.getReview(crawler.get(), review.getUrl());
        if (reScrapedReview == null) {
            final TemplateInstance template = edit(id).setAttribute(GlobalTemplateExtensions.ERRORS_ATTRIBUTE,
                    "Cannot re-scrape; failed to scrape the article");
            return Response.ok(template, MediaType.TEXT_HTML).location(getEditLink(id)).build();
        }

        review
                .setGame(reScrapedReview.getGame())
                .setCritic(reScrapedReview.getCritic())
                .setOutlet(reScrapedReview.getOutlet())
                .setSummary(reScrapedReview.getSummary())
                .setUrl(reScrapedReview.getUrl())
                .setTitle(reScrapedReview.getTitle())
                .setContent(reScrapedReview.getContent())
                .setDate(reScrapedReview.getDate())
                .setScore(reScrapedReview.getScore())
                .setRecommended(reScrapedReview.isRecommended())
                .setProcessed(reScrapedReview.isProcessed());
        rawReviewRepo.persist(review);

        return Response.seeOther(getEditLink(id)).build();
    }

    private URI getEditLink(int id) {
        return URI.create("/raw-review/" + id + "/edit");
    }
}
