package com.xiandao.android.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 此类描述的是:购物车返回数据对象
 *
 * @author TanYong
 *         create at 2017/6/1 15:27
 *         <p>
 *         {
 *         "count": 6,
 *         "homename": "瑶哥的商店2",
 *         "cartid": "23",
 *         "id": 24,
 *         "typeid": null,
 *         "shopType": null,
 *         "name": "取悦-啪啪玩宠之波波",
 *         "price": "1489",
 *         "discountprice": "899",
 *         "size": "个",
 *         "resource": null,
 *         "resourceList": [{
 *         "resourceid": null,
 *         "url": "upload/shop/goods_images/1520559541902_timg.jpg",
 *         "oldurl": null,
 *         "uploadtime": null,
 *         "resourcekey": null
 *         }],
 *         "detail": null,
 *         "sort": null,
 *         "createtime": null,
 *         "updatetime": null,
 *         "remark": null
 */
public class ShopCar1Entity implements Serializable {
    private static final long serialVersionUID = 1L;

    private String count;
    private String homename;
    private String homeid;
    private String id;
    private String typeid;
    private String shopType;
    private String name;
    private String price;
    private String discountprice;
    private String size;
    private String resource;
    private ArrayList<ResourcesEntity> resourceList;
    private String detail;
    private String sort;
    private String createtime;
    private String updatetime;
    private String remark;

    private String cartid;

    private boolean isChoosed = false;

    private String goodsid;
    private String orderid;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getHomename() {
        return homename;
    }

    public void setHomename(String homename) {
        this.homename = homename;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTypeid() {
        return typeid;
    }

    public void setTypeid(String typeid) {
        this.typeid = typeid;
    }

    public String getShopType() {
        return shopType;
    }

    public void setShopType(String shopType) {
        this.shopType = shopType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDiscountprice() {
        return discountprice;
    }

    public void setDiscountprice(String discountprice) {
        this.discountprice = discountprice;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public ArrayList<ResourcesEntity> getResourceList() {
        return resourceList;
    }

    public void setResourceList(ArrayList<ResourcesEntity> resourceList) {
        this.resourceList = resourceList;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public boolean isChoosed() {
        return isChoosed;
    }

    public void setChoosed(boolean choosed) {
        isChoosed = choosed;
    }

    public String getCartid() {
        return cartid;
    }

    public void setCartid(String cartid) {
        this.cartid = cartid;
    }

    public String getHomeid() {
        return homeid;
    }

    public void setHomeid(String homeid) {
        this.homeid = homeid;
    }

    public String getGoodsid() {
        return goodsid;
    }

    public void setGoodsid(String goodsid) {
        this.goodsid = goodsid;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }
}
