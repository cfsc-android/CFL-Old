package com.xiandao.android.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 帖子实体类
 */
public class ThemeEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * id : 5
     * ownerid : 4515
     * ownerName :
     * resourceskey : 92d93960-4e7c-424a-b8a7-1eeccf25eb90
     * time : 2019-01-28 17:54:21
     * homePicUrl :
     * content : 测试测试
     * upNumber : 2
     * commentList : []
     * picList : [{"url":"upload/theme/2ab9c5db-7c56-4bbd-9052-beb27b696170.png"}]
     */

    private int id;
    private int ownerid;
    private String ownerName;
    private String resourceskey;
    private String time;
    private String homePicUrl;
    private String content;
    private String upNumber;
    private List<?> commentList;
    private List<PicListBean> picList;
    private int ifUp;
    private int ifFollow;
    private String ownerFace;

    public String getOwnerFace() {
        return ownerFace;
    }

    public void setOwnerFace(String ownerFace) {
        this.ownerFace = ownerFace;
    }

    public int getIfUp() {
        return ifUp;
    }

    public void setIfUp(int ifUp) {
        this.ifUp = ifUp;
    }

    public int getIfFollow() {
        return ifFollow;
    }

    public void setIfFollow(int ifFollow) {
        this.ifFollow = ifFollow;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOwnerid() {
        return ownerid;
    }

    public void setOwnerid(int ownerid) {
        this.ownerid = ownerid;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getResourceskey() {
        return resourceskey;
    }

    public void setResourceskey(String resourceskey) {
        this.resourceskey = resourceskey;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getHomePicUrl() {
        return homePicUrl;
    }

    public void setHomePicUrl(String homePicUrl) {
        this.homePicUrl = homePicUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUpNumber() {
        return upNumber;
    }

    public void setUpNumber(String upNumber) {
        this.upNumber = upNumber;
    }

    public List<?> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<?> commentList) {
        this.commentList = commentList;
    }

    public List<PicListBean> getPicList() {
        return picList;
    }

    public void setPicList(List<PicListBean> picList) {
        this.picList = picList;
    }

    public class PicListBean implements Serializable{
        /**
         * url : upload/theme/2ab9c5db-7c56-4bbd-9052-beb27b696170.png
         */

        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
