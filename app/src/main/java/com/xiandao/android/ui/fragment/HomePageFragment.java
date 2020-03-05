package com.xiandao.android.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiandao.android.AndroidApplication;
import com.xiandao.android.R;
import com.xiandao.android.adapter.HomeBannerAdapter;
import com.xiandao.android.entity.LoginUserEntity;
import com.xiandao.android.entity.NoticeEntity;
import com.xiandao.android.entity.NoticeListEntity;
import com.xiandao.android.net.ApiHttpResult;
import com.xiandao.android.net.HttpUtils;
import com.xiandao.android.net.NetUtil;
import com.xiandao.android.ui.BaseLazyFragment;
import com.xiandao.android.ui.activity.LifePayActivity;
import com.xiandao.android.ui.activity.LoginActivity;
import com.xiandao.android.ui.activity.NeighborhoodActivity;
import com.xiandao.android.ui.activity.NoticeActivity;
import com.xiandao.android.ui.activity.NoticeDetailActivity;
import com.xiandao.android.ui.activity.PostActivity;
import com.xiandao.android.ui.activity.RepairsActivity;
import com.xiandao.android.ui.activity.ComplainActivity;
import com.xiandao.android.ui.activity.shop.StoreListActivity;
import com.xiandao.android.utils.FileManagement;
import com.xiandao.android.utils.GlideImageLoader;
import com.xiandao.android.utils.Tools;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.transformer.CubeOutTransformer;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * 此类描述的是:首页fragment
 *
 * @author TanYong
 * create at 2017/5/6 14:37
 */
public class HomePageFragment extends BaseLazyFragment implements View.OnClickListener {
    ImageView iv_title_left;
    ImageView iv_to_baoxiu;
    private ImageView iv_to_tousu;
    private ImageView iv_to_gonggao;
    private ImageView iv_to_yezhu_tongzhi;
    private ImageView iv_shoping;
    private ImageView iv_home_page_ll;
    private ImageView iv_to_shjf;
    private ImageView iv_service;
    private Intent intent;
    private HomeBannerAdapter myAdapter;
    Banner mBanner;
    RecyclerView mRecyclerView;


    private LinearLayout ll_home_notice_detail;
    private LoginUserEntity loginUserEntity;

    private TextView tv_home_notice_title;
    private TextView tv_home_notice_des;

    private String titleName;
    private String url;
    private String publisherName;
    private String publishTime;
    private int praises;
    private int reads;
    private String noticeId;
    private String resources;

    @Override
    protected void injectView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_main, null);
        x.view().inject(this, view);
        setContentView(view);
        initView(view);
        loginUserEntity = FileManagement.getLoginUserEntity();
        init();
    }

    @Override
    protected void onFragmentStartLazy() {
        super.onFragmentStartLazy();
        queryNoticeList();
    }

    protected void initView(View view) {
        iv_title_left = (ImageView) view.findViewById(R.id.iv_title_left);
        iv_title_left.setVisibility(View.GONE);
        iv_to_baoxiu = (ImageView) view.findViewById(R.id.iv_to_baoxiu);
        iv_to_tousu = (ImageView) view.findViewById(R.id.iv_to_tousu);
        iv_to_gonggao = (ImageView) view.findViewById(R.id.iv_to_gonggao);
        iv_to_yezhu_tongzhi = (ImageView) view.findViewById(R.id.iv_to_yezhu_tongzhi);
        iv_home_page_ll = (ImageView) view.findViewById(R.id.iv_home_page_ll);
        iv_shoping = (ImageView) view.findViewById(R.id.iv_shoping);
        iv_to_shjf = (ImageView) view.findViewById(R.id.iv_to_shjf);
        iv_service = (ImageView) view.findViewById(R.id.iv_service);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        ll_home_notice_detail = (LinearLayout) view.findViewById(R.id.ll_home_notice_detail);

        tv_home_notice_title = (TextView) view.findViewById(R.id.tv_home_notice_title);
        tv_home_notice_des = (TextView) view.findViewById(R.id.tv_home_notice_des);

        View header = LayoutInflater.from(getActivity()).inflate(R.layout.header, null);
        mBanner = (Banner) header.findViewById(R.id.banner);
        mBanner.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, AndroidApplication.H / 3));

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        myAdapter = new HomeBannerAdapter(getActivity());
        myAdapter.setHeaderView(mBanner);
        mRecyclerView.setAdapter(myAdapter);

        List<Integer> images = new ArrayList<>();
        images.add(R.mipmap.home_1);
        images.add(R.mipmap.home_2);
        images.add(R.mipmap.home_3);
        mBanner.setImages(images)
//                .setBannerAnimation(CubeOutTransformer.class)
                .setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
                .setImageLoader(new GlideImageLoader())
                .setDelayTime(4000)
                .start();
    }


    private void init() {
        iv_to_baoxiu.setOnClickListener(this);
        iv_to_tousu.setOnClickListener(this);
        iv_to_gonggao.setOnClickListener(this);
        iv_to_yezhu_tongzhi.setOnClickListener(this);
        ll_home_notice_detail.setOnClickListener(this);
        iv_home_page_ll.setOnClickListener(this);
        iv_to_shjf.setOnClickListener(this);
        iv_shoping.setOnClickListener(this);
        iv_service.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_to_baoxiu:
                openActivity(RepairsActivity.class);
                break;
            case R.id.iv_to_tousu:
                openActivity(ComplainActivity.class);
                break;
            case R.id.iv_to_yezhu_tongzhi:
//                intent = new Intent(getActivity(), NoticeActivity.class);
//                intent.putExtra("fromWhere", 1);
//                startActivity(intent);
                break;
            case R.id.iv_to_gonggao:
                openActivity(NoticeActivity.class);
                break;
            case R.id.iv_home_page_ll:
                openActivity(NeighborhoodActivity.class);
                break;
            case R.id.ll_home_notice_detail:
                Bundle bundle = new Bundle();
                bundle.putString("titleName", titleName);
                bundle.putString("url", url);
                bundle.putString("publisherName", publisherName);
                bundle.putString("publishTime", publishTime);
                bundle.putString("noticeId", noticeId);
                bundle.putString("resources", resources);
                bundle.putInt("praises", praises);
                bundle.putInt("reads", reads);
                openActivity(NoticeDetailActivity.class, bundle);
                break;
            case R.id.iv_to_shjf:
                openActivity(LifePayActivity.class);
                break;
            case R.id.iv_shoping:
                openActivity(StoreListActivity.class);
                break;
            case R.id.iv_service:
                Bundle bundle1 = new Bundle();
                bundle1.putBoolean("service", true);
                openActivity(StoreListActivity.class, bundle1);
                break;
            default:
                break;
        }
    }

    /**
     * @author TanYong
     * create at 2017/6/14 17:23
     * receive:1、业主；2、员工
     * TODO：获取通知公告列表
     */
    private void queryNoticeList() {
        startProgressDialog("");
        ApiHttpResult.queryNoticeList(getActivity(), new String[]{"currentPage", "pageSize", "receive", "appMobile", "projectid"},
                new Object[]{1, 1, "1", loginUserEntity.getMobileNumber(), Tools.getStringValue(loginUserEntity.getProjectId())},
                new HttpUtils.DataCallBack() {
                    @Override
                    public void callBack(Object o) {
                        stopProgressDialog();
                        if (o != null) {
                            NoticeListEntity noticeListEntity = (NoticeListEntity) o;
                            if (noticeListEntity != null && noticeListEntity.getResultCode().equals("0")) {
                                ll_home_notice_detail.setVisibility(View.VISIBLE);
                                ArrayList<NoticeEntity> resultNoticeList = noticeListEntity.getNoticeList();
                                if (resultNoticeList != null && resultNoticeList.size() > 0) {
                                    NoticeEntity entity = resultNoticeList.get(0);
                                    tv_home_notice_title.setText(Tools.getStringValue(entity.getTitle()));
                                    tv_home_notice_des.setText(Tools.getStringValue(entity.getRemark()));
                                    titleName = entity.getTitle();
                                    url = entity.getDetailUrl();
                                    publisherName = entity.getPublisherName();
                                    publishTime = entity.getPublishTime();
                                    praises = entity.getPraises();
                                    reads = entity.getReads();
                                    noticeId = entity.getNoticeId();
                                    resources = entity.getResources();
                                } else {
                                    ll_home_notice_detail.setVisibility(View.GONE);
                                }
                            } else {
                                Tools.showPrompt(noticeListEntity.getMsg());
                            }
                        } else {
                            NetUtil.toNetworkSetting(getActivity());
                        }
                    }
                });
    }
}
