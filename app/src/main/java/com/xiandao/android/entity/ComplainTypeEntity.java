package com.xiandao.android.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 此类描述的是:投诉类型返回数据对象
 *
 * @author TanYong
 *         create at 2017/6/1 15:27
 */
public class ComplainTypeEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private String resultCode;
    private String msg;
    private ArrayList<SpinnerEntity> complainTypeList;

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

    public ArrayList<SpinnerEntity> getComplainTypeList() {
        return complainTypeList;
    }

    public void setComplainTypeList(ArrayList<SpinnerEntity> complainTypeList) {
        this.complainTypeList = complainTypeList;
    }
}
