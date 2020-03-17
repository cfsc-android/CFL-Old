package com.xiandao.android.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.andview.refreshview.utils.LogUtils;
import com.xiandao.android.R;
import com.xiandao.android.adapter.smart.WorkflowStepAdapter;
import com.xiandao.android.entity.smart.WorkflowProcessesEntity;
import com.xiandao.android.ui.BaseActivity;

import org.xutils.event.annotation.ContentView;
import org.xutils.event.annotation.Event;
import org.xutils.event.annotation.ViewInject;

import java.util.List;


@ContentView(R.layout.activity_workflow_step)
public class WorkflowStepActivity extends BaseActivity {
    @ViewInject(R.id.toolbar_tv_title)
    TextView toolbar_title;
    @ViewInject(R.id.workflow_step_rlv)
    private RecyclerView workflow_step_rlv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar_title.setText("进度");
        if(getIntent().getExtras().getSerializable("workflowProcessesList")!=null){
            List<WorkflowProcessesEntity> data = (List<WorkflowProcessesEntity>) getIntent().getExtras().getSerializable("workflowProcessesList");
            if(data!=null){
                workflow_step_rlv.setLayoutManager(new LinearLayoutManager(this));
                workflow_step_rlv.setAdapter(new WorkflowStepAdapter(this,data));
                LogUtils.d("data:"+data.size());
            }
        }
    }

    @Event({R.id.toolbar_btn_back})
    private void onClickEvent(View v){
        switch (v.getId()){
            case R.id.toolbar_btn_back:
                finish();
                break;
        }
    }
}
