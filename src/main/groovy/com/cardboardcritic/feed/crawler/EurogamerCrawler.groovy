package com.cardboardcritic.feed.crawler

import com.cardboardcritic.feed.scraper.ArticleScraper

class EurogamerCrawler extends RssOutletCrawler {
    EurogamerCrawler(String outlet, ArticleScraper scraper) {
        super(outlet, scraper, 'https://www.eurogamer.net/?format=rss&platform=BOARDGAME')
    }
}
