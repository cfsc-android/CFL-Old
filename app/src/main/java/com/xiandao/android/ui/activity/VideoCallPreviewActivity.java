package com.xiandao.android.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

//import com.hikvision.tpopensdk.base.TPDeviceInfo;
//import com.hikvision.tpopensdk.base.TPOpenSDK;
//import com.hikvision.tpopensdk.base.TPPlayer;
//import com.hikvision.tpopensdk.play.OnRealPlayCallback;
//import com.hikvision.tpopensdk.util.P;
import com.videogo.widget.CheckTextButton;
import com.xiandao.android.R;
import com.xiandao.android.ui.BaseActivity;
import com.xiandao.android.utils.ScreenOrientationHelper;

import org.xutils.event.annotation.ContentView;
import org.xutils.event.annotation.Event;
import org.xutils.event.annotation.ViewInject;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

@ContentView(R.layout.activity_video_call_preview)
public class VideoCallPreviewActivity extends BaseActivity {
    @ViewInject(R.id.i_title_layout)
    private LinearLayout i_title_layout;
    @ViewInject(R.id.tv_title_name)
    private TextView tv_title_name;

    @ViewInject(R.id.realplay_sound_btn)
    private ImageView btnSound;
    @ViewInject(R.id.realplay_stop_btn)
    private ImageView btnPlayPause;
    @ViewInject(R.id.rl_playView)
    private RelativeLayout playLayout;
    @ViewInject(R.id.tv_progress)
    private TextView tvProgress;
    @ViewInject(R.id.sv_surfaceView)
    private SurfaceView surfaceView;
//    @ViewInject(R.id.fullscreen_button)
//    private CheckTextButton cbFullScreen;

    /**
     * 常量
     */
    private static final int PLAY_STOP = 0;// 已停止
    private static final int PLAY_PREPARING = 1;// 准备中
    private static final int PLAYING = 2;// 播放中

    /**
     * 全局成员变量
     */
    private boolean isSoundOn = true;// 开始播放后，声音默认开启
    private boolean isPlaying = true;// 进入界面之后自动开启播放
    private int playStatus = PLAY_STOP;// 当前播放状态
    private int channelNo;// 设备通道号
    private String deviceSerial;// 设备序列号
    private String channelName;// 通道名称

    /**
     * 业务对象
     */
//    private TPOpenSDK tpOpenSDK;// SDK实例
//    private TPPlayer tpPlayer;// 播放器实例
    private ScreenOrientationHelper screenOrientationHelper = null;// 转屏控制器
//    private TPDeviceInfo deviceInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tv_title_name.setText("视频预览");
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        channelNo = bundle.getInt("CHANNELNO");
        deviceSerial=bundle.getString("DEVICESERIAL");
        channelName=bundle.getString("NAME");
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// 保持屏幕常亮
//        tpOpenSDK = TPOpenSDK.getInstance();
//        screenOrientationHelper = new ScreenOrientationHelper(this, cbFullScreen);
        screenOrientationHelper.setOrientationChangeListener(new ScreenOrientationHelper.OnOrientationChange() {
            @Override
            public void currentOrientation(int orientation) {
                if(orientation==0){
                    i_title_layout.setVisibility(VISIBLE);
                }else{
                    i_title_layout.setVisibility(GONE);
                }
            }
        });
//        TPOpenSDK.getInstance().getTPDeviceInfo(deviceSerial, channelNo, new TPOpenSDK.OnGetTPDeviceInfoCallback() {
//            @Override
//            public void onGetDeviceInfo(TPDeviceInfo tpDeviceInfo) {
//                deviceInfo=tpDeviceInfo;
//            }
//        });
//        surfaceView.getHolder().addCallback(surfaceHolderCallback);
    }
//
//    @Event({R.id.realplay_sound_btn,R.id.realplay_stop_btn,R.id.iv_title_left})
//    private void onClickEvent(View v){
//        switch (v.getId()){
//            case R.id.iv_title_left:
//                finish();
//                break;
//            case R.id.realplay_sound_btn:
//                if (tpPlayer == null || playStatus != PLAYING) {
//                    return;
//                }
//                boolean success;
//                if (isSoundOn) {
//                    success = tpPlayer.closeSound();
//                    btnSound.setBackgroundResource(R.drawable.ezopen_vertical_preview_sound_off_selector);
//                } else {
//                    success = tpPlayer.openSound();
//                    btnSound.setBackgroundResource(R.drawable.ezopen_vertical_preview_sound_selector);
//                }
//                isSoundOn = success ? !isSoundOn : isSoundOn;
//                break;
//            case R.id.realplay_stop_btn:
//                if (playStatus == PLAYING) {
//                    stopPlay();
//                    btnSound.setVisibility(GONE);
//                    isPlaying = false;
//                    btnPlayPause.setBackgroundResource(R.drawable.play_play_selector);
//                } else {
//                    startPlay();
//                    isPlaying = true;
//                    btnPlayPause.setBackgroundResource(R.drawable.play_stop_selector);
//                }
//                break;
//        }
//    }
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            if(screenOrientationHelper.getOrientation()==1){
//                screenOrientationHelper.setOrientationPortrait();
//                return false;
//            }
//        }
//        return super.onKeyDown(keyCode, event);
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        screenOrientationHelper.postOnStop();
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        screenOrientationHelper.postOnStart();
//        screenOrientationHelper.disableSensorOrientation();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (tpPlayer != null) {
//            tpPlayer.stopRealPlay();
//            tpPlayer.stopVoiceTalk();
//        }
//        TPOpenSDK.releasePlayer(tpPlayer);
//    }
//
//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        P.i("onConfigurationChanged:" + newConfig.orientation);
//        // 横屏
//        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            //隐身通知栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
//
//            ViewGroup.LayoutParams rootParams = playLayout.getLayoutParams();
//            rootParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
//            rootParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
//        }
//        // 竖屏
//        else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
//            //展示通知栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
//            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//
//            ViewGroup.LayoutParams rootParams = playLayout.getLayoutParams();
//            rootParams.height = dip2px(this, 200);
//            rootParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
//        }
//    }
//
//    /**
//     * dp转px
//     *
//     * @param context
//     * @param dpValue
//     * @return
//     */
//    public static final int dip2px(Context context, float dpValue) {
//        final float scale = context.getResources().getDisplayMetrics().density;
//        return (int) (dpValue * scale + 0.5f);
//    }
//
//
//    private OnRealPlayCallback onRealPlayCallback = new OnRealPlayCallback() {
//        @Override
//        public void onRealPlaySuccess() {
//            tvProgress.setVisibility(GONE);
//            playStatus = PLAYING;
//            btnSound.setVisibility(VISIBLE);
//            screenOrientationHelper.enableSensorOrientation();
//            // 播放成功
//        }
//
//        @Override
//        public void onRealPlayError(int errorCode) {
//            tvProgress.setVisibility(VISIBLE);
//            tvProgress.setText("播放异常:" + errorCode);
//            stopPlay();
//            btnSound.setVisibility(GONE);
//            btnPlayPause.setBackgroundResource(R.drawable.play_play_selector);
//        }
//
//        @Override
//        public void onPrepareProgress(int progress) {
//            tvProgress.setVisibility(VISIBLE);
//            tvProgress.setText(progress + "%");
//            playStatus = PLAY_PREPARING;
//        }
//    };
//
//    /**
//     * 开始播放
//     */
//    private void startPlay() {
//        TPOpenSDK.createPlayer(VideoCallPreviewActivity.this, deviceSerial, channelNo, new TPOpenSDK.OnCreateTPPlayerCallback() {
//            @Override
//            public void onSuccess(TPPlayer player) {
//                surfaceView.setVisibility(VISIBLE);
//                tpPlayer = player;
//                tpPlayer.setPlayerView(surfaceView.getHolder());
//                tpPlayer.startRealPlay(onRealPlayCallback);
//            }
//
//            @Override
//            public void onFail(String msg) {
//                showLongToast("创建Player失败:" + msg);
//                tvProgress.setVisibility(GONE);
//                return;
//            }
//        });
//    }
//
//    /**
//     * 终止播放
//     */
//    private void stopPlay() {
//        if (tpPlayer != null) {
//            screenOrientationHelper.disableSensorOrientation();
//            playStatus = PLAY_STOP;
//            tpPlayer.stopRealPlay();
//            TPOpenSDK.releasePlayer(tpPlayer);
//            tpPlayer = null;
//        }
//    }
//
//
//    /**
//     * SurfaceView渲染完成后，自动开始播放
//     */
//    private SurfaceHolder.Callback surfaceHolderCallback = new SurfaceHolder.Callback() {
//        @Override
//        public void surfaceCreated(SurfaceHolder holder) {
//            startPlay();
//        }
//
//        @Override
//        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//        }
//
//        @Override
//        public void surfaceDestroyed(SurfaceHolder holder) {
//            if (tpPlayer != null) {
//                tpPlayer.stopRealPlay();
//                TPOpenSDK.releasePlayer(tpPlayer);
//            }
//        }
//    };


}
