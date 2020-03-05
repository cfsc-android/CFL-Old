package com.xiandao.android.entity;

import java.io.Serializable;
import java.util.List;

public class MyOrderResultDataEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<ShopOrderEntity> orderDetail;
    private Pagination pagination;

    public List<ShopOrderEntity> getOrderDetail() {
        return orderDetail;
    }

    public void setOrderDetail(List<ShopOrderEntity> orderDetail) {
        this.orderDetail = orderDetail;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }
}