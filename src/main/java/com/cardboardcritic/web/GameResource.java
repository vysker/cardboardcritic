package com.cardboardcritic.web;

import com.cardboardcritic.db.entity.Critic;
import com.cardboardcritic.db.entity.Game;
import com.cardboardcritic.db.entity.Review;
import com.cardboardcritic.db.repository.GameRepository;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import io.smallrye.mutiny.Uni;
import org.hibernate.reactive.mutiny.Mutiny;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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
    public Uni<TemplateInstance> game(@PathParam("slug") String slug) {
        // FIXME http://hibernate.org/reactive/documentation/1.0/reference/html_single/#_fetching_lazy_associations
        return gameRepo.findBySlug(slug)
                .flatMap(game -> Mutiny.fetch(game.getReviews()).map(reviews -> {
                    reviews.sort(Comparator.comparing(Review::getScore).reversed()); // Desc
                    reviews.forEach(review -> Mutiny.fetch(Critic.class));
                    game.setReviews(reviews);
                    return game;
                }))
                .map(Templates::game);
    }
}
