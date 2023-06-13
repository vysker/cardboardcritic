package com.cardboardcritic.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class ScraperUtil {
    private static final ObjectMapper JSON_MAPPER = new ObjectMapper();

    public static List<String> selectAllText(Document document, String selector) {
        return document.select(selector).eachText();
    }

    public static Optional<String> selectText(Document document, String selector) {
        return selectFirst(document, selector).map(Element::text);
    }

    public static Optional<Element> selectFirst(Document document, String selector) {
        final Element element = document.selectFirst(selector);
        return Optional.ofNullable(element);
    }

    public static int normalizeScore(float score, float scoreScale) {
        return new BigDecimal(score / scoreScale).movePointRight(2).intValue();
    }

    public static JsonNode toJson(String raw) throws JsonProcessingException {
        final String nonNullInput = raw == null ? "{}" : raw;
        return JSON_MAPPER.readTree(nonNullInput);
    }

    public static String youtubeEmbedLinkFromVideoId(String videoId) {
        return "https://www.youtube.com/embed/" + videoId;
    }
}
