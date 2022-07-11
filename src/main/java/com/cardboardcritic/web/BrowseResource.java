package com.cardboardcritic.web;

import com.cardboardcritic.db.entity.Game;
import com.cardboardcritic.db.repository.GameRepository;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import io.smallrye.mutiny.Uni;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("browse")
public class BrowseResource {
    private final GameRepository gameRepository;

    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance recent(List<Game> games);
    }

    public BrowseResource(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @GET
    @Path("recent")
    @Produces(MediaType.TEXT_HTML)
    public Uni<TemplateInstance> recent() {
        return gameRepository.recent()
                .map(Templates::recent);
    }
}
