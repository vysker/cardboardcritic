package com.cardboardcritic.feed.scraper;

import com.cardboardcritic.db.entity.RawReview;
import com.cardboardcritic.feed.ScrapeException;
import com.cardboardcritic.util.ScraperUtil;
import com.cardboardcritic.util.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import javax.enterprise.context.ApplicationScoped;
import java.util.Optional;

@ApplicationScoped
public class DiceTowerScraper extends ArticleScraper {

    @Override
    public RawReview getReview(String articleUrl, Document document) throws ScrapeException {
        final boolean recommended = document.select("img").eachAttr("alt").contains("Seal of Approval");

        final float score = document.select("div.views-field-field-rating h2.field-content").stream().findFirst()
                .map(Element::text)
                .filter(StringUtil::isNotEmpty)
                .map(rating -> StringUtil.after(rating, "Game Rating: "))
                .map(Float::parseFloat)
                .orElse(0f);

        // article titles are in the format "Monopoly Review - with John Doe"
        final String divider = " Review - with ";
        final Optional<String> maybeTitle = document.select("h1").eachText().stream()
                .filter(s -> s.contains(divider))
                .findFirst();
        final String critic = maybeTitle.map(title -> StringUtil.after(title, divider)).orElse(null);
        final String game = maybeTitle.map(title -> StringUtil.before(title, divider)).orElse(null);
        final String title = maybeTitle.orElseGet(() -> document.select("head title").first().text());
        final String content = ScraperUtil.toYouTubeEmbedLink(
                document.select("#player a.ytp-title-link").first().attr("href"));

        return new RawReview()
                .setTitle(title)
                .setUrl(articleUrl)
                .setCritic(critic)
                .setGame(game)
                .setScore(ScraperUtil.normalizeScore(score, 10f))
                .setContent(content)
                .setRecommended(recommended);
    }
}
