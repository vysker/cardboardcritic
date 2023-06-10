package com.cardboardcritic.config;

import com.cardboardcritic.feed.crawler.ArsCrawler;
import com.cardboardcritic.feed.crawler.BoardGameQuestCrawler;
import com.cardboardcritic.feed.crawler.DiceTowerCrawler;
import com.cardboardcritic.feed.crawler.DicebreakerCrawler;
import com.cardboardcritic.feed.crawler.OutletCrawler;
import com.cardboardcritic.feed.crawler.SusdCrawler;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.ext.web.client.WebClient;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import jakarta.ws.rs.Produces;
import java.util.List;

@ApplicationScoped
public class CbcConfig {

    @Produces
    @ApplicationScoped
    @Named("outletCrawlers")
    public List<OutletCrawler> outletCrawlers(ArsCrawler arsCrawler,
                                              DicebreakerCrawler dicebreakerCrawler,
                                              DiceTowerCrawler diceTowerCrawler,
                                              BoardGameQuestCrawler boardGameQuestCrawler,
                                              SusdCrawler susdCrawler) {
        return List.of(
                arsCrawler,
                dicebreakerCrawler,
                diceTowerCrawler,
                boardGameQuestCrawler,
                susdCrawler
        );
    }

    @Produces
    @ApplicationScoped
    public WebClient webClient(Vertx vertx) {
        return WebClient.create(vertx);
    }
}
