package com.cardboardcritic.web

import com.cardboardcritic.db.repository.GameRepository
import io.quarkus.qute.Template
import io.quarkus.qute.TemplateInstance

import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path('test')
class HomeResource {

    @Inject
    Template test

    @Inject
    GameRepository gameRepo

    @GET
    @Produces(MediaType.TEXT_HTML)
    TemplateInstance templatedHello() {
        test.data 'name', 'abc'
    }

    @GET
    @Path('hi')
    @Produces(MediaType.TEXT_PLAIN)
    String hello() {
        gameRepo.findAll().list()*.name
    }
}
