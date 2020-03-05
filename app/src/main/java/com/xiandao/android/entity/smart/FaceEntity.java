package com.xiandao.android.entity.smart;

import java.io.Serializable;

/**
 * Created by Loong on 2020/2/29.
 * Version: 1.0
 * Describe:
 */
public class FaceEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private String userId;
    private String name;
    private String nickname;
    private String faceUrl;
    private String updateTime;

    public FaceEntity(String userId, String name, String nickname, String faceUrl, String updateTime) {
        this.userId = userId;
        this.name = name;
        this.nickname = nickname;
        this.faceUrl = faceUrl;
        this.updateTime = updateTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getFaceUrl() {
        return faceUrl;
    }

    public void setFaceUrl(String faceUrl) {
        this.faceUrl = faceUrl;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
