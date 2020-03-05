package com.xiandao.android.entity;

import android.content.Context;

import com.xiandao.android.net.RequestVo;

import org.json.JSONException;

/**
 * 此类描述的是:解析工具抽象类
 *
 * @author TanYong
 *         create at 2017/4/21 9:30
 */
public abstract class BaseParser<T> {
    /**
     * @param str json字符串
     * @return 返回对象
     * @throws JSONException
     */
    public abstract T parseJSON(Context context, String str) throws JSONException;

    public abstract void reloadTask(RequestVo vo) throws Exception;
}
