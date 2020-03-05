package com.xiandao.android.entity;

import java.io.Serializable;

/**
 * Created by zengx on 2019/5/17.
 * Describe:
 */
public class DeviceInfoEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private String location;
    private String deviceSerial;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDeviceSerial() {
        return deviceSerial;
    }

    public void setDeviceSerial(String deviceSerial) {
        this.deviceSerial = deviceSerial;
    }
}
