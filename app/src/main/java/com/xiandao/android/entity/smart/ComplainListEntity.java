package com.xiandao.android.entity.smart;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Loong on 2020/2/17.
 * Version: 1.0
 * Describe: 投诉列表实体
 */
public class ComplainListEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private int count;
    private List<ComplainEntity> data;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<ComplainEntity> getData() {
        return data;
    }

    public void setData(List<ComplainEntity> data) {
        this.data = data;
    }
}
