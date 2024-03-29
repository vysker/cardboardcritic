package com.cardboardcritic.web;

import com.cardboardcritic.data.GameMapper;
import com.cardboardcritic.db.entity.Game;
import com.cardboardcritic.db.entity.Review;
import com.cardboardcritic.db.repository.GameRepository;
import com.cardboardcritic.util.StringUtil;
import com.cardboardcritic.web.template.form.GameEditForm;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
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

        public static native TemplateInstance edit(GameEditForm game, List<String> designers, List<String> publishers);
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
        final Game game = gameRepo.findBySlug(slug);

        final List<Review> reviews = game.getReviews().stream()
                .filter(Review::isPublished)
                .sorted(Comparator.comparing(Review::getScore).reversed()) // Desc
                .toList();
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
        final List<String> publishers = gameRepo.listAll().stream()
                .map(Game::getPublisher)
                .filter(Objects::nonNull)
                .distinct()
                .sorted(Comparator.comparing(publisher -> publisher, Comparator.naturalOrder()))
                .toList();

        return Templates.edit(gameMapper.toForm(game), designers, publishers);
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
        final String publisher = StringUtil.isNotEmpty(form.newPublisher) ? form.newPublisher : form.publisher;
        game.setPublisher(publisher);

        gameRepo.persist(game);
        return Response.seeOther(getEditLink(id)).build();
    }

    private URI getEditLink(int id) {
        return URI.create("/game/" + id + "/edit");
    }
}
