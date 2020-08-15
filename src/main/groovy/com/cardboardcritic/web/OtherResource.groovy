package com.cardboardcritic.web

import com.cardboardcritic.web.template.TemplateHelper
import com.cardboardcritic.web.template.data.TemplateData
import io.quarkus.qute.Template
import io.quarkus.qute.TemplateInstance

import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@ApplicationScoped
@Path('/')
class OtherResource {

    @Inject
    Template home

    @Inject
    Template about

    @Inject
    TemplateHelper templateHelper

    @GET
    @Produces(MediaType.TEXT_HTML)
    TemplateInstance home() {
        def data = new TemplateData()
        templateHelper.withGlobals data
        home.data data
    }

    @GET
    @Path('/about')
    @Produces(MediaType.TEXT_HTML)
    TemplateInstance about() {
        def data = new TemplateData()
        templateHelper.withGlobals data
        about.data data
    }
}
