package com.xiandao.android.view;


import com.xiandao.android.entity.smart.NoticeEntity;

/**
 * Created by guiya on 2017/4/20.
 * 广告被点击时回调
 */
public interface OnAdConetentClickListener {
    abstract void OnAdConetentClickListener(int index, NoticeEntity noticeEntity);
}
