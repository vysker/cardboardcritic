package com.cardboardcritic.feed.scraper;

import com.cardboardcritic.db.entity.RawReview;
import com.cardboardcritic.feed.ScrapeException;
import com.cardboardcritic.util.ScraperUtil;
import com.cardboardcritic.util.StringUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class ArsArticleScraper extends ArticleScraper {

    @Override
    public RawReview getReview(String articleUrl, Document document) {
        // We are only interested in the content on the final page, since that is usually where the summary is
        final String finalPageUrl = getFinalPageUrl(document);
        if (finalPageUrl != null && !finalPageUrl.equals(articleUrl))
            document = fetch(finalPageUrl);

        final Element articleBody = document.select("div[itemprop=articleBody]").last();
        final String content = Optional.ofNullable(articleBody)
                .map(body -> body.getElementsByTag("p"))
                .map(Elements::eachText)
                .stream()
                .flatMap(Collection::stream)
                .filter(StringUtil::isNotEmpty)
                .collect(Collectors.joining("\n\n"));

        final Element pageMeta = document.select("meta[name=parsely-page]").first();
        final String metaContent = pageMeta == null ? null : pageMeta.attr("content");

        try {
            final JsonNode meta = ScraperUtil.toJson(metaContent);
            final String date = meta.get("pub_date").asText();
            final String title = meta.get("title").asText();
            String critic = meta.get("author").asText();

            if ("Ars Staff".equalsIgnoreCase(critic.trim()))
                critic = document.select("article header section.post-meta span[itemprop=name]").stream()
                        .findFirst()
                        .map(Element::text)
                        .orElse(null);

            return new RawReview()
                    .setTitle(title)
                    .setDate(StringUtil.formatDateTime(date))
                    .setCritic(critic)
                    .setContent(content)
                    .setUrl(articleUrl);
        } catch (JsonProcessingException e) {
            throw ScrapeException.articleError(articleUrl, "Failed to parse page metadata, because: " + e.getMessage());
        }
    }

    String getFinalPageUrl(Document document) {
        final Elements pageNumbers = document.select("nav.page-numbers span a");
        if (pageNumbers.isEmpty())
            return null;

        // If there is no arrow button saying 'next page', then we are already on the last page
        final boolean isFinalPage = Optional.ofNullable(pageNumbers.last())
                .map(Element::text)
                .map(text -> text.contains("Next"))
                .orElse(false);
        if (isFinalPage)
            return null;

        // Otherwise, take the item before the 'next' arrow
        final Element finalPage = pageNumbers.get(pageNumbers.size() - 2);
        return finalPage == null ? null : finalPage.attr("href");
    }
}
