package com.xiandao.android.ui.fragment;

import android.animation.Animator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.idlestar.ratingstar.RatingStarView;
import com.xiandao.android.R;
import com.xiandao.android.entity.eventbus.EventBusMessage;
import com.xiandao.android.entity.smart.BaseEntity;
import com.xiandao.android.entity.smart.OperationInfoEntity;
import com.xiandao.android.entity.smart.WorkflowOrderActionType;
import com.xiandao.android.http.JsonParse;
import com.xiandao.android.http.MyCallBack;
import com.xiandao.android.http.XUtils;
import com.xiandao.android.ui.BaseFragment;
import com.xiandao.android.utils.AnimationUtil;

import org.greenrobot.eventbus.EventBus;
import org.xutils.LogUtils;
import org.xutils.event.annotation.ContentView;
import org.xutils.event.annotation.Event;
import org.xutils.event.annotation.ViewInject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.xiandao.android.base.Config.BASE_URL;
import static com.xiandao.android.base.Config.WORKORDER;


/**
 * Created by Loong on 2020/2/21.
 * Version: 1.0
 * Describe:
 */

@ContentView(R.layout.workflow_action_layout)
public class WorkflowActionFragment extends BaseFragment {

    public WorkflowActionFragment() {
    }
    public WorkflowActionFragment newInstance(Bundle bundle) {
        WorkflowActionFragment fragment = new WorkflowActionFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @ViewInject(R.id.workflow_action_name)
    private TextView workflow_action_name;
    @ViewInject(R.id.workflow_action_toggle_icon)
    private ImageView workflow_action_toggle_icon;
    @ViewInject(R.id.workflow_action_content)
    private FrameLayout workflow_action_content;

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

    private boolean toggle=true;

    private String action,workOrderId,operationName;
    private List<OperationInfoEntity> operationInfoEntities;
    private WorkflowOrderActionType actionType;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        action = getArguments().getString("action");
        workOrderId = getArguments().getString("workOrderId");
        operationName = getArguments().getString("operationName");
        operationInfoEntities = (List<OperationInfoEntity>) getArguments().getSerializable("operationInfos");
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
            R.id.pay_confirm,R.id.comment_confirm,R.id.workflow_action_toggle})
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
            case R.id.workflow_action_toggle:
                toggle=!toggle;
                int width = View.MeasureSpec.makeMeasureSpec(0,
                        View.MeasureSpec.UNSPECIFIED);
                int height = View.MeasureSpec.makeMeasureSpec(0,
                        View.MeasureSpec.UNSPECIFIED);
                workflow_action_content.measure(width, height);
                if(toggle){
                    workflow_action_content.setVisibility(View.VISIBLE);
                    AnimationUtil.startTransYAnimation(workflow_action_content,workflow_action_content.getMeasuredHeight(),0,200,new AnimationUtil.AnimationListener(){
                        @Override
                        public void onAnimationStart(Animator animation, boolean isReverse) {
                            super.onAnimationStart(animation, isReverse);
                            AnimationUtil.startRotateAnimation(workflow_action_toggle_icon,180,0,200);
                        }
                    });
                }else{
                    AnimationUtil.startTransYAnimation(workflow_action_content,0,workflow_action_content.getMeasuredHeight(),200,new AnimationUtil.AnimationListener(){
                        @Override
                        public void onAnimationStart(Animator animation, boolean isReverse) {
                            super.onAnimationStart(animation, isReverse);
                            AnimationUtil.startRotateAnimation(workflow_action_toggle_icon,0,180,200);

                        }

                        @Override
                        public void onAnimationEnd(Animator animation, boolean isReverse) {
                            super.onAnimationEnd(animation, isReverse);
                            workflow_action_content.setVisibility(View.GONE);
                        }
                    });
                }
                break;
        }
    }

    //客户接受价格
    private void acceptPrice(int result){
        startProgressDialog("");
        Map<String,Object> map=new HashMap<>();
        map.put("operationId",operationInfoEntities.get(result).getId());
        map.put("operationName",operationName);
        map.put("result",result==0?1:0);
        map.put("workOrderId",workOrderId);
        XUtils.PostJson(BASE_URL+WORKORDER+"work/order/api/owner/acceptPrice",map,myCallBack);
    }

    //客户确认完成员工完成工作
    private void confirmFinish(){
        startProgressDialog("");
        Map<String,Object> map=new HashMap<>();
        map.put("operationId",operationInfoEntities.get(0).getId());
        map.put("operationName",operationName);
        map.put("workOrderId",workOrderId);
        XUtils.PostJson(BASE_URL+WORKORDER+"work/order/api/owner/confirmWorkOrderFinish",map,myCallBack);
    }
    //客户付费
    private void pay(){
        startProgressDialog("");
        Map<String,Object> map=new HashMap<>();
        map.put("operationId",operationInfoEntities.get(0).getId());
        map.put("operationName",operationName);
        map.put("workOrderId",workOrderId);
        XUtils.PostJson(BASE_URL+WORKORDER+"work/order/api/owner/payWorkOrderMoney",map,myCallBack);
    }
    //客户评价
    private void comment(){
        if(TextUtils.isEmpty(comment_content.getText())){
            showToast("请输入评价内容");
            return;
        }
        startProgressDialog("");
        Map<String,Object> map=new HashMap<>();
        map.put("operationId",operationInfoEntities.get(0).getId());
        map.put("operationName",operationName);
        map.put("workOrderId",workOrderId);
        map.put("commentLevel",comment_rate.getRating()+"");
        map.put("comment",comment_content.getText().toString());
        XUtils.PostJson(BASE_URL+WORKORDER+"work/order/api/owner/commentWorkOrder",map,myCallBack);
    }

    private MyCallBack myCallBack=new MyCallBack<String>(){
        @Override
        public void onSuccess(String result) {
            super.onSuccess(result);
            LogUtils.d(result);
            BaseEntity baseEntity= JsonParse.parse(result);
            if(baseEntity.isSuccess()){
                EventBus.getDefault().post(new EventBusMessage<>("OrderDetailRefresh"));
            }else{
                showToast(baseEntity.getMessage());
            }

        }

        @Override
        public void onError(Throwable ex, boolean isOnCallback) {
            super.onError(ex, isOnCallback);
            showToast(ex.getMessage());
        }

        @Override
        public void onFinished() {
            super.onFinished();
            stopProgressDialog();
        }
    };

}
