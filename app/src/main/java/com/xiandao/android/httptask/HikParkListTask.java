package com.xiandao.android.httptask;

import android.content.Context;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.xiandao.android.entity.HikBaseEntity;
import com.xiandao.android.entity.HikParkInfo;
import com.xiandao.android.entity.HikParkingPayment;
import com.xiandao.android.entity.PersonalInfomation;
import com.xiandao.android.utils.Tools;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by zengx on 2019/6/14.
 * Describe:
 */
public class HikParkListTask extends HikBaseTask {
    @Override
    public Object parseJSON(Context context, String str) throws JSONException {
        super.parseJSON(context, str);
        Log.i("海康服务-获取车库列表",str);
        if (!Tools.isEmpty(str)) {
            HikBaseEntity hikBaseEntity=getHikBaseEntity();
            JSONObject jsonObject = new JSONObject(str);
            String data = jsonObject.getString("data");
            Type type = new TypeToken<List<HikParkInfo>>() {}.getType();
            List<HikParkInfo> list=gson.fromJson(data,type);
            hikBaseEntity.setData(list);
            return hikBaseEntity;
        } else {
            return null;
        }
    }
}