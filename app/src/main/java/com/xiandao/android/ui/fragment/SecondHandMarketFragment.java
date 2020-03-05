package com.xiandao.android.ui.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.xiandao.android.R;
import com.xiandao.android.ui.BaseLazyFragment;
import com.xiandao.android.ui.activity.SecondHandDetailActivity;

/**
 * Created by zengx on 2019/5/29.
 * Describe:
 */
public class SecondHandMarketFragment extends BaseLazyFragment implements View.OnClickListener  {
    private ImageView iv_main;

    @Override
    protected void injectView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragmnet_second_hand_market, null);
        setContentView(view);
        initView(view);
    }

    private void initView(View view){
        iv_main= (ImageView) view.findViewById(R.id.iv_second_hand_main_img);
        iv_main.setImageResource(R.drawable.secondhand_index);
        iv_main.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_second_hand_main_img:
                openActivity(SecondHandDetailActivity.class);
                break;
        }
    }
}
