package com.cardboardcritic.web;

import com.cardboardcritic.db.entity.Game;
import com.cardboardcritic.db.entity.Review;
import com.cardboardcritic.db.repository.GameRepository;
import com.cardboardcritic.web.template.data.GameData;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Comparator;

@Path("/game")
public class GameResource {
    private final GameRepository gameRepo;

    @Inject
    Template game;

    public GameResource(GameRepository gameRepo) {
        this.gameRepo = gameRepo;
    }

    @GET
    @Path("/{slug}")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance game(@PathParam("slug") String slug) {
        Game game1 = gameRepo.findBySlug(slug);
        game1.getReviews().sort(Comparator.comparing(Review::getScore).reversed()); // desc
        var data = new GameData(game1);
        return game.data(data);
    }
}
