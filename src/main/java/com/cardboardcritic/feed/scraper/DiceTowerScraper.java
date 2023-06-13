package com.cardboardcritic.feed.scraper;

import com.cardboardcritic.db.entity.RawReview;
import com.cardboardcritic.feed.ScrapeException;
import com.cardboardcritic.util.ScraperUtil;
import com.cardboardcritic.util.StringUtil;
import jakarta.enterprise.context.ApplicationScoped;
import org.jsoup.nodes.Document;

import java.util.Comparator;

@ApplicationScoped
public class DiceTowerScraper extends ArticleScraper {

    @Override
    public RawReview getReview(String articleUrl, Document document) throws ScrapeException {
        // The Dice Tower review page displays an image for either a "seal of approval/excellence" or nothing.
        // It always loads the images for both the "seal of approval" and "seal of excellence", hiding the one that's
        // not applicable. If neither are applicable, both are hidden. So here's the possible states to detect that:
        // - If a game has a "seal of approval", a class is added called "hide-seal-excellence";
        // - If a game has a "seal of excellence", a class is added called "hide-seal-approval";
        // - If a game has neither of those seals, a class is added called "hide-seal";
        final boolean recommended = document.select(".ReviewVideo__seal-wrapper .hide-seal").isEmpty();

        // Dice Tower shows everyone's rating separately. We use the highest rating.
        final float score = ScraperUtil.selectAllText(document, "span.rating").stream()
                .filter(StringUtil::isNotEmpty)
                .map(Float::parseFloat)
                .max(Comparator.naturalOrder())
                .orElse(0f);

        // TODO: Find critic; review titles are in the format "Monopoly Review with John Doe", but sometimes just "John"
        final String game = ScraperUtil.selectText(document, ".ReviewVideo__title h3").orElse(null);
        final String title = ScraperUtil.selectText(document, ".ReviewVideoFeatured__title h4").orElse(null);
        final String content = ScraperUtil.selectFirst(document, ".ReviewVideoFeatured iframe.media-oembed-content")
                .map(element -> element.attr("src"))
                .flatMap(src -> StringUtil.getRegexGroup("youtu\\.be\\/(.*?)&", src))
                .map(ScraperUtil::youtubeEmbedLinkFromVideoId)
                .orElse(null);

        return new RawReview()
                .setTitle(title)
                .setUrl(articleUrl)
                .setCritic("Dice Tower") // TODO: Sometimes there's more critics, sometimes there's one. What to do?
                .setGame(game)
                .setScore(ScraperUtil.normalizeScore(score, 10f))
                .setContent(content)
                .setRecommended(recommended);
    }
}
