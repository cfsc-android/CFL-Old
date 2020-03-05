package com.xiandao.android.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.xiandao.android.R;
import com.xiandao.android.ui.BaseActivity;
import com.xiandao.android.ui.fragment.FindFragment;
import com.xiandao.android.ui.fragment.NewHomePageFragment;
import com.xiandao.android.ui.fragment.NewMineFragment;
import com.xiandao.android.ui.fragment.SecondHandMarketFragment;
import com.xiandao.android.ui.fragment.SecondHandMineFragment;

import org.xutils.event.annotation.ContentView;
import org.xutils.event.annotation.Event;
import org.xutils.event.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_second_hand)
public class SecondHandActivity extends BaseActivity {
    @ViewInject(R.id.tv_title_name)
    private TextView tv_title_name;

    @ViewInject(R.id.vp_content)
    private com.xiandao.android.view.NoScrollViewPager vpContent;

    @ViewInject(R.id.rg_main)
    private RadioGroup rg_main;
    /**
     * 页面集合
     */
    private List<Fragment> fragmentList;

    //屏幕宽度
    int screenWidth;
    //当前选中的项
    int currenttab;


    private SecondHandMarketFragment marketFragment;
    private SecondHandMineFragment mineFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tv_title_name.setText("二手市场");
        init();
    }

    private void init(){
        fragmentList = new ArrayList<>();
        marketFragment=new SecondHandMarketFragment();
        mineFragment=new SecondHandMineFragment();
        fragmentList.add(marketFragment);
        fragmentList.add(mineFragment);
//屏幕的宽度
        screenWidth = getResources().getDisplayMetrics().widthPixels;
        vpContent.setOffscreenPageLimit(fragmentList.size());
        vpContent.setAdapter(new SecondHandActivity.MyFrageStatePagerAdapter(getSupportFragmentManager()));
        vpContent.setNoScroll(true);
        rg_main.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                vpContent.setCurrentItem(rg_main.indexOfChild(rg_main.findViewById(checkedId)));
            }
        });
    }

    @Event({R.id.iv_title_left,R.id.iv_second_hand_add})
    private void onClickEvent(View v){
        switch (v.getId()){
            case R.id.iv_title_left:
                finish();
                break;
            case R.id.iv_second_hand_add:
                openActivity(SecondHandPublishActivity.class);
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
    }
}
