package com.cardboardcritic.web;

import com.cardboardcritic.feed.FeedService;
import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
import io.smallrye.mutiny.Uni;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

// TODO remove this class. This is here just for debugging purposes
@Path("feed")
public class FeedResource {
    private final FeedService feedService;

    public FeedResource(FeedService feedService) {
        this.feedService = feedService;
    }

    @GET
    @Path("all")
    @Produces(MediaType.TEXT_HTML)
    @ReactiveTransactional
    public Uni<Response> refresh() {
        return Uni.createFrom().item(
                Response.status(302)
                        .location(URI.create("/"))
                        .build())
                .call(x -> feedService.refresh());
    }
}
