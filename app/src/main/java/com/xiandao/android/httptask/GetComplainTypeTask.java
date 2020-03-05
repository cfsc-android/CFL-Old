package com.xiandao.android.httptask;

import android.content.Context;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.xiandao.android.entity.ComplainTypeEntity;
import com.xiandao.android.entity.OverallSituationEntity;
import com.xiandao.android.entity.SpinnerEntity;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * 此类描述的是:获取投诉类型task
 * @author TanYong
 * create at 2017/5/2 10:34
 */
public class GetComplainTypeTask extends BaseTask {
    @Override
    public Object parseJSON(Context context, String str) throws JSONException {
        super.parseJSON(context, str);
        Log.e("GetComplainTypeTask=", str);
        if (str != null) {
            OverallSituationEntity osEntity = getOsEntity();
            JSONObject jsonObject = new JSONObject(osEntity.getData().toString());
            Type type = new TypeToken<ArrayList<SpinnerEntity>>() {
            }.getType();
            ArrayList<SpinnerEntity> data = gson.fromJson(jsonObject.getString("addressEntityList"), type);
            ComplainTypeEntity complainTypeEntity = new ComplainTypeEntity();
            complainTypeEntity.setMsg(osEntity.getMsg());
            complainTypeEntity.setComplainTypeList(data);
            complainTypeEntity.setResultCode(osEntity.getResultCode());
            return complainTypeEntity;
        }else{
            return null;
        }
    }
}
