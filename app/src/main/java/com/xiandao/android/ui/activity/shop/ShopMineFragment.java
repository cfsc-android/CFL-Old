package com.xiandao.android.ui.activity.shop;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiandao.android.AndroidApplication;
import com.xiandao.android.R;
import com.xiandao.android.entity.LoginUserEntity;
import com.xiandao.android.entity.RoomInfoEntity;
import com.xiandao.android.ui.BaseLazyFragment;
import com.xiandao.android.ui.activity.LoginActivity;
import com.xiandao.android.ui.activity.MyComplainActivity;
import com.xiandao.android.ui.activity.MyRepairsActivity;
import com.xiandao.android.ui.activity.SetMineActivity;
import com.xiandao.android.utils.FileManagement;
import com.xiandao.android.utils.PhotoUtils;
import com.xiandao.android.utils.Tools;

import java.util.ArrayList;

import cn.jpush.android.api.JPushInterface;

/**
 * 此类描述的是:购物我的fragment
 *
 * @author TanYong
 *         create at 2017/5/18 9:53
 */
public class ShopMineFragment extends BaseLazyFragment implements View.OnClickListener {

    private ImageView ivTitleLeft;
    private TextView tvTitleName;

    @Override
    protected void injectView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_shop_mine, null);
        setContentView(view);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_mine_head:
                openActivityResult(SetMineActivity.class);
                break;
            default:
                break;
        }
    }

}
