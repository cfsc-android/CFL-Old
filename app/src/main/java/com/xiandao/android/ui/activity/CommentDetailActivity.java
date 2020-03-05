package com.xiandao.android.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiandao.android.R;
import com.xiandao.android.ui.BaseActivity;

import org.xutils.event.annotation.ContentView;
import org.xutils.event.annotation.Event;
import org.xutils.event.annotation.ViewInject;

@ContentView(R.layout.activity_comment_detail)
public class CommentDetailActivity extends BaseActivity {
    @ViewInject(R.id.tv_title_name)
    private TextView tv_title_name;
    @ViewInject(R.id.iv_second_hand_main_img)
    private ImageView iv_second_hand_main_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tv_title_name.setText(getIntent().getExtras().getString("title"));
        switch (getIntent().getExtras().getInt("img",0)){
            case 0:
                iv_second_hand_main_img.setImageResource(R.drawable.comment_month);
                break;
            case 1:
                iv_second_hand_main_img.setImageResource(R.drawable.comment_manage);
                break;
            case 2:
                iv_second_hand_main_img.setImageResource(R.drawable.comment_team);
                break;
        }
    }

    @Event({R.id.iv_title_left})
    private void onClickEvent(View v){
        switch (v.getId()){
            case R.id.iv_title_left:
                finish();
                break;
        }
    }
}
