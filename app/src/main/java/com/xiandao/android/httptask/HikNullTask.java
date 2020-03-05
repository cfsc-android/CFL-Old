package com.xiandao.android.httptask;

import android.content.Context;
import android.util.Log;

import com.xiandao.android.utils.Tools;

import org.json.JSONException;

public class HikNullTask extends HikBaseTask {
    @Override
    public Object parseJSON(Context context, String str) throws JSONException {
        super.parseJSON(context, str);
        Log.i("海康服务-NULL",str);
        if (!Tools.isEmpty(str)) {
            return getHikBaseEntity();
        } else {
            return null;
        }
    }
}
