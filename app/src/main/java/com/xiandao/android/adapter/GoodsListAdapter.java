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
import com.xiandao.android.entity.GoodsEntity;
import com.xiandao.android.utils.PhotoUtils;
import com.xiandao.android.utils.Tools;

import org.xutils.event.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;


public class GoodsListAdapter extends BaseAdapter {

    Context mContext;
    private ArrayList<GoodsEntity> goodsEntities;
    private ForBuyCar forBuyCar;

    public GoodsListAdapter(Context context, ArrayList<GoodsEntity> goodsEntities, ForBuyCar forBuyCar) {
        super();
        this.mContext = context;
        this.goodsEntities = goodsEntities;
        this.forBuyCar = forBuyCar;
    }

    @Override
    public int getCount() {
        return goodsEntities.size();
    }

    @Override
    public Object getItem(int position) {
        return goodsEntities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final GoodsListAdapter.ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.goods_list_item, null);
            holder = new GoodsListAdapter.ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final GoodsEntity entity = goodsEntities.get(position);
        if (entity.getResourceList() != null && entity.getResourceList().size() > 0) {
            if (!Tools.isEmpty(entity.getResourceList().get(0).getUrl())) {
                String url = entity.getResourceList().get(0).getUrl();
                String[] urlList = url.split(",");
                PhotoUtils.loadImage(urlList[0], holder.iv_goods_image);
            }
        }
        holder.tv_goods_name.setText(Tools.getStringValue(entity.getName()));
        holder.tv_goods_now_price.setText("¥" + Tools.getStringValue(entity.getDiscountprice()));
        holder.tv_goods_old_price.setText("¥" + Tools.getStringValue(entity.getPrice()));
        holder.tv_goods_old_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        holder.iv_add_car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forBuyCar.add(entity.getId(), entity.getShopType().getHomeid() + "");
            }
        });
        return convertView;
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {
        @ViewInject(R.id.iv_goods_image)
        public ImageView iv_goods_image;
        @ViewInject(R.id.tv_goods_name)
        public TextView tv_goods_name;
        @ViewInject(R.id.tv_goods_now_price)
        public TextView tv_goods_now_price;
        @ViewInject(R.id.tv_goods_old_price)
        public TextView tv_goods_old_price;
        @ViewInject(R.id.iv_add_car)
        public ImageView iv_add_car;

        public ViewHolder(View view) {
            super(view);
            x.view().inject(this, view);
        }
    }

    public interface ForBuyCar {
        void add(String goodses, String homeid);
    }

}
