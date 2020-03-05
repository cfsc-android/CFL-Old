package com.xiandao.android.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.hikvision.zhyjsdk.ZHYJHandler;
import com.hikvision.zhyjsdk.ZHYJSDK;
import com.hikvision.zhyjsdk.utils.LogUtils;
import com.xiandao.android.R;
import com.xiandao.android.adapter.VideoListAdapter;
import com.xiandao.android.entity.VideoEntity;
import com.xiandao.android.entity.smart.BaseEntity;
import com.xiandao.android.entity.smart.DeviceEntity;
import com.xiandao.android.http.JsonParse;
import com.xiandao.android.http.MyCallBack;
import com.xiandao.android.http.XUtils;
import com.xiandao.android.http.hikhttp.HikConfig;
import com.xiandao.android.ui.BaseActivity;
import com.xiandao.android.utils.FileManagement;

import org.xutils.common.util.LogUtil;
import org.xutils.event.annotation.ContentView;
import org.xutils.event.annotation.Event;
import org.xutils.event.annotation.ViewInject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.view.View.VISIBLE;
import static com.xiandao.android.base.Config.BASE_URL;
import static com.xiandao.android.base.Config.IOT;

/**
 * Created by ZXL on 2019/4/8.
 * Describe:
 */
@ContentView(R.layout.activity_video_call)
public class VideoCallActivity extends BaseActivity {
    @ViewInject(R.id.tv_title_name)
    private TextView tv_title_name;
    @ViewInject(R.id.lv_video_call_list)
    private ListView lv_video_call_list;
    @ViewInject(R.id.ss_video_call_preview_play)
    private SurfaceView ss_video_call_preview_play;

    private VideoListAdapter adapter;
    private ArrayList<VideoEntity> list=new ArrayList<>();
    private ZHYJSDK sdk;
    private boolean sdkInit=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tv_title_name.setText("可视对讲");
        adapter=new VideoListAdapter(this,list);
        lv_video_call_list.setAdapter(adapter);
        lv_video_call_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ss_video_call_preview_play.setVisibility(VISIBLE);
                handler.sendMessage(handler.obtainMessage(0,list.get(position).getDeviceId()));
            }
        });
        getVideoList();
        initData();
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
                        sdkInit=true;
                    }

                    @Override
                    public void onStartPlaySuccess() {
                        Log.e("可视对讲","预览成功  设备ID" );
                    }

                    @Override
                    public void onStartPlayFail(com.videogo.errorlayer.ErrorInfo errorInfo) {
                        Log.e("可视对讲","预览失败" + errorInfo.description);
                        showShortToast("预览失败" + errorInfo.description);
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

    private void startPreview(String deviceID){
        Log.d("deviceID",deviceID);
        sdk.stopRealPlay();
        sdk.releasePlayer();
        sdk.startRealPlay(deviceID,ss_video_call_preview_play.getHolder());
    }

    private void getVideoList(){
        Map<String,String> map=new HashMap<>();
        map.put("phaseId",FileManagement.getUserInfoEntity().getRoomList().get(0).getPhaseId());
        map.put("userId",FileManagement.getUserInfoEntity().getId());
        XUtils.Get(BASE_URL+IOT+"community/api/access/v1/devices/user",map,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtil.d(result);
                BaseEntity baseEntity= JsonParse.parse(result);
                if(baseEntity.isSuccess()){
                    Type type = new TypeToken<List<DeviceEntity>>() {}.getType();
                    List<DeviceEntity> deviceEntities= (List<DeviceEntity>) JsonParse.parseList(result,type);
                    for (int i = 0; i < deviceEntities.size(); i++) {
                        VideoEntity videoEntity=new VideoEntity();
                        videoEntity.setDeviceName(deviceEntities.get(i).getDeviceName());
                        videoEntity.setDeviceId(deviceEntities.get(i).getDeviceSerial());
                        list.add(videoEntity);
                    }
                    adapter.notifyDataSetChanged();
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
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    startPreview((String) msg.obj);
                    break;
            }
        }
    };

    @Event({R.id.iv_title_left})
    private void onClickEvent(View v){
        switch (v.getId()){
            case R.id.iv_title_left:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(sdkInit){
            sdk.releasePlayer();
            sdk.destroySDK();
        }
    }
}
