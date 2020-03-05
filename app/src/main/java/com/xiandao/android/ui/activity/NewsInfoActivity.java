package com.xiandao.android.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.utils.UmengText;
import com.xiandao.android.R;
import com.xiandao.android.ui.BaseActivity;
import org.xutils.event.annotation.ContentView;
import org.xutils.event.annotation.Event;
import org.xutils.event.annotation.ViewInject;

@ContentView(R.layout.activity_news_info)
public class NewsInfoActivity extends BaseActivity {
    @ViewInject(R.id.tv_title_name)
    private TextView tvTitleName;
    @ViewInject(R.id.iv_title_right)
    private ImageView iv_title_right;
    @ViewInject(R.id.wv_news_info)
    private WebView wv_news_info;

    private  String rightAction;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvTitleName.setText(getIntent().getExtras().getString("title"));
        rightAction=getIntent().getExtras().getString("rightAction");
        if(rightAction!=null&&rightAction.equals("share")){
            iv_title_right.setImageResource(R.drawable.nav_btn_share);
            iv_title_right.setVisibility(View.VISIBLE);
        }
        init();
    }

    private void init() {
        WebSettings settings = wv_news_info.getSettings();
        settings.setJavaScriptEnabled(true);
        // 设置可以支持缩放
        if(rightAction!=null&&rightAction.equals("share")){
            settings.setSupportZoom(true);
            // 设置出现缩放工具
            settings.setBuiltInZoomControls(true);
            settings.setDisplayZoomControls(false);
        }else{
            settings.setSupportZoom(false);
            // 设置出现缩放工具
            settings.setBuiltInZoomControls(false);

        }
        // 设置默认缩放方式尺寸是far
        // shopElement.settings.setDefaultZoom(WebSettings.ZoomDensity.FAR);

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
        Bundle bundle = getIntent().getExtras();
        loadWeb(bundle.getString("url"));
    }


    private void loadWeb(String url) {
        wv_news_info.setHorizontalScrollBarEnabled(false);//水平不显示
        wv_news_info.setVerticalScrollBarEnabled(false); //垂直不显示

        wv_news_info.loadUrl(url);
        wv_news_info.setWebViewClient(new NewsInfoActivity.webViewClient());
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
        wv_news_info.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByTagName('img'); " +
                "for(var i=0;i<objs.length;i++)  " +
                "{"
                + "var img = objs[i];   " +
                " img.style.maxWidth = '100%';img.style.height='auto';" +
                "}" +
                "})()");
    }

    @Event({R.id.iv_title_left,R.id.iv_title_right})
    private void onClickEvent(View v){
        switch (v.getId()){
            case R.id.iv_title_left:
                finish();
                break;
            case R.id.iv_title_right:
                share();
                break;
        }
    }

    /**
     * 分享
     */
    private void share(){
        UMImage thumb =  new UMImage(this, R.drawable.ic_share_thumb);
        UMWeb  web = new UMWeb(getIntent().getExtras().getString("url"));
        web.setTitle("产证查询");//标题
        web.setThumb(thumb);  //缩略图
        web.setDescription("长沙市住房和城乡建设局-商品房合同签订查询");//描述
        new ShareAction(this)
                .withMedia(web)
                .setDisplayList(SHARE_MEDIA.QQ,SHARE_MEDIA.WEIXIN)
                .setCallback(shareListener).open();
    }

    private UMShareListener shareListener = new UMShareListener() {
        /**
         * @descrption 分享开始的回调
         * @param platform 平台类型
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        /**
         * @descrption 分享成功的回调
         * @param platform 平台类型
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {
            showShortToast("成功了");
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Log.d("错误日志",platform.toString());
            Log.d("错误日志",t.toString());
            showShortToast("失败了");
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            showShortToast("取消了");
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
