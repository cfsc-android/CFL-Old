package com.xiandao.android.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zengx on 2019/8/7.
 * Describe:
 */
public class NoticeDetailEntity implements Serializable {
    private static final long serialVersionUID = 1L;


    /**
     * noticeId : 23
     * title : 中国房地产百强企业-长房集团
     * content :
     * publishTime :
     * publisherName :
     * publisher : 1
     * upNO : 2
     * readedNO : 0
     * detailUrl : /upload\notice_html\html\20190531\171516.html
     * coverImgUrl :
     * remark : 中国房地产百强企业-长房集团
     * receive : 1
     * noticeImgUrl : upload/notice_html/image/20190531/20190531171450_257.png
     * praises : 0
     * reads : 60
     * projectid : 1
     * resources :
     * resourcesToadd :
     * resourceList : null
     * noticeType : 1
     */

    private int noticeId;
    private String title;
    private String content;
    private String publishTime;
    private String publisherName;
    private int publisher;
    private int upNO;
    private int readedNO;
    private String detailUrl;
    private String coverImgUrl;
    private String remark;
    private int receive;
    private String noticeImgUrl;
    private int praises;
    private int reads;
    private String projectid;
    private String resources;
    private String resourcesToadd;
    private List<NoticeEntity.ResourceEntity> resourceList;
    private int noticeType;

    public int getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(int noticeId) {
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

    public int getPublisher() {
        return publisher;
    }

    public void setPublisher(int publisher) {
        this.publisher = publisher;
    }

    public int getUpNO() {
        return upNO;
    }

    public void setUpNO(int upNO) {
        this.upNO = upNO;
    }

    public int getReadedNO() {
        return readedNO;
    }

    public void setReadedNO(int readedNO) {
        this.readedNO = readedNO;
    }

    public String getDetailUrl() {
        return detailUrl;
    }

    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }

    public String getCoverImgUrl() {
        return coverImgUrl;
    }

    public void setCoverImgUrl(String coverImgUrl) {
        this.coverImgUrl = coverImgUrl;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getReceive() {
        return receive;
    }

    public void setReceive(int receive) {
        this.receive = receive;
    }

    public String getNoticeImgUrl() {
        return noticeImgUrl;
    }

    public void setNoticeImgUrl(String noticeImgUrl) {
        this.noticeImgUrl = noticeImgUrl;
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

    public String getProjectid() {
        return projectid;
    }

    public void setProjectid(String projectid) {
        this.projectid = projectid;
    }

    public String getResources() {
        return resources;
    }

    public void setResources(String resources) {
        this.resources = resources;
    }

    public String getResourcesToadd() {
        return resourcesToadd;
    }

    public void setResourcesToadd(String resourcesToadd) {
        this.resourcesToadd = resourcesToadd;
    }

    public List<NoticeEntity.ResourceEntity> getResourceList() {
        return resourceList;
    }

    public void setResourceList(List<NoticeEntity.ResourceEntity> resourceList) {
        this.resourceList = resourceList;
    }

    public int getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(int noticeType) {
        this.noticeType = noticeType;
    }
}
