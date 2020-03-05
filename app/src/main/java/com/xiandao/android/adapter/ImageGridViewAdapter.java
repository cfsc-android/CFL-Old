package com.xiandao.android.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.xiandao.android.R;
import com.xiandao.android.utils.Constants;

import java.util.ArrayList;
import java.util.Collections;

/**
 * 此类描述的是:添加图片表格布局adapter
 *
 * @author TanYong
 *         create at 2017/5/5 10:09
 */
public class ImageGridViewAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Bitmap> bitmapList;
    private ArrayList<String> pathImageList;

    public ImageGridViewAdapter(Context context, ArrayList<Bitmap> bitmapList, ArrayList<String> pathImageList) {
        this.context = context;
        this.bitmapList = bitmapList;
        this.pathImageList = pathImageList;
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
        holder.ivImageGridviewItem.setImageBitmap(bitmapList.get(position));
        holder.ivImageCloseItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Constants.GRIDVIEW_IMAGE_COUNT) {
                    bitmapList.add(BitmapFactory.decodeResource(context.getResources(), R.mipmap.tousu_btn_zj));
                }
                bitmapList.remove(position);
                Collections.reverse(pathImageList);
                pathImageList.remove(position);
                Collections.reverse(pathImageList);
                getDeleteImageFlag.result(true);
                notifyDataSetChanged();
            }
        });
        if (bitmapList.get(position).sameAs(BitmapFactory.decodeResource(context.getResources(), R.mipmap.tousu_btn_zj))) {
            holder.ivImageCloseItem.setVisibility(View.GONE);
        } else {
            holder.ivImageCloseItem.setVisibility(View.VISIBLE);
        }
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
        return bitmapList.size();
    }

    public final class ViewHolder {
        public ImageView ivImageGridviewItem;
        public ImageView ivImageCloseItem;
    }

    private ImageGridViewAdapter.GetDeleteImageFlag getDeleteImageFlag;

    public void setCallback(GetDeleteImageFlag getDeleteImageFlag) {
        this.getDeleteImageFlag = getDeleteImageFlag;
    }

    public interface GetDeleteImageFlag {
        public void result(boolean flag);
    }
}
