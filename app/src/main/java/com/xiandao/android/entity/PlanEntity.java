package com.xiandao.android.entity;

import java.io.Serializable;

/**
 * 此类描述的是:工单或者投诉处理进度对象
 *
 * @author TanYong
 *         create at 2017/5/21 21:15
 *         <p>
 *         "planDateTime": "2017-05-20 18:12:01",
 *         "jobId": 42,
 *         "handler": 4,
 *         "piclist": "",
 *         "handlertype": "1",
 *         "planName": "客服接单",
 *         "planId": 95,
 *         "handlername": "用户（指挥中心）"
 */
public class PlanEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private String planDateTime;//进度生成时间
    private String handlertype;//哪个客户端操作的进度:0业主1员工
    private String planName;//进度名称
    private String planId;//进度ID
    private String notAcceptable;//不接受:用于判断业主是否不接受：0不接受，1接受

    private String remark;

    public String getPlanDateTime() {
        return planDateTime;
    }

    public void setPlanDateTime(String planDateTime) {
        this.planDateTime = planDateTime;
    }

    public String getHandlertype() {
        return handlertype;
    }

    public void setHandlertype(String handlertype) {
        this.handlertype = handlertype;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getNotAcceptable() {
        return notAcceptable;
    }

    public void setNotAcceptable(String notAcceptable) {
        this.notAcceptable = notAcceptable;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
