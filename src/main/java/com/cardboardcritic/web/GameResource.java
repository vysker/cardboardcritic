package com.cardboardcritic.web;

import com.cardboardcritic.data.GameMapper;
import com.cardboardcritic.db.entity.Game;
import com.cardboardcritic.db.entity.Review;
import com.cardboardcritic.db.repository.GameRepository;
import com.cardboardcritic.util.StringUtil;
import com.cardboardcritic.web.template.form.GameEditForm;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.transaction.Transactional;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Path("game")
public class GameResource {
    private final GameRepository gameRepo;
    private final GameMapper gameMapper;

    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance game(Game game);

        public static native TemplateInstance edit(GameEditForm game, List<String> designers);
    }

    public GameResource(GameRepository gameRepo, GameMapper gameMapper) {
        this.gameRepo = gameRepo;
        this.gameMapper = gameMapper;
    }

    @GET
    @Path("{slug}")
    @Produces(MediaType.TEXT_HTML)
    @PermitAll
    public TemplateInstance game(@PathParam("slug") String slug) {
        // FIXME: http://hibernate.org/reactive/documentation/1.0/reference/html_single/#_fetching_lazy_associations
        final Game game = gameRepo.findBySlug(slug);

        final List<Review> reviews = game.getReviews();
        reviews.sort(Comparator.comparing(Review::getScore).reversed()); // Desc
        game.setReviews(reviews);

        return Templates.game(game);
    }

    @GET
    @Path("{id}/edit")
    @Produces(MediaType.TEXT_HTML)
    @RolesAllowed("admin")
    public TemplateInstance edit(@PathParam("id") int id) {
        final Game game = gameRepo.findById(id);

        final List<String> designers = gameRepo.listAll().stream()
                .map(Game::getDesigner)
                .filter(Objects::nonNull)
                .distinct()
                .sorted(Comparator.comparing(designer -> designer, Comparator.naturalOrder()))
                .toList();

        return Templates.edit(gameMapper.toForm(game), designers);
    }

    @POST // Should be PATCH, but HTML forms only support POST
    @Path("{id}/edit")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    @Transactional
    @RolesAllowed("admin")
    public Response save(@PathParam("id") int id, @BeanParam GameEditForm form) {
        final Game game = gameRepo.findById(id);

        game.setName(form.name);
        game.setShortDescription(form.shortDescription);
        game.setDescription(form.description);
        game.setSlug(form.slug);
        game.setReleaseDate(LocalDate.parse(form.releaseDate, DateTimeFormatter.ISO_DATE));

        final String designer = StringUtil.isNotEmpty(form.newDesigner) ? form.newDesigner : form.designer;
        game.setDesigner(designer);

        gameRepo.persist(game);
        return Response.seeOther(getEditLink(id)).build();
    }

    private URI getEditLink(int id) {
        return URI.create("/game/" + id + "/edit");
    }
}
