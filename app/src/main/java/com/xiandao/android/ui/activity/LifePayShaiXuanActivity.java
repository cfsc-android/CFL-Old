package com.xiandao.android.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiandao.android.R;
import com.xiandao.android.ui.BaseActivity;
import com.xiandao.android.utils.Constants;
import com.xiandao.android.view.WheelDialog;

import org.xutils.event.annotation.ContentView;
import org.xutils.event.annotation.ViewInject;

/**
 * 此类描述的是:生活缴费筛选activity
 *
 * @author TanYong
 *         create at 2017/6/1 19:16
 */
@ContentView(R.layout.activity_life_pay_shai_xuan)
public class LifePayShaiXuanActivity extends BaseActivity implements View.OnClickListener, View.OnTouchListener {

    @ViewInject(R.id.iv_title_left)
    private ImageView ivTitleLeft;
    @ViewInject(R.id.tv_title_name)
    private TextView tvTitleName;
    @ViewInject(R.id.btn_ok)
    private Button btn_ok;
    @ViewInject(R.id.et_shaixuan_pay_time)
    private EditText etPayTime;
    @ViewInject(R.id.et_shaixuan_last_term)
    private EditText etLastTerm;

    //===========筛选条件=========
    private String payTime;//支付时间
    private String lastTerm;//最后期限

    private WheelDialog payTimeWheelDialog;
    private WheelDialog lastTermWheelDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        tvTitleName.setText("支付筛选");
        ivTitleLeft.setOnClickListener(this);
        btn_ok.setOnClickListener(this);

        etPayTime.setInputType(InputType.TYPE_NULL);
        etLastTerm.setInputType(InputType.TYPE_NULL);
        etPayTime.setOnTouchListener(this);
        etLastTerm.setOnTouchListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_title_left:
                finish();
                break;
            case R.id.btn_setmine_ok:
                Intent intent = new Intent();
                payTime = etPayTime.getText().toString();
                lastTerm = etLastTerm.getText().toString();
                intent.putExtra("payTime", payTime);
                intent.putExtra("lastTerm", lastTerm);
                this.setResult(Constants.LIFE_PAY_REQUEST_CODE, intent);
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            switch (view.getId()) {
                case R.id.et_shaixuan_pay_time:
                    payTimeWheelDialog = new WheelDialog(this, R.style.Dialog_Floating, new WheelDialog.Click() {
                        @Override
                        public void getvaue(String value) {
                            payTimeWheelDialog.cancel();
                            etPayTime.setText(value);
                        }
                    });
                    payTimeWheelDialog.show();
                    break;
                case R.id.et_shaixuan_last_term:
                    lastTermWheelDialog = new WheelDialog(this, R.style.Dialog_Floating, new WheelDialog.Click() {
                        @Override
                        public void getvaue(String value) {
                            lastTermWheelDialog.cancel();
                            etLastTerm.setText(value);
                        }
                    });
                    lastTermWheelDialog.show();
                    break;
                default:
                    break;
            }
        }
        return false;
    }
}
