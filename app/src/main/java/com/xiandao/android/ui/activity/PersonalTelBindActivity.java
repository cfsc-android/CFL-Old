package com.xiandao.android.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.xiandao.android.R;
import com.xiandao.android.entity.LoginResultDataEntity;
import com.xiandao.android.entity.LoginUserEntity;
import com.xiandao.android.entity.OverallSituationEntity;
import com.xiandao.android.entity.QQLoginEntity;
import com.xiandao.android.entity.RoomInfoEntity;
import com.xiandao.android.entity.WeiXinLoginEntity;
import com.xiandao.android.entity.eventbus.EventBusMessage;
import com.xiandao.android.http.MyCallBack;
import com.xiandao.android.http.XUtils;
import com.xiandao.android.net.ApiHttpResult;
import com.xiandao.android.net.HttpUtils;
import com.xiandao.android.net.NetUtil;
import com.xiandao.android.ui.BaseActivity;
import com.xiandao.android.utils.Constants;
import com.xiandao.android.utils.FileManagement;
import com.xiandao.android.utils.Tools;

import org.greenrobot.eventbus.EventBus;
import org.xutils.event.annotation.ContentView;
import org.xutils.event.annotation.Event;
import org.xutils.event.annotation.ViewInject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

@ContentView(R.layout.activity_personal_tel_bind)
public class PersonalTelBindActivity extends BaseActivity {
    @ViewInject(R.id.tv_title_name)
    private TextView tv_title_name;
    @ViewInject(R.id.tv_tel_bind_msg)
    private TextView tv_tel_bind_msg;
    @ViewInject(R.id.tv_tel_bind_get_ver)
    private TextView tv_tel_bind_get_ver;
    @ViewInject(R.id.et_tel_bind_tel)
    private EditText et_tel_bind_tel;
    @ViewInject(R.id.et_tel_bind_ver)
    private EditText et_tel_bind_ver;
    @ViewInject(R.id.btn_tel_bind_bind)
    private Button btn_tel_bind_bind;

    private boolean verFlag,bindFlag;
    private int verNum=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tv_title_name.setText("绑定业主");
        et_tel_bind_tel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()==11){
                    getUserInfo();
                }else{
                    setVerFlag(false);
                    tv_tel_bind_msg.setText("");
                }
                if(et_tel_bind_ver.getText().length()>3&&s.length()==11){
                    bindFlag=true;
                    btn_tel_bind_bind.setTextColor(getResources().getColor(R.color.white));
                }else{
                    bindFlag=false;
                    btn_tel_bind_bind.setTextColor(getResources().getColor(R.color.find_text_color));
                }
            }
        });
        et_tel_bind_ver.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>3&&et_tel_bind_tel.getText().length()==11){
                    bindFlag=true;
                    btn_tel_bind_bind.setTextColor(getResources().getColor(R.color.white));
                }else{
                    bindFlag=false;
                    btn_tel_bind_bind.setTextColor(getResources().getColor(R.color.find_text_color));
                }
            }
        });
    }

    private void getUserInfo(){
        ApiHttpResult.platCheckOwner(this, new String[]{"mobileNumber"},
                new Object[]{et_tel_bind_tel.getText().toString()}, new HttpUtils.DataCallBack<Object>() {
                    @Override
                    public void callBack(Object o) {
                        if (o != null) {
                            LoginResultDataEntity resultData = (LoginResultDataEntity) o;
                            if (resultData != null) {
                                OverallSituationEntity osEntity = resultData.getOsEntity();
                                if (osEntity.getResultCode().equals("0")) {
                                    List<RoomInfoEntity> roominfo= resultData.getRoominfo();
                                    if(roominfo!=null&&roominfo.size()>0){
                                        tv_tel_bind_msg.setText(roominfo.get(0).getAddress());
                                        setVerFlag(true);
                                    }else{
                                        tv_tel_bind_msg.setText("该手机号未注册，请联系物业");
                                        setVerFlag(false);
                                    }

                                }else{
                                    tv_tel_bind_msg.setText("该手机号未注册，请联系物业");
                                    setVerFlag(false);
                                }
                            } else {
                                tv_tel_bind_msg.setText("该手机号未注册，请联系物业");
                                setVerFlag(false);
                            }
                        } else {
                            NetUtil.toNetworkSetting(PersonalTelBindActivity.this);
                            setVerFlag(false);
                        }
                    }
                });
    }

    private void setVerFlag(boolean verFlag){
        this.verFlag=verFlag;
        if(verFlag){
            if(verNum==0){
                tv_tel_bind_get_ver.setText("获取验证码");
                tv_tel_bind_get_ver.setTextColor(getResources().getColor(R.color.payment_btn));
            }
        }else{
            tv_tel_bind_get_ver.setTextColor(getResources().getColor(R.color.find_text_color));
            if(verNum==0){
                tv_tel_bind_get_ver.setText("获取验证码");
            }
        }
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
                tv_tel_bind_get_ver.setText(verNum+"'s后重新获取");
                verNum--;
                if(verNum>0){
                    handler.sendEmptyMessageDelayed(1,1000);
                }else{
                    handler.removeMessages(1);
                    if(et_tel_bind_tel.getText().length()==11){
                        setVerFlag(true);
                    }
                }
            }
        }
    };

    @Event({R.id.iv_title_left,R.id.tv_tel_bind_get_ver,R.id.btn_tel_bind_bind})
    private void onEventClick(View v){
        switch (v.getId()){
            case R.id.iv_title_left:
                finish();
                break;
            case R.id.tv_tel_bind_get_ver:
                if(verFlag){
                    sendSMS();
                }
                break;
            case R.id.btn_tel_bind_bind:
                if(bindFlag){
                    bindTel();
                }
                break;
        }
    }

    private void bindTel(){
        startProgressDialog("");
        Map<String,String> requestMap=new HashMap<>();
        requestMap.put("phoneNo",et_tel_bind_tel.getText().toString());
        requestMap.put("vcerificationCode",et_tel_bind_ver.getText().toString());
        if(FileManagement.getLoginType().equals("wx")){
            WeiXinLoginEntity loginEntity=FileManagement.getWXLogin();
            requestMap.put("nickname",loginEntity.getName());
            requestMap.put("accountId",loginEntity.getUid());
            requestMap.put("sex",loginEntity.getGender().endsWith("男")?"0":"1");
            requestMap.put("type","0");
            requestMap.put("faceImg",loginEntity.getIconurl());
        }else{
            QQLoginEntity loginEntity=FileManagement.getQQLogin();
            requestMap.put("nickname",loginEntity.getName());
            requestMap.put("accountId",loginEntity.getUid());
            requestMap.put("sex",loginEntity.getGender().endsWith("男")?"0":"1");
            requestMap.put("type","1");
            requestMap.put("faceImg",loginEntity.getIconurl());
        }
        XUtils.Get(Constants.HOST+"/linkPhoneNo.action",requestMap,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                Log.e("result",result);
                showShortToast("绑定成功");
                updateOwner();
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

    /**
     * 更新用户信息
     */
    private void updateOwner(){
        ApiHttpResult.platCheckOwner(this, new String[]{"mobileNumber"},
                new Object[]{et_tel_bind_tel.getText().toString()}, new HttpUtils.DataCallBack<Object>() {
                    @Override
                    public void callBack(Object o) {
                        if (o != null) {
                            LoginResultDataEntity resultData = (LoginResultDataEntity) o;
                            if (resultData != null) {
                                OverallSituationEntity osEntity = resultData.getOsEntity();
                                if (osEntity.getResultCode().equals("0")) {
                                    LoginUserEntity userInfo = resultData.getUserInfo();
                                    if (userInfo != null) {
                                        FileManagement.saveTokenInfo(resultData.getToken());
                                        FileManagement.setBaseUser(userInfo);
                                        FileManagement.saveRoomInfo(resultData.getRoominfo());
                                        FileManagement.setDeviceInfo(resultData.getDeviceInfo());
                                        FileManagement.setThirdInfo(resultData.getThirdInfo());
                                    }
                                }
                            }
                        } else {
                            NetUtil.toNetworkSetting(PersonalTelBindActivity.this);
                        }
                        EventBus.getDefault().post(new EventBusMessage<>("bind"));
                        finish();
                    }
                });
    }

    /**
     * @author TanYong
     * create at 2017/4/21 13:36
     * TODO：获取手机验证码
     */
    private void sendSMS() {
        startProgressDialog("");
        ApiHttpResult.sendSMS(PersonalTelBindActivity.this, new String[]{"chid", "mobile"},
                new Object[]{"", et_tel_bind_tel.getText().toString()}, new HttpUtils.DataCallBack<Object>() {
                    @Override
                    public void callBack(Object o) {
                        stopProgressDialog();
                        if (o != null) {
                            OverallSituationEntity entity = (OverallSituationEntity) o;
                            if (entity.getResultCode().equals("0")) {
                                handler.sendEmptyMessage(0);
                            } else {
                                Tools.showPrompt(Tools.getStringValue(entity.getMsg()));
                            }
                        } else {
                            NetUtil.toNetworkSetting(PersonalTelBindActivity.this);
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeMessages(1);
    }
}
