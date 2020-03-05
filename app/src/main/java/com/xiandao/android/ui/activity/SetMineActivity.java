package com.xiandao.android.ui.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;
import com.xiandao.android.AndroidApplication;
import com.xiandao.android.R;
import com.xiandao.android.entity.PersonalInfomation;
import com.xiandao.android.httptask.CommitPersonalDataTask;
import com.xiandao.android.httptask.UploadPhotoTask;
import com.xiandao.android.net.NetUtil;
import com.xiandao.android.ui.BaseTakePhotoActivity;
import com.xiandao.android.utils.Constants;
import com.xiandao.android.utils.FileManagement;
import com.xiandao.android.utils.PhotoUtils;
import com.xiandao.android.utils.Tools;
import com.xiandao.android.view.alertview.AlertView;
import com.xiandao.android.view.alertview.OnItemClickListener;

import org.xutils.event.annotation.ContentView;
import org.xutils.event.annotation.ViewInject;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import cn.jpush.android.api.JPushInterface;


/**
 * 此类描述的是:编辑我的资料fragment
 *
 * @author TanYong
 *         create at 2017/5/18 9:53
 */
@ContentView(R.layout.activity_set_mine)
public class SetMineActivity extends BaseTakePhotoActivity implements View.OnClickListener {
    @ViewInject(R.id.iv_title_left)
    private ImageView ivTitleLeft;
    @ViewInject(R.id.tv_title_name)
    private TextView ivTitleName;

    @ViewInject(R.id.iv_select_photo)
    private ImageView ivSelectPhoto;
    @ViewInject(R.id.tv_select_linkman)
    private TextView etSelectLinkman;
    @ViewInject(R.id.tv_common_address)
    private TextView tv_common_address;
    @ViewInject(R.id.btn_setmine_ok)
    private Button btnSetmineOk;
    @ViewInject(R.id.btn_out)
    private Button btn_out;
    @ViewInject(R.id.et_nick_name)
    private EditText et_nick_name;

    private ArrayList<TImage> tImages = new ArrayList<>();// 添加图片集合
    private ArrayList<File> files = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        ivTitleName.setText("编辑我的资料");
        ivTitleLeft.setOnClickListener(this);
        ivSelectPhoto.setOnClickListener(this);
        tv_common_address.setOnClickListener(this);
        etSelectLinkman.setOnClickListener(this);
        btnSetmineOk.setOnClickListener(this);
        btn_out.setOnClickListener(this);
        PhotoUtils.loadRoundImage(loginUserEntity.getHeadImageUrl(), ivSelectPhoto, 200, 1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_title_left:
                finish();
                break;
            case R.id.iv_select_photo:
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
                        new String[]{"拍照", "从相册中选择"}, SetMineActivity.this,
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
                break;
            case R.id.tv_common_address:
                openActivity(SelectCommonAddressActivity.class);
                break;
            case R.id.tv_select_linkman:
                openActivity(SelectLinkmanActivity.class);
                break;
            case R.id.btn_setmine_ok:
                commit();
                break;
            case R.id.btn_out:
                exit();
                break;
            default:
                break;
        }
    }

    private void exit() {
        new AlertView("温馨提示", "确定退出登录？",
                "取消", new String[]{"确认"}, null, this, AlertView.Style.Alert, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                if (position == 0) {
                    logout();
                }
            }
        }).setCancelable(false).show();
    }

    /**
     * @author TanYong
     * create at 2017/4/21 15:57
     * TODO：退出登录
     */
    private void logout() {
        FileManagement.saveTokenInfo("");
        FileManagement.setHikToken("");
        FileManagement.saveJpushAlias("");
        FileManagement.saveJpushTags("");
        JPushInterface.setAliasAndTags(getApplicationContext(), "", null, null);
        openActivity(LoginActivity.class);
        AndroidApplication.getInstance().exitApp();
    }

    private void commit() {
        String nickName = et_nick_name.getText().toString();
        if (files != null && files.size() > 0) {
            files.clear();
        }
        startProgressDialog("");
        for (TImage image : tImages) {
            String imagePath = image.getOriginalPath().replace("/external_storage_root", "");
            if (imagePath.indexOf(Environment.getExternalStorageDirectory() + "") == -1) {
                imagePath = Environment.getExternalStorageDirectory() + imagePath;
            }
            files.add(new File(imagePath));
        }
        CommitPersonalDataTask repairsInfoCommitTask = new CommitPersonalDataTask(new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                stopProgressDialog();
                CommitPersonalDataTask task = (CommitPersonalDataTask) msg.obj;
                if (task == null) {
                    return;
                }
                switch (task.isDataExist()) {
                    case 0:
                        if (task.getResultCode() != null && task.getResultCode().equals("0")) {
                            showShortToast(task.getMsg());
                            PersonalInfomation data = task.getData();
                            if (null != data) {
                                loginUserEntity.setNickName(Tools.getStringValue(data.getNickname()));
                                loginUserEntity.setHeadImageUrl(Tools.getStringValue(data.getFace()));
                                FileManagement.setBaseUser(loginUserEntity);
                            }
                            SetMineActivity.this.setResult(100);
                            SetMineActivity.this.finish();
                        } else {
                            showShortToast(task.getMsg());
                        }
                        break;
                    case -1:
                        Toast.makeText(SetMineActivity.this, "个人信息提交失败", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        NetUtil.toNetworkSetting(SetMineActivity.this);
                        break;
                }
            }
        }, 0);
//        repairsInfoCommitTask.sendRequest(Constants.HOST + Constants.EDITPERSONALINFORMATION,
//                new String[]{"appTokenInfo", "appMobile", "ownerid", "nickname"},
//                new String[]{FileManagement.getTokenInfo(), loginUserEntity.getMobileNumber(), loginUserEntity.getUserId(), nickName},files);
        repairsInfoCommitTask.sendRequest(Constants.HOST + Constants.EDITPERSONALINFORMATION,
                new String[]{"ownerid", "nickname"},
                new String[]{loginUserEntity.getUserId(), nickName},null);
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
            Glide.with(this).load(new File(imagePath)).into(ivSelectPhoto);
            int old_orientation = getRotateDegree(imagePath);
            PhotoUtils.compressPicture(imagePath, imagePath);
            int new_orientation = getRotateDegree(imagePath);
            if(new_orientation!=old_orientation){
                setRotateDegree(imagePath,old_orientation);
            }
            getRotateDegree(imagePath);
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
}
