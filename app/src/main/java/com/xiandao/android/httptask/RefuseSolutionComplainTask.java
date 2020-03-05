package com.xiandao.android.httptask;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;

/**
 * 此类描述的是:拒绝解决方案task
 *
 * @author TanYong
 *         create at 2017/5/2 10:45
 */
public class RefuseSolutionComplainTask extends BaseTask {
    @Override
    public Object parseJSON(Context context, String str) throws JSONException {
        super.parseJSON(context, str);
        Log.e("RefuseSolutionComplainTask=", str);
        if (str != null)
            return getOsEntity();
        else
            return null;
    }
}
