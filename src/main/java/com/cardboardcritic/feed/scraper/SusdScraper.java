package com.cardboardcritic.feed.scraper;

import com.cardboardcritic.db.entity.RawReview;
import com.cardboardcritic.feed.ScrapeException;
import com.cardboardcritic.util.ScraperUtil;
import com.cardboardcritic.util.StringUtil;
import jakarta.enterprise.context.ApplicationScoped;
import org.jsoup.nodes.Document;

import java.time.format.DateTimeFormatter;
import java.util.Objects;

@ApplicationScoped
public class SusdScraper implements ArticleScraper {

    @Override
    public RawReview getReview(String articleUrl, Document document) throws ScrapeException {
        final String date = ScraperUtil.selectText(document, "span.entry-date")
                .map(stringDate -> DateTimeFormatter.ofPattern("MMMM d, yyyy").parse(stringDate))
                .map(Objects::toString)
                .orElse(null);
        final String title = ScraperUtil.selectText(document, "header h1.entry-title").orElse(null);
        final String game = ScraperUtil.selectText(document, "header h1.entry-title")
                .flatMap(titleText -> StringUtil.takeAfter(titleText, "Review"))
                .orElse(null);

        return new RawReview()
                .setDate(date)
                .setTitle(title)
                .setGame(game)
                .setUrl(articleUrl);
    }
}
