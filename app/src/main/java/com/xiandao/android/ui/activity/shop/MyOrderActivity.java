package com.xiandao.android.ui.activity.shop;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiandao.android.R;
import com.xiandao.android.ui.BaseActivity;

import org.xutils.event.annotation.ContentView;
import org.xutils.event.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * 此类描述的是:我的订单主界面
 *
 * @author TanYong
 *         create at 2017/4/21 19:27
 */
@ContentView(R.layout.activity_my_order)
public class MyOrderActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.tv_tab_1)
    private TextView tv_tab_1;
    @ViewInject(R.id.tv_tab_2)
    private TextView tv_tab_2;
    @ViewInject(R.id.tv_tab_3)
    private TextView tv_tab_3;
    @ViewInject(R.id.tv_tab_4)
    private TextView tv_tab_4;
    @ViewInject(R.id.vp_content)
    private ViewPager vp_content;
    @ViewInject(R.id.iv_cursor)
    private ImageView iv_cursor;
    @ViewInject(R.id.iv_title_left)
    private ImageView ivTitleLeft;
    @ViewInject(R.id.tv_title_name)
    private TextView tvTitleName;

    /**
     * 页面集合
     */
    private List<Fragment> fragmentList;

    //屏幕宽度
    int screenWidth;
    //当前选中的项
    int currenttab;

    /**
     * 四个Fragment（页面）
     * 订单状态（0已下单/1已支付/2已取消/3已完成）',
     */
    private MyOrderFragment myOrderFragment1;
    private MyOrderFragment myOrderFragment2;
    private MyOrderFragment myOrderFragment3;
    private MyOrderFragment myOrderFragment4;

    private int isFromWhere = 0;//1、从支付界面跳转过来

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        ivTitleLeft.setOnClickListener(this);
        tvTitleName.setText("我的订单");

        tv_tab_1.setOnClickListener(this);
        tv_tab_2.setOnClickListener(this);
        tv_tab_3.setOnClickListener(this);
        tv_tab_4.setOnClickListener(this);

        fragmentList = new ArrayList<Fragment>();
        myOrderFragment1 = new MyOrderFragment();
        myOrderFragment2 = new MyOrderFragment();
        myOrderFragment3 = new MyOrderFragment();
        myOrderFragment4 = new MyOrderFragment();

        Bundle bundle1 = new Bundle();
        bundle1.putInt("ident", 0);

        Bundle bundle2 = new Bundle();
        bundle2.putInt("ident", 1);

        Bundle bundle3 = new Bundle();
        bundle3.putInt("ident", 2);

        Bundle bundle4 = new Bundle();
        bundle4.putInt("ident", 3);

        myOrderFragment1.setArguments(bundle1);
        myOrderFragment2.setArguments(bundle2);
        myOrderFragment3.setArguments(bundle3);
        myOrderFragment4.setArguments(bundle4);

        fragmentList.add(myOrderFragment1);
        fragmentList.add(myOrderFragment2);
        fragmentList.add(myOrderFragment3);
        fragmentList.add(myOrderFragment4);

        //屏幕的宽度
        screenWidth = getResources().getDisplayMetrics().widthPixels;

        //前面一个参数是屏幕的四分之一，第二个参数是下划线的高度
        FrameLayout.LayoutParams imageParams = new FrameLayout.LayoutParams(screenWidth / 4, 5);
        iv_cursor.setLayoutParams(imageParams);
        //ViewPager切换界面背景色改为白色
        vp_content.setBackgroundColor(getResources().getColor(R.color.white));
        vp_content.setAdapter(new MyFrageStatePagerAdapter(getSupportFragmentManager()));
        vp_content.setOffscreenPageLimit(4);

        Intent intent = getIntent();
        if (null != intent) {
            Bundle bundle = intent.getExtras();
            if (null != bundle) {
                isFromWhere = bundle.getInt("isFromWhere");
                changeView(1);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_tab_1:
                changeView(0);
                break;
            case R.id.tv_tab_2:
                changeView(1);
                break;
            case R.id.tv_tab_3:
                changeView(2);
                break;
            case R.id.tv_tab_4:
                changeView(3);
                break;
            case R.id.iv_title_left:
                this.finish();
                break;
            default:
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
            //获取当前的视图是位于ViewGroup的第几个位置，用来更新对应的覆盖层所在的位置
            int currentItem = vp_content.getCurrentItem();
            if (currentItem == currenttab) {
                return;
            }
            imageMove(vp_content.getCurrentItem());
            currenttab = vp_content.getCurrentItem();
        }

    }

    //手动设置ViewPager要显示的视图
    private void changeView(int desTab) {
        vp_content.setCurrentItem(desTab, true);
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

        startPosition = currenttab * (screenWidth / 4);
        movetoPosition = moveToTab * (screenWidth / 4);
        //平移动画
        TranslateAnimation translateAnimation = new TranslateAnimation(startPosition, movetoPosition, 0, 0);
        translateAnimation.setFillAfter(true);
        translateAnimation.setDuration(200);
        iv_cursor.startAnimation(translateAnimation);

        switch (moveToTab) {
            case 0:
                tv_tab_1.setTextColor(getResources().getColor(R.color.title_backgroud));
                tv_tab_2.setTextColor(getResources().getColor(R.color.tab_no_onclicked_text_color));
                tv_tab_3.setTextColor(getResources().getColor(R.color.tab_no_onclicked_text_color));
                tv_tab_4.setTextColor(getResources().getColor(R.color.tab_no_onclicked_text_color));
                break;
            case 1:
                tv_tab_1.setTextColor(getResources().getColor(R.color.tab_no_onclicked_text_color));
                tv_tab_2.setTextColor(getResources().getColor(R.color.title_backgroud));
                tv_tab_3.setTextColor(getResources().getColor(R.color.tab_no_onclicked_text_color));
                tv_tab_4.setTextColor(getResources().getColor(R.color.tab_no_onclicked_text_color));
                break;
            case 2:
                tv_tab_1.setTextColor(getResources().getColor(R.color.tab_no_onclicked_text_color));
                tv_tab_2.setTextColor(getResources().getColor(R.color.tab_no_onclicked_text_color));
                tv_tab_3.setTextColor(getResources().getColor(R.color.title_backgroud));
                tv_tab_4.setTextColor(getResources().getColor(R.color.tab_no_onclicked_text_color));
                break;
            case 3:
                tv_tab_1.setTextColor(getResources().getColor(R.color.tab_no_onclicked_text_color));
                tv_tab_2.setTextColor(getResources().getColor(R.color.tab_no_onclicked_text_color));
                tv_tab_3.setTextColor(getResources().getColor(R.color.tab_no_onclicked_text_color));
                tv_tab_4.setTextColor(getResources().getColor(R.color.title_backgroud));
                break;
            default:
                break;
        }
    }
}
