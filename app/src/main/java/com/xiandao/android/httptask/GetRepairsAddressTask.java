package com.xiandao.android.httptask;

import android.content.Context;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.xiandao.android.entity.OverallSituationEntity;
import com.xiandao.android.entity.RepairsAddressEntity;
import com.xiandao.android.entity.SpinnerEntity;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * 此类描述的是:获取报修地址task
 *
 * @author TanYong
 *         create at 2017/4/21 16:14
 */
public class GetRepairsAddressTask extends BaseTask {
    @Override
    public Object parseJSON(Context context, String str) throws JSONException {
        super.parseJSON(context, str);
        Log.e("GetRepairsAddressTask",str);
        if (str != null) {
            OverallSituationEntity osEntity = getOsEntity();
            JSONObject jsonObject = new JSONObject(osEntity.getData().toString());
            Type type = new TypeToken<ArrayList<SpinnerEntity>>() {
            }.getType();
            ArrayList<SpinnerEntity> data = gson.fromJson(jsonObject.getString("addressEntityList"), type);
            RepairsAddressEntity repairsAddressEntity = new RepairsAddressEntity();
            repairsAddressEntity.setMsg(osEntity.getMsg());
            repairsAddressEntity.setRepairsTypeList(data);
            repairsAddressEntity.setResultCode(osEntity.getResultCode());
            return repairsAddressEntity;
        }else{
            return null;
        }
    }
}
