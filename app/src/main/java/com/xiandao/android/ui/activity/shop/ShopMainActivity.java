package com.xiandao.android.ui.activity.shop;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiandao.android.R;
import com.xiandao.android.ui.BaseActivity;
import com.xiandao.android.ui.activity.MainActivity;
import com.xiandao.android.utils.FileManagement;

import org.xutils.event.annotation.ContentView;
import org.xutils.event.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * 此类描述的是:商城主界面
 *
 * @author TanYong
 *         create at 2017/4/21 19:27
 */
@ContentView(R.layout.activity_store_main)
public class ShopMainActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.iv_classification)
    private ImageView iv_classification;
    @ViewInject(R.id.tv_classification)
    private TextView tv_classification;
    @ViewInject(R.id.ll_classification)
    private LinearLayout ll_classification;

    @ViewInject(R.id.iv_shopping_car)
    private ImageView iv_shopping_car;
    @ViewInject(R.id.tv_shopping_car)
    private TextView tv_shopping_car;
    @ViewInject(R.id.ll_shopping_car)
    private LinearLayout ll_shopping_car;

    @ViewInject(R.id.iv_my_shop)
    private ImageView iv_my_shop;
    @ViewInject(R.id.tv_my_shop)
    private TextView tv_my_shop;
    @ViewInject(R.id.ll_my_shop)
    private LinearLayout ll_my_shop;

    @ViewInject(R.id.vp_content)
    private com.xiandao.android.view.NoScrollViewPager vpContent;

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
    private ShopHomeFragment shopHomeFragment;
    private ShopCarFragment1 shopCarFragment;
    private ShopMineFragment shopMineFragment;

    public static boolean isForeground = false;

    private String storeId;

    private boolean isFromGoodsDetail = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        storeId = bundle.getString("storeId");
        isFromGoodsDetail = bundle.getBoolean("isFromDetail", false);

        ll_classification.setOnClickListener(this);
        ll_shopping_car.setOnClickListener(this);
        ll_my_shop.setOnClickListener(this);

        //两个不同的fragment
        fragmentList = new ArrayList<Fragment>();
        shopHomeFragment = new ShopHomeFragment();
        shopCarFragment = new ShopCarFragment1();
        shopMineFragment = new ShopMineFragment();

        Bundle bundleClassification = new Bundle();
        bundleClassification.putInt("key", 1);
        bundleClassification.putString("storeId", storeId);

        Bundle bundle_shopping_cart = new Bundle();
        bundle_shopping_cart.putInt("key", 2);

        Bundle bundle_my_shop = new Bundle();
        bundle_my_shop.putInt("key", 3);

        shopHomeFragment.setArguments(bundleClassification);
        shopCarFragment.setArguments(bundle_shopping_cart);
        shopMineFragment.setArguments(bundle_my_shop);

        fragmentList.add(shopHomeFragment);
        fragmentList.add(shopCarFragment);
        fragmentList.add(shopMineFragment);

        //屏幕的宽度
        screenWidth = getResources().getDisplayMetrics().widthPixels;
        vpContent.setOffscreenPageLimit(3);
        vpContent.setAdapter(new MyFrageStatePagerAdapter(getSupportFragmentManager()));
        vpContent.setNoScroll(true);

        if (isFromGoodsDetail) {
            changeView(1);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //点击首页
            case R.id.ll_classification:
                changeView(0);
                break;
            //点击购物车
            case R.id.ll_shopping_car:
                changeView(1);
                break;
            //点击我的先导
            case R.id.ll_my_shop:
                FileManagement.saveIsFromShop(true);
                openActivity(MainActivity.class);
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
            int currentItem = vpContent.getCurrentItem();
            if (currentItem == currenttab) {
                return;
            }
            imageMove(vpContent.getCurrentItem());
            currenttab = vpContent.getCurrentItem();
        }
    }

    //手动设置ViewPager要显示的视图
    private void changeView(int desTab) {
        vpContent.setCurrentItem(desTab, true);
    }

    /**
     * 移动覆盖层
     *
     * @param moveToTab 目标Tab，也就是要移动到的导航选项按钮的位置
     *                  第一个导航按钮对应0，第二个对应1，以此类推
     */
    private void imageMove(int moveToTab) {
        //选中全部
        if (moveToTab == 0) {
            iv_classification.setImageResource(R.drawable.class_selected);
            iv_shopping_car.setImageResource(R.drawable.buycar_profilet);
            iv_my_shop.setImageResource(R.mipmap.home_tab_wd_hui);
            tv_classification.setTextColor(getResources().getColor(R.color.title_backgroud));
            tv_shopping_car.setTextColor(getResources().getColor(R.color.text_color));
            tv_my_shop.setTextColor(getResources().getColor(R.color.text_color));
        } else if (moveToTab == 1) {
            iv_classification.setImageResource(R.drawable.class_profile);
            iv_shopping_car.setImageResource(R.drawable.buycar_selected);
            iv_my_shop.setImageResource(R.mipmap.home_tab_wd_hui);
            tv_classification.setTextColor(getResources().getColor(R.color.text_color));
            tv_shopping_car.setTextColor(getResources().getColor(R.color.title_backgroud));
            tv_my_shop.setTextColor(getResources().getColor(R.color.text_color));
        } else if (moveToTab == 2) {
            iv_classification.setImageResource(R.drawable.class_profile);
            iv_shopping_car.setImageResource(R.drawable.buycar_profilet);
            iv_my_shop.setImageResource(R.mipmap.home_tab_wd_jin);
            tv_classification.setTextColor(getResources().getColor(R.color.text_color));
            tv_shopping_car.setTextColor(getResources().getColor(R.color.text_color));
            tv_my_shop.setTextColor(getResources().getColor(R.color.title_backgroud));
        }

//        int startPosition = 0;
//        int movetoPosition = 0;
//
//        startPosition = currenttab * (screenWidth / 4);
//        movetoPosition = moveToTab * (screenWidth / 4);
//        //平移动画
//        TranslateAnimation translateAnimation = new TranslateAnimation(startPosition, movetoPosition, 0, 0);
//        translateAnimation.setFillAfter(true);
//        translateAnimation.setDuration(200);
//        iv_cursor.startAnimation(translateAnimation);
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
}
