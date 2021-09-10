package com.cardboardcritic.web;

import com.cardboardcritic.feed.FeedService;

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
    public Response refresh() {
        feedService.refresh();
        return Response.status(302)
                .location(URI.create("/browse"))
                .build();
    }
}
