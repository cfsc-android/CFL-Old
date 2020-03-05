package com.xiandao.android.entity;

import java.io.Serializable;
import java.util.List;

public class HikCardList implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * total : 2
     * pageNo : 1
     * pageSize : 2
     * list : []
     */

    private int total;
    private int pageNo;
    private int pageSize;
    private List<HikCard> list;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<HikCard> getList() {
        return list;
    }

    public void setList(List<HikCard> list) {
        this.list = list;
    }
}
