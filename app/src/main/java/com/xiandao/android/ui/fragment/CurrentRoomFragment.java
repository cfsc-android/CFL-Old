package com.xiandao.android.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andview.refreshview.utils.LogUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.xiandao.android.R;
import com.xiandao.android.adapter.smart.RoomHouseholdListAdapter;
import com.xiandao.android.entity.smart.BaseEntity;
import com.xiandao.android.entity.smart.CurrentDistrictEntity;
import com.xiandao.android.entity.smart.RoomEntity;
import com.xiandao.android.entity.smart.RoomHouseholdEntity;
import com.xiandao.android.entity.smart.UserInfoEntity;
import com.xiandao.android.http.JsonParse;
import com.xiandao.android.http.MyCallBack;
import com.xiandao.android.http.XUtils;
import com.xiandao.android.ui.BaseFragment;
import com.xiandao.android.ui.activity.HouseholdFaceActivity;
import com.xiandao.android.ui.activity.UnitSelectActivity;
import com.xiandao.android.utils.FileManagement;
import com.xiandao.android.view.RecyclerViewDivider;

import org.xutils.event.annotation.ContentView;
import org.xutils.event.annotation.Event;
import org.xutils.event.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import static com.xiandao.android.base.Config.BASE_URL;
import static com.xiandao.android.base.Config.BASIC;

/**
 * Created by Loong on 2020/3/17.
 * Version: 1.0
 * Describe:
 */
@ContentView(R.layout.fragment_current_room_layout)
public class CurrentRoomFragment extends BaseFragment {
    @ViewInject(R.id.current_room_project_name)
    private TextView current_room_project_name;
    @ViewInject(R.id.current_room_room_name)
    private TextView current_room_room_name;
    @ViewInject(R.id.current_room_room_code)
    private TextView current_room_room_code;
    @ViewInject(R.id.current_room_project_line)
    private TextView current_room_project_line;
    @ViewInject(R.id.current_room_project_ll)
    private LinearLayout current_room_project_ll;


    @ViewInject(R.id.current_room_ll_add)
    private LinearLayout current_room_ll_add;
    @ViewInject(R.id.current_room_avatar)
    private ImageView current_room_avatar;
    @ViewInject(R.id.current_room_nick_name)
    private TextView current_room_nick_name;

    @ViewInject(R.id.current_room_ll_show)
    private LinearLayout current_room_ll_show;
    @ViewInject(R.id.current_room_user_avatar)
    private ImageView current_room_user_avatar;
    @ViewInject(R.id.current_room_user_name)
    private TextView current_room_user_name;
    @ViewInject(R.id.current_room_user_type)
    private TextView current_room_user_type;
    @ViewInject(R.id.current_room_rv_other)
    private RecyclerView current_room_rv_other;



    private Activity context;
    private RoomHouseholdListAdapter adapter;
    private List<RoomHouseholdEntity> data=new ArrayList<>();
    private RoomHouseholdEntity currentHousehold;
    private RoomEntity roomEntity;
    private UserInfoEntity userInfo;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getActivity();
        userInfo=FileManagement.getUserInfoEntity();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        CurrentDistrictEntity currentDistrictEntity = userInfo.getCurrentDistrict();
        if(!TextUtils.isEmpty(currentDistrictEntity.getRoomId())){
            getRoomData();
            current_room_ll_show.setVisibility(View.VISIBLE);
            adapter=new RoomHouseholdListAdapter(context,data);
            current_room_rv_other.setLayoutManager(new LinearLayoutManager(context));
            current_room_rv_other.addItemDecoration(new RecyclerViewDivider(context, LinearLayoutManager.VERTICAL));
            current_room_rv_other.setAdapter(adapter);
            current_room_rv_other.addOnItemTouchListener(new OnItemClickListener() {
                @Override
                public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("household",data.get(position));
                    bundle.putBoolean("edit",false);
                    openActivity(HouseholdFaceActivity.class,bundle);
                }
            });
        }else{
            current_room_project_line.setVisibility(View.GONE);
            current_room_project_ll.setVisibility(View.GONE);
            current_room_ll_add.setVisibility(View.VISIBLE);
            if(userInfo.getAvatarResource()!=null){
                Glide.with(context)
                        .load(userInfo.getAvatarResource().getUrl())
                        .error(R.drawable.ic_default_img)
                        .circleCrop()
                        .into(current_room_avatar);
            }
            current_room_nick_name.setText(TextUtils.isEmpty(userInfo.getNickName())?userInfo.getName():userInfo.getNickName());
        }
    }

    private void getRoomData(){
        startProgressDialog("");
        XUtils.Get(BASE_URL+BASIC+"basic/room/"+FileManagement.getUserInfoEntity().getCurrentDistrict().getRoomId(),null,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtils.d(result);
                BaseEntity<RoomEntity> baseEntity= JsonParse.parse(result,RoomEntity.class);
                if(baseEntity.isSuccess()){
                    roomEntity=baseEntity.getResult();
                    List<RoomHouseholdEntity> householdEntityList=roomEntity.getHouseholdBoList();
                    for (int i = 0; i <householdEntityList.size() ; i++) {
                        RoomHouseholdEntity household=householdEntityList.get(i);
                        if(FileManagement.getUserInfoEntity().getId().equals(household.getId())){
                            currentHousehold=household;
                            initCurrentRoomView();
                        }else{
                            data.add(household);
                        }
                    }
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

            @Override
            public void onFinished() {
                super.onFinished();
                stopProgressDialog();
            }
        });
    }

    private void initCurrentRoomView(){
        if(!TextUtils.isEmpty(currentHousehold.getAvatarResource())){
            Glide.with(context)
                    .load(currentHousehold.getAvatarResource())
                    .error(R.drawable.ic_default_img)
                    .circleCrop()
                    .into(current_room_user_avatar);
        }else{
            Glide.with(context)
                    .load(R.drawable.icon_user_default)
                    .circleCrop()
                    .into(current_room_user_avatar);
        }
        current_room_project_name.setText(roomEntity.getProjectName());
        current_room_room_name.setText(roomEntity.getFullName());
        current_room_room_code.setText(roomEntity.getCode());
        current_room_user_name.setText(TextUtils.isEmpty(currentHousehold.getNickName())?currentHousehold.getName():currentHousehold.getNickName());
        current_room_user_type.setText(currentHousehold.getHouseholdTypeDisplay());
    }

    @Event({R.id.current_room_ll,R.id.current_room_btn_add})
    private void onClickEvent(View v){
        switch (v.getId()){
            case R.id.current_room_ll:
                Bundle bundle=new Bundle();
                bundle.putSerializable("household",currentHousehold);
                bundle.putBoolean("edit",true);
                openActivity(HouseholdFaceActivity.class,bundle);
                break;
            case R.id.current_room_btn_add:
                openActivity(UnitSelectActivity.class);
                break;
        }
    }
}
