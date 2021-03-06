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
import com.xiandao.android.ui.fragment.RoomAllFragment;
import com.xiandao.android.ui.fragment.RoomDelectFragment;

import java.util.ArrayList;
import java.util.List;

public class RoomMemberActivity extends BaseActivity implements View.OnClickListener {

    private TextView title, item_room_all, item_room_delect;
    private ViewPager vp;
    private RoomAllFragment roomAllFragment;
    private RoomDelectFragment roomDelectFragment;
    private List<Fragment> mFragmentList = new ArrayList<Fragment>();
    private FragmentAdapter mFragmentAdapter;

    String[] titles = new String[]{"所有人员", "已删除人员"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除工具栏
        //getSupportActionBar().hide();
        setContentView(R.layout.activity_room_member);
        initViews();

        mFragmentAdapter = new FragmentAdapter(this.getSupportFragmentManager(), mFragmentList);
        vp.setOffscreenPageLimit(2);//ViewPager的缓存为4帧
        vp.setAdapter(mFragmentAdapter);
        vp.setCurrentItem(0);//初始设置ViewPager选中第一帧
        item_room_all.setTextColor(Color.parseColor("#66CDAA"));

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
        title = (TextView) findViewById(R.id.title);
        item_room_all = (TextView) findViewById(R.id.item_room_all);
        item_room_delect = (TextView) findViewById(R.id.item_room_delect);

        item_room_all.setOnClickListener(this);
        item_room_delect.setOnClickListener(this);

        vp = (ViewPager) findViewById(R.id.mainViewPager);
        roomAllFragment = new RoomAllFragment();
        roomDelectFragment = new RoomDelectFragment();

        //给FragmentList添加数据
        mFragmentList.add(roomAllFragment);
        mFragmentList.add(roomDelectFragment);

    }

    /**
     * 点击Text 动态修改ViewPager的内容
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.item_room_all:
                vp.setCurrentItem(0, true);
                break;
            case R.id.item_room_delect:
                vp.setCurrentItem(1, true);
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
            item_room_all.setTextColor(Color.parseColor("#66CDAA"));
            item_room_delect.setTextColor(Color.parseColor("#000000"));
        } else if (position == 1) {
            item_room_all.setTextColor(Color.parseColor("#000000"));
            item_room_delect.setTextColor(Color.parseColor("#66CDAA"));
        }
    }





}






