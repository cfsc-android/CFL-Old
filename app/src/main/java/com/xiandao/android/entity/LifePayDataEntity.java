package com.xiandao.android.entity;

import java.io.Serializable;

/**
 * 生活缴费对象
 * <p>
 * {"fee":"1",
 * "adds":"长沙西中心-33-四单元-测试房屋",
 * "term":"2017年第四季度(10-12月)",
 * "shouldpaydate":"2017-10-15 15:27:00.0",
 * "advancetype":"1",
 * "ifpay":null,
 * "paydate":null,
 * "roomid":null}]
 */
public class LifePayDataEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String fee;
    private String adds;
    private String term;
    private String shouldpaydate;
    private String advancetype;
    private String ifpay;
    private String paydate;
    private String roomid;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getAdds() {
        return adds;
    }

    public void setAdds(String adds) {
        this.adds = adds;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getShouldpaydate() {
        return shouldpaydate;
    }

    public void setShouldpaydate(String shouldpaydate) {
        this.shouldpaydate = shouldpaydate;
    }

    public String getAdvancetype() {
        return advancetype;
    }

    public void setAdvancetype(String advancetype) {
        this.advancetype = advancetype;
    }

    public String getIfpay() {
        return ifpay;
    }

    public void setIfpay(String ifpay) {
        this.ifpay = ifpay;
    }

    public String getPaydate() {
        return paydate;
    }

    public void setPaydate(String paydate) {
        this.paydate = paydate;
    }

    public String getRoomid() {
        return roomid;
    }

    public void setRoomid(String roomid) {
        this.roomid = roomid;
    }
}
