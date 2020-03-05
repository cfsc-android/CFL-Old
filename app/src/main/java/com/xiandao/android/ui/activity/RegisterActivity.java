package com.xiandao.android.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.xiandao.android.R;
import com.xiandao.android.entity.QQLoginEntity;
import com.xiandao.android.entity.WeiXinLoginEntity;
import com.xiandao.android.entity.eventbus.EventBusMessage;
import com.xiandao.android.entity.smart.BaseEntity;
import com.xiandao.android.entity.smart.OrderTypeListEntity;
import com.xiandao.android.entity.smart.SmsKeyEntity;
import com.xiandao.android.entity.smart.TokenEntity;
import com.xiandao.android.entity.smart.UserInfoEntity;
import com.xiandao.android.http.JsonParse;
import com.xiandao.android.http.MyCallBack;
import com.xiandao.android.http.XUtils;
import com.xiandao.android.ui.BaseActivity;
import com.xiandao.android.utils.FileManagement;
import com.xiandao.android.utils.Tools;
import com.xiandao.android.utils.Utils;
import com.xiandao.android.view.alertview.AlertView;

import org.greenrobot.eventbus.EventBus;
import org.xutils.LogUtils;
import org.xutils.app.LynActivityManager;
import org.xutils.event.annotation.ContentView;
import org.xutils.event.annotation.Event;
import org.xutils.event.annotation.ViewInject;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;

import static com.xiandao.android.base.Config.AUTH;
import static com.xiandao.android.base.Config.BASE_URL;
import static com.xiandao.android.base.Config.BASIC;
import static com.xiandao.android.base.Config.SMS;

@ContentView(R.layout.activity_register)
public class RegisterActivity extends BaseActivity {
    @ViewInject(R.id.tv_title_name)
    private TextView tv_title_name;
    @ViewInject(R.id.et_tel_no)
    private EditText et_tel_no;
    @ViewInject(R.id.et_tel_code)
    private EditText et_tel_code;
    @ViewInject(R.id.tv_tel_get_ver)
    private TextView tv_tel_get_ver;

    private boolean verFlag;
    private int verNum=0;
    private String validKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tv_title_name.setText("注册");
        et_tel_no.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(Utils.isMobileNO(et_tel_no.getText().toString())){
                    tv_tel_get_ver.setTextColor(getResources().getColor(R.color.payment_btn));
                    verFlag=true;
                }else{
                    tv_tel_get_ver.setTextColor(getResources().getColor(R.color.text_black));
                    verFlag=false;
                }
            }
        });
    }

    @Event({R.id.iv_title_left, R.id.tv_login_app_agreement, R.id.tv_tel_get_ver, R.id.btn_register,R.id.ll_umeng_login_weixin, R.id.ll_umeng_login_qq})
    private void onClickEvent(View v){
        switch (v.getId()){
            case R.id.iv_title_left:
                finish();
                break;
            case R.id.tv_login_app_agreement:
                Bundle bundle=new Bundle();
                bundle.putString("title","App使用协议");
                bundle.putString("url","http://dev.chanfine.com:9082/privacy/135310.html");
                openActivity(NewsInfoActivity.class,bundle);
                break;
            case R.id.tv_tel_get_ver:
                if(verFlag){
                    sendSMS();
                }
                break;
            case R.id.btn_register:
                if(TextUtils.isEmpty(et_tel_no.getText())){
                    showToast("手机号码不能为空");
                    return;
                }
                if(TextUtils.isEmpty(et_tel_code.getText())){
                    showToast("验证码不能为空");
                    return;
                }
                telRegister();
                break;
            case R.id.ll_umeng_login_weixin:
                authorization(SHARE_MEDIA.WEIXIN);
                break;
            case R.id.ll_umeng_login_qq:
                authorization(SHARE_MEDIA.QQ);
        }
    }

    //获取用户信息
    private void getUserInfo(String userId){
        XUtils.Get(BASE_URL+BASIC+"basic/householdInfo/"+userId,null,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtils.d(result);
                BaseEntity<UserInfoEntity> baseEntity= JsonParse.parse(result,UserInfoEntity.class);
                if(baseEntity.isSuccess()){
                    FileManagement.setUserInfo(baseEntity.getResult());//缓存用户信息
                    LynActivityManager.getScreenManager().finishActivity(LoginActivity.class);//杀掉登录界面
                    openActivity(MainActivity.class);//跳转到主界面
                    finish();//关闭当前页面
                }else{
                    showToast(baseEntity.getMessage());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                showToast(ex.getMessage());
            }

            @Override
            public void onFinished() {
                super.onFinished();
                stopProgressDialog();
            }
        });
    }

    //登录
    private void login(final String userId) {
        RequestParams params=new RequestParams(BASE_URL+AUTH+"oauth/user/household/login");
        params.addHeader("client_id","mobile");
        params.addHeader("client_secret","mobile");

        params.addBodyParameter("mobile",et_tel_no.getText().toString());
        params.addBodyParameter("key",validKey);
        params.addBodyParameter("validCode",et_tel_code.getText().toString());
        x.http().post(params,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtils.d(result);
                Gson gson = new Gson();
                TokenEntity token=gson.fromJson(result,TokenEntity.class);
                FileManagement.setTokenEntity(token);
                getUserInfo(userId);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                showToast(ex.getMessage());
                stopProgressDialog();
            }
        });

    }

    //手机号注册
    private void telRegister(){
        startProgressDialog("");
        Map<String,Object> map=new HashMap<>();
        map.put("loginMode","MOBILE");
        map.put("mobile",et_tel_no.getText().toString());
        map.put("validKey",validKey);
        map.put("validCode",et_tel_code.getText().toString());
        XUtils.PostJson(BASE_URL+BASIC+"basic/householdInfo/register",map,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtils.d(result);
                BaseEntity<String> baseEntity= JsonParse.parse(result,String.class);
                if(baseEntity.isSuccess()){
                    String userId= baseEntity.getResult();
                    login(userId);

                }else{
                    showToast(baseEntity.getMessage());
                    stopProgressDialog();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                showToast(ex.getMessage());
                stopProgressDialog();
            }
        });

    }

    //微信注册
    private void weiXinRegister(WeiXinLoginEntity weiXinLoginEntity){
        startProgressDialog("");
        Map<String,Object> requestMap=new HashMap<>();
        requestMap.put("qqNo",weiXinLoginEntity.getUnionid());
        requestMap.put("loginMode","WeiXin");
        requestMap.put("name",weiXinLoginEntity.getName());
        requestMap.put("nickName",weiXinLoginEntity.getScreen_name());
        XUtils.PostJson(BASE_URL+BASIC+"basic/householdInfo/register",requestMap,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtils.d(result);
                BaseEntity baseEntity= JsonParse.parse(result);
                if(baseEntity.isSuccess()){

                }else{
                    showToast(baseEntity.getMessage());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                showToast(ex.getMessage());
            }

            @Override
            public void onFinished() {
                super.onFinished();
                stopProgressDialog();
            }
        });
    }

    //QQ注册
    private void qqRegister(QQLoginEntity qqLoginEntity){
        startProgressDialog("");
        Map<String,Object> requestMap=new HashMap<>();
        requestMap.put("qqNo",qqLoginEntity.getUnionid());
        requestMap.put("loginMode","QQ");
        requestMap.put("nickName",qqLoginEntity.getName());
        XUtils.PostJson(BASE_URL+BASIC+"basic/householdInfo/register",requestMap,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtils.d(result);
                BaseEntity baseEntity= JsonParse.parse(result);
                if(baseEntity.isSuccess()){

                }else{
                    showToast(baseEntity.getMessage());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                showToast(ex.getMessage());
            }

            @Override
            public void onFinished() {
                super.onFinished();
                stopProgressDialog();
            }
        });
    }

    //授权
    private void authorization(SHARE_MEDIA share_media){
        startProgressDialog("授权中...");
        UMShareAPI.get(RegisterActivity.this).getPlatformInfo(RegisterActivity.this, share_media, new UMAuthListener(){
            @Override
            public void onStart(SHARE_MEDIA share_media) {
                Log.d("LoginActivity", share_media.toString()+" onStart 授权开始");
            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                Log.d("LoginActivity", share_media.toString()+i+" onComplete 授权完成");
                StringBuilder str=new StringBuilder();
                str.append("{");
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    str.append("\""+entry.getKey()+"\":\""+entry.getValue()+"\",");
                }
                String thirdLogin= str.toString();
                thirdLogin=thirdLogin.substring(0,thirdLogin.length()-1);
                thirdLogin+="}";
                Gson gson=new Gson();

                if(SHARE_MEDIA.WEIXIN==share_media){
                    WeiXinLoginEntity weiXinLoginEntity=gson.fromJson(thirdLogin, WeiXinLoginEntity.class);
                    weiXinRegister(weiXinLoginEntity);
                    Log.e("LoginActivity",weiXinLoginEntity.toString());
                }else if(SHARE_MEDIA.QQ==share_media){
                    QQLoginEntity qqLoginEntity=gson.fromJson(thirdLogin, QQLoginEntity.class);
                    qqRegister(qqLoginEntity);
                    Log.d("LoginActivity", qqLoginEntity.toString());
                }

            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                Log.d("LoginActivity", "onError 授权错误");
                stopProgressDialog();
                showShortToast("授权错误");
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
                Log.d("LoginActivity", share_media.toString()+" onCancel 授权取消");
                stopProgressDialog();
            }
        });

    }

    //获取验证码状态切换
    private void setVerFlag(boolean verFlag){
        this.verFlag=verFlag;
        if(verFlag){
            if(verNum==0){
                tv_tel_get_ver.setText("获取验证码");
                tv_tel_get_ver.setTextColor(getResources().getColor(R.color.payment_btn));
            }
        }else{
            tv_tel_get_ver.setTextColor(getResources().getColor(R.color.text_black));
            if(verNum==0){
                tv_tel_get_ver.setText("获取验证码");
            }
        }
    }

    //获取验证码
    private void sendSMS() {
        Map<String,Object> requestMap=new HashMap<>();
        requestMap.put("phone",et_tel_no.getText().toString());
        XUtils.Post(BASE_URL+SMS+"sms-internal/codes",requestMap,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtils.d(result);
                BaseEntity<SmsKeyEntity> baseEntity= JsonParse.parse(result,SmsKeyEntity.class);
                if(baseEntity.isSuccess()){
                    validKey = baseEntity.getResult().getKey();
                    handler.sendEmptyMessage(0);
                }else{
                    showToast(baseEntity.getMessage());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                showToast(ex.getMessage());
            }
        });

    }


    @SuppressLint("HandlerLeak")
    Handler handler=new Handler(){
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            if(msg.what==0){
                setVerFlag(false);
                verNum=60;
                handler.sendEmptyMessage(1);
            }else if(msg.what==1){
                tv_tel_get_ver.setText(verNum+"'s后重新获取");
                verNum--;
                if(verNum>0){
                    handler.sendEmptyMessageDelayed(1,1000);
                }else{
                    handler.removeMessages(1);
                    if(Utils.isMobileNO(et_tel_no.getText().toString())){
                        setVerFlag(true);
                    }
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeMessages(1);
    }
}
