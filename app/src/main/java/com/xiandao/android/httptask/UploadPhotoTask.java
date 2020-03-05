package com.xiandao.android.httptask;

import android.os.Handler;
import android.util.Log;

import com.xiandao.android.http.HttpDataConnet;
import com.xiandao.android.utils.Tools;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 上传图片
 */
public class UploadPhotoTask extends HttpDataConnet {

    /**
     * @param handler 异步任务
     * @param httpCmd 请求类型
     * @构造函数
     * @说明：无
     */
    public UploadPhotoTask(Handler handler, int httpCmd) {
        super(handler, httpCmd);
    }

    private String resultCode;
    private String msg;

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public void responDataParse(Object... param) {
        String info = (String) param[1];
        Log.e("UploadPhotoTask",info);
        if (Tools.isEmpty(info)) return;
        try {
            JSONObject jsonObject = new JSONObject(info);
            if (jsonObject == null) return;
            setResultCode(jsonObject.getString("resultCode"));
            setMsg(jsonObject.getString("msg"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
