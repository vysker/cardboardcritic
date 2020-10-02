package com.cardboardcritic.web;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Collections;

@Path("")
public class OtherResource {
    @Inject
    Template home;

    @Inject
    Template about;

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance home() {
        return home.data(Collections.emptyMap());
    }

    @GET
    @Path("/about")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance about() {
        return about.data(Collections.emptyMap());
    }
}
