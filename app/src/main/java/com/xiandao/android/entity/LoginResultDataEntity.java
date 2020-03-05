package com.xiandao.android.entity;

import org.xutils.common.task.Priority;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 此类描述的是:登录接口返回的数据对象
 *
 * @author TanYong
 *         create at 2017/4/25 14:19
 */
public class LoginResultDataEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private String token;
    private LoginUserEntity userInfo;
    private ArrayList<ThirdInfoEntity> thirdInfo;
    private ArrayList<DeviceInfoEntity> deviceInfo;
    private ArrayList<RoomInfoEntity> roominfo;
    private OverallSituationEntity osEntity;

    public ArrayList<ThirdInfoEntity> getThirdInfo() {
        return thirdInfo;
    }

    public void setThirdInfo(ArrayList<ThirdInfoEntity> thirdInfo) {
        this.thirdInfo = thirdInfo;
    }

    public ArrayList<DeviceInfoEntity> getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(ArrayList<DeviceInfoEntity> deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public ArrayList<RoomInfoEntity> getRoominfo() {
        return roominfo;
    }

    public void setRoominfo(ArrayList<RoomInfoEntity> roominfo) {
        this.roominfo = roominfo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LoginUserEntity getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(LoginUserEntity userInfo) {
        this.userInfo = userInfo;
    }

    public OverallSituationEntity getOsEntity() {
        return osEntity;
    }

    public void setOsEntity(OverallSituationEntity osEntity) {
        this.osEntity = osEntity;
    }
}
