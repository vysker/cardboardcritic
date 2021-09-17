package com.cardboardcritic.web;

import com.cardboardcritic.db.entity.Game;
import com.cardboardcritic.service.GameService;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Collections;
import java.util.List;

@Path("")
public class OtherResource {
    private final GameService gameService;

    public OtherResource(GameService gameService) {
        this.gameService = gameService;
    }

    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance home(List<Game> games);

        public static native TemplateInstance about();
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance home() {
        final List<Game> games = gameService.recent();
        return Templates.home(games);
    }

    @GET
    @Path("about")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance about() {
        return Templates.about().data(Collections.emptyMap());
    }
}
