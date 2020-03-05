package com.xiandao.android.entity;

import java.io.Serializable;

/**
 * 此类描述的是:报修用户信息对象
 *
 * @author TanYong
 *         create at 2017/5/10 10:11
 */
public class RepairsUserInfoEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private int userId;//用户ID
    private String userName;//用户名称
    private String userMobileNo;//用户手机号码
    private String userAddress;//用户住址
    private String userHearImageUrl;//用户头像网址
    private String atProperty;//所属物业
    private String userRoomAddress;//33-二单元-F8105
    private String gender;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserMobileNo() {
        return userMobileNo;
    }

    public void setUserMobileNo(String userMobileNo) {
        this.userMobileNo = userMobileNo;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getUserHearImageUrl() {
        return userHearImageUrl;
    }

    public void setUserHearImageUrl(String userHearImageUrl) {
        this.userHearImageUrl = userHearImageUrl;
    }

    public String getAtProperty() {
        return atProperty;
    }

    public void setAtProperty(String atProperty) {
        this.atProperty = atProperty;
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
}
