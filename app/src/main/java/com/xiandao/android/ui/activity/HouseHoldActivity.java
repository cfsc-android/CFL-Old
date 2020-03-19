package com.xiandao.android.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiandao.android.R;
import com.xiandao.android.adapter.smart.HouseholdPagerAdapter;
import com.xiandao.android.ui.BaseActivity;
import com.xiandao.android.ui.fragment.CurrentRoomFragment;
import com.xiandao.android.ui.fragment.OtherRoomFragment;
import com.xiandao.android.view.easyindicator.EasyIndicator;

import org.xutils.event.annotation.ContentView;
import org.xutils.event.annotation.Event;
import org.xutils.event.annotation.ViewInject;

import java.util.ArrayList;

@ContentView(R.layout.activity_house_hold)
public class HouseHoldActivity extends BaseActivity{
    @ViewInject(R.id.tv_title_name)
    private TextView tv_title_name;
    @ViewInject(R.id.tv_title_right)
    private TextView tv_title_right;
    @ViewInject(R.id.iv_title_right)
    private ImageView iv_title_right;

    @ViewInject(R.id.household_ei_tab)
    private EasyIndicator household_ei_tab;
    @ViewInject(R.id.household_vp_tab)
    private ViewPager household_vp_tab;

    private HouseholdPagerAdapter adapter;
    private ArrayList<Fragment> data=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tv_title_name.setText("我的房屋");
        iv_title_right.setVisibility(View.GONE);
        tv_title_right.setText("管理房屋");
        tv_title_right.setVisibility(View.VISIBLE);

        data.add(new CurrentRoomFragment());
        data.add(new OtherRoomFragment());
        adapter=new HouseholdPagerAdapter(getSupportFragmentManager(),data);

        household_ei_tab.setTabTitles(new String[]{"当前房屋","其他房屋","",""});
        household_ei_tab.setViewPager(household_vp_tab, adapter);
        household_vp_tab.setOffscreenPageLimit(1);
        household_vp_tab.setCurrentItem(0);
    }


    @Event({R.id.iv_title_left,R.id.tv_title_right})
    private void onClickEvent(View v){
        switch (v.getId()){
            case R.id.iv_title_left:
                finish();
                break;
            case R.id.tv_title_right:
                openActivity(HouseManageActivity.class);
                break;
        }
    }


}






