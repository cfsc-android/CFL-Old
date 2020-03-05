package com.xiandao.android.entity;

import java.io.Serializable;

/**
 * Created by 谭勇 on 2017/8/8.
 */

public class PatrolAddressResultEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private String msg;
    private String resultCode;
    private PatrolAddress data;

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

    public PatrolAddress getData() {
        return data;
    }

    public void setData(PatrolAddress data) {
        this.data = data;
    }
}
