package com.xiandao.android.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 此类描述的是:通知公告对象
 *
 * @author TanYong
 *         create at 2017/6/14 15:42
 */
public class NoticeEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private String noticeId;
    private String title;
    private String content;
    private String publishTime;
    private String publisherName;
    private String publisher;
    private String upNO;
    private String readedNO;
    private String detailUrl;

    private String noticeImgUrl;
    private String coverImgUrl;
    private String remark;
    private String resources;

    private int praises;//点赞数
    private int reads;//阅读数

    private int noticeType;
    private List<ResourceEntity> resourceList;



    private String resultCode;
    private String msg;

    public String getCoverImgUrl() {
        return coverImgUrl;
    }

    public void setCoverImgUrl(String coverImgUrl) {
        this.coverImgUrl = coverImgUrl;
    }

    public List<ResourceEntity> getResourceList() {
        return resourceList;
    }

    public void setResourceList(List<ResourceEntity> resourceList) {
        this.resourceList = resourceList;
    }

    public int getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(int noticeType) {
        this.noticeType = noticeType;
    }

    public int getPraises() {
        return praises;
    }

    public void setPraises(int praises) {
        this.praises = praises;
    }

    public int getReads() {
        return reads;
    }

    public void setReads(int reads) {
        this.reads = reads;
    }

    public String getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(String noticeId) {
        this.noticeId = noticeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getUpNO() {
        return upNO;
    }

    public void setUpNO(String upNO) {
        this.upNO = upNO;
    }

    public String getReadedNO() {
        return readedNO;
    }

    public void setReadedNO(String readedNO) {
        this.readedNO = readedNO;
    }

    public String getDetailUrl() {
        return detailUrl;
    }

    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }

    public String getNoticeImgUrl() {
        return noticeImgUrl;
    }

    public void setNoticeImgUrl(String noticeImgUrl) {
        this.noticeImgUrl = noticeImgUrl;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public String getResources() {
        return resources;
    }

    public void setResources(String resources) {
        this.resources = resources;
    }

    public class ResourceEntity implements Serializable{
        private static final long serialVersionUID = 1L;
        private String resourceRealName;
        private String uploadTime;
        private String url;

        public String getResourceRealName() {
            return resourceRealName;
        }

        public void setResourceRealName(String resourceRealName) {
            this.resourceRealName = resourceRealName;
        }

        public String getUploadTime() {
            return uploadTime;
        }

        public void setUploadTime(String uploadTime) {
            this.uploadTime = uploadTime;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
