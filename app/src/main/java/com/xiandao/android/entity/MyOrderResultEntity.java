package com.xiandao.android.entity;

import java.io.Serializable;
import java.util.List;

public class MyOrderResultEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private String resultCode;
    private String msg;
    private MyOrderResultDataEntity data;

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

    public MyOrderResultDataEntity getData() {
        return data;
    }

    public void setData(MyOrderResultDataEntity data) {
        this.data = data;
    }
}