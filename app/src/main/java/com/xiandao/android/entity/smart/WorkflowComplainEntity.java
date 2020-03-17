package com.xiandao.android.entity.smart;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Loong on 2020/2/23.
 * Version: 1.0
 * Describe:
 */
public class WorkflowComplainEntity implements Serializable {
    private static final long serialVersionUID = 1L;



    /**
     * id : 170af3c3dd5c8b7c13da68b4b8f95c8f
     * handlerId : b0aa355d5ea911eaab9e000c29b72d7a
     * handlerName : 客服1
     * mobile : 13564521234
     * avatarUrl : http://10.222.1.38:80/group1/M00/00/03/Ct4BJl5gv-uAc2ckAAA6xQG929o578.jpg
     * notAcceptable :
     * comment :
     * nodeName : 检视投诉单
     * operation : 住户APP建单
     * resourceKey :
     * shortDesc : 客服部
     * createTime : 2020-03-06 17:45:26
     * updateTime :
     */

    private String id;
    private String handlerId;
    private String handlerName;
    private String mobile;
    private String avatarUrl;
    private String notAcceptable;
    private String comment;
    private String nodeName;
    private String operation;
    private String resourceKey;
    private String shortDesc;
    private String createTime;
    private String updateTime;
    private List<ResourceEntity> resourceValue;
    private List<OperationInfoEntity> operationInfos;
    private String assigneeId;


    public List<ResourceEntity> getResourceValue() {
        return resourceValue;
    }

    public void setResourceValue(List<ResourceEntity> resourceValue) {
        this.resourceValue = resourceValue;
    }

    public List<OperationInfoEntity> getOperationInfos() {
        return operationInfos;
    }

    public void setOperationInfos(List<OperationInfoEntity> operationInfos) {
        this.operationInfos = operationInfos;
    }

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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getNotAcceptable() {
        return notAcceptable;
    }

    public void setNotAcceptable(String notAcceptable) {
        this.notAcceptable = notAcceptable;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(String assigneeId) {
        this.assigneeId = assigneeId;
    }
}
