package com.cardboardcritic.feed;

public class ScrapeException extends RuntimeException {

    public ScrapeException(String message) {
        super(message);
    }

    public static ScrapeException articleError(String articleUrl, String message) {
        return new ScrapeException("Failed to parse article with url '%s' because: %s".formatted(articleUrl, message));
    }
}
