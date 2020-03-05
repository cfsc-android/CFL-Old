package com.xiandao.android.ui.activity.shop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiandao.android.R;
import com.xiandao.android.adapter.MyOrderDetailListAdapter;
import com.xiandao.android.entity.ShopCar1Entity;
import com.xiandao.android.entity.ShopOrderDetailEntity;
import com.xiandao.android.entity.ShopOrderEntity;
import com.xiandao.android.ui.BaseActivity;
import com.xiandao.android.ui.activity.ComplainCommentActivity;
import com.xiandao.android.utils.Tools;
import com.xiandao.android.view.NoScrollListView;

import org.xutils.event.annotation.ContentView;
import org.xutils.event.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;


/**
 * 此类描述的是:订单详情activity
 *
 * @author TanYong
 *         create at 2017/6/14 17:09
 */
@ContentView(R.layout.activity_order_detail)
public class OrderDetailActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.iv_title_left)
    private ImageView ivTitleLeft;
    @ViewInject(R.id.tv_title_name)
    private TextView tvTitleName;
    @ViewInject(R.id.tv_store_name)
    private TextView tv_store_name;
    @ViewInject(R.id.tv_total_price)
    private TextView tv_total_price;
    @ViewInject(R.id.tv_order_num)
    private TextView tv_order_num;
    @ViewInject(R.id.tv_order_pay_type)
    private TextView tv_order_pay_type;
    @ViewInject(R.id.tv_order_receiving_type)
    private TextView tv_order_receiving_type;
    @ViewInject(R.id.tv_order_create_time)
    private TextView tv_order_create_time;
    @ViewInject(R.id.tv_pay_time)
    private TextView tv_pay_time;
    @ViewInject(R.id.tv_do_go_pay)
    private TextView tv_do_go_pay;
    //    @ViewInject(R.id.tv_go_shop_comment)
//    private TextView tv_go_shop_comment;
    @ViewInject(R.id.nslv_order_detail)
    private NoScrollListView nslv_order_detail;

    //ident:（0已下单/1已支付/2已取消/3已完成）
    private int ident = -1;

    private MyOrderDetailListAdapter adapter;
    private List<ShopCar1Entity> shopOrderDetail;
    private ShopOrderEntity shopOrderEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (null != intent) {
            Bundle bundle = intent.getExtras();
            if (null != bundle) {
                ident = bundle.getInt("ident");
                shopOrderEntity = (ShopOrderEntity) bundle.getSerializable("shopOrderEntity");
                shopOrderDetail = (List<ShopCar1Entity>) bundle.getSerializable("shopOrderDetail");
                init();
            }
        }
    }

    private void init() {
        tvTitleName.setText("订单详情");
        ivTitleLeft.setOnClickListener(this);

//        tv_go_shop_comment.setOnClickListener(this);
        switch (ident) {
            case 0:
                tv_do_go_pay.setVisibility(View.VISIBLE);
                tv_do_go_pay.setOnClickListener(this);
//                tv_go_shop_comment.setVisibility(View.GONE);
                break;
            case 1:
                tv_do_go_pay.setVisibility(View.GONE);
//                tv_go_shop_comment.setVisibility(View.GONE);
                break;
            case 2:
                tv_do_go_pay.setVisibility(View.GONE);
//                tv_go_shop_comment.setVisibility(View.GONE);
                break;
            case 3:
//                tv_go_shop_comment.setVisibility(View.VISIBLE);
                tv_do_go_pay.setVisibility(View.GONE);
                break;
            default:
                break;
        }
        adapter = new MyOrderDetailListAdapter(this, ident, shopOrderDetail, new MyOrderDetailListAdapter.CommentOrderListener() {
            @Override
            public void goShopComment(String goodsId, String orderid) {
                Bundle bundle1 = new Bundle();
                bundle1.putString("goodsId", goodsId);
                bundle1.putString("orderid", orderid);
                bundle1.putBoolean("fromShop", true);
                openActivity(ComplainCommentActivity.class, bundle1);
            }
        });
        nslv_order_detail.setAdapter(adapter);

        tv_store_name.setText(Tools.getStringValue(shopOrderEntity.getShopOrderDetail().get(0).getHomeName()));
        tv_total_price.setText("¥" + Tools.getStringValue(shopOrderEntity.getCountprice()));
        tv_order_create_time.setText(Tools.getStringValue(shopOrderEntity.getCreatetime()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_title_left:
                finish();
                break;
            case R.id.tv_do_go_pay:
                Bundle bundle1 = new Bundle();
                bundle1.putInt("fromWhere",1);//从哪个界面跳转到支付界面：1、商城
                bundle1.putString("orderId", shopOrderEntity.getId());
                bundle1.putString("price", shopOrderEntity.getCountprice());
                openActivity(ShopPayActivity.class, bundle1);
                OrderDetailActivity.this.finish();
                break;
//            case R.id.tv_go_shop_comment:
//                Bundle bundle1 = new Bundle();
//                bundle1.putString("goodsId", shopOrderEntity.getShopOrderDetail().get(0).getGoodsid());
//                bundle1.putString("orderid", shopOrderEntity.getId());
//                bundle1.putBoolean("fromShop", true);
//                openActivity(ComplainCommentActivity.class, bundle1);
//                break;
            default:
                break;
        }
    }

}
