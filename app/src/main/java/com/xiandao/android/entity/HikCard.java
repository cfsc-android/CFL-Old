package com.xiandao.android.entity;

import java.io.Serializable;

public class HikCard implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * cardId : c0bf80fc-a9f5-11e8-bc13-674008df3d9a
     * cardNo : 77777777
     * personId : 8ffb3b91-f823-42b6-8fca-137bff553857
     * personName : 张三丰
     * useStatus : 1
     * startDate : 2018-08-27T00:00:00:000+08:00
     * endDate : 2037-12-31T23:59:59:000+08:00
     * lossDate : 2037-12-31T23:59:59:000+08:00
     * unlossDate : 2037-12-31T23:59:59:000+08:00
     */

    private String cardId;
    private String cardNo;
    private String personId;
    private String personName;
    private int useStatus;
    private String startDate;
    private String endDate;
    private String lossDate;
    private String unlossDate;

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
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

    public int getUseStatus() {
        return useStatus;
    }

    public void setUseStatus(int useStatus) {
        this.useStatus = useStatus;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getLossDate() {
        return lossDate;
    }

    public void setLossDate(String lossDate) {
        this.lossDate = lossDate;
    }

    public String getUnlossDate() {
        return unlossDate;
    }

    public void setUnlossDate(String unlossDate) {
        this.unlossDate = unlossDate;
    }
}
