package com.xiandao.android.entity;

import java.io.Serializable;

/**
 * Created by 谭勇 on 2018/3/8.
 */
public class GoodsClassificationEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private int homeid;
    private String name;
    private String pic;
    private String sort;
    private String homename;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHomeid() {
        return homeid;
    }

    public void setHomeid(int homeid) {
        this.homeid = homeid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getHomename() {
        return homename;
    }

    public void setHomename(String homename) {
        this.homename = homename;
    }
}
