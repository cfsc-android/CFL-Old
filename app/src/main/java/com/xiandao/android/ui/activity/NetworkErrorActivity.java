package com.xiandao.android.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiandao.android.R;
import com.xiandao.android.ui.BaseActivity;

import org.xutils.event.annotation.ContentView;
import org.xutils.event.annotation.ViewInject;

/**
 * 此类描述的是:网络异常
 *
 * @author TanYong
 *         create at 2017/6/17 19:28
 */
@ContentView(R.layout.activity_network_error)
public class NetworkErrorActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.iv_title_left)
    private ImageView iv_title_left;
    @ViewInject(R.id.tv_title_name)
    private TextView tv_title_name;
    @ViewInject(R.id.btn_to_setting)
    private Button btn_to_setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        iv_title_left.setOnClickListener(this);
        tv_title_name.setText("网络不通");
        btn_to_setting.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_left:
                this.finish();
                break;
            case R.id.btn_to_setting:
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                startActivity(intent);
                this.finish();
                break;
            default:
                break;
        }
    }
}
