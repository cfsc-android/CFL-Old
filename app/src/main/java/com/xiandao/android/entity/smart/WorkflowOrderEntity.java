package com.xiandao.android.entity.smart;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Loong on 2020/2/17.
 * Version: 1.0
 * Describe: 工单工作流实体类
 */
public class WorkflowOrderEntity implements Serializable {
    private static final long serialVersionUID = 1L;


    /**
     * id : 1706112715997204dfcf23d4986ad245
     * handlerId : a75d45a015c44384a04449ee80dc3503
     * handlerName : 超级管理员
     * avatarUrl : http://img3.imgtn.bdimg.com/it/u=378824344,1185609431&fm=26&gp=0.jpg
     * handlerType : 住户
     * handlerMobile : 13787176775
     * nodeName : 填报工单
     * operation : pc新建工单
     * resourceKey :
     * notAcceptable : 接受
     * remark : 流程实例[435014] 任务[填报工单,usertask1]指派[a75d45a015c44384a04449ee80dc3503]
     * shortDesc : 长房数创科技有限公司
     * createTime : 2020-02-20 13:29:24
     */

    private String id;
    private String handlerId;
    private String handlerName;
    private String avatarUrl;
    private String handlerType;
    private String handlerMobile;
    private String nodeName;
    private String operation;
    private String resourceKey;
    private String notAcceptable;
    private String remark;
    private String shortDesc;
    private String createTime;
    private List<ResourceEntity> resourceValues;
    private List<OperationInfoEntity> operationInfos;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHandlerId() {
        return handlerId;
    }

    public void setHandlerId(String handlerId) {
        this.handlerId = handlerId;
    }

    public String getHandlerName() {
        return handlerName;
    }

    public void setHandlerName(String handlerName) {
        this.handlerName = handlerName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getHandlerType() {
        return handlerType;
    }

    public void setHandlerType(String handlerType) {
        this.handlerType = handlerType;
    }

    public String getHandlerMobile() {
        return handlerMobile;
    }

    public void setHandlerMobile(String handlerMobile) {
        this.handlerMobile = handlerMobile;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getResourceKey() {
        return resourceKey;
    }

    public void setResourceKey(String resourceKey) {
        this.resourceKey = resourceKey;
    }

    public String getNotAcceptable() {
        return notAcceptable;
    }

    public void setNotAcceptable(String notAcceptable) {
        this.notAcceptable = notAcceptable;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public List<ResourceEntity> getResourceValues() {
        return resourceValues;
    }

    public void setResourceValues(List<ResourceEntity> resourceValues) {
        this.resourceValues = resourceValues;
    }

    public List<OperationInfoEntity> getOperationInfos() {
        return operationInfos;
    }

    public void setOperationInfos(List<OperationInfoEntity> operationInfos) {
        this.operationInfos = operationInfos;
    }
}
