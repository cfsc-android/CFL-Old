package com.xiandao.android.entity;

import java.io.Serializable;
import java.util.List;

public class ShopOrderDetailEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String orderid;
    private String goodsid;
    private String goodsName;
    private String goodsPrice;
    private String number;
    private String homeid;
    private String homeName;

    private List<ResourcesEntity> pics;

    private ShopCar1Entity shopGoods;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getGoodsid() {
        return goodsid;
    }

    public void setGoodsid(String goodsid) {
        this.goodsid = goodsid;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(String goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getHomeid() {
        return homeid;
    }

    public void setHomeid(String homeid) {
        this.homeid = homeid;
    }

    public String getHomeName() {
        return homeName;
    }

    public void setHomeName(String homeName) {
        this.homeName = homeName;
    }

    public List<ResourcesEntity> getPics() {
        return pics;
    }

    public void setPics(List<ResourcesEntity> pics) {
        this.pics = pics;
    }

    public ShopCar1Entity getShopGoods() {
        return shopGoods;
    }

    public void setShopGoods(ShopCar1Entity shopGoods) {
        this.shopGoods = shopGoods;
    }
}