package com.xiandao.android.httptask;

import android.content.Context;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.xiandao.android.entity.OverallSituationEntity;
import com.xiandao.android.entity.PlanEntity;
import com.xiandao.android.entity.QueryPlanResult;
import com.xiandao.android.utils.Tools;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * 此类描述的是:获取我的报修进度task
 *
 * @author TanYong
 *         create at 2017/5/2 10:45
 */
public class QueryMyRepairsPlanTask extends BaseTask {
    @Override
    public Object parseJSON(Context context, String str) throws JSONException {
        super.parseJSON(context, str);
        Log.e("QueryMyRepairsPlanTask", str);
        if (!Tools.isEmpty(str)) {
            JSONObject jsonObject = new JSONObject(str);
            String data = jsonObject.getString("data");
            if (Tools.isEmpty(data)) {
                data = "";
            }
            Type type = new TypeToken<ArrayList<PlanEntity>>() {
            }.getType();
            ArrayList<PlanEntity> planEntityArrayList = gson.fromJson(data, type);
            OverallSituationEntity overallSituationEntity = getOsEntity();
            QueryPlanResult planResult = new QueryPlanResult(planEntityArrayList, overallSituationEntity.getResultCode(), overallSituationEntity.getMsg());
            return planResult;
        } else {
            return null;
        }
    }
}
