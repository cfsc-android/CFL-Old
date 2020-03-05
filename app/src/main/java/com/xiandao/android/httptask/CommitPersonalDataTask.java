package com.xiandao.android.httptask;

import android.os.Handler;
import android.util.Log;

import com.google.gson.Gson;
import com.xiandao.android.entity.PersonalInfomation;
import com.xiandao.android.http.HttpDataConnet;
import com.xiandao.android.utils.Tools;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 提交个人资料
 */
public class CommitPersonalDataTask extends HttpDataConnet {

    /**
     * @param handler 异步任务
     * @param httpCmd 请求类型
     * @构造函数
     * @说明：无
     */
    public CommitPersonalDataTask(Handler handler, int httpCmd) {
        super(handler, httpCmd);
    }

    private String resultCode;
    private String msg;
    private PersonalInfomation data;

    public PersonalInfomation getData() {
        return data;
    }

    public void setData(PersonalInfomation data) {
        this.data = data;
    }

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
        Log.e("CommitPersonalDataTask", info);
        if (Tools.isEmpty(info)) {
            return;
        }
        try {
            JSONObject jsonObject = new JSONObject(info);
            if (jsonObject == null) {
                return;
            }

            String data = jsonObject.getString("data");
            if (!Tools.isEmpty(data)) {
                Gson gson = new Gson();
                PersonalInfomation entity = gson.fromJson(data, PersonalInfomation.class);
                if (null != entity) {
                    setData(entity);
                }
            }
            setResultCode(jsonObject.getString("resultCode"));
            setMsg(jsonObject.getString("msg"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
