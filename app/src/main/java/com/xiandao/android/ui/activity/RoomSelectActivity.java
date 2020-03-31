package com.xiandao.android.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.andview.refreshview.utils.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xiandao.android.R;
import com.xiandao.android.adapter.smart.ProjectSelectAdapter;
import com.xiandao.android.entity.smart.BaseEntity;
import com.xiandao.android.entity.smart.ComplainListEntity;
import com.xiandao.android.entity.smart.KeyTitleEntity;
import com.xiandao.android.entity.smart.ListLoadingType;
import com.xiandao.android.entity.smart.RoomEntity;
import com.xiandao.android.entity.smart.RoomListEntity;
import com.xiandao.android.http.JsonParse;
import com.xiandao.android.http.MyCallBack;
import com.xiandao.android.http.XUtils;
import com.xiandao.android.ui.BaseActivity;
import com.xiandao.android.view.RecyclerViewDivider;

import org.xutils.event.annotation.ContentView;
import org.xutils.event.annotation.Event;
import org.xutils.event.annotation.ViewInject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.xiandao.android.base.Config.BASE_URL;
import static com.xiandao.android.base.Config.BASIC;

@ContentView(R.layout.activity_room_select)
public class RoomSelectActivity extends BaseActivity {
    @ViewInject(R.id.tv_title_name)
    private TextView tv_title_name;
    @ViewInject(R.id.room_select_label)
    private TextView room_select_label;

    @ViewInject(R.id.room_select_srl)
    private SmartRefreshLayout room_select_srl;
    @ViewInject(R.id.room_select_rlv)
    private RecyclerView room_select_rlv;

    private ProjectSelectAdapter adapter;
    private List<KeyTitleEntity> data=new ArrayList<>();
    private String unitId,title;
    private ListLoadingType loadType;
    private int page=1;
    private int pageSize=10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tv_title_name.setText("选择房屋");
        unitId=getIntent().getExtras().getString("unitId");
        title=getIntent().getExtras().getString("title");
        room_select_label.setText(title);
        adapter=new ProjectSelectAdapter(this,data);
        room_select_rlv.setLayoutManager(new LinearLayoutManager(this));
        room_select_rlv.addItemDecoration(new RecyclerViewDivider(this, LinearLayoutManager.VERTICAL));
        room_select_rlv.setAdapter(adapter);
        room_select_rlv.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                Bundle bundle=new Bundle();
                bundle.putString("roomId",data.get(position).getKey());
                openActivity(HouseholdTypeSelectActivity.class,bundle);
            }
        });
        room_select_srl.setPrimaryColorsId(R.color.view_background,R.color.text_primary);
        room_select_srl.setRefreshHeader(new ClassicsHeader(this).setSpinnerStyle(SpinnerStyle.Translate));
        room_select_srl.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                page=1;
                getData();
                loadType= ListLoadingType.Refresh;
            }
        });
        room_select_srl.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                page++;
                getData();
                loadType=ListLoadingType.LoadMore;
            }
        });
        room_select_srl.autoRefresh();
        getData();
    }

    private void getData(){
        Map<String,String> map=new HashMap<>();
        map.put("unitId",unitId);
        map.put("pageNo",page+"");
        map.put("pageSize",pageSize+"");
        XUtils.Get(BASE_URL+BASIC+"basic/room/page",map,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtils.d(result);
                BaseEntity<RoomListEntity> baseEntity= JsonParse.parse(result,RoomListEntity.class);
                if(baseEntity.isSuccess()){
                    if(page==1){
                        data.clear();
                    }
                    List<RoomEntity> list=baseEntity.getResult().getData();
                    for (int i = 0; i < list.size(); i++) {
                        data.add(new KeyTitleEntity(list.get(i).getName(),list.get(i).getId()));
                    }
                    adapter.notifyDataSetChanged();
                    if(loadType==ListLoadingType.Refresh){
                        room_select_srl.finishRefresh();
                        if(page*pageSize>=baseEntity.getResult().getCount()){
                            room_select_srl.setNoMoreData(true);
                        }
                    }else{
                        if(page*pageSize>=baseEntity.getResult().getCount()){
                            room_select_srl.finishLoadMoreWithNoMoreData();
                        }else{
                            room_select_srl.finishLoadMore();
                        }
                    }

                }else{
                    if(loadType== ListLoadingType.Refresh){
                        room_select_srl.finishRefresh();
                    }else{
                        page--;
                        room_select_srl.finishLoadMore(false);
                    }
                    showToast(baseEntity.getMessage());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                if(loadType== ListLoadingType.Refresh){
                    room_select_srl.finishRefresh();
                }else{
                    page--;
                    room_select_srl.finishLoadMore(false);
                }
                showToast(ex.getMessage());
            }
        });
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
