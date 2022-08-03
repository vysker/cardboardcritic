package com.cardboardcritic.web;

import com.cardboardcritic.db.entity.Game;
import com.cardboardcritic.db.repository.GameRepository;
import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import io.smallrye.mutiny.Uni;

import javax.annotation.security.PermitAll;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Collections;
import java.util.List;

@Path("")
@PermitAll
public class OtherResource {
    private final GameRepository gameRepository;

    public OtherResource(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance home(List<Game> recent, List<Game> topOfYear);

        public static native TemplateInstance about();

        public static native TemplateInstance error();
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @ReactiveTransactional
    public Uni<TemplateInstance> home() {
        final Uni<List<Game>> recent = gameRepository.recent();
        final Uni<List<Game>> topOfYear = gameRepository.topOfYear();
        return Uni.combine().all().unis(recent, topOfYear)
                .combinedWith(Templates::home);
    }

    @GET
    @Path("about")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance about() {
        return Templates.about().data(Collections.emptyMap());
    }

    @GET
    @Path("error")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance error() {
        return Templates.error();
    }
}
