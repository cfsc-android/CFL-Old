package com.xiandao.android.httptask;

import android.content.Context;
import android.util.Log;

import com.xiandao.android.entity.HikBaseEntity;
import com.xiandao.android.entity.HikCardList;
import com.xiandao.android.utils.Tools;

import org.json.JSONException;
import org.json.JSONObject;

public class HikCardListTask extends HikBaseTask {
    @Override
    public Object parseJSON(Context context, String str) throws JSONException {
        super.parseJSON(context, str);
        Log.i("海康服务-获取用户卡片信息",str);
        if (!Tools.isEmpty(str)) {
            HikBaseEntity hikBaseEntity=getHikBaseEntity();
            JSONObject jsonObject = new JSONObject(str);
            String data = jsonObject.getString("data");
            HikCardList hikCardList=gson.fromJson(data,HikCardList.class);
            hikBaseEntity.setData(hikCardList);
            return hikBaseEntity;
        } else {
            return null;
        }
    }
}
