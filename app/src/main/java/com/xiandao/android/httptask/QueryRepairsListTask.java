package com.xiandao.android.httptask;

import android.content.Context;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.xiandao.android.entity.MyRepairsEntity;
import com.xiandao.android.entity.MyRepairsListEntity;
import com.xiandao.android.entity.Pagination;
import com.xiandao.android.utils.Tools;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * 此类描述的是:获取报修列表task
 *
 * @author TanYong
 *         create at 2017/5/2 10:45
 */
public class QueryRepairsListTask extends BaseTask {
    @Override
    public Object parseJSON(Context context, String str) throws JSONException {
        super.parseJSON(context, str);
        Log.e("QueryRepairsListTask=", str);
        if (!Tools.isEmpty(str)) {
            JSONObject jsonObject = new JSONObject(str);
            String data = jsonObject.getString("data");
            String resultCode = jsonObject.getString("resultCode");
            String msg = jsonObject.getString("msg");
            Type type = new TypeToken<ArrayList<MyRepairsEntity>>() {
            }.getType();
            ArrayList<MyRepairsEntity> myRepairsEntities = gson.fromJson(new JSONObject(data).getString("repairsEntityList"), type);
            jsonObject = new JSONObject(data);
            Pagination pagination = gson.fromJson(jsonObject.getString("pagination"), Pagination.class);
            MyRepairsListEntity myRepairsListEntity = new MyRepairsListEntity();
            myRepairsListEntity.setRepairsEntityList(myRepairsEntities);
            myRepairsListEntity.setPagination(pagination);
            myRepairsListEntity.setMsg(msg);
            myRepairsListEntity.setResultCode(resultCode);
            return myRepairsListEntity;
        } else {
            return null;
        }
    }
}
