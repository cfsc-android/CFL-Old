package com.xiandao.android.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.andview.refreshview.utils.LogUtils;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.gson.reflect.TypeToken;
import com.xiandao.android.R;
import com.xiandao.android.adapter.smart.HouseManageAdapter;
import com.xiandao.android.entity.eventbus.EventBusMessage;
import com.xiandao.android.entity.smart.ApprovalStatusType;
import com.xiandao.android.entity.smart.BaseEntity;
import com.xiandao.android.entity.smart.CurrentDistrictEntity;
import com.xiandao.android.entity.smart.HouseholdRoomEntity;
import com.xiandao.android.entity.smart.UserInfoEntity;
import com.xiandao.android.http.JsonParse;
import com.xiandao.android.http.MyCallBack;
import com.xiandao.android.http.XUtils;
import com.xiandao.android.ui.BaseActivity;
import com.xiandao.android.utils.FileManagement;

import org.greenrobot.eventbus.EventBus;
import org.xutils.app.LynActivityManager;
import org.xutils.common.util.LogUtil;
import org.xutils.event.annotation.ContentView;
import org.xutils.event.annotation.Event;
import org.xutils.event.annotation.ViewInject;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.xiandao.android.base.Config.BASE_URL;
import static com.xiandao.android.base.Config.BASIC;

@ContentView(R.layout.activity_house_manage)
public class HouseManageActivity extends BaseActivity{
    @ViewInject(R.id.tv_title_name)
    private TextView tv_title_name;

    @ViewInject(R.id.house_manage_current_smlv_list)
    private SwipeMenuListView house_manage_current_smlv_list;
    @ViewInject(R.id.house_manage_smlv_list)
    private SwipeMenuListView house_manage_smlv_list;

    private HouseManageAdapter currentAdapter;
    private HouseManageAdapter otherAdapter;

    private List<HouseholdRoomEntity> currentData=new ArrayList<>();
    private List<HouseholdRoomEntity> otherData=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tv_title_name.setText("管理房屋");
        currentAdapter = new HouseManageAdapter(this,currentData,false);
        house_manage_current_smlv_list.setAdapter(currentAdapter);
        house_manage_current_smlv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle=new Bundle();
                bundle.putString("roomId",currentData.get(position).getId());
                openActivity(HouseholdAuditListActivity.class,bundle);
            }
        });
        // 为ListView设置创建器
        house_manage_current_smlv_list.setMenuCreator(creator);
        // 第2步：为ListView设置菜单项点击监听器，来监听菜单项的点击事件
        house_manage_current_smlv_list.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
                new AlertDialog.Builder(HouseManageActivity.this)
                        .setTitle("删除房屋")
                        .setMessage("确认要删除房屋？")
                        .setCancelable(true)
                        .setNegativeButton(
                                "确认删除",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        deleteCurrentRoom();
                                    }
                                }).show();
                return false;
            }
        });
        otherAdapter = new HouseManageAdapter(this,otherData,false);
        house_manage_smlv_list.setAdapter(otherAdapter);
        house_manage_smlv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle=new Bundle();
                bundle.putString("roomId",otherData.get(position).getId());
                openActivity(HouseholdAuditListActivity.class,bundle);
            }
        });
        // 为ListView设置创建器
        house_manage_smlv_list.setMenuCreator(creator);
        // 第2步：为ListView设置菜单项点击监听器，来监听菜单项的点击事件
        house_manage_smlv_list.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
                new AlertDialog.Builder(HouseManageActivity.this)
                        .setTitle("删除房屋")
                        .setMessage("确认要删除房屋？")
                        .setCancelable(true)
                        .setNegativeButton(
                                "确认删除",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        HouseholdRoomEntity roomEntity=otherData.get(position);
                                        if(roomEntity.getApprovalStatus()== ApprovalStatusType.Pass.getType()){
                                            deleteOtherRoom(roomEntity.getId());
                                        }else{
                                            deleteRefuseRoom(roomEntity.getApprovalId());
                                        }
                                    }
                                }).show();
                return false;
            }
        });
        intData();
    }

    private void intData(){
        CurrentDistrictEntity currentRoom=FileManagement.getUserInfoEntity().getCurrentDistrict();
        List<HouseholdRoomEntity> roomList= FileManagement.getUserInfoEntity().getRoomList();
        if(roomList!=null&&roomList.size()>0){
            for (int i = 0; i < roomList.size(); i++) {
                roomList.get(i).setApprovalStatus(2);
                if(roomList.get(i).getId().equals(currentRoom.getRoomId())){
                    currentData.add(roomList.get(i));
                }else{
                    otherData.add(roomList.get(i));
                }
            }
            currentAdapter.notifyDataSetChanged();
            otherAdapter.notifyDataSetChanged();
        }
        getRoomData();
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
                    for (int i = 0; i < list.size(); i++) {
                        if(list.get(i).getApprovalStatus()== ApprovalStatusType.Refuse.getType()){
                            otherData.add(list.get(i));
                        }
                    }
                    otherAdapter.notifyDataSetChanged();
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

    private void deleteCurrentRoom(){
        startProgressDialog("");
        Map<String,String> map=new HashMap<>();
        List<HouseholdRoomEntity> list=FileManagement.getUserInfoEntity().getRoomList();
        String roomId=currentData.get(0).getId();
        for (int i = 0; i < list.size(); i++) {
            if(!roomId.equals(list.get(i).getId())){
                map.put(list.get(i).getId(),list.get(i).getHouseholdType());
            }
        }
        Map<String,Object> requestMap=new HashMap<>();
        requestMap.put("id",FileManagement.getUserInfoEntity().getId());
        requestMap.put("roomMap",map);
        XUtils.Put(BASE_URL+BASIC+"basic/householdInfo",null,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtils.d(result);
                BaseEntity baseEntity= JsonParse.parse(result);
                if(baseEntity.isSuccess()){
                    unBindRoom();
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

    private void unBindRoom(){
        Map<String,Object> map=new HashMap<>();
        map.put("projectId",currentData.get(0).getProjectId());
        map.put("householdId", FileManagement.getUserInfoEntity().getId());
        XUtils.PostJson(BASE_URL+BASIC+"basic/current/bind",map,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtil.d(result);
                BaseEntity baseEntity= JsonParse.parse(result);
                if(baseEntity.isSuccess()){
                    getUserInfo(true);
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


    private void deleteOtherRoom(String roomId){
        startProgressDialog("");
        Map<String,String> map=new HashMap<>();
        List<HouseholdRoomEntity> list=FileManagement.getUserInfoEntity().getRoomList();
        for (int i = 0; i < list.size(); i++) {
            if(!roomId.equals(list.get(i).getId())){
                map.put(list.get(i).getId(),list.get(i).getHouseholdType());
            }
        }
        Map<String,Object> requestMap=new HashMap<>();
        requestMap.put("id",FileManagement.getUserInfoEntity().getId());
        requestMap.put("roomMap",map);
        XUtils.Put(BASE_URL+BASIC+"basic/householdInfo",requestMap,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtils.d(result);
                BaseEntity baseEntity= JsonParse.parse(result);
                if(baseEntity.isSuccess()){
                    getUserInfo(false);
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

    private void deleteRefuseRoom(String approvalId){
        startProgressDialog("");
        Map<String,Object> map=new HashMap<>();
        map.put("id",approvalId);
        XUtils.Delete(BASE_URL+BASIC+"basic/verify/delete",map,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtils.d(result);
                BaseEntity baseEntity= JsonParse.parse(result);
                if(baseEntity.isSuccess()){
                    intData();
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

    private void getUserInfo(final boolean finish){
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
                    if(finish){
                        LynActivityManager.getInstance().finishActivity(HouseHoldActivity.class);
                        EventBus.getDefault().post(new EventBusMessage<>("projectSelect"));
                        finish();
                    }else{
                        intData();
                    }
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


    private SwipeMenuCreator creator = new SwipeMenuCreator() {

        @Override
        public void create(SwipeMenu menu) {
            SwipeMenuItem openItem = new SwipeMenuItem(
                    getApplicationContext());
            openItem.setBackground(new ColorDrawable(Color.rgb(0xF9,0x3F, 0x25)));
            openItem.setWidth(dp2px(90));
            openItem.setTitle("删除");
            openItem.setTitleSize(18);
            openItem.setTitleColor(Color.WHITE);
            menu.addMenuItem(openItem);
        }
    };

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }



    @Event({R.id.iv_title_left,R.id.house_manage_ll_add})
    private void onClickEvent(View v){
        switch (v.getId()){
            case R.id.iv_title_left:
                finish();
                break;
            case R.id.house_manage_ll_add:
                Bundle bundle=new Bundle();
                bundle.putString("openFrom","HouseManage");
                openActivity(ProjectSelectActivity.class,bundle);
                break;
        }
    }
}
