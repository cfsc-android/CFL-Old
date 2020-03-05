package com.xiandao.android.adapter.smart;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiandao.android.R;
import com.xiandao.android.entity.smart.OrderEntity;


import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by Loong on 2020/2/17.
 * Version: 1.0
 * Describe:
 */
public class OrderListAdapter extends BaseQuickAdapter<OrderEntity, BaseViewHolder> {
    private Context context;
    public OrderListAdapter(Context context, @Nullable List<OrderEntity> data) {
        super(R.layout.item_order_list, data);
        this.context=context;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, OrderEntity item) {
        helper.setText(R.id.order_list_tv_title,item.getProblemDesc());
        helper.setText(R.id.order_list_tv_type, item.getWorkTypeName());
        helper.setText(R.id.order_list_tv_time,item.getCreateTime());
        helper.setText(R.id.order_list_tv_workflow,item.getWorkStatusName());
        helper.setText(R.id.order_list_tv_room,item.getRoomNameAll());
        ImageView picView=helper.getView(R.id.order_list_iv_pic);
        if(item.getResourceValue()!=null&&item.getResourceValue().size()>0){
            Glide.with(context)
                    .load(item.getResourceValue().get(0).getUrl())
                    .centerCrop()
                    .into(picView);
        }else{
            Glide.with(context)
                    .load(R.drawable.workflow_default)
                    .centerCrop()
                    .into(picView);
        }
    }


}
