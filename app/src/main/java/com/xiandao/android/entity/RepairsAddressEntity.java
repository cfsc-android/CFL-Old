package com.xiandao.android.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by 谭勇 on 2017/4/26.
 */

public class RepairsAddressEntity implements Serializable{
    private static final long serialVersionUID = 1L;
    private String resultCode;
    private String msg;
    private ArrayList<SpinnerEntity> repairsTypeList;

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

    public ArrayList<SpinnerEntity> getRepairsTypeList() {
        return repairsTypeList;
    }

    public void setRepairsTypeList(ArrayList<SpinnerEntity> repairsTypeList) {
        this.repairsTypeList = repairsTypeList;
    }
}
