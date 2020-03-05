package com.xiandao.android.entity.smart;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Loong on 2020/2/6.
 * Version: 1.0
 * Describe: 紧急程度类型
 */
public enum EmergType {
    非常紧急("1"),
    紧急("2"),
    一般("3"),
    低("4"),
    可以忽略("5");
    private final String type;

    EmergType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static List<String> getEmergTypeList() {
        EmergType[] types = EmergType.values();
        List<String> list=new ArrayList<>();
        for (int i = 0; i < types.length; i++) {
            list.add(types[i].name());
        }
        return list;
    }
}