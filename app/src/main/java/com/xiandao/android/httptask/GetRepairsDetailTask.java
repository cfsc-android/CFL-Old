package com.xiandao.android.httptask;

import android.content.Context;
import android.util.Log;

import com.xiandao.android.entity.OverallSituationEntity;
import com.xiandao.android.entity.RepairsDetailEntity;
import com.xiandao.android.utils.Tools;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 此类描述的是:获取报修详情task
 *
 * @author TanYong
 *         create at 2017/5/2 10:45
 */
public class GetRepairsDetailTask extends BaseTask {
    @Override
    public Object parseJSON(Context context, String str) throws JSONException {
        super.parseJSON(context, str);
        Log.e("GetRepairsDetailTask=", str);
        if (!Tools.isEmpty(str)) {
            OverallSituationEntity osEntity=getOsEntity();
            JSONObject jsonObject = new JSONObject(str);
            String data = jsonObject.getString("data");
            RepairsDetailEntity entity = gson.fromJson(data, RepairsDetailEntity.class);
            entity.setMsg(osEntity.getMsg());
            entity.setResultCode(osEntity.getResultCode());
            return entity;
        } else {
            return null;
        }
    }
}
