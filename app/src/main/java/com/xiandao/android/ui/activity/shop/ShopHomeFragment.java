package com.xiandao.android.ui.activity.shop;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiandao.android.R;
import com.xiandao.android.adapter.GoodsListAdapter;
import com.xiandao.android.adapter.ShopHomeAdapter;
import com.xiandao.android.entity.GoodsClassificationEntity;
import com.xiandao.android.entity.ShopHomeResultEntity;
import com.xiandao.android.net.ApiHttpResult;
import com.xiandao.android.net.HttpUtils;
import com.xiandao.android.net.NetUtil;
import com.xiandao.android.ui.BaseLazyFragment;
import com.xiandao.android.utils.Tools;

import org.xutils.event.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

/**
 * 此类描述的是:购物首页fragment--商品分类
 *
 * @author TanYong
 *         create at 2017/5/18 9:53
 */
public class ShopHomeFragment extends BaseLazyFragment implements View.OnClickListener {

    @ViewInject(R.id.iv_title_left)
    private ImageView ivTitleLeft;
    @ViewInject(R.id.tv_title_name)
    private TextView tvTitleName;
    @ViewInject(R.id.gv_shop_home)
    private GridView gv_shop_home;

    private ArrayList<GoodsClassificationEntity> goodsList;
    private ShopHomeAdapter mShopHomeAdapter;
    private String homeid;

    @Override
    protected void injectView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_shop_home, null);
        x.view().inject(this, view);
        setContentView(view);
        init();
    }

    private void init() {
        Bundle bundle = getArguments();
        homeid = bundle.getString("storeId");

        ivTitleLeft.setOnClickListener(this);
        tvTitleName.setText("商品分类");
        goodsList = new ArrayList<>();
        mShopHomeAdapter = new ShopHomeAdapter(getActivity(), goodsList);
        gv_shop_home.setAdapter(mShopHomeAdapter);

        gv_shop_home.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putString("typeid", goodsList.get(position).getId() + "");
                openActivity(GoodsListActivity.class, bundle);
            }
        });

        queryList();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_title_left:
                getActivity().finish();
                break;
            default:
                break;
        }
    }

    /**
     * 查询分类列表
     */
    private void queryList() {
        startProgressDialog("");
        ApiHttpResult.queryShopHome(getActivity(), new String[]{"homeid"},
                new Object[]{homeid},
                new HttpUtils.DataCallBack() {
                    @Override
                    public void callBack(Object o) {
                        stopProgressDialog();
                        if (o != null) {
                            ShopHomeResultEntity shopHomeResultEntity = (ShopHomeResultEntity) o;
                            if (shopHomeResultEntity != null && shopHomeResultEntity.getResultCode().equals("0")) {
                                ArrayList<GoodsClassificationEntity> list = shopHomeResultEntity.getData();
                                if (list != null && list.size() > 0) {
                                    goodsList.addAll(list);
                                    mShopHomeAdapter.notifyDataSetChanged();
                                } else {
                                    Tools.showPrompt("没有数据");
                                }
                            } else {
                                Tools.showPrompt(shopHomeResultEntity.getMsg());
                            }
                        } else {
                            NetUtil.toNetworkSetting(getActivity());
                        }
                    }
                });
    }
}
