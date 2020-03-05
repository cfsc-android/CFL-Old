package com.xiandao.android.adapter;

import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;
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
public class NewAddImageGridViewAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<TImage> tImages;
    private GridView gridView;
    private int maxImages = 3;
    private LayoutInflater inflater;

    public NewAddImageGridViewAdapter(Context context, ArrayList<TImage> tImages, GridView gridView) {
        this.context = context;
        this.tImages = tImages;
        this.gridView = gridView;
        inflater = LayoutInflater.from(context);
    }

    /**
     * 获取最大图片张数
     *
     * @return
     */
    public int getMaxImages() {
        return maxImages;
    }

    /**
     * 设置最大图片张数
     *
     * @param maxImages
     */
    public void setMaxImages(int maxImages) {
        this.maxImages = maxImages;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.image_gridview_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        /**代表+号之前的需要正常显示图片**/
        if (tImages != null && position < tImages.size()) {
            holder.ivImageCloseItem.setVisibility(View.VISIBLE);
            String imagePath = tImages.get(position).getOriginalPath().replace("/external_storage_root", "");
            if (imagePath.indexOf(Environment.getExternalStorageDirectory() + "") == -1) {
                imagePath = Environment.getExternalStorageDirectory() + imagePath;
            }
            Glide.with(context).load(new File(imagePath)).into(holder.ivImageGridviewItem);
            holder.ivImageCloseItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tImages.remove(position);
                    Tools.setGridViewHeightBasedOnChildren(gridView);
                    notifyDataSetChanged();
                }
            });
        } else {
            /**代表+号的需要+号图片显示图片**/
            Glide.with(context).load(R.mipmap.tousu_btn_zj).priority(Priority.HIGH).into(holder.ivImageGridviewItem);
            holder.ivImageCloseItem.setVisibility(View.GONE);
        }
        return convertView; // 返回ImageView
    }

    @Override
    public Object getItem(int position) {
        return tImages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        if (tImages.size() == 0) {
            return 1;
        } else if (tImages.size() < maxImages) {
            return tImages.size() + 1;
        } else {
            return maxImages;
        }
    }

    public class ViewHolder {
        ImageView ivImageGridviewItem;
        ImageView ivImageCloseItem;
        public final View root;

        public ViewHolder(View root) {
            ivImageGridviewItem = (ImageView) root.findViewById(R.id.iv_image_gridview_item);
            ivImageCloseItem = (ImageView) root.findViewById(R.id.iv_image_close_item);
            this.root = root;
        }
    }
}
