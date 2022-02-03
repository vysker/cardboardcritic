package com.cardboardcritic.config;

import com.cardboardcritic.feed.crawler.*;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.ext.web.client.WebClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.ws.rs.Produces;
import java.util.List;

@ApplicationScoped
public class CbcConfig {

    @Produces
    @ApplicationScoped
    @Named("outletCrawlers")
    public List<OutletCrawler> outletCrawlers(ArsCrawler arsCrawler,
                                              EurogamerCrawler eurogamerCrawler,
                                              DiceTowerCrawler diceTowerCrawler,
                                              BoardGameQuestCrawler boardGameQuestCrawler,
                                              SusdCrawler susdCrawler) {
        return List.of(arsCrawler, eurogamerCrawler, diceTowerCrawler, boardGameQuestCrawler, susdCrawler);
    }

    @Produces
    @ApplicationScoped
    public WebClient webClient(Vertx vertx) {
        return WebClient.create(vertx);
    }
}
