package com.xiandao.android.httptask;

import android.content.Context;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.xiandao.android.entity.MyComplainEntity;
import com.xiandao.android.entity.MyComplainListEntity;
import com.xiandao.android.entity.OverallSituationEntity;
import com.xiandao.android.entity.Pagination;
import com.xiandao.android.utils.Tools;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * 此类描述的是:获取我的投诉列表task
 *
 * @author TanYong
 *         create at 2017/5/2 10:42
 */
public class QueryMyComplainListTask extends BaseTask {
    @Override
    public Object parseJSON(Context context, String str) throws JSONException {
        super.parseJSON(context, str);
        Log.e("QueryMyComplainListTask=", str);
        if (!Tools.isEmpty(str)) {
            MyComplainListEntity myComplainListEntity = new MyComplainListEntity();
            OverallSituationEntity osEntity = getOsEntity();
            JSONObject jsonObject = new JSONObject(str);
            String data = jsonObject.getString("data");
            if (!Tools.isEmpty(data)) {
                Type type = new TypeToken<ArrayList<MyComplainEntity>>() {
                }.getType();
                ArrayList<MyComplainEntity> myComplainEntities = gson.fromJson(new JSONObject(data).getString("complainEntityList"), type);
                jsonObject = new JSONObject(data);
                Pagination pagination = gson.fromJson(jsonObject.getString("pagination"), Pagination.class);
                if (myComplainEntities != null)
                    myComplainListEntity.setComplainEntityList(myComplainEntities);
                if (pagination != null)
                    myComplainListEntity.setPagination(pagination);
            }
            myComplainListEntity.setMsg(osEntity.getMsg());
            myComplainListEntity.setResultCode(osEntity.getResultCode());
            return myComplainListEntity;
        } else {
            return null;
        }
    }
}
