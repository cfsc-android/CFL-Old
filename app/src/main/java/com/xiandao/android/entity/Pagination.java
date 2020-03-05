package com.xiandao.android.entity;

import java.io.Serializable;

/**
 * 此类描述的是:页面信息对象
 *
 * @author TanYong
 *         create at 2017/5/8 15:42
 */
public class Pagination implements Serializable {
    private static final long serialVersionUID = 1L;
    private int currentPage;//当前页
    private int pageSize;//每页的记录数
    private int totalPages;//总的页面数
    private int totalResults;//总的记录数

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }
}
