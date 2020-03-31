package com.xiandao.android.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.gson.reflect.TypeToken;
import com.xiandao.android.R;
import com.xiandao.android.adapter.smart.ProjectSelectAdapter;
import com.xiandao.android.entity.smart.BaseEntity;
import com.xiandao.android.entity.smart.CurrentDistrictEntity;
import com.xiandao.android.entity.smart.KeyTitleEntity;
import com.xiandao.android.entity.smart.ProjectTreeEntity;
import com.xiandao.android.http.JsonParse;
import com.xiandao.android.http.MyCallBack;
import com.xiandao.android.http.XUtils;
import com.xiandao.android.ui.BaseActivity;
import com.xiandao.android.utils.FileManagement;
import com.xiandao.android.view.RecyclerViewDivider;

import org.xutils.common.util.LogUtil;
import org.xutils.event.annotation.ContentView;
import org.xutils.event.annotation.Event;
import org.xutils.event.annotation.ViewInject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.xiandao.android.base.Config.BASE_URL;
import static com.xiandao.android.base.Config.BASIC;

@ContentView(R.layout.activity_unit_select)
public class UnitSelectActivity extends BaseActivity {
    @ViewInject(R.id.tv_title_name)
    private TextView tv_title_name;

    @ViewInject(R.id.unit_select_rlv)
    private RecyclerView unit_select_rlv;

    private ProjectSelectAdapter adapter;
    private List<KeyTitleEntity> data=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tv_title_name.setText("选择房屋");
        adapter=new ProjectSelectAdapter(this,data);
        unit_select_rlv.setLayoutManager(new LinearLayoutManager(this));
        unit_select_rlv.addItemDecoration(new RecyclerViewDivider(this, LinearLayoutManager.VERTICAL));
        unit_select_rlv.setAdapter(adapter);
        unit_select_rlv.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                String key=data.get(position).getKey();
                String title=data.get(position).getTitle();
                if(!TextUtils.isEmpty(key)){
                    Bundle bundle=new Bundle();
                    bundle.putString("unitId",key);
                    bundle.putString("title",title);
                    openActivity(RoomSelectActivity.class,bundle);
                }
            }
        });
        getProjectTree();
    }
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
                    getUnitListData(list);
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

    private void getUnitListData(List<ProjectTreeEntity> list){
        CurrentDistrictEntity currentDistrict=FileManagement.getUserInfoEntity().getCurrentDistrict();
        ProjectTreeEntity projectTree=null;
        for (int i = 0; i < list.size(); i++) {
            if(currentDistrict.getProjectId().equals(list.get(i).getKey())){
                projectTree=list.get(i);
            }
        }
        if(projectTree!=null){
            String projectTitle=projectTree.getTitle();
            for (int i = 0; i < projectTree.getChildren().size(); i++) {
                ProjectTreeEntity phase=projectTree.getChildren().get(i);
                data.add(new KeyTitleEntity(projectTitle+phase.getTitle(),""));
                String phaseTitle=phase.getTitle();
                for (int i1 = 0; i1 < phase.getChildren().size(); i1++) {
                    ProjectTreeEntity build=phase.getChildren().get(i1);
                    String buildTitle=build.getTitle();
                    for (int i2 = 0; i2 < build.getChildren().size(); i2++) {
                        ProjectTreeEntity unit=build.getChildren().get(i2);
                        String unitTitle=unit.getTitle();
                        data.add(new KeyTitleEntity(projectTitle+phaseTitle+buildTitle+unitTitle,unit.getKey()));
                    }
                }
            }

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

}
