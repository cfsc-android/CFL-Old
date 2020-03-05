package com.xiandao.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiandao.android.R;
import com.xiandao.android.entity.PlanEntity;
import com.xiandao.android.utils.Tools;

import java.util.ArrayList;

/**
 * 此类描述的是:进度适配
 *
 * @author TanYong
 * create at 2017/5/8 16:04
 */
public class PlanAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private ArrayList<PlanEntity> planEntityArrayList;

    public PlanAdapter(Context context, ArrayList<PlanEntity> planEntityArrayList) {
        this.mInflater = LayoutInflater.from(context);
        this.planEntityArrayList = planEntityArrayList;
    }

    @Override
    public int getCount() {
        return planEntityArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return planEntityArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PlanHolder planHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_plan, null);
            planHolder = new PlanHolder();
            planHolder.ll_left = (LinearLayout) convertView.findViewById(R.id.ll_left);
            planHolder.tv_plan_left_datetime = (TextView) convertView.findViewById(R.id.tv_plan_left_datetime);
            planHolder.tv_plan_left_des = (TextView) convertView.findViewById(R.id.tv_plan_left_des);
            planHolder.tv_plan_right_datetime = (TextView) convertView.findViewById(R.id.tv_plan_right_datetime);
            planHolder.tv_plan_right_des = (TextView) convertView.findViewById(R.id.tv_plan_right_des);
            planHolder.tv_plan_left_remark = (TextView) convertView.findViewById(R.id.tv_plan_left_remark);
            planHolder.plan_line = (View) convertView.findViewById(R.id.plan_line);

            planHolder.tv_plan_date = (TextView) convertView.findViewById(R.id.tv_plan_date);
            planHolder.tv_plan_time = (TextView) convertView.findViewById(R.id.tv_plan_time);
            planHolder.tv_plan_des = (TextView) convertView.findViewById(R.id.tv_plan_des);
            planHolder.tv_plan_remake = (TextView) convertView.findViewById(R.id.tv_plan_remake);
            planHolder.iv_plan_icon = (ImageView) convertView.findViewById(R.id.iv_plan_icon);
            convertView.setTag(planHolder);
        } else {
            planHolder = (PlanHolder) convertView.getTag();
        }
        PlanEntity planEntity = planEntityArrayList.get(position);
//        if ("0".equals(planEntity.getHandlertype())) {//业主端操作的，显示在右边
//            planHolder.ll_left.setVisibility(View.INVISIBLE);
//            planHolder.tv_plan_right_datetime.setVisibility(View.VISIBLE);
//            planHolder.tv_plan_right_des.setVisibility(View.VISIBLE);
//            planHolder.tv_plan_right_datetime.setText(planEntity.getPlanDateTime());
//            planHolder.tv_plan_right_des.setText(planEntity.getPlanName());
//
//            if ("0".equals(planEntity.getNotAcceptable())) {
//                planHolder.tv_plan_right_des.setBackgroundResource(R.mipmap.tsxq_dhk_hui_right);
//            } else {
//                planHolder.tv_plan_right_des.setBackgroundResource(R.mipmap.tsxq_dhk_right);
//            }
//
//        } else {//员工端操作的，显示在左边
//            planHolder.ll_left.setVisibility(View.VISIBLE);
//            planHolder.tv_plan_right_datetime.setVisibility(View.INVISIBLE);
//            planHolder.tv_plan_right_des.setVisibility(View.INVISIBLE);
//
//            if (Tools.isEmpty(planEntity.getRemark())) {
//                planHolder.tv_plan_left_remark.setVisibility(View.GONE);
//                planHolder.plan_line.setVisibility(View.GONE);
//            } else {
//                planHolder.tv_plan_left_remark.setVisibility(View.VISIBLE);
//                planHolder.plan_line.setVisibility(View.VISIBLE);
//                planHolder.tv_plan_left_remark.setText(Tools.getStringValue(planEntity.getRemark()));
//            }
//
//            planHolder.tv_plan_left_datetime.setText(planEntity.getPlanDateTime());
//            planHolder.tv_plan_left_des.setText(planEntity.getPlanName());
//        }

        String[] split = planEntity.getPlanDateTime().split(" ");
        planHolder.tv_plan_date.setText(split[0]);
        planHolder.tv_plan_time.setText(split[1]);
        planHolder.tv_plan_des.setText(planEntity.getPlanName());
        if ("0".equals(planEntity.getHandlertype())) {//业主端操作的
            planHolder.iv_plan_icon.setImageResource(R.drawable.icon_public_user);
            planHolder.tv_plan_remake.setVisibility(View.GONE);
        } else {//员工端操作的
            planHolder.iv_plan_icon.setImageResource(R.drawable.icon_public_employee);
            planHolder.tv_plan_remake.setVisibility(View.VISIBLE);
            planHolder.tv_plan_remake.setText(planEntity.getRemark());
        }
        return convertView;
    }

    /**
     * "planDateTime": "2017-05-22 15:33:47",
     * "jobId": 62,
     * "handler": 2,
     * "piclist": [],
     * "handlertype": "0",0业主1员工:业主显示在右边，员工显示在左边
     * "planName": "工单报修",
     * "planId": 121,
     * "handlername": "tyy",
     * "notAcceptable": "1":用于判断业主是否不接受：0不接受，1接受：不接受，灰色背景
     */
    public class PlanHolder {
        LinearLayout ll_left;
        TextView tv_plan_left_datetime;//左侧时间
        TextView tv_plan_left_des;//左侧描述
        TextView tv_plan_left_remark;
        View plan_line;
        TextView tv_plan_right_datetime;//左侧时间
        TextView tv_plan_right_des;//左侧描述

        TextView tv_plan_date, tv_plan_time, tv_plan_des, tv_plan_remake;
        ImageView iv_plan_icon;
    }
}