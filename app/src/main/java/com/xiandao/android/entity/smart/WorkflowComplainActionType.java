package com.xiandao.android.entity.smart;

/**
 * Created by Loong on 2020/2/6.
 * Version: 1.0
 * Describe: 投诉流程节点枚举
 */
public enum WorkflowComplainActionType {
    ComplainView("检视投诉单",0x01),
    TelContact("电话沟通业主",0x02),
    DistributionDirector("分发职能主管",0x03),
    ScenePhoto("到场查看",0x04),
    WriteHandle("已填投诉解决方案",0x05),
    DistributionEmployee("分发员工实施",0x06),
    CustomerContact("客服沟通业主重新指派",0x07),
    ExecuteHandlePlan("实施投诉解决方案",0x08),
    IsPlanFinish("方案是否完成",0x09),
    ReturnVisit("投诉实施完成，回访业主",0x0a),
    CustomerFeedback("投诉评价",0x0b),
    ContinueToCommunicate("继续沟通",0x0c),
    WriteCorrectiveAction("填写整改措施",0x0c),
    IsCustomerAccept("业主是否接受",0x0e);
    private final String type;
    private final int code;

    WorkflowComplainActionType(String type ,int code) {
        this.type = type;
        this.code=code;
    }

    public String getType() {
        return type;
    }

    public int getCode() {
        return code;
    }
}