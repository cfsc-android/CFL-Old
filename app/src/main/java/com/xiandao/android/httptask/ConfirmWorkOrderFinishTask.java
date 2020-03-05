package com.xiandao.android.httptask;

import android.content.Context;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.xiandao.android.entity.ConfirmWorkOrderFinishResultEntity;
import com.xiandao.android.utils.Tools;

import org.json.JSONException;

import java.lang.reflect.Type;

/**
 * 此类描述的是:判断是跳转到支付还是评价界面task
 *
 * @author TanYong
 *         create at 2017/5/2 10:45
 */
public class ConfirmWorkOrderFinishTask extends BaseTask {
    @Override
    public Object parseJSON(Context context, String str) throws JSONException {
        super.parseJSON(context, str);
        Log.e("ConfirmWorkOrderFinishTask=", str);
        if (!Tools.isEmpty(str)) {
            Type type = new TypeToken<ConfirmWorkOrderFinishResultEntity>() {
            }.getType();
            ConfirmWorkOrderFinishResultEntity confirmWorkOrderFinishResultEntity = gson.fromJson(str, type);
            return confirmWorkOrderFinishResultEntity;
        } else {
            return null;
        }
    }
}
