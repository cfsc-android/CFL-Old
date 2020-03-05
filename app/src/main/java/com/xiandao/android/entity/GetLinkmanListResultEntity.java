package com.xiandao.android.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 此类描述的是:获取联系人返回数据
 *
 * @author TanYong
 *         create at 2017/5/18 10:33
 */
public class GetLinkmanListResultEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private ArrayList<LinkmanEntity> linkmanEntityList;
    private String resultCode;
    private String msg;

    public ArrayList<LinkmanEntity> getLinkmanEntityList() {
        return linkmanEntityList;
    }

    public void setLinkmanEntityList(ArrayList<LinkmanEntity> linkmanEntityList) {
        this.linkmanEntityList = linkmanEntityList;
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
