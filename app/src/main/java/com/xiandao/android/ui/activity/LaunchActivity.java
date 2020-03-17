package com.xiandao.android.ui.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.pgyersdk.update.DownloadFileListener;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;
import com.pgyersdk.update.javabean.AppBean;
import com.xiandao.android.R;
import com.xiandao.android.entity.smart.BaseEntity;
import com.xiandao.android.entity.LoginResultDataEntity;
import com.xiandao.android.entity.LoginUserEntity;
import com.xiandao.android.entity.smart.OrderStatusEntity;
import com.xiandao.android.entity.smart.OrderTypeListEntity;
import com.xiandao.android.entity.OverallSituationEntity;
import com.xiandao.android.entity.ProjectInfo;
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
import com.xiandao.android.utils.Utils;

import org.xutils.LogUtils;
import org.xutils.event.annotation.ContentView;
import org.xutils.event.annotation.ViewInject;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.xiandao.android.base.Config.BASE_URL;
import static com.xiandao.android.base.Config.BASIC;
import static com.xiandao.android.base.Config.WORKORDER;

@ContentView(R.layout.activity_launch)
public class LaunchActivity extends BaseActivity {
    @ViewInject(R.id.tv_loading_version)
    private TextView tv_loading_version;

    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tv_loading_version.setText("v-"+ Utils.getCurrentVersion(this));
        checkVersion();
//        checkLogin();
    }
    private void checkVersion(){
        new PgyUpdateManager.Builder()
                .setForced(false)                //设置是否强制提示更新,非自定义回调更新接口此方法有用
                .setUserCanRetry(false)         //失败后是否提示重新下载，非自定义下载 apk 回调此方法有用
                .setDeleteHistroyApk(false)     // 检查更新前是否删除本地历史 Apk， 默认为true
                .setUpdateManagerListener(new UpdateManagerListener() {
                    @Override
                    public void onNoUpdateAvailable() {
                        //没有更新是回调此方法
                        Log.d("pgyer", "there is no new version");
                        checkLogin();
                    }

                    @Override
                    public void onUpdateAvailable(final AppBean appBean) {
                        //有更新回调此方法
                        Log.d("pgyer", "there is new version can update"
                                + "new versionCode is " + appBean.getVersionCode());
                        //调用以下方法，DownloadFileListener 才有效；
                        //如果完全使用自己的下载方法，不需要设置DownloadFileListener
//
                        if("99".equals(appBean.getVersionCode())){
                            new AlertDialog.Builder(LaunchActivity.this)
                                    .setTitle("强制更新")
                                    .setMessage("发现新版本"+appBean.getVersionName()+"\n"+appBean.getReleaseNote())
                                    .setCancelable(false)
                                    .setNegativeButton(
                                            "确认更新",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    PgyUpdateManager.downLoadApk(appBean.getDownloadURL());
                                                }
                                            }).show();
                        }else{
                            new AlertDialog.Builder(LaunchActivity.this)
                                    .setTitle("更新")
                                    .setMessage("发现新版本"+appBean.getVersionName()+"\n"+appBean.getReleaseNote())
                                    .setCancelable(false)
                                    .setNegativeButton("确认更新",new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            PgyUpdateManager.downLoadApk(appBean.getDownloadURL());
                                        }
                                    }).
                                    setNeutralButton("下次再说", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            checkLogin();
                                        }
                                    }).show();
                        }

                    }

                    @Override
                    public void checkUpdateFailed(Exception e) {
                        //更新检测失败回调
                        //更新拒绝（应用被下架，过期，不在安装有效期，下载次数用尽）以及无网络情况会调用此接口
                        Log.e("pgyer", "check update failed ", e);
                        checkLogin();
                    }
                })
                .setDownloadFileListener(new DownloadFileListener() {
                    @Override
                    public void downloadFailed() {
                        //下载失败
                        Log.e("pgyer", "download apk failed");
                    }

                    @Override
                    public void downloadSuccessful(Uri uri) {
                        Log.e("pgyer", "download apk failed");
                        // 使用蒲公英提供的安装方法提示用户 安装apk
                        PgyUpdateManager.installApk(uri);
                    }

                    @Override
                    public void onProgressUpdate(Integer... integers) {
                        Log.e("pgyer", "update download apk progress" + integers[0]);
                        initProgressDialog(integers[0]);
                    }
                })
                .register();
        if(Tools.isEmpty(FileManagement.getNoticeFlag())){
            FileManagement.setNoticeFlag("1");
        }
    }

    /**
     * 下载最新版进度
     * @param progress
     */
    private void initProgressDialog(int progress){
        if(progressDialog==null){
            progressDialog=new ProgressDialog(this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setCancelable(false);// 设置是否可以通过点击Back键取消
            progressDialog.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
            progressDialog.setTitle("下载最新版");
            progressDialog.setMax(100);
            progressDialog.setProgress(0);
            progressDialog.show();
        }else{
            progressDialog.setProgress(progress);
            if(progress==100){
                progressDialog.dismiss();
            }
        }
    }

    private void checkLogin(){
        TokenEntity token= FileManagement.getTokenEntity();
        if(token!=null){
            long time=new Date().getTime()/1000 - token.getInit_time();
            if(token.getExpires_in()-time>3){
                initData();
            }else{
                openActivity(LoginActivity.class);
                finish();
            }
        }else{
            openActivity(LoginActivity.class);
            finish();
        }
    }

    private void initData(){
        initOrderType();
        initOrderStatus();
        initComplainType();
        initComplainStatus();
        getUserInfo();
    }

    //初始化工单类型
    private void initOrderType(){
        Map<String,String> requestMap=new HashMap<>();
        requestMap.put("pageNo","1");
        requestMap.put("pageSize","100");
        XUtils.Get(BASE_URL+WORKORDER+"work/orderType/pageByCondition",requestMap,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtils.d(result);
                BaseEntity<OrderTypeListEntity> baseEntity= JsonParse.parse(result, OrderTypeListEntity.class);
                if(baseEntity.isSuccess()){
                    FileManagement.setOrderType(baseEntity.getResult().getData());
                    checkInitStatus(1);
                }else{
                    showToast(baseEntity.getMessage());
                    checkInitStatus(0);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                showToast(ex.getMessage());
                checkInitStatus(0);
            }

        });

    }
    //初始化工单状态
    private void initOrderStatus(){
        XUtils.Get(BASE_URL+WORKORDER+"work/orderStatus/selectWorkorderStatus",null,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtils.d(result);
                BaseEntity baseEntity= JsonParse.parse(result);
                if(baseEntity.isSuccess()){
                    Type type = new TypeToken<List<OrderStatusEntity>>() {}.getType();
                    List<OrderStatusEntity> list= (List<OrderStatusEntity>) JsonParse.parseList(result,type);
                    FileManagement.setOrderStatus(list);
                    checkInitStatus(1);
                }else{
                    showToast(baseEntity.getMessage());
                    checkInitStatus(0);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                showToast(ex.getMessage());
                checkInitStatus(0);
            }

        });

    }
    //初始化投诉类型
    private void initComplainType(){
        Map<String,String> requestMap=new HashMap<>();
        requestMap.put("pageNo","1");
        requestMap.put("pageSize","100");
        XUtils.Get(BASE_URL+WORKORDER+"work/complaintType/pageByCondition",requestMap,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtils.d(result);
                BaseEntity<OrderTypeListEntity> baseEntity= JsonParse.parse(result, OrderTypeListEntity.class);
                if(baseEntity.isSuccess()){
                    FileManagement.setComplainType(baseEntity.getResult().getData());
                    checkInitStatus(1);
                }else{
                    showToast(baseEntity.getMessage());
                    checkInitStatus(0);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                showToast(ex.getMessage());
                checkInitStatus(0);
            }

        });
    }
    //初始化投诉状态
    private void initComplainStatus(){
        XUtils.Get(BASE_URL+WORKORDER+"work/complaintStatus/complaintStatusList",null,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtils.d(result);
                BaseEntity baseEntity= JsonParse.parse(result);
                if(baseEntity.isSuccess()){
                    Type type = new TypeToken<List<OrderStatusEntity>>() {}.getType();
                    List<OrderStatusEntity> list= (List<OrderStatusEntity>) JsonParse.parseList(result,type);
                    FileManagement.setComplainStatus(list);
                    checkInitStatus(1);
                }else{
                    showToast(baseEntity.getMessage());
                    checkInitStatus(0);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                showToast(ex.getMessage());
                checkInitStatus(0);
            }

        });
    }
    //获取用户信息
    private void getUserInfo(){

        RequestParams params=new RequestParams(BASE_URL+BASIC+"basic/householdInfo/phone");
        params.addParameter("phoneNumber",FileManagement.getPhone());
        x.http().get(params,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtils.d(result);
                BaseEntity<UserInfoEntity> baseEntity= JsonParse.parse(result,UserInfoEntity.class);
                if(baseEntity.isSuccess()){
                    FileManagement.setUserInfo(baseEntity.getResult());//缓存用户信息
                    checkInitStatus(1);
                }else{
                    showToast(baseEntity.getMessage());
                    checkInitStatus(0);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                showToast(ex.getMessage());
                checkInitStatus(0);
            }


        });
    }


    private List<Integer> initStatus=new ArrayList<>();

    private void checkInitStatus(int status){
        initStatus.add(status);
        if(initStatus.indexOf(0)!=-1){
            openActivity(LoginActivity.class);
            finish();
        }else{
            if(initStatus.size()==5){
                openActivity(MainActivity.class);
                finish();
            }
        }
    }

    /**
     * 更新用户信息
     */
    private void updateOwner(){
        ApiHttpResult.platCheckOwner(this, new String[]{"mobileNumber"},
                new Object[]{FileManagement.getLoginUserEntity().getMobileNumber()}, new HttpUtils.DataCallBack<Object>() {
                    @Override
                    public void callBack(Object o) {
                        if (o != null) {
                            LoginResultDataEntity resultData = (LoginResultDataEntity) o;
                            if (resultData != null) {
                                OverallSituationEntity osEntity = resultData.getOsEntity();
                                if (osEntity.getResultCode().equals("0")) {
                                    LoginUserEntity userInfo = resultData.getUserInfo();
                                    if (userInfo != null) {
                                        FileManagement.setBaseUser(userInfo);
                                        FileManagement.saveRoomInfo(resultData.getRoominfo());
                                        FileManagement.setDeviceInfo(resultData.getDeviceInfo());
                                        FileManagement.setThirdInfo(resultData.getThirdInfo());
                                        openActivity(MainActivity.class);
                                        finish();
                                    }else{
                                        openActivity(LoginActivity.class);
                                        finish();
                                    }
                                }else{
                                    openActivity(LoginActivity.class);
                                    finish();
                                }
                            } else {
                                openActivity(LoginActivity.class);
                                finish();
                            }
                        } else {
                            NetUtil.toNetworkSetting(LaunchActivity.this);
                            openActivity(LoginActivity.class);
                            finish();
                        }
                    }
                });
    }
}
