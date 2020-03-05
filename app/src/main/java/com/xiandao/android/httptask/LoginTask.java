package com.xiandao.android.httptask;

import android.content.Context;
import android.util.Log;

import com.xiandao.android.entity.LoginResultDataEntity;
import com.xiandao.android.entity.OverallSituationEntity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 此类描述的是:登录task
 *
 * @author TanYong
 *         create at 2017/4/21 16:14
 */
public class LoginTask extends BaseTask {
    @Override
    public Object parseJSON(Context context, String str) throws JSONException {
        super.parseJSON(context, str);
        Log.e("LoginTask=", str);
        JSONObject jsonObject = new JSONObject(str);
        OverallSituationEntity osEntity = getOsEntity();
        LoginResultDataEntity entity = gson.fromJson(jsonObject.getString("data"), LoginResultDataEntity.class);
        if (entity == null) {
            entity = new LoginResultDataEntity();
        }
        entity.setOsEntity(osEntity);
        return entity;
    }
}
