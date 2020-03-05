package com.xiandao.android.ui.activity.shop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.xiandao.android.R;
import com.xiandao.android.adapter.StoreListAdapter;
import com.xiandao.android.entity.StoreEntity;
import com.xiandao.android.entity.StoreListResultEntity;
import com.xiandao.android.net.ApiHttpResult;
import com.xiandao.android.net.HttpUtils;
import com.xiandao.android.net.NetUtil;
import com.xiandao.android.ui.BaseActivity;
import com.xiandao.android.utils.Tools;

import org.xutils.event.annotation.ContentView;
import org.xutils.event.annotation.ViewInject;

import java.util.ArrayList;

/**
 * 此类描述的是:商店列表activity
 *
 * @author TanYong
 *         create at 2017/6/14 17:09
 */
@ContentView(R.layout.activity_store_list)
public class StoreListActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.iv_title_left)
    private ImageView ivTitleLeft;
    @ViewInject(R.id.tv_title_name)
    private TextView tvTitleName;

    @ViewInject(R.id.lv_store_list)
    private ListView lv_store_list;

    private ArrayList<StoreEntity> storeList = new ArrayList<>();
    private StoreListAdapter mStoreListAdapter;

    private boolean isService = false;
    private int homeType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        tvTitleName.setText("商城便利店");
        ivTitleLeft.setOnClickListener(this);
        Intent intent = getIntent();
        if (null != intent) {
            Bundle bundle = intent.getExtras();
            if (null != bundle) {
                isService = bundle.getBoolean("service");
            }
        }

        mStoreListAdapter = new StoreListAdapter(StoreListActivity.this, storeList);
        lv_store_list.setAdapter(mStoreListAdapter);

        lv_store_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putString("storeId", storeList.get(position).getId());
                openActivity(ShopMainActivity.class, bundle);
            }
        });
        queryStoreList();
    }

    /**
     * @author TanYong
     * create at 2018/3/1 09:53
     * TODO：获取商店列表
     */
    private void queryStoreList() {
        if (isService) {
            homeType = 1;
        }
        startProgressDialog("");
        ApiHttpResult.queryStoreList(this, new String[]{"projectId", "homeType"},
                new Object[]{Tools.getStringValue(loginUserEntity.getProjectId()), homeType},
                new HttpUtils.DataCallBack() {
                    @Override
                    public void callBack(Object o) {
                        stopProgressDialog();
                        if (o != null) {
                            StoreListResultEntity resultEntity = (StoreListResultEntity) o;
                            if (resultEntity != null && resultEntity.getResultCode().equals("0")) {
                                ArrayList<StoreEntity> resultStoreList = resultEntity.getData();
                                if (resultStoreList != null && resultStoreList.size() > 0) {
                                    storeList.addAll(resultStoreList);
                                    mStoreListAdapter.notifyDataSetChanged();
                                } else {
                                    Tools.showPrompt("没有数据");
                                }
                            } else {
                                Tools.showPrompt(resultEntity.getMsg());
                            }
                        } else {
                            NetUtil.toNetworkSetting(StoreListActivity.this);
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
            default:
                break;
        }
    }
}
