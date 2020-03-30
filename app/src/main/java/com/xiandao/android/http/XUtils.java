package com.xiandao.android.http;

import com.google.gson.Gson;
import com.xiandao.android.entity.smart.TokenEntity;
import com.xiandao.android.utils.FileManagement;
import com.xiandao.android.utils.TokenUtils;

import org.xutils.common.Callback;
import org.xutils.common.Callback.Cancelable;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.Map;

/**
 * Created by Administrator on 2016/7/19.
 */
public class XUtils {
    /**
     * 发送get请求
     * @param <T>
     */
    public static <T> Callback.Cancelable Get(String url, Map<String, String> map, Callback.CommonCallback<T> callback){
        LogUtil.d(url);
        RequestParams params=new RequestParams(url);
        params=addAuthorization(params);
        if(null!=map){
            for(Map.Entry<String, String> entry : map.entrySet()){
                params.addQueryStringParameter(entry.getKey(), entry.getValue());
            }
        }
        Map<String,String> headers=params.getHeaders();
        for (Map.Entry<String,String> entry:headers.entrySet()) {
            LogUtil.d(entry.getKey()+"-"+entry.getValue());
        }
        return x.http().get(params, callback);
    }



    /**
     * 发送异步post请求
     *
     * @param <T>
     */
    public static <T> Callback.Cancelable Post(String url, Map<String, Object> map, Callback.CommonCallback<T> callback) {
        LogUtil.d(url);
        RequestParams params = new RequestParams(url);
        params=addAuthorization(params);
        if (null != map) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                params.addParameter(entry.getKey(), entry.getValue());
            }
        }
        Map<String,String> headers=params.getHeaders();
        for (Map.Entry<String,String> entry:headers.entrySet()) {
            LogUtil.d(entry.getKey()+"-"+entry.getValue());
        }
        return x.http().post(params, callback);
    }


    /**
     * 发送异步post请求
     *
     * @param <T>
     */
    public static <T> Callback.Cancelable PostJson(String url, Object object, Callback.CommonCallback<T> callback) {
        LogUtil.d(url);
        RequestParams params = new RequestParams(url);
        params=addAuthorization(params);
        if (null != object) {
            Gson gson=new Gson();
            LogUtil.d(gson.toJson(object));
            params.setAsJsonContent(true);
            params.setBodyContent(gson.toJson(object));
        }
        Map<String,String> headers=params.getHeaders();
        for (Map.Entry<String,String> entry:headers.entrySet()) {
            LogUtil.d(entry.getKey()+"-"+entry.getValue());
        }

        return x.http().post(params, callback);
    }

    /**
     * 发送异步put请求
     *
     * @param <T>
     */
    public static <T> Callback.Cancelable Put(String url, Map<String, Object> map, Callback.CommonCallback<T> callback) {
        LogUtil.d(url);
        RequestParams params = new RequestParams(url);
        params=addAuthorization(params);
        if (null != map) {
            Gson gson=new Gson();
            LogUtil.d(gson.toJson(map));
            params.setAsJsonContent(true);
            params.setBodyContent(gson.toJson(map));
        }
        Map<String,String> headers=params.getHeaders();
        for (Map.Entry<String,String> entry:headers.entrySet()) {
            LogUtil.d(entry.getKey()+"-"+entry.getValue());
        }
        return x.http().request(HttpMethod.PUT,params, callback);
    }

    /**
     * 发送异步put请求
     *
     * @param <T>
     */
    public static <T> Callback.Cancelable PutNormal(String url, Map<String, Object> map, Callback.CommonCallback<T> callback) {
        LogUtil.d(url);
        RequestParams params = new RequestParams(url);
        params=addAuthorization(params);
        if (null != map) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                params.addParameter(entry.getKey(), entry.getValue());
            }
        }
        Map<String,String> headers=params.getHeaders();
        for (Map.Entry<String,String> entry:headers.entrySet()) {
            LogUtil.d(entry.getKey()+"-"+entry.getValue());
        }
        return x.http().request(HttpMethod.PUT,params, callback);
    }


    /**
     * 发送异步put请求
     *
     * @param <T>
     */
    public static <T> Callback.Cancelable Delete(String url, Map<String, Object> map, Callback.CommonCallback<T> callback) {
        LogUtil.d(url);
        RequestParams params = new RequestParams(url);
        params=addAuthorization(params);
        if (null != map) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                params.addParameter(entry.getKey(), entry.getValue());
            }
        }
        Map<String,String> headers=params.getHeaders();
        for (Map.Entry<String,String> entry:headers.entrySet()) {
            LogUtil.d(entry.getKey()+"-"+entry.getValue());
        }
        return x.http().request(HttpMethod.DELETE,params, callback);
    }

    private static RequestParams addAuthorization(RequestParams params){
//        if(Config.ENV.equals("release")){
            TokenEntity tokenEntity = FileManagement.getTokenEntity();
            if(tokenEntity!=null){
                params.addHeader("Authorization","bearer "+tokenEntity.getAccess_token());
//                params.addHeader("test","bearer "+tokenEntity.getAccess_token());
            }
//        }
        return params;
    }


    /**
     * 海康云眸开放平台post请求
     * @param url
     * @param map
     * @param callback
     * @param <T>
     * @return
     */
    public static <T> void HikCloudPost(final String url, final Map<String, Object> map, final CommonCallback<T> callback){
        if(TokenUtils.tokenValid()){
            request(HttpType.POST,url,map,null,callback);
        }else{
            TokenUtils.initHikCloudToken(new TokenUtils.OnInitHikCloudToken() {
                @Override
                public void success(String token) {
                    request(HttpType.POST,url,map,null,callback);
                }

                @Override
                public void fail(Throwable ex) {
                    callback.onError(ex,false);
                }

            });
        }
    }

    public static <T> Cancelable request(HttpType type,String url,Map<String, Object> map1,Map<String, String> map2,CommonCallback<T> callback){
        RequestParams params = new RequestParams(url);
        params.addHeader("Authorization", "bearer "+TokenUtils.getToken());
        if(type==HttpType.GET){
            if (null != map2) {
                for (Map.Entry<String, String> entry : map2.entrySet()) {
                    params.addParameter(entry.getKey(), entry.getValue());
                }
            }
            return x.http().get(params, callback);
        }else{
            if (null != map1) {
                for (Map.Entry<String, Object> entry : map1.entrySet()) {
                    params.addParameter(entry.getKey(), entry.getValue());
                }
            }
            return x.http().post(params, callback);
        }

    }

    public static <T> Cancelable UploadFile(RequestParams params,CommonCallback<T> callback){
        params.setMultipart(true);
        Cancelable cancelable = x.http().post(params, callback);
        return cancelable;
    }

    /**
     * 上传文件
     *
     * @param <T>
     */
    public static <T> Callback.Cancelable UpLoadFile(String url, Map<String, String> map, Map<String, File> fileMap, Callback.CommonCallback<T> callback) {
        RequestParams params = new RequestParams(url);
        if (null != map) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                params.addBodyParameter(entry.getKey(), entry.getValue());
            }
            for (Map.Entry<String, File> entry : fileMap.entrySet()) {
                params.addBodyParameter(entry.getKey(), entry.getValue());
            }
        }
        params.setMultipart(true);
        return x.http().post(params, callback);
    }


    public enum HttpType{
        GET,
        POST
    }
}
