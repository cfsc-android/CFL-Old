package com.xiandao.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xiandao.android.R;
import com.xiandao.android.entity.VisitorRecord;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by kss on 2019/4/9
 * Describe:访客记录适配器
 */
public class VisitorRecordListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<VisitorRecord> visitorRecords;

    public VisitorRecordListAdapter(Context context, ArrayList<VisitorRecord> visitorRecords) {
        this.context = context;
        this.visitorRecords = visitorRecords;
    }

    @Override
    public int getCount() {
        return visitorRecords.size();
    }

    @Override
    public Object getItem(int position) {
        return visitorRecords.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        VisitorRecordHolder holder;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.visitor_record_list_item,null);
            holder=new VisitorRecordHolder();
            holder.visitorName = (TextView) convertView.findViewById(R.id.tv_visitor_record_name);
            holder.validTime= (TextView) convertView.findViewById(R.id.tv_visitor_record_valid_time);
            holder.valid= (TextView) convertView.findViewById(R.id.tv_visitor_record_valid);
            convertView.setTag(holder);
        }else{
            holder= (VisitorRecordHolder) convertView.getTag();
        }
        VisitorRecord record=visitorRecords.get(position);
        holder.visitorName.setText(record.getVisitorName());
        holder.validTime.setText(record.getExpireTime());
        String valid="";
        try {
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if(new Date().getTime()>sdf.parse(record.getExpireTime()).getTime()){
                valid="已作废";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.valid.setText(valid);
        return convertView;
    }

    public class VisitorRecordHolder{
        TextView visitorName;//访客名
        TextView validTime;//有效期
        TextView valid;//是否有效
    }
}
