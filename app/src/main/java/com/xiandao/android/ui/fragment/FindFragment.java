package com.xiandao.android.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiandao.android.R;
import com.xiandao.android.adapter.StaggeredGridAdapter;
import com.xiandao.android.entity.PersonalInfomation;
import com.xiandao.android.entity.eventbus.EventBusMessage;
import com.xiandao.android.entity.eventbus.NickNameEventBusData;
import com.xiandao.android.ui.BaseLazyFragment;
import com.xiandao.android.ui.activity.MyRepairsActivity;
import com.xiandao.android.ui.activity.PersonalInformationActivity;
import com.xiandao.android.ui.activity.PersonalTelBindActivity;
import com.xiandao.android.ui.activity.ReleaseThemeActivity;
import com.xiandao.android.utils.Constants;
import com.xiandao.android.utils.FileManagement;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.http.RequestParams;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FindFragment extends BaseLazyFragment implements View.OnClickListener {
    TextView tv_tab_1, tv_tab_2,tv_tab_3;
    ImageView iv_cursor;
    ViewPager vp_content;
    ImageView iv_to_release;

    /**
     * 页面集合
     */
    private List<Fragment> fragmentList;
    private AllFindFragment allFindFragment;
    private MyFindFragment myFindFragment;
    private FollowFindFragment followFindFragment;
    //屏幕宽度
    int screenWidth;
    //当前选中的项
    int currenttab;

    private LayoutInflater inflater;
    private boolean bind;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        if(FileManagement.getTokenInfo().equals("third")){
            bind=false;
        }else{
            bind=true;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(EventBusMessage message){
        if("bind".equals(message.getMessage())){
            Log.e("bind","Find_bind");
            bind=true;
            if(inflater!=null){
                View view = inflater.inflate(R.layout.fragment_find, null);
                setContentView(view);
                initView(view);
                init();
            }
        }
    }

    @Override
    protected void injectView(LayoutInflater inflater) {
        this.inflater=inflater;
        if(bind){
            View view = inflater.inflate(R.layout.fragment_find, null);
            setContentView(view);
            initView(view);
            init();
        }else{
            View view = inflater.inflate(R.layout.fragment_find_unbind, null);
            setContentView(view);
            initView(view);
        }

    }

    private void init() {
        //两个不同的fragment
        fragmentList = new ArrayList<Fragment>();
        allFindFragment = new AllFindFragment();
        myFindFragment = new MyFindFragment();
        followFindFragment=new FollowFindFragment();
        fragmentList.add(allFindFragment);
        fragmentList.add(myFindFragment);
        fragmentList.add(followFindFragment);

        vp_content.setAdapter(new FindFragment.MyFrageStatePagerAdapter(getChildFragmentManager()));
        vp_content.setOffscreenPageLimit(3);
    }

    private void initView(View view) {
        iv_to_release = (ImageView) view.findViewById(R.id.iv_to_release);
        if(!bind){
            iv_to_release.setVisibility(View.INVISIBLE);
        }
        iv_to_release.setOnClickListener(this);
        tv_tab_1 = (TextView) view.findViewById(R.id.tv_tab_1);
        tv_tab_1.setOnClickListener(this);
        tv_tab_2 = (TextView) view.findViewById(R.id.tv_tab_2);
        tv_tab_2.setOnClickListener(this);
        tv_tab_3= (TextView) view.findViewById(R.id.tv_tab_3);
        tv_tab_3.setOnClickListener(this);
        iv_cursor = (ImageView) view.findViewById(R.id.iv_cursor);
        //屏幕的宽度
        screenWidth = getResources().getDisplayMetrics().widthPixels;
        //前面一个参数是屏幕的二分之一，第二个参数是下划线的高度
        FrameLayout.LayoutParams imageParams = new FrameLayout.LayoutParams(screenWidth / 3, 5);
        iv_cursor.setLayoutParams(imageParams);
        if(bind){
            vp_content = (ViewPager) view.findViewById(R.id.vp_content);
        }else{
            Button btn_find_unbind= (Button) view.findViewById(R.id.btn_find_unbind);
            btn_find_unbind.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openActivity(PersonalTelBindActivity.class);
                }
            });
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_to_release://跳转发新鲜事
                startActivity(new Intent(getActivity(), ReleaseThemeActivity.class));
                break;
            case R.id.tv_tab_1:
                if(bind){
                    changeView(0);
                }else{
                    imageMove(0);
                    currenttab=0;
                }
                break;
            case R.id.tv_tab_2:
                if(bind){
                    changeView(1);
                }else{
                    imageMove(1);
                    currenttab=1;
                }
                break;
            case R.id.tv_tab_3:
                if(bind){
                    changeView(2);
                }else{
                    imageMove(2);
                    currenttab=2;
                }
                break;

        }
    }

    private void changeView(int desTab) {
        vp_content.setCurrentItem(desTab, true);
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
            //获取当前的视图是位于ViewGroup的第几个位置，用来更新对应的覆盖层所在的位置
            int currentItem = vp_content.getCurrentItem();
            if (currentItem == currenttab) {
                return;
            }
            imageMove(vp_content.getCurrentItem());
            currenttab = vp_content.getCurrentItem();
        }

    }

    /**
     * 移动覆盖层
     *
     * @param moveToTab 目标Tab，也就是要移动到的导航选项按钮的位置
     *                  第一个导航按钮对应0，第二个对应1，以此类推
     */
    private void imageMove(int moveToTab) {
        int startPosition = 0;
        int movetoPosition = 0;

        startPosition = currenttab * (screenWidth / 3);
        movetoPosition = moveToTab * (screenWidth / 3);
        //平移动画
        TranslateAnimation translateAnimation = new TranslateAnimation(startPosition, movetoPosition, 0, 0);
        translateAnimation.setFillAfter(true);
        translateAnimation.setDuration(200);
        iv_cursor.startAnimation(translateAnimation);

        //选中全部
        if (moveToTab == 0) {
            tv_tab_1.setTextColor(getResources().getColor(R.color.title_backgroud));
            tv_tab_2.setTextColor(getResources().getColor(R.color.tab_no_onclicked_text_color));
            tv_tab_3.setTextColor(getResources().getColor(R.color.tab_no_onclicked_text_color));
        } else if (moveToTab == 1) {
            tv_tab_1.setTextColor(getResources().getColor(R.color.tab_no_onclicked_text_color));
            tv_tab_2.setTextColor(getResources().getColor(R.color.title_backgroud));
            tv_tab_3.setTextColor(getResources().getColor(R.color.tab_no_onclicked_text_color));
        }else if (moveToTab == 2) {
            tv_tab_1.setTextColor(getResources().getColor(R.color.tab_no_onclicked_text_color));
            tv_tab_2.setTextColor(getResources().getColor(R.color.tab_no_onclicked_text_color));
            tv_tab_3.setTextColor(getResources().getColor(R.color.title_backgroud));
        }
    }
}
