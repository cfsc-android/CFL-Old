package com.xiandao.android.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
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


public class CommitOrderListAdapter extends BaseAdapter {

    Context mContext;
    private List<ShopCar1Entity> entityArrayList = new ArrayList<>();
//    private ForBuyCar forBuyCar;

    public CommitOrderListAdapter(Context context, List<ShopCar1Entity> entityArrayList) {
        super();
        this.mContext = context;
        this.entityArrayList = entityArrayList;
    }

    @Override
    public int getCount() {
        return entityArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return entityArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final CommitOrderListAdapter.ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_commit_order, null);
            holder = new CommitOrderListAdapter.ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final ShopCar1Entity entity = entityArrayList.get(position);
        if (entity.getResourceList() != null && entity.getResourceList().size() > 0) {
            PhotoUtils.loadImage(entity.getResourceList().get(0).getUrl(), holder.iv_shop_car1);
        }
        holder.tv_shop_car_goods_name.setText(Tools.getStringValue(entity.getName()));
        holder.tv_shop_car_size.setText(Tools.getStringValue(entity.getSize()));
        holder.tv_shop_car_count.setText("x" + Tools.getStringValue(entity.getCount()));
        holder.tv_shop_car_discountprice.setText("¥" + Tools.getStringValue(entity.getDiscountprice()));
        holder.tv_shop_car_price.setText("¥" + Tools.getStringValue(entity.getPrice()));
        holder.tv_shop_car_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
//        holder.iv_add_car.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                forBuyCar.add(entity.getId(), entity.getShopType().getHomeid()+"");
//            }
//        });
        return convertView;
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {
        @ViewInject(R.id.cb_shop_car1)
        public CheckBox cb_shop_car1;
        @ViewInject(R.id.tv_shop_car_goods_name)
        public TextView tv_shop_car_goods_name;
        @ViewInject(R.id.tv_shop_car_size)
        public TextView tv_shop_car_size;
        @ViewInject(R.id.tv_shop_car_price)
        public TextView tv_shop_car_price;
        @ViewInject(R.id.tv_shop_car_count)
        public TextView tv_shop_car_count;
        @ViewInject(R.id.tv_shop_car_discountprice)
        public TextView tv_shop_car_discountprice;
        @ViewInject(R.id.iv_shop_car1)
        public ImageView iv_shop_car1;

        public ViewHolder(View view) {
            super(view);
            x.view().inject(this, view);
        }
    }

//    public interface ForBuyCar {
//        public void add(String goodses, String homeid);
//    }

}
