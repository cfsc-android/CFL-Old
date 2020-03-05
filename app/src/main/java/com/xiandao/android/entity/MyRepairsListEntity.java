package com.xiandao.android.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 此类描述的是:我的报修列表对象
 * @author TanYong
 * create at 2017/5/8 16:39
 */
public class MyRepairsListEntity implements Serializable{
    private static final long serialVersionUID = 1L;
    private ArrayList<MyRepairsEntity> repairsEntityList;
    private Pagination pagination;
    private String resultCode;
    private String msg;

    public ArrayList<MyRepairsEntity> getRepairsEntityList() {
        return repairsEntityList;
    }

    public void setRepairsEntityList(ArrayList<MyRepairsEntity> repairsEntityList) {
        this.repairsEntityList = repairsEntityList;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
