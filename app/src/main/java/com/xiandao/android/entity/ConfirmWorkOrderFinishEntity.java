package com.xiandao.android.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 此类描述的是:用来判断是跳转到评价界面还是支付界面
 * @author TanYong
 * create at 2017/5/26 20:50
 */
public class ConfirmWorkOrderFinishEntity implements Serializable{
    private static final long serialVersionUID = 1L;

    private String taskId;
    private ArrayList<String> jbpmOutcomes;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public ArrayList<String> getJbpmOutcomes() {
        return jbpmOutcomes;
    }

    public void setJbpmOutcomes(ArrayList<String> jbpmOutcomes) {
        this.jbpmOutcomes = jbpmOutcomes;
    }
}
