package com.xiandao.android.entity.smart;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Loong on 2020/2/14.
 * Version: 1.0
 * Describe:
 */
public class SmsKeyEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
