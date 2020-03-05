package com.xiandao.android.entity;

import java.io.Serializable;
import java.util.List;

public class ShopCommentDataEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<ShopCommentEntity> comments;
    private Pagination pagination;

    public List<ShopCommentEntity> getComments() {
        return comments;
    }

    public void setComments(List<ShopCommentEntity> comments) {
        this.comments = comments;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }
}