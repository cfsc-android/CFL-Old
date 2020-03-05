package com.xiandao.android.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.xiandao.android.R;
import com.xiandao.android.utils.PhotoUtils;

import java.util.ArrayList;

/**
 * 此类描述的是:显示图片表格布局adapter
 * @author TanYong
 * create at 2017/5/10 14:50
 */
public class DisplayImageGridViewAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> imageUrlList;

    public DisplayImageGridViewAdapter(Context context, ArrayList<String> imageUrlList) {
        this.context = context;
        this.imageUrlList = imageUrlList;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.display_image_gridview_item, null);
            holder = new ViewHolder();
            holder.ivImageGridviewItem = (ImageView) convertView.findViewById(R.id.iv_image_gridview_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        PhotoUtils.loadImage(imageUrlList.get(position), holder.ivImageGridviewItem);
        return convertView; // 返回ImageView
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return imageUrlList.size();
    }

    public final class ViewHolder {
        public ImageView ivImageGridviewItem;
    }

}
