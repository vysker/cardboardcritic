package com.cardboardcritic.feed.scraper;

import com.cardboardcritic.db.entity.RawReview;
import com.cardboardcritic.feed.ScrapeException;
import com.cardboardcritic.util.ScraperUtil;
import com.cardboardcritic.util.StringUtil;
import io.smallrye.mutiny.Uni;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class BoardGameQuestScraper extends ArticleScraper {

    @Override
    public Uni<RawReview> getReview(String articleUrl, Document document) throws ScrapeException {
        final Element body = document.select("div[itemprop=reviewBody]").first();
        final String title = document.select("meta[property=og:title]").attr("content"); // alternatively use "header h1.entry-title"
        final String date = document.select("header time").attr("datetime");
        final String critic = document.select("div.meta-info div.td-post-author-name a").text();
        final String game = StringUtil.before(title, " Review");
        final String content;

        // there is an image on the page to indicate the score. That image has an alt text containing the score
        final float score = document.select("img").eachAttr("alt").stream()
                .filter(alt -> alt.contains("Stars")).findFirst()
                .map(alt -> StringUtil.before(alt, " Stars"))
                .map(Float::parseFloat)
                .orElse(0f);

        // there is a header at the end of the article summarizing the author's thoughts. It is not always immediately
        // followed by a paragraph (usually a figure, instead), so find that header, then search for the first paragraph
        // after that
        Element finalThoughts = body.select("h3").last();
        if (finalThoughts != null && "final thoughts:".equalsIgnoreCase(finalThoughts.text().trim())) {
            finalThoughts = finalThoughts.nextElementSibling();
            while (!finalThoughts.is("p")) {
                finalThoughts = finalThoughts.nextElementSibling();
            }
            content = finalThoughts.text();
        } else {
            content = body.wholeText();
        }

        final var review = new RawReview()
                .setGame(game)
                .setDate(StringUtil.formatDateTime(date))
                .setTitle(title)
                .setCritic(critic)
                .setContent(content)
                .setScore(ScraperUtil.normalizeScore(score, 5f))
                .setUrl(articleUrl);
        return Uni.createFrom().item(review);
    }
}
