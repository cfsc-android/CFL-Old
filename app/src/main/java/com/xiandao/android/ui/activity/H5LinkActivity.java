package com.xiandao.android.ui.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xiandao.android.R;
import com.xiandao.android.entity.LoginResultDataEntity;
import com.xiandao.android.ui.BaseActivity;
import com.xiandao.android.utils.FileManagement;
import com.xiandao.android.utils.JsJavaBridge;
import com.xiandao.android.utils.Utils;

import org.xutils.event.annotation.ContentView;
import org.xutils.event.annotation.Event;
import org.xutils.event.annotation.ViewInject;

@ContentView(R.layout.activity_h5_link)
public class H5LinkActivity extends BaseActivity {
    @ViewInject(R.id.tv_title_name)
    private TextView tv_title_name;
    @ViewInject(R.id.h5_link_web_view)
    private WebView h5_link_web_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tv_title_name.setText("H5测试");
        h5_link_web_view.getSettings().setJavaScriptEnabled(true);

        h5_link_web_view.getSettings().setUserAgentString(h5_link_web_view.getSettings().getUserAgentString()+" smart_android/"+Utils.getCurrentVersion(this));
        h5_link_web_view.setWebViewClient(new WebViewClient(){
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
//                h5_link_web_view.evaluateJavascript("javascript:testFun('123')", new ValueCallback<String>() {
//                    @Override
//                    public void onReceiveValue(String value) {
//                        Log.d("js返回结果",value);
//                        showShortToast("js返回结果--"+value);
//                    }
//                });
                LoginResultDataEntity userInfo=new LoginResultDataEntity();
                userInfo.setDeviceInfo(FileManagement.getDeviceInfo());
                userInfo.setRoominfo(FileManagement.getRoomInfo());
                userInfo.setThirdInfo(FileManagement.getThirdInfo());
                userInfo.setUserInfo(FileManagement.getLoginUserEntity());
                Gson gson=new Gson();
                String param=gson.toJson(userInfo);
                h5_link_web_view.evaluateJavascript("javascript:setUserInfo('"+param+"')", null);
            }
        });
        h5_link_web_view.addJavascriptInterface(new JsJavaBridge(this, h5_link_web_view), "$ZHSQApp");
//        h5_link_web_view.loadUrl("http://mafpa4.natappfree.cc/unlock");
        h5_link_web_view.loadUrl("http://fe5p3s.natappfree.cc/#/visitor/detail/140");
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
