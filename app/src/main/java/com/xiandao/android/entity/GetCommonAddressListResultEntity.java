package com.xiandao.android.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 此类描述的是:获取常用地址返回数据
 *
 * @author TanYong
 *         create at 2017/5/18 10:33
 */
public class GetCommonAddressListResultEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private ArrayList<RoomInfoEntity> roomEntityList;
    private String resultCode;
    private String msg;

    public ArrayList<RoomInfoEntity> getRoomEntityList() {
        return roomEntityList;
    }

    public void setRoomEntityList(ArrayList<RoomInfoEntity> roomEntityList) {
        this.roomEntityList = roomEntityList;
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
