package com.cardboardcritic.web;

import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Collections;

@Path("")
public class OtherResource {

    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance home();

        public static native TemplateInstance about();
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance home() {
        return Templates.home().data(Collections.emptyMap());
    }

    @GET
    @Path("about")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance about() {
        return Templates.about().data(Collections.emptyMap());
    }
}
