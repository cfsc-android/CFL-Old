package com.xiandao.android.ui.activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.andview.refreshview.utils.LogUtils;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.gson.reflect.TypeToken;
import com.xiandao.android.R;
import com.xiandao.android.adapter.smart.HouseholdAuditListAdapter;
import com.xiandao.android.entity.smart.ApprovalStatusType;
import com.xiandao.android.entity.smart.AuditEntity;
import com.xiandao.android.entity.smart.AuditListEntity;
import com.xiandao.android.entity.smart.BaseEntity;
import com.xiandao.android.entity.smart.HouseholdRoomEntity;
import com.xiandao.android.http.JsonParse;
import com.xiandao.android.http.MyCallBack;
import com.xiandao.android.http.XUtils;
import com.xiandao.android.ui.BaseActivity;
import com.xiandao.android.utils.FileManagement;

import org.xutils.common.util.LogUtil;
import org.xutils.event.annotation.ContentView;
import org.xutils.event.annotation.Event;
import org.xutils.event.annotation.ViewInject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.xiandao.android.base.Config.BASE_URL;
import static com.xiandao.android.base.Config.BASIC;

@ContentView(R.layout.activity_household_audit_list)
public class HouseholdAuditListActivity extends BaseActivity {
    @ViewInject(R.id.tv_title_name)
    private TextView tv_title_name;
    @ViewInject(R.id.household_audit_list)
    private SwipeMenuListView household_audit_list;

    private HouseholdAuditListAdapter adapter;
    private List<AuditEntity> data=new ArrayList<>();

    private String roomId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tv_title_name.setText("审核处理");
        roomId=getIntent().getExtras().getString("roomId");

        adapter=new HouseholdAuditListAdapter(this,data);
        household_audit_list.setAdapter(adapter);
        // 为ListView设置创建器
        household_audit_list.setMenuCreator(creator);
        // 第2步：为ListView设置菜单项点击监听器，来监听菜单项的点击事件
        household_audit_list.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                LogUtil.d("position:"+position+" index:"+index);
                if(index==0){
                    refuseAudit(position);
                }else{
                    passAudit(position);
                }
                return false;
            }
        });
        getData();
    }

    private void getData(){
        startProgressDialog("");
        Map<String,String> map=new HashMap<>();
//        map.put("householdId", FileManagement.getUserInfoEntity().getId());
        map.put("roomId", roomId);
        XUtils.Get(BASE_URL+BASIC+"basic/verify/page",map,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtils.d(result);
                BaseEntity<AuditListEntity> baseEntity= JsonParse.parse(result,AuditListEntity.class);
                if(baseEntity.isSuccess()){
                    data.clear();
                    data.addAll(baseEntity.getResult().getData());
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

    private void passAudit(int position){
        AuditEntity audit=data.get(position);
        Map<String,String> map=new HashMap<>();
        map.put("householdId",audit.getHouseholdId());
        map.put("id",audit.getId());
        map.put("roomId",audit.getRoomId());
        map.put("type",audit.getType());
        XUtils.Get(BASE_URL+BASIC+"basic/verify/bind",map,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtils.d(result);
                BaseEntity baseEntity= JsonParse.parse(result);
                if(baseEntity.isSuccess()){
                    getData();
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

    private void refuseAudit(int position){
        AuditEntity audit=data.get(position);
        Map<String,Object> map=new HashMap<>();
        map.put("id",audit.getId());
        XUtils.PutNormal(BASE_URL+BASIC+"basic/verify",map,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtils.d(result);
                BaseEntity baseEntity= JsonParse.parse(result);
                if(baseEntity.isSuccess()){
                    getData();
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

    @Event({R.id.iv_title_left})
    private void onClickEvent(View v){
        switch (v.getId()){
            case R.id.iv_title_left:
                finish();
                break;
        }
    }


    private SwipeMenuCreator creator = new SwipeMenuCreator() {

        @Override
        public void create(SwipeMenu menu) {
            SwipeMenuItem refuseItem = new SwipeMenuItem(
                    getApplicationContext());
            refuseItem.setBackground(new ColorDrawable(Color.rgb(0xF9,0x3F, 0x25)));
            refuseItem.setWidth(dp2px(90));
            refuseItem.setTitle("拒绝");
            refuseItem.setTitleSize(18);
            refuseItem.setTitleColor(Color.WHITE);
            menu.addMenuItem(refuseItem);
            SwipeMenuItem passItem = new SwipeMenuItem(
                    getApplicationContext());
            passItem.setBackground(new ColorDrawable(Color.rgb(19,173, 87)));
            passItem.setWidth(dp2px(90));
            passItem.setTitle("通过");
            passItem.setTitleSize(18);
            passItem.setTitleColor(Color.WHITE);
            menu.addMenuItem(passItem);
        }
    };

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }
}
