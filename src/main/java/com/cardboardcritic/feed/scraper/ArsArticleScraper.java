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
import java.util.function.Predicate;
import java.util.stream.Collectors;

@ApplicationScoped
public class ArsArticleScraper extends ArticleScraper {

    @Override
    public RawReview getReview(String articleUrl, final Document firstPage) {
        // We are only interested in the content on the final page, since that is usually where the summary is.
        // So, we try to find the "final page url", and check it against the "article url". If the urls are identical,
        // that means this article has just one page. If the urls are different, then go there, and fetch the
        // content/document of that final page.
        final Document document = getFinalPageUrl(firstPage)
                .filter(Predicate.not(articleUrl::equals))
                .map(this::fetch)
                .orElse(firstPage);

        final Element articleBody = document.select("div[itemprop=articleBody]").last();
        final String content = Optional.ofNullable(articleBody)
                .map(body -> body.getElementsByTag("p"))
                .map(Elements::eachText)
                .stream()
                .flatMap(Collection::stream)
                .filter(StringUtil::isNotEmpty)
                .collect(Collectors.joining("\n\n"));
        final String metaContent = ScraperUtil.selectFirst(document, "meta[name=parsely-page]")
                .map(pageMeta -> pageMeta.attr("content"))
                .orElse(null);

        try {
            final JsonNode meta = ScraperUtil.toJson(metaContent);
            final String date = meta.get("pub_date").asText(null);
            final String title = meta.get("title").asText(null);
            String critic = meta.get("author").asText(null);

            if ("Ars Staff".equalsIgnoreCase(critic.trim())) {
                critic = ScraperUtil.selectText(document, "article header section.post-meta span[itemprop=name]")
                        .orElse(null);
            }

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

    private Optional<String> getFinalPageUrl(Document document) {
        final Elements pageNumbers = document.select("nav.page-numbers span a");
        if (pageNumbers.isEmpty())
            return Optional.empty();

        // If there is no arrow button saying 'next page', then we are already on the last page
        final boolean isFinalPage = Optional.ofNullable(pageNumbers.last())
                .map(Element::text)
                .map(text -> text.contains("Next"))
                .orElse(false);
        if (isFinalPage)
            return Optional.empty();

        // Otherwise, take the item before the 'next' arrow
        final Element finalPage = pageNumbers.get(pageNumbers.size() - 2);
        return Optional.ofNullable(finalPage)
                .map(element -> element.attr("href"));
    }
}
