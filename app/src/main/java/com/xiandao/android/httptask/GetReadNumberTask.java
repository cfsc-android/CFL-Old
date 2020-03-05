package com.xiandao.android.httptask;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;

/**
 * 此类描述的是:公告详情获取阅读数
 * @author TanYong
 * create at 2017/7/3 17:14
 */
public class GetReadNumberTask extends BaseTask {
    @Override
    public Object parseJSON(Context context, String str) throws JSONException {
        super.parseJSON(context, str);
        Log.e("GetReadNumberTask=", str);
        if (str != null)
            return getOsEntity();
        else
            return null;
    }
}
