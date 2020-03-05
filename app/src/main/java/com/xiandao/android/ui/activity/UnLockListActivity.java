package com.xiandao.android.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.gson.reflect.TypeToken;
import com.xiandao.android.R;
import com.xiandao.android.adapter.smart.ComplainListAdapter;
import com.xiandao.android.adapter.smart.EquipmentAdapter;
import com.xiandao.android.entity.VideoEntity;
import com.xiandao.android.entity.smart.BaseEntity;
import com.xiandao.android.entity.smart.ComplainEntity;
import com.xiandao.android.entity.smart.DeviceEntity;
import com.xiandao.android.entity.smart.EquipmentInfoBo;
import com.xiandao.android.http.JsonParse;
import com.xiandao.android.http.MyCallBack;
import com.xiandao.android.http.XUtils;
import com.xiandao.android.ui.BaseActivity;
import com.xiandao.android.utils.FileManagement;
import com.xiandao.android.view.RecyclerViewDivider;

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
import static com.xiandao.android.base.Config.IOT;

@ContentView(R.layout.activity_un_lock_list)
public class UnLockListActivity extends BaseActivity {
    @ViewInject(R.id.toolbar_tv_title)
    TextView toolbar_title;

    @ViewInject(R.id.unlock_rlv)
    private RecyclerView unlock_rlv;

    private EquipmentAdapter adapter;
    private List<DeviceEntity> data=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar_title.setText("门禁列表");
        getData();
    }

    private void getData(){
        Map<String,String> map=new HashMap<>();
        map.put("phaseId",FileManagement.getUserInfoEntity().getRoomList().get(0).getPhaseId());
        map.put("userId",FileManagement.getUserInfoEntity().getId());
        XUtils.Get(BASE_URL+IOT+"community/api/access/v1/devices/user",map,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtil.d(result);
                BaseEntity baseEntity= JsonParse.parse(result);
                if(baseEntity.isSuccess()){
                    Type type = new TypeToken<List<DeviceEntity>>() {}.getType();
                    List<DeviceEntity> deviceEntities= (List<DeviceEntity>) JsonParse.parseList(result,type);
                    data.addAll(deviceEntities);
                    initAdapter();
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

    private void initAdapter(){
        adapter=new EquipmentAdapter(this,data);
        unlock_rlv.setLayoutManager(new LinearLayoutManager(this));
        unlock_rlv.addItemDecoration(new RecyclerViewDivider(this, LinearLayoutManager.VERTICAL));
        unlock_rlv.setAdapter(adapter);
        unlock_rlv.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                if(!((EquipmentAdapter)adapter).getEquipmentOpen(position)){
                    openDoor(position);
                }
            }
        });
    }
    private void openDoor(final int position){
        Map<String,Object> map=new HashMap<>();
        map.put("cmd","open");
        map.put("deviceSerial",data.get(position).getDeviceSerial());
        map.put("phaseId", FileManagement.getUserInfoEntity().getRoomList().get(0).getPhaseId());
        XUtils.PostJson(BASE_URL+IOT+"community/api/access/v1/remote/open",map,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtil.d(result);
                BaseEntity baseEntity= JsonParse.parse(result);
                if(baseEntity.isSuccess()){
                    adapter.setEquipmentOpen(position);
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

    @Event({R.id.toolbar_btn_back,R.id.toolbar_btn_action})
    private void onClickEvent(View v){
        switch (v.getId()){
            case R.id.toolbar_btn_back:
                finish();
                break;
        }
    }
}
