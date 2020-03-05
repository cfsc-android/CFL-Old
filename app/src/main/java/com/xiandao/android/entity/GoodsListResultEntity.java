package com.xiandao.android.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by 谭勇 on 2018/3/8.
 */
public class GoodsListResultEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private String resultCode;
    private String msg;
    private GoodsListData data;

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

    public GoodsListData getData() {
        return data;
    }

    public void setData(GoodsListData data) {
        this.data = data;
    }

    public class GoodsListData implements Serializable {
        private static final long serialVersionUID = 1L;
        private ArrayList<GoodsEntity> list;
        private Pagination pagination;

        public ArrayList<GoodsEntity> getList() {
            return list;
        }

        public void setList(ArrayList<GoodsEntity> list) {
            this.list = list;
        }

        public Pagination getPagination() {
            return pagination;
        }

        public void setPagination(Pagination pagination) {
            this.pagination = pagination;
        }
    }

}
