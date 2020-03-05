package com.xiandao.android.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 此类描述的是:通知公告列表对象
 */
public class NoticeListEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private ArrayList<NoticeEntity> noticeList;
    private Pagination pagination;

    private String resultCode;
    private String msg;

    public ArrayList<NoticeEntity> getNoticeList() {
        return noticeList;
    }

    public void setNoticeList(ArrayList<NoticeEntity> noticeList) {
        this.noticeList = noticeList;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
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
