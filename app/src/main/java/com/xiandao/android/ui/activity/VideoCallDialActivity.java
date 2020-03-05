package com.xiandao.android.ui.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hikvision.zhyjsdk.ZHYJCallInfo;
import com.hikvision.zhyjsdk.ZHYJCallback;
import com.hikvision.zhyjsdk.ZHYJConstants;
import com.hikvision.zhyjsdk.ZHYJHandler;
import com.hikvision.zhyjsdk.ZHYJSDK;
import com.hikvision.zhyjsdk.utils.LogUtils;
import com.xiandao.android.R;
import com.xiandao.android.entity.hikcloud.VideoCallPush;
import com.xiandao.android.http.hikhttp.HikConfig;
import com.xiandao.android.ui.BaseActivity;
import com.xiandao.android.utils.DataCleanManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.event.annotation.ContentView;
import org.xutils.event.annotation.Event;
import org.xutils.event.annotation.ViewInject;


import static android.view.View.GONE;


@ContentView(R.layout.activity_video_call_dial)
public class VideoCallDialActivity extends BaseActivity {
    @ViewInject(R.id.tv_title_name)
    private TextView tv_title_name;
    @ViewInject(R.id.ss_video_call_play)
    private SurfaceView ss_video_call_play;
    @ViewInject(R.id.ll_video_call_accept)
    private LinearLayout ll_video_call_accept;


    private MediaPlayer mediaPlayer = new MediaPlayer();
    private ZHYJSDK sdk;
    private String callStatus="";
    private VideoCallPush videoCallPush;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tv_title_name.setText("可视对讲");
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String message=bundle.getString("message");
        Log.d("message",message);
        videoCallPush= (VideoCallPush) bundle.getSerializable("videoCallPush");
        initData();
        mediaPlayer = new MediaPlayer();
        mediaPlay("video_call.wav",true);
        getCallStatus();
    }

    @SuppressLint("HandlerLeak")
    private void initData(){
        sdk = ZHYJSDK.getInstance();
        sdk.initConfig(getApplicationContext(), HikConfig.CLIENT_ID, HikConfig.CLIENT_SECRET, new ZHYJSDK.GetEZConfigCallback() {
            @Override
            public void onSuccess(String s, String s1) {
                Log.e("tees",s+"++++++++++++++++++++++++++++"+s1);
                sdk.initSDK(s, s1, new ZHYJHandler(getApplicationContext()) {

                    @Override
                    public void onGetAppkeySuccess(String s, String s1) {
                        Log.e("可视对讲","appkey:  " + s + "  token:  " + s1);
                        startPreview();
                    }

                    @Override
                    public void onStartPlaySuccess() {
                        Log.e("可视对讲","预览成功");
                    }

                    @Override
                    public void onStartPlayFail(com.videogo.errorlayer.ErrorInfo errorInfo) {
                        Log.e("可视对讲","预览失败" + errorInfo.description);
                    }

                    @Override
                    public void onStartTalkSuccess() {

                    }

                    @Override
                    public void onStartTalkFail(com.videogo.errorlayer.ErrorInfo errorInfo) {

                    }
                });
            }

            @Override
            public void onFaile(int i, Throwable throwable) {
                LogUtils.deBug(throwable.getMessage());
            }
        });
        ZHYJSDK.setDebugLogEnable(true);
    }

    private void startPreview(){
        ZHYJCallInfo.getInstance().setDeviceSN(videoCallPush.getDeviceSerial());
        ZHYJCallInfo.getInstance().setRoomNum(videoCallPush.getFloorNumber()+""+(videoCallPush.getRoomNumber()>9?videoCallPush.getRoomNumber():"0"+videoCallPush.getRoomNumber()));
        ZHYJCallInfo.getInstance().setPeriodNumber(videoCallPush.getPeriodNumber()+"");
        ZHYJCallInfo.getInstance().setBuildingNumber(videoCallPush.getBuildingNumber()+"");
        ZHYJCallInfo.getInstance().setUnitNumber(videoCallPush.getUnitNumber()+"");
        ZHYJCallInfo.getInstance().setUnitType(videoCallPush.getUnitType());
        ZHYJCallInfo.getInstance().setDevIndex(videoCallPush.getDevIndex()+"");
        ZHYJCallInfo.getInstance().setMessageType(videoCallPush.getDeviceSerial());
        sdk.startRealPlay(videoCallPush.getDeviceSerial(),ss_video_call_play.getHolder());
    }

    private void mediaPlay(String fileName,boolean loop){
        mediaPlayer.reset();
        try {
            AssetFileDescriptor fd = getAssets().openFd(fileName);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_VOICE_CALL);
            mediaPlayer.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd.getLength());
            mediaPlayer.setLooping(loop);
            mediaPlayer.prepare();
            mediaPlayer.setVolume(0.8f, 08f);
            mediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
        mediaPlayer.release();
        sdk.releasePlayer();
        sdk.destroySDK();
    }

    @Event({R.id.iv_title_left,R.id.btn_video_call_accept,R.id.btn_video_call_hangup,R.id.btn_video_call_open_door})
    private void onClickEvent(View v){
        switch (v.getId()){
            case R.id.iv_title_left:
                myFinish();
                break;
            case R.id.btn_video_call_accept:
                if (callStatus.equals(ZHYJConstants.CallStatus.RING)){
                    ll_video_call_accept.setVisibility(GONE);
                    answer();
                }else {
                    showShortToast("正在连接设备");
                }
                break;
            case R.id.btn_video_call_hangup:
                if(callStatus.equals(ZHYJConstants.CallStatus.ONCALL)){
                    hangUp();
                }else{
                    refuse();
                }
                break;
            case R.id.btn_video_call_open_door:
                openDoor();
                break;
        }
    }

    @SuppressLint("HandlerLeak")
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==0){
                handler.removeMessages(1);
                finish();
            }else if(msg.what==1){
                getCallStatus();
            }
        }
    };

    /**
     * 获取状态
     */
    private void getCallStatus(){
        sdk.getCallStatusFromNet(new ZHYJCallback.Answer() {
            @Override
            public void onSuccess(String s) {
                callStatus=s;
                if(callStatus.equals(ZHYJConstants.CallStatus.IDLE)){
                    showShortToast("对方已挂断");
                    mediaPlay("hangup.wav",false);
                    handler.sendEmptyMessageDelayed(0,500);
                }
            }

            @Override
            public void onFailure(int i) {
                Log.e("获取状态失败","错误值=>"+i);
            }
        });
        handler.sendEmptyMessageDelayed(1,1000);
    }

    /**
     * 挂断
     */
    private void hangUp(){
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
        sdk.hangUp(new ZHYJCallback.HangUp() {
            @Override
            public void onSuccess(String results) {
                Log.d("可视对讲","挂断成功");
                mediaPlay("hangup.wav",false);
                handler.sendEmptyMessageDelayed(0,500);
            }

            @Override
            public void onFailure(int errorCode) {
                Log.d("可视对讲","挂断失败");
            }
        });
    }

    /**
     * 拒接
     */
    private void refuse(){
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
        sdk.refuse(new ZHYJCallback.Refuse() {
            @Override
            public void onSuccess(String results) {
                Log.d("可视对讲","拒接成功");
                mediaPlay("hangup.wav",false);
                handler.sendEmptyMessageDelayed(0,500);
            }

            @Override
            public void onFailure(int errorCode) {
                Log.d("可视对讲","拒接失败");
            }
        });
    }

    /**
     * 接听
     */
    private void answer(){
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
        sdk.answer(new ZHYJCallback.Answer() {
            @Override
            public void onSuccess(String results) {
                Log.d("可视对讲","接听成功");
            }

            @Override
            public void onFailure(int errorCode) {
                Log.d("可视对讲","接听失败=>"+errorCode);
            }
        });
    }

    /**
     * 开门
     */
    private void openDoor(){
        sdk.remoteUnlock(new ZHYJCallback.RemoteUnlock() {
            @Override
            public void onSuccess(String results) {
                Log.d("可视对讲","开门成功");
            }

            @Override
            public void onFailure(int errorCode) {
                Log.d("可视对讲","开门失败");
            }
        });
    }

    private void myFinish(){
        new AlertDialog.Builder(VideoCallDialActivity.this)
                .setTitle("提示")
                .setMessage("当前通话中，确认挂断并退出？")
                .setNegativeButton("确认",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(callStatus.equals(ZHYJConstants.CallStatus.ONCALL)){
                            hangUp();
                        }else{
                            refuse();
                        }
                    }
                }).
                setNeutralButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            myFinish();
        }
        return false;
    }
}
