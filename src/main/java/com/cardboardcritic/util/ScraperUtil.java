package com.cardboardcritic.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;

public class ScraperUtil {
    private static final ObjectMapper JSON_MAPPER = new ObjectMapper();

    public static int normalizeScore(float score, float scoreScale) {
        return new BigDecimal(score / scoreScale).movePointRight(2).intValue();
    }

    public static JsonNode toJson(String raw) throws JsonProcessingException {
        final String nonNullInput = raw == null ? "{}" : raw;
        return JSON_MAPPER.readTree(nonNullInput);
    }

    public static String toYouTubeEmbedLink(String youTubeLink) {
        return youTubeLink.replace("watch?v=", "embed/");
    }
}
