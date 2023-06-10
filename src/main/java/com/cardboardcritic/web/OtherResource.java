package com.cardboardcritic.web;

import com.cardboardcritic.db.entity.Game;
import com.cardboardcritic.db.repository.GameRepository;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;

import jakarta.annotation.security.PermitAll;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
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

        public static native TemplateInstance faq();

        public static native TemplateInstance error();
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance home() {
        final List<Game> recent = gameRepository.recent();
        final List<Game> topOfYear = gameRepository.topOfYear();
        return Templates.home(recent, topOfYear);
    }

    @GET
    @Path("about")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance about() {
        return Templates.about();
    }

    @GET
    @Path("faq")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance faq() {
        return Templates.faq();
    }

    @GET
    @Path("error")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance error() {
        return Templates.error();
    }
}
