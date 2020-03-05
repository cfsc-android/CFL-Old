package com.xiandao.android.utils;

import android.app.Activity;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import com.xiandao.android.ui.activity.H5TestActivity;

/**
 * Created by zengx on 2019/5/8.
 * Describe:
 */
public class JsJavaBridge {
    private Activity activity;
    private WebView webView;

    public JsJavaBridge(Activity activity, WebView webView) {
        this.activity = activity;
        this.webView = webView;
    }

    @JavascriptInterface
    public void onFinishActivity() {
        activity.finish();
    }

    @JavascriptInterface
    public void showToast(String msg) {
        Toast.makeText(activity,msg,Toast.LENGTH_SHORT).show();
    }

    @JavascriptInterface
    public void getContent(String url,int index){
        ((H5TestActivity)activity).initContent(url,index);
    }


}
