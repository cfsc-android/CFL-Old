package com.xiandao.android.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiandao.android.R;
import com.xiandao.android.ui.BaseActivity;

import org.xutils.event.annotation.ContentView;

@ContentView(R.layout.activity_house_manage)
public class HouseManageActivity extends BaseActivity  implements View.OnClickListener {

    private ImageView iv_title_left;
    private View tv_house_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_manage);
        init();
    }

    private void init() {
        iv_title_left =  (ImageView) findViewById(R.id.iv_title_left);
        tv_house_add = findViewById(R.id.tv_house_add);
        iv_title_left.setOnClickListener(this);
        tv_house_add.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_title_left:
                finish();
                break;
            case R.id.tv_house_add:
                openActivity(SelectCommunityActivity.class);
                break;
        }
    }
}
