package com.cardboardcritic.web;

import com.cardboardcritic.db.entity.Game;
import com.cardboardcritic.db.repository.GameRepository;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;

import javax.annotation.security.PermitAll;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("browse")
@PermitAll
public class BrowseResource {
    private final GameRepository gameRepository;

    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance recent(List<Game> games);

        public static native TemplateInstance search(List<Game> games);
    }

    public BrowseResource(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @GET
    @Path("recent")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance recent() {
        return Templates.recent(gameRepository.recent());
    }

    @GET
    @Path("search")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance recent(@QueryParam("q") String name) {
        final List<Game> matches = gameRepository.getEntityManager()
                .createNativeQuery("""
                        select *
                        from game
                        where to_tsvector('english', name) @@ plainto_tsquery('english', :name)
                        """.stripIndent(), Game.class)
                .setParameter("name", name)
                .getResultList();
        return Templates.search(matches);
    }
}
