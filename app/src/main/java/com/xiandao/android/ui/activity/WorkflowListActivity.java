package com.xiandao.android.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xiandao.android.R;
import com.xiandao.android.adapter.smart.WorkflowListAdapter;
import com.xiandao.android.entity.eventbus.EventBusMessage;
import com.xiandao.android.entity.smart.BaseEntity;
import com.xiandao.android.entity.smart.FinishStatusType;
import com.xiandao.android.entity.smart.ListLoadingType;
import com.xiandao.android.entity.smart.UserType;
import com.xiandao.android.entity.smart.WorkflowEntity;
import com.xiandao.android.entity.smart.WorkflowListEntity;
import com.xiandao.android.entity.smart.WorkflowType;
import com.xiandao.android.http.JsonParse;
import com.xiandao.android.http.MyCallBack;
import com.xiandao.android.http.XUtils;
import com.xiandao.android.ui.BaseActivity;
import com.xiandao.android.ui.activity.shop.OrderDetailActivity;
import com.xiandao.android.utils.FileManagement;
import com.xiandao.android.view.RecyclerViewDivider;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.LogUtils;
import org.xutils.event.annotation.ContentView;
import org.xutils.event.annotation.Event;
import org.xutils.event.annotation.ViewInject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.xiandao.android.base.Config.BASE_URL;
import static com.xiandao.android.base.Config.WORKORDER;

@ContentView(R.layout.activity_workflow_list)
public class WorkflowListActivity extends BaseActivity {
    @ViewInject(R.id.toolbar_tv_title)
    TextView toolbar_title;
    @ViewInject(R.id.toolbar_btn_action)
    ImageButton toolbar_btn_action;
    @ViewInject(R.id.workflow_list_srl)
    private SmartRefreshLayout workflow_list_srl;
    @ViewInject(R.id.workflow_list_rlv)
    private RecyclerView workflow_list_rlv;

    private WorkflowListAdapter adapter;
    private List<WorkflowEntity> data=new ArrayList<>();
    private ListLoadingType loadType;
    private int page=1;
    private int pageSize=10;
    private WorkflowType workflowType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        workflowType= (WorkflowType) getIntent().getExtras().getSerializable("workflowType");
        toolbar_title.setText(workflowType.getTypeChs());

        toolbar_btn_action.setBackgroundResource(R.drawable.icon_btn_add);
        toolbar_btn_action.setVisibility(View.VISIBLE);


        adapter=new WorkflowListAdapter(this,data);
        workflow_list_rlv.setLayoutManager(new LinearLayoutManager(this));
        workflow_list_rlv.addItemDecoration(new RecyclerViewDivider(this, LinearLayoutManager.VERTICAL));
        workflow_list_rlv.setAdapter(adapter);
        workflow_list_rlv.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                Bundle bundle=new Bundle();
                if("1".equals(workflowType.getType())){//工单
                    bundle.putString("order_id",data.get(position).getId());
                    openActivity(RepairsDetailActivity.class,bundle);
                }else if("2".equals(workflowType.getType())){//投诉
                    bundle.putString("complain_id",data.get(position).getId());
                    openActivity(ComplainDetailActivity.class,bundle);
                }
            }
        });

        workflow_list_srl.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                page=1;
                getData();
                loadType=ListLoadingType.Refresh;
            }
        });
        workflow_list_srl.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                page++;
                getData();
                loadType=ListLoadingType.LoadMore;
            }
        });
        workflow_list_srl.autoRefresh();
        EventBus.getDefault().register(this);
    }

    @Event({R.id.toolbar_btn_back,R.id.toolbar_btn_action})
    private void onClickEvent(View v){
        switch (v.getId()){
            case R.id.toolbar_btn_back:
                finish();
                break;
            case R.id.toolbar_btn_action:
                if("1".equals(workflowType.getType())){//工单
                    openActivity(RepairsActivity.class);
                }else if("2".equals(workflowType.getType())){//投诉
                    openActivity(ComplainActivity.class);
                }
                break;
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(EventBusMessage message){
        if("WorkListRefresh".equals(message.getMessage())){
            workflow_list_srl.autoRefresh();
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
    private void getData(){
        Map<String,String> map=new HashMap<>();
        map.put("pageNo",page+"");
        map.put("pageSize",pageSize+"");
        map.put("isFinish", FinishStatusType.UnFinish.getType());
        map.put("type",workflowType.getType());
        map.put("userType", UserType.Household.getType()+"");
        XUtils.Get(BASE_URL+WORKORDER+"workflow/api/page",map,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtils.d(result);
                BaseEntity<WorkflowListEntity> baseEntity= JsonParse.parse(result,WorkflowListEntity.class);
                if(baseEntity.isSuccess()){
                    if(page==1){
                        data.clear();
                    }
                    data.addAll(baseEntity.getResult().getData());
                    adapter.notifyDataSetChanged();
                    if(loadType==ListLoadingType.Refresh){
                        workflow_list_srl.finishRefresh();
                        if(page*pageSize>=baseEntity.getResult().getCount()){
                            workflow_list_srl.setNoMoreData(true);
                        }
                    }else{
                        if(page*pageSize>=baseEntity.getResult().getCount()){
                            workflow_list_srl.finishLoadMoreWithNoMoreData();
                        }else{
                            workflow_list_srl.finishLoadMore();
                        }
                    }

                }else{
                    if(loadType==ListLoadingType.Refresh){
                        workflow_list_srl.finishRefresh();
                    }else{
                        page--;
                        workflow_list_srl.finishLoadMore(false);
                    }
                    showToast(baseEntity.getMessage());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                if(loadType==ListLoadingType.Refresh){
                    workflow_list_srl.finishRefresh();
                }else{
                    page--;
                    workflow_list_srl.finishLoadMore(false);
                }
                showToast(ex.getMessage());
            }
        });
    }
}
