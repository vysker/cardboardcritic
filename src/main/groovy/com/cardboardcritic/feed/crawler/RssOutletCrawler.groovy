package com.cardboardcritic.feed.crawler

import com.cardboardcritic.feed.scraper.ArticleScraper
import groovy.xml.XmlSlurper
import groovy.xml.slurpersupport.GPathResult

class RssOutletCrawler extends OutletCrawler {
    String feedUrl

    RssOutletCrawler(String outlet, ArticleScraper scraper, String feedUrl) {
        super(outlet, scraper)
        this.feedUrl = feedUrl
    }

    @Override
    List<String> getArticleLinks() {
        def rssRaw = new URL(feedUrl).text
        def xmlParser = new XmlSlurper()
        GPathResult rss = xmlParser.parseText rssRaw
        rss.channel.item.link*.text()
    }
}
