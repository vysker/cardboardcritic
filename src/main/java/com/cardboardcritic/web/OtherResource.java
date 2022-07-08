package com.cardboardcritic.web;

import com.cardboardcritic.db.entity.Game;
import com.cardboardcritic.service.GameService;
import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.groups.UniZip;

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
        public static native TemplateInstance home(List<Game> recent, List<Game> topOfYear);

        public static native TemplateInstance about();
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @ReactiveTransactional
    public Uni<TemplateInstance> home() {
        final Uni<List<Game>> recent = gameService.recent();
        final Uni<List<Game>> topOfYear = gameService.topOfYear();
        return Uni.combine().all().unis(recent, topOfYear)
                .combinedWith(Templates::home);
    }

    @GET
    @Path("about")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance about() {
        return Templates.about().data(Collections.emptyMap());
    }
}
