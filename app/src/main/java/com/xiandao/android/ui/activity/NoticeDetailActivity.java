package com.xiandao.android.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.xiandao.android.R;
import com.xiandao.android.entity.NoticeDetailEntity;
import com.xiandao.android.entity.OverallSituationEntity;
import com.xiandao.android.entity.VisitorRecord;
import com.xiandao.android.entity.eventbus.EventBusMessage;
import com.xiandao.android.entity.smart.BaseEntity;
import com.xiandao.android.entity.smart.NoticeEntity;
import com.xiandao.android.http.JsonParse;
import com.xiandao.android.http.MyCallBack;
import com.xiandao.android.http.XUtils;
import com.xiandao.android.net.ApiHttpResult;
import com.xiandao.android.net.HttpUtils;
import com.xiandao.android.ui.BaseActivity;
import com.xiandao.android.utils.Constants;
import com.xiandao.android.utils.FileManagement;
import com.xiandao.android.utils.Tools;
import com.xiandao.android.utils.XUtilsImageUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.LogUtils;
import org.xutils.common.Callback;
import org.xutils.event.annotation.ContentView;
import org.xutils.event.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.xiandao.android.base.Config.ARTICLE;
import static com.xiandao.android.base.Config.BASE_URL;


/**
 * 此类描述的是:通知公告详情activity
 *
 * @author TanYong
 * create at 2017/6/14 17:20
 */
@ContentView(R.layout.activity_notice_detail)
public class NoticeDetailActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.iv_title_left)
    private ImageView ivTitleLeft;
    @ViewInject(R.id.tv_title_name)
    private TextView tvTitleName;
    @ViewInject(R.id.tv_notice_detail_title)
    private TextView tv_notice_detail_title;
    @ViewInject(R.id.tv_notice_detail_publisher_name)
    private TextView tv_notice_detail_publisher_name;
    @ViewInject(R.id.tv_notice_detail_publisher_time)
    private TextView tv_notice_detail_publisher_time;
    @ViewInject(R.id.tv_browse)
    private TextView tv_browse;
    @ViewInject(R.id.tv_look_resources)
    private TextView tv_look_resources;
    @ViewInject(R.id.tv_thumbs_up)
    private TextView tv_thumbs_up;
    @ViewInject(R.id.tv_share)
    private TextView tv_share;

    @ViewInject(R.id.wv_notice_detail)
    private WebView wv_notice_detail;
    @ViewInject(R.id.iv_thumbs_up)
    private ImageView iv_thumbs_up;
    @ViewInject(R.id.ll_thumbs_up)
    private LinearLayout ll_thumbs_up;

    private String noticeId;
    private String htmlUrl;

    private NoticeEntity notice;
    private boolean bind;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(FileManagement.getTokenInfo().equals("third")){
            bind=false;
            EventBus.getDefault().register(this);
        }else{
            bind=true;
        }
        tvTitleName.setText(getIntent().getStringExtra("title"));
        noticeId=getIntent().getStringExtra("noticeId");
        getData();
        postPreview();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(EventBusMessage message){
        if("bind".equals(message.getMessage())){
            Log.e("bind","Home_bind");
            bind=true;
            EventBus.getDefault().unregister(this);
        }
    }

    private void getData(){
        startProgressDialog("");
        XUtils.Get(BASE_URL+ARTICLE+"smart/content/"+noticeId,null,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtils.d(result);
                BaseEntity<NoticeEntity> baseEntity= JsonParse.parse(result,NoticeEntity.class);
                if(baseEntity.isSuccess()){
                    notice=baseEntity.getResult();
                    init();
                }else{
                    showToast(baseEntity.getMessage());
                    stopProgressDialog();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                showToast(ex.getMessage());
                stopProgressDialog();
            }
        });

    }

    //预览+1
    private void postPreview(){
        Map<String,Object> map=new HashMap<>();
        map.put("announcementId",noticeId);
        map.put("field","browseNum");
        XUtils.PostJson(BASE_URL+ARTICLE+"smart/contentThumbup/annoucementUp",map,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtils.d(result);
                BaseEntity baseEntity= JsonParse.parse(result);
                if(!baseEntity.isSuccess()){
                    showToast(baseEntity.getMessage());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                showToast(ex.getMessage());
            }
        });
    }

    //点赞/取消点赞
    private void thumbUp(final String flag){
        String url=BASE_URL+ARTICLE+("0".equals(flag)?"smart/contentThumbup/noLikeAnnouncement/":"smart/contentThumbup/likeAnnouncement/")+noticeId;
        XUtils.Get(url,null,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtils.d(result);
                BaseEntity baseEntity= JsonParse.parse(result);
                if(baseEntity.isSuccess()){
                    if("0".equals(flag)){
                        notice.setIsThumbup("0");
                        notice.setPraiseNum(notice.getPraiseNum()-1);
                        tv_thumbs_up.setText(notice.getPraiseNum()+"");
                        iv_thumbs_up.setImageResource(R.mipmap.ggxq_icon_zan);
                    }else{
                        notice.setIsThumbup("1");
                        notice.setPraiseNum(notice.getPraiseNum()+1);
                        tv_thumbs_up.setText(notice.getPraiseNum()+"");
                        iv_thumbs_up.setImageResource(R.mipmap.ggxq_icon_zan_p);
                    }
                }else{
                    showToast(baseEntity.getMessage());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                showToast(ex.getMessage());
                stopProgressDialog();
            }
        });
    }
    private void init() {

        ivTitleLeft.setOnClickListener(this);

        WebSettings settings = wv_notice_detail.getSettings();
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

        htmlUrl = notice.getDetailUrl();
        tv_notice_detail_title.setText(notice.getTitle());
        tv_notice_detail_publisher_name.setText(notice.getPublisher());
        tv_notice_detail_publisher_time.setText(notice.getPublishTime());
        tv_browse.setText(notice.getBrowseNum() + "");
        tv_thumbs_up.setText(notice.getPraiseNum() + "");
        iv_thumbs_up.setImageResource("0".equals(notice.getIsThumbup())?R.mipmap.ggxq_icon_zan:R.mipmap.ggxq_icon_zan_p);

        loadNoticeDetail();

        if (notice.getAttachments()==null||notice.getAttachments().size()==0){
            tv_look_resources.setVisibility(View.GONE);
        }

        tv_look_resources.setOnClickListener(this);
        tv_share.setOnClickListener(this);
        ll_thumbs_up.setOnClickListener(this);
        stopProgressDialog();
    }

    /**
     * @author TanYong
     * create at 2017/6/14 15:36
     * TODO：加载公告详情
     */
    private void loadNoticeDetail() {
        wv_notice_detail.setHorizontalScrollBarEnabled(false);//水平不显示
        wv_notice_detail.setVerticalScrollBarEnabled(false); //垂直不显示

        wv_notice_detail.loadUrl(htmlUrl);
        wv_notice_detail.setWebViewClient(new webViewClient());
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
        wv_notice_detail.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByTagName('img'); " +
                "for(var i=0;i<objs.length;i++)  " +
                "{"
                + "var img = objs[i];   " +
                " img.style.maxWidth = '100%';img.style.height='auto';" +
                "}" +
                "})()");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_title_left:
                finish();
                break;
            case R.id.tv_share:
                share();
                break;
            case R.id.ll_thumbs_up:
                if(bind){
                    if("1".equals(notice.getIsThumbup())){
                        thumbUp("0");
                    }else{
                        thumbUp("1");
                    }
                }else{
                    telBindDialog();
                }
                break;
            case R.id.tv_look_resources:
                Bundle bundle = new Bundle();
                bundle.putSerializable("resourceList", (Serializable) notice.getAttachments());
                openActivity(NoticeResourcesListActivity.class, bundle);
                break;
        }
    }

    private void telBindDialog(){
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setTitle("绑定业主");
        dialog.setMessage("未绑定业主，APP部分功能受限制！");
        dialog.setCancelable(false);
        dialog.setNegativeButton("稍后绑定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.setPositiveButton("前往绑定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                openActivity(PersonalTelBindActivity.class);
            }
        });
        dialog.create().show();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if(EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }

    /**
     * 分享
     */
    private void share(){
        String thumbUrl=notice.getCoverUrl();
        if(!Tools.isEmpty(thumbUrl)){
            x.image().loadFile(thumbUrl, XUtilsImageUtils.getImageOption(), new Callback.CommonCallback<File>() {
                @Override
                public void onSuccess(File result) {
                    Log.e("loadFile","onSuccess");
                    toShare(result);
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    Log.e("loadFile",ex.getMessage());
                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {
                    Log.e("loadFile","onFinished");
                }
            });
        }else{
            toShare(null);
        }
    }

    private void toShare(File file){
        UMImage thumb;
        if(file!=null){
            thumb =  new UMImage(NoticeDetailActivity.this,file);
        }else{
            thumb =  new UMImage(this, R.drawable.ic_share_thumb);
        }
        UMWeb web = new UMWeb(htmlUrl);
        web.setTitle("通知公告");//标题
        web.setThumb(thumb);  //缩略图
        web.setDescription(notice.getTitle());//描述
        new ShareAction(NoticeDetailActivity.this)
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
//            showShortToast("取消了");
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}