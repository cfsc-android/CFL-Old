package com.xiandao.android.entity;

import java.io.Serializable;

public class ShopCommentResultEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private String msg;
    private String resultCode;
    private ShopCommentDataEntity data;

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

    public ShopCommentDataEntity getData() {
        return data;
    }

    public void setData(ShopCommentDataEntity data) {
        this.data = data;
    }
}