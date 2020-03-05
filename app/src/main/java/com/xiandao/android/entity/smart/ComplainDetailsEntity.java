package com.xiandao.android.entity.smart;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Loong on 2020/2/19.
 * Version: 1.0
 * Describe: 工单详情实体类
 */
public class ComplainDetailsEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private ComplainEntity complaint;
    private List<WorkflowComplainEntity> cfcComplaintDetailsVo;

    public ComplainEntity getComplaint() {
        return complaint;
    }

    public void setComplaint(ComplainEntity complaint) {
        this.complaint = complaint;
    }

    public List<WorkflowComplainEntity> getCfcComplaintDetailsVo() {
        return cfcComplaintDetailsVo;
    }

    public void setCfcComplaintDetailsVo(List<WorkflowComplainEntity> cfcComplaintDetailsVo) {
        this.cfcComplaintDetailsVo = cfcComplaintDetailsVo;
    }
}
