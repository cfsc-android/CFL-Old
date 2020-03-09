package com.xiandao.android.entity.smart;

import java.io.Serializable;

/**
 * Created by Loong on 2020/3/6.
 * Version: 1.0
 * Describe:
 */
public class OperationInfoEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id : D2B4B7804D1FFF12348F76639A4B7BFE
     * desc : 客服接受价格
     */

    private String id;
    private String desc;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
