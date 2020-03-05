package com.xiandao.android.adapter.smart;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiandao.android.R;
import com.xiandao.android.entity.smart.DeviceEntity;
import com.xiandao.android.entity.smart.EquipmentInfoBo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Loong on 2020/3/2.
 * Version: 1.0
 * Describe:
 */
public class EquipmentAdapter extends BaseQuickAdapter<DeviceEntity, BaseViewHolder> {
    private Context context;
    private List<Boolean> statusList =new ArrayList<>();
    public EquipmentAdapter(Context context, @Nullable List<DeviceEntity> data) {
        super(R.layout.item_equipment_list,data);
        this.context=context;
        for (int i = 0; i < data.size(); i++) {
            statusList.add(false);
        }
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, DeviceEntity item) {
        helper.setText(R.id.equipment_tv_name,item.getDeviceName());
        boolean status=statusList.get(helper.getAdapterPosition());
        if(status){
            helper.setText(R.id.equipment_tv_status,"门已打开");
            helper.setTextColor(R.id.equipment_tv_status,context.getResources().getColor(R.color.blue_color));
        }else{
            helper.setText(R.id.equipment_tv_status,"点击开门");
            helper.setTextColor(R.id.equipment_tv_status,context.getResources().getColor(R.color.text_warn));
        }
    }

    public void setEquipmentOpen(int position){
        statusList.set(position,true);
        notifyDataSetChanged();
    }

    public boolean getEquipmentOpen(int position){
        return statusList.get(position);
    }
}
