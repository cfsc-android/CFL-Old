package com.xiandao.android.entity.smart;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Loong on 2020/2/14.
 * Version: 1.0
 * Describe:
 */
public class ComplainEntity implements Serializable {
    private static final long serialVersionUID = 1L;


    /**
     * id : 1706839244464288cc0764f45e48067b
     * roomId : 16fa72dd4e7034b8aa5a2af40d1972ce
     * projectId : ec93bb06f5be4c1f19522ca78180e2i9
     * typeId : 1
     * statusId : 11
     * csacceptor : a75d45a015c44384a04449ee80dc3503
     * deparmentLeader : 16f8953af1c7d06dde0843b4f6181c81
     * lastHandler : 16f7fc9d8d87741402393e049e9bc1d0
     * complainTime : 2020-02-21 22:49:00
     * content : pc端客服帮业主建投诉单---> hezhou
     * csacceptTime : 2020-02-21 23:11:58
     * rejectedTime :
     * handerStatus : 1
     * isfinish : 0
     * finishType :
     * nosatisfyDesc :
     * householdId : 16fa73e5607ee21f4ff5e0842e7b5d4c
     * piid : 502501
     * resourcekey : 33b112ec53ad11eaab9e000c29b72d7a
     * solveType : 0
     * solutionId :
     * reportType : 1
     * checkResourcekey : 173c9352de654628b56cf925d30996b4
     * handerTime : 2020-02-22 17:27:54
     * checkContent : 职能主管到场查看内容1
     * solutionContent : 这是客服主管出的解决方案
     * solutionDate : 2020-02-22 20:04:59
     * esolutionContent : 基本以已经完成了aa
     * esolutionDate : 2020-02-22 21:06:50
     * esolutionResourcekey : 173c9352de654628b56cf925d30996b4
     * comment :
     * emerg :
     * commentLevel :
     * measureContent :
     * measureDate :
     * chatContent :
     * complainBy : a75d45a015c44384a04449ee80dc3503
     * projectName : 长房明宸府
     * complaintTypeName : 物业投诉
     * complaintStatusName : 整改措施已完成
     * roomNameAll : 1栋 一单元 3202
     * householdName : 马冬梅
     * csacceptorName : 超级管理员
     * deparmentLeaderName : 超管
     * lastHandlerName : 测试用户
     * householdMobile :
     * creatorAvatarUrl : http://img3.imgtn.bdimg.com/it/u=378824344,1185609431&fm=26&gp=0.jpg
     */

    private String id;
    private String roomId;
    private String projectId;
    private int typeId;
    private int statusId;
    private String csacceptor;
    private String deparmentLeader;
    private String lastHandler;
    private String complainTime;
    private String content;
    private String csacceptTime;
    private String rejectedTime;
    private int handerStatus;
    private int isfinish;
    private String finishType;
    private String nosatisfyDesc;
    private String householdId;
    private String piid;
    private String resourcekey;
    private int solveType;
    private String solutionId;
    private int reportType;
    private String checkResourcekey;
    private String handerTime;
    private String checkContent;
    private String solutionContent;
    private String solutionDate;
    private String esolutionContent;
    private String esolutionDate;
    private String esolutionResourcekey;
    private String comment;
    private String emerg;
    private String commentLevel;
    private String measureContent;
    private String measureDate;
    private String chatContent;
    private String complainBy;
    private String projectName;
    private String complaintTypeName;
    private String complaintStatusName;
    private String roomNameAll;
    private String householdName;
    private String csacceptorName;
    private String deparmentLeaderName;
    private String lastHandlerName;
    private String householdMobile;
    private String creatorAvatarUrl;
    private List<ResourceEntity> resourceValue;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public String getCsacceptor() {
        return csacceptor;
    }

    public void setCsacceptor(String csacceptor) {
        this.csacceptor = csacceptor;
    }

    public String getDeparmentLeader() {
        return deparmentLeader;
    }

    public void setDeparmentLeader(String deparmentLeader) {
        this.deparmentLeader = deparmentLeader;
    }

    public String getLastHandler() {
        return lastHandler;
    }

    public void setLastHandler(String lastHandler) {
        this.lastHandler = lastHandler;
    }

    public String getComplainTime() {
        return complainTime;
    }

    public void setComplainTime(String complainTime) {
        this.complainTime = complainTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCsacceptTime() {
        return csacceptTime;
    }

    public void setCsacceptTime(String csacceptTime) {
        this.csacceptTime = csacceptTime;
    }

    public String getRejectedTime() {
        return rejectedTime;
    }

    public void setRejectedTime(String rejectedTime) {
        this.rejectedTime = rejectedTime;
    }

    public int getHanderStatus() {
        return handerStatus;
    }

    public void setHanderStatus(int handerStatus) {
        this.handerStatus = handerStatus;
    }

    public int getIsfinish() {
        return isfinish;
    }

    public void setIsfinish(int isfinish) {
        this.isfinish = isfinish;
    }

    public String getFinishType() {
        return finishType;
    }

    public void setFinishType(String finishType) {
        this.finishType = finishType;
    }

    public String getNosatisfyDesc() {
        return nosatisfyDesc;
    }

    public void setNosatisfyDesc(String nosatisfyDesc) {
        this.nosatisfyDesc = nosatisfyDesc;
    }



    public String getPiid() {
        return piid;
    }

    public void setPiid(String piid) {
        this.piid = piid;
    }

    public String getResourcekey() {
        return resourcekey;
    }

    public void setResourcekey(String resourcekey) {
        this.resourcekey = resourcekey;
    }

    public int getSolveType() {
        return solveType;
    }

    public void setSolveType(int solveType) {
        this.solveType = solveType;
    }

    public String getSolutionId() {
        return solutionId;
    }

    public void setSolutionId(String solutionId) {
        this.solutionId = solutionId;
    }

    public int getReportType() {
        return reportType;
    }

    public void setReportType(int reportType) {
        this.reportType = reportType;
    }

    public String getCheckResourcekey() {
        return checkResourcekey;
    }

    public void setCheckResourcekey(String checkResourcekey) {
        this.checkResourcekey = checkResourcekey;
    }

    public String getHanderTime() {
        return handerTime;
    }

    public void setHanderTime(String handerTime) {
        this.handerTime = handerTime;
    }

    public String getCheckContent() {
        return checkContent;
    }

    public void setCheckContent(String checkContent) {
        this.checkContent = checkContent;
    }

    public String getSolutionContent() {
        return solutionContent;
    }

    public void setSolutionContent(String solutionContent) {
        this.solutionContent = solutionContent;
    }

    public String getSolutionDate() {
        return solutionDate;
    }

    public void setSolutionDate(String solutionDate) {
        this.solutionDate = solutionDate;
    }

    public String getEsolutionContent() {
        return esolutionContent;
    }

    public void setEsolutionContent(String esolutionContent) {
        this.esolutionContent = esolutionContent;
    }

    public String getEsolutionDate() {
        return esolutionDate;
    }

    public void setEsolutionDate(String esolutionDate) {
        this.esolutionDate = esolutionDate;
    }

    public String getEsolutionResourcekey() {
        return esolutionResourcekey;
    }

    public void setEsolutionResourcekey(String esolutionResourcekey) {
        this.esolutionResourcekey = esolutionResourcekey;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getEmerg() {
        return emerg;
    }

    public void setEmerg(String emerg) {
        this.emerg = emerg;
    }

    public String getCommentLevel() {
        return commentLevel;
    }

    public void setCommentLevel(String commentLevel) {
        this.commentLevel = commentLevel;
    }

    public String getMeasureContent() {
        return measureContent;
    }

    public void setMeasureContent(String measureContent) {
        this.measureContent = measureContent;
    }

    public String getMeasureDate() {
        return measureDate;
    }

    public void setMeasureDate(String measureDate) {
        this.measureDate = measureDate;
    }

    public String getChatContent() {
        return chatContent;
    }

    public void setChatContent(String chatContent) {
        this.chatContent = chatContent;
    }

    public String getComplainBy() {
        return complainBy;
    }

    public void setComplainBy(String complainBy) {
        this.complainBy = complainBy;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getComplaintTypeName() {
        return complaintTypeName;
    }

    public void setComplaintTypeName(String complaintTypeName) {
        this.complaintTypeName = complaintTypeName;
    }

    public String getComplaintStatusName() {
        return complaintStatusName;
    }

    public void setComplaintStatusName(String complaintStatusName) {
        this.complaintStatusName = complaintStatusName;
    }

    public String getRoomNameAll() {
        return roomNameAll;
    }

    public void setRoomNameAll(String roomNameAll) {
        this.roomNameAll = roomNameAll;
    }


    public String getCsacceptorName() {
        return csacceptorName;
    }

    public void setCsacceptorName(String csacceptorName) {
        this.csacceptorName = csacceptorName;
    }

    public String getDeparmentLeaderName() {
        return deparmentLeaderName;
    }

    public void setDeparmentLeaderName(String deparmentLeaderName) {
        this.deparmentLeaderName = deparmentLeaderName;
    }

    public String getLastHandlerName() {
        return lastHandlerName;
    }

    public void setLastHandlerName(String lastHandlerName) {
        this.lastHandlerName = lastHandlerName;
    }


    public String getHouseholdMobile() {
        return householdMobile;
    }

    public void setHouseholdMobile(String householdMobile) {
        this.householdMobile = householdMobile;
    }

    public String getCreatorAvatarUrl() {
        return creatorAvatarUrl;
    }

    public void setCreatorAvatarUrl(String creatorAvatarUrl) {
        this.creatorAvatarUrl = creatorAvatarUrl;
    }


    public List<ResourceEntity> getResourceValue() {
        return resourceValue;
    }

    public void setResourceValue(List<ResourceEntity> resourceValue) {
        this.resourceValue = resourceValue;
    }


    public String getHouseholdId() {
        return householdId;
    }

    public void setHouseholdId(String householdId) {
        this.householdId = householdId;
    }

    public String getHouseholdName() {
        return householdName;
    }

    public void setHouseholdName(String householdName) {
        this.householdName = householdName;
    }
}
