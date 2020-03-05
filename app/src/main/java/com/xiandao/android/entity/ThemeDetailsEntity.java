package com.xiandao.android.entity;

public class ThemeDetailsEntity {

    /**
     * data : []
     * resultCode : 0
     * msg : 查询成功
     */

    private String resultCode;
    private String msg;
    private ThemeEntity data;

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

    public ThemeEntity getData() {
        return data;
    }

    public void setData(ThemeEntity data) {
        this.data = data;
    }
}
