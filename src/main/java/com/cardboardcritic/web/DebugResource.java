package com.cardboardcritic.web;

import com.cardboardcritic.feed.CrawlerService;
import com.cardboardcritic.feed.ScoreService;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.concurrent.CompletableFuture;

// TODO remove this class. This is here just for debugging purposes
@Path("debug")
@RolesAllowed("admin")
public class DebugResource {
    private final CrawlerService crawlerService;
    private final ScoreService scoreService;

    public DebugResource(CrawlerService crawlerService, ScoreService scoreService) {
        this.crawlerService = crawlerService;
        this.scoreService = scoreService;
    }

    @GET
    @Path("score")
    @Produces(MediaType.TEXT_HTML)
    public Response score() {
        scoreService.aggregateScores();
        return Response.seeOther(URI.create("/")).build();
    }

    @GET
    @Path("feed")
    @Produces(MediaType.TEXT_HTML)
    public Response feed() {
        Uni.createFrom().completionStage(() -> {
                    final CompletableFuture<String> future = new CompletableFuture<>();
                    final boolean success = crawlerService.crawl();
                    if (success) {
                        future.complete("Crawled successfully");
                    } else {
                        future.exceptionally(x -> "Crawling failed");
                    }
                    return future.minimalCompletionStage();
                })
                .emitOn(Infrastructure.getDefaultWorkerPool())
                .subscribe().with(x -> System.out.println("done"));

        return Response.seeOther(URI.create("/")).build();
    }
}
