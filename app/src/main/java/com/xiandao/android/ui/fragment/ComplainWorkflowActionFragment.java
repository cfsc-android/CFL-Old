package com.xiandao.android.ui.fragment;

import android.animation.Animator;
import android.os.Bundle;
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
import com.xiandao.android.entity.smart.WorkflowComplainActionType;
import com.xiandao.android.entity.smart.WorkflowTrendType;
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

import androidx.annotation.Nullable;

import static com.xiandao.android.base.Config.BASE_URL;
import static com.xiandao.android.base.Config.WORKORDER;


/**
 * Created by Loong on 2020/2/21.
 * Version: 1.0
 * Describe:
 */

@ContentView(R.layout.workflow_action_layout)
public class ComplainWorkflowActionFragment extends BaseFragment {
    public ComplainWorkflowActionFragment() {
    }
    public ComplainWorkflowActionFragment newInstance(Bundle bundle) {
        ComplainWorkflowActionFragment fragment = new ComplainWorkflowActionFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @ViewInject(R.id.workflow_action_name)
    private TextView workflow_action_name;
    @ViewInject(R.id.workflow_action_toggle_icon)
    private ImageView workflow_action_toggle_icon;
    @ViewInject(R.id.workflow_action_content)
    private FrameLayout workflow_action_content;

    //职能主管填写解决方案
    @ViewInject(R.id.workflow_action_write_plan)
    private LinearLayout workflow_action_write_plan;

    //评价
    @ViewInject(R.id.workflow_action_complain_comment)
    private LinearLayout workflow_action_complain_comment;
    @ViewInject(R.id.complain_comment_rate)
    private RatingStarView complain_comment_rate;
    @ViewInject(R.id.complain_comment_content)
    private EditText complain_comment_content;


    private String action,complaintId;
    private List<OperationInfoEntity> operationInfoEntities;
    private WorkflowComplainActionType actionType;
    private boolean toggle=true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        action = getArguments().getString("action");
        complaintId = getArguments().getString("complaintId");
        operationInfoEntities = (List<OperationInfoEntity>) getArguments().getSerializable("operationInfos");
        WorkflowComplainActionType[] types=WorkflowComplainActionType.values();
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
            case 业主确认方案:
                workflow_action_write_plan.setVisibility(View.VISIBLE);
                break;
            case 投诉评价:
                workflow_action_complain_comment.setVisibility(View.VISIBLE);
                break;
        }
    }


    @Event({R.id.workflow_action_toggle,R.id.write_plan_household_reject,R.id.write_plan_household_accept,R.id.complain_comment_confirm})
    private void onClickEvent(View v){
        switch (v.getId()){
            case R.id.write_plan_household_reject:
                householdAcceptPlan(WorkflowTrendType.Reject);
                break;
            case R.id.write_plan_household_accept:
                householdAcceptPlan(WorkflowTrendType.Accept);
                break;
            case R.id.complain_comment_confirm:
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


    //已填写解决方案
    private void householdAcceptPlan(WorkflowTrendType type){
        startProgressDialog("");
        Map<String,Object> map=new HashMap<>();
        for (int i = 0; i < operationInfoEntities.size(); i++) {
//            String trend=operationInfoEntities.get(i).getChoose();
//            if(trend.equals(type.getType())){
//                map.put("operationId",operationInfoEntities.get(i).getId());
//                map.put("operationName",operationInfoEntities.get(i).getDesc());
//            }
        }
        if(type==WorkflowTrendType.Accept){
            map.put("accept",1);
        }else{
            map.put("accept",0);
        }
        map.put("complaintId",complaintId);
        XUtils.PostJson(BASE_URL+WORKORDER+"work/complaintOwner/communicateOwner",map,myCallBack);
    }
    //客户评价
    private void comment(){
        if(TextUtils.isEmpty(complain_comment_content.getText())){
            showToast("请输入评价内容");
            return;
        }
        startProgressDialog("");
        Map<String,Object> map=new HashMap<>();
        map.put("operationId",operationInfoEntities.get(0).getId());
        map.put("operationName",operationInfoEntities.get(0).getDesc());
        map.put("complaintId",complaintId);
        map.put("commentLevel",complain_comment_rate.getRating()+"");
        map.put("comment",complain_comment_content.getText().toString());
        XUtils.PostJson(BASE_URL+WORKORDER+"work/complaintOwner/customCommentComplain",map,myCallBack);
    }
    private MyCallBack myCallBack=new MyCallBack<String>(){
        @Override
        public void onSuccess(String result) {
            super.onSuccess(result);
            LogUtils.d(result);
            BaseEntity baseEntity= JsonParse.parse(result);
            if(baseEntity.isSuccess()){
                EventBus.getDefault().post(new EventBusMessage<>("ComplainDetailRefresh"));
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
