package com.xiandao.android.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.photoview.PhotoView;
import com.xiandao.android.R;
import com.xiandao.android.ui.BaseActivity;
import com.xiandao.android.utils.Utils;

import org.xutils.event.annotation.ContentView;
import org.xutils.event.annotation.ViewInject;

import java.util.ArrayList;
import java.util.Collections;

/**
 * 此类描述的是:查看添加的放大图片
 *
 * @author TanYong
 *         create at 2017/6/17 22:26
 */
@ContentView(R.layout.photo_viewpager_view)
public class ShowBitmapPhotoActivity extends BaseActivity {
    @ViewInject(R.id.photo_viewpager)
    private ViewPager viewPager;
    private int position = 0;
    private ArrayList<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        Intent intent = getIntent();
        if (intent != null && getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            position = bundle.getInt("position");
            list = bundle.getStringArrayList("list");
        }
        Collections.reverse(list);
        if (list == null) return;
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(new PhotoViewPagerAdapter());
        viewPager.setCurrentItem(position, true);
    }

    class PhotoViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position,
                                Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            PhotoView photoview = new PhotoView(container.getContext());
            photoview.setImageBitmap(Utils.getImage(list.get(position)));
            photoview.setOnDoubleTapListener(new GestureDetector.OnDoubleTapListener() {
                @Override
                public boolean onSingleTapConfirmed(MotionEvent e) {
                    finish();
                    return false;
                }

                @Override
                public boolean onDoubleTap(MotionEvent e) {

                    return false;
                }

                @Override
                public boolean onDoubleTapEvent(MotionEvent e) {
                    return false;
                }
            });
            container.addView(photoview, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            return photoview;
        }
    }

    private class MySimpleGesture extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            finish();
            return true;
        }
    }
}
