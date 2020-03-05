package com.xiandao.android.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 此类描述的是:查询进度结果数据
 *
 * @author TanYong
 *         create at 2017/5/21 21:20
 */
public class QueryPlanResult implements Serializable {
    private static final long serialVersionUID = 1L;
    private ArrayList<PlanEntity> planEntityArrayList;
    private String resultCode;
    private String msg;

    public QueryPlanResult(ArrayList<PlanEntity> planEntityArrayList, String resultCode, String msg) {
        this.planEntityArrayList = planEntityArrayList;
        this.resultCode = resultCode;
        this.msg = msg;
    }

    public ArrayList<PlanEntity> getPlanEntityArrayList() {
        return planEntityArrayList;
    }

    public void setPlanEntityArrayList(ArrayList<PlanEntity> planEntityArrayList) {
        this.planEntityArrayList = planEntityArrayList;
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
