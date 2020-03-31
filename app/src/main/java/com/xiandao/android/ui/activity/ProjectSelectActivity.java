package com.xiandao.android.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.gson.reflect.TypeToken;
import com.xiandao.android.R;
import com.xiandao.android.adapter.smart.ProjectSelectAdapter;
import com.xiandao.android.entity.eventbus.EventBusMessage;
import com.xiandao.android.entity.smart.BaseEntity;
import com.xiandao.android.entity.smart.KeyTitleEntity;
import com.xiandao.android.entity.smart.ProjectTreeEntity;
import com.xiandao.android.entity.smart.UserInfoEntity;
import com.xiandao.android.http.JsonParse;
import com.xiandao.android.http.MyCallBack;
import com.xiandao.android.http.XUtils;
import com.xiandao.android.ui.BaseActivity;
import com.xiandao.android.utils.FileManagement;
import com.xiandao.android.view.RecyclerViewDivider;

import org.greenrobot.eventbus.EventBus;
import org.xutils.LogUtils;
import org.xutils.app.LynActivityManager;
import org.xutils.common.util.LogUtil;
import org.xutils.event.annotation.ContentView;
import org.xutils.event.annotation.Event;
import org.xutils.event.annotation.ViewInject;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.xiandao.android.base.Config.BASE_URL;
import static com.xiandao.android.base.Config.BASIC;

@ContentView(R.layout.activity_project_select)
public class ProjectSelectActivity extends BaseActivity {
    @ViewInject(R.id.iv_title_left)
    private ImageView iv_title_left;
    @ViewInject(R.id.tv_title_name)
    private TextView tv_title_name;

    @ViewInject(R.id.project_select_rlv)
    private RecyclerView project_select_rlv;

    private ProjectSelectAdapter adapter;
    private List<KeyTitleEntity> data=new ArrayList<>();

    private String openFrom="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tv_title_name.setText("选择社区");
        openFrom=getIntent().getExtras().getString("openFrom");
        if("Login".equals(openFrom)||"Register".equals(openFrom)){
            iv_title_left.setVisibility(View.INVISIBLE);
        }
        adapter=new ProjectSelectAdapter(this,data);
        project_select_rlv.setLayoutManager(new LinearLayoutManager(this));
        project_select_rlv.addItemDecoration(new RecyclerViewDivider(this, LinearLayoutManager.VERTICAL));
        project_select_rlv.setAdapter(adapter);
        project_select_rlv.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                if(!TextUtils.isEmpty(data.get(position).getKey())){
                    bindProject(data.get(position).getKey());
                }
            }
        });
        getProjectTree();
    }

    private void bindProject(String projectId){
        startProgressDialog("");
        Map<String,Object> map=new HashMap<>();
        map.put("projectId",projectId);
        map.put("householdId", FileManagement.getUserInfoEntity().getId());
        XUtils.PostJson(BASE_URL+BASIC+"basic/current/bind",map,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtil.d(result);
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
                stopProgressDialog();
            }


        });
    }

    //获取项目树结构
    private void getProjectTree(){
        XUtils.Get(BASE_URL+BASIC+"basic/project/tree",null,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtil.d(result);
                BaseEntity baseEntity= JsonParse.parse(result);
                if(baseEntity.isSuccess()){
                    Type type = new TypeToken<List<ProjectTreeEntity>>() {}.getType();
                    List<ProjectTreeEntity> list= (List<ProjectTreeEntity>) JsonParse.parseList(result,type);
                    getProjectListData(list);
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
    
    private void getProjectListData(List<ProjectTreeEntity> list){
        data.add(new KeyTitleEntity("全部社区",""));
        for (int i = 0; i < list.size(); i++) {
            data.add(new KeyTitleEntity(list.get(i).getTitle(),list.get(i).getKey()));
        }
        adapter.notifyDataSetChanged();
    }


    @Event({R.id.iv_title_left})
    private void onClickEvent(View v){
        switch (v.getId()){
            case R.id.iv_title_left:
                finish();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 当按下返回键时所执行的命令
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if("Login".equals(openFrom)||"Register".equals(openFrom)){
                showToast("请选择项目");
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    //获取用户信息
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
                    if(LynActivityManager.getInstance().getActivityByClass(HouseManageActivity.class)!=null){
                        LynActivityManager.getInstance().finishActivity(HouseManageActivity.class);
                        LynActivityManager.getInstance().finishActivity(HouseHoldActivity.class);
                        EventBus.getDefault().post(new EventBusMessage<>("projectSelect"));
                        finish();
                    }else{
                        openActivity(MainActivity.class);
                    }
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

}
