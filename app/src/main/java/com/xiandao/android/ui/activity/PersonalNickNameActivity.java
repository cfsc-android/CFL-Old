package com.xiandao.android.ui.activity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiandao.android.R;
import com.xiandao.android.entity.LoginUserEntity;
import com.xiandao.android.entity.eventbus.EventBusMessage;
import com.xiandao.android.entity.eventbus.FaceCollectionEventBusData;
import com.xiandao.android.entity.eventbus.NickNameEventBusData;
import com.xiandao.android.ui.BaseActivity;
import com.xiandao.android.utils.FileManagement;
import com.xiandao.android.utils.Tools;
import com.xiandao.android.view.alertview.AlertView;
import com.xiandao.android.view.alertview.OnItemClickListener;

import org.greenrobot.eventbus.EventBus;
import org.xutils.event.annotation.ContentView;
import org.xutils.event.annotation.Event;
import org.xutils.event.annotation.ViewInject;

import java.io.File;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

@ContentView(R.layout.activity_personal_nick_name)
public class PersonalNickNameActivity extends BaseActivity {
    @ViewInject(R.id.tv_title_name)
    private TextView tv_title_name;
    @ViewInject(R.id.tv_title_right)
    private TextView tv_title_right;
    @ViewInject(R.id.iv_title_right)
    private ImageView iv_title_right;
    @ViewInject(R.id.et_nick_name)
    private EditText et_nick_name;

    private LoginUserEntity personal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        personal=FileManagement.getLoginUserEntity();
        if(!Tools.isEmpty(personal.getNickName())){
            et_nick_name.setText(personal.getNickName());
            et_nick_name.setSelection(personal.getNickName().length());
        }
        et_nick_name.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tv_title_right.setVisibility(VISIBLE);
                tv_title_right.setText("确定");
            }
        });
        tv_title_name.setText("编辑昵称");
        iv_title_right.setVisibility(GONE);
    }

    @Event({R.id.iv_title_left,R.id.tv_title_right})
    private void onClickEvent(View v){
        switch (v.getId()){
            case R.id.iv_title_left:
                finish();
                break;
            case R.id.tv_title_right:
                EventBusMessage<NickNameEventBusData> eventBusMessage=new EventBusMessage<>("nickName");
                eventBusMessage.setData(new NickNameEventBusData(et_nick_name.getText().toString()));
                EventBus.getDefault().post(eventBusMessage);
                finish();
                break;
        }
    }

}
