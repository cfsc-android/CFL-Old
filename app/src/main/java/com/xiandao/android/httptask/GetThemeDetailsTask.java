package com.xiandao.android.httptask;

import android.content.Context;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.xiandao.android.entity.ThemeDetailsEntity;
import com.xiandao.android.entity.ThemeEntity;
import com.xiandao.android.entity.ThemeListEntity;
import com.xiandao.android.utils.Tools;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * 此类描述的是:获取帖子详情task
 *
 * @author TanYong
 *         create at 2017/5/2 10:44
 */
public class GetThemeDetailsTask extends BaseTask {
    @Override
    public Object parseJSON(Context context, String str) throws JSONException {
        super.parseJSON(context, str);
        Log.e("GetThemeDetailsTask=", str);
        if (!Tools.isEmpty(str)) {
            JSONObject jsonObject = new JSONObject(str);
            String data = jsonObject.getString("data");
            String resultCode = jsonObject.getString("resultCode");
            String msg = jsonObject.getString("msg");
            Type type = new TypeToken<ThemeEntity>() {
            }.getType();
            ThemeEntity themeEntitie = gson.fromJson(data, type);
            ThemeDetailsEntity themeDetailsEntity = new ThemeDetailsEntity();
            themeDetailsEntity.setResultCode(resultCode);
            themeDetailsEntity.setMsg(msg);
            themeDetailsEntity.setData(themeEntitie);
            return themeDetailsEntity;
        } else {
            return null;
        }
    }
}
