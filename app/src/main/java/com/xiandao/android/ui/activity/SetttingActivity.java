package com.xiandao.android.ui.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pgyersdk.feedback.PgyerFeedbackManager;
import com.pgyersdk.update.DownloadFileListener;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;
import com.pgyersdk.update.javabean.AppBean;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.xiandao.android.AndroidApplication;
import com.xiandao.android.R;
import com.xiandao.android.entity.LoginUserEntity;
import com.xiandao.android.entity.QQLoginEntity;
import com.xiandao.android.entity.WeiXinLoginEntity;
import com.xiandao.android.entity.smart.TokenEntity;
import com.xiandao.android.ui.BaseActivity;
import com.xiandao.android.utils.Constants;
import com.xiandao.android.utils.FileManagement;
import com.xiandao.android.utils.Tools;
import com.xiandao.android.utils.Utils;
import com.xiandao.android.view.NoUnderlineSpan;
import com.xiandao.android.view.alertview.AlertView;
import com.xiandao.android.view.alertview.OnItemClickListener;

import org.xutils.event.annotation.ContentView;
import org.xutils.event.annotation.Event;
import org.xutils.event.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

@ContentView(R.layout.activity_settting)
public class SetttingActivity extends BaseActivity {
    @ViewInject(R.id.tv_title_name)
    private TextView tv_title_name;
    @ViewInject(R.id.tv_setting_version)
    private TextView tv_setting_version;
    @ViewInject(R.id.ll_setting_logout_layer)
    private LinearLayout ll_setting_logout_layer;
    @ViewInject(R.id.tv_setting_contact)
    private TextView tv_setting_contact;

    private ProgressDialog progressDialog;
    private AppBean _appBean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tv_title_name.setText("设置");
        tv_setting_version.setText(Utils.getCurrentVersion(this));
        initCheckVersion();
        NoUnderlineSpan mNoUnderlineSpan = new NoUnderlineSpan();
        if(tv_setting_contact.getText() instanceof Spannable){
            Spannable s =(Spannable)tv_setting_contact.getText();
            s.setSpan(mNoUnderlineSpan,0,s.length(),Spanned.SPAN_MARK_MARK);
        }
    }

    @Event({R.id.iv_title_left,R.id.ll_setting_feedback,R.id.ll_setting_version,R.id.ll_setting_logout,
    R.id.ll_setting_logout_layer,R.id.tv_setting_logout_cancel,R.id.tv_setting_logout_quit,R.id.tv_setting_logout_change,
    R.id.ll_setting_notice,R.id.ll_setting_cache,R.id.ll_setting_about})
    private void onClickEvent(View v){
        switch (v.getId()){
            case R.id.iv_title_left:
                finish();
                break;
            case R.id.ll_setting_feedback:
                feedback();
                break;
            case R.id.ll_setting_version:
                checkVersion();
                break;
            case R.id.ll_setting_logout:
                ll_setting_logout_layer.setVisibility(VISIBLE);
                ll_setting_logout_layer.setAnimation(alphaAnimation());
                break;
            case R.id.ll_setting_logout_layer:
                ll_setting_logout_layer.setVisibility(GONE);
                break;
            case R.id.tv_setting_logout_cancel:
                ll_setting_logout_layer.setVisibility(GONE);
                break;
            case R.id.tv_setting_logout_quit:
                exit();
                ll_setting_logout_layer.setVisibility(GONE);
                break;
            case R.id.tv_setting_logout_change:
                changeAccount();
                ll_setting_logout_layer.setVisibility(GONE);
                break;
            case R.id.ll_setting_notice:
                Bundle bundle=new Bundle();
                bundle.putString("type","0");
                bundle.putString("title","消息通知设置");
                openActivity(SettingDetailActivity.class,bundle);
                break;
            case R.id.ll_setting_cache:
                Bundle _bundle=new Bundle();
                _bundle.putString("type","1");
                _bundle.putString("title","缓存管理");
                openActivity(SettingDetailActivity.class,_bundle);
                break;
            case R.id.ll_setting_about:
                openActivity(SettingAboutActivity.class);
//                Bundle a_bundle=new Bundle();
//                a_bundle.putString("title","关于长房里");
//                a_bundle.putString("url",Constants.BASEHOST+"upload/notice_html/html/20190509/194402.html");
//                openActivity(NewsInfoActivity.class,a_bundle);
                break;
        }
    }

    /**
     * 退出
     */
    private void exit() {
        new AlertView("温馨提示", "确定关闭应用？",
                "取消", new String[]{"确认"}, null, this, AlertView.Style.Alert, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                if (position == 0) {
                    AndroidApplication.getInstance().exitApp();
                }
            }
        }).setCancelable(false).show();
    }

    /**
     * 切换账号
     */
    private void changeAccount() {
        new AlertView("温馨提示", "确定退出登录？",
                "取消", new String[]{"确认"}, null, this, AlertView.Style.Alert, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                if (position == 0) {
                    if(FileManagement.getTokenInfo().equals("third")){
                        if(FileManagement.getLoginType().equals("wx")){
                            thirdUnBind(SHARE_MEDIA.WEIXIN);
                        }else{
                            thirdUnBind(SHARE_MEDIA.QQ);
                        }
                    }else{
                        loginOut();
                    }

                }
            }
        }).setCancelable(false).show();

    }

    private void thirdUnBind(SHARE_MEDIA shareMedia){
        UMShareAPI.get(SetttingActivity.this).deleteOauth(SetttingActivity.this, shareMedia, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
                Log.d("SettingActivity", share_media.toString()+" onStart 授权开始");
            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                Log.d("SettingActivity", share_media.toString()+i+" onComplete 授权完成");
                loginOut();
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                Log.d("SettingActivity", "onError 授权错误");
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
                Log.d("SettingActivity", share_media.toString()+" onCancel 授权取消");
            }
        });
    }


    private void loginOut(){
//        FileManagement.setLoginType("");
//        FileManagement.setWXLogin(new WeiXinLoginEntity());
//        FileManagement.setQQLogin(new QQLoginEntity());
//        FileManagement.setBaseUser(new LoginUserEntity());
//        FileManagement.saveTokenInfo("");
//        FileManagement.setHikToken("");
//        FileManagement.saveJpushAlias("");
//        FileManagement.saveJpushTags("");
//        JPushInterface.setAliasAndTags(getApplicationContext(), "", null, null);
        TokenEntity tokenEntity=FileManagement.getTokenEntity();
        tokenEntity.setExpires_in(0);
        FileManagement.setTokenEntity(tokenEntity);
        openActivity(LoginActivity.class);
        AndroidApplication.getInstance().exitApp();
    }

    /**
     * 意见反馈
     */
    private void feedback(){
        new PgyerFeedbackManager.PgyerFeedbackBuilder()
                .setShakeInvoke(false)       //fasle 则不触发摇一摇，最后需要调用 invoke 方法
                // true 设置需要调用 register 方法使摇一摇生效
                .setDisplayType(PgyerFeedbackManager.TYPE.DIALOG_TYPE)   //设置以Dialog 的方式打开
                .setColorDialogTitle("#FFFFFF")    //设置Dialog 标题的字体颜色，默认为颜色为#ffffff
                .setColorTitleBg("#2C3447")        //设置Dialog 标题栏的背景色，默认为颜色为#2E2D2D
                .setBarBackgroundColor("#FF0000")      // 设置顶部按钮和底部背景色，默认颜色为 #2E2D2D
                .setBarButtonPressedColor("#FF0000")        //设置顶部按钮和底部按钮按下时的反馈色 默认颜色为 #383737
                .setColorPickerBackgroundColor("#FF0000")   //设置颜色选择器的背景色,默认颜色为 #272828
                .setMoreParam("用户昵称",FileManagement.getLoginUserEntity().getNickName()) //自定义的反馈数据
                .setMoreParam("用户手机",FileManagement.getLoginUserEntity().getMobileNumber()) //自定义的反馈数据
                .builder()
                .invoke();                  //激活直接显示的方式
    }

    /**
     * 获取新版本
     */
    private void initCheckVersion(){
        new PgyUpdateManager.Builder()
                .setForced(false)                //设置是否强制提示更新,非自定义回调更新接口此方法有用
                .setUserCanRetry(false)         //失败后是否提示重新下载，非自定义下载 apk 回调此方法有用
                .setDeleteHistroyApk(false)     // 检查更新前是否删除本地历史 Apk， 默认为true
                .setUpdateManagerListener(new UpdateManagerListener() {
                    @Override
                    public void onNoUpdateAvailable() {
                        //没有更新是回调此方法
                        Log.d("pgyer", "there is no new version");

                    }

                    @Override
                    public void onUpdateAvailable(final AppBean appBean) {
                        //有更新回调此方法
                        Log.d("pgyer", "there is new version can update"
                                + "new versionCode is " + appBean.getVersionCode());
                        //调用以下方法，DownloadFileListener 才有效；
                        //如果完全使用自己的下载方法，不需要设置DownloadFileListener
                        _appBean=appBean;

                    }

                    @Override
                    public void checkUpdateFailed(Exception e) {
                        //更新检测失败回调
                        //更新拒绝（应用被下架，过期，不在安装有效期，下载次数用尽）以及无网络情况会调用此接口
                        Log.e("pgyer", "check update failed ", e);
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
    }

    /**
     * 检测新版本
     */
    private void checkVersion(){
        if(_appBean!=null){
            if("99".equals(_appBean.getVersionCode())){
                new AlertDialog.Builder(SetttingActivity.this)
                        .setTitle("强制更新")
                        .setMessage("发现新版本"+_appBean.getVersionName()+"\n"+_appBean.getReleaseNote())
                        .setCancelable(false)
                        .setNegativeButton(
                                "确认更新",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        PgyUpdateManager.downLoadApk(_appBean.getDownloadURL());
                                    }
                                }).show();
            }else{
                new AlertDialog.Builder(SetttingActivity.this)
                        .setTitle("更新")
                        .setMessage("发现新版本"+_appBean.getVersionName()+"\n"+_appBean.getReleaseNote())
                        .setNegativeButton("确认更新",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                PgyUpdateManager.downLoadApk(_appBean.getDownloadURL());
                            }
                        }).
                        setNeutralButton("下次再说", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            }

        }else{
            showShortToast("没有发现可更新版本");
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

    private AlphaAnimation alphaAnimation(){
        AlphaAnimation animation=new AlphaAnimation(0,1);
        animation.setDuration(300);
        return animation;
    }
}
