package com.xiandao.android.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiandao.android.R;
import com.xiandao.android.entity.eventbus.EventBusMessage;
import com.xiandao.android.entity.eventbus.FaceCollectionEventBusData;
import com.xiandao.android.ui.BaseActivity;

import org.greenrobot.eventbus.EventBus;
import org.xutils.event.annotation.ContentView;
import org.xutils.event.annotation.Event;
import org.xutils.event.annotation.ViewInject;

@ContentView(R.layout.activity_payment_test)
public class PaymentTestActivity extends BaseActivity {
    @ViewInject(R.id.tv_title_name)
    private TextView tv_title_name;
    @ViewInject(R.id.tv_payment_test_no)
    private TextView tv_payment_test_no;
    @ViewInject(R.id.tv_payment_test_count)
    private TextView tv_payment_test_count;
    @ViewInject(R.id.iv_payment_test_wx_check)
    private ImageView iv_payment_test_wx_check;
    @ViewInject(R.id.iv_payment_test_ali_check)
    private ImageView iv_payment_test_ali_check;
    @ViewInject(R.id.iv_payment_test_union_check)
    private ImageView iv_payment_test_union_check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tv_title_name.setText("支付订单");
        Bundle bundle=getIntent().getExtras();
        tv_payment_test_no.setText(bundle.getString("no"));
        tv_payment_test_count.setText(bundle.getString("count"));
    }
    @Event({R.id.iv_title_left,R.id.btn_payment_test_pay,R.id.ll_payment_test_wx,R.id.ll_payment_test_ali,R.id.ll_payment_test_union})
    private void onClickEvent(View v){
        switch (v.getId()){
            case R.id.iv_title_left:
                finish();
                break;
            case R.id.btn_payment_test_pay:
                startProgressDialog("正在支付...");
                handler.sendEmptyMessageDelayed(1,1000);
                break;
            case R.id.ll_payment_test_wx:
                iv_payment_test_wx_check.setImageResource(R.drawable.payment_list_select);
                iv_payment_test_ali_check.setImageResource(R.drawable.payment_list_normal);
                iv_payment_test_union_check.setImageResource(R.drawable.payment_list_normal);
                break;
            case R.id.ll_payment_test_ali:
                iv_payment_test_wx_check.setImageResource(R.drawable.payment_list_normal);
                iv_payment_test_ali_check.setImageResource(R.drawable.payment_list_select);
                iv_payment_test_union_check.setImageResource(R.drawable.payment_list_normal);
                break;
            case R.id.ll_payment_test_union:
                iv_payment_test_wx_check.setImageResource(R.drawable.payment_list_normal);
                iv_payment_test_ali_check.setImageResource(R.drawable.payment_list_normal);
                iv_payment_test_union_check.setImageResource(R.drawable.payment_list_select);
                break;
        }
    }

    @SuppressLint("HandlerLeak")
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==1){
                stopProgressDialog();
                EventBus.getDefault().post(new EventBusMessage<>("paymentOk"));
                finish();
            }
        }
    };
}
