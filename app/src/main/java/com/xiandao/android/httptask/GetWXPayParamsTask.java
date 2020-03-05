package com.xiandao.android.httptask;

import android.content.Context;
import android.util.Log;

import com.xiandao.android.entity.GetWXPapParamsResultEntity;
import com.xiandao.android.utils.Tools;

import org.json.JSONException;

/**
 * 此类描述的是:获取微信支付参数task
 *
 * @author TanYong
 *         create at 2017/5/2 10:45
 */
public class GetWXPayParamsTask extends BaseTask {
    @Override
    public Object parseJSON(Context context, String str) throws JSONException {
        super.parseJSON(context, str);
        Log.e("GetWXPayParamsTask=", str);
        if (!Tools.isEmpty(str)) {
            GetWXPapParamsResultEntity entity = gson.fromJson(str, GetWXPapParamsResultEntity.class);
            return entity;
        } else {
            return null;
        }
    }
}
