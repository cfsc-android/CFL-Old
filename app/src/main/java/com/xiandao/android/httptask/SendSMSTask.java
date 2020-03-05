package com.xiandao.android.httptask;

import android.content.Context;
import android.util.Log;

import com.xiandao.android.entity.SMSEntity;
import com.xiandao.android.utils.Tools;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 此类描述的是:发送短信验证码task
 * {"code":200,"msg":"6","obj":"6372"}
 *
 * @author TanYong
 *         create at 2017/4/21 10:41
 */
public class SendSMSTask extends BaseTask {
    @Override
    public Object parseJSON(Context context, String str) throws JSONException {
        super.parseJSON(context, str);
        Log.e("SendSMSTask=", str);
        if (!Tools.isEmpty(str)) {
//            JSONObject jsonObject = new JSONObject(str);
//            String data = jsonObject.getString("data");
//            String resultCode = jsonObject.getString("resultCode");
//            String msg = jsonObject.getString("msg");
//            SMSEntity smsEntity = new SMSEntity();
//            smsEntity.setResultCode(resultCode);
//            smsEntity.setMsg(msg);
//            jsonObject = new JSONObject(data);
//            String obj = jsonObject.getString("obj");
//            smsEntity.setObj(obj);
            return getOsEntity();
        } else {
            return null;
        }
    }
}
