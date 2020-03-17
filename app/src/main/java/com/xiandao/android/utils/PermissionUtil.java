package com.xiandao.android.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Loong on 2020/3/16.
 * Version: 1.0
 * Describe:
 */
public class PermissionUtil {
    public static final int REQUEST_CODE = 0x100;
    //应用所需运行时权限
    private static final String[] PERMISSION={Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};


    //检查应用是否有权限
    public static String[] checkPermission(Activity context) {
        if (Build.VERSION.SDK_INT < 23) {//6.0才用动态权限
            return null;
        }

        List<String> unPermission = new ArrayList<>();
        //逐个判断你要的权限是否已经通过
        for (int i = 0; i < PERMISSION.length; i++) {
            if (ContextCompat.checkSelfPermission(context.getApplicationContext(), PERMISSION[i]) != PackageManager.PERMISSION_GRANTED) {
                unPermission.add(PERMISSION[i]);//添加还未授予的权限
            }
        }
        //申请权限
        if (unPermission.size() > 0) {//有权限没有通过，需要申请
            String[] strings = new String[unPermission.size()];
            return unPermission.toArray(strings);
        }
        return null;
    }
}
