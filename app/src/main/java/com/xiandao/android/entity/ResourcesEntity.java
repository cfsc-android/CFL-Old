package com.xiandao.android.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 此类描述的是:商品详情
 *
 * @author TanYong
 *         create at 2017/6/14 15:42
 */
public class ResourcesEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer resourceid;
    private String url;
    private String oldurl;
    private Date uploadtime;
    private String resourcekey;

    public Integer getResourceid() {
        return resourceid;
    }

    public void setResourceid(Integer resourceid) {
        this.resourceid = resourceid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getOldurl() {
        return oldurl;
    }

    public void setOldurl(String oldurl) {
        this.oldurl = oldurl;
    }

    public Date getUploadtime() {
        return uploadtime;
    }

    public void setUploadtime(Date uploadtime) {
        this.uploadtime = uploadtime;
    }

    public String getResourcekey() {
        return resourcekey;
    }

    public void setResourcekey(String resourcekey) {
        this.resourcekey = resourcekey;
    }
}
