package com.xiandao.android.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.webkit.JavascriptInterface;

import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.xiandao.android.R;
import com.xiandao.android.entity.RoomInfoEntity;
import com.xiandao.android.ui.BaseActivity;
import com.xiandao.android.utils.Constants;
import com.xiandao.android.utils.FileManagement;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.xiandao.android.utils.Tools;

import org.xutils.event.annotation.ContentView;
import org.xutils.event.annotation.ViewInject;

import java.io.IOException;
import java.util.ArrayList;


/**
 * 此类描述的是:邻里activity
 *
 * @author TanYong
 *         create at 2017/6/1 19:16
 */
@ContentView(R.layout.fragment_neighborhood)
public class NeighborhoodActivity extends BaseActivity implements View.OnClickListener {
    private ArrayList<RoomInfoEntity> roomInfoEntity;
    @ViewInject(R.id.wv_neighborhood)
    private WebView wv_neighborhood;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        roomInfoEntity = FileManagement.getRoomInfo();
        WebSettings settings = wv_neighborhood.getSettings();
        settings.setJavaScriptEnabled(true);
        // 设置可以支持缩放
        settings.setSupportZoom(false);
        // 设置默认缩放方式尺寸是far
        // shopElement.settings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        // 设置出现缩放工具
        settings.setBuiltInZoomControls(false);
        // 让网页自适应屏幕宽度
        // shopElement.settings.setLayoutAlgorithm(
        // LayoutAlgorithm.SINGLE_COLUMN);
        int screenDensity = getResources().getDisplayMetrics().densityDpi;
        WebSettings.ZoomDensity zoomDensity = WebSettings.ZoomDensity.MEDIUM;
        switch (screenDensity) {
            case DisplayMetrics.DENSITY_LOW:
                zoomDensity = WebSettings.ZoomDensity.CLOSE;
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                zoomDensity = WebSettings.ZoomDensity.MEDIUM;
                break;
            case DisplayMetrics.DENSITY_HIGH:
            case DisplayMetrics.DENSITY_XHIGH:
            case DisplayMetrics.DENSITY_TV:
                zoomDensity = WebSettings.ZoomDensity.FAR;
                break;
            default:
                break;
        }
        settings.setDefaultZoom(zoomDensity);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        // 设置可以访问文件
        settings.setAllowFileAccess(true);
        settings.setDefaultTextEncodingName("utf-8");

        wv_neighborhood.setHorizontalScrollBarEnabled(false);//水平不显示
        wv_neighborhood.setVerticalScrollBarEnabled(false); //垂直不显示
        wv_neighborhood.addJavascriptInterface(new MyJavascriptInterface(), "javaInterface");
        wv_neighborhood.setWebChromeClient(new WebChromeClient());
        wv_neighborhood.clearCache(true);
        settings.setAllowFileAccess(true);
        settings.setDatabaseEnabled(true);
        String dir = this.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
        settings.setDatabasePath(dir);
        settings.setDomStorageEnabled(true);
        settings.setGeolocationEnabled(true);

//        String url = "javascript:(function(){" +
//                "var myInfo = {id:'" + loginUserEntity.getUserId() + "',nickname:'"+loginUserEntity.getNickName()+"'}" +
//                " window.localStorage.setItem('myInfo', JSON.stringify(myInfo));"+
//                "})()";
//        String url = "javascript:(function(){" +
//                "window.myInfo = {id: '4504', nickname: 'id-4504'};" +
//                "})()";

        startProgressDialog("");

//        String path = Constants.BASEHOST + "/nearby/index.html#id=" + loginUserEntity.getUserId()
//                + "&nickname=" + loginUserEntity.getNickName() + "";
//        wv_neighborhood.loadUrl(path);
        wv_neighborhood.loadUrl(Constants.BASEHOST +"/nearby/index.html#id=1001&nickname=id-1001");
//        wv_neighborhood.loadUrl(url);
//        wv_neighborhood.loadUrl("http://192.168.0.250:8100/#id=" + 1001 + "&nickname=" + userEntity.getNickName());
        wv_neighborhood.setWebViewClient(new webViewClient());
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

    class MyJavascriptInterface {
        @JavascriptInterface
        public void backToApp() {
            NeighborhoodActivity.this.finish();
        }

        @JavascriptInterface
        public void editPost() {
            openActivityResult(PostActivity.class);
        }
    }

    //Web视图
    private class webViewClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            imgReset();
            stopProgressDialog();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        /**
         * flag:1、vendor.js
         * 2、main.js
         * 3、main.css
         *
         * @return 本地jquery
         */
        private WebResourceResponse editResponse(int flag) {
            try {
                if (flag == 1) {
                    WebResourceResponse webResourceResponse = new WebResourceResponse("application/x-javascript", "utf-8", getAssets().open("vendor.js"));
                    return webResourceResponse;
                } else if (flag == 2) {
                    WebResourceResponse webResourceResponse = new WebResourceResponse("application/x-javascript", "utf-8", getAssets().open("main.js"));
                    return webResourceResponse;
                } else if (flag == 3) {
                    WebResourceResponse webResourceResponse = new WebResourceResponse("text/css", "utf-8", getAssets().open("main.css"));
                    return webResourceResponse;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            //需处理特殊情况
            return null;
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
            if (Build.VERSION.SDK_INT < 21) {
                if (url.contains("vendor.js")) {
                    return editResponse(1);
                } else if (url.contains("main.js")) {
                    return editResponse(2);
                } else if (url.contains("main.css")) {
                    return editResponse(3);
                }
            }
            return super.shouldInterceptRequest(view, url);
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            if (Build.VERSION.SDK_INT >= 21) {
                String url = request.getUrl().toString();
                if (!Tools.isEmpty(url)) {
                    if (url.contains("vendor.js")) {
                        return editResponse(1);
                    } else if (url.contains("main.js")) {
                        return editResponse(2);
                    } else if (url.contains("main.css")) {
                        return editResponse(3);
                    }
                }
            }
            return super.shouldInterceptRequest(view, request);
        }
    }

    private void imgReset() {
        wv_neighborhood.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByTagName('img'); " +
                "for(var i=0;i<objs.length;i++)  " +
                "{"
                + "var img = objs[i];   " +
                " img.style.maxWidth = '100%';img.style.height='auto';" +
                "}" +
                "})()");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            String jsUrl = "javascript:(function(){" +
                    "_getMyPostList();" +
                    "})()";
            wv_neighborhood.loadUrl(jsUrl);
        }
    }
//
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            return true;
//        }
//        return false;
//    }
}
