package com.xiandao.android.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiandao.android.R;
import com.xiandao.android.ui.BaseActivity;

import org.xutils.event.annotation.ContentView;
import org.xutils.event.annotation.ViewInject;

/**
 * 此类描述的是:待开发activity
 *
 * @author TanYong
 * create at 2017/6/14 17:09
 */
@ContentView(R.layout.activity_waiting_for_development)
public class WaitingForDevelopmentActivity extends BaseActivity implements View.OnClickListener {
    @ViewInject(R.id.iv_title_left)
    private ImageView ivTitleLeft;
    @ViewInject(R.id.tv_title_name)
    private TextView tvTitleName;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title = getIntent().getStringExtra("title");
        init();
    }

    private void init() {
        tvTitleName.setText(title);
        ivTitleLeft.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_title_left:
                finish();
                break;
            default:
                break;
        }
    }
}
