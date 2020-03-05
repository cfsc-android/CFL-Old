package com.xiandao.android.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xiandao.android.R;
import com.xiandao.android.entity.HikBaseEntity;
import com.xiandao.android.entity.HikUser;
import com.xiandao.android.entity.eventbus.EventBusMessage;
import com.xiandao.android.entity.smart.BaseEntity;
import com.xiandao.android.entity.smart.QrCodeEntity;
import com.xiandao.android.http.JsonParse;
import com.xiandao.android.http.MyCallBack;
import com.xiandao.android.http.XUtils;
import com.xiandao.android.net.ApiHttpResult;
import com.xiandao.android.net.HttpUtils;
import com.xiandao.android.ui.BaseActivity;
import com.xiandao.android.ui.activity.shop.MyOrderActivity;
import com.xiandao.android.utils.Constants;
import com.xiandao.android.utils.FileManagement;
import com.xiandao.android.utils.Tools;
import com.xiandao.android.utils.XUtilsImageUtils;
import com.xiandao.android.view.WheelDialog;
import com.xiandao.android.view.alertview.AlertView;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.LogUtils;
import org.xutils.event.annotation.ContentView;
import org.xutils.event.annotation.Event;
import org.xutils.event.annotation.ViewInject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.xiandao.android.base.Config.BASE_URL;
import static com.xiandao.android.base.Config.IOT;

/**
 * Created by ZXL on 2019/4/9.
 * Describe:
 */
@ContentView(R.layout.activity_new_visitor)
public class NewVisitorActivity extends BaseActivity {
    @ViewInject(R.id.tv_title_name)
    private TextView tv_title_name;

    @ViewInject(R.id.et_new_visitor_name)
    private EditText et_new_visitor_name;
    @ViewInject(R.id.et_new_visitor_valid_start)
    private EditText et_new_visitor_valid_start;
    @ViewInject(R.id.et_new_visitor_valid_end)
    private EditText et_new_visitor_valid_end;
    @ViewInject(R.id.et_new_visitor_valid_num)
    private EditText et_new_visitor_valid_num;

    private String cardNo="";
    private WheelDialog wheeldialog;
    private String valid_start,valid_end;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tv_title_name.setText("新邀请");
    }

    @Event(type = View.OnTouchListener.class,value ={R.id.et_new_visitor_valid_start,R.id.et_new_visitor_valid_end})
    private boolean onTouchEvent(View v,MotionEvent event){
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            switch (v.getId()) {
                case R.id.et_new_visitor_valid_start:
                    wheeldialog = new WheelDialog(this, R.style.Dialog_Floating, new WheelDialog.OnDateTimeConfirm() {
                        @Override
                        public void returnData(String dateText, String dateValue) {
                            wheeldialog.cancel();
                            et_new_visitor_valid_start.setText(dateText);
                            valid_start=dateValue;
                        }
                    });
                    wheeldialog.show();
                    break;
                case R.id.et_new_visitor_valid_end:
                    wheeldialog = new WheelDialog(this, R.style.Dialog_Floating, new WheelDialog.OnDateTimeConfirm() {
                        @Override
                        public void returnData(String dateText, String dateValue) {
                            wheeldialog.cancel();
                            et_new_visitor_valid_end.setText(dateText);
                            valid_end=dateValue;
                        }
                    });
                    wheeldialog.show();
                    break;
            }
        }
        return false;
    }

    @Event({R.id.iv_title_left, R.id.btn_new_visitor_create})
    private void onClickEvent(View v){
        switch (v.getId()){
            case R.id.iv_title_left:
                finish();
                break;
            case R.id.btn_new_visitor_create:
                String name = et_new_visitor_name.getText().toString();
                String start = et_new_visitor_valid_start.getText().toString();
                String end = et_new_visitor_valid_end.getText().toString();
                int num=4;
                if(!Tools.isEmpty(et_new_visitor_valid_num.getText().toString())){
                    num=Integer.parseInt(et_new_visitor_valid_num.getText().toString());
                }
                if (Tools.isEmpty(name)) {
                    new AlertView("温馨提示", Tools.getStringValue("请输入邀请人姓名"),
                            null, new String[]{"知道了"}, null, this, AlertView.Style.Alert, null).setCancelable(true).show();
                    return;
                }
                if (Tools.isEmpty(start)) {
                    new AlertView("温馨提示", Tools.getStringValue("请输入生效时间"),
                            null, new String[]{"知道了"}, null, this, AlertView.Style.Alert, null).setCancelable(true).show();
                    return;

                }
                if (Tools.isEmpty(end)) {
                    new AlertView("温馨提示", Tools.getStringValue("请输入失效时间"),
                            null, new String[]{"知道了"}, null, this, AlertView.Style.Alert, null).setCancelable(true).show();
                    return;
                }
                if (!Tools.isEmpty(compareDateTime(start,end))) {
                    new AlertView("温馨提示", Tools.getStringValue(compareDateTime(start,end)),
                            null, new String[]{"知道了"}, null, this, AlertView.Style.Alert, null).setCancelable(true).show();
                    return;
                }
                createNewVisitor(name,num,start,end);
                break;
        }
    }

    private String compareDateTime(String start,String end){
        String err="";
        SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd HH:mm" );
        try {
            Date start_date = sdf.parse(start);
            Date end_date=sdf.parse(end);
            if(start_date.getTime()>end_date.getTime()){
                err="失效时间不能小于生效时间";
            }
            if(end_date.getTime()-start_date.getTime()>48*60*60*1000){
                err="有效期不能大于48小时";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return err;
    }

    private void createNewVisitor(final String name, final int num, final String start, final String end) {
        startProgressDialog("");
        Map<String,Object> requestMap=new HashMap<>();
        requestMap.put("phaseId",FileManagement.getUserInfoEntity().getRoomList().get(0).getPhaseId());
        requestMap.put("visitorName",name);
        requestMap.put("cardNo",FileManagement.getUserInfoEntity().getDefaultCardNo());
        requestMap.put("effectTime",valid_start);
        requestMap.put("expireTime",valid_end);
        requestMap.put("openTimes",num);
        XUtils.PostJson(BASE_URL+IOT+"community/api/access/v1/qrcode/visitor",requestMap,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtils.d(result);
                BaseEntity<QrCodeEntity> baseEntity= JsonParse.parse(result,QrCodeEntity.class);
                if(baseEntity.isSuccess()){
                    EventBus.getDefault().post(new EventBusMessage<>("visitorRefresh"));
                    Bundle bundle = new Bundle();
                    bundle.putString("qrCodeUrl", baseEntity.getResult().getQrCodeUrl());
                    bundle.putString("name", name);
                    bundle.putString("start", start);
                    bundle.putString("end", end);
                    bundle.putInt("num",num);
                    openActivity(VisitorQrCodeActivity.class,bundle);
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
}
