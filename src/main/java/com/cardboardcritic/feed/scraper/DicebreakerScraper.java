package com.cardboardcritic.feed.scraper;

import com.cardboardcritic.db.entity.RawReview;
import com.cardboardcritic.util.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.stream.Collectors;

@ApplicationScoped
public class DicebreakerScraper extends ArticleScraper {

    @Override
    public RawReview getReview(String articleUrl, Document document) {
        final String date = document.select("meta[property=article:published_time]").stream()
                .findFirst()
                .map(element -> element.attr("content"))
                .orElse(null);
        final String title = document.select("meta[property=og:title]").stream()
                .findFirst()
                .map(element -> element.attr("content"))
                .orElse(null);
        final String critic = document.select("div.author span.name a").stream()
                .findFirst()
                .map(Element::text)
                .orElse(null);
        final String game = document.select("aside.about_game a").stream()
                .findFirst()
                .map(Element::text)
                .orElse(null);
        final String content = document.select("div.article_body_content p").eachText().stream()
                .filter(StringUtil::isNotEmpty)
                .collect(Collectors.joining("\n\n"));
        final String articleTag = document.select("meta[property=article:tag]").stream()
                .findFirst()
                .map(element -> element.attr("content"))
                .orElse(null);
        final boolean recommended = "Dicebreaker Recommends".equals(articleTag);

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
