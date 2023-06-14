package com.cardboardcritic.feed.scraper;

import com.cardboardcritic.db.entity.RawReview;
import com.cardboardcritic.feed.ScrapeException;
import org.jsoup.nodes.Document;

/**
 * A scraper fetches the contents of an article (review), then parses and converts that content to a "raw review".
 */
public interface ArticleScraper {

    RawReview getReview(String articleUrl, Document document) throws ScrapeException;
}
