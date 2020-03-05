package com.xiandao.android.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;
import com.xiandao.android.R;
import com.xiandao.android.adapter.AddImageGridViewAdapter;
import com.xiandao.android.adapter.NewAddImageGridViewAdapter;
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

import java.io.File;
import java.util.ArrayList;

/**
 * 此类描述的是:邻里发帖activity
 *
 * @author TanYong
 *         create at 2017/6/1 19:16
 */
@ContentView(R.layout.activity_post)
public class PostActivity extends BaseTakePhotoActivity implements View.OnClickListener {

    @ViewInject(R.id.iv_title_left)
    private ImageView iv_title_left;
    @ViewInject(R.id.tv_title_name)
    private TextView tvTitleName;
    @ViewInject(R.id.et_post_content)
    private EditText et_post_content;
    @ViewInject(R.id.btn_post)
    private TextView btn_post;
    @ViewInject(R.id.gv_add_image)
    private GridView gv_add_image;
    private ArrayList<TImage> tImages = new ArrayList<>();// 添加图片集合
    private ArrayList<File> files = new ArrayList<>();
    private NewAddImageGridViewAdapter addImageGridViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        tvTitleName.setText("发帖");
        iv_title_left.setOnClickListener(this);
        btn_post.setOnClickListener(this);
        addImageGridViewAdapter = new NewAddImageGridViewAdapter(this, tImages, gv_add_image);
        gv_add_image.setAdapter(addImageGridViewAdapter);
        gv_add_image.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
                            new String[]{"拍照", "从相册中选择"}, PostActivity.this,
                            AlertView.Style.ActionSheet, new OnItemClickListener() {
                        @Override
                        public void onItemClick(Object o, int position) {
                            if (position == 0) {
                                takePhoto.onPickFromCapture(imageUri);
                            } else if (position == 1) {
                                takePhoto.onPickMultiple(0 - tImages.size());
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_title_left:
                finish();
                break;
            case R.id.btn_post:
                if (Tools.isEmpty(et_post_content.getText().toString())) {
                    showLongToast("发帖内容不能为空");
                } else {
                    postCommit();
                }
                break;
            default:
                break;
        }
    }

    /**
     * @author TanYong
     * create at 2017/5/20 10:21
     * TODO：发帖
     */
    private void postCommit() {
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

        UploadPhotoTask postCommitTask = new UploadPhotoTask(new Handler(Looper.getMainLooper()) {
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
                                    null, new String[]{"知道了"}, null, PostActivity.this, AlertView.Style.Alert, new OnItemClickListener() {
                                @Override
                                public void onItemClick(Object o, int position) {
                                    Intent intent = new Intent();
                                    intent.putExtra("succese", true);
                                    PostActivity.this.setResult(Constants.POST_CODE, intent);
                                    PostActivity.this.finish();
                                }
                            }).setCancelable(false).show();
                        } else {
                            showShortToast(task.getMsg());
                        }
                        break;
                    case -1:
                        showShortToast("发帖提交失败");
                        break;
                    default:
                        NetUtil.toNetworkSetting(PostActivity.this);
                        break;
                }
            }
        }, 0);
        postCommitTask.sendRequest(Constants.HOST + Constants.LINLIPOST,
                new String[]{"appTokenInfo", "publishId", "body"},
                new String[]{FileManagement.getTokenInfo(),
                        loginUserEntity.getUserId(), et_post_content.getText().toString()
                },
                files);
    }

    @Override
    public void takeSuccess(TResult result) {
        ArrayList<TImage> images = result.getImages();
        if (images != null && images.size() > 0) {
            tImages.addAll(images);
            Tools.setGridViewHeightBasedOnChildren(gv_add_image);
            addImageGridViewAdapter.notifyDataSetChanged();

            for (TImage image : images) {
                String imagePath = image.getOriginalPath().replace("/external_storage_root", "");
                if (imagePath.indexOf(Environment.getExternalStorageDirectory() + "") == -1) {
                    imagePath = Environment.getExternalStorageDirectory() + imagePath;
                }
                PhotoUtils.compressPicture(imagePath, imagePath);
            }
        }
    }
}
