package com.xiandao.android.entity.smart;

import java.io.Serializable;

/**
 * Created by Loong on 2020/2/29.
 * Version: 1.0
 * Describe:
 */
public class DeviceEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * deviceSerial : D16444599
     * devicePlatformId : bca35d1384a84bb4a4055918964c865d
     * deviceName : test-607
     */

    private String deviceSerial;
    private String devicePlatformId;
    private String deviceName;

    public String getDeviceSerial() {
        return deviceSerial;
    }

    public void setDeviceSerial(String deviceSerial) {
        this.deviceSerial = deviceSerial;
    }

    public String getDevicePlatformId() {
        return devicePlatformId;
    }

    public void setDevicePlatformId(String devicePlatformId) {
        this.devicePlatformId = devicePlatformId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
}
