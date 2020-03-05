package com.xiandao.android.httptask;

import android.content.Context;
import android.util.Log;

import com.xiandao.android.entity.ShopCommentResultEntity;
import com.xiandao.android.entity.ShopHomeResultEntity;
import com.xiandao.android.utils.Tools;

import org.json.JSONException;

/**
 * 此类描述的是:获取商品评价列表task
 *
 * @author TanYong
 *         create at 2017/5/2 10:45
 */
public class QueryShopCommentListTask extends BaseTask {
    @Override
    public Object parseJSON(Context context, String str) throws JSONException {
        super.parseJSON(context, str);
        Log.e("QueryShopCommentListTask=", str);
        if (!Tools.isEmpty(str)) {
            ShopCommentResultEntity entity = gson.fromJson(str, ShopCommentResultEntity.class);
            return entity;
        } else {
            return null;
        }
    }
}
