package com.xiandao.android.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.xiandao.android.R;
import com.xiandao.android.adapter.SelectCommonAddressAdapter;
import com.xiandao.android.entity.GetCommonAddressListResultEntity;
import com.xiandao.android.entity.RoomInfoEntity;
import com.xiandao.android.net.ApiHttpResult;
import com.xiandao.android.net.HttpUtils;
import com.xiandao.android.net.NetUtil;
import com.xiandao.android.ui.BaseActivity;
import com.xiandao.android.utils.Tools;

import org.xutils.event.annotation.ContentView;
import org.xutils.event.annotation.ViewInject;

import java.util.ArrayList;

/**
 * 此类描述的是:选择常用地址activity
 *
 * @author TanYong
 *         create at 2017/5/11 21:59
 */
@ContentView(R.layout.activity_select_common_address)
public class SelectCommonAddressActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.iv_title_left)
    private ImageView ivTitleLeft;
    @ViewInject(R.id.tv_title_name)
    private TextView ivTitleName;

    @ViewInject(R.id.lv_select_common_address)
    private ListView lv_select_common_address;
    @ViewInject(R.id.tv_create_common_address)
    private TextView tv_create_common_address;
    @ViewInject(R.id.btn_select_common_address_ok)
    private Button btn_select_common_address_ok;

    private ArrayList<RoomInfoEntity> roomInfoEntities;
    private SelectCommonAddressAdapter selectCommonAddressAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        init();
    }

    private void init() {
        ivTitleName.setText("选择常用地址");
        ivTitleLeft.setOnClickListener(this);
        tv_create_common_address.setOnClickListener(this);
        btn_select_common_address_ok.setOnClickListener(this);

        roomInfoEntities = new ArrayList<>();

        selectCommonAddressAdapter = new SelectCommonAddressAdapter(SelectCommonAddressActivity.this, roomInfoEntities);
        lv_select_common_address.setAdapter(selectCommonAddressAdapter);

        lv_select_common_address.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (roomInfoEntities.get(position).getCheckedFlag() == 1) {
                    roomInfoEntities.get(position).setCheckedFlag(2);
                } else if (roomInfoEntities.get(position).getCheckedFlag() == 2) {
                    roomInfoEntities.get(position).setCheckedFlag(1);
                    for (int i = 0; i < roomInfoEntities.size(); i++) {
                        if (i != position && roomInfoEntities.get(i).getCheckedFlag() == 1) {
                            roomInfoEntities.get(i).setCheckedFlag(2);
                        }
                    }
                }
                selectCommonAddressAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * @author TanYong
     * create at 2017/5/18 10:22
     * TODO：
     */
    private void queryCommonAddress() {
        startProgressDialog("");
        ApiHttpResult.getCommonAddressList(SelectCommonAddressActivity.this, new String[]{"userId", "appMobile"},
                new Object[]{loginUserEntity.getUserId(), loginUserEntity.getMobileNumber()}, new HttpUtils.DataCallBack<Object>() {
                    @Override
                    public void callBack(Object o) {
                        stopProgressDialog();
                        if (o != null) {
                            GetCommonAddressListResultEntity getCommonAddressListResultEntity = (GetCommonAddressListResultEntity) o;
                            if ("0".equals(getCommonAddressListResultEntity.getResultCode())) {
                                if (getCommonAddressListResultEntity.getRoomEntityList() != null &&
                                        getCommonAddressListResultEntity.getRoomEntityList().size() > 0) {
                                    if (roomInfoEntities.size() > 0) {
                                        roomInfoEntities.clear();
                                    }
                                    roomInfoEntities.addAll(getCommonAddressListResultEntity.getRoomEntityList());
                                    for (int i = 0; i < roomInfoEntities.size(); i++) {
                                        if (i == 0) {
                                            roomInfoEntities.get(i).setCheckedFlag(1);
                                        } else {
                                            roomInfoEntities.get(i).setCheckedFlag(2);
                                        }
                                    }
                                    selectCommonAddressAdapter.notifyDataSetChanged();
                                } else {
                                    Tools.showPrompt("没有常用地址，请新建");
                                }
                            } else {
                                Tools.showPrompt(getCommonAddressListResultEntity.getMsg());
                            }
                        } else {
                            NetUtil.toNetworkSetting(SelectCommonAddressActivity.this);
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_title_left:
                finish();
                break;
            case R.id.tv_create_common_address://新建常用地址
                openActivity(CreateCommonAddressActivity.class);
                break;
            case R.id.btn_select_linkman_ok:
                break;
            default:
                break;
        }

    }
}
