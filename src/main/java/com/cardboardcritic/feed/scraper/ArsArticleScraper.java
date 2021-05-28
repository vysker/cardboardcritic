package com.cardboardcritic.feed.scraper;

import com.cardboardcritic.db.entity.RawReview;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@ApplicationScoped
public class ArsArticleScraper extends ArticleScraper {

    @SneakyThrows // FIXME bad, bad code
    @Override
    public RawReview getReview(String articleUrl, Document document) {
        String finalPageUrl = getFinalPageUrl(document);
        if (finalPageUrl != null && !finalPageUrl.equals(articleUrl))
            document = fetch(finalPageUrl);

        Element articleBody = document.select("div[itemprop=articleBody]").last();
        Elements articleContentRaw = articleBody.getElementsByTag("p");
        String articleContent = articleContentRaw.eachText().stream()
                .filter(content -> !ScraperUtil.isEmptyString(content))
                .collect(Collectors.joining());

        Element pageMeta = document.select("meta[name=parsely-page]").first();
        String metaContent = pageMeta == null ? null : pageMeta.attr("content");
        JsonNode meta = new ObjectMapper().readTree(metaContent == null ? "{}" : metaContent);

        String pubDateRaw = meta.get("pub_date").asText();
        String pubDate = ScraperUtil.isEmptyString(pubDateRaw)
                ? LocalDateTime.parse(pubDateRaw, DateTimeFormatter.ISO_DATE_TIME).toString()
                : null;

        RawReview.builder()
                .title(meta.get("title").asText())
                .date(pubDate)
                .critic(meta.get("author").asText())
                .content(articleContent)
                .url(articleUrl)
                .build();
        return null;
    }

    String getFinalPageUrl(Document document) {
        Elements pageNumbers = document.select("nav.page-numbers span a");
        if (pageNumbers.isEmpty())
            return null;

        // if there is no arrow button saying 'next page', then we are already on the last page
        boolean isFinalPage = !pageNumbers.last().text().contains("Next");
        if (isFinalPage)
            return null;

        // otherwise, take the item before the 'next' arrow
        Element finalPage = pageNumbers.get(pageNumbers.size() - 2);
        return finalPage == null ? null : finalPage.attr("href");
    }
}
