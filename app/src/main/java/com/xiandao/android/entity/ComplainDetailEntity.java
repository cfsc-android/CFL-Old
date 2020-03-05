package com.xiandao.android.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 此类描述的是:我的投诉详情对象
 *
 * @author TanYong
 *         create at 2017/6/2 13:42
 *         <p>
 *         {
 *         "msg": "查询投诉详细成功",
 *         "data": {
 *         "resourcekey": "949a6a20-8260-4b4e-8872-d63d5666ae9a",
 *         "complainDisposeImageUrlList": [],
 *         "measureContent": "",//整改措施内容
 *         "empHandler": "员工1",//处理员工
 *         "complainDateTime": "2017-06-06 19:01:35",//投诉时间
 *         "checkContent": "checkContent123456",//现场查询情况
 *         "empSolutionDate": "",//员工完成投诉时间
 *         "UserInfo": { //用户信息
 *         "userAddress": "1111",
 *         "gender": 1,
 *         "atProperty": "长沙西中心",
 *         "userName": "孙明",
 *         "userId": 7,
 *         "userMobileNo": "18700001111",
 *         "userHearImageUrl": "1"
 *         },
 *         "managercheckpic": "",//主管现场照片
 *         "ownerId": 7,//投诉业主
 *         "empSolutionContent": "",//员工完成投诉结果
 *         "solveType": "1",//投诉用户判定是否解决
 *         "roomId": 1,//业主投诉房屋
 *         "deparmenthandertime": "2017-06-06 19:26:06",//部门处理时间
 *         "finishType": "1",//投诉完结类型 1 正常完结 2 无上级处理完结
 *         "solutionDate": "2017-06-06 19:37:33",//主管解决方案时间
 *         "complainStatus": "投诉处理已评价",//投诉状态
 *         "solutionContent": "解决方案内容2017-06-06solution",//主管解决方案内容
 *         "csAcceptTime": "2017-06-06 19:14:04",//客服接单时间
 *         "complainId": 40,//投诉单据ID
 *         "commentContent": "评价内容",//投诉业主评价内容
 *         "isfinish": "1",//是否投诉完结 0未 1是
 *         "deparmentLeader": "职能用户张三",//主管处理人
 *         "empSolutionPic": [{//员工完成投诉结果上传图片
 *         "url": "upload/empSolutionComplains/6_20170606194110_1_yt.png"
 *         }
 *         ],
 *         "complainType": "设备",//投诉类型
 *         "csAcceptor": "用户（指挥中心）",//客服处理人
 *         "complainDes": "投诉内容123",//业主填报投诉内容
 *         "complainImageUrlList": [{//业主填报投诉图片
 *         "url": "upload/complains/7_20170606190134_1_yt.png"
 *         }
 *         ]
 *         },
 *         "resultCode": "0"
 *         }
 */
public class ComplainDetailEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private String resourcekey;
    private ArrayList<ImageUrlEntity> complainDisposeImageUrlList;
    private String measureContent;
    private String empHandler;
    private String complainDateTime;
    private String checkContent;
    private String empSolutionDate;
    private DetailUserInfoEntity UserInfo;
    private ArrayList<ImageUrlEntity> managercheckpic;
    private String ownerId;
    private String empSolutionContent;
    private String solveType;
    private String roomId;
    private String deparmenthandertime;
    private String finishType;
    private String solutionDate;
    private String complainStatus;
    private String solutionContent;
    private String csAcceptTime;
    private String complainId;
    private String commentContent;
    private String isfinish;
    private String deparmentLeader;
    private ArrayList<ImageUrlEntity> empSolutionPic;
    private String complainType;
    private String csAcceptor;
    private String complainDes;//投诉内容
    private ArrayList<ImageUrlEntity> complainImageUrlList;//投诉填报图片

    private String commentLevel;//评价星级
    private String measureDate;//整改时间

    private String resultCode;
    private String msg;

    public String getResourcekey() {
        return resourcekey;
    }

    public void setResourcekey(String resourcekey) {
        this.resourcekey = resourcekey;
    }

    public ArrayList<ImageUrlEntity> getComplainDisposeImageUrlList() {
        return complainDisposeImageUrlList;
    }

    public void setComplainDisposeImageUrlList(ArrayList<ImageUrlEntity> complainDisposeImageUrlList) {
        this.complainDisposeImageUrlList = complainDisposeImageUrlList;
    }

    public String getMeasureContent() {
        return measureContent;
    }

    public void setMeasureContent(String measureContent) {
        this.measureContent = measureContent;
    }

    public String getEmpHandler() {
        return empHandler;
    }

    public void setEmpHandler(String empHandler) {
        this.empHandler = empHandler;
    }

    public String getComplainDateTime() {
        return complainDateTime;
    }

    public void setComplainDateTime(String complainDateTime) {
        this.complainDateTime = complainDateTime;
    }

    public String getCheckContent() {
        return checkContent;
    }

    public void setCheckContent(String checkContent) {
        this.checkContent = checkContent;
    }

    public String getEmpSolutionDate() {
        return empSolutionDate;
    }

    public void setEmpSolutionDate(String empSolutionDate) {
        this.empSolutionDate = empSolutionDate;
    }

    public DetailUserInfoEntity getUserInfo() {
        return UserInfo;
    }

    public void setUserInfo(DetailUserInfoEntity userInfo) {
        UserInfo = userInfo;
    }

    public ArrayList<ImageUrlEntity> getManagercheckpic() {
        return managercheckpic;
    }

    public void setManagercheckpic(ArrayList<ImageUrlEntity> managercheckpic) {
        this.managercheckpic = managercheckpic;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getEmpSolutionContent() {
        return empSolutionContent;
    }

    public void setEmpSolutionContent(String empSolutionContent) {
        this.empSolutionContent = empSolutionContent;
    }

    public String getSolveType() {
        return solveType;
    }

    public void setSolveType(String solveType) {
        this.solveType = solveType;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getDeparmenthandertime() {
        return deparmenthandertime;
    }

    public void setDeparmenthandertime(String deparmenthandertime) {
        this.deparmenthandertime = deparmenthandertime;
    }

    public String getFinishType() {
        return finishType;
    }

    public void setFinishType(String finishType) {
        this.finishType = finishType;
    }

    public String getSolutionDate() {
        return solutionDate;
    }

    public void setSolutionDate(String solutionDate) {
        this.solutionDate = solutionDate;
    }

    public String getComplainStatus() {
        return complainStatus;
    }

    public void setComplainStatus(String complainStatus) {
        this.complainStatus = complainStatus;
    }

    public String getSolutionContent() {
        return solutionContent;
    }

    public void setSolutionContent(String solutionContent) {
        this.solutionContent = solutionContent;
    }

    public String getCsAcceptTime() {
        return csAcceptTime;
    }

    public void setCsAcceptTime(String csAcceptTime) {
        this.csAcceptTime = csAcceptTime;
    }

    public String getComplainId() {
        return complainId;
    }

    public void setComplainId(String complainId) {
        this.complainId = complainId;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public String getIsfinish() {
        return isfinish;
    }

    public void setIsfinish(String isfinish) {
        this.isfinish = isfinish;
    }

    public String getDeparmentLeader() {
        return deparmentLeader;
    }

    public void setDeparmentLeader(String deparmentLeader) {
        this.deparmentLeader = deparmentLeader;
    }

    public ArrayList<ImageUrlEntity> getEmpSolutionPic() {
        return empSolutionPic;
    }

    public void setEmpSolutionPic(ArrayList<ImageUrlEntity> empSolutionPic) {
        this.empSolutionPic = empSolutionPic;
    }

    public String getComplainType() {
        return complainType;
    }

    public void setComplainType(String complainType) {
        this.complainType = complainType;
    }

    public String getCsAcceptor() {
        return csAcceptor;
    }

    public void setCsAcceptor(String csAcceptor) {
        this.csAcceptor = csAcceptor;
    }

    public String getComplainDes() {
        return complainDes;
    }

    public void setComplainDes(String complainDes) {
        this.complainDes = complainDes;
    }

    public ArrayList<ImageUrlEntity> getComplainImageUrlList() {
        return complainImageUrlList;
    }

    public void setComplainImageUrlList(ArrayList<ImageUrlEntity> complainImageUrlList) {
        this.complainImageUrlList = complainImageUrlList;
    }

    public String getCommentLevel() {
        return commentLevel;
    }

    public void setCommentLevel(String commentLevel) {
        this.commentLevel = commentLevel;
    }

    public String getMeasureDate() {
        return measureDate;
    }

    public void setMeasureDate(String measureDate) {
        this.measureDate = measureDate;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
