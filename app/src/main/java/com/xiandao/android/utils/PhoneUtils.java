package com.xiandao.android.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * 此类描述的是:打电话工具类
 *
 * @author TanYong
 *         create at 2017/5/10 15:06
 */
public class PhoneUtils {
//    /**
//     * @author TanYong
//     * create at 2017/5/10 15:06
//     * TODO：拨打电话（跳转到打电话的界面自动拨号）
//     */
//    public static void autoCallPhone(Context context, String phone) {
//        Intent intent = new Intent(Intent.ACTION_CALL,
//                Uri.parse("tel:" + phone));
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(intent);
//    }

    /**
     * @author TanYong
     * create at 2017/5/10 15:06
     * TODO：跳转到打电话的界面但是不自动拨号
     */
    public static void callPhone(Context context, String phone) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_DIAL); // android.intent.action.DIAL
        intent.setData(Uri.parse("tel:" + phone));
        context.startActivity(intent);
    }
}
