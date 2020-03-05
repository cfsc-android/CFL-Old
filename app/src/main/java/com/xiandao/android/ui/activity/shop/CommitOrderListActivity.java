package com.xiandao.android.ui.activity.shop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.xiandao.android.R;
import com.xiandao.android.adapter.CommitOrderListAdapter;
import com.xiandao.android.entity.CreateOrderResultEntity;
import com.xiandao.android.entity.OverallSituationEntity;
import com.xiandao.android.entity.ShopCar1Entity;
import com.xiandao.android.entity.ShopCar1ResultEntity;
import com.xiandao.android.entity.ShopOrderEntity;
import com.xiandao.android.entity.StoreInfo;
import com.xiandao.android.net.ApiHttpResult;
import com.xiandao.android.net.HttpUtils;
import com.xiandao.android.net.NetUtil;
import com.xiandao.android.ui.BaseActivity;
import com.xiandao.android.utils.FileManagement;
import com.xiandao.android.utils.Tools;
import com.xiandao.android.view.NoScrollListView;
import com.xiandao.android.view.alertview.AlertView;
import com.xiandao.android.view.alertview.OnItemClickListener;

import org.xutils.event.annotation.ContentView;
import org.xutils.event.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;


/**
 * 此类描述的是:提交订单列表activity
 *
 * @author TanYong
 *         create at 2017/6/14 17:09
 */
@ContentView(R.layout.activity_commit_order_list)
public class CommitOrderListActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.iv_title_left)
    private ImageView ivTitleLeft;
    @ViewInject(R.id.tv_title_name)
    private TextView tvTitleName;
    @ViewInject(R.id.tv_store_name)
    private TextView tv_store_name;
    @ViewInject(R.id.tv_do_commit_order)
    private TextView tv_do_commit_order;
    @ViewInject(R.id.lv_commit_order)
    private NoScrollListView lv_commit_order;

    private CommitOrderListAdapter commitOrderListAdapter;
    private List<ShopCar1Entity> shopCar1Entities;
    private String mtotalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (null != intent) {
            Bundle bundle = intent.getExtras();
            if (null != bundle) {
                mtotalPrice = bundle.getString("mtotalPrice");
                shopCar1Entities = (List<ShopCar1Entity>) bundle.getSerializable("shopCar1Entities");
                if (null != shopCar1Entities && shopCar1Entities.size() > 0) {
                    init();
                }
            }
        }
    }

    private void init() {
        tvTitleName.setText("下单");
        ivTitleLeft.setOnClickListener(this);
        tv_do_commit_order.setOnClickListener(this);
        tv_store_name.setText(Tools.getStringValue(shopCar1Entities.get(0).getHomename()));
        commitOrderListAdapter = new CommitOrderListAdapter(this, shopCar1Entities);
        lv_commit_order.setAdapter(commitOrderListAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_title_left:
                finish();
                break;
            case R.id.tv_do_commit_order:
                new AlertView("温馨提示", "确认提交订单？",
                        "取消", new String[]{"确认"}, null, this, AlertView.Style.Alert, new OnItemClickListener() {
                    @Override
                    public void onItemClick(Object o, int position) {
                        if (position == 0) {
                            String cartid = shopCar1Entities.get(0).getCartid();
                            String goodsids = "";
                            for (ShopCar1Entity entity : shopCar1Entities) {
                                goodsids = goodsids + "," + entity.getId() + "=" + entity.getCount();
                            }
                            goodsids = goodsids.substring(1);
                            String homeid = shopCar1Entities.get(0).getHomeid();
                            commitOrder(Tools.getStringValue(FileManagement.getLoginUserEntity().getUserId()),
                                    cartid, goodsids, homeid, mtotalPrice
                            );
                        }
                    }
                }).setCancelable(false).show();
                break;
            default:
                break;
        }
    }

    /**
     * TODO：提交订单
     *
     * @param userid     业主id
     * @param cartid     购物车id
     * @param goodsids   多个商品id，例子（id1=数量,id2=数量,id3=数量,id4=数量）
     * @param homeid     商家id
     * @param countprice 总金额
     * @author TanYong
     */
    private void commitOrder(String userid, String cartid, String goodsids, String homeid, String countprice) {
        startProgressDialog("");
        ApiHttpResult.commitOrder(this, new String[]{"userid", "cartid", "goodsids", "homeid", "countprice"},
                new Object[]{userid, cartid, goodsids, homeid, countprice},
                new HttpUtils.DataCallBack() {
                    @Override
                    public void callBack(Object o) {
                        stopProgressDialog();
                        if (o != null) {
                            CreateOrderResultEntity resultEntity = (CreateOrderResultEntity) o;
                            if (resultEntity != null && resultEntity.getResultCode().equals("0")) {
                                Tools.showPrompt(resultEntity.getMsg());
//                                setResult(0);
                                ShopOrderEntity data = resultEntity.getData();
                                Bundle bundle1 = new Bundle();
                                bundle1.putInt("fromWhere",1);//从哪个界面跳转到支付界面：1、商城
                                bundle1.putString("orderId", data.getId());
                                bundle1.putString("price", data.getCountprice());
                                openActivity(ShopPayActivity.class, bundle1);
                                CommitOrderListActivity.this.finish();
                            } else {
                                Tools.showPrompt(resultEntity.getMsg());
                            }
                        } else {
                            NetUtil.toNetworkSetting(CommitOrderListActivity.this);
                        }
                    }
                });
    }


}
