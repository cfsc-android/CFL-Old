package com.xiandao.android.utils;

import android.util.Log;

import com.google.gson.Gson;
import com.xiandao.android.entity.hikcloud.AccessToken;
import com.xiandao.android.http.MyCallBack;
import com.xiandao.android.http.XUtils;
import com.xiandao.android.http.hikhttp.HikConfig;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zengx on 2019/4/25.
 * Describe:
 */
public class TokenUtils {
    public static void initHikCloudToken(final OnInitHikCloudToken onInitHikCloudToken){
        XUtils.Get(Constants.HOST+"/getHIKVisionAccessToken.action",null,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                Log.e("token",result);
                Gson gson=new Gson();
                AccessToken token=gson.fromJson(result,AccessToken.class);
                token.setInit_time(new Date().getTime()/1000);
                FileManagement.setAccessToken(token);
                FileManagement.setHikToken(token.getAccess_token());
                if(onInitHikCloudToken!=null){
                    onInitHikCloudToken.success(token.getAccess_token());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                if(onInitHikCloudToken!=null){
                    onInitHikCloudToken.fail(ex);
                }
            }
        });
    }

    public static String getToken(){
       return FileManagement.getAccessToken().getAccess_token();
    }

    public static boolean tokenValid(){
        if (!Utils.isEmpty(FileManagement.getHikToken())) {
            AccessToken accessToken=FileManagement.getAccessToken();
            if(accessToken!=null){
                try {
                    long cur_time=new Date().getTime();
                    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    long expires_time = sdf.parse(accessToken.getExpire_time()).getTime();
                    return cur_time < expires_time;
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        }
        return false;
    }

    public interface OnInitHikCloudToken{
        void success(String token);
        void fail(Throwable ex);
    }
}
