package com.cardboardcritic.web

import com.cardboardcritic.db.repository.GameRepository
import com.cardboardcritic.web.template.data.RecentData
import io.quarkus.qute.Template
import io.quarkus.qute.TemplateInstance

import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path('/browse')
class BrowseResource {

    @Inject
    Template recent

    @Inject
    GameRepository gameRepo

    @GET
    @Path('/recent')
    @Produces(MediaType.TEXT_HTML)
    TemplateInstance recent() {
        def data = new RecentData()
        data.games = gameRepo.findAll().list()
        recent.data data
    }
}
