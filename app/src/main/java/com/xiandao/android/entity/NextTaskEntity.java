package com.xiandao.android.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 此类描述的是:进入下一步任务对象
 *
 * @author TanYong
 *         create at 2017/6/12 0:20
 */
public class NextTaskEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private ArrayList<String> jbpmOutcomes;
    private String taskId;

    public ArrayList<String> getJbpmOutcomes() {
        return jbpmOutcomes;
    }

    public void setJbpmOutcomes(ArrayList<String> jbpmOutcomes) {
        this.jbpmOutcomes = jbpmOutcomes;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
}
