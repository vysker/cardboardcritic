package com.cardboardcritic.config;

import com.cardboardcritic.feed.crawler.*;

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
}
