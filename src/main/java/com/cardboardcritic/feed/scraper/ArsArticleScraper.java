package com.cardboardcritic.feed.scraper;

import com.cardboardcritic.db.entity.RawReview;
import com.cardboardcritic.feed.ScrapeException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.enterprise.context.ApplicationScoped;
import java.util.stream.Collectors;

@ApplicationScoped
public class ArsArticleScraper extends ArticleScraper {

    @Override
    public RawReview getReview(String articleUrl, Document document) {
        final String finalPageUrl = getFinalPageUrl(document);
        if (finalPageUrl != null && !finalPageUrl.equals(articleUrl))
            document = fetch(finalPageUrl);

        final Element articleBody = document.select("div[itemprop=articleBody]").last();
        final Elements articleContentRaw = articleBody.getElementsByTag("p");
        final String articleContent = articleContentRaw.eachText().stream()
                .filter(content -> !ScraperUtil.isEmptyString(content))
                .collect(Collectors.joining());

        final Element pageMeta = document.select("meta[name=parsely-page]").first();
        final String metaContent = pageMeta == null ? null : pageMeta.attr("content");

        try {
            final JsonNode meta = new ObjectMapper().readTree(metaContent == null ? "{}" : metaContent);
            final String dateRaw = meta.get("pub_date").asText();
            final String title = meta.get("title").asText();
            final String critic = meta.get("author").asText();

            return new RawReview()
                    .setTitle(title)
                    .setDate(ScraperUtil.stringToDate(dateRaw))
                    .setCritic(critic)
                    .setContent(articleContent)
                    .setUrl(articleUrl);
        } catch (JsonProcessingException e) {
            throw ScrapeException.articleError(articleUrl, "Failed to parse page metadata, because: " + e.getMessage());
        }
    }

    String getFinalPageUrl(Document document) {
        final Elements pageNumbers = document.select("nav.page-numbers span a");
        if (pageNumbers.isEmpty())
            return null;

        // if there is no arrow button saying 'next page', then we are already on the last page
        final boolean isFinalPage = !pageNumbers.last().text().contains("Next");
        if (isFinalPage)
            return null;

        // otherwise, take the item before the 'next' arrow
        final Element finalPage = pageNumbers.get(pageNumbers.size() - 2);
        return finalPage == null ? null : finalPage.attr("href");
    }
}
