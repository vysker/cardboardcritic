package com.cardboardcritic.config;

import com.cardboardcritic.feed.crawler.ArsCrawler;
import com.cardboardcritic.feed.crawler.EurogamerCrawler;
import com.cardboardcritic.feed.crawler.OutletCrawler;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.ws.rs.Produces;
import java.util.List;

@ApplicationScoped
public class CbcConfig {

    @Produces
    @ApplicationScoped
    @Named("outletCrawlers")
    public List<OutletCrawler> outletCrawlers(ArsCrawler arsCrawler, EurogamerCrawler eurogamerCrawler) {
        return List.of(arsCrawler, eurogamerCrawler);
    }
}
