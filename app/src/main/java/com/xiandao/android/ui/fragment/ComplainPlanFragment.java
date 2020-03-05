package com.xiandao.android.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.xiandao.android.R;
import com.xiandao.android.adapter.PlanAdapter;
import com.xiandao.android.entity.LoginUserEntity;
import com.xiandao.android.entity.PlanEntity;
import com.xiandao.android.entity.QueryPlanResult;
import com.xiandao.android.net.ApiHttpResult;
import com.xiandao.android.net.HttpUtils;
import com.xiandao.android.net.NetUtil;
import com.xiandao.android.ui.BaseLazyFragment;
import com.xiandao.android.ui.activity.LoginActivity;
import com.xiandao.android.utils.FileManagement;
import com.xiandao.android.utils.Tools;
import com.xiandao.android.view.NoScrollListView;

import org.xutils.x;

import java.util.ArrayList;

/**
 * 此类描述的是:投诉进度fragment
 *
 * @author TanYong
 *         create at 2017/6/2 10:39
 */
public class ComplainPlanFragment extends BaseLazyFragment {

    private LoginUserEntity loginUserEntity;
    private String complainId;//工单ID

    private ArrayList<PlanEntity> planEntityArrayList = new ArrayList<>();//进度对象集合
    private PlanAdapter planAdapter;
    private ListView lv_plan;

    @Override
    protected void injectView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_complain_plan, null);
        x.view().inject(this, view);
        setContentView(view);
        if (Tools.isUserLogin()) {
            loginUserEntity = FileManagement.getLoginUserEntity();
            lv_plan = (ListView) view.findViewById(R.id.lv_plan);
            init();
        } else {
            openActivity(LoginActivity.class);
            FileManagement.saveTokenInfo("");
            getActivity().finish();
        }
    }

    private void init() {
        Bundle bundle = getArguments();
        planAdapter = new PlanAdapter(getActivity(), planEntityArrayList);
        lv_plan.setAdapter(planAdapter);
        if (null != bundle) {
            complainId = bundle.getString("complainId");
        }
        queryComplainPlan();
    }

    /**
     * @author TanYong
     * create at 2017/6/2 10:43
     * TODO：查询投诉进度
     */
    private void queryComplainPlan() {
        startProgressDialog("");
        ApiHttpResult.queryMyComplainPlan(getActivity(), new String[]{"appMobile", "complainId"},
                new Object[]{loginUserEntity.getMobileNumber(), complainId},
                new HttpUtils.DataCallBack<Object>() {
                    @Override
                    public void callBack(Object o) {
                        stopProgressDialog();
                        if (o != null) {
                            QueryPlanResult planResult = (QueryPlanResult) o;
                            if ("0".equals(planResult.getResultCode()) && planResult.getPlanEntityArrayList() != null
                                    && planResult.getPlanEntityArrayList().size() > 0) {
                                if (planEntityArrayList.size() > 0)
                                    planEntityArrayList.clear();
                                planEntityArrayList.addAll(planResult.getPlanEntityArrayList());
                                planAdapter.notifyDataSetChanged();
                            } else {
                                Tools.showPrompt(planResult.getMsg());
                            }
                        } else {
                            NetUtil.toNetworkSetting(getActivity());
                        }
                    }
                });
    }


}
