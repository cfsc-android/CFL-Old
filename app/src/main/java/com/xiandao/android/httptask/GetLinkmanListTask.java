package com.xiandao.android.httptask;

import android.content.Context;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.xiandao.android.entity.GetLinkmanListResultEntity;
import com.xiandao.android.entity.LinkmanEntity;
import com.xiandao.android.utils.Tools;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * 此类描述的是:获取联系人列表task
 *
 * @author TanYong
 *         create at 2017/5/2 10:58
 */
public class GetLinkmanListTask extends BaseTask {
    @Override
    public Object parseJSON(Context context, String str) throws JSONException {
        super.parseJSON(context, str);
        //{"data":{"linkmanEntityList":[]},"resultCode":"0","msg":"查询成功"}
        Log.e("GetLinkmanListTask=", str);
        if (!Tools.isEmpty(str)) {
            JSONObject jsonObject = new JSONObject(str);
            String data = jsonObject.getString("data");
            String resultCode = jsonObject.getString("resultCode");
            String msg = jsonObject.getString("msg");
            Type type = new TypeToken<ArrayList<LinkmanEntity>>() {
            }.getType();
            ArrayList<LinkmanEntity> linkmanEntityList = gson.fromJson(new JSONObject(data).getString("linkmanEntityList"), type);
            GetLinkmanListResultEntity getLinkmanListResultEntity = new GetLinkmanListResultEntity();
            getLinkmanListResultEntity.setResultCode(resultCode);
            getLinkmanListResultEntity.setMsg(msg);
            getLinkmanListResultEntity.setLinkmanEntityList(linkmanEntityList);
            return getLinkmanListResultEntity;
        } else {
            return null;
        }
    }
}
