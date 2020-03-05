package com.xiandao.android.httptask;

import android.content.Context;
import android.util.Log;

import com.xiandao.android.entity.OverallSituationEntity;
import com.xiandao.android.utils.Tools;

import org.json.JSONException;

/**
 * 此类描述的是:投诉--业主是否满意整改task
 *
 * @author TanYong
 *         create at 2017/5/2 10:32
 */
public class ComplainInfoConfirmResultTask extends BaseTask {
    @Override
    public Object parseJSON(Context context, String str) throws JSONException {
        super.parseJSON(context, str);
        Log.e("ComplainInfoConfirmResultTask", str);
        if (!Tools.isEmpty(str)) {
            OverallSituationEntity overallSituationEntity = getOsEntity();
            return overallSituationEntity;
        } else {
            return null;
        }
    }
}
