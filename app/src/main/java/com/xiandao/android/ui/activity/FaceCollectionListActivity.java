package com.xiandao.android.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.xiandao.android.R;
import com.xiandao.android.adapter.FaceCollectionListAdapter;
import com.xiandao.android.entity.eventbus.EventBusMessage;
import com.xiandao.android.entity.smart.BaseEntity;
import com.xiandao.android.entity.smart.FaceEntity;
import com.xiandao.android.entity.smart.FileEntity;
import com.xiandao.android.entity.smart.UserInfoEntity;
import com.xiandao.android.http.JsonParse;
import com.xiandao.android.http.MyCallBack;
import com.xiandao.android.http.XUtils;
import com.xiandao.android.ui.BaseActivity;
import com.xiandao.android.utils.FileManagement;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.LogUtils;
import org.xutils.event.annotation.ContentView;
import org.xutils.event.annotation.Event;
import org.xutils.event.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import static com.xiandao.android.base.Config.BASE_URL;
import static com.xiandao.android.base.Config.FILE;

@ContentView(R.layout.activity_face_collection_list)
public class FaceCollectionListActivity extends BaseActivity {
    @ViewInject(R.id.tv_title_name)
    private TextView tv_title_name;
    @ViewInject(R.id.gv_face_collection_list)
    private GridView gv_face_collection_list;

    private List<FaceEntity> faceEntities=new ArrayList<>();
    private FaceCollectionListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tv_title_name.setText("人脸采集");
        adapter=new FaceCollectionListAdapter(this,faceEntities);
        gv_face_collection_list.setAdapter(adapter);
        gv_face_collection_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle=new Bundle();
                bundle.putString("userId",faceEntities.get(position).getUserId());
                bundle.putString("name",faceEntities.get(position).getName());
                bundle.putString("nickname",faceEntities.get(position).getNickname());
                bundle.putString("faceDisUrl",faceEntities.get(position).getFaceUrl());
                bundle.putString("faceDisTime",faceEntities.get(position).getUpdateTime());
                openActivity(FaceCollectionActivity.class,bundle);
            }
        });
        getData();
        EventBus.getDefault().register(this);
    }

    private void getData(){
        final UserInfoEntity userInfo=FileManagement.getUserInfoEntity();
        if(TextUtils.isEmpty(userInfo.getFaceId())){
            faceEntities.add(new FaceEntity(userInfo.getId(),userInfo.getName(),userInfo.getNickName(),"",""));
            adapter.notifyDataSetChanged();
        }else{
            startProgressDialog("");
            XUtils.Get(BASE_URL+FILE+"files/byid/"+userInfo.getFaceId(),null,new MyCallBack<String>(){
                @Override
                public void onSuccess(String result) {
                    super.onSuccess(result);
                    LogUtils.d(result);
                    BaseEntity<FileEntity> baseEntity= JsonParse.parse(result,FileEntity.class);
                    if(baseEntity.isSuccess()){
                        FileEntity file=baseEntity.getResult();
                        faceEntities.add(new FaceEntity(userInfo.getId(),userInfo.getName(),userInfo.getNickName(),file.getDomain()+file.getUrl(),file.getCreateTime()));
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
    }

    @Event({R.id.iv_title_left})
    private void onClickEvent(View v){
        switch (v.getId()){
            case R.id.iv_title_left:
                finish();
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(EventBusMessage message){
        if("faceCollection".equals(message.getMessage())){
            getData();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
