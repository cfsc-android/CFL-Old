package com.xiandao.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiandao.android.R;
import com.xiandao.android.entity.MyRepairsEntity;
import com.xiandao.android.utils.Tools;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * 此类描述的是:我的报修列表适配
 *
 * @author TanYong
 *         create at 2017/5/8 16:04
 */
public class MyRepairsListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private ArrayList<MyRepairsEntity> myrepairsListEntities;

    public MyRepairsListAdapter(Context context, ArrayList<MyRepairsEntity> myrepairsListEntities) {
        this.mInflater = LayoutInflater.from(context);
        this.myrepairsListEntities = myrepairsListEntities;
    }

    @Override
    public int getCount() {
        return myrepairsListEntities.size();
    }

    @Override
    public Object getItem(int position) {
        return myrepairsListEntities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        repairsHolder repairsHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.my_repairs_list_item, null);
            repairsHolder = new repairsHolder();
            repairsHolder.ivMyRepairs = (ImageView) convertView.findViewById(R.id.iv_my_repairs_list_url);
            repairsHolder.tvRepairsTitle = (TextView) convertView.findViewById(R.id.tv_repairs_title);
            repairsHolder.tv_repairs_address = (TextView) convertView.findViewById(R.id.tv_repairs_address);
            repairsHolder.tv_repairs_id = (TextView) convertView.findViewById(R.id.tv_repairs_id);
            repairsHolder.tvRepairsDateTime = (TextView) convertView.findViewById(R.id.tv_repairs_datetime);
            repairsHolder.tvRepairsStatus = (TextView) convertView.findViewById(R.id.tv_repairs_status);
            convertView.setTag(repairsHolder);
        } else {
            repairsHolder = (repairsHolder) convertView.getTag();
        }
        MyRepairsEntity data = myrepairsListEntities.get(position);
        repairsHolder.ivMyRepairs.setImageResource(R.drawable.btn_public_phone);
        repairsHolder.tvRepairsTitle.setText(data.getRepairsTitle());
        repairsHolder.tv_repairs_id.setVisibility(View.GONE);
        repairsHolder.tv_repairs_address.setVisibility(View.GONE);
        repairsHolder.tv_repairs_id.setText("编号：" + Tools.getStringValue(data.getRepairsTime()).substring(2, 4) + "/" + data.getRepairsId());
        if (!Tools.isEmpty(data.getAddress())) {
            repairsHolder.tv_repairs_address.setText("地址：" + data.getAddress());
        }
        Date date = new Date(data.getRepairsTime().replace("-", "/"));
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月DD日 HH:mm:ss");
        String dateString = format.format(date.getTime());
        repairsHolder.tvRepairsDateTime.setText(dateString);
        repairsHolder.tvRepairsStatus.setText(data.getRepairsStatus());
        return convertView;
    }

    public class repairsHolder {
        ImageView ivMyRepairs;//投诉图片
        TextView tvRepairsTitle;//投诉标题
        TextView tv_repairs_id;//工单id
        TextView tv_repairs_address;//工单地址
        TextView tvRepairsDateTime;//投诉时间
        TextView tvRepairsStatus;//投诉状态
    }
}