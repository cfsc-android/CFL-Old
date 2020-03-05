package com.xiandao.android.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.xiandao.android.R;
import com.xiandao.android.entity.ProjectInfo;
import com.xiandao.android.entity.eventbus.EventBusMessage;
import com.xiandao.android.ui.BaseActivity;
import com.xiandao.android.utils.Constants;
import com.xiandao.android.utils.FileManagement;
import com.xiandao.android.utils.Tools;

import org.greenrobot.eventbus.EventBus;
import org.xutils.event.annotation.ContentView;
import org.xutils.event.annotation.Event;
import org.xutils.event.annotation.ViewInject;

@ContentView(R.layout.activity_ip_setting)
public class IpSettingActivity extends BaseActivity {
    @ViewInject(R.id.tv_title_name)
    private TextView tv_title_name;
    @ViewInject(R.id.et_ip_setting_ip)
    private EditText et_ip_setting_ip;
    @ViewInject(R.id.et_ip_setting_name)
    private EditText et_ip_setting_name;
    @ViewInject(R.id.et_ip_setting_address)
    private EditText et_ip_setting_address;
    @ViewInject(R.id.et_ip_setting_tag)
    private EditText et_ip_setting_tag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tv_title_name.setText("自定义项目");
    }

    @Event({R.id.iv_title_left,R.id.btn_ip_setting_change})
    private void onClickEvent(View v){
        switch (v.getId()){
            case R.id.iv_title_left:
                finish();
                break;
            case R.id.btn_ip_setting_change:
                if(Tools.isEmpty(et_ip_setting_name.getText().toString())){
                    showShortToast("项目名称不能为空");
                    return;
                }

                if(Tools.isEmpty(et_ip_setting_address.getText().toString())){
                    showShortToast("项目地址不能为空");
                    return;
                }

                if(Tools.isEmpty(et_ip_setting_ip.getText().toString())){
                    showShortToast("服务地址不能为空");
                    return;
                }
                FileManagement.setCustomerProject(new ProjectInfo(et_ip_setting_name.getText().toString(),
                        et_ip_setting_address.getText().toString(),
                        et_ip_setting_ip.getText().toString(),
                        et_ip_setting_tag.getText().toString()));
                EventBus.getDefault().post(new EventBusMessage<>("customerProject"));
                finish();
                break;

        }
    }

    @SuppressLint("HandlerLeak")
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==1){

            }
        }
    };

}
