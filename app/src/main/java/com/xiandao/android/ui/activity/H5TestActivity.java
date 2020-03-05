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

import com.xiandao.android.R;
import com.xiandao.android.http.MyCallBack;
import com.xiandao.android.http.XUtils;
import com.xiandao.android.ui.BaseActivity;
import com.xiandao.android.utils.Constants;
import com.xiandao.android.utils.JsJavaBridge;

import org.xutils.event.annotation.ContentView;
import org.xutils.event.annotation.Event;
import org.xutils.event.annotation.ViewInject;

@ContentView(R.layout.activity_h5_test)
public class H5TestActivity extends BaseActivity {
    @ViewInject(R.id.tv_title_name)
    private TextView tv_title_name;
    @ViewInject(R.id.h5_test_web_view)
    private WebView h5_test_web_view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tv_title_name.setText("H5测试");
        h5_test_web_view.getSettings().setJavaScriptEnabled(true);
        h5_test_web_view.setWebViewClient(new WebViewClient(){
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                h5_test_web_view.evaluateJavascript("javascript:initContent()", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        Log.d("js返回结果",value);
                    }
                });
            }
        });
        h5_test_web_view.addJavascriptInterface(new JsJavaBridge(this, h5_test_web_view), "$App");
        h5_test_web_view.loadUrl("http://172.19.13.101:8080/");
    }

    public void initContent(String url, final int index){
        XUtils.Get(Constants.BASEHOST+url,null,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                String content=result.substring(result.indexOf("<body")+40,result.indexOf("</body"));
                content=content.replaceAll("\\t","");
                content=content.replaceAll("\\\\s{1,}","");
                content=content.replaceAll("\\n","");
                content=content.replaceAll("\\r","");
                h5_test_web_view.evaluateJavascript("javascript:nativeCall('"+content+"',"+index+")", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        Log.d("js返回结果",value);
                        imgReset();
                    }
                });
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                showShortToast(ex.getMessage());
            }

            @Override
            public void onFinished() {
                super.onFinished();
            }
        });
    }

    private void imgReset() {
        h5_test_web_view.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByTagName('img'); " +
                "for(var i=0;i<objs.length;i++)  " +
                "{"
                + "var img = objs[i];   " +
                " img.style.maxWidth = '100%';img.style.height='auto';" +
                "}" +
                "})()");
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
