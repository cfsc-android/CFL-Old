package com.xiandao.android.ui.fragment;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.xiandao.android.R;
import com.xiandao.android.entity.LoginUserEntity;
import com.xiandao.android.entity.RoomInfoEntity;
import com.xiandao.android.ui.BaseLazyFragment;
import com.xiandao.android.utils.FileManagement;

import java.util.ArrayList;


/**
 * 此类描述的是:邻里fragment
 *
 * @author TanYong
 *         create at 2017/5/18 9:53
 */
public class NeighborhoodFragment extends BaseLazyFragment {

    private LoginUserEntity userEntity;
    private ArrayList<RoomInfoEntity> roomInfoEntity;

    private WebView wv_neighborhood;

    @Override
    protected void injectView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_neighborhood, null);
        setContentView(view);
        wv_neighborhood = (WebView) view.findViewById(R.id.wv_neighborhood);
        userEntity = FileManagement.getLoginUserEntity();
        roomInfoEntity = FileManagement.getRoomInfo();
    }

    @Override
    protected void onFragmentStartLazy() {
        super.onFragmentStartLazy();
        init();
    }

    private void init() {
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
        settings.setAllowFileAccess(true);
        settings.setDatabaseEnabled(true);
        String dir = this.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
        settings.setDatabasePath(dir);
        settings.setDomStorageEnabled(true);
        settings.setGeolocationEnabled(true);

//        wv_neighborhood.loadUrl("http://www.baidu.com");
        wv_neighborhood.loadUrl("http://192.168.0.250:8100/#id=" + userEntity.getUserId() + "&nickname=" + userEntity.getNickName());
        wv_neighborhood.setWebViewClient(new webViewClient());
    }

    //Web视图
    private class webViewClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            imgReset();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
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

}
