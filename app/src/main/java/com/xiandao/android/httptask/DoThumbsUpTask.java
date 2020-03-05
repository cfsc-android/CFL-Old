package com.xiandao.android.httptask;

import android.content.Context;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.xiandao.android.entity.ThemeDetailsEntity;
import com.xiandao.android.entity.ThemeEntity;
import com.xiandao.android.utils.Tools;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

/**
 * 此类描述的是:点赞或关注task
 *
 * @author TanYong
 *         create at 2017/5/2 10:44
 */
public class DoThumbsUpTask extends BaseTask {
    @Override
    public Object parseJSON(Context context, String str) throws JSONException {
        super.parseJSON(context, str);
        Log.e("DoThumbsUpTask=", str);
        if (!Tools.isEmpty(str)) {
            String[] status = {"", ""};
            JSONObject jsonObject = new JSONObject(str);
            String data = jsonObject.getString("data");
            String resultCode = jsonObject.getString("resultCode");
            String msg = jsonObject.getString("msg");
            status[0] = resultCode;
            status[1] = msg;
            return status;
        } else {
            return null;
        }
    }
}
