package com.cardboardcritic.feed.scraper;

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
}
