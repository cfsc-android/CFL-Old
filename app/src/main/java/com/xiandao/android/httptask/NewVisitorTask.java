package com.xiandao.android.httptask;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;

/**
 * Created by zengx on 2019/4/25.
 * Describe:
 */
public class NewVisitorTask extends BaseTask {
    @Override
    public Object parseJSON(Context context, String str) throws JSONException {
        super.parseJSON(context, str);
        Log.e("NewVisitorTask=", str);
        if (str != null) {
            return getOsEntity();
        } else {
            return null;
        }
    }
}
