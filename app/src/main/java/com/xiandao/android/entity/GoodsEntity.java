package com.xiandao.android.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 此类描述的是:商品对象
 *
 * @author TanYong
 *         create at 2017/6/14 15:42
 */
public class GoodsEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String typeid;
    private GoodsClassificationEntity shopType;
    private String name;
    private String price;
    private String discountprice;
    private String size;
    private String resource;
    private List<ResourcesEntity> resourceList;
    private String detail;
    private String sort;
    private String createtime;
    private String updatetime;
    private String remark;
    private String homename;

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

    public GoodsClassificationEntity getShopType() {
        return shopType;
    }

    public void setShopType(GoodsClassificationEntity shopType) {
        this.shopType = shopType;
    }

    public List<ResourcesEntity> getResourceList() {
        return resourceList;
    }

    public void setResourceList(List<ResourcesEntity> resourceList) {
        this.resourceList = resourceList;
    }

    public String getHomename() {
        return homename;
    }

    public void setHomename(String homename) {
        this.homename = homename;
    }
}
