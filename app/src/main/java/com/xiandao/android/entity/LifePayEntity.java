package com.xiandao.android.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 生活缴费对象
 * <p>
 * {"data":[{"fee":"1",
 * "adds":"长沙西中心-33-四单元-测试房屋",
 * "term":"2017年第四季度(10-12月)",
 * "shouldpaydate":"2017-09-15 15:27:00.0",
 * "advancetype":"0",
 * "ifpay":null,
 * "paydate":null,
 * "roomid":null},
 * {"fee":"1",
 * "adds":"长沙西中心-33-四单元-测试房屋",
 * "term":"2017年第四季度(10-12月)",
 * "shouldpaydate":"2017-10-15 15:27:00.0",
 * "advancetype":"1",
 * "ifpay":null,
 * "paydate":null,
 * "roomid":null}],
 * "resultCode":"0",
 * "msg":"查询成功。"}
 */
public class LifePayEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private ArrayList<LifePayDataEntity> data;
    private String resultCode;
    private String msg;

    public ArrayList<LifePayDataEntity> getData() {
        return data;
    }

    public void setData(ArrayList<LifePayDataEntity> data) {
        this.data = data;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
