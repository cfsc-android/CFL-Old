package com.xiandao.android.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.andview.refreshview.utils.LogUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.gson.reflect.TypeToken;
import com.xiandao.android.R;
import com.xiandao.android.adapter.smart.OtherRoomListAdapter;
import com.xiandao.android.adapter.smart.RoomHouseholdListAdapter;
import com.xiandao.android.entity.smart.BaseEntity;
import com.xiandao.android.entity.smart.CurrentDistrictEntity;
import com.xiandao.android.entity.smart.HouseholdRoomEntity;
import com.xiandao.android.entity.smart.OrderStatusEntity;
import com.xiandao.android.entity.smart.RoomEntity;
import com.xiandao.android.entity.smart.RoomHouseholdEntity;
import com.xiandao.android.entity.smart.UserInfoEntity;
import com.xiandao.android.http.JsonParse;
import com.xiandao.android.http.MyCallBack;
import com.xiandao.android.http.XUtils;
import com.xiandao.android.ui.BaseFragment;
import com.xiandao.android.ui.HouseholdFaceActivity;
import com.xiandao.android.ui.activity.OtherRoomDetailActivity;
import com.xiandao.android.utils.FileManagement;
import com.xiandao.android.view.RecyclerViewDivider;

import org.xutils.event.annotation.ContentView;
import org.xutils.event.annotation.ViewInject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.xiandao.android.base.Config.BASE_URL;
import static com.xiandao.android.base.Config.BASIC;

/**
 * Created by Loong on 2020/3/17.
 * Version: 1.0
 * Describe:
 */
@ContentView(R.layout.fragment_other_room_layout)
public class OtherRoomFragment extends BaseFragment {
    @ViewInject(R.id.other_room_rv)
    private RecyclerView other_room_rv;

    private Activity context;
    private OtherRoomListAdapter adapter;
    private List<HouseholdRoomEntity> data=new ArrayList<>();
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getActivity();
        getRoomData();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter=new OtherRoomListAdapter(context,data);
        other_room_rv.setLayoutManager(new LinearLayoutManager(context));
        other_room_rv.addItemDecoration(new RecyclerViewDivider(context, LinearLayoutManager.VERTICAL));
        other_room_rv.setAdapter(adapter);
        other_room_rv.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                Bundle bundle=new Bundle();
                bundle.putString("roomId",data.get(position).getId());
                openActivity(OtherRoomDetailActivity.class,bundle);
            }
        });
        initOtherRoom();
    }

    private void initOtherRoom(){
        UserInfoEntity userInfoEntity=FileManagement.getUserInfoEntity();
        List<HouseholdRoomEntity> householdRoomEntities= userInfoEntity.getRoomList();
        CurrentDistrictEntity currentDistrictEntity=userInfoEntity.getCurrentDistrict();
        if(householdRoomEntities!=null&&householdRoomEntities.size()>0){
            for (int i = 0; i < householdRoomEntities.size(); i++) {
                HouseholdRoomEntity householdRoomEntity=householdRoomEntities.get(i);
                if(!currentDistrictEntity.getRoomId().equals(householdRoomEntity.getId())){
                    householdRoomEntity.setApprovalStatus(2);
                    data.add(householdRoomEntity);
                }
            }
            adapter.notifyDataSetChanged();
        }

    }

    private void getRoomData(){
        Map<String,String> map=new HashMap<>();
        map.put("householdId",FileManagement.getUserInfoEntity().getId());
        XUtils.Get(BASE_URL+BASIC+"basic/verify/pendingList",map,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtils.d(result);
                BaseEntity baseEntity= JsonParse.parse(result);
                if(baseEntity.isSuccess()){
                    Type type = new TypeToken<List<HouseholdRoomEntity>>() {}.getType();
                    List<HouseholdRoomEntity> list= (List<HouseholdRoomEntity>) JsonParse.parseList(result,type);
                    data.addAll(list);
                    adapter.notifyDataSetChanged();
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

}
