package com.xiandao.android.ui.activity.shop;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiandao.android.AndroidApplication;
import com.xiandao.android.R;
import com.xiandao.android.adapter.HomeBannerAdapter;
import com.xiandao.android.entity.GoodsCommentOwnersEntity;
import com.xiandao.android.entity.GoodsEntity;
import com.xiandao.android.entity.OverallSituationEntity;
import com.xiandao.android.entity.ResourcesEntity;
import com.xiandao.android.entity.ShopCommentEntity;
import com.xiandao.android.entity.ShopCommentResultEntity;
import com.xiandao.android.entity.StoreEntity;
import com.xiandao.android.entity.StoreListResultEntity;
import com.xiandao.android.net.ApiHttpResult;
import com.xiandao.android.net.HttpUtils;
import com.xiandao.android.net.NetUtil;
import com.xiandao.android.ui.BaseActivity;
import com.xiandao.android.ui.activity.NoticeDetailActivity;
import com.xiandao.android.utils.Constants;
import com.xiandao.android.utils.GlideImageLoader;
import com.xiandao.android.utils.Tools;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import org.xutils.event.annotation.ContentView;
import org.xutils.event.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;


/**
 * 此类描述的是:商品详情activity
 *
 * @author TanYong
 *         create at 2017/6/14 17:09
 */
@ContentView(R.layout.activity_goods_detail)
public class GoodsDetailActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.iv_title_left)
    private ImageView ivTitleLeft;
    @ViewInject(R.id.tv_title_name)
    private TextView tvTitleName;
    @ViewInject(R.id.rv_goods_detail)
    private RecyclerView mRecyclerView;
    @ViewInject(R.id.wb_goods_detail)
    private WebView wb_goods_detail;
    @ViewInject(R.id.tv_goods_detail_store_name)
    private TextView tv_goods_detail_store_name;
    @ViewInject(R.id.tv_goods_detail_goods_name)
    private TextView tv_goods_detail_goods_name;
    @ViewInject(R.id.tv_goods_detail_discountprice)
    private TextView tv_goods_detail_discountprice;
    @ViewInject(R.id.tv_goods_detail_price)
    private TextView tv_goods_detail_price;
    @ViewInject(R.id.tv_goods_detail_size)
    private TextView tv_goods_detail_size;
    @ViewInject(R.id.ll_goods_detail_sc)
    private LinearLayout ll_goods_detail_sc;
    @ViewInject(R.id.ll_goods_detail_gwc)
    private LinearLayout ll_goods_detail_gwc;
    @ViewInject(R.id.tv_goods_detail_add_shopcar)
    private TextView tv_goods_detail_add_shopcar;
    @ViewInject(R.id.tv_goods_detail_buy)
    private TextView tv_goods_detail_buy;
    //评价
    @ViewInject(R.id.tv_buyer_name)
    private TextView tv_buyer_name;
    @ViewInject(R.id.tv_shop_comment_time)
    private TextView tv_shop_comment_time;
    @ViewInject(R.id.tv_shop_comment_content)
    private TextView tv_shop_comment_content;
    @ViewInject(R.id.tv_shop_comment_level)
    private TextView tv_shop_comment_level;
    @ViewInject(R.id.iv_star_1)
    private ImageView iv_star_1;
    @ViewInject(R.id.iv_star_2)
    private ImageView iv_star_2;
    @ViewInject(R.id.iv_star_3)
    private ImageView iv_star_3;
    @ViewInject(R.id.iv_star_4)
    private ImageView iv_star_4;
    @ViewInject(R.id.iv_star_5)
    private ImageView iv_star_5;
    @ViewInject(R.id.tv_query_more_shop_comment)
    private TextView tv_query_more_shop_comment;
    @ViewInject(R.id.tv_no_shop_comment)
    private TextView tv_no_shop_comment;
    @ViewInject(R.id.ll_shop_comment_content)
    private LinearLayout ll_shop_comment_content;

    private HomeBannerAdapter myAdapter;
    Banner mBanner;

    private GoodsEntity goodsEntity;
    private List<ResourcesEntity> resourceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (null != intent) {
            Bundle bundle = intent.getExtras();
            if (null != bundle) {
                goodsEntity = (GoodsEntity) bundle.getSerializable("GoodsEntity");
                if (null != goodsEntity) {
                    resourceList = goodsEntity.getResourceList();
                    init();
                }
            }
        }
    }

    private void init() {
        tvTitleName.setText("商品详情");
        ivTitleLeft.setOnClickListener(this);
        ll_goods_detail_gwc.setOnClickListener(this);
        tv_goods_detail_add_shopcar.setOnClickListener(this);
        tv_goods_detail_buy.setOnClickListener(this);
        tv_query_more_shop_comment.setOnClickListener(this);

        tv_goods_detail_store_name.setText(Tools.getStringValue(goodsEntity.getHomename()));
        tv_goods_detail_goods_name.setText(Tools.getStringValue(goodsEntity.getName()));
        tv_goods_detail_discountprice.setText("¥" + Tools.getStringValue(goodsEntity.getDiscountprice()));
        tv_goods_detail_price.setText("¥" + Tools.getStringValue(goodsEntity.getPrice()));
        tv_goods_detail_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        tv_goods_detail_size.setText(Tools.getStringValue(goodsEntity.getSize()));

        View header = LayoutInflater.from(this).inflate(R.layout.header, null);
        mBanner = (Banner) header.findViewById(R.id.banner);
        mBanner.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, AndroidApplication.H / 3));

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        myAdapter = new HomeBannerAdapter(this);
        myAdapter.setHeaderView(mBanner);
        mRecyclerView.setAdapter(myAdapter);

        List<String> images = new ArrayList<>();
        if (null != resourceList && resourceList.size() > 0) {
            ResourcesEntity resourcesEntity1 = resourceList.get(0);
            if (!Tools.isEmpty(resourcesEntity1.getUrl())) {
                String urlEntity = resourcesEntity1.getUrl();
                String[] urlList = urlEntity.split(",");
                for (String url : urlList) {
                    if (!url.substring(0, 1).equals("/")) {
                        url = "/" + url;
                    }
                    url = Constants.BASEHOST + url;
                    images.add(url);
                }
            }
        }
        mBanner.setImages(images)
//                .setBannerAnimation(CubeOutTransformer.class)
                .setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
                .setImageLoader(new GlideImageLoader())
                .setDelayTime(4000)
                .start();

        WebSettings settings = wb_goods_detail.getSettings();
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

        loadGoodsDetail(goodsEntity.getDetail());

        queryShopCommentList();
    }


    /**
     * @author TanYong
     * TODO：加载商品
     */
    private void loadGoodsDetail(String path) {
        wb_goods_detail.setHorizontalScrollBarEnabled(false);//水平不显示
        wb_goods_detail.setVerticalScrollBarEnabled(false); //垂直不显示
        if (!Tools.isEmpty(path)) {
            if (!path.substring(0, 1).equals("/")) {
                path = "/" + path;
            }
            path = Constants.BASEHOST + path;
            wb_goods_detail.loadUrl(path);
            wb_goods_detail.setWebViewClient(new webViewClient());
        }
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
        wb_goods_detail.loadUrl("javascript:(function(){" +
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
            case R.id.ll_goods_detail_gwc://跳转到购物车界面
                Bundle bundle = new Bundle();
                bundle.putString("storeId", goodsEntity.getShopType().getHomeid() + "");
                bundle.putBoolean("isFromDetail", true);
                openActivity(ShopMainActivity.class, bundle);
                break;
            case R.id.tv_goods_detail_add_shopcar://加入购物车
                addBuyCar(goodsEntity.getId() + "=1", goodsEntity.getShopType().getHomeid() + "");
                break;
            case R.id.tv_goods_detail_buy://立即购买

                break;
            case R.id.tv_query_more_shop_comment:
                Bundle bundleComment = new Bundle();
                bundleComment.putString("goodsId", goodsEntity.getId());
                openActivity(ShopCommentListActivity.class, bundleComment);
                break;
            default:
                break;
        }
    }

    /**
     * @author TanYong
     * create at 2018/3/1 09:53
     * TODO：获取商品评价列表
     */
    private void queryShopCommentList() {
        startProgressDialog("");
        ApiHttpResult.queryShopCommentList(this, new String[]{"goodsId", "page", "pagesize"},
                new Object[]{goodsEntity.getId(), 1, 1},
                new HttpUtils.DataCallBack() {
                    @Override
                    public void callBack(Object o) {
                        stopProgressDialog();
                        if (o != null) {
                            ShopCommentResultEntity resultEntity = (ShopCommentResultEntity) o;
                            if (resultEntity != null && resultEntity.getResultCode().equals("0")) {
                                List<ShopCommentEntity> entityList = resultEntity.getData().getComments();
                                if (entityList != null && entityList.size() > 0) {
                                    tv_no_shop_comment.setVisibility(View.GONE);
                                    ll_shop_comment_content.setVisibility(View.VISIBLE);
                                    ShopCommentEntity entity = entityList.get(0);
                                    if (null != entity) {
                                        GoodsCommentOwnersEntity owners = entity.getOwners();
                                        if (null != owners) {
                                            if (!Tools.isEmpty(owners.getName())) {
                                                String name = owners.getName();
                                                String buyerName = "";
                                                for (int i = 0; i < name.length(); i++) {
                                                    if (i == 0) {
                                                        buyerName = name.substring(0, 1);
                                                    } else {
                                                        buyerName = buyerName + "*";
                                                    }
                                                }
                                                tv_buyer_name.setText(buyerName);
                                            }
                                        }
                                        tv_shop_comment_time.setText(Tools.getStringValue(entity.getCommentdatetime()));
                                        tv_shop_comment_content.setText(Tools.getStringValue(entity.getContent()));
                                        int stargrade = Integer.valueOf(entity.getStargrade());
                                        switch (stargrade) {
                                            case 1:
                                                iv_star_1.setImageResource(R.mipmap.star_yellow);
                                                iv_star_2.setImageResource(R.mipmap.star_gray);
                                                iv_star_3.setImageResource(R.mipmap.star_gray);
                                                iv_star_4.setImageResource(R.mipmap.star_gray);
                                                iv_star_5.setImageResource(R.mipmap.star_gray);
                                                break;
                                            case 2:
                                                iv_star_1.setImageResource(R.mipmap.star_yellow);
                                                iv_star_2.setImageResource(R.mipmap.star_yellow);
                                                iv_star_3.setImageResource(R.mipmap.star_gray);
                                                iv_star_4.setImageResource(R.mipmap.star_gray);
                                                iv_star_5.setImageResource(R.mipmap.star_gray);
                                                break;
                                            case 3:
                                                iv_star_1.setImageResource(R.mipmap.star_yellow);
                                                iv_star_2.setImageResource(R.mipmap.star_yellow);
                                                iv_star_3.setImageResource(R.mipmap.star_yellow);
                                                iv_star_4.setImageResource(R.mipmap.star_gray);
                                                iv_star_5.setImageResource(R.mipmap.star_gray);
                                                break;
                                            case 4:
                                                iv_star_1.setImageResource(R.mipmap.star_yellow);
                                                iv_star_2.setImageResource(R.mipmap.star_yellow);
                                                iv_star_3.setImageResource(R.mipmap.star_yellow);
                                                iv_star_4.setImageResource(R.mipmap.star_yellow);
                                                iv_star_5.setImageResource(R.mipmap.star_gray);
                                                break;
                                            case 5:
                                                iv_star_1.setImageResource(R.mipmap.star_yellow);
                                                iv_star_2.setImageResource(R.mipmap.star_yellow);
                                                iv_star_3.setImageResource(R.mipmap.star_yellow);
                                                iv_star_4.setImageResource(R.mipmap.star_yellow);
                                                iv_star_5.setImageResource(R.mipmap.star_yellow);
                                                break;
                                            default:
                                                break;
                                        }
                                        int commentGrage = Integer.valueOf(entity.getCommentgrade());
                                        switch (commentGrage) {
                                            case 0:
                                                tv_shop_comment_level.setText("评价等级：差评");
                                                break;
                                            case 1:
                                                tv_shop_comment_level.setText("评价等级：中评");
                                                break;
                                            case 2:
                                                tv_shop_comment_level.setText("评价等级：好评");
                                                break;
                                            default:
                                                break;
                                        }
                                    }
                                } else {
                                    tv_no_shop_comment.setVisibility(View.VISIBLE);
                                    ll_shop_comment_content.setVisibility(View.GONE);
                                }
                            } else {
                                Tools.showPrompt(resultEntity.getMsg());
                            }
                        } else {
                            NetUtil.toNetworkSetting(GoodsDetailActivity.this);
                        }
                    }
                });
    }

    /**
     * 添加到购物车
     */
    private void addBuyCar(String goodses, String homeid) {
        startProgressDialog("");
        ApiHttpResult.addBuyCar(this, new String[]{"ownerid", "goodses", "homeid"},
                new Object[]{loginUserEntity.getUserId(), goodses, homeid},
                new HttpUtils.DataCallBack() {
                    @Override
                    public void callBack(Object o) {
                        stopProgressDialog();
                        if (o != null) {
                            OverallSituationEntity entity = (OverallSituationEntity) o;
                            if (entity != null) {
                                Tools.showPrompt(entity.getMsg());
                            }
                        } else {
                            NetUtil.toNetworkSetting(GoodsDetailActivity.this);
                        }
                    }
                });
    }
}
