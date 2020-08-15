package com.cardboardcritic.web

import com.cardboardcritic.db.repository.GameRepository
import com.cardboardcritic.web.template.TemplateHelper
import com.cardboardcritic.web.template.data.GameData
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
    Template base

    @Inject
    GameRepository gameRepo

    @Inject
    TemplateHelper templateHelper

    @GET
    @Produces(MediaType.TEXT_HTML)
    TemplateInstance templatedHello() {
        def data = new GameData()
        data.game = gameRepo.findAll().list().first()
        templateHelper.withGlobals data
        base.data data
    }

    @GET
    @Path('hi')
    @Produces(MediaType.TEXT_PLAIN)
    String hello() {
        gameRepo.findAll().list()*.name
    }
}
