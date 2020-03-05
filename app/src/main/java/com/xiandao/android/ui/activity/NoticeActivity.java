package com.xiandao.android.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xiandao.android.R;
import com.xiandao.android.adapter.smart.NoticeListAdapter;
import com.xiandao.android.entity.smart.BaseEntity;
import com.xiandao.android.entity.smart.ListLoadingType;
import com.xiandao.android.entity.smart.NoticeEntity;
import com.xiandao.android.entity.smart.NoticeListEntity;
import com.xiandao.android.entity.smart.NoticeReceiverType;
import com.xiandao.android.entity.smart.NoticeType;
import com.xiandao.android.http.JsonParse;
import com.xiandao.android.http.MyCallBack;
import com.xiandao.android.http.XUtils;
import com.xiandao.android.ui.BaseActivity;
import com.xiandao.android.view.RecyclerViewDivider;

import org.xutils.LogUtils;
import org.xutils.event.annotation.ContentView;
import org.xutils.event.annotation.Event;
import org.xutils.event.annotation.ViewInject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.xiandao.android.base.Config.ARTICLE;
import static com.xiandao.android.base.Config.BASE_URL;

/**
 * 此类描述的是:通知公告activity
 *
 * @author TanYong
 * create at 2017/6/14 17:09
 */
@ContentView(R.layout.activity_notice)
public class NoticeActivity extends BaseActivity{
    @ViewInject(R.id.toolbar_tv_title)
    TextView toolbar_title;

    @ViewInject(R.id.notice_srl)
    private SmartRefreshLayout notice_srl;
    @ViewInject(R.id.notice_rlv)
    private RecyclerView notice_rlv;

    private NoticeListAdapter adapter;
    private List<NoticeEntity> data=new ArrayList<>();
    private ListLoadingType loadType;
    private int page=1;
    private int pageSize=10;

    private String noticeType;
    private String noticeTypeName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        noticeType=getIntent().getExtras().getString("notice_type");
        noticeTypeName=NoticeType.getName(noticeType);
        toolbar_title.setText(noticeTypeName);
        adapter=new NoticeListAdapter(this,data);
        notice_rlv.setLayoutManager(new LinearLayoutManager(this));
        notice_rlv.addItemDecoration(new RecyclerViewDivider(this, LinearLayoutManager.VERTICAL));
        notice_rlv.setAdapter(adapter);
        notice_rlv.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                Bundle bundle=new Bundle();
                bundle.putString("title",noticeTypeName);
                bundle.putString("noticeId",data.get(position).getId());
                openActivity(NoticeDetailActivity.class,bundle);
            }
        });

        notice_srl.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                page=1;
                getData();
                loadType=ListLoadingType.Refresh;
            }
        });
        notice_srl.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                page++;
                getData();
                loadType=ListLoadingType.LoadMore;
            }
        });
        notice_srl.autoRefresh();
    }

    private void getData(){
        Map<String,String> map=new HashMap<>();
        map.put("projectId","ec93bb06f5be4c1f19522ca78180e2i9");
        map.put("receiver", NoticeReceiverType.全部.getType()+","+NoticeReceiverType.业主.getType());
        map.put("announcementTypeId", noticeType);
        map.put("auditStatus","1");
        map.put("pageNo","1");
        map.put("pageSize","10");
        XUtils.Get(BASE_URL+ARTICLE+"smart/content/pages",map,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtils.d(result);
                BaseEntity<NoticeListEntity> baseEntity= JsonParse.parse(result,NoticeListEntity.class);
                if(baseEntity.isSuccess()){
                    if(page==1){
                        data.clear();
                    }
                    data.addAll(baseEntity.getResult().getData());
                    if(data.size()==0){
                        showToast("没有数据");
                    }
                    adapter.notifyDataSetChanged();
                    if(loadType==ListLoadingType.Refresh){
                        notice_srl.finishRefresh();
                        if(page*pageSize>=baseEntity.getResult().getCount()){
                            notice_srl.setNoMoreData(true);
                        }
                    }else{
                        if(page*pageSize>=baseEntity.getResult().getCount()){
                            notice_srl.finishLoadMoreWithNoMoreData();
                        }else{
                            notice_srl.finishLoadMore();
                        }
                    }

                }else{
                    if(loadType==ListLoadingType.Refresh){
                        notice_srl.finishRefresh();
                    }else{
                        page--;
                        notice_srl.finishLoadMore(false);
                    }
                    showToast(baseEntity.getMessage());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                if(loadType==ListLoadingType.Refresh){
                    notice_srl.finishRefresh();
                }else{
                    page--;
                    notice_srl.finishLoadMore(false);
                }
                showToast(ex.getMessage());
            }
        });



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
