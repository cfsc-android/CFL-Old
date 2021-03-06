package com.xiandao.android.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.andview.refreshview.utils.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.xiandao.android.R;
import com.xiandao.android.entity.eventbus.EventBusMessage;
import com.xiandao.android.entity.smart.BaseEntity;
import com.xiandao.android.entity.smart.OrderTypeEntity;
import com.xiandao.android.entity.smart.UserType;
import com.xiandao.android.entity.smart.WorkflowType;
import com.xiandao.android.http.JsonParse;
import com.xiandao.android.http.MyCallBack;
import com.xiandao.android.http.XUtils;
import com.xiandao.android.ui.BaseActivity;
import com.xiandao.android.utils.FileManagement;
import com.xiandao.android.utils.FilePathUtil;
import com.xiandao.android.utils.PermissionsUtils;
import com.xiandao.android.view.imagepreview.ImagePreviewListAdapter;
import com.xiandao.android.view.imagepreview.ImageViewInfo;
import com.xiandao.android.view.imagepreview.PreviewBuilder;
import com.xiandao.android.view.photopicker.PhotoPicker;
import com.zhihu.matisse.Matisse;

import org.greenrobot.eventbus.EventBus;
import org.xutils.app.LynActivityManager;
import org.xutils.common.util.LogUtil;
import org.xutils.event.annotation.ContentView;
import org.xutils.event.annotation.Event;
import org.xutils.event.annotation.ViewInject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import androidx.annotation.NonNull;
import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

import static com.xiandao.android.base.Config.BASE_URL;
import static com.xiandao.android.base.Config.FILE;
import static com.xiandao.android.base.Config.PHOTO_DIR_NAME;
import static com.xiandao.android.base.Config.SD_APP_DIR_NAME;
import static com.xiandao.android.base.Config.WORKORDER;


/**
 * 此类描述的是:投诉activity
 *
 * @author TanYong
 *         create at 2017/6/1 15:09
 */
@ContentView(R.layout.activity_complain)
public class ComplainActivity extends BaseActivity {
    private static final String[] permission={Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    private static final int REQUEST_CODE_CHOOSE=0x001;

    @ViewInject(R.id.toolbar_tv_title)
    TextView toolbar_title;
    @ViewInject(R.id.add_complain_et_remark)
    private EditText add_complain_et_remark;
    @ViewInject(R.id.add_complain_rlv_pic)
    private RecyclerView add_complain_rlv_pic;
    @ViewInject(R.id.add_complain_ms_complain_type)
    private MaterialSpinner add_complain_ms_complain_type;


    private List<ImageViewInfo> dataList=new ArrayList<>();
    private GridLayoutManager mGridLayoutManager;
    private ImagePreviewListAdapter adapter;
    private boolean permissionFlag;
    private List<OrderTypeEntity> complainTypeEntityList;
    private List<String> complainTypeData=new ArrayList<>();
    private int complainTypeValue;
    private String resourceKey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar_title.setText("创建投诉");
        //默认不弹出软键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

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

        dataList.add(new ImageViewInfo("plus"));
        add_complain_rlv_pic.setLayoutManager(mGridLayoutManager = new GridLayoutManager(this,4));
        adapter=new ImagePreviewListAdapter(this,R.layout.item_workflow_image_perview_list,dataList);
        add_complain_rlv_pic.setAdapter(adapter);
        add_complain_rlv_pic.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                if(position==dataList.size()-1){
                    if(permissionFlag){
                        PhotoPicker.pick(ComplainActivity.this,10,true,REQUEST_CODE_CHOOSE);
                    }else{
                        showToast("相机或读写手机存储的权限被禁止！");
                    }
                }else{
                    List<ImageViewInfo> data=new ArrayList<>();
                    for (int i = 0; i < dataList.size()-1; i++) {
                        data.add(dataList.get(i));
                    }
                    computeBoundsBackward(mGridLayoutManager.findFirstVisibleItemPosition());
                    PreviewBuilder.from(ComplainActivity.this)
                            .setImgs(data)
                            .setCurrentIndex(position)
                            .setSingleFling(true)
                            .setType(PreviewBuilder.IndicatorType.Number)
                            .start();
                }
            }
        });
        initSpinner();
        resourceKey= UUID.randomUUID().toString().replaceAll("-","");
    }

    private void initSpinner(){
        complainTypeEntityList= FileManagement.getComplainType();
        for (int i = 0; i < complainTypeEntityList.size(); i++) {
            complainTypeData.add(complainTypeEntityList.get(i).getName());
        }
        complainTypeValue= complainTypeEntityList.get(0).getId();
        add_complain_ms_complain_type.setItems(complainTypeData);
        add_complain_ms_complain_type.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                complainTypeValue=complainTypeEntityList.get(position).getId();
            }
        });
    }
    //计算返回的边界
    private void computeBoundsBackward(int firstCompletelyVisiblePos) {
        for (int i = firstCompletelyVisiblePos; i < adapter.getItemCount(); i++) {
            View itemView = mGridLayoutManager.findViewByPosition(i);
            Rect bounds = new Rect();
            if (itemView != null) {
                ImageView imageView = itemView.findViewById(R.id.iiv_item_image_preview);
                imageView.getGlobalVisibleRect(bounds);
            }
            adapter.getItem(i).setBounds(bounds);
        }
    }

    @Event({R.id.toolbar_btn_back,R.id.complain_btn_submit})
    private void onClickEvent(View v){
        switch (v.getId()){
            case R.id.toolbar_btn_back:
                finish();
                break;
            case R.id.complain_btn_submit:

                addComplainSubmit();
                break;
        }
    }

    private void addComplainSubmit(){
        if(TextUtils.isEmpty(add_complain_et_remark.getText())){
            showToast("请输入投诉内容");
            return;
        }
        Map<String,Object> map=new HashMap<>();
        map.put("createType",UserType.Household.getType());
        map.put("householdId",FileManagement.getUserInfoEntity().getId());
        map.put("problemDesc",add_complain_et_remark.getText().toString());
        map.put("projectId",FileManagement.getUserInfoEntity().getRoomList().get(0).getProjectId());
        map.put("reportType", UserType.Household.getType());
        map.put("roomId",FileManagement.getUserInfoEntity().getRoomList().get(0).getId());
        map.put("typeId",complainTypeValue);
        if(dataList.size()>1)
            map.put("problemResourceKey",resourceKey);
        XUtils.PostJson(BASE_URL+WORKORDER+"workflow/api/complaint",map,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtils.d(result);
                BaseEntity baseEntity= JsonParse.parse(result);
                if(baseEntity.isSuccess()){
                    if(LynActivityManager.getInstance().getActivityByClass(WorkflowListActivity.class)!=null){
                        EventBus.getDefault().post(new EventBusMessage<>("WorkListRefresh"));
                    }else{
                        Bundle bundle=new Bundle();
                        bundle.putSerializable("workflowType", WorkflowType.Complain);
                        openActivity(WorkflowListActivity.class,bundle);
                    }
                    finish();

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
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionsUtils.getInstance().onRequestPermissionsResult(this,requestCode,permissions,grantResults);
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
//                        mUploadPic(file.getAbsolutePath());
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                }).launch();
    }

    //上传照片
    private void uploadPic(final String path){
        Map<String,String> requestMap=new HashMap<>();
        requestMap.put("resourceKey",resourceKey);
        Map<String,File> fileMap=new HashMap<>();
        fileMap.put("UploadFile",new File(path));
        XUtils.UpLoadFile(BASE_URL+FILE+"files-anon", requestMap,fileMap,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtils.d(result);
                dataList.add(dataList.size()-1,new ImageViewInfo(path));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                showToast(ex.getMessage());
            }


        });
    }
}
