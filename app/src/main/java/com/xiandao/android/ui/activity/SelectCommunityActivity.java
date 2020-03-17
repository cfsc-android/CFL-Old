package com.xiandao.android.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.xiandao.android.R;
import com.xiandao.android.ui.BaseActivity;

import org.xutils.event.annotation.ContentView;

@ContentView(R.layout.activity_select_community)
public class SelectCommunityActivity extends BaseActivity  implements View.OnClickListener {

    private ImageView iv_title_left;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_community);
        init();
    }

    private void init() {
        iv_title_left =  (ImageView) findViewById(R.id.iv_title_left);
        iv_title_left.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_title_left:
                finish();
                break;
        }
    }
}
