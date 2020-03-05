package com.xiandao.android.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 此类描述的是:我的投诉列表对象
 *
 * @author TanYong
 *         create at 2017/5/8 16:39
 *         <p>
 *         {"msg":"查询成功","data":{"pagination":{"totalResults":1,"totalPages":1,"pageSize":10,"currentPage":1},
 *         "complainEntityList":[{"piid":"Complaints.810001","complainStatus":"未受理",
 *         "complainId":53,"resourcekey":"fbc75851-83b3-4aa3-bb05-21f665920d13","complainTitle":"测试数据1",
 *         "complainImageUrl":[{"url":"upload/complains/2_20170609101002_1_yt.png"},{"url":"upload/complains/2_20170609101002_2_yt.png"},
 *         {"url":"upload/complains/2_20170609101002_3_yt.png"}],"complainDateTime":"2017-06-09 10:10:03","jbpmOutcomes":[]}]},
 *         "resultCode":"0"}
 */
public class MyComplainListEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private ArrayList<MyComplainEntity> complainEntityList;
    private Pagination pagination;

    private String resultCode;
    private String msg;

    public ArrayList<MyComplainEntity> getComplainEntityList() {
        return complainEntityList;
    }

    public void setComplainEntityList(ArrayList<MyComplainEntity> complainEntityList) {
        this.complainEntityList = complainEntityList;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
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
