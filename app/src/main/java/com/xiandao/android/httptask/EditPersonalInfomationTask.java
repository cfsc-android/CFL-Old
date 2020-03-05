package com.xiandao.android.httptask;

import android.content.Context;
import android.util.Log;

import com.xiandao.android.entity.OverallSituationEntity;
import com.xiandao.android.utils.Tools;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 此类描述的是:编辑个人资料task
 * @author TanYong
 * create at 2017/5/2 10:58
 */
public class EditPersonalInfomationTask extends BaseTask {
    @Override
    public Object parseJSON(Context context, String str) throws JSONException {
        super.parseJSON(context, str);
        Log.e("EditPersonalInfomationTask=", str);
        if (!Tools.isEmpty(str)) {
            JSONObject jsonObject = new JSONObject(str);
            String data = jsonObject.getString("data");
            if (Tools.isEmpty(data)) {
                data = "";
            }
            OverallSituationEntity overallSituationEntity = getOsEntity();
            overallSituationEntity.setData(data);
            return overallSituationEntity;
        } else {
            return null;
        }
    }
}