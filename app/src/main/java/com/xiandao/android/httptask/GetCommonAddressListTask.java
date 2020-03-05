package com.xiandao.android.httptask;

import android.content.Context;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.xiandao.android.entity.GetCommonAddressListResultEntity;
import com.xiandao.android.entity.RoomInfoEntity;
import com.xiandao.android.utils.Tools;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * 此类描述的是:获取常用地址列表task
 *
 * @author TanYong
 *         create at 2017/5/2 10:58
 */
public class GetCommonAddressListTask extends BaseTask {
    @Override
    public Object parseJSON(Context context, String str) throws JSONException {
        super.parseJSON(context, str);
        Log.e("GetCommonAddressListTask=", str);
        if (!Tools.isEmpty(str)) {
            JSONObject jsonObject = new JSONObject(str);
            String data = jsonObject.getString("data");
            String resultCode = jsonObject.getString("resultCode");
            String msg = jsonObject.getString("msg");
            Type type = new TypeToken<ArrayList<RoomInfoEntity>>() {
            }.getType();
            ArrayList<RoomInfoEntity> roomEntityList = gson.fromJson(new JSONObject(data).getString("roomEntityList"), type);
            GetCommonAddressListResultEntity getCommonAddressListResultEntity = new GetCommonAddressListResultEntity();
            getCommonAddressListResultEntity.setResultCode(resultCode);
            getCommonAddressListResultEntity.setMsg(msg);
            getCommonAddressListResultEntity.setRoomEntityList(roomEntityList);
            return getCommonAddressListResultEntity;
        } else {
            return null;
        }
    }
}
