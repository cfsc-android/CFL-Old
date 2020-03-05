package com.xiandao.android.ui.activity;

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
import com.xiandao.android.ui.fragment.LifePayedFragment;
import com.xiandao.android.ui.fragment.LifeUnpayFragment;
import com.xiandao.android.utils.Constants;

import org.xutils.event.annotation.ContentView;
import org.xutils.event.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * 此类描述的是:生活缴费activity
 *
 * @author TanYong
 *         create at 2017/6/1 16:04
 */
@ContentView(R.layout.activity_life_pay)
public class LifePayActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.tv_tab_1)
    private TextView tv_tab_life_unpay;
    @ViewInject(R.id.tv_tab_2)
    private TextView tv_tab_life_payed;
    @ViewInject(R.id.vp_content)
    private ViewPager vpContentLifePay;
    @ViewInject(R.id.iv_cursor)
    private ImageView iv_cursor;
    @ViewInject(R.id.iv_title_left)
    private ImageView ivTitleLeft;
    @ViewInject(R.id.iv_title_right)
    private ImageView ivTitleRight;
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
     * 两个Fragment（页面）
     */
    private LifeUnpayFragment lifeUnpayFragment;
    private LifePayedFragment lifePayedFragment;

    //===========筛选条件=========
    private String payTime;//支付时间
    private String lastTerm;//最后期限

    private String payType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        init();
    }

    private void init() {
        ivTitleRight.setVisibility(View.VISIBLE);
        ivTitleLeft.setOnClickListener(this);
        ivTitleRight.setOnClickListener(this);
        tv_tab_life_unpay.setOnClickListener(this);
        tv_tab_life_payed.setOnClickListener(this);

        tvTitleName.setText("支付列表");

        //两个不同的fragment
        fragmentList = new ArrayList<Fragment>();
        lifeUnpayFragment = new LifeUnpayFragment();
        lifePayedFragment = new LifePayedFragment();

        //根据传值不一样区分两个界面的显示
        Bundle bundleUnpay = new Bundle();
        bundleUnpay.putString("payTime", payTime);
        bundleUnpay.putString("lastTerm", lastTerm);

        Bundle bundlePayed = new Bundle();
        bundlePayed.putString("payTime", payTime);
        bundlePayed.putString("lastTerm", lastTerm);

        lifeUnpayFragment.setArguments(bundleUnpay);
        lifePayedFragment.setArguments(bundlePayed);

        fragmentList.add(lifeUnpayFragment);
        fragmentList.add(lifePayedFragment);

        //屏幕的宽度
        screenWidth = getResources().getDisplayMetrics().widthPixels;

        //前面一个参数是屏幕的二分之一，第二个参数是下划线的高度
        FrameLayout.LayoutParams imageParams = new FrameLayout.LayoutParams(screenWidth / 2, 5);
        iv_cursor.setLayoutParams(imageParams);
        vpContentLifePay.setAdapter(new LifePayActivity.MyFrageStatePagerAdapter(getSupportFragmentManager()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_title_left:
                finish();
                break;
            case R.id.iv_title_right:
                Intent intent = new Intent(this, LifePayShaiXuanActivity.class);
                startActivityForResult(intent, Constants.LIFE_PAY_REQUEST_CODE);
                break;
            //点击待处理
            case R.id.tv_tab_1:
                changeView(0);
                break;
            //点击已处理
            case R.id.tv_tab_2:
                changeView(1);
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
            int currentItem = vpContentLifePay.getCurrentItem();
            if (currentItem == currenttab) {
                return;
            }
            imageMove(vpContentLifePay.getCurrentItem());
            currenttab = vpContentLifePay.getCurrentItem();
        }

    }

    //手动设置ViewPager要显示的视图
    private void changeView(int desTab) {
        vpContentLifePay.setCurrentItem(desTab, true);
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

        startPosition = currenttab * (screenWidth / 2);
        movetoPosition = moveToTab * (screenWidth / 2);
        //平移动画
        TranslateAnimation translateAnimation = new TranslateAnimation(startPosition, movetoPosition, 0, 0);
        translateAnimation.setFillAfter(true);
        translateAnimation.setDuration(200);
        iv_cursor.startAnimation(translateAnimation);

        //选中全部
        if (moveToTab == 0) {
            tv_tab_life_unpay.setTextColor(getResources().getColor(R.color.title_backgroud));
            tv_tab_life_payed.setTextColor(getResources().getColor(R.color.tab_no_onclicked_text_color));
        } else if (moveToTab == 1) {
            tv_tab_life_unpay.setTextColor(getResources().getColor(R.color.tab_no_onclicked_text_color));
            tv_tab_life_payed.setTextColor(getResources().getColor(R.color.title_backgroud));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            switch (requestCode) {
                case Constants.LIFE_PAY_REQUEST_CODE:
                    payTime = data.getStringExtra("payTime");
                    lastTerm = data.getStringExtra("lastTerm");
                    init();
                    break;
                default:
                    break;
            }
        }
    }
}
