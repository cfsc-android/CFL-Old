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
     * id : 170684e3835bc3f573c33c547f892b7f
     * complaintId : 1706839244464288cc0764f45e48067b
     * nodeName : 电话沟通业主
     * operation : 电话沟通业主
     * handler : a75d45a015c44384a04449ee80dc3503
     * handlerType : 1
     * createTime : 2020-02-21 23:12:01
     * updateTime :
     * comment :
     * resourceKey :
     * notAcceptable : 1
     * handlerName : 超级管理员
     * mobile :
     * avatarUrl : http://img3.imgtn.bdimg.com/it/u=378824344,1185609431&fm=26&gp=0.jpg
     * shortDesc : 长房数创科技有限公司
     */

    private String id;
    private String complaintId;
    private String nodeName;
    private String operation;
    private String handler;
    private int handlerType;
    private String createTime;
    private String updateTime;
    private String comment;
    private String resourceKey;
    private int notAcceptable;
    private String handlerName;
    private String mobile;
    private String avatarUrl;
    private String shortDesc;
    private List<ResourceEntity> resourceValue;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComplaintId() {
        return complaintId;
    }

    public void setComplaintId(String complaintId) {
        this.complaintId = complaintId;
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

    public String getHandler() {
        return handler;
    }

    public void setHandler(String handler) {
        this.handler = handler;
    }

    public int getHandlerType() {
        return handlerType;
    }

    public void setHandlerType(int handlerType) {
        this.handlerType = handlerType;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getResourceKey() {
        return resourceKey;
    }

    public void setResourceKey(String resourceKey) {
        this.resourceKey = resourceKey;
    }

    public int getNotAcceptable() {
        return notAcceptable;
    }

    public void setNotAcceptable(int notAcceptable) {
        this.notAcceptable = notAcceptable;
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

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public List<ResourceEntity> getResourceValue() {
        return resourceValue;
    }

    public void setResourceValue(List<ResourceEntity> resourceValue) {
        this.resourceValue = resourceValue;
    }
}
