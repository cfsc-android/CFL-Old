package com.xiandao.android.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.xiandao.android.R;
import com.xiandao.android.entity.DeviceInfoEntity;
import com.xiandao.android.entity.HikBaseEntity;
import com.xiandao.android.entity.HikUser;
import com.xiandao.android.entity.LoginResultDataEntity;
import com.xiandao.android.entity.LoginUserEntity;
import com.xiandao.android.entity.OverallSituationEntity;
import com.xiandao.android.entity.ProjectInfo;
import com.xiandao.android.entity.QQLoginEntity;
import com.xiandao.android.entity.RoomInfoEntity;
import com.xiandao.android.entity.ThirdInfoEntity;
import com.xiandao.android.entity.WeiXinLoginEntity;
import com.xiandao.android.entity.eventbus.EventBusMessage;
import com.xiandao.android.entity.smart.BaseEntity;
import com.xiandao.android.entity.smart.SmsKeyEntity;
import com.xiandao.android.entity.smart.TokenEntity;
import com.xiandao.android.entity.smart.UserInfoEntity;
import com.xiandao.android.http.JsonParse;
import com.xiandao.android.http.MyCallBack;
import com.xiandao.android.http.XUtils;
import com.xiandao.android.net.ApiHttpResult;
import com.xiandao.android.net.HttpUtils;
import com.xiandao.android.net.NetUtil;
import com.xiandao.android.ui.BaseActivity;
import com.xiandao.android.utils.Constants;
import com.xiandao.android.utils.FileManagement;
import com.xiandao.android.utils.Tools;
import com.xiandao.android.view.EditTextDelView;
import com.xiandao.android.view.alertview.AlertView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.LogUtils;
import org.xutils.app.LynActivityManager;
import org.xutils.event.annotation.ContentView;
import org.xutils.event.annotation.Event;
import org.xutils.event.annotation.ViewInject;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static com.xiandao.android.base.Config.AUTH;
import static com.xiandao.android.base.Config.BASE_URL;
import static com.xiandao.android.base.Config.BASIC;
import static com.xiandao.android.base.Config.SMS;


/**
 * 此类描述的是:登录activity
 *
 * @author TanYong
 * create at 2017/5/8 14:15
 */
@ContentView(R.layout.activity_login)
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.btn_login)
    private Button btnLogin;
    @ViewInject(R.id.tv_get_code)
    private TextView tvGetCode;
    @ViewInject(R.id.etd_user_mobile_number)
    private EditTextDelView etdUserMobileNumber;
    @ViewInject(R.id.et_user_mobile_code)
    private EditText etUserMobileCode;
    @ViewInject(R.id.tv_login_project)
    private TextView tv_login_project;

    private String mobileNum;//手机号码
    private String vcerificationCode;//验证码
    private Timer timer = new Timer();
    int recLen = Constants.CODETIME;
    private MyTimerTask task;

    private AlertView mAlertView;
    private ProgressDialog progressDialog;

    private ProjectInfo projectInfo;
    private String validKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        projectInfo= FileManagement.getProjectInfo();
        if(projectInfo!=null){
            tv_login_project.setText(projectInfo.getProjectName());
            Constants.BASEHOST=projectInfo.getProjectHost();
            Constants.HOST= Constants.BASEHOST+"api/";
        }
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(EventBusMessage message){
        if("projectChange".equals(message.getMessage())){
            projectInfo= FileManagement.getProjectInfo();
            tv_login_project.setText(projectInfo.getProjectName());
            Constants.BASEHOST=projectInfo.getProjectHost();
            Constants.HOST= Constants.BASEHOST+"api/";
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        init();
    }

    private void init() {
//        AccessToken accessToken=FileManagement.getAccessToken();
//        if(accessToken!=null){
//            if(TokenUtils.tokenValid()){
//                TokenUtils.initHikCloudToken(new TokenUtils.OnInitHikCloudToken(){
//                    @Override
//                    public void success(String token) {
//                        if (Tools.isUserLogin()) {
//                            openActivity(MainActivity.class);
//                            LoginActivity.this.finish();
//                        }
//                    }
//
//                    @Override
//                    public void fail(Throwable ex) {
//                        showShortToast(ex.getMessage());
//                    }
//                });
//            }
//        }else{
//            TokenUtils.initHikCloudToken(new TokenUtils.OnInitHikCloudToken(){
//                @Override
//                public void success(String token) {
//                    if (Tools.isUserLogin()) {
//                        openActivity(MainActivity.class);
//                        LoginActivity.this.finish();
//                    }
//                }
//
//                @Override
//                public void fail(Throwable ex) {
//                    showShortToast(ex.getMessage());
//                }
//            });
//        }
        //检查版本更新

        btnLogin.setOnClickListener(this);
        tvGetCode.setOnClickListener(this);
    }

    @Event({R.id.btn_ip_setting, R.id.ll_umeng_login_weixin, R.id.ll_umeng_login_qq, R.id.tv_login_app_agreement, R.id.tv_login_project, R.id.tv_register})
    private void onClickEvent(View v){
        switch (v.getId()){
            case R.id.btn_ip_setting:
                openActivity(IpSettingActivity.class);
                break;
            case R.id.ll_umeng_login_weixin:
                if(projectInfo==null){
                    Toast.makeText(LoginActivity.this,"请选择小区",Toast.LENGTH_SHORT).show();
                    return;
                }
                authorization(SHARE_MEDIA.WEIXIN);
                break;
            case R.id.ll_umeng_login_qq:
                if(projectInfo==null){
                    Toast.makeText(LoginActivity.this,"请选择小区",Toast.LENGTH_SHORT).show();
                    return;
                }
                authorization(SHARE_MEDIA.QQ);

                break;
            case R.id.tv_login_app_agreement:
                Bundle bundle=new Bundle();
                bundle.putString("title","App使用协议");
                bundle.putString("url","http://dev.chanfine.com:9082/privacy/135310.html");
                openActivity(NewsInfoActivity.class,bundle);
                break;
            case R.id.tv_login_project:
                openActivity(ProjectListActivity.class);
                break;
            case R.id.tv_register:
                openActivity(RegisterActivity.class);
                break;
        }
    }

    private void authorization(SHARE_MEDIA share_media){
        startProgressDialog("授权中...");
        UMShareAPI.get(LoginActivity.this).getPlatformInfo(LoginActivity.this, share_media, new UMAuthListener(){
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
                    FileManagement.setLoginType("wx");
                    WeiXinLoginEntity weiXinLoginEntity=gson.fromJson(thirdLogin, WeiXinLoginEntity.class);
                    FileManagement.setWXLogin(weiXinLoginEntity);
                    queryThirdBind(weiXinLoginEntity.getUid(),"0");
                    Log.e("LoginActivity",weiXinLoginEntity.toString());
                }else if(SHARE_MEDIA.QQ==share_media){
                    FileManagement.setLoginType("qq");
                    QQLoginEntity qqLoginEntity=gson.fromJson(thirdLogin, QQLoginEntity.class);
                    FileManagement.setQQLogin(qqLoginEntity);
                    queryThirdBind(qqLoginEntity.getUid(),"1");
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

    private void queryThirdBind(String uid,String type){
        final Map<String,String> requestMap=new HashMap<>();
        requestMap.put("accountId",uid);
        requestMap.put("type",type);
        XUtils.Get(Constants.HOST+"/queryPhoneNo.action",requestMap,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                Log.e("result",result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if(jsonObject.getString("resultCode").equals("0")){
                        JSONObject data=jsonObject.getJSONObject("data");
                        FileManagement.saveTokenInfo(data.getString("token"));
                        Gson gson=new Gson();
                        LoginUserEntity loginUserEntity=gson.fromJson(data.getJSONObject("userInfo").toString(), LoginUserEntity.class);
                        FileManagement.setBaseUser(loginUserEntity);
                        Type room_type = new TypeToken<ArrayList<RoomInfoEntity>>() {}.getType();
                        ArrayList<RoomInfoEntity> roomInfoEntities=gson.fromJson(data.getJSONArray("roominfo").toString(),room_type);
                        FileManagement.saveRoomInfo(roomInfoEntities);
                        Type device_type = new TypeToken<ArrayList<DeviceInfoEntity>>() {}.getType();
                        ArrayList<DeviceInfoEntity> deviceInfoEntities=gson.fromJson(data.getJSONArray("deviceInfo").toString(),device_type);
                        FileManagement.setDeviceInfo(deviceInfoEntities);
                        Type third_type = new TypeToken<ArrayList<ThirdInfoEntity>>() {}.getType();
                        ArrayList<ThirdInfoEntity> thirdInfoEntities=gson.fromJson(data.getJSONArray("thirdInfo").toString(),third_type);
                        FileManagement.setThirdInfo(thirdInfoEntities);
                    }else{
                        FileManagement.saveTokenInfo("third");
                        showShortToast(jsonObject.getString("msg"));
                    }

                openActivity(MainActivity.class);
                LoginActivity.this.finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                showShortToast(ex.getMessage());
            }

            @Override
            public void onFinished() {
                super.onFinished();
                stopProgressDialog();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_get_code:
                mobileNum = etdUserMobileNumber.getText().toString();
                if (mobileNum.trim().equals("")) {
                    Tools.showPrompt("请输入手机号码");
                } else {
                    //调用绑定手机短信接口
                    sendSMS();
                }
                break;
            case R.id.btn_login:
//                getUserInfoHik();

                /** zxl 临时登录 2019-4-8*/
//                openActivity(MainActivity.class);
//                LoginActivity.this.finish();


                mobileNum = etdUserMobileNumber.getText().toString();
                vcerificationCode = etUserMobileCode.getText().toString();
                if (Tools.isEmpty(mobileNum)) {
                    mAlertView = new AlertView("温馨提示", "手机号码不能为空",
                            null, new String[]{"知道了"}, null, LoginActivity.this, AlertView.Style.Alert, null).setCancelable(true);
                    mAlertView.show();
                } else if (Tools.isEmpty(vcerificationCode)) {
                    mAlertView = new AlertView("温馨提示", "验证码不能为空",
                            null, new String[]{"知道了"}, null, this, AlertView.Style.Alert, null).setCancelable(true);
                    mAlertView.show();
//                } else if (!(Tools.isMobile(mobileNum))) {
//                    Tools.showPrompt("请输入正确的手机号");
                } else {
//                    startProgressDialog("");
                    login();
                }
                break;
            default:
                break;
        }
    }

    /**
     * @author TanYong
     * create at 2017/4/21 13:36
     * TODO：获取手机验证码
     */
    private void sendSMS() {
        RequestParams params=new RequestParams(BASE_URL+SMS+"sms-internal/codes");

        params.addBodyParameter("phone",mobileNum);
        x.http().post(params,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtils.d(result);
                BaseEntity<SmsKeyEntity> baseEntity= JsonParse.parse(result,SmsKeyEntity.class);
                if(baseEntity.isSuccess()){
                    validKey = baseEntity.getResult().getKey();
                    etUserMobileCode.setText("");
                    etUserMobileCode.setFocusable(true);
                    etUserMobileCode.setFocusableInTouchMode(true);
                    etUserMobileCode.requestFocus();//获取焦点 光标出现
                    if (timer != null) {
                        timer = new Timer();
                        if (task != null) {
                            task.cancel();  //将原任务从队列中移除
                        }
                        task = new MyTimerTask();  // 新建一个任务
                        timer.schedule(task, 1000, 1000);
                    }
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

    class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            runOnUiThread(new Runnable() {      // UI thread
                @Override
                public void run() {
                    tvGetCode.setOnClickListener(null);
                    tvGetCode.setBackgroundResource(R.mipmap.login_btn_yzm_hui);
                    tvGetCode.setText("");
                    tvGetCode.setText(recLen + "s");
                    recLen--;
                    if (recLen < 0) {
                        task.cancel();
                        tvGetCode.setText("获取验证码");
                        tvGetCode.setBackgroundResource(R.mipmap.login_btn_yzm);
                        tvGetCode.setOnClickListener(LoginActivity.this);
                        recLen = Constants.CODETIME;
                    }
                }
            });
        }
    }

    /**
     * @author TanYong
     * create at 2017/4/21 15:57
     * TODO：登录
     */
    private void login() {
        startProgressDialog("");
        RequestParams params=new RequestParams(BASE_URL+AUTH+"oauth/user/household/login");
        params.addHeader("client_id","mobile");
        params.addHeader("client_secret","mobile");

        params.addBodyParameter("mobile",mobileNum);
        params.addBodyParameter("key",validKey);
        params.addBodyParameter("validCode",vcerificationCode);
        x.http().post(params,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtils.d(result);
                Gson gson = new Gson();
                TokenEntity token=gson.fromJson(result,TokenEntity.class);
                token.setInit_time(new Date().getTime()/1000);
                FileManagement.setTokenEntity(token);
                FileManagement.setPhone(mobileNum);
                getUserInfo();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                showToast(ex.getMessage());
                stopProgressDialog();
            }
        });

    }

    //获取用户信息
    private void getUserInfo(){

        RequestParams params=new RequestParams(BASE_URL+BASIC+"basic/householdInfo/phone");
        params.addParameter("phoneNumber",mobileNum);
        x.http().get(params,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtils.d(result);
                BaseEntity<UserInfoEntity> baseEntity= JsonParse.parse(result,UserInfoEntity.class);
                if(baseEntity.isSuccess()){
                    FileManagement.setUserInfo(baseEntity.getResult());//缓存用户信息
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
