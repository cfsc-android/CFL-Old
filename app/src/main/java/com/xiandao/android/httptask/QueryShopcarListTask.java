package com.xiandao.android.httptask;

import android.content.Context;
import android.util.Log;

import com.xiandao.android.entity.ShopCar1ResultEntity;
import com.xiandao.android.utils.Tools;

import org.json.JSONException;

/**
 * 此类描述的是:获取购物车列表task
 *
 * @author TanYong
 *         create at 2017/5/2 10:45
 */
public class QueryShopcarListTask extends BaseTask {
    @Override
    public Object parseJSON(Context context, String str) throws JSONException {
        super.parseJSON(context, str);
        Log.e("QueryShopcarListTask=", str);
        if (!Tools.isEmpty(str)) {
            ShopCar1ResultEntity entity = gson.fromJson(str, ShopCar1ResultEntity.class);
            return entity;
        } else {
            return null;
        }
    }
}
