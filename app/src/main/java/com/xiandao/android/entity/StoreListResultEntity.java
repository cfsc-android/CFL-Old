package com.xiandao.android.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by 谭勇 on 2018/3/8.
 */
public class StoreListResultEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private String resultCode;
    private String msg;
    private ArrayList<StoreEntity> data;

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

    public ArrayList<StoreEntity> getData() {
        return data;
    }

    public void setData(ArrayList<StoreEntity> data) {
        this.data = data;
    }
}
