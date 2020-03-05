package com.xiandao.android.entity;

import java.util.List;

public class ThemeListEntity {

    /**
     * data : []
     * resultCode : 0
     * msg : 查询成功
     */

    private String resultCode;
    private String msg;
    private List<ThemeEntity> data;

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

    public List<ThemeEntity> getData() {
        return data;
    }

    public void setData(List<ThemeEntity> data) {
        this.data = data;
    }
}
