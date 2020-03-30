package com.xiandao.android.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.andview.refreshview.utils.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.jaredrummler.materialspinner.MaterialSpinnerAdapter;
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
import com.xiandao.android.view.WheelDialog;
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
 * 此类描述的是:报修activity
 *
 * @author TanYong
 *         create at 2017/5/6 14:37
 */
@ContentView(R.layout.activity_repairs)
public class RepairsActivity extends BaseActivity {
    private static final String[] permission={Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    private static final int REQUEST_CODE_CHOOSE=0x001;

    @ViewInject(R.id.toolbar_tv_title)
    private TextView toolbar_title;
    @ViewInject(R.id.add_order_et_address)
    private EditText add_order_et_address;
    @ViewInject(R.id.add_order_et_contact)
    private EditText add_order_et_contact;
    @ViewInject(R.id.add_order_et_contact_tel)
    private EditText add_order_et_contact_tel;
    @ViewInject(R.id.add_order_et_plain_time)
    private EditText add_order_et_plain_time;
    @ViewInject(R.id.add_order_et_remark)
    private EditText add_order_et_remark;
    @ViewInject(R.id.add_order_rlv_pic)
    private RecyclerView add_order_rlv_pic;
    @ViewInject(R.id.add_order_ms_project_type)
    private MaterialSpinner add_order_ms_project_type;
    @ViewInject(R.id.add_order_ms_problem_type)
    private MaterialSpinner add_order_ms_problem_type;

    private List<ImageViewInfo> dataList=new ArrayList<>();
    private GridLayoutManager mGridLayoutManager;
    private ImagePreviewListAdapter adapter;
    private boolean permissionFlag;
    private List<OrderTypeEntity> orderTypeEntityList;
    private List<String> problemData=new ArrayList<>();
    private List<OrderTypeEntity> problemTypeData=new ArrayList<>();
    private List<String> projectData=new ArrayList<>();
    private List<OrderTypeEntity> projectTypeData=new ArrayList<>();
    private int problemValue,projectValue;
    private String resourceKey;
    private MaterialSpinnerAdapter projectDataAdapter;
    private WheelDialog wheeldialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar_title.setText("创建工单");
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
        add_order_rlv_pic.setLayoutManager(mGridLayoutManager = new GridLayoutManager(this,4));
        adapter=new ImagePreviewListAdapter(this, R.layout.item_workflow_image_perview_list,dataList);
        add_order_rlv_pic.setAdapter(adapter);
        add_order_rlv_pic.addOnItemTouchListener(new com.chad.library.adapter.base.listener.OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                if(position==dataList.size()-1){
                    if(permissionFlag){
                        PhotoPicker.pick(RepairsActivity.this,10,true,REQUEST_CODE_CHOOSE);
                    }else{
                        showToast("相机或读写手机存储的权限被禁止！");
                    }
                }else{
                    List<ImageViewInfo> data=new ArrayList<>();
                    for (int i = 0; i < dataList.size()-1; i++) {
                        data.add(dataList.get(i));
                    }
                    computeBoundsBackward(mGridLayoutManager.findFirstVisibleItemPosition());
                    PreviewBuilder.from(RepairsActivity.this)
                            .setImgs(data)
                            .setCurrentIndex(position)
                            .setSingleFling(true)
                            .setType(PreviewBuilder.IndicatorType.Number)
                            .start();
                }
            }
        });


        orderTypeEntityList= FileManagement.getOrderType();
        initProblemSpinner();
        resourceKey= UUID.randomUUID().toString().replaceAll("-","");
        add_order_et_address.setText(FileManagement.getUserInfoEntity().getAncestor());
        add_order_et_contact.setText(FileManagement.getUserInfoEntity().getName());
        add_order_et_contact_tel.setText(FileManagement.getUserInfoEntity().getMobile());

    }

    private void initProblemSpinner(){
        for (int i = 0; i < orderTypeEntityList.size(); i++) {
            if(orderTypeEntityList.get(i).getParentId()==0){
                problemData.add(orderTypeEntityList.get(i).getName());
                problemTypeData.add(orderTypeEntityList.get(i));
            }
        }
        problemValue=problemTypeData.get(0).getId();
        add_order_ms_problem_type.setItems(problemData);
        add_order_ms_problem_type.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                LogUtils.d(position+"_"+id+"_"+item.toString());
                problemValue=problemTypeData.get(position).getId();
                initProjectSpinner();
            }
        });
        initProjectSpinner();
    }

    private void initProjectSpinner(){
        projectData.clear();
        projectTypeData.clear();
        for (int i = 0; i < orderTypeEntityList.size(); i++) {
            if(orderTypeEntityList.get(i).getParentId()==problemValue){
                projectData.add(orderTypeEntityList.get(i).getName());
                projectTypeData.add(orderTypeEntityList.get(i));
                projectValue=orderTypeEntityList.get(i).getId();
            }
        }
        projectValue=projectTypeData.get(0).getId();
        if(projectDataAdapter==null){
            projectDataAdapter=new MaterialSpinnerAdapter(this,projectData);
            add_order_ms_project_type.setAdapter(projectDataAdapter);
            add_order_ms_project_type.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
                @Override
                public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                    LogUtils.d(position+"_"+id+"_"+item.toString());
                    projectValue=projectTypeData.get(position).getId();
                }
            });
        }else{
            projectDataAdapter.notifyDataSetChanged();
            add_order_ms_project_type.setSelectedIndex(0);
        }

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

    @Event(type = View.OnTouchListener.class,value ={R.id.add_order_et_plain_time})
    private boolean onTouchEvent(View v, MotionEvent event){
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            switch (v.getId()) {
                case R.id.add_order_et_plain_time:
                    wheeldialog = new WheelDialog(this, R.style.Dialog_Floating, new WheelDialog.OnDateTimeConfirm() {
                        @Override
                        public void returnData(String dateText, String dateValue) {
                            wheeldialog.cancel();
                            add_order_et_plain_time.setText(dateText);
                        }
                    });
                    wheeldialog.show();
                    break;
            }
        }
        return false;
    }


    @Event({R.id.toolbar_btn_back, R.id.login_btn_login})
    private void onClickEvent(View v){
        switch (v.getId()){
            case R.id.toolbar_btn_back:
                finish();
                break;
            case R.id.login_btn_login:
                addOrderSubmit();
                break;
        }
    }

    private void addOrderSubmit(){
        if(TextUtils.isEmpty(add_order_et_address.getText())){
            showToast("请输入维修地点");
            return;
        }
        if(TextUtils.isEmpty(add_order_et_contact.getText())){
            showToast("请输入联系人");
            return;
        }
        if(TextUtils.isEmpty(add_order_et_contact_tel.getText())){
            showToast("请输入联系电话");
            return;
        }
        if(TextUtils.isEmpty(add_order_et_remark.getText())){
            showToast("请输入问题描述");
            return;
        }
        startProgressDialog("");
        Map<String,Object> requestMap=new HashMap<>();
        requestMap.put("address",add_order_et_address.getText().toString());
        requestMap.put("createType", UserType.Household.getType());
        requestMap.put("expectTime",add_order_et_plain_time.getText().toString()+":00");
        requestMap.put("householdId",FileManagement.getUserInfoEntity().getId());
        requestMap.put("linkMan",add_order_et_contact.getText().toString());
        requestMap.put("mobile",add_order_et_contact_tel.getText().toString());
        requestMap.put("problemDesc",add_order_et_remark.getText().toString());
        requestMap.put("projectId",FileManagement.getUserInfoEntity().getRoomList().get(0).getProjectId());
        requestMap.put("reportType",UserType.Household.getType());
        requestMap.put("roomId",FileManagement.getUserInfoEntity().getRoomList().get(0).getId());
        requestMap.put("typeId",projectValue);
        if(dataList.size()>1)
            requestMap.put("problemResourceKey",resourceKey);
        XUtils.PostJson(BASE_URL+WORKORDER+"workflow/api/workorder",requestMap,new MyCallBack<String>(){
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
                        bundle.putSerializable("workflowType", WorkflowType.Order);
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
                LogUtils.d(ex.toString());
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
