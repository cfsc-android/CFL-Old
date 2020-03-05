package com.xiandao.android.entity.hikcloud;

import java.io.Serializable;

/**
 * Created by zengx on 2019/5/8.
 * Describe:
 */
public class VideoCallPush implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * buildingNumber : 1
     * cmdType : request
     * dateTime : 2019-05-08T16:48:40+08:00
     * devIndex : 0
     * deviceSerial : 231857475
     * floorNumber : 6
     * periodNumber : 1
     * roomNumber : 1
     * unitNumber : 1
     * unitType : outdoor
     */

    private int buildingNumber;
    private String cmdType;
    private String dateTime;
    private int devIndex;
    private String deviceSerial;
    private int floorNumber;
    private int periodNumber;
    private int roomNumber;
    private int unitNumber;
    private String unitType;

    public int getBuildingNumber() {
        return buildingNumber;
    }

    public void setBuildingNumber(int buildingNumber) {
        this.buildingNumber = buildingNumber;
    }

    public String getCmdType() {
        return cmdType;
    }

    public void setCmdType(String cmdType) {
        this.cmdType = cmdType;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public int getDevIndex() {
        return devIndex;
    }

    public void setDevIndex(int devIndex) {
        this.devIndex = devIndex;
    }

    public String getDeviceSerial() {
        return deviceSerial;
    }

    public void setDeviceSerial(String deviceSerial) {
        this.deviceSerial = deviceSerial;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(int floorNumber) {
        this.floorNumber = floorNumber;
    }

    public int getPeriodNumber() {
        return periodNumber;
    }

    public void setPeriodNumber(int periodNumber) {
        this.periodNumber = periodNumber;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public int getUnitNumber() {
        return unitNumber;
    }

    public void setUnitNumber(int unitNumber) {
        this.unitNumber = unitNumber;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }
}
