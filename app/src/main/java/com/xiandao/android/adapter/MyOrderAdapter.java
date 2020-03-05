package com.xiandao.android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiandao.android.R;
import com.xiandao.android.entity.ShopOrderEntity;
import com.xiandao.android.utils.Tools;

import org.xutils.event.annotation.ViewInject;
import org.xutils.x;

import java.util.List;


public class MyOrderAdapter extends BaseAdapter {

    Context mContext;
    private List<ShopOrderEntity> orderDetail;
    private ForMyOrderListener forMyOrderListener;
    //ident:（0已下单/1已支付/2已取消/3已完成）
    private int ident = -1;

    public MyOrderAdapter(Context context, List<ShopOrderEntity> orderDetail, int ident, ForMyOrderListener forMyOrderListener) {
        super();
        this.mContext = context;
        this.orderDetail = orderDetail;
        this.ident = ident;
        this.forMyOrderListener = forMyOrderListener;
    }


    @Override
    public int getCount() {
        return orderDetail.size();
    }

    @Override
    public Object getItem(int position) {
        return orderDetail.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_my_order, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final ShopOrderEntity entity = orderDetail.get(position);

        holder.tv_my_order_num.setText(Tools.getStringValue(entity.getId()));
        holder.tv_store_name.setText(Tools.getStringValue(entity.getShopOrderDetail().get(0).getHomeName()));
        holder.tv_my_order_datetime.setText(Tools.getStringValue(entity.getCreatetime()));
//        holder.tv_my_order_pay_type.setText(Tools.getStringValue(entity.getStatus()));
        holder.tv_my_order_money.setText("¥" + Tools.getStringValue(entity.getCountprice()));

        if (0 == ident) {
            holder.ll_do_my_order.setVisibility(View.VISIBLE);
            holder.tv_cancel_my_order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    forMyOrderListener.cancelMyOrder(entity.getId(), position);
                }
            });
            holder.tv_do_go_pay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    forMyOrderListener.doGoPay(entity.getId(),entity.getCountprice());
                }
            });
        } else {
            holder.ll_do_my_order.setVisibility(View.GONE);
        }

        if (3 == ident) {
//            holder.tv_go_shop_comment.setVisibility(View.VISIBLE);
            holder.tv_go_shop_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    forMyOrderListener.goShopComment(entity.getShopOrderDetail().get(0).getGoodsid(), entity.getId());
                }
            });
        } else {
            holder.tv_go_shop_comment.setVisibility(View.GONE);
        }
        return convertView;
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {
        @ViewInject(R.id.tv_my_order_num)
        public TextView tv_my_order_num;
        @ViewInject(R.id.tv_store_name)
        public TextView tv_store_name;
        @ViewInject(R.id.tv_my_order_datetime)
        public TextView tv_my_order_datetime;
        @ViewInject(R.id.tv_my_order_pay_type)
        public TextView tv_my_order_pay_type;
        @ViewInject(R.id.tv_my_order_money)
        public TextView tv_my_order_money;
        @ViewInject(R.id.tv_cancel_my_order)
        public TextView tv_cancel_my_order;
        @ViewInject(R.id.tv_do_go_pay)
        public TextView tv_do_go_pay;
        @ViewInject(R.id.tv_go_shop_comment)
        public TextView tv_go_shop_comment;
        @ViewInject(R.id.ll_do_my_order)
        public LinearLayout ll_do_my_order;

        public ViewHolder(View view) {
            super(view);
            x.view().inject(this, view);
        }
    }

    public interface ForMyOrderListener {
        void cancelMyOrder(String id, int position);

        void doGoPay(String orderId,String price);

        void goShopComment(String goodsId, String orderid);
    }

}
