package com.cardboardcritic.web;

import com.cardboardcritic.db.entity.Game;
import com.cardboardcritic.db.repository.GameRepository;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("browse")
public class BrowseResource {
    private final GameRepository gameRepo;

    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance recent(List<Game> games);
    }

    public BrowseResource(GameRepository gameRepo) {
        this.gameRepo = gameRepo;
    }

    @GET
    @Path("recent")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance recent() {
        final List<Game> games = gameRepo.listAll();
        return Templates.recent(games);
    }
}
