package com.cardboardcritic.feed.scraper;

import com.cardboardcritic.db.entity.RawReview;
import com.cardboardcritic.feed.ScrapeException;
import com.cardboardcritic.util.ScraperUtil;
import com.cardboardcritic.util.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class BoardGameQuestScraper implements ArticleScraper {

    @Override
    public RawReview getReview(String articleUrl, Document document) throws ScrapeException {
        final Element body = document.select("div[itemprop=reviewBody]").first();
        final String title = ScraperUtil.selectFirst(document, "meta[property=og:title]")
                .map(element -> element.attr("content"))
                .or(() -> ScraperUtil.selectText(document, "header h1.entry-title"))
                .orElse(null);
        final String date = ScraperUtil.selectFirst(document, "header time")
                .map(element -> element.attr("datetime"))
                .orElse(null);
        final String critic = document.select("div.meta-info div.td-post-author-name a").text();
        final String game = StringUtil.takeBefore(title, " Review").orElse(title);

        // There is an image on the page to indicate the score. That image has an alt text containing the score
        final float score = document.select("img").eachAttr("alt").stream()
                .filter(alt -> alt.contains("Stars")).findFirst()
                .flatMap(alt -> StringUtil.takeBefore(alt, " Stars"))
                .map(Float::parseFloat)
                .orElse(0f);

        return new RawReview()
                .setGame(game)
                .setDate(StringUtil.formatDateTime(date))
                .setTitle(title)
                .setCritic(critic)
                .setContent(getFinalThoughts(body))
                .setScore(ScraperUtil.normalizeScore(score, 5f))
                .setUrl(articleUrl);
    }

    private String getFinalThoughts(Element body) {
        if (body == null)
            return null;

        // There is a header at the end of the article summarizing the author's thoughts. It is not always immediately
        // followed by a paragraph (usually a figure, instead), so find that header, then search for the first paragraph
        // after that
        return Optional.ofNullable(body.select("h3").last())
                .filter(heading -> "final thoughts:".equalsIgnoreCase(heading.text().trim()))
                .flatMap(heading -> heading.nextElementSiblings().stream()
                    .filter(element -> element.is("p"))
                    .findFirst()
                    .map(Element::text))
                .orElse(body.wholeText());
    }
}
