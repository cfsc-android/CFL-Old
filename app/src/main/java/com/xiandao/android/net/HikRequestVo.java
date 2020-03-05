package com.xiandao.android.net;

import android.content.Context;

import com.xiandao.android.entity.BaseParser;

public class HikRequestVo {
    private Context context;
    private String api;
    private String body;
    private BaseParser<?> jsonParser;

    public HikRequestVo(Context context, String api, String body, BaseParser<?> jsonParser) {
        this.context = context;
        this.api = api;
        this.body = body;
        this.jsonParser = jsonParser;
    }
    private HttpUtils.DataCallBack callback;

    public void setHttpDataCallBack(HttpUtils.DataCallBack callback) {
        this.callback = callback;
    }

    public HttpUtils.DataCallBack getCallback() {
        return callback;
    }
}
