package com.xiandao.android.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiandao.android.R;
import com.xiandao.android.ui.BaseActivity;

import org.xutils.event.annotation.ContentView;
import org.xutils.event.annotation.Event;
import org.xutils.event.annotation.ViewInject;

@ContentView(R.layout.activity_second_hand_detail)
public class SecondHandDetailActivity extends BaseActivity {
    @ViewInject(R.id.tv_title_name)
    private TextView tv_title_name;
    @ViewInject(R.id.iv_second_hand_main_img)
    private ImageView iv_second_hand_main_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tv_title_name.setText("宝贝详情");
        iv_second_hand_main_img.setImageResource(R.drawable.secondhand_detail);
    }

    @Event({R.id.iv_title_left,R.id.btn_second_hand_detail_im,R.id.btn_second_hand_detail_call})
    private void onClickEvnet(View v){
        switch (v.getId()){
            case R.id.iv_title_left:
                finish();
                break;
            case R.id.btn_second_hand_detail_im:
                openActivity(SecondHandIMActivity.class);
                break;
            case R.id.btn_second_hand_detail_call:
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:13800000000");
                intent.setData(data);
                startActivity(intent);
                break;
        }
    }
}
