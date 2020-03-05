package com.xiandao.android.entity.smart;

/**
 * Created by Loong on 2020/2/6.
 * Version: 1.0
 * Describe: 工单流程节点枚举
 */
public enum WorkflowOrderActionType {
    填报工单(0x01),
    指挥中心确认工单(0x02),
    强制关闭工单(0x03),
    接单工单补充(0x04),
    分发工单(0x05),
    员工检视工单(0x06),
    到场拍照(0x07),
    员工接工单(0x08),
    接单(0x09),
    职能主管处理(0x0a),
    强制处理(0x0b),
    客服介入(0x0c),
    客服接单(0x0d),
    员工发送费用(0x0e),
    客户接受价格(0x0f),
    是否缺失材料(0x10),
    备料(0x11),
    采购材料(0x12),
    领料(0x13),
    立即采购(0x14),
    入库(0x15),
    工作(0x16),
    客户确认工作完成(0x17),
    付费(0x18),
    评价(0x19),
    回访(0x1a);

    private final int code;

    WorkflowOrderActionType(int code) {
        this.code=code;
    }
    public int getCode() {
        return code;
    }

}