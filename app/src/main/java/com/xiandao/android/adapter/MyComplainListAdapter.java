package com.xiandao.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiandao.android.R;
import com.xiandao.android.entity.MyComplainEntity;

import java.util.ArrayList;

/**
 * 此类描述的是:我的投诉列表adapter
 *
 * @author TanYong
 *         create at 2017/6/1 19:09
 */
public class MyComplainListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private ArrayList<MyComplainEntity> myComplainEntities;

    public MyComplainListAdapter(Context context, ArrayList<MyComplainEntity> myComplainEntities) {
        this.mInflater = LayoutInflater.from(context);
        this.myComplainEntities = myComplainEntities;
    }

    @Override
    public int getCount() {
        return myComplainEntities.size();
    }

    @Override
    public Object getItem(int position) {
        return myComplainEntities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ComplainHolder complainHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.my_complain_list_item, null);
            complainHolder = new ComplainHolder();
            complainHolder.iv_my_complain_list = (ImageView) convertView.findViewById(R.id.iv_my_complain_list);
            complainHolder.tv_my_complain_list_title = (TextView) convertView.findViewById(R.id.tv_my_complain_list_title);
            complainHolder.tv_my_complain_list_datetime = (TextView) convertView.findViewById(R.id.tv_my_complain_list_datetime);
            complainHolder.tv_my_complain_list_status = (TextView) convertView.findViewById(R.id.tv_my_complain_list_status);
            convertView.setTag(complainHolder);
        } else {
            complainHolder = (ComplainHolder) convertView.getTag();
        }
        MyComplainEntity data = myComplainEntities.get(position);
        complainHolder.iv_my_complain_list.setImageResource(R.mipmap.wdbx_icon_bs);
        complainHolder.tv_my_complain_list_title.setText(data.getComplainTitle());
        complainHolder.tv_my_complain_list_datetime.setText(data.getComplainDateTime());
        complainHolder.tv_my_complain_list_status.setText(data.getComplainStatus());
        return convertView;
    }

    public class ComplainHolder {
        ImageView iv_my_complain_list;//投诉图片
        TextView tv_my_complain_list_title;//投诉标题
        TextView tv_my_complain_list_datetime;//投诉时间
        TextView tv_my_complain_list_status;//投诉状态
    }
}