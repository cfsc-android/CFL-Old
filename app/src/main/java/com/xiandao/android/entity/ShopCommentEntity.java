package com.xiandao.android.entity;

import java.io.Serializable;
import java.util.List;

public class ShopCommentEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private String orderid;
    private GoodsCommentOwnersEntity owners;
    private String id;
    private String goosid;
    private String ownerid;
    private String content;
    private String stargrade;
    private String commentgrade;
    private String commentdatetime;

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public GoodsCommentOwnersEntity getOwners() {
        return owners;
    }

    public void setOwners(GoodsCommentOwnersEntity owners) {
        this.owners = owners;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGoosid() {
        return goosid;
    }

    public void setGoosid(String goosid) {
        this.goosid = goosid;
    }

    public String getOwnerid() {
        return ownerid;
    }

    public void setOwnerid(String ownerid) {
        this.ownerid = ownerid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStargrade() {
        return stargrade;
    }

    public void setStargrade(String stargrade) {
        this.stargrade = stargrade;
    }

    public String getCommentgrade() {
        return commentgrade;
    }

    public void setCommentgrade(String commentgrade) {
        this.commentgrade = commentgrade;
    }

    public String getCommentdatetime() {
        return commentdatetime;
    }

    public void setCommentdatetime(String commentdatetime) {
        this.commentdatetime = commentdatetime;
    }
}