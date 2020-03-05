package com.xiandao.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiandao.android.R;
import com.xiandao.android.entity.StoreEntity;
import com.xiandao.android.utils.PhotoUtils;
import com.xiandao.android.utils.Tools;

import java.util.ArrayList;

/**
 * 此类描述的是:商店列表adapter
 *
 * @author TanYong
 *         create at 2017/6/14 17:28
 */
public class StoreListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private ArrayList<StoreEntity> storeEntities;

    public StoreListAdapter(Context context, ArrayList<StoreEntity> storeEntities) {
        this.mInflater = LayoutInflater.from(context);
        this.storeEntities = storeEntities;
    }

    @Override
    public int getCount() {
        return storeEntities.size();
    }

    @Override
    public Object getItem(int position) {
        return storeEntities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        StoreHolder storeHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.store_list_item, null);
            storeHolder = new StoreHolder();
            storeHolder.iv_store_item = (ImageView) convertView.findViewById(R.id.iv_store_item);
            storeHolder.tv_store_title_item = (TextView) convertView.findViewById(R.id.tv_store_title_item);
            storeHolder.tv_store_address_item = (TextView) convertView.findViewById(R.id.tv_store_address_item);
            convertView.setTag(storeHolder);
        } else {
            storeHolder = (StoreHolder) convertView.getTag();
        }
        StoreEntity data = storeEntities.get(position);
        storeHolder.tv_store_title_item.setText(Tools.getStringValue(data.getName()));
        storeHolder.tv_store_address_item.setText(Tools.getStringValue(data.getAddress()));
        return convertView;
    }

    public class StoreHolder {
        ImageView iv_store_item;//商店图片
        TextView tv_store_title_item;//商店名称
        TextView tv_store_address_item;//商店地址
    }
}