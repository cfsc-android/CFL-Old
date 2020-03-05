package com.xiandao.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiandao.android.R;
import com.xiandao.android.entity.NoticeEntity;
import com.xiandao.android.utils.PhotoUtils;
import com.xiandao.android.utils.Tools;

import java.util.ArrayList;

/**
 * 此类描述的是:通知公告列表adapter
 *
 * @author TanYong
 *         create at 2017/6/14 17:28
 */
public class NoticeListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private ArrayList<NoticeEntity> noticeList;

    public NoticeListAdapter(Context context, ArrayList<NoticeEntity> noticeList) {
        this.mInflater = LayoutInflater.from(context);
        this.noticeList = noticeList;
    }

    @Override
    public int getCount() {
        return noticeList.size();
    }

    @Override
    public Object getItem(int position) {
        return noticeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NoticeHolder noticeHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.notice_item, null);
            noticeHolder = new NoticeHolder();
            noticeHolder.iv_notice_item = (ImageView) convertView.findViewById(R.id.iv_notice_item);
            noticeHolder.tv_notice_title_item = (TextView) convertView.findViewById(R.id.tv_notice_title_item);
            noticeHolder.tv_notice_des_item = (TextView) convertView.findViewById(R.id.tv_notice_des_item);
            convertView.setTag(noticeHolder);
        } else {
            noticeHolder = (NoticeHolder) convertView.getTag();
        }
        NoticeEntity data = noticeList.get(position);
        if (!Tools.isEmpty(data.getNoticeImgUrl()))
            PhotoUtils.loadImage(data.getNoticeImgUrl(), noticeHolder.iv_notice_item);
        noticeHolder.tv_notice_title_item.setText(Tools.getStringValue(data.getTitle()));
        noticeHolder.tv_notice_des_item.setText(Tools.getStringValue(data.getRemark()));
        return convertView;
    }

    public class NoticeHolder {
        ImageView iv_notice_item;//公告图片
        TextView tv_notice_title_item;//公告标题
        TextView tv_notice_des_item;//公告说明
    }
}