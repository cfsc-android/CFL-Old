package com.xiandao.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiandao.android.R;
import com.xiandao.android.entity.PersonalInfomation;
import com.xiandao.android.entity.smart.FaceEntity;
import com.xiandao.android.utils.XUtilsImageUtils;

import java.util.List;

/**
 * Created by zengx on 2019/6/13.
 * Describe:
 */
public class FaceCollectionListAdapter extends BaseAdapter {
    private Context context;
    private List<FaceEntity> list;

    public FaceCollectionListAdapter(Context context, List<FaceEntity> list) {
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
        ViewHolder holder;
        if(convertView==null){
            convertView=LayoutInflater.from(context).inflate(R.layout.item_face_collection_list,null);
            holder=new ViewHolder();
            holder.pic= (ImageView) convertView.findViewById(R.id.iv_item_face_collection_pic);
            holder.name= (TextView) convertView.findViewById(R.id.tv_item_face_collection_name);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        FaceEntity face=list.get(position);
        holder.name.setText(face.getNickname());
        if(face.getFaceUrl()!=null){
            XUtilsImageUtils.display(holder.pic,face.getFaceUrl(),true);
        }
        return convertView;
    }

    public class ViewHolder{
        ImageView pic;
        TextView name;
    }

}
