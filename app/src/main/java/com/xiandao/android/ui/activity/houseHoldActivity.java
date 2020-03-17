package com.xiandao.android.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.xiandao.android.R;
import com.xiandao.android.ui.BaseActivity;
import com.xiandao.android.ui.fragment.HouseCurrentFragment;
import com.xiandao.android.ui.fragment.HouseOthersFragment;

import java.util.ArrayList;
import java.util.List;

public class houseHoldActivity extends BaseActivity implements View.OnClickListener {

    private TextView item_house_current, item_house_others, tv_manager_house;
    private ViewPager vp;
    private HouseCurrentFragment houseCurrentFragment;
    private HouseOthersFragment houseOthersFragment;
    private List<Fragment> mFragmentList = new ArrayList<Fragment>();
    private FragmentAdapter mFragmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除工具栏
        //getSupportActionBar().hide();
        setContentView(R.layout.activity_house_hold);
        initViews();

        mFragmentAdapter = new FragmentAdapter(this.getSupportFragmentManager(), mFragmentList);
        vp.setOffscreenPageLimit(2);//ViewPager的缓存为4帧
        vp.setAdapter(mFragmentAdapter);
        vp.setCurrentItem(0);//初始设置ViewPager选中第一帧
        item_house_current.setTextColor(Color.parseColor("#66CDAA"));

        //ViewPager的监听事件
        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                /*此方法在页面被选中时调用*/

                changeTextColor(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                /*此方法是在状态改变的时候调用，其中arg0这个参数有三种状态（0，1，2）。
                arg0 ==1的时辰默示正在滑动，
                arg0==2的时辰默示滑动完毕了，
                arg0==0的时辰默示什么都没做。*/
            }
        });
    }

    /**
     * 初始化布局View
     */
    private void initViews() {
//        title = (TextView) findViewById(R.id.title);
        item_house_current = (TextView) findViewById(R.id.item_house_current);
        item_house_others = (TextView) findViewById(R.id.item_house_others);
        tv_manager_house = findViewById(R.id.tv_manager_house);

        item_house_current.setOnClickListener(this);
        item_house_others.setOnClickListener(this);
        tv_manager_house.setOnClickListener(this);

        vp = (ViewPager) findViewById(R.id.mainViewPager);
        houseCurrentFragment = new HouseCurrentFragment();
        houseOthersFragment = new HouseOthersFragment();

        //给FragmentList添加数据
        mFragmentList.add(houseCurrentFragment);
        mFragmentList.add(houseOthersFragment);

    }

    /**
     * 点击Text 动态修改ViewPager的内容
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.item_house_current:
                vp.setCurrentItem(0, true);
                break;
            case R.id.item_house_others:
                vp.setCurrentItem(1, true);
                break;
            case R.id.tv_manager_house:
                openActivity(HouseManageActivity.class);
                break;

        }
    }


    public class FragmentAdapter extends FragmentPagerAdapter {

        List<Fragment> fragmentList = new ArrayList<Fragment>();

        public FragmentAdapter(FragmentManager fm, List<Fragment> fragmentList) {
            super(fm);
            this.fragmentList = fragmentList;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

    }

    /*
     *由ViewPager的滑动修改导航Text的颜色
     */
    private void changeTextColor(int position) {
        if (position == 0) {
            item_house_current.setTextColor(Color.parseColor("#66CDAA"));
            item_house_others.setTextColor(Color.parseColor("#000000"));
        } else if (position == 1) {
            item_house_current.setTextColor(Color.parseColor("#000000"));
            item_house_others.setTextColor(Color.parseColor("#66CDAA"));
        }
    }





}






