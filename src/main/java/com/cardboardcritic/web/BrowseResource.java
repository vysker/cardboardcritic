package com.cardboardcritic.web;

import com.cardboardcritic.db.entity.Game;
import com.cardboardcritic.service.GameService;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("browse")
public class BrowseResource {
    private final GameService gameService;

    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance recent(List<Game> games);
    }

    public BrowseResource(GameService gameService) {
        this.gameService = gameService;
    }

    @GET
    @Path("recent")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance recent() {
        final List<Game> games = gameService.recent();
        return Templates.recent(games);
    }
}
