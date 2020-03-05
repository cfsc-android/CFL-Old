package com.xiandao.android.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.andview.refreshview.utils.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xiandao.android.R;
import com.xiandao.android.adapter.VisitorRecordListAdapter;
import com.xiandao.android.adapter.smart.OrderListAdapter;
import com.xiandao.android.adapter.smart.VisitorListAdapter;
import com.xiandao.android.entity.VisitorRecord;
import com.xiandao.android.entity.eventbus.EventBusMessage;
import com.xiandao.android.entity.smart.BaseEntity;
import com.xiandao.android.entity.smart.ListLoadingType;
import com.xiandao.android.entity.smart.OrderEntity;
import com.xiandao.android.entity.smart.OrderListEntity;
import com.xiandao.android.entity.smart.VisitorEntity;
import com.xiandao.android.entity.smart.VisitorListEntity;
import com.xiandao.android.http.JsonParse;
import com.xiandao.android.http.MyCallBack;
import com.xiandao.android.http.XUtils;
import com.xiandao.android.ui.BaseActivity;
import com.xiandao.android.utils.Constants;
import com.xiandao.android.utils.FileManagement;
import com.xiandao.android.utils.MessageEvent;
import com.xiandao.android.utils.Tools;
import com.xiandao.android.view.RecyclerViewDivider;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.event.annotation.ContentView;
import org.xutils.event.annotation.Event;
import org.xutils.event.annotation.ViewInject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.xiandao.android.base.Config.BASE_URL;
import static com.xiandao.android.base.Config.IOT;

/**
 * Created by ZXL on 2019/4/8.
 * Describe:
 */
@ContentView(R.layout.activity_visitor)
public class VisitorActivity extends BaseActivity {
    @ViewInject(R.id.iv_title_left)
    private ImageView iv_title_left;
    @ViewInject(R.id.tv_title_name)
    private TextView tv_title_name;
    @ViewInject(R.id.visitor_srl)
    private SmartRefreshLayout visitor_srl;
    @ViewInject(R.id.visitor_rlv)
    private RecyclerView visitor_rlv;

    private VisitorListAdapter adapter;
    private List<VisitorEntity> data=new ArrayList<>();
    private ListLoadingType loadType;
    private int page=1;
    private int pageSize=10;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tv_title_name.setText("访客邀约");

        adapter=new VisitorListAdapter(data);
        visitor_rlv.setLayoutManager(new LinearLayoutManager(this));
        visitor_rlv.addItemDecoration(new RecyclerViewDivider(this, LinearLayoutManager.VERTICAL));
        visitor_rlv.setAdapter(adapter);
        visitor_rlv.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                VisitorEntity visitor = data.get(position);
                Bundle bundle = new Bundle();
                bundle.putString("qrCodeUrl", visitor.getQrcodeUrl());
                bundle.putString("name", visitor.getVisitorName());
                bundle.putString("start", visitor.getEffectTime());
                bundle.putString("end", visitor.getExpireTime());
                bundle.putInt("num",visitor.getOpenTimes());
                openActivity(VisitorQrCodeActivity.class,bundle);
            }
        });

        visitor_srl.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                page=1;
                getData();
                loadType=ListLoadingType.Refresh;
            }
        });
        visitor_srl.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                page++;
                getData();
                loadType=ListLoadingType.LoadMore;
            }
        });
        visitor_srl.autoRefresh();

        EventBus.getDefault().register(this);
    }


    private void getData(){
        Map<String,String> map=new HashMap<>();
        map.put("pageNo",page+"");
        map.put("pageSize",pageSize+"");
        map.put("phaseId",FileManagement.getUserInfoEntity().getRoomList().get(0).getPhaseId());
//        map.put("userId",FileManagement.getUserInfoEntity().getId());
        map.put("userId","a75d45a015c44384a04449ee80dc3503");
        XUtils.Get(BASE_URL+IOT+"community/api/access/v1/visitor/pages",map,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtils.d(result);
                BaseEntity<VisitorListEntity> baseEntity= JsonParse.parse(result,VisitorListEntity.class);
                if(baseEntity.isSuccess()){
                    if(page==1){
                        data.clear();
                    }
                    data.addAll(baseEntity.getResult().getData());
                    adapter.notifyDataSetChanged();
                    if(loadType==ListLoadingType.Refresh){
                        visitor_srl.finishRefresh();
                        if(page*pageSize>=baseEntity.getResult().getCount()){
                            visitor_srl.setNoMoreData(true);
                        }
                    }else{
                        if(page*pageSize>=baseEntity.getResult().getCount()){
                            visitor_srl.finishLoadMoreWithNoMoreData();
                        }else{
                            visitor_srl.finishLoadMore();
                        }
                    }

                }else{
                    if(loadType==ListLoadingType.Refresh){
                        visitor_srl.finishRefresh();
                    }else{
                        page--;
                        visitor_srl.finishLoadMore(false);
                    }
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


    @Event({R.id.iv_title_left,R.id.btn_visitor_add})
    private void onClickEvent(View v){
        switch (v.getId()){
            case R.id.iv_title_left:
                finish();
                break;
            case R.id.btn_visitor_add:
                openActivity(NewVisitorActivity.class);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(EventBusMessage message){
        if("visitorRefresh".equals(message.getMessage())){
            visitor_srl.autoRefresh();
        }
    }
}
