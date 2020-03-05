package com.xiandao.android.ui.activity;

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
import com.xiandao.android.http.MyCallBack;
import com.xiandao.android.http.XUtils;
import com.xiandao.android.net.ApiHttpResult;
import com.xiandao.android.net.HttpUtils;
import com.xiandao.android.net.NetUtil;
import com.xiandao.android.ui.BaseActivity;
import com.xiandao.android.ui.BaseTakePhotoActivity;
import com.xiandao.android.utils.Constants;
import com.xiandao.android.utils.FileManagement;
import com.xiandao.android.utils.PhotoUtils;
import com.xiandao.android.utils.Tools;
import com.xiandao.android.utils.XUtilsImageUtils;
import com.xiandao.android.view.BirthWheelDialog;
import com.xiandao.android.view.WheelDialog;
import com.xiandao.android.view.alertview.AlertView;
import com.xiandao.android.view.alertview.OnItemClickListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.event.annotation.ContentView;
import org.xutils.event.annotation.Event;
import org.xutils.event.annotation.ViewInject;
import org.xutils.http.RequestParams;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


import static android.view.View.GONE;
import static android.view.View.VISIBLE;

@ContentView(R.layout.activity_personal_information)
public class PersonalInformationActivity extends BaseTakePhotoActivity {
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



    private LoginUserEntity personal;
    private ArrayList<RoomInfoEntity> roomInfoEntity;
    private ArrayList<TImage> tImages = new ArrayList<>();// 添加图片集合
    private ArrayList<File> files = new ArrayList<>();
    private int sexChoice=-1;
    private BirthWheelDialog wheeldialog;
    private boolean editFlag=false;
    private AlertDialog thirdBindDialog;
    private View dialogView;
    private ImageView qq_img,wx_img;
    private TextView qq_name,wx_name,qq_cancel,wx_cancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tv_title_name.setText("个人资料");
        EventBus.getDefault().register(this);
        init();
    }


    private void init(){
        if(FileManagement.getTokenInfo().equals("third")){
            editFlag=false;
            rl_personal_pic.setVisibility(VISIBLE);
            tv_personal_pic.setVisibility(GONE);
            iv_personal_pic_set.setVisibility(GONE);
            if(FileManagement.getLoginType().equals("wx")){
                XUtilsImageUtils.display(iv_personal_pic,FileManagement.getWXLogin().getIconurl(),ImageView.ScaleType.CENTER_INSIDE);
                tv_personal_nick_name.setText(FileManagement.getWXLogin().getName());
                tv_personal_sex.setText(FileManagement.getWXLogin().getGender());
            }else{
                XUtilsImageUtils.display(iv_personal_pic,FileManagement.getQQLogin().getIconurl(),ImageView.ScaleType.CENTER_INSIDE);
                tv_personal_nick_name.setText(FileManagement.getQQLogin().getName());
                tv_personal_sex.setText(FileManagement.getQQLogin().getGender());
            }
            iv_personal_nick_name_set.setVisibility(GONE);
            iv_personal_sex_set.setVisibility(GONE);
            tv_personal_birth.setText("");
            iv_personal_birth_set.setVisibility(GONE);
            tv_personal_tel.setText("");
            iv_personal_tel_set.setVisibility(GONE);
            ll_personal_tel_bind_ll.setVisibility(VISIBLE);
            ll_personal_third_bind_ll.setVisibility(GONE);
        }else{
            editFlag=true;
            personal=FileManagement.getLoginUserEntity();
            roomInfoEntity = FileManagement.getRoomInfo();
            if(!Tools.isEmpty(personal.getHeadImageUrl())){
                rl_personal_pic.setVisibility(VISIBLE);
                tv_personal_pic.setVisibility(GONE);
                XUtilsImageUtils.display(iv_personal_pic,Constants.BASEHOST+personal.getHeadImageUrl(),ImageView.ScaleType.CENTER_INSIDE);
            }else{
                rl_personal_pic.setVisibility(GONE);
                tv_personal_pic.setVisibility(VISIBLE);
            }
            tv_personal_nick_name.setText(personal.getNickName());
            tv_personal_tel.setText(personal.getMobileNumber());
            iv_personal_tel_set.setVisibility(GONE);
            tv_personal_sex.setText(getSex());
            tv_personal_birth.setText("");
            if (roomInfoEntity != null && roomInfoEntity.size() > 0) {
                tv_personal_address.setText(Tools.getStringValue(roomInfoEntity.get(0).getAddress()));
            }else{
                tv_personal_address.setText("");
            }
            tv_personal_in_date.setText("");
            tv_personal_housing_mode.setText("");
            ll_personal_tel_bind_ll.setVisibility(GONE);
            ll_personal_third_bind_ll.setVisibility(VISIBLE);
            getPersonalInfo();
        }

    }

    private void getPersonalInfo(){
        startProgressDialog("");
        XUtils.Get(Constants.HOST+"/owner/getOwnerInfo.action?userId="+personal.getUserId(),null,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                Log.e("getPersonalInfo",result);
                try {
                    JSONObject jsonObject=new JSONObject(result);
                    if("0".equals(jsonObject.getString("resultCode"))){
                        Gson gson=new Gson();
                        PersonalInfomation personalInfomation=gson.fromJson(jsonObject.getString("data"),PersonalInfomation.class);
                        tv_personal_birth.setText(personalInfomation.getBirthday());
                        tv_personal_in_date.setText(personalInfomation.getCheckindate());
                        tv_personal_housing_mode.setText(getLiveMode(personalInfomation.getLivemode()));
                    }
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

    private void editPersonalInfo(RequestParams params, final OnEditSuccessLisener lisener){
        startProgressDialog("");
        params.addBodyParameter("ownerid",personal.getUserId());
        XUtils.UploadFile(params,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                Log.e("result",result);
                try {
                    JSONObject jsonObject=new JSONObject(result);
                    if("0".equals(jsonObject.getString("resultCode"))){
                        Gson gson=new Gson();
                        PersonalInfomation personalInfomation=gson.fromJson(jsonObject.getString("data"),PersonalInfomation.class);
                        personal.setHeadImageUrl(personalInfomation.getFace());
                        personal.setNickName(personalInfomation.getNickname());
                        personal.setGender(personalInfomation.getGender()+"");
                        FileManagement.setBaseUser(personal);
                        lisener.onEditSuccess(personalInfomation);
                    }
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

    private String getSex(){
        String sex="未知";
        if(!Tools.isEmpty(personal.getGender())){
            if("1".equals(personal.getGender())){
                sex="男";
            }else if("2".equals(personal.getGender())){
                sex="女";
            }
        }
        return sex;
    }

    private String getLiveMode(String mode){
        String result="";
        if("1".equals(mode)){
            result="单身居住";
        }else if("2".equals(mode)){
            result="合伙居住";
        }else if("3".equals(mode)){
            result="家庭居住";
        }else if("4".equals(mode)){
            result="集体居住";
        }else if("99".equals(mode)){
            result="其它";
        }
        return result;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(EventBusMessage message){
        if("nickName".equals(message.getMessage())){
            RequestParams params=new RequestParams(Constants.HOST+Constants.EDITPERSONALINFORMATION);
            params.addBodyParameter("nickname",((NickNameEventBusData)message.getData()).getNickName());
            editPersonalInfo(params, new OnEditSuccessLisener() {
                @Override
                public void onEditSuccess(PersonalInfomation personalInfomation) {
                    tv_personal_nick_name.setText(personalInfomation.getNickname());
                }
            });
        }else if("bind".equals(message.getMessage())){
            Log.e("bind","Personal_bind");
            init();
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
                        public void returnData(String dateText, String dateValue) {
                            wheeldialog.cancel();
                            RequestParams params=new RequestParams(Constants.HOST+Constants.EDITPERSONALINFORMATION);
                            params.addBodyParameter("birthday",dateText);
                            editPersonalInfo(params, new OnEditSuccessLisener() {
                                @Override
                                public void onEditSuccess(PersonalInfomation personalInfomation) {
                                    tv_personal_birth.setText(personalInfomation.getBirthday());
                                }
                            });
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
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0); //强制隐藏键盘

                    getTakePhoto();
                    File file = new File(Environment.getExternalStorageDirectory(), "/xiandao/" + System.currentTimeMillis() + ".jpg");
                    if (!file.getParentFile().exists()) {
                        file.getParentFile().mkdirs();
                    }
                    final Uri imageUri = Uri.fromFile(file);
                    configCompress(takePhoto);
                    configTakePhotoOption(takePhoto);

                    new AlertView("选择获取图片方式", null, "取消", null,
                            new String[]{"拍照", "从相册中选择"}, PersonalInformationActivity.this,
                            AlertView.Style.ActionSheet, new OnItemClickListener() {
                        @Override
                        public void onItemClick(Object o, int position) {
                            if (position == 0) {
                                takePhoto.onPickFromCapture(imageUri);
                            } else if (position == 1) {
                                takePhoto.onPickMultiple(1 - tImages.size());
                            }
                        }
                    }).show();
                }
                break;
            case R.id.ll_personal_sex:
                if(editFlag){
                    dialogChoice();
                }
                break;
            case R.id.ll_personal_third_bind:
                thirdBindDialogShow();
                break;
            case R.id.ll_personal_tel_bind:
                openActivity(PersonalTelBindActivity.class);
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
        builder.setSingleChoiceItems(items, choicePosition,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sexChoice=which;
                    }
                });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                RequestParams params=new RequestParams(Constants.HOST+Constants.EDITPERSONALINFORMATION);
                params.addBodyParameter("gender",(sexChoice+1)+"");
                editPersonalInfo(params, new OnEditSuccessLisener() {
                    @Override
                    public void onEditSuccess(PersonalInfomation personalInfomation) {
                        tv_personal_sex.setText(personalInfomation.getGender()==1?"男":"女");
                    }
                });

            }
        });
        builder.create().show();
    }

    private void thirdBindDialogShow(){
        if(thirdBindDialog==null){
            initDialogView();
            AlertDialog.Builder customerDialog= new AlertDialog.Builder(PersonalInformationActivity.this);
            customerDialog.setTitle("绑定QQ/微信");
            customerDialog.setView(dialogView);
            customerDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            thirdBindDialog=customerDialog.create();
        }
        thirdBindDialog.show();
    }

    private void initDialogView(){
        if(dialogView==null){
            dialogView=LayoutInflater.from(PersonalInformationActivity.this)
                    .inflate(R.layout.dialog_personal_third_bind,null);
            qq_img= (ImageView) dialogView.findViewById(R.id.iv_dialog_third_bind_qq);
            qq_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    thirdBind(SHARE_MEDIA.QQ);
                }
            });
            wx_img= (ImageView) dialogView.findViewById(R.id.iv_dialog_third_bind_wx);
            wx_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    thirdBind(SHARE_MEDIA.WEIXIN);
                }
            });
            qq_name= (TextView) dialogView.findViewById(R.id.tv_dialog_third_bind_qq);
            wx_name= (TextView) dialogView.findViewById(R.id.tv_dialog_third_bind_wx);
            qq_cancel= (TextView) dialogView.findViewById(R.id.tv_dialog_third_bind_qq_cancel);
            qq_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    thirdUnBind(SHARE_MEDIA.QQ);
                    thirdUnToTelBind("1");
                }
            });
            wx_cancel= (TextView) dialogView.findViewById(R.id.tv_dialog_third_bind_wx_cancel);
            wx_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    thirdUnBind(SHARE_MEDIA.WEIXIN);
                    thirdUnToTelBind("0");
                }
            });
        }
        boolean wx_bind=false,qq_bind=false;
        ArrayList<ThirdInfoEntity> thirdInfoEntities=FileManagement.getThirdInfo();
        if(thirdInfoEntities!=null&&thirdInfoEntities.size()>0){
            for (ThirdInfoEntity thirdInfoEntity : thirdInfoEntities) {
                if(thirdInfoEntity.getType().equals("0")){
                    wx_bind=true;
                    XUtilsImageUtils.display(wx_img,thirdInfoEntity.getFaceImg(),true);
                    wx_name.setText(thirdInfoEntity.getNickname());
                }else if(thirdInfoEntity.getType().equals("1")){
                    qq_bind=true;
                    XUtilsImageUtils.display(qq_img,thirdInfoEntity.getFaceImg(),true);
                    qq_name.setText(thirdInfoEntity.getNickname());
                }
            }
        }
        if(wx_bind){
            wx_cancel.setVisibility(VISIBLE);
        }else{
            wx_img.setImageResource(R.drawable.umeng_socialize_wechat);
            wx_name.setText("微信");
            wx_cancel.setVisibility(GONE);
        }
        if(qq_bind){
            qq_cancel.setVisibility(VISIBLE);
        }else{
            qq_img.setImageResource(R.drawable.umeng_socialize_qq);
            qq_name.setText("QQ");
            qq_cancel.setVisibility(GONE);
        }
    }

    private void thirdBind(SHARE_MEDIA shareMedia){
        startProgressDialog("授权中...");
        UMShareAPI.get(PersonalInformationActivity.this).getPlatformInfo(PersonalInformationActivity.this, shareMedia, new UMAuthListener(){
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
                    WeiXinLoginEntity weiXinLoginEntity=gson.fromJson(thirdLogin,WeiXinLoginEntity.class);
                    FileManagement.setWXLogin(weiXinLoginEntity);
                    thirdToTelBind("0");
                }else if(SHARE_MEDIA.QQ==share_media){
                    QQLoginEntity qqLoginEntity=gson.fromJson(thirdLogin,QQLoginEntity.class);
                    FileManagement.setQQLogin(qqLoginEntity);
                    thirdToTelBind("1");
                }
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                Log.d("LoginActivity", "onError 授权错误");
                stopProgressDialog();
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
                Log.d("LoginActivity", share_media.toString()+" onCancel 授权取消");
                stopProgressDialog();
            }
        });
    }

    private void thirdToTelBind(String type){
        final Map<String,String> requestMap=new HashMap<>();
        requestMap.put("type",type);
        requestMap.put("phoneNo",FileManagement.getLoginUserEntity().getMobileNumber());
        if(type.equals("0")){
            WeiXinLoginEntity loginEntity=FileManagement.getWXLogin();
            requestMap.put("nickname",loginEntity.getName());
            requestMap.put("accountId",loginEntity.getUid());
            requestMap.put("sex",loginEntity.getGender().endsWith("男")?"0":"1");
            requestMap.put("faceImg",loginEntity.getIconurl());
        }else{
            QQLoginEntity loginEntity=FileManagement.getQQLogin();
            requestMap.put("nickname",loginEntity.getName());
            requestMap.put("accountId",loginEntity.getUid());
            requestMap.put("sex",loginEntity.getGender().endsWith("男")?"0":"1");
            requestMap.put("faceImg",loginEntity.getIconurl());
        }
        XUtils.Get(Constants.HOST+"/linkThirdPartyAccount.action",requestMap,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                Log.e("result",result);
                try {
                    JSONObject jsonObject=new JSONObject(result);
                    if(jsonObject.get("resultCode").equals("0")){
                        updateOwner();
                    }else{
                        showShortToast(jsonObject.getString("msg"));
                    }
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

    private void thirdUnToTelBind(String type) {
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("type", type);
        if (type.equals("0")) {
            WeiXinLoginEntity loginEntity = FileManagement.getWXLogin();
            requestMap.put("accountId", loginEntity.getUid());
        } else {
            QQLoginEntity loginEntity = FileManagement.getQQLogin();
            requestMap.put("accountId", loginEntity.getUid());
        }
        XUtils.Get(Constants.HOST + "/deleteThirdPartyAccount.action", requestMap, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                Log.e("result",result);
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
                                    }
                                }
                            }
                        } else {
                            NetUtil.toNetworkSetting(PersonalInformationActivity.this);
                        }
                        initDialogView();
                    }
                });
    }

    @Override
    public void takeSuccess(TResult result) {
        TImage image = result.getImages().get(0);
        if (image != null) {
            tImages.add(image);
            String imagePath = image.getOriginalPath().replace("/external_storage_root", "");
            if (imagePath.indexOf(Environment.getExternalStorageDirectory() + "") == -1) {
                imagePath = Environment.getExternalStorageDirectory() + imagePath;
            }
            int old_orientation = getRotateDegree(imagePath);
            PhotoUtils.compressPicture(imagePath, imagePath);
            int new_orientation = getRotateDegree(imagePath);
            if(new_orientation!=old_orientation){
                setRotateDegree(imagePath,old_orientation);
            }
            RequestParams params=new RequestParams(Constants.HOST+Constants.EDITPERSONALINFORMATION);
            params.addBodyParameter("pic",new File(imagePath));
            editPersonalInfo(params, new OnEditSuccessLisener() {
                @Override
                public void onEditSuccess(PersonalInfomation personalInfomation) {
                    XUtilsImageUtils.display(iv_personal_pic,Constants.BASEHOST+personalInfomation.getFace(),ImageView.ScaleType.CENTER_INSIDE);
//                    XUtilsImageUtils.display(iv_personal_pic,Constants.BASEHOST+personalInfomation.getFace()+"?v="+new Date().getTime(),ImageView.ScaleType.CENTER_INSIDE);

                    EventBus.getDefault().post(new EventBusMessage<>("headerImg"));
                }
            });
        }

    }
    /** 从给定的路径加载图片，并指定是否自动旋转方向*/
    private void setRotateDegree(String imgpath, int orientation) {
        Bitmap bitmap=BitmapFactory.decodeFile(imgpath);
        // 旋转图片
        Matrix m = new Matrix();
        m.postRotate(orientation);
        Bitmap return_bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
        FileOutputStream out;
        try {
            out = new FileOutputStream(imgpath);
            return_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    /** 获取照片旋转方向*/
    private static int getRotateDegree(String path) {
        int result = 0;
        try {
            ExifInterface exif = new ExifInterface(path);
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    result = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    result = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    result = 270;
                    break;
            }
        } catch (IOException ignore) {
            return 0;
        }
        return result;
    }

    public interface OnEditSuccessLisener{
        void onEditSuccess(PersonalInfomation personalInfomation);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
