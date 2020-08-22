package com.cardboardcritic.web

import com.cardboardcritic.db.repository.GameRepository
import com.cardboardcritic.web.template.data.GameData
import io.quarkus.qute.Template
import io.quarkus.qute.TemplateInstance

import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.Path
import org.jboss.resteasy.annotations.jaxrs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path('/game')
class GameResource {

    @Inject
    Template game

    @Inject
    GameRepository gameRepo

    @GET
    @Path('/{slug}')
    @Produces(MediaType.TEXT_HTML)
    TemplateInstance game(@PathParam('slug') String slug) {
        def data = new GameData()
        data.game = gameRepo.findBySlug slug
        data.game.reviews.sort { a, b -> b.score <=> a.score } // desc
        game.data data
    }
}
