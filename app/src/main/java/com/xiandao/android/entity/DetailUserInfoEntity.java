package com.xiandao.android.entity;

import java.io.Serializable;

/**
 * 此类描述的是:详情返回用户数据对象
 *
 * @author TanYong
 *         create at 2017/4/20 23:10
 *         <p>
 *         "UserInfo": {
 *         "userAddress": "上海啊",
 *         "userRoomAddress": "12栋-三单元-C9001",
 *         "gender": 1,
 *         "atProperty": "长沙西中心",
 *         "userName": "tyy",
 *         "userId": 2,
 *         "userMobileNo": "13811112222",
 *         "userHearImageUrl": "upload/ownerFace/1_face.png"
 *         }
 */
public class DetailUserInfoEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private String userAddress;
    private String userRoomAddress;
    private String gender;
    private String atProperty;
    private String userName;
    private String userId;
    private String userMobileNo;
    private String userHearImageUrl;

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getUserRoomAddress() {
        return userRoomAddress;
    }

    public void setUserRoomAddress(String userRoomAddress) {
        this.userRoomAddress = userRoomAddress;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAtProperty() {
        return atProperty;
    }

    public void setAtProperty(String atProperty) {
        this.atProperty = atProperty;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserMobileNo() {
        return userMobileNo;
    }

    public void setUserMobileNo(String userMobileNo) {
        this.userMobileNo = userMobileNo;
    }

    public String getUserHearImageUrl() {
        return userHearImageUrl;
    }

    public void setUserHearImageUrl(String userHearImageUrl) {
        this.userHearImageUrl = userHearImageUrl;
    }
}
