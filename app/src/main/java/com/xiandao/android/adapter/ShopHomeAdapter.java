package com.xiandao.android.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jph.takephoto.model.TImage;
import com.xiandao.android.R;
import com.xiandao.android.entity.GoodsClassificationEntity;
import com.xiandao.android.utils.PhotoUtils;
import com.xiandao.android.utils.Tools;

import java.io.File;
import java.util.ArrayList;

/**
 * 此类描述的是:购物首页adapter
 *
 * @author TanYong
 *         create at 2017/5/5 10:09
 */
public class ShopHomeAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<GoodsClassificationEntity> entityArrayList;

    public ShopHomeAdapter(Context context, ArrayList<GoodsClassificationEntity> entityArrayList) {
        this.context = context;
        this.entityArrayList = entityArrayList;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.shop_home_item, null);
            holder = new ViewHolder();
            holder.iv_shop_home = (ImageView) convertView.findViewById(R.id.iv_shop_home);
            holder.tv_shop_home = (TextView) convertView.findViewById(R.id.tv_shop_home);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        PhotoUtils.loadImage(entityArrayList.get(position).getPic(), holder.iv_shop_home);
        holder.tv_shop_home.setText(entityArrayList.get(position).getName());
        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return entityArrayList.get(position);
    }

    @Override
    public int getCount() {
        return entityArrayList.size();
    }

    class ViewHolder {
        ImageView iv_shop_home;
        TextView tv_shop_home;
    }
}
