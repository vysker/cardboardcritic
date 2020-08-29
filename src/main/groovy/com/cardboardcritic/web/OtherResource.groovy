package com.cardboardcritic.web

import io.quarkus.qute.Template
import io.quarkus.qute.TemplateInstance

import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@ApplicationScoped
class OtherResource {

    @Inject
    Template home

    @Inject
    Template about

    @GET
    @Produces(MediaType.TEXT_HTML)
    TemplateInstance home() {
        home.data {}
    }

    @GET
    @Path('/about')
    @Produces(MediaType.TEXT_HTML)
    TemplateInstance about() {
        about.data {}
    }
}
