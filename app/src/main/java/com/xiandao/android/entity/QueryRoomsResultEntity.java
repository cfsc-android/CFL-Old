package com.xiandao.android.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 此类描述的是:查询房间返回数据对象
 *
 * @author TanYong
 *         create at 2017/5/18 10:33
 */
public class QueryRoomsResultEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private ArrayList<SpinnerEntity> entities;
    private String resultCode;
    private String msg;

    public ArrayList<SpinnerEntity> getEntities() {
        return entities;
    }

    public void setEntities(ArrayList<SpinnerEntity> entities) {
        this.entities = entities;
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
