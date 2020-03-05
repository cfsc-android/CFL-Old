package com.xiandao.android.entity;

import java.io.Serializable;

public class HikCar implements Serializable {
    public static final long serialVersionUID=1L;

    /**
     * vehicleId : ddfff934-3632-4891-a9e9-bea8c25e54b6
     * plateNo : 浙A12345
     * isBandPerson : 1
     * personId : f1ed9ade-8057-4d01-bb38-2af26d43083e
     * personName : shunzi
     * phoneNo : null
     * plateType : 0
     * plateColor : 0
     * vehicleType : 1
     * vehicleColor : 1
     * mark : 备注备注
     */

    private String vehicleId;
    private String plateNo;
    private int isBandPerson;
    private String personId;
    private String personName;
    private String phoneNo;
    private int plateType;
    private int plateColor;
    private int vehicleType;
    private int vehicleColor;
    private String mark;

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getPlateNo() {
        return plateNo;
    }

    public void setPlateNo(String plateNo) {
        this.plateNo = plateNo;
    }

    public int getIsBandPerson() {
        return isBandPerson;
    }

    public void setIsBandPerson(int isBandPerson) {
        this.isBandPerson = isBandPerson;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public int getPlateType() {
        return plateType;
    }

    public void setPlateType(int plateType) {
        this.plateType = plateType;
    }

    public int getPlateColor() {
        return plateColor;
    }

    public void setPlateColor(int plateColor) {
        this.plateColor = plateColor;
    }

    public int getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(int vehicleType) {
        this.vehicleType = vehicleType;
    }

    public int getVehicleColor() {
        return vehicleColor;
    }

    public void setVehicleColor(int vehicleColor) {
        this.vehicleColor = vehicleColor;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }
}
