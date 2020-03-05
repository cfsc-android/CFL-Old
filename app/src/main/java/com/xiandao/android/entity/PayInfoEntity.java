package com.xiandao.android.entity;

import java.io.Serializable;

/**
 * 此类描述的是:支付信息对象
 * <p>
 * "data": {
 * "id": null,
 * "payid": "74D5B5506C074A8D887BC9F0E77D417B",
 * "operationuser": "20",
 * "paylistids": "21",
 * "roomid": "14",
 * "payamount": "1",
 * "paytype": "通联支付",
 * "iphonetype": "0",
 * "createdate": "2017-12-12 14:26:40",
 * "ispay": "0"
 * },
 *
 * @author TanYong
 *         create at 2017/5/18 10:33
 */
public class PayInfoEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String payid;
    private String operationuser;
    private String paylistids;
    private String roomid;
    private String payamount;
    private String paytype;
    private String iphonetype;
    private String createdate;
    private String ispay;
    private String createdatefm;
    private String userid;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPayid() {
        return payid;
    }

    public void setPayid(String payid) {
        this.payid = payid;
    }

    public String getOperationuser() {
        return operationuser;
    }

    public void setOperationuser(String operationuser) {
        this.operationuser = operationuser;
    }

    public String getPaylistids() {
        return paylistids;
    }

    public void setPaylistids(String paylistids) {
        this.paylistids = paylistids;
    }

    public String getRoomid() {
        return roomid;
    }

    public void setRoomid(String roomid) {
        this.roomid = roomid;
    }

    public String getPayamount() {
        return payamount;
    }

    public void setPayamount(String payamount) {
        this.payamount = payamount;
    }

    public String getPaytype() {
        return paytype;
    }

    public void setPaytype(String paytype) {
        this.paytype = paytype;
    }

    public String getIphonetype() {
        return iphonetype;
    }

    public void setIphonetype(String iphonetype) {
        this.iphonetype = iphonetype;
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }

    public String getIspay() {
        return ispay;
    }

    public void setIspay(String ispay) {
        this.ispay = ispay;
    }

    public String getCreatedatefm() {
        return createdatefm;
    }

    public void setCreatedatefm(String createdatefm) {
        this.createdatefm = createdatefm;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
