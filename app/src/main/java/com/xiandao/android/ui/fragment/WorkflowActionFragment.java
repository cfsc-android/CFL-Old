package com.xiandao.android.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.idlestar.ratingstar.RatingStarView;
import com.shizhefei.fragment.BaseFragment;
import com.xiandao.android.R;
import com.xiandao.android.entity.smart.WorkflowOrderActionType;

import org.xutils.event.annotation.ContentView;
import org.xutils.event.annotation.Event;
import org.xutils.event.annotation.ViewInject;

import androidx.annotation.Nullable;

/**
 * Created by Loong on 2020/2/21.
 * Version: 1.0
 * Describe:
 */

@ContentView(R.layout.workflow_action_layout)
public class WorkflowActionFragment extends BaseFragment {

    private static final String ARG = "action";
    public WorkflowActionFragment() {
    }
    public WorkflowActionFragment newInstance(String action) {
        WorkflowActionFragment fragment = new WorkflowActionFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG, action);
        fragment.setArguments(bundle);
        return fragment;
    }


    @ViewInject(R.id.workflow_action_name)
    private TextView workflow_action_name;
    //是否接受价格
    @ViewInject(R.id.workflow_action_accept_price)
    private LinearLayout workflow_action_accept_price;
    //确认完成
    @ViewInject(R.id.workflow_action_confirm_finish)
    private LinearLayout workflow_action_confirm_finish;
    //付费
    @ViewInject(R.id.workflow_action_pay)
    private LinearLayout workflow_action_pay;
    //评价
    @ViewInject(R.id.workflow_action_comment)
    private LinearLayout workflow_action_comment;
    @ViewInject(R.id.comment_rate)
    private RatingStarView comment_rate;
    @ViewInject(R.id.comment_content)
    private EditText comment_content;

    private String action;
    private WorkflowOrderActionType actionType;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        action = getArguments().getString(ARG);
        WorkflowOrderActionType[] types=WorkflowOrderActionType.values();
        for (int i = 0; i < types.length; i++) {
            if(action.equals(types[i].name())){
                actionType=types[i];
            }
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        workflow_action_name.setText(action);
        switch (actionType){
            case 客户接受价格:
                workflow_action_accept_price.setVisibility(View.VISIBLE);
                break;
            case 客户确认工作完成:
                workflow_action_confirm_finish.setVisibility(View.VISIBLE);
                break;
            case 付费:
                workflow_action_pay.setVisibility(View.VISIBLE);
                break;
            case 评价:
                workflow_action_comment.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Event({R.id.accept_price_reject,R.id.accept_price_confirm,R.id.confirm_finish_confirm,
            R.id.pay_confirm,R.id.comment_confirm})
    private void onClickEvent(View v){
        switch (v.getId()){
            case R.id.accept_price_reject:
                acceptPrice(1);
                break;
            case R.id.accept_price_confirm:
                acceptPrice(0);
                break;
            case R.id.confirm_finish_confirm:
                confirmFinish();
                break;
            case R.id.pay_confirm:
                pay();
                break;
            case R.id.comment_confirm:
                comment();
                break;

        }
    }

    private void acceptPrice(int result){

    }
    private void confirmFinish(){

    }
    private void pay(){

    }
    private void comment(){
        float rate=comment_rate.getRating();
        String comment=comment_content.getText().toString();
    }
}
