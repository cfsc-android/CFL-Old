package com.xiandao.android.entity;

import java.io.Serializable;

/**
 * Created by kss on 2019/4/9
 * Describe:车辆出入记录适配器
 */
public class VisitorRecord implements Serializable {
    private static final long serialVersionUID = 1L;


    /**
     * applyTime : 2019-04-29 09:56:48
     * effectTime : 2019-04-29 09:56:00
     * expireTime : 2019-04-29 14:56:00
     * openTimes : 2
     * qrCodeUrl : https://pic.hik-cloud.com/poseidon/cff3b073d0604d8abacec71dd9f0e844/1556503016902.jpg
     * visitorName : 酷狗
     */

    private String applyTime;
    private String effectTime;
    private String expireTime;
    private int openTimes;
    private String qrCodeUrl;
    private String visitorName;

    public String getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(String applyTime) {
        this.applyTime = applyTime;
    }

    public String getEffectTime() {
        return effectTime;
    }

    public void setEffectTime(String effectTime) {
        this.effectTime = effectTime;
    }

    public String getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }

    public int getOpenTimes() {
        return openTimes;
    }

    public void setOpenTimes(int openTimes) {
        this.openTimes = openTimes;
    }

    public String getQrCodeUrl() {
        return qrCodeUrl;
    }

    public void setQrCodeUrl(String qrCodeUrl) {
        this.qrCodeUrl = qrCodeUrl;
    }

    public String getVisitorName() {
        return visitorName;
    }

    public void setVisitorName(String visitorName) {
        this.visitorName = visitorName;
    }
}
