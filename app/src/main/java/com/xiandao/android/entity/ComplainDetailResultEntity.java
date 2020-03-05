package com.xiandao.android.entity;

import java.io.Serializable;

/**
 * 此类描述的是:我的投诉详情返回数据对象
 *
 * @author TanYong
 *         create at 2017/6/2 13:42
 */
public class ComplainDetailResultEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private ComplainDetailEntity data;
    private String resultCode;
    private String msg;

    public ComplainDetailEntity getData() {
        return data;
    }

    public void setData(ComplainDetailEntity data) {
        this.data = data;
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
