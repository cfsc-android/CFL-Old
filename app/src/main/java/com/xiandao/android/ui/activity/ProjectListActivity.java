package com.xiandao.android.ui.activity;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xiandao.android.R;
import com.xiandao.android.adapter.NoticeListAdapter;
import com.xiandao.android.adapter.ProjectListAdapter;
import com.xiandao.android.entity.CarManageEntity;
import com.xiandao.android.entity.NoticeEntity;
import com.xiandao.android.entity.ProjectInfo;
import com.xiandao.android.entity.eventbus.EventBusMessage;
import com.xiandao.android.http.MyCallBack;
import com.xiandao.android.http.XUtils;
import com.xiandao.android.ui.BaseActivity;
import com.xiandao.android.utils.Constants;
import com.xiandao.android.utils.FileManagement;
import com.xiandao.android.utils.MessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.event.annotation.ContentView;
import org.xutils.event.annotation.Event;
import org.xutils.event.annotation.ViewInject;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@ContentView(R.layout.activity_project_list)
public class ProjectListActivity extends BaseActivity {
    @ViewInject(R.id.tv_title_name)
    private TextView tv_title_name;
    @ViewInject(R.id.lv_project_list)
    private ListView lv_project_list;
    @ViewInject(R.id.ll_project_list_delete)
    private LinearLayout ll_project_list_delete;
    @ViewInject(R.id.ll_project_list_add)
    private LinearLayout ll_project_list_add;

    private ArrayList<ProjectInfo> projectInfos = new ArrayList<>();
    private ProjectListAdapter projectListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        tv_title_name.setText("选择小区");
        initProjectInfos();
        projectListAdapter=new ProjectListAdapter(this,projectInfos);
        lv_project_list.setAdapter(projectListAdapter);
        lv_project_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(projectListAdapter.isEdit()){
                    if(position>1){
                        projectListAdapter.checkItem(position);
                    }
                }else{
                    FileManagement.setProjectInfo(projectInfos.get(position));
                    EventBus.getDefault().post(new EventBusMessage<>("projectChange"));
                    finish();
                }
            }
        });
        lv_project_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                projectListAdapter.setEdit(true);
                ll_project_list_delete.setVisibility(View.VISIBLE);
                ll_project_list_add.setVisibility(View.GONE);
                return true;
            }
        });

    }

    private void initProjectInfos(){
        projectInfos.clear();
        XUtils.Get("http://dev.chanfine.com:9082/smartxd/api/selectProjectForApp.action",null,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                Log.e("result",result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if("0".equals(jsonObject.getString("resultCode"))) {
                        Gson gson=new Gson();
                        Type type = new TypeToken<List<ProjectInfo>>() {}.getType();
                        projectInfos.addAll((Collection<? extends ProjectInfo>) gson.fromJson(jsonObject.getString("data"),type));
                        ArrayList<ProjectInfo> list=FileManagement.getCustomerProjects();
                        if(list!=null){
                            projectInfos.addAll(FileManagement.getCustomerProjects());
                        }
                        projectListAdapter.notifyDataSetChanged();
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
        });

//        projectInfos.add(new ProjectInfo("长房 · 云西府",
//                "岳麓大道与麓谷大道交汇处东北角",
//                "http://dev.chanfine.com:9082/smartxd/",
////                "http://222.240.37.83:9082/smartxd/",
//                "",
//                "YXF"));
//        projectInfos.add(new ProjectInfo("长房 · 金阳府",
//                "浏阳市健寿大道与康万路交汇处西南角",
//                "http://dev.chanfine.com:9082/smartjy/",
////                "http://222.240.37.83:9082/smartxd/",
//                "",
//                "JYF"));
//        projectInfos.add(new ProjectInfo("长房 · 万楼公馆",
//                "湖南省湘潭市雨湖区潭州大道与护谭路交汇处",
//                "http://dev.chanfine.com:9082/smartwl/",
////                "http://222.240.37.83:9082/smartxd/",
//                "",
//                "WLGG"));
//        projectInfos.add(new ProjectInfo("长房 · 雍景湾",
//                "湖南省长沙市开福区盛世路",
//                "http://dev.chanfine.com:9082/smartyjw/",
////                "http://222.240.37.83:9082/smartxd/",
//                "",
//                "YJW"));
//        ArrayList<ProjectInfo> list=FileManagement.getCustomerProjects();
//        if(list!=null){
//            projectInfos.addAll(FileManagement.getCustomerProjects());
//        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(EventBusMessage message){
        if("customerProject".equals(message.getMessage())){
            initProjectInfos();
            projectListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Event({R.id.iv_title_left,R.id.btn_project_list_add,R.id.btn_project_list_cancel,R.id.btn_project_list_delete})
    private void onClickEvent(View v){
        switch (v.getId()){
            case R.id.iv_title_left:
                finish();
                break;
            case R.id.btn_project_list_add:
                openActivity(IpSettingActivity.class);
                break;
            case R.id.btn_project_list_cancel:
                projectListAdapter.clearCheckItem();
                projectListAdapter.setEdit(false);
                ll_project_list_delete.setVisibility(View.GONE);
                ll_project_list_add.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_project_list_delete:
                ArrayList<Integer> deleteList=projectListAdapter.getCheckList();
                if(deleteList.size()>0){
                    ArrayList<ProjectInfo> temp=new ArrayList<>();
                    for (int i = 0; i < projectInfos.size(); i++) {
                        if(!deleteList.contains(i)){
                            temp.add(projectInfos.get(i));
                        }
                    }
                    projectInfos.clear();
                    projectInfos.addAll(temp);
                    FileManagement.removeCustomerProject(deleteList);
                    projectListAdapter.clearCheckItem();
                    projectListAdapter.setEdit(false);
                    ll_project_list_delete.setVisibility(View.GONE);
                    ll_project_list_add.setVisibility(View.VISIBLE);
                }else{
                    Toast.makeText(ProjectListActivity.this,"请选择项目",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
