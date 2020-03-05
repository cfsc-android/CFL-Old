package com.xiandao.android.entity;

import java.io.Serializable;

/**
 * 此类描述的是:联系人对象
 *
 * @author TanYong
 *         create at 2017/5/18 10:33
 */
public class LinkmanEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 联系人ID
     */
    private int linkmanId;
    /**
     * 联系人号码
     */
    private String linkmanMobileNo;
    /**
     * 与用户关系
     */
    private String relationship;
    /**
     * 姓名
     */
    private String linkmanName;

    /**
     * 判断是否选中:1、选中，2、没选中
     */
    private int checkedFlag;

    public int getLinkmanId() {
        return linkmanId;
    }

    public void setLinkmanId(int linkmanId) {
        this.linkmanId = linkmanId;
    }

    public String getLinkmanMobileNo() {
        return linkmanMobileNo;
    }

    public void setLinkmanMobileNo(String linkmanMobileNo) {
        this.linkmanMobileNo = linkmanMobileNo;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getLinkmanName() {
        return linkmanName;
    }

    public void setLinkmanName(String linkmanName) {
        this.linkmanName = linkmanName;
    }

    public int getCheckedFlag() {
        return checkedFlag;
    }

    public void setCheckedFlag(int checkedFlag) {
        this.checkedFlag = checkedFlag;
    }
}
