package com.xiandao.android.ui.activity;

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
import com.xiandao.android.ui.fragment.RepairsConfirmCostFragment;
import com.xiandao.android.ui.fragment.RepairsPlanFragment;

import org.xutils.event.annotation.ContentView;
import org.xutils.event.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * 此类描述的是:报修详情--确认费用activity
 *
 * @author TanYong
 *         create at 2017/5/9 16:50
 */
@ContentView(R.layout.activity_complain_detail)
public class RepairsConfirmCostActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.tv_tab_detail)
    private TextView tvTabDetail;
    @ViewInject(R.id.tv_tab_jindu)
    private TextView tvTabJindu;
    @ViewInject(R.id.vp_content)
    private ViewPager vpContent;
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

    private String repairsId;
    private String taskId;
    private int repairsStatus;//工单状态：0、确认费用，1、确认是评价还是支付，2、支付，3、评价

    /**
     * 两个Fragment（页面）
     */
    private RepairsConfirmCostFragment repairsConfirmCostFragment;
    private RepairsPlanFragment repairsPlanFragment;

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
        ivTitleLeft.setOnClickListener(this);
        tvTabDetail.setOnClickListener(this);
        tvTabJindu.setOnClickListener(this);

        tvTitleName.setText("报修详情");

        //接受分别从商品详情页跳转过来的值和从个人中心点击我的评论跳转过来的值
        repairsId = getIntent().getExtras().getString("repairsId");
        taskId = getIntent().getExtras().getString("taskId");
        repairsStatus = getIntent().getExtras().getInt("repairsStatus");
        //两个不同的fragment
        fragmentList = new ArrayList<Fragment>();
        repairsConfirmCostFragment = new RepairsConfirmCostFragment();
        repairsPlanFragment = new RepairsPlanFragment();

        //根据传值不一样区分两个界面的显示
        Bundle bundleDetail = new Bundle();
        bundleDetail.putString("repairsId", repairsId);
        bundleDetail.putString("taskId", taskId);
        bundleDetail.putInt("repairsStatus", repairsStatus);

        Bundle bundleJindu = new Bundle();
        bundleJindu.putString("repairsId", repairsId);

        repairsConfirmCostFragment.setArguments(bundleDetail);
        repairsPlanFragment.setArguments(bundleJindu);

        fragmentList.add(repairsConfirmCostFragment);
        fragmentList.add(repairsPlanFragment);

        //屏幕的宽度
        screenWidth = getResources().getDisplayMetrics().widthPixels;

        //前面一个参数是屏幕的二分之一，第二个参数是下划线的高度
        FrameLayout.LayoutParams imageParams = new FrameLayout.LayoutParams(screenWidth / 2, 5);
        iv_cursor.setLayoutParams(imageParams);
        vpContent.setAdapter(new RepairsConfirmCostActivity.MyFrageStatePagerAdapter(getSupportFragmentManager()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_title_left:
                finish();
                break;
            //点击详情
            case R.id.tv_tab_detail:
                changeView(0);
                break;
            //点击进度
            case R.id.tv_tab_jindu:
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
            tvTabDetail.setTextColor(getResources().getColor(R.color.title_backgroud));
            tvTabJindu.setTextColor(getResources().getColor(R.color.tab_no_onclicked_text_color));
        } else if (moveToTab == 1) {
            tvTabDetail.setTextColor(getResources().getColor(R.color.tab_no_onclicked_text_color));
            tvTabJindu.setTextColor(getResources().getColor(R.color.title_backgroud));
        }
    }
}
