package com.xiandao.android.entity.smart;

import java.io.Serializable;

/**
 * Created by Loong on 2020/3/2.
 * Version: 1.0
 * Describe:
 */
public class DictionaryEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * value : WHITE
     * text : 白色
     * title : 白色
     */

    private String value;
    private String text;
    private String title;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
