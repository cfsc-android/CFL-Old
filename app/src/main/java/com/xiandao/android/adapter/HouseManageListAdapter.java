package com.xiandao.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xiandao.android.R;
import com.xiandao.android.entity.smart.RoomEntity;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by zengx on 2019/5/22.
 * Describe:
 */
public class HouseManageListAdapter extends BaseAdapter {
    private Context context;

    private LayoutInflater mInflater;
    private List<RoomEntity> roomManageList;
    private ArrayList<Integer> checkList=new ArrayList<>();
    private Boolean edit;

    public HouseManageListAdapter(Context context, List<RoomEntity> roomManageList) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.roomManageList = roomManageList;
        if(roomManageList != null){
            for (int i = 0; i < this.roomManageList.size(); i++) {
                checkList.add(0);
            }
        }else{
            roomManageList = new ArrayList<>();
        }

        this.edit=false;
    }

    @Override
    public int getCount() {
        return roomManageList.size();
    }

    @Override
    public Object getItem(int position) {
        return roomManageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setEdit(boolean edit){
        this.edit=edit;
        notifyDataSetChanged();
    }

    public boolean getEdit(){
        return this.edit;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        RoomManageHolder roomManageHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.house_manage_list_item, null);
            roomManageHolder = new RoomManageHolder();
            roomManageHolder.projectName= (TextView) convertView.findViewById(R.id.tv_house_manage_project_name);
            roomManageHolder.fullName = (TextView) convertView.findViewById(R.id.tv_house_manage_full_name);

            convertView.setTag(roomManageHolder);
        } else {
            roomManageHolder = (RoomManageHolder) convertView.getTag();
        }
        RoomEntity roomManage = roomManageList.get(position);

        roomManageHolder.projectName.setText(roomManage.getProjectName());
        roomManageHolder.fullName.setText(roomManage.getFullName());


//        carManageHolder.check.setVisibility(edit?View.VISIBLE:View.GONE);
//        carManageHolder.check.setImageResource(checkList.get(position)==0?R.drawable.check_normal:R.drawable.check_checked);
//        carManageHolder.check.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(checkList.get(position)==0){
//                    checkList.set(position,1);
//                }else{
//                    checkList.set(position,0);
//                }
//                notifyDataSetChanged();
//            }
//        });

        return convertView;
    }



    public class RoomManageHolder {
        TextView projectName;//项目分期
        TextView fullName;//牌照类型

    }
}
