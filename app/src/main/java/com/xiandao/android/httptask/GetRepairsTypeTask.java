package com.xiandao.android.httptask;

import android.content.Context;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.xiandao.android.entity.OverallSituationEntity;
import com.xiandao.android.entity.RepairsTypeEntity;
import com.xiandao.android.entity.SpinnerEntity;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * 此类描述的是:获取报修类型task
 *
 * @author TanYong
 *         create at 2017/4/21 16:14
 */
public class GetRepairsTypeTask extends BaseTask {
    @Override
    public Object parseJSON(Context context, String str) throws JSONException {
        super.parseJSON(context, str);
        Log.e("GetRepairsTypeTask=", str);
        OverallSituationEntity osEntity = gson.fromJson(str, OverallSituationEntity.class);
        JSONObject jsonObject = new JSONObject(osEntity.getData().toString());
        Type type = new TypeToken<ArrayList<SpinnerEntity>>() {
        }.getType();
        ArrayList<SpinnerEntity> data = gson.fromJson(jsonObject.getString("repairsTypeEntityList"), type);
        RepairsTypeEntity repairsTypeEntity = new RepairsTypeEntity();
        repairsTypeEntity.setMsg(osEntity.getMsg());
        repairsTypeEntity.setRepairsTypeList(data);
        repairsTypeEntity.setResultCode(osEntity.getResultCode());
        return repairsTypeEntity;
    }
}
