package com.xiandao.android.httptask;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.gson.Gson;
import com.xiandao.android.entity.BaseParser;
import com.xiandao.android.entity.HikBaseEntity;
import com.xiandao.android.net.HttpUtils;
import com.xiandao.android.net.RequestVo;
import com.xiandao.android.utils.Tools;

import org.json.JSONException;

public class HikBaseTask extends BaseParser<Object> {
    protected Gson gson = null;
    private RequestVo vo;
    private Context context;

    private HikBaseEntity hikBaseEntity;

    public HikBaseEntity getHikBaseEntity() {
        return hikBaseEntity;
    }

    public void setHikBaseEntity(HikBaseEntity hikBaseEntity) {
        this.hikBaseEntity = hikBaseEntity;
    }


    @Override
    public Object parseJSON(Context context, String str) throws JSONException {
        this.context = context;
        if (Tools.isEmpty(str)) {
            return null;
        }
        gson = new Gson();
        setHikBaseEntity(gson.fromJson(str,HikBaseEntity.class));
        return str;
    }

    @Override
    public void reloadTask(RequestVo vo) throws Exception {
        this.vo=vo;
    }
    public void loadReloadData(final RequestVo revo) {
        try {
            if (revo == null) return;
            if (revo.getCallback() == null) return;
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    new HttpUtils(context).getServerForResult(revo.getCallback(), revo);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
