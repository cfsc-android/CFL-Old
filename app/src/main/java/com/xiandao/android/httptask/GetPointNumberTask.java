package com.xiandao.android.httptask;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;

/**
 * 此类描述的是:公告详情获取点赞数
 * @author TanYong
 * create at 2017/7/3 17:14
 */
public class GetPointNumberTask extends BaseTask {
    @Override
    public Object parseJSON(Context context, String str) throws JSONException {
        super.parseJSON(context, str);
        Log.e("GetPointNumberTask=", str);
        if (str != null)
            return getOsEntity();
        else
            return null;
    }
}
