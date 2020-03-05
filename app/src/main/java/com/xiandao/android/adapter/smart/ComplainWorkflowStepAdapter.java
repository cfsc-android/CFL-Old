package com.xiandao.android.adapter.smart;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiandao.android.R;
import com.xiandao.android.entity.smart.WorkflowComplainEntity;


import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by Loong on 2020/2/20.
 * Version: 1.0
 * Describe:
 */
public class ComplainWorkflowStepAdapter extends BaseQuickAdapter<WorkflowComplainEntity, BaseViewHolder> {
    private Context context;
    public ComplainWorkflowStepAdapter(Context context, @Nullable List<WorkflowComplainEntity> data) {
        super(R.layout.item_workflow_step_list, data);
        this.context=context;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, WorkflowComplainEntity item) {
        if(helper.getAdapterPosition()==0){
            helper.setGone(R.id.workflow_step_up_line,false);
        }else{
            helper.setGone(R.id.workflow_step_up_line,true);
        }
        helper.setText(R.id.workflow_step_date,item.getCreateTime().substring(0,10));
        helper.setText(R.id.workflow_step_time,item.getCreateTime().substring(11));
        ImageView avatar=helper.getView(R.id.workflow_step_avatar);
        Glide.with(context)
                .load(item.getAvatarUrl())
                .error(R.drawable.icon_user_default)
                .circleCrop()
                .into(avatar);
        helper.setText(R.id.workflow_step_press,item.getNodeName());
    }
}
