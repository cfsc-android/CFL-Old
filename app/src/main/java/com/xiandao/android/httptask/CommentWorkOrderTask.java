package com.xiandao.android.httptask;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;

/**
 * 此类描述的是:评价工单task
 *
 * @author TanYong
 *         create at 2017/5/2 10:45
 */
public class CommentWorkOrderTask extends BaseTask {
    @Override
    public Object parseJSON(Context context, String str) throws JSONException {
        super.parseJSON(context, str);
        Log.e("CommentWorkOrderTask=", str);
        if (str != null)
            return getOsEntity();
        else
            return null;
    }
}
