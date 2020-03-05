package com.xiandao.android.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.jph.takephoto.model.TImage;
import com.xiandao.android.R;
import com.xiandao.android.utils.Tools;

import java.io.File;
import java.util.ArrayList;

/**
 * 此类描述的是:图片表格布局adapter
 *
 * @author TanYong
 *         create at 2017/5/5 10:09
 */
public class AddImageGridViewAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<TImage> tImages;
    private GridView gridView;
    private int displayCount;//显示几张图片

    public AddImageGridViewAdapter(Context context, ArrayList<TImage> tImages, GridView gridView, int displayCount) {
        this.context = context;
        this.tImages = tImages;
        this.gridView = gridView;
        this.displayCount = displayCount;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.image_gridview_item, null);
            holder = new ViewHolder();
            holder.ivImageGridviewItem = (ImageView) convertView.findViewById(R.id.iv_image_gridview_item);
            holder.ivImageCloseItem = (ImageView) convertView.findViewById(R.id.iv_image_close_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position < tImages.size()) {
            holder.ivImageCloseItem.setVisibility(View.VISIBLE);
            Glide.with(context).load(new File(tImages.get(position).getCompressPath())).into(holder.ivImageGridviewItem);
            holder.ivImageCloseItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tImages.remove(position);
                    Tools.setGridViewHeightBasedOnChildren(gridView);
                    notifyDataSetChanged();
                }
            });
        } else {
            if (tImages.size() == displayCount)
                holder.ivImageGridviewItem.setVisibility(View.GONE);
            Glide.with(context).load(R.mipmap.tongyong_btn_tianjia).into(holder.ivImageGridviewItem);
            holder.ivImageCloseItem.setVisibility(View.GONE);
        }

        return convertView; // 返回ImageView
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return tImages.get(position);
    }

    @Override
    public int getCount() {
        return tImages.size() + 1;
    }

    class ViewHolder {
        ImageView ivImageGridviewItem;
        ImageView ivImageCloseItem;
    }
}
