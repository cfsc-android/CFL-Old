package com.xiandao.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.xiandao.android.R;
import com.xiandao.android.entity.RoomInfoEntity;

import java.util.ArrayList;

/**
 * 此类描述的是:选择常用地址adapter
 *
 * @author TanYong
 *         create at 2017/5/11 20:36
 */
public class SelectCommonAddressAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private ArrayList<RoomInfoEntity> roomInfoEntities;

    public SelectCommonAddressAdapter(Context context, ArrayList<RoomInfoEntity> roomInfoEntities) {
        this.mInflater = LayoutInflater.from(context);
        this.roomInfoEntities = roomInfoEntities;
    }

    @Override
    public int getCount() {
        return roomInfoEntities.size();
    }

    @Override
    public Object getItem(int position) {
        return roomInfoEntities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final SelectCommonAddressHolder selectCommonAddressHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.select_linkman_item, null);
            selectCommonAddressHolder = new SelectCommonAddressHolder();
            selectCommonAddressHolder.tv_address_houses = (TextView) convertView.findViewById(R.id.tv_address_houses);
            selectCommonAddressHolder.tv_address_building_and_room_num = (TextView) convertView.findViewById(R.id.tv_address_building_and_room_num);
            selectCommonAddressHolder.cb_select_common_address = (CheckBox) convertView.findViewById(R.id.cb_select_common_address);
            convertView.setTag(selectCommonAddressHolder);
        } else {
            selectCommonAddressHolder = (SelectCommonAddressHolder) convertView.getTag();
        }
        final RoomInfoEntity data = roomInfoEntities.get(position);
        selectCommonAddressHolder.tv_address_houses.setText(data.getHouses());
        selectCommonAddressHolder.tv_address_building_and_room_num.setText(data.getBuilding() + data.getRoomNumber());
        if (data.getCheckedFlag() == 1) {
            selectCommonAddressHolder.cb_select_common_address.setChecked(true);
        } else if (data.getCheckedFlag() == 2) {
            selectCommonAddressHolder.cb_select_common_address.setChecked(false);
        }
        return convertView;
    }

    public class SelectCommonAddressHolder {
        TextView tv_address_houses;//楼盘
        TextView tv_address_building_and_room_num;//楼栋和房号
        CheckBox cb_select_common_address;//选中按钮
    }
}