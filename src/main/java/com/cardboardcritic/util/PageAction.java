package com.cardboardcritic.util;

public enum PageAction {
    NEXT,
    PREVIOUS,
    NONE;

    public static PageAction fromString(String action) {
        return switch (action.toLowerCase()) {
            case "next" -> NEXT;
            case "previous" -> PREVIOUS;
            default -> NONE;
        };
    }
}
