package com.xiandao.android.entity.smart;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Loong on 2020/3/19.
 * Version: 1.0
 * Describe:
 */
public class ProjectSelectEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private String label;
    private List<KeyTitleEntity> data;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<KeyTitleEntity> getData() {
        return data;
    }

    public void setData(List<KeyTitleEntity> data) {
        this.data = data;
    }
}
