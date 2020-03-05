package com.xiandao.android.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiandao.android.R;
import com.xiandao.android.ui.BaseActivity;

import org.xutils.event.annotation.ContentView;
import org.xutils.event.annotation.Event;
import org.xutils.event.annotation.ViewInject;

@ContentView(R.layout.activity_comment)
public class CommentActivity extends BaseActivity {
    @ViewInject(R.id.tv_title_name)
    private TextView tv_title_name;
    @ViewInject(R.id.iv_second_hand_main_img)
    private ImageView iv_second_hand_main_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tv_title_name.setText("评论");
        iv_second_hand_main_img.setImageResource(R.drawable.comment_index);
    }

    @Event({R.id.iv_title_left,R.id.tv_comment_month,R.id.tv_comment_manage,R.id.tv_comment_team})
    private void onClickEvent(View v){
        Bundle bundle=new Bundle();
        switch (v.getId()){
            case R.id.iv_title_left:
                finish();
                break;
            case R.id.tv_comment_month:
                bundle.putString("title","月度评价");
                bundle.putInt("img",0);
                openActivity(CommentDetailActivity.class,bundle);
                break;
            case R.id.tv_comment_manage:
                bundle.putString("title","管家评价");
                bundle.putInt("img",1);
                openActivity(CommentDetailActivity.class,bundle);
                break;
            case R.id.tv_comment_team:
                bundle.putString("title","服务团队");
                bundle.putInt("img",2);
                openActivity(CommentDetailActivity.class,bundle);
                break;
        }
    }
}
