package com.xiandao.android.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 此类描述的是:我的投诉列表对象
 *
 * @author TanYong
 *         create at 2017/6/1 19:11
 *         "complainEntityList":[{"piid":"Complaints.810001","complainStatus":"未受理",
 *         "complainId":53,"resourcekey":"fbc75851-83b3-4aa3-bb05-21f665920d13","complainTitle":"测试数据1",
 *         "complainImageUrl":[{"url":"upload/complains/2_20170609101002_1_yt.png"},{"url":"upload/complains/2_20170609101002_2_yt.png"},
 *         {"url":"upload/complains/2_20170609101002_3_yt.png"}],"complainDateTime":"2017-06-09 10:10:03","jbpmOutcomes":[]}]},
 */
public class MyComplainEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private String piid;
    private String complainStatus;
    private String handerresourcekey;
    private String complainId;
    private String resourcekey;
    private String complainTitle;
    private ArrayList<ImageUrlEntity> complainImageUrl;
    private String complainDateTime;
    private ArrayList<String> jbpmOutcomes;
    private String taskid;

    public String getPiid() {
        return piid;
    }

    public void setPiid(String piid) {
        this.piid = piid;
    }

    public String getComplainStatus() {
        return complainStatus;
    }

    public void setComplainStatus(String complainStatus) {
        this.complainStatus = complainStatus;
    }

    public String getHanderresourcekey() {
        return handerresourcekey;
    }

    public void setHanderresourcekey(String handerresourcekey) {
        this.handerresourcekey = handerresourcekey;
    }

    public String getComplainId() {
        return complainId;
    }

    public void setComplainId(String complainId) {
        this.complainId = complainId;
    }

    public String getResourcekey() {
        return resourcekey;
    }

    public void setResourcekey(String resourcekey) {
        this.resourcekey = resourcekey;
    }

    public String getComplainTitle() {
        return complainTitle;
    }

    public void setComplainTitle(String complainTitle) {
        this.complainTitle = complainTitle;
    }

    public ArrayList<ImageUrlEntity> getComplainImageUrl() {
        return complainImageUrl;
    }

    public void setComplainImageUrl(ArrayList<ImageUrlEntity> complainImageUrl) {
        this.complainImageUrl = complainImageUrl;
    }

    public String getComplainDateTime() {
        return complainDateTime;
    }

    public void setComplainDateTime(String complainDateTime) {
        this.complainDateTime = complainDateTime;
    }

    public ArrayList<String> getJbpmOutcomes() {
        return jbpmOutcomes;
    }

    public void setJbpmOutcomes(ArrayList<String> jbpmOutcomes) {
        this.jbpmOutcomes = jbpmOutcomes;
    }

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }
}
