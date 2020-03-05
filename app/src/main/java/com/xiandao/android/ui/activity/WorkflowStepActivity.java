package com.xiandao.android.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.andview.refreshview.utils.LogUtils;
import com.xiandao.android.R;
import com.xiandao.android.adapter.smart.ComplainWorkflowStepAdapter;
import com.xiandao.android.adapter.smart.OrderWorkflowStepAdapter;
import com.xiandao.android.entity.smart.WorkflowComplainEntity;
import com.xiandao.android.entity.smart.WorkflowOrderEntity;
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
        if(getIntent().getExtras().getSerializable("orderWorkflowList")!=null){
            List<WorkflowOrderEntity> data = (List<WorkflowOrderEntity>) getIntent().getExtras().getSerializable("orderWorkflowList");
            if(data!=null){
                workflow_step_rlv.setLayoutManager(new LinearLayoutManager(this));
                workflow_step_rlv.setAdapter(new OrderWorkflowStepAdapter(this,data));
                LogUtils.d("data:"+data.size());
            }
        }else{
            List<WorkflowComplainEntity> data = (List<WorkflowComplainEntity>) getIntent().getExtras().getSerializable("complainWorkflowList");
            if(data!=null){
                workflow_step_rlv.setLayoutManager(new LinearLayoutManager(this));
                workflow_step_rlv.setAdapter(new ComplainWorkflowStepAdapter(this,data));
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
