package com.xiandao.android.httptask;

import android.content.Context;
import android.util.Log;

import com.xiandao.android.entity.LifePayEntity;
import com.xiandao.android.entity.RepairsDetailEntity;
import com.xiandao.android.utils.Tools;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 此类描述的是:获取生活缴费task
 *
 * @author TanYong
 *         create at 2017/5/2 10:45
 */
public class QueryLifePayTask extends BaseTask {
    @Override
    public Object parseJSON(Context context, String str) throws JSONException {
        super.parseJSON(context, str);
        Log.e("QueryLifePayTask=", str);
        if (!Tools.isEmpty(str)) {
            LifePayEntity entity = gson.fromJson(str, LifePayEntity.class);
            return entity;
        } else {
            return null;
        }
    }
}
