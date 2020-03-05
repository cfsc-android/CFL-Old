package com.xiandao.android.entity;

import java.io.Serializable;
import java.util.List;

public class HikCarList implements Serializable {
    private static final long serialVersionUID=1L;


    /**
     * total : 1
     * pageNo : 1
     * pageSize : 1000
     * list : [{"vehicleId":"ddfff934-3632-4891-a9e9-bea8c25e54b6","plateNo":"浙A12345","isBandPerson":1,"personId":"f1ed9ade-8057-4d01-bb38-2af26d43083e","personName":"shunzi","phoneNo":null,"plateType":0,"plateColor":0,"vehicleType":1,"vehicleColor":1,"mark":"备注备注"}]
     */

    private int total;
    private int pageNo;
    private int pageSize;
    private List<HikCar> list;

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

    public List<HikCar> getList() {
        return list;
    }

    public void setList(List<HikCar> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "HikCarList{" +
                "total=" + total +
                ", pageNo=" + pageNo +
                ", pageSize=" + pageSize +
                ", list=" + list.size() +
                '}';
    }
}
