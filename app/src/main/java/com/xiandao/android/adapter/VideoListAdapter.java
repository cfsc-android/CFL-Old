package com.xiandao.android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiandao.android.R;
import com.xiandao.android.entity.VideoEntity;
import com.xiandao.android.utils.XUtilsImageUtils;

import org.xutils.event.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

public class VideoListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<VideoEntity> list;
    private String[] pics=new String[]{
            "yxf_dm_01.png",
            "yxf_dm_02.png",
            "yxf_dm_03.png",
            "yxf_dm_04.png",
            "yxf_dm_05.png",
            "yxf_dm_06.png",
    };

    public VideoListAdapter(Context context, ArrayList<VideoEntity> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.video_list_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final VideoEntity videoEntity = list.get(position);
        holder.video_list_item_name.setText(videoEntity.getDeviceName());
        XUtilsImageUtils.display(holder.video_list_item_pic,"assets://"+pics[position%6]);
        return convertView;
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {
        @ViewInject(R.id.video_list_item_pic)
        public ImageView video_list_item_pic;
        @ViewInject(R.id.video_list_item_name)
        public TextView video_list_item_name;
        public ViewHolder(View view) {
            super(view);
            x.view().inject(this, view);
        }
    }
}
