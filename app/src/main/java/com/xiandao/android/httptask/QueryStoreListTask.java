package com.xiandao.android.httptask;

import android.content.Context;
import android.util.Log;

import com.xiandao.android.entity.StoreListResultEntity;
import com.xiandao.android.utils.Tools;

import org.json.JSONException;

/**
 * 此类描述的是:获取购物首页task--商品分类
 *
 * @author TanYong
 *         create at 2017/5/2 10:45
 */
public class QueryStoreListTask extends BaseTask {
    @Override
    public Object parseJSON(Context context, String str) throws JSONException {
        super.parseJSON(context, str);
        Log.e("QueryStoreListTask=", str);
        if (!Tools.isEmpty(str)) {
            StoreListResultEntity entity = gson.fromJson(str, StoreListResultEntity.class);
            return entity;
        } else {
            return null;
        }
    }
}
