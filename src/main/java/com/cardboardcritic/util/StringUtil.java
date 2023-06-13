package com.cardboardcritic.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    /**
     * Checks for null and blank.
     *
     * @param s String to check
     * @return True if null or blank (empty/whitespace only), false otherwise.
     */
    public static boolean isEmpty(String s) {
        return s == null || s.isBlank();
    }

    public static boolean isNotEmpty(String s) {
        return !isEmpty(s);
    }

    public static String before(String s, String pattern) {
        final int index = s.indexOf(pattern);
        if (index < 0) return s;
        return s.substring(0, index);
    }

    public static String after(String s, String pattern) {
        final int index = s.indexOf(pattern);
        if (index < 0) return s;
        return s.substring(index + pattern.length());
    }

    public static String dropRight(String s, int length) {
        return s.substring(0, s.length() - length);
    }

    /**
     * Mostly accepts strings of the form "2021-05-30T11:15:16+01:00"
     *
     * @param date date to be formatted
     * @return ISO date, so "2021-05-30"
     */
    public static String formatDateTime(String date) {
        return isEmpty(date)
                ? null
                : LocalDateTime.parse(date, DateTimeFormatter.ISO_DATE_TIME).toLocalDate().toString();
    }

    public static Optional<String> getRegexGroup(String regex, String input) {
        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(input);
        return matcher.find() ? Optional.of(matcher.group(1)) : Optional.empty();
    }
}
