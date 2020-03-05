package com.xiandao.android.entity;

import java.io.Serializable;

/**
 * Created by 谭勇 on 2017/4/26.
 */
public class SpinnerEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
