package com.xiandao.android.net;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.xiandao.android.base.BaseParameter;
import com.xiandao.android.entity.BaseParser;
import com.xiandao.android.utils.Constants;

//import org.json.JSONException;
//import org.json.JSONObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * 此类描述的是:请求工具类
 *
 * @author TanYong
 *         create at 2017/4/21 9:29
 */
public class RequestVo {
    public String requestUrl = null;//默认地址头 sp.getString("requestUrl", "");
    public Context context;
    private boolean isShowDialog = true;//是否弹出进度框
    private String type = "get";//以什么方式提交 get或post 默认是get
    public Map<String, Object> requestDataMap;//请求工具
    public BaseParser<?> jsonParser;
    private String api;
    private String body;
    private String httpMthod;

    public RequestVo() {
        super();
    }

    /**
     * 以什么方式提交 get或post 默认是get
     */
    public String getType() {
        return type;
    }

    /**
     * 以什么方式提交 get或post 默认是post
     */
    public void setType(String type) {
        this.type = type;
    }

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getHttpMthod() {
        return httpMthod;
    }

    public void setHttpMthod(String httpMthod) {
        this.httpMthod = httpMthod;
    }

    /**
     * 是否要弹Dialog
     *
     * @return
     */
    public boolean isShowDialog() {
        return isShowDialog;
    }

    /**
     * 是否要弹Dialog 默认是true,如果不弹,则改成false;
     */
    public void setShowDialog(boolean isShowDialog) {
        this.isShowDialog = isShowDialog;
    }

    /**
     * @param context        上下文
     * @param requestDataMap 请求参数
     * @param jsonParser     解析工具类
     */
    public RequestVo(Context context, Map<String, Object> requestDataMap, BaseParser<?> jsonParser) {
        super();
        this.context = context;
        SharedPreferences sp = context.getSharedPreferences("config",
                Context.MODE_PRIVATE);
        requestUrl = sp.getString("requestUrl", "");
        this.requestDataMap = requestDataMap;
        this.jsonParser = jsonParser;
    }

    /**
     * @param requehstUrl    请求根url
     * @param context        上下文
     * @param requestDataMap 请求参数
     * @param jsonParser     解析工具类
     */
    public RequestVo(String requehstUrl, Context context,
                     Map<String, Object> requestDataMap,
                     BaseParser<?> jsonParser) {
        super();
        this.requestUrl = Constants.HOST + requehstUrl;
        this.context = context;
        this.requestDataMap = requestDataMap;
        this.jsonParser = jsonParser;
        Log.e("requestUrl=", requestUrl + "?" + BaseParameter.orderParamsMapAndReturnParamsString1(requestDataMap));
    }

    /**
     *
     * @param httpMthod
     * @param api
     * @param context
     * @param requestDataMap
     * @param jsonParser
     */
    public RequestVo(String httpMthod,String api, Context context,Map<String, Object> requestDataMap,BaseParser<?> jsonParser) {
        super();
        this.httpMthod=httpMthod;
        this.api=api;
        this.requestUrl = Constants.HIKHOST + api;
        this.context = context;
        JSONObject jsonBody = new JSONObject(requestDataMap);
//        JSONObject jsonBody = new JSONObject();
//        try {
//            for (Map.Entry<String, Object> entry : requestDataMap.entrySet()) {
//                jsonBody.put(entry.getKey(),entry.getValue());
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        Log.e("body",jsonBody.toString());
        this.body=jsonBody.toString();
        this.jsonParser = jsonParser;
    }

    /**
     *
     * @param httpMthod
     * @param api
     * @param context
     * @param body
     * @param jsonParser
     */
    public RequestVo(String httpMthod,String api, Context context,String body,BaseParser<?> jsonParser) {
        super();
        this.httpMthod=httpMthod;
        this.api=api;
        this.requestUrl = Constants.HIKHOST + api;
        this.context = context;
        this.body=body;
        this.jsonParser = jsonParser;
    }

    private HttpUtils.DataCallBack callback;

    public void setHttpDataCallBack(HttpUtils.DataCallBack callback) {
        this.callback = callback;
    }

    public HttpUtils.DataCallBack getCallback() {
        return callback;
    }

    public void setUrl(String url) {
        this.requestUrl = url;
    }
}
