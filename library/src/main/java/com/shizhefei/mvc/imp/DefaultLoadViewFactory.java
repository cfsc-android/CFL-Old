/*
Copyright 2015 shizhefei（LuckyJayce）

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package com.shizhefei.mvc.imp;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.devspark.progressfragment.ProgressWheel;
import com.shizhefei.mvc.ILoadViewFactory;
import com.shizhefei.vary.VaryViewHelper;

import org.xutils.R;

public class DefaultLoadViewFactory implements ILoadViewFactory {

    @Override
    public ILoadMoreView madeLoadMoreView() {
        return new LoadMoreHelper();
    }

    @Override
    public ILoadView madeLoadView() {
        return new LoadViewHelper();
    }

    private class LoadMoreHelper implements ILoadMoreView {

        protected TextView footView;

        protected OnClickListener onClickRefreshListener;

        @Override
        public void init(FootViewAdder footViewHolder, OnClickListener onClickRefreshListener) {
            footView = (TextView) footViewHolder.addFootView(R.layout.layout_listview_foot);
            this.onClickRefreshListener = onClickRefreshListener;
            showNormal();
        }

        @Override
        public void showNormal() {
            footView.setText("点击加载更多");
            footView.setOnClickListener(onClickRefreshListener);
        }

        @Override
        public void showLoading() {
            footView.setText("正在加载中..");
            footView.setOnClickListener(null);
        }

        @Override
        public void showFail(Exception exception) {
            footView.setText("加载失败，点击重新加载");
            footView.setOnClickListener(onClickRefreshListener);
        }

        @Override
        public void showNomore() {
            footView.setText("已经加载完毕");
            footView.setOnClickListener(null);
        }

    }

    private class LoadViewHelper implements ILoadView {
        private VaryViewHelper helper;
        private OnClickListener onClickRefreshListener;
        private Context context;
        ProgressWheel progressWheel;

        @Override
        public void init(View switchView, OnClickListener onClickRefreshListener) {
            this.context = switchView.getContext().getApplicationContext();
            this.onClickRefreshListener = onClickRefreshListener;
            helper = new VaryViewHelper(switchView);
        }

        @Override
        public void restore() {
            helper.restoreView();
        }

        @Override
        public void showLoading() {
            View layout = helper.inflate(R.layout.load_ing);
            progressWheel = (ProgressWheel) layout.findViewById(R.id.progressBar1);
            TextView textView = (TextView) layout.findViewById(R.id.textView1);
            textView.setText("加载中...");
            styleRandom(progressWheel, context);
            progressWheel.startSpinning();
            helper.showLayout(layout);
        }

        @Override
        public void tipFail(Exception exception) {
            Toast.makeText(context, "网络加载失败", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void showFail(Exception exception) {
            if (progressWheel != null) progressWheel.stopSpinning();
            View layout = helper.inflate(R.layout.load_error);
            TextView textView = (TextView) layout.findViewById(R.id.textView1);
            textView.setText("网络加载失败");
            Button button = (Button) layout.findViewById(R.id.button1);
            button.setText("重试");
            button.setOnClickListener(onClickRefreshListener);
            helper.showLayout(layout);
        }

        @Override
        public void showEmpty() {
            if (progressWheel != null) progressWheel.stopSpinning();
            View layout = helper.inflate(R.layout.load_empty);
            TextView textView = (TextView) layout.findViewById(R.id.textView1);
            textView.setText("暂无数据");
            Button button = (Button) layout.findViewById(R.id.button1);
            button.setText("重试");
            button.setOnClickListener(onClickRefreshListener);
            helper.showLayout(layout);
        }

    }


    public static void styleRandom(ProgressWheel wheel, Context ctx) {
        wheel.setRimShader(null);
        wheel.setRimColor(0xFFFFFFFF);
        wheel.setCircleColor(0x00000000);
        wheel.setBarColor(0xFF8a2d98);
        wheel.setContourColor(0xFFFFFFFF);
        wheel.setBarWidth(pxFromDp(ctx, 1));
        wheel.setBarLength(pxFromDp(ctx, 100));
        wheel.setSpinSpeed(4f);
        wheel.setDelayMillis(3);
        wheel.setText("加载中...");
        wheel.setTextColor(ctx.getResources().getColor(R.color.exist_login_color));
    }

    public static int pxFromDp(final Context context, final float dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }

}
