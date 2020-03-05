package com.xiandao.android.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 此类描述的是:我的报修对象
 *
 * @author TanYong
 *         create at 2017/5/8 16:01
 *         <p>
 *         {
 *         "repairsStatus": "未受理",
 *         "piid": "gongdan_newAdd.690033",
 *         "repairsTitle": "111",
 *         "repairsTypeName": "空调维修个j8",
 *         "repairsTime": "2017-05-22 15:36:59",
 *         "jbpmOutcomes": [],
 *         "repairsId": 63
 *         }
 */
public class MyRepairsEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private String repairsId;//报修ID
    private String repairsTime;//报修时间
    private String repairsTitle;//报修标题
    private String repairsTypeId;//报修类别ID:根据不同的报修类别显示不同的头图片

    private String piid;
    private String repairsStatus;
    private String repairsTypeName;
    private String address;//报修地址
    private ArrayList<String> jbpmOutcomes;

    private String taskid;

    public String getRepairsId() {
        return repairsId;
    }

    public void setRepairsId(String repairsId) {
        this.repairsId = repairsId;
    }

    public String getRepairsTime() {
        return repairsTime;
    }

    public void setRepairsTime(String repairsTime) {
        this.repairsTime = repairsTime;
    }

    public String getRepairsTitle() {
        return repairsTitle;
    }

    public void setRepairsTitle(String repairsTitle) {
        this.repairsTitle = repairsTitle;
    }

    public String getRepairsTypeId() {
        return repairsTypeId;
    }

    public void setRepairsTypeId(String repairsTypeId) {
        this.repairsTypeId = repairsTypeId;
    }

    public String getPiid() {
        return piid;
    }

    public void setPiid(String piid) {
        this.piid = piid;
    }

    public String getRepairsStatus() {
        return repairsStatus;
    }

    public void setRepairsStatus(String repairsStatus) {
        this.repairsStatus = repairsStatus;
    }

    public String getRepairsTypeName() {
        return repairsTypeName;
    }

    public void setRepairsTypeName(String repairsTypeName) {
        this.repairsTypeName = repairsTypeName;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
