package com.xiandao.android.httptask;

import android.content.Context;
import android.util.Log;

import com.xiandao.android.entity.HikAlarmCarlist;
import com.xiandao.android.entity.HikBaseEntity;
import com.xiandao.android.entity.HikParkingPayment;
import com.xiandao.android.utils.Tools;

import org.json.JSONException;
import org.json.JSONObject;

public class HikParkingPaymentTask extends HikBaseTask {
    @Override
    public Object parseJSON(Context context, String str) throws JSONException {
        super.parseJSON(context, str);
        Log.i("海康服务-获取停车账单",str);
        if (!Tools.isEmpty(str)) {
            HikBaseEntity hikBaseEntity=getHikBaseEntity();
            JSONObject jsonObject = new JSONObject(str);
            String data = jsonObject.getString("data");
            HikParkingPayment hikParkingPayment=gson.fromJson(data,HikParkingPayment.class);
            hikBaseEntity.setData(hikParkingPayment);
            return hikBaseEntity;
        } else {
            return null;
        }
    }
}
