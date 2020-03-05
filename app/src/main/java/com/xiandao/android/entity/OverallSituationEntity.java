package com.xiandao.android.entity;

import java.io.Serializable;

/**
 * 此类描述的是:OverallSituation对象
 *
 * @author TanYong
 *         create at 2017/4/21 9:45
 */
public class OverallSituationEntity implements Serializable {

    public void setData(OverallSituationEntity entity) {
        this.resultCode = entity.getResultCode();
        this.msg = entity.getMsg();
        this.data = entity.getData();
    }

    private String resultCode = "";//返回状态码
    private String msg = "";//返回描述信息
    private Object data;//返回数据

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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
