package com.xiandao.android.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xiandao.android.R;
import com.xiandao.android.entity.eventbus.EventBusMessage;
import com.xiandao.android.entity.smart.BaseEntity;
import com.xiandao.android.entity.smart.QrCodeEntity;
import com.xiandao.android.http.JsonParse;
import com.xiandao.android.http.MyCallBack;
import com.xiandao.android.http.XUtils;
import com.xiandao.android.ui.BaseActivity;
import com.xiandao.android.utils.FileManagement;

import org.greenrobot.eventbus.EventBus;
import org.xutils.LogUtils;
import org.xutils.app.LynActivityManager;
import org.xutils.event.annotation.ContentView;
import org.xutils.event.annotation.Event;
import org.xutils.event.annotation.ViewInject;

import java.util.HashMap;
import java.util.Map;

import static com.xiandao.android.base.Config.BASE_URL;
import static com.xiandao.android.base.Config.BASIC;


@ContentView(R.layout.activity_household_audit)
public class HouseholdAuditActivity extends BaseActivity {
    @ViewInject(R.id.tv_title_name)
    private TextView tv_title_name;
    @ViewInject(R.id.household_audit_card_no)
    private EditText household_audit_card_no;
    @ViewInject(R.id.household_audit_tel)
    private EditText household_audit_tel;
    @ViewInject(R.id.household_audit_remark)
    private EditText household_audit_remark;

    private String roomId,type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tv_title_name.setText("提交个人信息");
        roomId=getIntent().getExtras().getString("roomId");
        type=getIntent().getExtras().getString("type");
    }


    private void audit(){
        if(TextUtils.isEmpty(household_audit_card_no.getText())){
            showToast("请填写身份证号");
            return;
        }
        if(TextUtils.isEmpty(household_audit_tel.getText())){
            showToast("请填写手机号");
            return;
        }
        startProgressDialog("");
        Map<String,Object> map =new HashMap<>();
        map.put("idcardNo",household_audit_card_no.getText().toString());
        map.put("mobile",household_audit_tel.getText().toString());
        map.put("remark",household_audit_remark.getText().toString());
        map.put("roomId",roomId);
        map.put("type",type);
        map.put("householdId", FileManagement.getUserInfoEntity().getId());
        XUtils.PostJson(BASE_URL+BASIC+"basic/verify",map,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtils.d(result);
                BaseEntity baseEntity= JsonParse.parse(result);
                if(baseEntity.isSuccess()){
                    LynActivityManager.getInstance().finishActivity(HouseholdTypeSelectActivity.class);
                    LynActivityManager.getInstance().finishActivity(RoomSelectActivity.class);
                    LynActivityManager.getInstance().finishActivity(UnitSelectActivity.class);
                    EventBus.getDefault().post(new EventBusMessage<>("householdAudit"));
                    finish();
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
        });
    }


    @Event({R.id.iv_title_left,R.id.household_type_select_btn})
    private void onClickEvent(View v){
        switch (v.getId()){
            case R.id.iv_title_left:
                finish();
                break;
            case R.id.household_type_select_btn:
                audit();
                break;
        }
    }
}
