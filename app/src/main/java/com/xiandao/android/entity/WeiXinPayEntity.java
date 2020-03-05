package com.xiandao.android.entity;

import java.io.Serializable;

public class WeiXinPayEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    //应用ID
    private String appid;
    //商户号
    private String partnerid;
    //预支付交易会话ID
    private String prepayid;
    //随机字符串
    private String noncestr;
    //时间戳
    private String timestamp;
    //签名
    private String sign;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
