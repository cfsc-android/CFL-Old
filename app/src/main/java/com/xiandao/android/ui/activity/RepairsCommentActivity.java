package com.xiandao.android.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiandao.android.R;
import com.xiandao.android.entity.OverallSituationEntity;
import com.xiandao.android.net.ApiHttpResult;
import com.xiandao.android.net.HttpUtils;
import com.xiandao.android.net.NetUtil;
import com.xiandao.android.ui.BaseActivity;
import com.xiandao.android.utils.Tools;

import org.xutils.event.annotation.ContentView;
import org.xutils.event.annotation.ViewInject;


/**
 * 此类描述的是:工单评价界面
 *
 * @author TanYong
 *         create at 2017/5/26 22:33
 */
@ContentView(R.layout.activity_repairs_comment)
public class RepairsCommentActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.iv_title_left)
    private ImageView iv_title_left;
    @ViewInject(R.id.tv_title_name)
    private TextView tv_title_name;

    @ViewInject(R.id.tv_comment_result)
    private TextView tv_comment_result;
    @ViewInject(R.id.iv_star_1)
    private ImageView iv_star_1;
    @ViewInject(R.id.iv_star_2)
    private ImageView iv_star_2;
    @ViewInject(R.id.iv_star_3)
    private ImageView iv_star_3;
    @ViewInject(R.id.iv_star_4)
    private ImageView iv_star_4;
    @ViewInject(R.id.iv_star_5)
    private ImageView iv_star_5;
    @ViewInject(R.id.et_comment_des)
    private EditText et_comment_des;
    @ViewInject(R.id.btn_comment_commit)
    private Button btn_comment_commit;

    private int commentLevel = 5;//评分

    private String workOrderId;
    private String taskId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        init();
    }

    private void init() {
        Intent intent = getIntent();
        if (null != intent) {
            workOrderId = intent.getStringExtra("workOrderId");
            taskId = intent.getStringExtra("taskId");
        }
        tv_title_name.setText("工单评价");
        iv_star_1.setOnClickListener(this);
        iv_star_2.setOnClickListener(this);
        iv_star_3.setOnClickListener(this);
        iv_star_4.setOnClickListener(this);
        iv_star_5.setOnClickListener(this);
        iv_title_left.setOnClickListener(this);
        btn_comment_commit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_left:
                this.finish();
                break;
            case R.id.iv_star_1:
                iv_star_1.setImageResource(R.mipmap.star_yellow);
                iv_star_2.setImageResource(R.mipmap.star_gray);
                iv_star_3.setImageResource(R.mipmap.star_gray);
                iv_star_4.setImageResource(R.mipmap.star_gray);
                iv_star_5.setImageResource(R.mipmap.star_gray);
                commentLevel = 1;
                tv_comment_result.setText("非常差");
                break;
            case R.id.iv_star_2:
                iv_star_1.setImageResource(R.mipmap.star_yellow);
                iv_star_2.setImageResource(R.mipmap.star_yellow);
                iv_star_3.setImageResource(R.mipmap.star_gray);
                iv_star_4.setImageResource(R.mipmap.star_gray);
                iv_star_5.setImageResource(R.mipmap.star_gray);
                commentLevel = 2;
                tv_comment_result.setText("差");
                break;
            case R.id.iv_star_3:
                iv_star_1.setImageResource(R.mipmap.star_yellow);
                iv_star_2.setImageResource(R.mipmap.star_yellow);
                iv_star_3.setImageResource(R.mipmap.star_yellow);
                iv_star_4.setImageResource(R.mipmap.star_gray);
                iv_star_5.setImageResource(R.mipmap.star_gray);
                commentLevel = 3;
                tv_comment_result.setText("一般");
                break;
            case R.id.iv_star_4:
                iv_star_1.setImageResource(R.mipmap.star_yellow);
                iv_star_2.setImageResource(R.mipmap.star_yellow);
                iv_star_3.setImageResource(R.mipmap.star_yellow);
                iv_star_4.setImageResource(R.mipmap.star_yellow);
                iv_star_5.setImageResource(R.mipmap.star_gray);
                commentLevel = 4;
                tv_comment_result.setText("好");
                break;
            case R.id.iv_star_5:
                iv_star_1.setImageResource(R.mipmap.star_yellow);
                iv_star_2.setImageResource(R.mipmap.star_yellow);
                iv_star_3.setImageResource(R.mipmap.star_yellow);
                iv_star_4.setImageResource(R.mipmap.star_yellow);
                iv_star_5.setImageResource(R.mipmap.star_yellow);
                commentLevel = 5;
                tv_comment_result.setText("非常好");
                break;
            case R.id.btn_comment_commit:
                commentWorkOrder();
                break;
            default:
                break;
        }
    }

    /**
     * @author TanYong
     * create at 2017/5/27 0:49
     * TODO：评价工单
     */
    private void commentWorkOrder() {
        ApiHttpResult.commentWorkOrder(this, new String[]{"appMobile", "workOrderId", "userId", "taskId", "outcome", "commentContent", "commentLevel"},
                new Object[]{loginUserEntity.getMobileNumber(), workOrderId, loginUserEntity.getUserId(),
                        taskId, "评价", et_comment_des.getText().toString(), commentLevel},
                new HttpUtils.DataCallBack<Object>() {
                    @Override
                    public void callBack(Object o) {
                        stopProgressDialog();
                        if (o != null) {
                            OverallSituationEntity osEntity = (OverallSituationEntity) o;
                            if ("0".equals(osEntity.getResultCode())) {
                                RepairsCommentActivity.this.finish();
                            }
                            Tools.showPrompt(osEntity.getMsg());
                        } else {
                            NetUtil.toNetworkSetting(RepairsCommentActivity.this);
                        }
                    }
                });
    }
}
