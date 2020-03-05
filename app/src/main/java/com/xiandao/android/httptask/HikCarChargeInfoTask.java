package com.xiandao.android.httptask;

import android.content.Context;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.xiandao.android.entity.CarChargeInfo;
import com.xiandao.android.entity.HikBaseEntity;
import com.xiandao.android.utils.Tools;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by zengx on 2019/6/14.
 * Describe:
 */
public class HikCarChargeInfoTask extends HikBaseTask {
    @Override
    public Object parseJSON(Context context, String str) throws JSONException {
        super.parseJSON(context, str);
        Log.i("海康服务-获取车辆包车信息",str);
        if (!Tools.isEmpty(str)) {
            HikBaseEntity hikBaseEntity=getHikBaseEntity();
            JSONObject jsonObject = new JSONObject(str);
            JSONObject data=jsonObject.getJSONObject("data");
            String list = data.getString("list");
            Type type = new TypeToken<List<CarChargeInfo>>() {}.getType();
            List<CarChargeInfo> carChargeInfos=gson.fromJson(list,type);
            hikBaseEntity.setData(carChargeInfos);
            return hikBaseEntity;
        } else {
            return null;
        }
    }
}