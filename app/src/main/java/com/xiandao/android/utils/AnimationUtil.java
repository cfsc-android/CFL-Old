package com.xiandao.android.utils;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import org.xutils.common.util.LogUtil;

/**
 * Created by Loong on 2020/3/4.
 * Version: 1.0
 * Describe:
 */
public class AnimationUtil {
    public static final int DECELERATE = 2;//减速
    public static final int ACCELERATE = 1;//加速sss
    /**
     * 旋转动画
     *
     * @param paramView
     * @param duration
     *            时间
     */
    public static void startRotateAnimation(View paramView, float fromDegrees, float toDegrees, int duration) {
        float[] arrayOfFloat = new float[2];
        arrayOfFloat[0] = fromDegrees;
        arrayOfFloat[1] = toDegrees;
        ObjectAnimator localObjectAnimator = ObjectAnimator.ofFloat(paramView,
                "rotation", arrayOfFloat);
        localObjectAnimator.setDuration(duration);
        localObjectAnimator.setInterpolator(null);
        localObjectAnimator.setRepeatCount(0);
        localObjectAnimator.start();
    }
    /**
     * 旋转动画
     *
     * @param paramView
     * @param duration
     *            时间
     */
    public static void startRotateAnimation(View paramView, float fromDegrees, float toDegrees, int duration,AnimationListener listener) {
        float[] arrayOfFloat = new float[2];
        arrayOfFloat[0] = fromDegrees;
        arrayOfFloat[1] = toDegrees;
        ObjectAnimator localObjectAnimator = ObjectAnimator.ofFloat(paramView,
                "rotation", arrayOfFloat);
        localObjectAnimator.setDuration(duration);
        localObjectAnimator.setInterpolator(null);
        localObjectAnimator.setRepeatCount(0);
        localObjectAnimator.addListener(listener);
        localObjectAnimator.start();
    }
    /**
     * 开启Alpha 动画
     *
     * @param view
     * @param fromAlpha
     * @param toAlpha
     */
    public static void startAlphaAnima(View view,
                                       float fromAlpha, float toAlpha, int speedType,int duration) {
        if (view == null) {
            return;
        }
        if (view.getAnimation() != null) {
            view.getAnimation().cancel();
            view.clearAnimation();
            return;
        }
        //-------Alpaha--------
        AnimatorSet set = new AnimatorSet();
        set.play(ObjectAnimator.ofFloat(view, "alpha", fromAlpha, toAlpha));
        set.setDuration(duration);
        if (speedType == ACCELERATE)
            set.setInterpolator(new AccelerateInterpolator());//加速
        else if (speedType == DECELERATE)
            set.setInterpolator(new DecelerateInterpolator());//减速
        set.start();

    }
    /**
     * 清理目标View的动画
     *
     * @param paramView
     */
    public static void clearAnimation(View paramView) {
        if (paramView == null)
            return;

        if (paramView.getAnimation() == null) {
            return;
        }

        paramView.getAnimation().cancel();
        paramView.clearAnimation();
    }

    /**
     * 水平移动
     * @param view
     * @param fromX
     * @param toX
     * @param duration
     */
    public static void startTransXAnimation(View view, int fromX,int toX,int duration) {
        ObjectAnimator translationX = ObjectAnimator.ofFloat(view, "translationX", fromX, toX);
        AnimatorSet animatorSet = new AnimatorSet();  //组合动画
        animatorSet.setInterpolator(new DecelerateInterpolator());
        animatorSet.play(translationX); //设置动画
        animatorSet.setDuration(duration);  //设置动画时间
        animatorSet.start();
    }

    /**
     * 竖直移动
     * @param view
     * @param fromY
     * @param toY
     * @param duration
     */
    public static void startTransYAnimation(View view, int fromY,int toY,int duration) {
        ObjectAnimator translationY = ObjectAnimator.ofFloat(view, "translationY", fromY, toY);
        AnimatorSet animatorSet = new AnimatorSet();  //组合动画
        animatorSet.setInterpolator(new DecelerateInterpolator());
        animatorSet.play( translationY); //设置动画
        animatorSet.setDuration(duration);  //设置动画时间
        animatorSet.start();
    }

    /**
     * 竖直移动
     * @param view
     * @param fromY
     * @param toY
     * @param duration
     * @param listener
     */
    public static void startTransYAnimation(View view, int fromY,int toY,int duration,AnimationListener listener ) {
        ObjectAnimator translationY = ObjectAnimator.ofFloat(view, "translationY", fromY, toY);
        AnimatorSet animatorSet = new AnimatorSet();  //组合动画
        animatorSet.setInterpolator(new DecelerateInterpolator());
        animatorSet.play( translationY); //设置动画
        animatorSet.setDuration(duration);  //设置动画时间
        animatorSet.addListener(listener);
        animatorSet.start();
    }

    /**
     * 水平和竖直同时移动
     * @param view
     * @param fromX 起始X的坐标
     * @param toX 终点X的坐标
     * @param fromY 起始Y的坐标
     * @param toY 终点Y的坐标
     * @param duration
     */
    public static void startTransXYAnimation(View view, int fromX,int toX,int fromY,int toY,int duration) {
        ObjectAnimator translationX = ObjectAnimator.ofFloat(view, "translationX", fromX, toX);
        ObjectAnimator translationY = ObjectAnimator.ofFloat(view, "translationY", fromY, toY);
        AnimatorSet animatorSet = new AnimatorSet();  //组合动画
        animatorSet.setInterpolator(new DecelerateInterpolator());
        animatorSet.playTogether(translationX, translationY); //设置动画
        animatorSet.setDuration(duration);  //设置动画时间
        animatorSet.start();
    }


    public static class AnimationListener implements Animator.AnimatorListener{

        @Override
        public void onAnimationStart(Animator animation, boolean isReverse) {
            LogUtil.d("动画启动--"+isReverse);
        }

        @Override
        public void onAnimationEnd(Animator animation, boolean isReverse) {
            LogUtil.d("启画结束--"+isReverse);
        }

        @Override
        public void onAnimationStart(Animator animation) {
            LogUtil.d("动画启动");
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            LogUtil.d("动画结束");
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            LogUtil.d("动画取消");
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
            LogUtil.d("动画重复");
        }
    }

}