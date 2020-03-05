package com.xiandao.android.httptask;

import android.content.Context;
import android.util.Log;

import com.xiandao.android.entity.GoodsListResultEntity;
import com.xiandao.android.entity.ShopHomeResultEntity;
import com.xiandao.android.utils.Tools;

import org.json.JSONException;

/**
 * 此类描述的是:获取商品列表task
 *
 * @author TanYong
 *         create at 2017/5/2 10:45
 */
public class QueryGoodsListTask extends BaseTask {
    @Override
    public Object parseJSON(Context context, String str) throws JSONException {
        super.parseJSON(context, str);
        Log.e("QueryGoodsListTask=", str);
        if (!Tools.isEmpty(str)) {
            GoodsListResultEntity entity = gson.fromJson(str, GoodsListResultEntity.class);
            return entity;
        } else {
            return null;
        }
    }
}
