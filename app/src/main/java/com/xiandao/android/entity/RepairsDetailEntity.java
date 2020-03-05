package com.xiandao.android.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 此类描述的是:我的报修详情对象
 *
 * @author TanYong
 *         create at 2017/5/10 10:10
 *         <p>
 *         "handerresourcekey": "",
 *         "disposeDes": "",
 *         "resourcekey": "949d8bb3-18fc-4009-bd08-101a00cfebe4",
 *         "piclist": [
 *         {
 *         "url": "upload/job/2_20170522153338_1_yt.png"
 *         },
 *         {
 *         "url": "upload/job/2_20170522153338_2_yt.png"
 *         },
 *         {
 *         "url": "upload/job/2_20170522153338_3_yt.png"
 *         }
 *         ],
 *         "repairsType": "空调维修个j8",
 *         "materialCost": 0,
 *         "liveContentDesc": "",
 *         "UserInfo": {
 *         "userAddress": "上海啊",
 *         "userRoomAddress": "33-二单元-F8105",
 *         "gender": 1,
 *         "atProperty": "长沙西中心",
 *         "userName": "tyy",
 *         "userId": 2,
 *         "userMobileNo": "13811112222",
 *         "userHearImageUrl": "upload/ownerFace/1_face.png"
 *         },
 *         "repairsId": 62,
 *         "repairsDateTime": "2017-05-22 15:33:39",
 *         "ownerid": "2",
 *         "title": "1234",
 *         "disposePerson": "",
 *         "roomid": "4",
 *         "newpiclist": [],
 *         "repairsStatus": "未受理",
 *         "disposeTime": "",
 *         "manualCost": 0,
 *         "livedate": "",
 *         "handerpiclist": [],
 *         "totalCost": 0
 */
public class RepairsDetailEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private String handerresourcekey;
    private String disposeDes;
    private String resourcekey;
    private ArrayList<ImageUrlEntity> piclist;//业主报修的图片
    private ArrayList<ImageUrlEntity> newpiclist;//员工现场检视的图片
    private ArrayList<ImageUrlEntity> handerpiclist;//员工处理后的图片
    private String repairsType;
    private String materialCost;
    private String liveContentDesc;
    private RepairsUserInfoEntity UserInfo;
    private String repairsId;
    private String repairsDateTime;
    private String ownerid;
    private String title;
    private String disposePerson;
    private String roomid;
    private String repairsStatus;
    private String disposeTime;
    private String manualCost;
    private String livedate;
    private String totalCost;
    private String address;

    private String plandate;

    private String repairsDes;

    private String commentContent;
    private String commentLevel;

    private String resultCode;
    private String msg;

    public String getRepairsDes() {
        return repairsDes;
    }

    public void setRepairsDes(String repairsDes) {
        this.repairsDes = repairsDes;
    }

    public String getHanderresourcekey() {
        return handerresourcekey;
    }

    public void setHanderresourcekey(String handerresourcekey) {
        this.handerresourcekey = handerresourcekey;
    }

    public String getDisposeDes() {
        return disposeDes;
    }

    public void setDisposeDes(String disposeDes) {
        this.disposeDes = disposeDes;
    }

    public String getResourcekey() {
        return resourcekey;
    }

    public void setResourcekey(String resourcekey) {
        this.resourcekey = resourcekey;
    }

    public ArrayList<ImageUrlEntity> getPiclist() {
        return piclist;
    }

    public void setPiclist(ArrayList<ImageUrlEntity> piclist) {
        this.piclist = piclist;
    }

    public ArrayList<ImageUrlEntity> getNewpiclist() {
        return newpiclist;
    }

    public void setNewpiclist(ArrayList<ImageUrlEntity> newpiclist) {
        this.newpiclist = newpiclist;
    }

    public ArrayList<ImageUrlEntity> getHanderpiclist() {
        return handerpiclist;
    }

    public void setHanderpiclist(ArrayList<ImageUrlEntity> handerpiclist) {
        this.handerpiclist = handerpiclist;
    }

    public String getRepairsType() {
        return repairsType;
    }

    public void setRepairsType(String repairsType) {
        this.repairsType = repairsType;
    }

    public String getMaterialCost() {
        return materialCost;
    }

    public void setMaterialCost(String materialCost) {
        this.materialCost = materialCost;
    }

    public String getLiveContentDesc() {
        return liveContentDesc;
    }

    public void setLiveContentDesc(String liveContentDesc) {
        this.liveContentDesc = liveContentDesc;
    }

    public RepairsUserInfoEntity getUserInfo() {
        return UserInfo;
    }

    public void setUserInfo(RepairsUserInfoEntity userInfo) {
        UserInfo = userInfo;
    }

    public String getRepairsId() {
        return repairsId;
    }

    public void setRepairsId(String repairsId) {
        this.repairsId = repairsId;
    }

    public String getRepairsDateTime() {
        return repairsDateTime;
    }

    public void setRepairsDateTime(String repairsDateTime) {
        this.repairsDateTime = repairsDateTime;
    }

    public String getOwnerid() {
        return ownerid;
    }

    public void setOwnerid(String ownerid) {
        this.ownerid = ownerid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDisposePerson() {
        return disposePerson;
    }

    public void setDisposePerson(String disposePerson) {
        this.disposePerson = disposePerson;
    }

    public String getRoomid() {
        return roomid;
    }

    public void setRoomid(String roomid) {
        this.roomid = roomid;
    }

    public String getRepairsStatus() {
        return repairsStatus;
    }

    public void setRepairsStatus(String repairsStatus) {
        this.repairsStatus = repairsStatus;
    }

    public String getDisposeTime() {
        return disposeTime;
    }

    public void setDisposeTime(String disposeTime) {
        this.disposeTime = disposeTime;
    }

    public String getManualCost() {
        return manualCost;
    }

    public void setManualCost(String manualCost) {
        this.manualCost = manualCost;
    }

    public String getLivedate() {
        return livedate;
    }

    public void setLivedate(String livedate) {
        this.livedate = livedate;
    }

    public String getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(String totalCost) {
        this.totalCost = totalCost;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getPlandate() {
        return plandate;
    }

    public void setPlandate(String plandate) {
        this.plandate = plandate;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public String getCommentLevel() {
        return commentLevel;
    }

    public void setCommentLevel(String commentLevel) {
        this.commentLevel = commentLevel;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
