package com.cardboardcritic.db;

public class Pageable {
    private long resultCount;
    private int pageCount;
    private int currentPage;

    public long getResultCount() {
        return resultCount;
    }

    public Pageable setResultCount(long resultCount) {
        this.resultCount = resultCount;
        return this;
    }

    public int getPageCount() {
        return pageCount;
    }

    public Pageable setPageCount(int pageCount) {
        this.pageCount = pageCount;
        return this;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public Pageable setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
        return this;
    }
}
