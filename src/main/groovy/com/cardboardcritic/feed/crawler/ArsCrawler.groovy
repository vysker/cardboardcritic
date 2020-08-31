package com.cardboardcritic.feed.crawler

import com.cardboardcritic.feed.scraper.ArticleScraper

class ArsCrawler extends RssOutletCrawler {

    ArsCrawler(String outlet, ArticleScraper scraper) {
        super(outlet, scraper, 'http://feeds.arstechnica.com/arstechnica/cardboard.rss')
    }
}
