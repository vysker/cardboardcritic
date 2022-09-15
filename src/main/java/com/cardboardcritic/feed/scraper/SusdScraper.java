package com.cardboardcritic.feed.scraper;

import com.cardboardcritic.db.entity.RawReview;
import com.cardboardcritic.feed.ScrapeException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import javax.enterprise.context.ApplicationScoped;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@ApplicationScoped
public class SusdScraper extends ArticleScraper {

    @Override
    public RawReview getReview(String articleUrl, Document document) throws ScrapeException {
        final String date = document.select("span.entry-date").stream()
                .findFirst()
                .map(Element::text)
                .map(stringDate -> DateTimeFormatter.ofPattern("MMMM d, yyyy").parse(stringDate))
                .map(Objects::toString)
                .orElse(null);
        final String title = document.select("header h1.entry-title").stream()
                .findFirst()
                .map(Element::text)
                .orElse(null);

        return new RawReview()
                .setDate(date)
                .setTitle(title)
                .setUrl(articleUrl);
    }
}
