package com.cardboardcritic.feed.scraper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScraperUtil {

    /**
     * Checks for null and blank.
     *
     * @param s String to check
     * @return True if null or blank (empty/whitespace only), false otherwise.
     */
    public static boolean isEmptyString(String s) {
        return s == null || s.isBlank();
    }

    public static String stringToDate(String raw) {
        return isEmptyString(raw)
                ? LocalDateTime.parse(raw, DateTimeFormatter.ISO_DATE_TIME).toString()
                : null;
    }
}
