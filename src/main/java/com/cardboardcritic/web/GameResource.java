package com.cardboardcritic.web;

import com.cardboardcritic.db.entity.Game;
import com.cardboardcritic.db.entity.Review;
import com.cardboardcritic.db.repository.GameRepository;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Comparator;

@Path("game")
public class GameResource {
    private final GameRepository gameRepo;

    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance game(Game game);
    }

    public GameResource(GameRepository gameRepo) {
        this.gameRepo = gameRepo;
    }

    @GET
    @Path("{slug}")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance game(@PathParam("slug") String slug) {
        final Game game = gameRepo.findBySlug(slug);
        game.getReviews().sort(Comparator.comparing(Review::getScore).reversed()); // desc
        return Templates.game(game);
    }
}
