package com.xiandao.android.httptask;

import android.content.Context;
import android.util.Log;

import com.xiandao.android.entity.MyOrderResultEntity;
import com.xiandao.android.utils.Tools;

import org.json.JSONException;

/**
 * 此类描述的是:获取我的订单
 *
 * @author TanYong
 *         create at 2017/5/2 10:45
 */
public class QueryMyOrderTask extends BaseTask {
    @Override
    public Object parseJSON(Context context, String str) throws JSONException {
        super.parseJSON(context, str);
        Log.e("QueryMyOrderTask=", str);
        if (!Tools.isEmpty(str)) {
            MyOrderResultEntity entity = gson.fromJson(str, MyOrderResultEntity.class);
            return entity;
        } else {
            return null;
        }
    }
}
