package com.cardboardcritic.feed.scraper;

import com.cardboardcritic.db.entity.RawReview;
import com.cardboardcritic.feed.ScrapeException;
import com.cardboardcritic.util.ScraperUtil;
import jakarta.enterprise.context.ApplicationScoped;
import org.jsoup.nodes.Document;

import java.time.format.DateTimeFormatter;
import java.util.Objects;

@ApplicationScoped
public class SusdScraper extends ArticleScraper {

    @Override
    public RawReview getReview(String articleUrl, Document document) throws ScrapeException {
        final String date = ScraperUtil.selectText(document, "span.entry-date")
                .map(stringDate -> DateTimeFormatter.ofPattern("MMMM d, yyyy").parse(stringDate))
                .map(Objects::toString)
                .orElse(null);
        final String title = ScraperUtil.selectText(document, "header h1.entry-title").orElse(null);

        return new RawReview()
                .setDate(date)
                .setTitle(title)
                .setUrl(articleUrl);
    }
}
