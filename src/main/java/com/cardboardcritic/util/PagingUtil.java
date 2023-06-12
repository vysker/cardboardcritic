package com.cardboardcritic.util;

import com.cardboardcritic.db.Pageable;
import io.quarkus.hibernate.orm.panache.PanacheQuery;

import java.util.Optional;

public class PagingUtil {

    /**
     * Gives the next, previous or current page number, depending on the chosen action.
     *
     * @param maybePage Zero-based page number
     * @param pageActionString Page navigation action
     * @return Next, previous or current page, based on the page action
     */
    public static int getNewPageNumber(Optional<Integer> maybePage, Optional<String> pageActionString) {
        final PageAction action = pageActionString.map(PageAction::fromString).orElse(PageAction.NONE);
        final int currentPage = maybePage.orElse(0);
        return getNewPageNumber(currentPage, action);
    }

    /**
     * Gives the next, previous or current page number, depending on the chosen action.
     *
     * @param currentPage Zero-based page number
     * @param pageAction Page navigation action
     * @return Next, previous or current page, based on the page action
     */
    public static int getNewPageNumber(int currentPage, PageAction pageAction) {
        final int newPage = switch (pageAction) {
            case NEXT -> currentPage + 1;
            case PREVIOUS -> currentPage - 1;
            default -> currentPage;
        };
        return Math.max(0, newPage);
    }

    public static Pageable pageable(PanacheQuery<?> query, int page) {
        return new Pageable()
                .setResultCount(query.count())
                .setPageCount(query.pageCount())
                .setCurrentPage(page);
    }
}
