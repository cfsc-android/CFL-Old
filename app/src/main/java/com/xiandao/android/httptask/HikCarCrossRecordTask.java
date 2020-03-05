package com.xiandao.android.httptask;

import android.content.Context;
import android.util.Log;

import com.xiandao.android.entity.HikAlarmCarlist;
import com.xiandao.android.entity.HikBaseEntity;
import com.xiandao.android.entity.HikCarCrossRecordList;
import com.xiandao.android.utils.Tools;

import org.json.JSONException;
import org.json.JSONObject;

public class HikCarCrossRecordTask extends HikBaseTask {
    @Override
    public Object parseJSON(Context context, String str) throws JSONException {
        super.parseJSON(context, str);
        Log.i("海康服务-车辆出入信息",str);
        if (!Tools.isEmpty(str)) {
            HikBaseEntity hikBaseEntity=getHikBaseEntity();
            JSONObject jsonObject = new JSONObject(str);
            String data = jsonObject.getString("data");
            HikCarCrossRecordList hikCarCrossRecordList=gson.fromJson(data,HikCarCrossRecordList.class);
            hikBaseEntity.setData(hikCarCrossRecordList);
            return hikBaseEntity;
        } else {
            return null;
        }
    }
}
