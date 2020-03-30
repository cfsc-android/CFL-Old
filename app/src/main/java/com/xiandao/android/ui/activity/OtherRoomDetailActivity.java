package com.xiandao.android.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.andview.refreshview.utils.LogUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.xiandao.android.R;
import com.xiandao.android.adapter.smart.RoomHouseholdListAdapter;
import com.xiandao.android.entity.eventbus.EventBusMessage;
import com.xiandao.android.entity.smart.BaseEntity;
import com.xiandao.android.entity.smart.RoomEntity;
import com.xiandao.android.entity.smart.RoomHouseholdEntity;
import com.xiandao.android.entity.smart.UserInfoEntity;
import com.xiandao.android.http.JsonParse;
import com.xiandao.android.http.MyCallBack;
import com.xiandao.android.http.XUtils;
import com.xiandao.android.ui.BaseActivity;
import com.xiandao.android.utils.FileManagement;
import com.xiandao.android.view.RecyclerViewDivider;

import org.greenrobot.eventbus.EventBus;
import org.xutils.app.LynActivityManager;
import org.xutils.common.util.LogUtil;
import org.xutils.event.annotation.ContentView;
import org.xutils.event.annotation.Event;
import org.xutils.event.annotation.ViewInject;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.xiandao.android.base.Config.BASE_URL;
import static com.xiandao.android.base.Config.BASIC;


@ContentView(R.layout.activity_other_room_detail)
public class OtherRoomDetailActivity extends BaseActivity {
    @ViewInject(R.id.tv_title_name)
    private TextView tv_title_name;

    @ViewInject(R.id.other_room_project_name)
    private TextView other_room_project_name;
    @ViewInject(R.id.other_room_room_name)
    private TextView other_room_room_name;
    @ViewInject(R.id.other_room_room_code)
    private TextView other_room_room_code;

    @ViewInject(R.id.other_room_user_avatar)
    private ImageView other_room_user_avatar;
    @ViewInject(R.id.other_room_user_name)
    private TextView other_room_user_name;
    @ViewInject(R.id.other_room_user_type)
    private TextView other_room_user_type;
    @ViewInject(R.id.other_room_rv_other)
    private RecyclerView other_room_rv_other;

    private RoomHouseholdListAdapter adapter;
    private List<RoomHouseholdEntity> data=new ArrayList<>();
    private RoomHouseholdEntity currentHousehold;
    private RoomEntity roomEntity;
    private String roomId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tv_title_name.setText("其他房屋");
        roomId=getIntent().getExtras().getString("roomId");
        adapter=new RoomHouseholdListAdapter(this,data);
        other_room_rv_other.setLayoutManager(new LinearLayoutManager(this));
        other_room_rv_other.addItemDecoration(new RecyclerViewDivider(this, LinearLayoutManager.VERTICAL));
        other_room_rv_other.setAdapter(adapter);
        other_room_rv_other.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                Bundle bundle=new Bundle();
                bundle.putSerializable("household",data.get(position));
                bundle.putBoolean("edit",false);
                openActivity(HouseholdFaceActivity.class,bundle);
            }
        });
        getRoomData();
    }


    private void getRoomData(){
        startProgressDialog("");
        XUtils.Get(BASE_URL+BASIC+"basic/room/"+roomId,null,new MyCallBack<String>(){
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
            Glide.with(this)
                    .load(currentHousehold.getAvatarResource())
                    .error(R.drawable.ic_default_img)
                    .circleCrop()
                    .into(other_room_user_avatar);
        }else{
            Glide.with(this)
                    .load(R.drawable.icon_user_default)
                    .circleCrop()
                    .into(other_room_user_avatar);
        }
        other_room_project_name.setText(roomEntity.getProjectName());
        other_room_room_name.setText(roomEntity.getFullName());
        other_room_room_code.setText(roomEntity.getCode());
        other_room_user_name.setText(TextUtils.isEmpty(currentHousehold.getNickName())?currentHousehold.getName():currentHousehold.getNickName());
        other_room_user_type.setText(currentHousehold.getHouseholdTypeDisplay());
    }



    @Event({R.id.iv_title_left,R.id.other_room_ll,R.id.other_room_btn_trans})
    private void onClickEvent(View v){
        switch (v.getId()){
            case R.id.iv_title_left:
                finish();
                break;
            case R.id.other_room_ll:
                Bundle bundle=new Bundle();
                bundle.putSerializable("household",currentHousehold);
                bundle.putBoolean("edit",true);
                openActivity(HouseholdFaceActivity.class,bundle);
                break;
            case R.id.other_room_btn_trans:
                bindRoom();
                break;
        }
    }

    private void bindRoom(){
        startProgressDialog("");
        Map<String,Object> map=new HashMap<>();
        map.put("projectId",roomEntity.getProjectId());
        map.put("phaseId",roomEntity.getPhaseId());
        map.put("buildingId",roomEntity.getBuildingId());
        map.put("unitId",roomEntity.getUnitId());
        map.put("roomId",roomEntity.getId());
        map.put("householdId", FileManagement.getUserInfoEntity().getId());
        XUtils.PostJson(BASE_URL+BASIC+"basic/current/bind",map,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtil.d(result);
                BaseEntity baseEntity= JsonParse.parse(result);
                if(baseEntity.isSuccess()){
                    getUserInfo();
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

    private void getUserInfo(){
        RequestParams params=new RequestParams(BASE_URL+BASIC+"basic/householdInfo/phone");
        params.addParameter("phoneNumber",FileManagement.getPhone());
        x.http().get(params,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtils.d(result);
                BaseEntity<UserInfoEntity> baseEntity= JsonParse.parse(result,UserInfoEntity.class);
                if(baseEntity.isSuccess()){
                    FileManagement.setUserInfo(baseEntity.getResult());//缓存用户信息
                    LynActivityManager.getInstance().finishActivity(HouseHoldActivity.class);
                    EventBus.getDefault().post(new EventBusMessage<>("projectSelect"));
                    finish();
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
