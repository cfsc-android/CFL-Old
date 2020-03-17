package com.xiandao.android.entity.smart;

/**
 * Created by Loong on 2020/2/17.
 * Version: 1.0
 * Describe: 流程分支枚举
 */
public enum WorkflowTrendType {
    Reject("reject"),
    Accept("accept");

    private final String type;
    WorkflowTrendType(String type){
        this.type=type;
    }
    public String getType() {
        return type;
    }
}
