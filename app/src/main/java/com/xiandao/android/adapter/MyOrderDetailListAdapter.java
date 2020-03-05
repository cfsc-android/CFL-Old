package com.xiandao.android.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiandao.android.R;
import com.xiandao.android.entity.ShopCar1Entity;
import com.xiandao.android.utils.PhotoUtils;
import com.xiandao.android.utils.Tools;

import org.xutils.event.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;


public class MyOrderDetailListAdapter extends BaseAdapter {

    Context mContext;
    private List<ShopCar1Entity> shopOrderDetail = new ArrayList<>();
    private CommentOrderListener orderListener;
    private int ident = 0;

    public MyOrderDetailListAdapter(Context context, int ident, List<ShopCar1Entity> shopOrderDetail, CommentOrderListener orderListener) {
        super();
        this.mContext = context;
        this.shopOrderDetail = shopOrderDetail;
        this.orderListener = orderListener;
        this.ident = ident;
    }

    public void setList(List<ShopCar1Entity> shopOrderDetail) {
        this.shopOrderDetail = shopOrderDetail;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return shopOrderDetail.size();
    }

    @Override
    public Object getItem(int position) {
        return shopOrderDetail.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final MyOrderDetailListAdapter.ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_my_order_detail, null);
            holder = new MyOrderDetailListAdapter.ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final ShopCar1Entity entity = shopOrderDetail.get(position);
        if (entity.getResourceList() != null && entity.getResourceList().size() > 0) {
            PhotoUtils.loadImage(entity.getResourceList().get(0).getUrl(), holder.iv_my_order_detail);
        }
        holder.tv_store_name.setText(Tools.getStringValue(entity.getHomename()));
        holder.tv_my_order_datail_count.setText("x" + Tools.getStringValue(entity.getCount()));
        holder.tv_my_order_datail_size.setText(Tools.getStringValue(entity.getSize()));
        holder.tv_my_order_datail_discountprice.setText("¥" + Tools.getStringValue(entity.getDiscountprice()));
        holder.tv_my_order_datail_price.setText("¥" + Tools.getStringValue(entity.getPrice()));
        holder.tv_my_order_datail_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        if (ident == 3) {
            holder.tv_go_shop_comment.setVisibility(View.VISIBLE);
        }
        holder.tv_go_shop_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderListener.goShopComment(entity.getGoodsid(), entity.getOrderid());
            }
        });
        return convertView;
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {
        @ViewInject(R.id.iv_my_order_detail)
        public ImageView iv_my_order_detail;
        @ViewInject(R.id.tv_store_name)
        public TextView tv_store_name;
        @ViewInject(R.id.tv_my_order_datail_size)
        public TextView tv_my_order_datail_size;
        @ViewInject(R.id.tv_my_order_datail_discountprice)
        public TextView tv_my_order_datail_discountprice;
        @ViewInject(R.id.tv_my_order_datail_count)
        public TextView tv_my_order_datail_count;
        @ViewInject(R.id.tv_my_order_datail_price)
        public TextView tv_my_order_datail_price;
        @ViewInject(R.id.tv_go_shop_comment)
        public TextView tv_go_shop_comment;

        public ViewHolder(View view) {
            super(view);
            x.view().inject(this, view);
        }
    }

    public interface CommentOrderListener {
        void goShopComment(String goodsId, String orderId);
    }
}
