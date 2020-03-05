package com.xiandao.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiandao.android.R;
import com.xiandao.android.entity.LifePayDataEntity;
import com.xiandao.android.entity.MyComplainEntity;
import com.xiandao.android.utils.Tools;

import java.util.ArrayList;

/**
 * 此类描述的是:生活缴费列表adapter
 *
 * @author TanYong
 *         create at 2017/6/1 19:09
 */
public class LifePayListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private ArrayList<LifePayDataEntity> data;

    public LifePayListAdapter(Context context, ArrayList<LifePayDataEntity> data) {
        this.mInflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LifePayListHolder lifePayListHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.life_pay_list_item, null);
            lifePayListHolder = new LifePayListHolder();
            lifePayListHolder.tv_payment_detail_money = (TextView) convertView.findViewById(R.id.tv_payment_detail_money);
            lifePayListHolder.tv_payment_detail_address = (TextView) convertView.findViewById(R.id.tv_payment_detail_address);
            lifePayListHolder.tv_payment_detail_date = (TextView) convertView.findViewById(R.id.tv_payment_detail_date);
            lifePayListHolder.tv_payment_detail_term = (TextView) convertView.findViewById(R.id.tv_payment_detail_term);
            convertView.setTag(lifePayListHolder);
        } else {
            lifePayListHolder = (LifePayListHolder) convertView.getTag();
        }
        LifePayDataEntity entity = data.get(position);
        lifePayListHolder.tv_payment_detail_money.setText(Tools.getTwoNumber(Double.valueOf(entity.getFee())) + "");
        lifePayListHolder.tv_payment_detail_address.setText(Tools.getStringValue(entity.getAdds()));
        lifePayListHolder.tv_payment_detail_date.setText(Tools.getStringValue(entity.getTerm()));
        lifePayListHolder.tv_payment_detail_term.setText(Tools.getStringValue(entity.getShouldpaydate()));
        return convertView;
    }

    class LifePayListHolder {
        TextView tv_payment_detail_money;//缴费金额
        TextView tv_payment_detail_address;//房屋
        TextView tv_payment_detail_date;//账期
        TextView tv_payment_detail_term;//期限
    }
}