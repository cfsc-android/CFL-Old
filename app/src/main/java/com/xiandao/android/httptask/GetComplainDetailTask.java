package com.xiandao.android.httptask;

import android.content.Context;
import android.util.Log;

import com.xiandao.android.entity.ComplainDetailEntity;
import com.xiandao.android.entity.OverallSituationEntity;
import com.xiandao.android.utils.Tools;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 此类描述的是:获取我的投诉详情信息task
 *
 * @author TanYong
 *         create at 2017/5/2 10:44
 */
public class GetComplainDetailTask extends BaseTask {
    @Override
    public Object parseJSON(Context context, String str) throws JSONException {
        super.parseJSON(context, str);
        Log.e("GetComplainDetailTask=", str);
        if (!Tools.isEmpty(str)) {
            OverallSituationEntity osEntity = getOsEntity();
            JSONObject jsonObject = new JSONObject(str);
            String data = jsonObject.getString("data");
            ComplainDetailEntity entity = gson.fromJson(data, ComplainDetailEntity.class);
            entity.setMsg(osEntity.getMsg());
            entity.setResultCode(osEntity.getResultCode());
            return entity;
        } else {
            return null;
        }
    }
}
