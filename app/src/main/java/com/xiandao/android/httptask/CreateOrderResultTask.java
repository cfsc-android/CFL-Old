package com.xiandao.android.httptask;

import android.content.Context;
import android.util.Log;

import com.xiandao.android.entity.CreateOrderResultEntity;
import com.xiandao.android.utils.Tools;

import org.json.JSONException;

/**
 * 此类描述的是:创建订单task
 *
 * @author TanYong
 *         create at 2017/5/2 10:45
 */
public class CreateOrderResultTask extends BaseTask {
    @Override
    public Object parseJSON(Context context, String str) throws JSONException {
        super.parseJSON(context, str);
        Log.e("CreateOrderResultTask=", str);
        if (!Tools.isEmpty(str)) {
            CreateOrderResultEntity entity = gson.fromJson(str, CreateOrderResultEntity.class);
            return entity;
        } else {
            return null;
        }
    }
}
