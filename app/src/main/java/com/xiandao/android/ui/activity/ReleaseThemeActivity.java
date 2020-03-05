package com.xiandao.android.ui.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;
import com.xiandao.android.R;
import com.xiandao.android.adapter.NewAddImageGridViewAdapter;
import com.xiandao.android.entity.LoginUserEntity;
import com.xiandao.android.httptask.UploadPhotoTask;
import com.xiandao.android.net.NetUtil;
import com.xiandao.android.ui.BaseActivity;
import com.xiandao.android.ui.BaseTakePhotoActivity;
import com.xiandao.android.utils.Constants;
import com.xiandao.android.utils.FileManagement;
import com.xiandao.android.utils.PhotoUtils;
import com.xiandao.android.utils.Tools;
import com.xiandao.android.view.alertview.AlertView;
import com.xiandao.android.view.alertview.OnItemClickListener;

import org.xutils.event.annotation.ContentView;
import org.xutils.event.annotation.ViewInject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

@ContentView(R.layout.activity_release_theme)
public class ReleaseThemeActivity extends BaseTakePhotoActivity implements View.OnClickListener {
    @ViewInject(R.id.iv_title_left)
    private ImageView ivTitleLeft;
    @ViewInject(R.id.tv_title_name)
    private TextView tvTitleName;
    @ViewInject(R.id.et_release_theme_content)
    private EditText et_release_theme_content;
    @ViewInject(R.id.gv_release_add_image)
    private GridView gv_release_add_image;
    @ViewInject(R.id.btn_release_commit)
    private Button btn_release_commit;

    private LoginUserEntity userEntity;
    private String content;


    private ArrayList<TImage> tImages = new ArrayList<>();// 添加图片集合
    private ArrayList<File> files = new ArrayList<>();
    private NewAddImageGridViewAdapter addImageGridViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userEntity = FileManagement.getLoginUserEntity();
        init();
    }

    private void init() {
        tvTitleName.setText("发新鲜事");
        ivTitleLeft.setOnClickListener(this);

        addImageGridViewAdapter = new NewAddImageGridViewAdapter(this, tImages, gv_release_add_image);
        gv_release_add_image.setAdapter(addImageGridViewAdapter);
        gv_release_add_image.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (tImages.size() != addImageGridViewAdapter.getMaxImages() && position == tImages.size()) {//点击添加图片
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘

                    getTakePhoto();
                    File file = new File(Environment.getExternalStorageDirectory(), "/xiandao/" + System.currentTimeMillis() + ".jpg");
                    if (!file.getParentFile().exists()) {
                        file.getParentFile().mkdirs();
                    }
                    final Uri imageUri = Uri.fromFile(file);
                    configCompress(takePhoto);
                    configTakePhotoOption(takePhoto);

                    new AlertView("选择获取图片方式", null, "取消", null,
                            new String[]{"拍照", "从相册中选择"}, ReleaseThemeActivity.this,
                            AlertView.Style.ActionSheet, new OnItemClickListener() {
                        @Override
                        public void onItemClick(Object o, int position) {
                            if (position == 0) {
                                takePhoto.onPickFromCapture(imageUri);
                            } else if (position == 1) {
                                takePhoto.onPickMultiple(3 - tImages.size());
                            }
                        }
                    }).show();
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putInt("position", position);
                    bundle.putSerializable("tImages", tImages);
                    openActivity(ShowAddImageActivity.class, bundle);
                }
            }
        });
        btn_release_commit.setOnClickListener(this);
    }

    /**
     * 获取TakePhoto实例
     *
     * @return
     */
    public TakePhoto getTakePhoto() {
        if (takePhoto == null) {
            takePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));
        }
        return takePhoto;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_title_left:
                finish();
                break;
            case R.id.btn_release_commit:
                content = et_release_theme_content.getText().toString();
                String regex = "(草|操|哈哈)";
                content=content.replaceAll(regex, "**");
                if (Tools.isEmpty(content)) {
                    showShortToast("新鲜事内容不能为空");
                } else {
                    releaseCommit();
                }
                break;
        }
    }

    private void releaseCommit() {
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
        UploadPhotoTask repairsInfoCommitTask = new UploadPhotoTask(new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                stopProgressDialog();
                UploadPhotoTask task = (UploadPhotoTask) msg.obj;
                if (task == null) {
                    return;
                }
                switch (task.isDataExist()) {
                    case 0:
                        if (task.getResultCode() != null && task.getResultCode().equals("0")) {
                            new AlertView("温馨提示", task.getMsg(),
                                    null, new String[]{"知道了"}, null, ReleaseThemeActivity.this, AlertView.Style.Alert, new OnItemClickListener() {
                                @Override
                                public void onItemClick(Object o, int position) {
                                    ReleaseThemeActivity.this.finish();
                                }
                            }).setCancelable(false).show();
                        } else {
                            showShortToast(task.getMsg());
                        }
                        break;
                    case -1:
                        showShortToast("报修信息提交失败");
                        break;
                    default:
                        NetUtil.toNetworkSetting(ReleaseThemeActivity.this);
                        break;
                }
            }
        }, 0);
        repairsInfoCommitTask.sendRequest(Constants.HOST + Constants.RELEASETHEME,
                new String[]{"appTokenInfo", "ownerid", "content"},
                new String[]{FileManagement.getTokenInfo(), loginUserEntity.getUserId(), content},
                files);
    }

    @Override
    public void takeSuccess(TResult result) {
        ArrayList<TImage> images = result.getImages();
        if (images != null && images.size() > 0) {
            tImages.addAll(images);
            Tools.setGridViewHeightBasedOnChildren(gv_release_add_image);
            addImageGridViewAdapter.notifyDataSetChanged();

            for (TImage image : images) {
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
            }
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
        Log.e("----result",result+"");
        return result;
    }

}
