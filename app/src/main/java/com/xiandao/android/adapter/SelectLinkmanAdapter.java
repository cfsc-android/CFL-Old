package com.xiandao.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiandao.android.R;
import com.xiandao.android.entity.LinkmanEntity;

import java.util.ArrayList;

/**
 * 此类描述的是:选择联系人adapter
 *
 * @author TanYong
 *         create at 2017/5/18 10:39
 */
public class SelectLinkmanAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private ArrayList<LinkmanEntity> linkmanEntities;

    public SelectLinkmanAdapter(Context context, ArrayList<LinkmanEntity> linkmanEntities) {
        this.mInflater = LayoutInflater.from(context);
        this.linkmanEntities = linkmanEntities;
    }

    @Override
    public int getCount() {
        return linkmanEntities.size();
    }

    @Override
    public Object getItem(int position) {
        return linkmanEntities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final SelectLinkmanHolder linkmanHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.select_linkman_item, null);
            linkmanHolder = new SelectLinkmanHolder();
            linkmanHolder.tv_select_linkman_name = (TextView) convertView.findViewById(R.id.tv_select_linkman_name);
            linkmanHolder.tv_select_linkman_mobile_no = (TextView) convertView.findViewById(R.id.tv_select_linkman_phone);
            linkmanHolder.iv_select_linkman_relationship = (ImageView) convertView.findViewById(R.id.iv_select_linkman_status);
            linkmanHolder.cb_select_linkman = (CheckBox) convertView.findViewById(R.id.cb_select_linkman);
            convertView.setTag(linkmanHolder);
        } else {
            linkmanHolder = (SelectLinkmanHolder) convertView.getTag();
        }
        final LinkmanEntity data = linkmanEntities.get(position);
        linkmanHolder.tv_select_linkman_name.setText(data.getLinkmanName());
        linkmanHolder.tv_select_linkman_mobile_no.setText(data.getLinkmanMobileNo());
        if ("业主".equals(data.getRelationship())) {
            linkmanHolder.iv_select_linkman_relationship.setImageResource(R.mipmap.xzlxr_icon_yezhu);
        } else if ("家属".equals(data.getRelationship())) {
            linkmanHolder.iv_select_linkman_relationship.setImageResource(R.mipmap.xzlxr_iocn_jiashu);
        }
        if (data.getCheckedFlag() == 1) {
            linkmanHolder.cb_select_linkman.setChecked(true);
        } else if (data.getCheckedFlag() == 2) {
            linkmanHolder.cb_select_linkman.setChecked(false);
        }
        return convertView;
    }

    public class SelectLinkmanHolder {
        TextView tv_select_linkman_name;//联系人姓名
        TextView tv_select_linkman_mobile_no;//联系人电话
        ImageView iv_select_linkman_relationship;//联系人身份
        CheckBox cb_select_linkman;//选中按钮
    }
}