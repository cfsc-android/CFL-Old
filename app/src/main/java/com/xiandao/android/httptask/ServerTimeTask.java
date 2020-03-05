package com.xiandao.android.httptask;

import android.content.Context;
import android.util.Log;

import com.xiandao.android.utils.Tools;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 此类描述的是:获取服务器时间task
 * @author TanYong
 * create at 2017/4/21 14:13
 */
public class ServerTimeTask extends BaseTask {

    @Override
    public Object parseJSON(Context context, String str) throws JSONException {
        super.parseJSON(context, str);
        Log.e("ServerTimeTask",str);
        if (Tools.isEmpty(str)) {
            return null;
        }
        String time = new JSONObject(str).getString("data");
        return time;
    }
}
