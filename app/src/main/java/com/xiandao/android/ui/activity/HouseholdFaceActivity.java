package com.xiandao.android.ui.activity;

import android.Manifest;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xiandao.android.R;
import com.xiandao.android.entity.eventbus.EventBusMessage;
import com.xiandao.android.entity.eventbus.FaceCollectionEventBusData;
import com.xiandao.android.entity.smart.RoomHouseholdEntity;
import com.xiandao.android.ui.BaseActivity;
import com.xiandao.android.ui.activity.FaceCollectionPotoActivity;
import com.xiandao.android.utils.PermissionsUtils;
import com.xiandao.android.utils.XUtilsImageUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.common.util.LogUtil;
import org.xutils.event.annotation.ContentView;
import org.xutils.event.annotation.Event;
import org.xutils.event.annotation.ViewInject;

import androidx.annotation.NonNull;

@ContentView(R.layout.activity_household_face)
public class HouseholdFaceActivity extends BaseActivity {
    private static final String[] permission={Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    private boolean permissionFlag;

    @ViewInject(R.id.tv_title_name)
    private TextView tv_title_name;
    @ViewInject(R.id.household_face_btn_collection)
    private Button household_face_btn_collection;
    @ViewInject(R.id.household_face_tv_text)
    private TextView household_face_tv_text;
    @ViewInject(R.id.household_face_iv_pic)
    private ImageView household_face_iv_pic;


    private boolean edit;
    private RoomHouseholdEntity householdEntity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        edit=getIntent().getExtras().getBoolean("edit");
        if(!edit){
            household_face_btn_collection.setVisibility(View.GONE);
        }
        householdEntity= (RoomHouseholdEntity) getIntent().getExtras().getSerializable("household");
        tv_title_name.setText(TextUtils.isEmpty(householdEntity.getNickName())?"人脸信息":householdEntity.getNickName());
        if(householdEntity.getFaceInfos()!=null&&householdEntity.getFaceInfos().size()>0){
            Glide.with(this)
                    .load(householdEntity.getFaceInfos().get(0).getUrl())
                    .circleCrop()
                    .error(R.drawable.ic_default_img)
                    .into(household_face_iv_pic);
            household_face_tv_text.setText(householdEntity.getFaceInfos().get(0).getCreateTime());
        }else{
            Glide.with(this)
                    .load(R.drawable.icon_user_default)
                    .circleCrop()
                    .into(household_face_iv_pic);
            household_face_tv_text.setText("人脸照片尚未上传");
        }


        PermissionsUtils.getInstance().checkPermissions(this, permission, new PermissionsUtils.IPermissionsResult() {
            @Override
            public void success() {
                LogUtil.d("申请权限通过");
                permissionFlag=true;
            }

            @Override
            public void fail() {
                LogUtil.d("申请权限未通过");
                permissionFlag=false;
            }
        });
        EventBus.getDefault().register(this);
    }

    @Event({R.id.iv_title_left,R.id.household_face_btn_collection})
    private void onClickEvent(View v){
        switch (v.getId()){
            case R.id.iv_title_left:
                finish();
                break;
            case R.id.btn_face_collection:
                if(permissionFlag){
                    Bundle bundle=new Bundle();
                    bundle.putString("id",householdEntity.getId());
                    bundle.putString("name",householdEntity.getName());
                    bundle.putBoolean("update",householdEntity.getFaceInfos()!=null&&householdEntity.getFaceInfos().size()>0);
                    openActivity(FaceCollectionPhotoActivity.class,bundle);
                }else{
                    showToast("相机或读写手机存储的权限被禁止！");
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionsUtils.getInstance().onRequestPermissionsResult(this,requestCode,permissions,grantResults);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(EventBusMessage message){
        if("faceCollection".equals(message.getMessage())){
            household_face_btn_collection.setText("重新上传");
            household_face_tv_text.setText("上传时间:"+((FaceCollectionEventBusData)message.getData()).getFaceDisTime());
            Glide.with(this)
                    .load(((FaceCollectionEventBusData)message.getData()).getFaceDisUrl())
                    .circleCrop()
                    .error(R.drawable.ic_default_img)
                    .into(household_face_iv_pic);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
