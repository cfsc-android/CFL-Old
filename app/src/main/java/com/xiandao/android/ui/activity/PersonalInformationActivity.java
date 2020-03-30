package com.xiandao.android.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andview.refreshview.utils.LogUtils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.xiandao.android.R;
import com.xiandao.android.entity.LoginResultDataEntity;
import com.xiandao.android.entity.LoginUserEntity;
import com.xiandao.android.entity.OverallSituationEntity;
import com.xiandao.android.entity.PersonalInfomation;
import com.xiandao.android.entity.QQLoginEntity;
import com.xiandao.android.entity.RoomInfoEntity;
import com.xiandao.android.entity.ThirdInfoEntity;
import com.xiandao.android.entity.WeiXinLoginEntity;
import com.xiandao.android.entity.eventbus.EventBusMessage;
import com.xiandao.android.entity.eventbus.FaceCollectionEventBusData;
import com.xiandao.android.entity.eventbus.NickNameEventBusData;
import com.xiandao.android.entity.smart.BaseEntity;
import com.xiandao.android.entity.smart.FileEntity;
import com.xiandao.android.entity.smart.UserInfoEntity;
import com.xiandao.android.http.JsonParse;
import com.xiandao.android.http.MyCallBack;
import com.xiandao.android.http.XUtils;
import com.xiandao.android.net.ApiHttpResult;
import com.xiandao.android.net.HttpUtils;
import com.xiandao.android.net.NetUtil;
import com.xiandao.android.ui.BaseActivity;
import com.xiandao.android.ui.BaseTakePhotoActivity;
import com.xiandao.android.utils.Constants;
import com.xiandao.android.utils.FileManagement;
import com.xiandao.android.utils.FilePathUtil;
import com.xiandao.android.utils.PermissionsUtils;
import com.xiandao.android.utils.PhotoUtils;
import com.xiandao.android.utils.Tools;
import com.xiandao.android.utils.XUtilsImageUtils;
import com.xiandao.android.view.BirthWheelDialog;
import com.xiandao.android.view.WheelDialog;
import com.xiandao.android.view.alertview.AlertView;
import com.xiandao.android.view.alertview.OnItemClickListener;
import com.xiandao.android.view.imagepreview.ImageViewInfo;
import com.xiandao.android.view.photopicker.PhotoPicker;
import com.zhihu.matisse.Matisse;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.app.LynActivityManager;
import org.xutils.common.util.LogUtil;
import org.xutils.event.annotation.ContentView;
import org.xutils.event.annotation.Event;
import org.xutils.event.annotation.ViewInject;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


import androidx.annotation.NonNull;
import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.xiandao.android.base.Config.BASE_URL;
import static com.xiandao.android.base.Config.BASIC;
import static com.xiandao.android.base.Config.FILE;
import static com.xiandao.android.base.Config.PHOTO_DIR_NAME;
import static com.xiandao.android.base.Config.SD_APP_DIR_NAME;

@ContentView(R.layout.activity_personal_information)
public class PersonalInformationActivity extends BaseActivity {
    @ViewInject(R.id.tv_title_name)
    private TextView tv_title_name;
    @ViewInject(R.id.iv_personal_pic)
    private ImageView iv_personal_pic;
    @ViewInject(R.id.tv_personal_nick_name)
    private TextView tv_personal_nick_name;
    @ViewInject(R.id.tv_personal_tel)
    private TextView tv_personal_tel;
    @ViewInject(R.id.tv_personal_sex)
    private TextView tv_personal_sex;
    @ViewInject(R.id.tv_personal_birth)
    private TextView tv_personal_birth;
    @ViewInject(R.id.tv_personal_address)
    private TextView tv_personal_address;
    @ViewInject(R.id.tv_personal_in_date)
    private TextView tv_personal_in_date;
    @ViewInject(R.id.tv_personal_housing_mode)
    private TextView tv_personal_housing_mode;
    @ViewInject(R.id.tv_personal_pic)
    private TextView tv_personal_pic;
    @ViewInject(R.id.rl_personal_pic)
    private RelativeLayout rl_personal_pic;
    @ViewInject(R.id.iv_personal_pic_set)
    private ImageView iv_personal_pic_set;
    @ViewInject(R.id.iv_personal_nick_name_set)
    private ImageView iv_personal_nick_name_set;
    @ViewInject(R.id.iv_personal_tel_set)
    private ImageView iv_personal_tel_set;
    @ViewInject(R.id.iv_personal_sex_set)
    private ImageView iv_personal_sex_set;
    @ViewInject(R.id.iv_personal_birth_set)
    private ImageView iv_personal_birth_set;
    @ViewInject(R.id.ll_personal_tel_bind_ll)
    private LinearLayout ll_personal_tel_bind_ll;
    @ViewInject(R.id.ll_personal_third_bind_ll)
    private LinearLayout ll_personal_third_bind_ll;

    private static final String[] permission={Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    private static final int REQUEST_CODE_CHOOSE=0x001;

    private UserInfoEntity userInfo;
    private int sexChoice=-1;
    private BirthWheelDialog wheeldialog;
    private boolean editFlag=false;
    private boolean permissionFlag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tv_title_name.setText("个人资料");
        EventBus.getDefault().register(this);
        initView();
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
    }

    private void initView(){
        editFlag=true;
        userInfo=FileManagement.getUserInfoEntity();
        if(userInfo.getAvatarResource()!=null){
            rl_personal_pic.setVisibility(VISIBLE);
            tv_personal_pic.setVisibility(GONE);
            XUtilsImageUtils.display(iv_personal_pic,userInfo.getAvatarResource().getUrl(),ImageView.ScaleType.CENTER_INSIDE);
        }else{
            rl_personal_pic.setVisibility(GONE);
            tv_personal_pic.setVisibility(VISIBLE);
        }
        tv_personal_nick_name.setText(userInfo.getNickName());
        tv_personal_tel.setText(userInfo.getMobile());
        iv_personal_tel_set.setVisibility(GONE);
        tv_personal_sex.setText(getSex());
        tv_personal_birth.setText(userInfo.getBirthday());
        if (TextUtils.isEmpty(userInfo.getCurrentDistrict().getRoomId())) {
            tv_personal_address.setText(userInfo.getCurrentDistrict().getBuildingName()+userInfo.getCurrentDistrict().getUnitName()+userInfo.getCurrentDistrict().getRoomName());
        }else{
            tv_personal_address.setText("");
        }
        tv_personal_in_date.setText(userInfo.getCheckinDate());
        tv_personal_housing_mode.setText("");
        ll_personal_tel_bind_ll.setVisibility(GONE);
        ll_personal_third_bind_ll.setVisibility(VISIBLE);
    }

    private void editPersonalInfo(Map<String,Object> map){
        map.put("id",FileManagement.getUserInfoEntity().getId());
        startProgressDialog("");
        XUtils.Put(BASE_URL+BASIC+"basic/householdInfo/specificField",map,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtils.d(result);
                BaseEntity baseEntity= JsonParse.parse(result);
                if(baseEntity.isSuccess()){
                    getUserInfo();
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
                    initView();
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

    private String getSex(){
        String sex="未知";
        if(!Tools.isEmpty(userInfo.getGender())){
            if("1".equals(userInfo.getGender())){
                sex="男";
            }else if("2".equals(userInfo.getGender())){
                sex="女";
            }
        }
        return sex;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(final EventBusMessage message){
        if("nickName".equals(message.getMessage())){
            final Map<String,Object> map=new HashMap<>();
            map.put("nickName",((NickNameEventBusData)message.getData()).getNickName());
            editPersonalInfo(map);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Event({R.id.iv_title_left,R.id.ll_personal_address,R.id.ll_personal_birth,
            R.id.ll_personal_nick_name,R.id.ll_personal_pic,R.id.ll_personal_sex,
            R.id.ll_personal_tel_bind,R.id.ll_personal_third_bind})
    private void onClickEvent(View v){
        switch (v.getId()){
            case R.id.iv_title_left:
                finish();
                break;
            case R.id.ll_personal_birth:
                if(editFlag){
                    wheeldialog = new BirthWheelDialog(this, R.style.Dialog_Floating, new BirthWheelDialog.OnDateTimeConfirm() {
                        @Override
                        public void returnData(final String dateText, String dateValue) {
                            wheeldialog.cancel();
                            Map<String,Object> map=new HashMap<>();
                            map.put("birthday",dateText+" 00:00:00");
                            editPersonalInfo(map);
                        }
                    });
                    wheeldialog.show();
                    wheeldialog.setBirth(tv_personal_birth.getText().toString());
                }

                break;
            case R.id.ll_personal_nick_name:
                if(editFlag){
                    openActivity(PersonalNickNameActivity.class);
                }
                break;
            case R.id.ll_personal_pic:
                if(editFlag){
                    if(permissionFlag){
                        PhotoPicker.pick(PersonalInformationActivity.this,10,true,REQUEST_CODE_CHOOSE);
                    }else{
                        showToast("相机或读写手机存储的权限被禁止！");
                    }
                }
                break;
            case R.id.ll_personal_sex:
                if(editFlag){
                    dialogChoice();
                }
                break;
            case R.id.ll_personal_third_bind:
                showShortToast("敬请期待");
                break;
            case R.id.ll_personal_tel_bind:
                showShortToast("待开发");
                break;
        }
    }
    /**
     * 单选
     */
    private void dialogChoice() {
        int choicePosition;
        if("男".equals(getSex())){
            choicePosition=0;
        }else if("女".equals(getSex())){
            choicePosition=1;
        }else{
            choicePosition=-1;
        }
        final String items[] = {"男", "女"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this,0);
        builder.setTitle("选择性别");
        builder.setSingleChoiceItems(items, choicePosition, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sexChoice=which;
                    }
                });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Map<String,Object> map=new HashMap<>();
                map.put("gender",sexChoice+1);
                editPersonalInfo(map);

            }
        });
        builder.create().show();
    }

    //上传照片
    private void uploadPic(String path){
        Map<String,String> requestMap=new HashMap<>();
        Map<String,File> fileMap=new HashMap<>();
        fileMap.put("UploadFile",new File(path));
        XUtils.UpLoadFile(BASE_URL+FILE+"files-anon", requestMap,fileMap,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtils.d(result);
                LogUtils.d(result);
                BaseEntity<FileEntity> baseEntity= JsonParse.parse(result,FileEntity.class);
                if(baseEntity.isSuccess()){
                    Map<String,Object> map=new HashMap<>();
                    map.put("avatarId",baseEntity.getResult().getId());
                    editPersonalInfo(map);
                }else{
                    showToast(baseEntity.getMessage());
                }


            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                showShortToast(ex.getMessage());
            }
        });
    }

    public interface OnEditSuccessListener{
        void onEditSuccess();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE_CHOOSE&&resultCode==RESULT_OK){
            //图片路径 同样视频地址也是这个 根据requestCode
            List<Uri> pathList = Matisse.obtainResult(data);
            List<String> _List = new ArrayList<>();
            for (Uri _Uri : pathList)
            {
                String _Path = FilePathUtil.getPathByUri(this,_Uri);
                File _File = new File(_Path);
                LogUtil.d("压缩前图片大小->" + _File.length() / 1024 + "k");
                _List.add(_Path);
            }
            compress(_List);
        }else{
            UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        }

    }


    //压缩图片
    private void compress(List<String> list){
        String _Path = FilePathUtil.createPathIfNotExist("/" + SD_APP_DIR_NAME + "/" + PHOTO_DIR_NAME);
        LogUtil.d("_Path->" + _Path);
        Luban.with(this)
                .load(list)
                .ignoreBy(100)
                .setTargetDir(_Path)
                .filter(new CompressionPredicate() {
                    @Override
                    public boolean apply(String path) {
                        return !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif"));
                    }
                })
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {
                        LogUtil.d(" 压缩开始前调用，可以在方法内启动 loading UI");
                    }

                    @Override
                    public void onSuccess(File file) {
                        LogUtil.d(" 压缩成功后调用，返回压缩后的图片文件");
                        LogUtil.d("压缩后图片大小->" + file.length() / 1024 + "k");
                        LogUtil.d("getAbsolutePath->" + file.getAbsolutePath());
                        uploadPic(file.getAbsolutePath());
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                }).launch();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionsUtils.getInstance().onRequestPermissionsResult(this,requestCode,permissions,grantResults);
    }
}
