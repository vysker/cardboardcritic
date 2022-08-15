package com.cardboardcritic.util;

import com.cardboardcritic.db.Pageable;
import io.quarkus.hibernate.orm.panache.PanacheQuery;

import java.util.Optional;

public class PagingUtil {

    public static int getNewPage(Optional<Integer> page, Optional<String> pageAction) {
        final int currentPage = page.orElse(0);
        if (pageAction.isPresent() && pageAction.get().equals("Previous"))
            return currentPage - 1;
        else if (pageAction.isPresent() && pageAction.get().equals("Next"))
            return currentPage + 1;
        return currentPage;
    }

    public static Pageable pageable(PanacheQuery<?> query, int page) {
        return new Pageable()
                .setResultCount(query.count())
                .setPageCount(query.pageCount())
                .setCurrentPage(page);
    }
}
