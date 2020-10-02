package com.cardboardcritic.web;

import com.cardboardcritic.db.repository.GameRepository;
import com.cardboardcritic.web.template.data.RecentData;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/browse")
public class BrowseResource {
    private final GameRepository gameRepo;

    @Inject
    Template recent;

    public BrowseResource(GameRepository gameRepo) {
        this.gameRepo = gameRepo;
    }

    @GET
    @Path("/recent")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance recent() {
        var games = gameRepo.listAll();
        var data = new RecentData(games);
        return recent.data(data);
    }
}
