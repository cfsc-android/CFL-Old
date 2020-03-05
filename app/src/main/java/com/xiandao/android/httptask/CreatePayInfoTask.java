package com.xiandao.android.httptask;

import android.content.Context;
import android.util.Log;

import com.xiandao.android.entity.CreatePayInfoResultEntity;
import com.xiandao.android.entity.LifePayEntity;
import com.xiandao.android.utils.Tools;

import org.json.JSONException;

/**
 * 此类描述的是:创建支付订单task
 *
 * @author TanYong
 *         create at 2017/5/2 10:45
 */
public class CreatePayInfoTask extends BaseTask {
    @Override
    public Object parseJSON(Context context, String str) throws JSONException {
        super.parseJSON(context, str);
        Log.e("CreatePayInfoTask=", str);
        if (!Tools.isEmpty(str)) {
            CreatePayInfoResultEntity entity = gson.fromJson(str, CreatePayInfoResultEntity.class);
            return entity;
        } else {
            return null;
        }
    }
}
