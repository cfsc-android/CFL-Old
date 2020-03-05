package com.xiandao.android.httptask;

import android.content.Context;
import android.util.Log;

import com.xiandao.android.entity.OverallSituationEntity;

import org.json.JSONException;

/**
 * 此类描述的是:退出登录task
 *
 * @author TanYong
 *         create at 2017/4/21 16:14
 */
public class LogoutTask extends BaseTask {
    @Override
    public Object parseJSON(Context context, String str) throws JSONException {
        super.parseJSON(context, str);
        Log.e("LogoutTask=", str);
        OverallSituationEntity osEntity = getOsEntity();
        return osEntity;
    }
}
