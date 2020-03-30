package com.xiandao.android.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.xiandao.android.R;
import com.xiandao.android.entity.ImageBanner;
import com.xiandao.android.entity.LoginUserEntity;
import com.xiandao.android.entity.eventbus.EventBusMessage;
import com.xiandao.android.entity.smart.BaseEntity;
import com.xiandao.android.entity.smart.NoticeEntity;
import com.xiandao.android.entity.smart.NoticeListEntity;
import com.xiandao.android.entity.smart.NoticeReceiverType;
import com.xiandao.android.entity.smart.NoticeType;
import com.xiandao.android.http.JsonParse;
import com.xiandao.android.http.MyCallBack;
import com.xiandao.android.http.XUtils;
import com.xiandao.android.net.ApiHttpResult;
import com.xiandao.android.net.HttpUtils;
import com.xiandao.android.net.NetUtil;
import com.xiandao.android.ui.BaseLazyFragment;
import com.xiandao.android.ui.activity.CarLock;
import com.xiandao.android.ui.activity.ComplainActivity;
import com.xiandao.android.ui.activity.H5LinkActivity;
import com.xiandao.android.ui.activity.H5TestActivity;
import com.xiandao.android.ui.activity.JoinInActivity;
import com.xiandao.android.ui.activity.LifePayActivity;
import com.xiandao.android.ui.activity.LifePaymentActivity;
import com.xiandao.android.ui.activity.NeighborhoodActivity;
import com.xiandao.android.ui.activity.NewsInfoActivity;
import com.xiandao.android.ui.activity.NoticeActivity;
import com.xiandao.android.ui.activity.NoticeDetailActivity;
import com.xiandao.android.ui.activity.ParkingPaymentActivity;
import com.xiandao.android.ui.activity.ProjectProgressActivity;
import com.xiandao.android.ui.activity.RepairsActivity;
import com.xiandao.android.ui.activity.SecondHandActivity;
import com.xiandao.android.ui.activity.UnLock;
import com.xiandao.android.ui.activity.VideoCallActivity;
import com.xiandao.android.ui.activity.VisitorActivity;
import com.xiandao.android.ui.activity.WaitingForDevelopmentActivity;
import com.xiandao.android.ui.activity.shop.StoreListActivity;
import com.xiandao.android.utils.Constants;
import com.xiandao.android.utils.FileManagement;
import com.xiandao.android.utils.Tools;
import com.xiandao.android.utils.XUtilsImageUtils;
import com.xiandao.android.view.ADTextView;
import com.xiandao.android.view.OnAdConetentClickListener;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoaderInterface;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.LogUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.util.TextUtils;

import static com.xiandao.android.base.Config.ARTICLE;
import static com.xiandao.android.base.Config.BASE_URL;

public class NewHomePageFragment extends BaseLazyFragment implements View.OnClickListener {
    TextView tv_to_menjin;
    TextView tv_to_baoxiu;
    TextView tv_to_tousu;
    TextView iv_to_jiesuo;
    TextView tv_to_gonggao;
    TextView tv_to_tongzhi;
    TextView tv_home_page_ll;
    TextView tv_to_shjf;
    TextView tv_shoping;
    TextView tv_to_zhoubian;
    TextView tv_to_more;
    TextView tv_service;
    LinearLayout ll_new_home_notice_detail;
    ADTextView ad_textview;
    Banner banner_home_ad;
    TextView tv_to_visitor;
    TextView tv_to_video_call;
    TextView tv_home_page_parking_payment;
    TextView tv_project_progress;
    TextView tv_property_right;
    ImageView iv_project_icon;
    TextView tv_project_logo_name;

    private LoginUserEntity loginUserEntity;
//    private ArrayList<NoticeEntity> hotTopicList = new ArrayList<>();
//    private ArrayList<NoticeEntity> bannerList = new ArrayList<>();
//    private List<ImageBanner> imageUrls=new ArrayList<>();

    private boolean bind;

    private ArrayList<NoticeEntity> hotTopicList=new ArrayList<>();
    private List<NoticeEntity> bannerList=new ArrayList<>();
    private List<ImageBanner> imageUrls=new ArrayList<>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(FileManagement.getUserInfoEntity().getCurrentDistrict()!=null&&!TextUtils.isEmpty(FileManagement.getUserInfoEntity().getCurrentDistrict().getRoomId())){
            bind=true;
        }else{
            bind=false;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(EventBusMessage message){
        if("projectSelect".equals(message.getMessage())){
            tv_project_logo_name.setText(FileManagement.getUserInfoEntity().getCurrentDistrict().getProjectName());
            getHotTips();
            getWheelPlanting();
        }
    }

    @Override
    protected void injectView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_home, null);
        setContentView(view);
        loginUserEntity = FileManagement.getLoginUserEntity();
        initView(view);
    }

    private void initView(View view) {
        tv_to_menjin = (TextView) view.findViewById(R.id.tv_to_menjin);
        tv_to_menjin.setOnClickListener(this);
        tv_to_baoxiu = (TextView) view.findViewById(R.id.tv_repair);
        tv_to_baoxiu.setOnClickListener(this);
        tv_to_tousu = (TextView) view.findViewById(R.id.tv_complaint);
        tv_to_tousu.setOnClickListener(this);
        iv_to_jiesuo = (TextView) view.findViewById(R.id.iv_to_jiesuo);
        iv_to_jiesuo.setOnClickListener(this);
        tv_to_gonggao = (TextView) view.findViewById(R.id.tv_to_gonggao);
        tv_to_gonggao.setOnClickListener(this);
        tv_to_tongzhi = (TextView) view.findViewById(R.id.tv_to_tongzhi);
        tv_to_tongzhi.setOnClickListener(this);
        tv_home_page_ll = (TextView) view.findViewById(R.id.tv_home_page_ll);
        tv_home_page_ll.setOnClickListener(this);
        tv_to_shjf = (TextView) view.findViewById(R.id.tv_to_shjf);
        tv_to_shjf.setOnClickListener(this);
        tv_shoping = (TextView) view.findViewById(R.id.tv_shoping);
        tv_shoping.setOnClickListener(this);
        tv_to_zhoubian = (TextView) view.findViewById(R.id.tv_to_zhoubian);
        tv_to_zhoubian.setOnClickListener(this);
        tv_to_more = (TextView) view.findViewById(R.id.tv_to_more);
        tv_to_more.setOnClickListener(this);
        tv_service = (TextView) view.findViewById(R.id.tv_service);
        tv_service.setOnClickListener(this);
        ll_new_home_notice_detail = (LinearLayout) view.findViewById(R.id.ll_new_home_notice_detail);
        ad_textview = (ADTextView) view.findViewById(R.id.ad_textview);
        tv_to_visitor= (TextView) view.findViewById(R.id.tv_to_visitor);
        tv_to_visitor.setOnClickListener(this);
        tv_to_video_call= (TextView) view.findViewById(R.id.tv_to_video_call);
        tv_to_video_call.setOnClickListener(this);
        tv_home_page_parking_payment= (TextView) view.findViewById(R.id.tv_home_page_parking_payment);
        tv_home_page_parking_payment.setOnClickListener(this);
        tv_project_progress= (TextView) view.findViewById(R.id.tv_project_progress);
        tv_project_progress.setOnClickListener(this);
        tv_property_right= (TextView) view.findViewById(R.id.tv_property_right);
        tv_property_right.setOnClickListener(this);
        banner_home_ad = (Banner) view.findViewById(R.id.banner_home_ad);
        banner_home_ad.setOffscreenPageLimit(5);
        banner_home_ad.setImageLoader(new ImageLoaderInterface(){
            @Override
            public void displayImage(Context context, Object path, View imageView) {
                Glide.with(context).load(((ImageBanner)path).getImageUrl()).into((ImageView) imageView);
//                XUtilsImageUtils.display((ImageView) imageView,((ImageBanner)path).getImageUrl());
            }

            @Override
            public View createImageView(Context context) {
                return null;
            }
        });
        banner_home_ad.setOnBannerListener(new OnBannerListener(){
            @Override
            public void OnBannerClick(int position) {
                Bundle bundle = new Bundle();
                bundle.putString("title","新闻动态");
                bundle.putString("noticeId", bannerList.get(position).getId());
                openActivity(NoticeDetailActivity.class, bundle);
            }
        });
        banner_home_ad.start();
        tv_project_logo_name= (TextView) view.findViewById(R.id.tv_project_logo_name);

//        iv_project_icon= (ImageView) view.findViewById(R.id.iv_project_icon);
//        iv_project_icon.setImageResource(FileManagement.getProjectInfo().getProjectIcon());
    }

    @Override
    protected void onFragmentStartLazy() {
        super.onFragmentStartLazy();
        if(FileManagement.getUserInfoEntity().getCurrentDistrict()!=null&&!TextUtils.isEmpty(FileManagement.getUserInfoEntity().getCurrentDistrict().getRoomId())){
            bind=true;
        }else{
            bind=false;
        }
        tv_project_logo_name.setText(FileManagement.getUserInfoEntity().getCurrentDistrict().getProjectName());
        getHotTips();
        getWheelPlanting();
    }

    private void getHotTips(){
        Map<String,String> map=new HashMap<>();
        map.put("projectId",FileManagement.getUserInfoEntity().getCurrentDistrict().getProjectId());
        map.put("receiver", NoticeReceiverType.全部.getType()+","+NoticeReceiverType.业主.getType());
        map.put("announcementTypeId", NoticeType.热点关注.getType());
        map.put("auditStatus","1");
        map.put("pageNo","1");
        map.put("pageSize","10");
        XUtils.Get(BASE_URL+ARTICLE+"smart/content/pages",map,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtils.d(result);
                BaseEntity<NoticeListEntity> baseEntity= JsonParse.parse(result,NoticeListEntity.class);
                if(baseEntity.isSuccess()){
                    hotTopicList.clear();
                    hotTopicList.addAll(baseEntity.getResult().getData());
                    if(hotTopicList.size()>0){
                        ll_new_home_notice_detail.setVisibility(View.VISIBLE);
                        initADTextView();
                    }else{
                        ll_new_home_notice_detail.setVisibility(View.GONE);
                    }

                }else{
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

    private void getWheelPlanting(){
        Map<String,String> map=new HashMap<>();
        map.put("projectId",FileManagement.getUserInfoEntity().getCurrentDistrict().getProjectId());
        map.put("receiver", NoticeReceiverType.全部.getType()+","+NoticeReceiverType.业主.getType());
        map.put("announcementTypeId", NoticeType.轮播动态.getType());
        map.put("auditStatus","1");
        map.put("pageNo","1");
        map.put("pageSize","10");
        XUtils.Get(BASE_URL+ARTICLE+"smart/content/pages",map,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtils.d(result);
                BaseEntity<NoticeListEntity> baseEntity= JsonParse.parse(result,NoticeListEntity.class);
                if(baseEntity.isSuccess()){
                    bannerList.clear();
                    imageUrls.clear();
                    bannerList.addAll(baseEntity.getResult().getData());
                    for (NoticeEntity noticeEntity : bannerList) {
                        imageUrls.add(new ImageBanner(noticeEntity.getDetailUrl(),noticeEntity.getCoverUrl()));
                    }
                    banner_home_ad.update(imageUrls);

                }else{
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


    private void initADTextView() {
        ad_textview.setSpeed(2);
        ad_textview.setData(hotTopicList);
        ad_textview.setMode(ADTextView.RunMode.UP);
        ad_textview.setOnAdConetentClickListener(new OnAdConetentClickListener() {
            @Override
            public void OnAdConetentClickListener(int index, NoticeEntity noticeEntity) {
                NoticeEntity selectNotice = hotTopicList.get(index);
                Bundle bundle = new Bundle();
                bundle.putString("title","热点关注");
                bundle.putString("noticeId", selectNotice.getId());
                openActivity(NoticeDetailActivity.class, bundle);


            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_to_menjin:
                //startActivity(new Intent(getActivity(), WaitingForDevelopmentActivity.class).putExtra("title", "门禁开锁"));
                if(bind){
//                    openActivity(H5LinkActivity.class);
                    openActivity(UnLock.class);
                }else{
                    EventBus.getDefault().post(new EventBusMessage<>("unbind"));
                }
                break;
            case R.id.tv_repair:
                if(bind){
                    openActivity(RepairsActivity.class);
                }else{
                    EventBus.getDefault().post(new EventBusMessage<>("unbind"));
                }
                break;
            case R.id.tv_complaint:
                if(bind){
                    openActivity(ComplainActivity.class);
                }else{
                    EventBus.getDefault().post(new EventBusMessage<>("unbind"));
                }
                break;
            case R.id.iv_to_jiesuo:
                if(bind){
                    openActivity(CarLock.class);
                }else{
                    EventBus.getDefault().post(new EventBusMessage<>("unbind"));
                }
                break;
            case R.id.tv_to_tongzhi:
                Bundle noticeBundle=new Bundle();
                noticeBundle.putString("notice_type",NoticeType.社区公告.getType());
                openActivity(NoticeActivity.class,noticeBundle);
                break;
            case R.id.tv_home_page_ll:
                openActivity(NeighborhoodActivity.class);
                break;
            case R.id.tv_to_shjf:
                openActivity(LifePaymentActivity.class);
//                openActivity(LifePayActivity.class);
                break;
            case R.id.tv_shoping:
                openActivity(SecondHandActivity.class);
//                openActivity(StoreListActivity.class);
//                startActivity(new Intent(getActivity(), WaitingForDevelopmentActivity.class).putExtra("title", "二手市场"));
                break;
            case R.id.tv_to_zhoubian:
                Bundle a_bundle=new Bundle();
                a_bundle.putString("title","周边服务");
                a_bundle.putString("url","https://map.baidu.com/mobile/webapp/index/index");
                openActivity(NewsInfoActivity.class,a_bundle);
//                startActivity(new Intent(getActivity(), WaitingForDevelopmentActivity.class).putExtra("title", "周边服务"));
                break;
            case R.id.tv_to_more:
                Bundle joinBundle=new Bundle();
                joinBundle.putString("notice_type",NoticeType.入伙.getType());
                openActivity(NoticeActivity.class,joinBundle);
                break;
            case R.id.tv_service:
                Bundle bundle1 = new Bundle();
                bundle1.putBoolean("service", true);
                openActivity(StoreListActivity.class, bundle1);
                break;
            case R.id.tv_to_visitor:
                if(bind){
                    openActivity(VisitorActivity.class);
                }else{
                    EventBus.getDefault().post(new EventBusMessage<>("unbind"));
                }
                break;
            case R.id.tv_to_video_call:
                if(bind){
                    openActivity(VideoCallActivity.class);
                }else{
                    EventBus.getDefault().post(new EventBusMessage<>("unbind"));
                }
                break;
            case R.id.tv_home_page_parking_payment:
                openActivity(ParkingPaymentActivity.class);
                break;
            case R.id.tv_project_progress:
                Bundle projectBundle=new Bundle();
                projectBundle.putString("notice_type",NoticeType.工程进程.getType());
                openActivity(NoticeActivity.class,projectBundle);
                break;
            case R.id.tv_property_right:
                Bundle bundle_b=new Bundle();
                bundle_b.putString("title","产证查询");
                bundle_b.putString("url","http://szjw.changsha.gov.cn/ywcx/");
                bundle_b.putString("rightAction","share");
                openActivity(NewsInfoActivity.class,bundle_b);
                break;
            default:
                break;
        }
    }
}
