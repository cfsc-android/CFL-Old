package com.xiandao.android.entity;

import java.io.Serializable;

/**
 * Created by zengx on 2019/5/22.
 * Describe:
 */
public class CarManageEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String vehiclecode;
    private String plateType;
    private String plateColor;
    private String carType;
    private String carColor;
    private String carPhoto;
    private int vehiclestatus;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVehiclestatus() {
        return vehiclestatus;
    }

    public void setVehiclestatus(int vehiclestatus) {
        this.vehiclestatus = vehiclestatus;
    }

    public String getVehiclecode() {
        return vehiclecode;
    }

    public void setVehiclecode(String vehiclecode) {
        this.vehiclecode = vehiclecode;
    }

    public String getPlateType() {
        return plateType;
    }

    public void setPlateType(String plateType) {
        this.plateType = plateType;
    }

    public String getPlateColor() {
        return plateColor;
    }

    public void setPlateColor(String plateColor) {
        this.plateColor = plateColor;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getCarColor() {
        return carColor;
    }

    public void setCarColor(String carColor) {
        this.carColor = carColor;
    }

    public String getCarPhoto() {
        return carPhoto;
    }

    public void setCarPhoto(String carPhoto) {
        this.carPhoto = carPhoto;
    }

    @Override
    public String toString() {
        return "CarManageEntity{" +
                "vehiclecode='" + vehiclecode + '\'' +
                ", plateType='" + plateType + '\'' +
                ", plateColor='" + plateColor + '\'' +
                ", carType='" + carType + '\'' +
                ", carColor='" + carColor + '\'' +
                ", carPhoto='" + carPhoto + '\'' +
                ", vehiclestatus=" + vehiclestatus +
                '}';
    }
}
