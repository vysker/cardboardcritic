package com.cardboardcritic.feed.scraper;

import com.cardboardcritic.db.entity.RawReview;
import com.cardboardcritic.util.ScraperUtil;
import com.cardboardcritic.util.StringUtil;
import jakarta.enterprise.context.ApplicationScoped;
import org.jsoup.nodes.Document;

import java.util.stream.Collectors;

@ApplicationScoped
public class DicebreakerScraper extends ArticleScraper {

    @Override
    public RawReview getReview(String articleUrl, Document document) {
        final String date = ScraperUtil.selectAttr(document, "meta[property=article:published_time]", "content").orElse(null);
        final String title = ScraperUtil.selectAttr(document, "meta[property=og:title]", "content").orElse(null);
        final String critic = ScraperUtil.selectText(document, "span.author a").orElse(null);
        final String game = ScraperUtil.selectText(document, "aside.about_game a").orElse(null);
        final String content = ScraperUtil.selectAllText(document, "div.article_body_content p").stream()
                .filter(StringUtil::isNotEmpty)
                .collect(Collectors.joining("\n\n"));
        final String articleTag = String.join("", document.select("meta[property=article:tag]").eachAttr("content"));
        final boolean recommended = "Dicebreaker Recommends".equals(articleTag); // TODO: Rework; I think they removed this tag

        return new RawReview()
                .setTitle(title)
                .setDate(StringUtil.formatDateTime(date))
                .setCritic(critic)
                .setGame(game)
                .setContent(content)
                .setRecommended(recommended)
                .setUrl(articleUrl);
    }
}
