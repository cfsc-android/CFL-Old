package com.xiandao.android.entity;

import java.io.Serializable;

public class VideoEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * deviceId :
     * deviceName :
     * deviceModel :
     * deviceSerial :
     * deviceStatus : 1
     * groupId :
     */

    private String deviceId;
    private String deviceName;
    private String deviceModel;
    private String deviceSerial;
    private int deviceStatus;
    private String groupId;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getDeviceSerial() {
        return deviceSerial;
    }

    public void setDeviceSerial(String deviceSerial) {
        this.deviceSerial = deviceSerial;
    }

    public int getDeviceStatus() {
        return deviceStatus;
    }

    public void setDeviceStatus(int deviceStatus) {
        this.deviceStatus = deviceStatus;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
