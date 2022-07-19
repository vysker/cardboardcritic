package com.cardboardcritic.web;

import com.cardboardcritic.feed.FeedService;
import com.cardboardcritic.feed.ScoreService;
import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
import io.smallrye.mutiny.Uni;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

// TODO remove this class. This is here just for debugging purposes
@Path("debug")
@RolesAllowed("admin")
public class DebugResource {
    private final FeedService feedService;
    private final ScoreService scoreService;

    public DebugResource(FeedService feedService, ScoreService scoreService) {
        this.feedService = feedService;
        this.scoreService = scoreService;
    }

    @GET
    @Path("score")
    @Produces(MediaType.TEXT_HTML)
    @ReactiveTransactional
    public Uni<Response> score() {
        return Uni.createFrom().item(Response.seeOther(URI.create("/")).build())
                .call(x -> scoreService.aggregateScores());
    }

    @GET
    @Path("feed")
    @Produces(MediaType.TEXT_HTML)
    @ReactiveTransactional
    public Uni<Response> feed() {
        return Uni.createFrom().item(Response.seeOther(URI.create("/")).build())
                .call(x -> feedService.refresh());
    }
}
