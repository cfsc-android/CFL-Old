package com.xiandao.android.entity;

import java.io.Serializable;

/**
 * 此类描述的是:用来判断是跳转到评价界面还是支付界面
 * @author TanYong
 * create at 2017/5/26 20:50
 */
public class ConfirmWorkOrderFinishResultEntity implements Serializable{
    private static final long serialVersionUID = 1L;

    private String resultCode;
    private String msg;
    private ConfirmWorkOrderFinishEntity data;

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

    public ConfirmWorkOrderFinishEntity getData() {
        return data;
    }

    public void setData(ConfirmWorkOrderFinishEntity data) {
        this.data = data;
    }
}
