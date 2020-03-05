package com.xiandao.android.httptask;

import android.content.Context;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.xiandao.android.entity.OverallSituationEntity;
import com.xiandao.android.entity.QueryRoomsResultEntity;
import com.xiandao.android.entity.SpinnerEntity;
import com.xiandao.android.utils.Tools;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * 此类描述的是:查询房间task
 *
 * @author TanYong
 *         create at 2017/4/21 16:14
 */
public class QueryRoomsTask extends BaseTask {
    @Override
    public Object parseJSON(Context context, String str) throws JSONException {
        super.parseJSON(context, str);
        Log.e("QueryRoomsTask=", str);
        if (!Tools.isEmpty(str)) {
            QueryRoomsResultEntity roomsResultEntity = new QueryRoomsResultEntity();
            JSONObject jsonObject = new JSONObject(str);
            String data = jsonObject.getString("data");
            OverallSituationEntity overallSituationEntity = getOsEntity();
            if (data != null) {
                Type type = new TypeToken<ArrayList<SpinnerEntity>>() {
                }.getType();
                ArrayList<SpinnerEntity> entityArrayList = gson.fromJson(data, type);
                roomsResultEntity.setEntities(entityArrayList);
            }
            roomsResultEntity.setMsg(overallSituationEntity.getMsg());
            roomsResultEntity.setResultCode(overallSituationEntity.getResultCode());
            return roomsResultEntity;
        } else {
            return null;
        }
    }
}
