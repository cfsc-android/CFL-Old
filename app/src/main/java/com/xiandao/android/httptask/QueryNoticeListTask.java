package com.xiandao.android.httptask;

import android.content.Context;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.xiandao.android.entity.MyComplainEntity;
import com.xiandao.android.entity.MyComplainListEntity;
import com.xiandao.android.entity.NoticeEntity;
import com.xiandao.android.entity.NoticeListEntity;
import com.xiandao.android.entity.OverallSituationEntity;
import com.xiandao.android.entity.Pagination;
import com.xiandao.android.entity.RepairsDetailEntity;
import com.xiandao.android.utils.Tools;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * 此类描述的是:获取公告列表task
 *
 * @author TanYong
 *         create at 2017/5/2 10:39
 */
public class QueryNoticeListTask extends BaseTask {
    @Override
    public Object parseJSON(Context context, String str) throws JSONException {
        super.parseJSON(context, str);
        Log.e("QueryNoticeListTask=", str);
        if (!Tools.isEmpty(str)) {
            NoticeListEntity noticeListEntity = new NoticeListEntity();
            OverallSituationEntity osEntity = getOsEntity();
            JSONObject jsonObject = new JSONObject(str);
            String data = jsonObject.getString("data");
            if (!Tools.isEmpty(data)) {
                Type type = new TypeToken<ArrayList<NoticeEntity>>() {
                }.getType();
                ArrayList<NoticeEntity> noticeList = gson.fromJson(new JSONObject(data).getString("noticeList"), type);
                jsonObject = new JSONObject(data);
                Pagination pagination = gson.fromJson(jsonObject.getString("pagination"), Pagination.class);
                if (noticeList != null)
                    noticeListEntity.setNoticeList(noticeList);
                if (pagination != null)
                    noticeListEntity.setPagination(pagination);
            }
            noticeListEntity.setMsg(osEntity.getMsg());
            noticeListEntity.setResultCode(osEntity.getResultCode());
            return noticeListEntity;
        } else {
            return null;
        }
    }
}
