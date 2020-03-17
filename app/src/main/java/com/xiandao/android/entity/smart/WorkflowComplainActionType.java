package com.xiandao.android.entity.smart;

/**
 * Created by Loong on 2020/2/6.
 * Version: 1.0
 * Describe: 投诉流程节点枚举
 */
public enum WorkflowComplainActionType {
    检视投诉单(0x01),
    电话沟通业主(0x02),
    分发职能主管(0x03),
    到场查看(0x04),
    业主确认方案(0x05),
    分发员工实施(0x06),
    客服沟通业主重新指派(0x07),
    实施投诉解决方案(0x08),
    方案是否完成(0x09),
    回访业主(0x0a),
    投诉评价(0x0b),
    继续沟通(0x0c),
    填写整改措施(0x0d),
    业主是否接受(0x0e);

    private final int code;

    WorkflowComplainActionType(int code) {
        this.code=code;
    }
    public int getCode() {
        return code;
    }

}