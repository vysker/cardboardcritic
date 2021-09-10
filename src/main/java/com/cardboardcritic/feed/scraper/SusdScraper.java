package com.cardboardcritic.feed.scraper;

import com.cardboardcritic.db.entity.RawReview;
import com.cardboardcritic.feed.ScrapeException;
import org.jsoup.nodes.Document;

import javax.enterprise.context.ApplicationScoped;
import java.time.format.DateTimeFormatter;

@ApplicationScoped
public class SusdScraper extends ArticleScraper {

    @Override
    public RawReview getReview(String articleUrl, Document document) throws ScrapeException {
        final String date = document.select("span.entry-date").first().text();
        final String title = document.select("header h1.entry-title").first().text();

        return new RawReview()
                .setDate(DateTimeFormatter.ofPattern("MMMM d, yyyy").parse(date).toString())
                .setTitle(title)
                .setUrl(articleUrl);
    }
}
