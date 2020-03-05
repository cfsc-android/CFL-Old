package com.xiandao.android.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.xiandao.android.R;
import com.xiandao.android.ui.BaseActivity;

import org.xutils.event.annotation.ContentView;
import org.xutils.event.annotation.Event;
import org.xutils.event.annotation.ViewInject;

@ContentView(R.layout.activity_express_search)
public class ExpressSearchActivity extends BaseActivity {
    @ViewInject(R.id.tv_title_name)
    private TextView tvTitleName;
    @ViewInject(R.id.wv_express_search)
    private WebView wv_express_search;
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvTitleName.setText("包裹查询");
        wv_express_search.setWebViewClient(new WebViewClient(){});
        wv_express_search.getSettings().setJavaScriptEnabled(true);
        wv_express_search.loadUrl("https://m.kuaidi100.com/app/?coname=hao123");
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
