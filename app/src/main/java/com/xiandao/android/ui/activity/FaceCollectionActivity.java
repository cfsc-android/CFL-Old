package com.xiandao.android.ui.activity;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiandao.android.R;
import com.xiandao.android.entity.eventbus.EventBusMessage;
import com.xiandao.android.entity.eventbus.FaceCollectionEventBusData;
import com.xiandao.android.ui.BaseActivity;
import com.xiandao.android.utils.Tools;
import com.xiandao.android.utils.XUtilsImageUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.event.annotation.ContentView;
import org.xutils.event.annotation.Event;
import org.xutils.event.annotation.ViewInject;

@ContentView(R.layout.activity_face_collection)
public class FaceCollectionActivity extends BaseActivity {
    @ViewInject(R.id.tv_title_name)
    private TextView tv_title_name;
    @ViewInject(R.id.btn_face_collection)
    private Button btn_face_collection;
    @ViewInject(R.id.tv_user_text)
    private TextView tv_user_text;
    @ViewInject(R.id.iv_user_pic)
    private ImageView iv_user_pic;

    private boolean cameraPermiss=true;
    private String userId="",name="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle=getIntent().getExtras();
        userId=bundle.getString("userId");
        name=bundle.getString("name");
        tv_title_name.setText(bundle.getString("nickname"));
        String faceDisUrl=bundle.getString("faceDisUrl");
        if(!Tools.isEmpty(faceDisUrl)){
            btn_face_collection.setText("重新上传");
            tv_user_text.setText("上传时间:"+bundle.getString("faceDisTime"));
            XUtilsImageUtils.display(iv_user_pic,faceDisUrl,true);
        }
//        LoginUserEntity loginUserEntity=FileManagement.getLoginUserEntity();
//        if(!Tools.isEmpty(loginUserEntity.getFaceDisUrl())){
//            btn_face_collection.setText("重新上传");
//            tv_user_text.setText("上传时间:"+loginUserEntity.getFaceDisTime());
//            XUtilsImageUtils.display(iv_user_pic,loginUserEntity.getFaceDisUrl(),true);
//        }
        if(Build.VERSION.SDK_INT>=23){
            int hasPermission = checkSelfPermission(android.Manifest.permission.CAMERA);
            if(hasPermission!= PackageManager.PERMISSION_GRANTED){
                cameraPermiss=false;
                String[] mPermissionList = new String[]{android.Manifest.permission.CAMERA};
                ActivityCompat.requestPermissions(this,mPermissionList,123);
            }
        }
        EventBus.getDefault().register(this);
    }
    @Event({R.id.iv_title_left,R.id.btn_face_collection})
    private void onClickEvent(View v){
        switch (v.getId()){
            case R.id.iv_title_left:
                finish();
                break;
            case R.id.btn_face_collection:
                if(cameraPermiss){
                    Bundle bundle=new Bundle();
                    bundle.putString("userId",userId);
                    bundle.putString("name",name);
                    openActivity(FaceCollectionPotoActivity.class,bundle);
                }
                break;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        if (requestCode == 123) {
            cameraPermiss=true;
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(EventBusMessage message){
        if("faceCollection".equals(message.getMessage())){
            btn_face_collection.setText("重新上传");
            tv_user_text.setText("上传时间:"+((FaceCollectionEventBusData)message.getData()).getFaceDisTime());
            XUtilsImageUtils.display(iv_user_pic,((FaceCollectionEventBusData)message.getData()).getFaceDisUrl(),true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
