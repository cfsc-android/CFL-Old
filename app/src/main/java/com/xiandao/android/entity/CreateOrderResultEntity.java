package com.xiandao.android.entity;

import java.io.Serializable;

public class CreateOrderResultEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private String msg;
    private String resultCode;
    private ShopOrderEntity data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public ShopOrderEntity getData() {
        return data;
    }

    public void setData(ShopOrderEntity data) {
        this.data = data;
    }
}