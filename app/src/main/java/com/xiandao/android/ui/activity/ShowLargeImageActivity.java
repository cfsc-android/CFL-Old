package com.xiandao.android.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jph.takephoto.model.TImage;
import com.xiandao.android.R;
import com.xiandao.android.ui.BaseActivity;
import com.xiandao.android.utils.Constants;

import org.xutils.event.annotation.ContentView;
import org.xutils.event.annotation.Event;
import org.xutils.event.annotation.ViewInject;


@ContentView(R.layout.activity_show_large_image)
public class ShowLargeImageActivity extends BaseActivity {
    @ViewInject(R.id.iv_photo_view)
    private ImageView iv_photo_view;

    private String path="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent != null && getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            path = bundle.getString("path");
        }
        if(!"".equals(path)){
            Glide.with(ShowLargeImageActivity.this).load(Constants.BASEHOST+"/"+path).into(iv_photo_view);
        }else{
            showShortToast("图片加载失败");
            finish();
        }
    }

    @Event({R.id.iv_photo_view})
    private void onClickEvent(View v){
        switch (v.getId()){
            case R.id.iv_photo_view:
                finish();
                break;
        }
    }
}
