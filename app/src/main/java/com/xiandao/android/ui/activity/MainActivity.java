package com.xiandao.android.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.gson.Gson;
import com.xiandao.android.R;
import com.xiandao.android.entity.eventbus.EventBusMessage;
import com.xiandao.android.entity.hikcloud.VideoCallPush;
import com.xiandao.android.entity.smart.CurrentDistrictEntity;
import com.xiandao.android.entity.smart.WorkflowType;
import com.xiandao.android.ui.BaseActivity;
import com.xiandao.android.ui.fragment.FindFragment;
import com.xiandao.android.ui.fragment.NewHomePageFragment;
import com.xiandao.android.ui.fragment.NewMineFragment;
import com.xiandao.android.utils.FileManagement;
import com.xiandao.android.utils.JpushUtil;
import com.xiandao.android.utils.LocalBroadcastManager;
import com.xiandao.android.utils.Tools;
import com.xiandao.android.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;
import org.xutils.LogUtils;
import org.xutils.event.annotation.ContentView;
import org.xutils.event.annotation.Event;
import org.xutils.event.annotation.ViewInject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * 此类描述的是:主界面
 *
 * @author TanYong
 *         create at 2017/4/21 19:27
 */
@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity implements View.OnClickListener {


    @ViewInject(R.id.vp_content)
    private com.xiandao.android.view.NoScrollViewPager vpContent;

    @ViewInject(R.id.rg_main)
    private RadioGroup rg_main;
    @ViewInject(R.id.rb_home)
    private RadioButton rb_home;
    @ViewInject(R.id.rb_mine)
    private RadioButton rb_mine;


    /**
     * 页面集合
     */
    private List<Fragment> fragmentList;

    //屏幕宽度
    int screenWidth;
    //当前选中的项
    int currenttab;

    /**
     * 三个Fragment（页面）
     */
//    private HomePageFragment homePageFragment;
    private NewHomePageFragment newHomePageFragment;
//    private FindFragment findFragment;
    //    NeighborhoodFragment neighborhoodFragment;
//    private MineFragment mineFragment;
    private NewMineFragment newMineFragment;

    public static boolean isForeground = false;

    private static final int MSG_SET_ALIAS = 1001;
    private static final int MSG_SET_TAGS = 1002;

    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    JPushInterface.setAliasAndTags(getApplicationContext(), (String) msg.obj, null, mAliasCallback);
                    break;
                case MSG_SET_TAGS:
                    JPushInterface.setAliasAndTags(getApplicationContext(), null, (Set<String>) msg.obj, mTagsCallback);
                    break;
                default:
                    break;
            }
        }
    };

    private final TagAliasCallback mTagsCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    Log.e("=====", "Set tag and alias success");
                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    Log.e("====", "Failed to set alias and tags due to timeout. Try again after 60s.");
                    if (JpushUtil.isConnected(getApplicationContext())) {
                        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_TAGS, tags), 1000 * 60);
                    } else {
                        Log.e("=====", "No network");
                    }
                    break;
                default:
                    logs = "Failed with errorCode = " + code;
                    Log.e("Failed with errorCode =", code + "");
                    break;
            }
//            Tools.showPrompt(logs);
        }
    };

    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    Log.e("=====", "Set tag and alias success");
                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    Log.e("====", "Failed to set alias and tags due to timeout. Try again after 60s.");
                    if (JpushUtil.isConnected(getApplicationContext())) {
                        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    } else {
                        Log.e("=====", "No network");
                    }
                    break;
                default:
                    logs = "Failed with errorCode = " + code;
                    Log.e("Failed with errorCode =", code + "");
            }
//            Tools.showPrompt(logs);
        }

    };

    private boolean bind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CurrentDistrictEntity currentDistrict=FileManagement.getUserInfoEntity().getCurrentDistrict();
        if(currentDistrict!=null&&!TextUtils.isEmpty(currentDistrict.getRoomId())){
            bind=true;
        }else{
            bind=false;
        }
        init();
        getPromiss();
        EventBus.getDefault().register(this);
        if(TextUtils.isEmpty(currentDistrict.getRoomId())){
            showUnBindView();
        }
    }

    private void showUnBindView(){
        View unSelectView= LayoutInflater.from(this).inflate(R.layout.dialog_unselect_room,null);
        Button selectBtn=unSelectView.findViewById(R.id.btn_select_room);
        selectBtn.setOnClickListener(this);
        Button unSelectBtn=unSelectView.findViewById(R.id.btn_un_select_room);
        unSelectBtn.setOnClickListener(this);
        ImageView closeBtn=unSelectView.findViewById(R.id.iv_un_select_room);
        closeBtn.setOnClickListener(this);
        startCustomerDialog(unSelectView,false);
    }

    private void getPromiss(){
        if(Build.VERSION.SDK_INT>=23){
            int hasPermission = checkSelfPermission(Manifest.permission.RECORD_AUDIO);
            if(hasPermission!= PackageManager.PERMISSION_GRANTED){
                String[] mPermissionList = new String[]{android.Manifest.permission.RECORD_AUDIO};
                ActivityCompat.requestPermissions(this,mPermissionList,123);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        if (requestCode == 123) {
            Log.e("RECORD_AUDIO","RECORD_AUDIO_OK");
        }
    }

    private void init() {

//        ll_home_page.setOnClickListener(this);
//        ll_mine.setOnClickListener(this);
//        ll_home_neighborhood.setOnClickListener(this);

        //两个不同的fragment
        fragmentList = new ArrayList<Fragment>();
        newHomePageFragment = new NewHomePageFragment();
//        findFragment = new FindFragment();
//        homePageFragment = new HomePageFragment();
//        neighborhoodFragment = new NeighborhoodFragment();
//        mineFragment = new MineFragment();
        newMineFragment = new NewMineFragment();

        //根据传值不一样区分两个界面的显示
        Bundle bundleHomePage = new Bundle();
        bundleHomePage.putInt("key", 1);

//        Bundle bundleNeighborhood = new Bundle();
//        bundleNeighborhood.putInt("key", 2);

//        Bundle bundleFind = new Bundle();
//        bundleFind.putInt("key", 2);

        Bundle bundleMine = new Bundle();
        bundleMine.putInt("key", 3);

        newHomePageFragment.setArguments(bundleHomePage);
//        findFragment.setArguments(bundleFind);
//        homePageFragment.setArguments(bundleHomePage);
//        neighborhoodFragment.setArguments(bundleNeighborhood);
//        mineFragment.setArguments(bundleMine);
        newMineFragment.setArguments(bundleMine);

        fragmentList.add(newHomePageFragment);
//        fragmentList.add(findFragment);
//        fragmentList.add(homePageFragment);
//        fragmentList.add(neighborhoodFragment);
//        fragmentList.add(mineFragment);
        fragmentList.add(newMineFragment);


        //屏幕的宽度
        screenWidth = getResources().getDisplayMetrics().widthPixels;
        vpContent.setOffscreenPageLimit(fragmentList.size());
//        前面一个参数是屏幕的二分之一，第二个参数是下划线的高度
//        FrameLayout.LayoutParams imageParams = new FrameLayout.LayoutParams(screenWidth / 3, 3);
//        iv_cursor.setLayoutParams(imageParams);
        vpContent.setAdapter(new MyFrageStatePagerAdapter(getSupportFragmentManager()));
        vpContent.setNoScroll(true);

        rg_main.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                vpContent.setCurrentItem(rg_main.indexOfChild(rg_main.findViewById(checkedId)));
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(EventBusMessage message){
        if("bind".equals(message.getMessage())){
            Log.e("bind","Main_bind");
            bind=true;
//            registerJPush();
            EventBus.getDefault().unregister(this);
        }else if("unbind".equals(message.getMessage())){
            LogUtils.d("============================="+"unbind"+"==========================");
            showUnBindView();
        }else if("projectSelect".equals(message.getMessage())){
            changeView(0);
        }
    }

    @Event({R.id.main_btn_unlock})
    private void onClickEvent(View v){
        switch (v.getId()){
            case R.id.main_btn_unlock:
                if(bind){
                    openActivity(UnLockListActivity.class);
                }else{
                    EventBus.getDefault().post(new EventBusMessage<>("unbind"));
                }
                break;
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (FileManagement.getIsFromShop()) {
            changeView(1);
            FileManagement.saveIsFromShop(false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //点击首页
            case R.id.ll_home_page1:
                changeView(0);
                break;
//            //点击邻里
//            case R.id.ll_home_neighborhood:
//                changeView(1);
//                break;
            //点击我的
            case R.id.ll_mine1:
                changeView(1);
                break;
            case R.id.btn_select_room:
                openActivity(HouseHoldActivity.class);
                stopProgressDialog();
                break;
            case R.id.btn_un_select_room:
                stopProgressDialog();
                break;
            case R.id.iv_un_select_room:
                stopProgressDialog();
                break;

        }
    }


    /**
     * 定义ViewPager适配器。
     */
    class MyFrageStatePagerAdapter extends FragmentStatePagerAdapter {

        public MyFrageStatePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        /**
         * 每次更新完成ViewPager的内容后，调用该接口，此处复写主要是为了让导航按钮上层的覆盖层能够动态的移动
         */
        @Override
        public void finishUpdate(ViewGroup container) {
            super.finishUpdate(container);//这句话要放在最前面，否则会报错
//            //获取当前的视图是位于ViewGroup的第几个位置，用来更新对应的覆盖层所在的位置
//            int currentItem = vpContent.getCurrentItem();
//            if (currentItem == currenttab) {
//                return;
//            }
//            imageMove(vpContent.getCurrentItem());
//            currenttab = vpContent.getCurrentItem();
        }
    }

    //手动设置ViewPager要显示的视图
    private void changeView(int desTab) {
        vpContent.setCurrentItem(desTab, true);
        imageMove(desTab);
    }

    /**
     * 移动覆盖层
     *
     * @param moveToTab 目标Tab，也就是要移动到的导航选项按钮的位置
     *                  第一个导航按钮对应0，第二个对应1，以此类推
     */
    private void imageMove(int moveToTab) {
//        rg_main.check(moveToTab);
        switch (moveToTab){
            case 0:
                rb_home.setChecked(true);
                break;
            case 1:
                rb_mine.setChecked(true);
                break;
        }
    }
//    private void imageMove(int moveToTab) {
//        //选中全部
//        if (moveToTab == 0) {
//            iv_home_page.setImageResource(R.mipmap.home_tab_sy_jin);
////            iv_neighborhood_page.setImageResource(R.mipmap.home_tab_linli);
//            iv_mine.setImageResource(R.mipmap.home_tab_wd_hui);
//            tv_home_page.setTextColor(getResources().getColor(R.color.title_backgroud));
////            tv_neighborhood_page.setTextColor(getResources().getColor(R.color.text_color));
//            tv_mine.setTextColor(getResources().getColor(R.color.text_color));
////        } else if (moveToTab == 1) {
////            iv_home_page.setImageResource(R.mipmap.home_tab_sy_sy);
//////            iv_neighborhood_page.setImageResource(R.mipmap.home_tab_linli_jin);
////            iv_mine.setImageResource(R.mipmap.home_tab_wd_hui);
////            tv_home_page.setTextColor(getResources().getColor(R.color.text_color));
//////            tv_neighborhood_page.setTextColor(getResources().getColor(R.color.title_backgroud));
////            tv_mine.setTextColor(getResources().getColor(R.color.text_color));
//        } else if (moveToTab == 1) {
//            iv_home_page.setImageResource(R.mipmap.home_tab_sy_sy);
////            iv_neighborhood_page.setImageResource(R.mipmap.home_tab_linli);
//            iv_mine.setImageResource(R.mipmap.home_tab_wd_jin);
//            tv_home_page.setTextColor(getResources().getColor(R.color.text_color));
////            tv_neighborhood_page.setTextColor(getResources().getColor(R.color.text_color));
//            tv_mine.setTextColor(getResources().getColor(R.color.title_backgroud));
//        }
//
////        int startPosition = 0;
////        int movetoPosition = 0;
////
////        startPosition = currenttab * (screenWidth / 3);
////        movetoPosition = moveToTab * (screenWidth / 3);
////        //平移动画
////        TranslateAnimation translateAnimation = new TranslateAnimation(startPosition, movetoPosition, 0, 0);
////        translateAnimation.setFillAfter(true);
////        translateAnimation.setDuration(200);
////        iv_cursor.startAnimation(translateAnimation);
//    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {//退出登录
            exit();
        }
        return false;
    }

    private long exitTime = 0;

    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Tools.showPrompt("再按一次退出程序");
            exitTime = System.currentTimeMillis();
        } else {
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);
        }
    }

    @Override
    protected void onResume() {
        isForeground = true;
        super.onResume();
    }


    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }

    //    @Override
//    protected void onDestroy() {
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
//        super.onDestroy();
//    }


    //for receive customer msg from jpush server
    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.xiandao.android.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                    String messge = intent.getStringExtra(KEY_MESSAGE);
                    String extras = intent.getStringExtra(KEY_EXTRAS);
                    StringBuilder showMsg = new StringBuilder();
                    showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                    if (!JpushUtil.isEmpty(extras)) {
                        showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                    }
                    JSONObject object=new JSONObject(messge);
                    String cmdType=object.getString("cmdType");
                    String callDate=object.getString("dateTime");
                    callDate =callDate.replace("T"," ").replace("+08:00","");
                    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Log.e("callDate",callDate);
                    Date date=sdf.parse(callDate);
                    if(new Date().getTime()-date.getTime()<10000){
                        if(cmdType.equals("request")){
                            Bundle bundle = new Bundle();
                            Gson gson=new Gson();
                            VideoCallPush videoCallPush=gson.fromJson(messge,VideoCallPush.class);
                            bundle.putString("message", messge);
                            bundle.putSerializable("videoCallPush",videoCallPush);
                            openActivity(VideoCallDialActivity.class,bundle);
                        }else if(cmdType.equals("cancel")){
                            //EventBus.getDefault().post(new EventBusMessage("videoCallCancel"));
                        }
                    }else{
                        Log.e("time",new Date().getTime()+"_"+date.getTime());
                    }

                }
            } catch (Exception e) {
            }
        }
    }



}
