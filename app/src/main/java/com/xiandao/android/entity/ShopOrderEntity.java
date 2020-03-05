package com.xiandao.android.entity;

import java.io.Serializable;
import java.util.List;

public class ShopOrderEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String userid;
    private String userName;
    private String contact;
    private String countprice;
    private String status;
    private String createtime;
    private String paytime;
    private String overtime;
    private String cancletime;
    private String remark;
    private List<ShopOrderDetailEntity> shopOrderDetail;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getCountprice() {
        return countprice;
    }

    public void setCountprice(String countprice) {
        this.countprice = countprice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getPaytime() {
        return paytime;
    }

    public void setPaytime(String paytime) {
        this.paytime = paytime;
    }

    public String getOvertime() {
        return overtime;
    }

    public void setOvertime(String overtime) {
        this.overtime = overtime;
    }

    public String getCancletime() {
        return cancletime;
    }

    public void setCancletime(String cancletime) {
        this.cancletime = cancletime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<ShopOrderDetailEntity> getShopOrderDetail() {
        return shopOrderDetail;
    }

    public void setShopOrderDetail(List<ShopOrderDetailEntity> shopOrderDetail) {
        this.shopOrderDetail = shopOrderDetail;
    }
}