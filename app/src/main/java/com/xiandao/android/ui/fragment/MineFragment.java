package com.xiandao.android.ui.fragment;

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
import com.xiandao.android.ui.activity.shop.MyOrderActivity;
import com.xiandao.android.ui.activity.shop.MyReceivingAddressActivity;
import com.xiandao.android.utils.FileManagement;
import com.xiandao.android.utils.PhotoUtils;
import com.xiandao.android.utils.Tools;
import com.xiandao.android.view.alertview.AlertView;
import com.xiandao.android.view.alertview.OnItemClickListener;

import java.util.ArrayList;

import cn.jpush.android.api.JPushInterface;

/**
 * 此类描述的是:我的fragment
 *
 * @author TanYong
 *         create at 2017/5/18 9:53
 */
public class MineFragment extends BaseLazyFragment implements View.OnClickListener {

    private ImageView ivTitleLeft;
    private TextView tvTitleName;
    /**
     * 我的头像
     */
    private ImageView iv_mine_head;
    /**
     * 我的工单
     */
    private TextView tv_mine_gongdan;
    /**
     * 我的投诉
     */
    private TextView tv_mine_tousu;
    /**
     * 我的账单
     */
    private TextView tv_mine_zhangdan;
    /**
     * 通知与公告
     */
    private TextView tv_mine_gonggaoyutongzhi;
    /**
     * 我的订单
     */
    private TextView tv_my_order;
    /**
     * 我的收货地址
     */
    private TextView tv_my_shop_address;
    /**
     * 我的钱包
     */
    private TextView tv_mine_qianbao;
    private TextView tv_logout;

    private TextView tv_name;//姓名
    private TextView tv_at_property;//所在物业
    private TextView tv_address;//地址

    private LoginUserEntity userEntity;

    private ArrayList<RoomInfoEntity> roomInfoEntity;

    @Override
    protected void injectView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_mine, null);
        setContentView(view);
        userEntity = FileManagement.getLoginUserEntity();
        roomInfoEntity = FileManagement.getRoomInfo();
        if (userEntity != null) {
            initView(view);
            init();
        }
    }

    private void initView(View view) {
        ivTitleLeft = (ImageView) view.findViewById(R.id.iv_title_left);
        ivTitleLeft.setVisibility(View.GONE);
        tvTitleName = (TextView) view.findViewById(R.id.tv_title_name);

        tv_name = (TextView) view.findViewById(R.id.tv_name);
        tv_at_property = (TextView) view.findViewById(R.id.tv_at_property);
        tv_address = (TextView) view.findViewById(R.id.tv_address);

        tv_name.setText(userEntity.getNickName());
        tv_at_property.setText("智慧物业");
        if (roomInfoEntity != null && roomInfoEntity.size() > 0) {
            tv_address.setText(Tools.getStringValue(roomInfoEntity.get(0).getAddress()));
        }
        iv_mine_head = (ImageView) view.findViewById(R.id.iv_mine_head);
        tv_mine_gongdan = (TextView) view.findViewById(R.id.tv_mine_gongdan);
        tv_mine_tousu = (TextView) view.findViewById(R.id.tv_mine_tousu);
        tv_mine_zhangdan = (TextView) view.findViewById(R.id.tv_mine_zhangdan);
        tv_mine_gonggaoyutongzhi = (TextView) view.findViewById(R.id.tv_mine_gonggaoyutongzhi);
        tv_mine_qianbao = (TextView) view.findViewById(R.id.tv_mine_qianbao);
        tv_logout = (TextView) view.findViewById(R.id.tv_logout);
        tv_my_order = (TextView) view.findViewById(R.id.tv_my_order);
        tv_my_shop_address = (TextView) view.findViewById(R.id.tv_my_shop_address);

        iv_mine_head.setOnClickListener(this);
        tv_mine_gongdan.setOnClickListener(this);
        tv_mine_tousu.setOnClickListener(this);
        tv_mine_zhangdan.setOnClickListener(this);
        tv_mine_gonggaoyutongzhi.setOnClickListener(this);
        tv_mine_qianbao.setOnClickListener(this);
        tv_logout.setOnClickListener(this);
        tv_my_order.setOnClickListener(this);
        tv_my_shop_address.setOnClickListener(this);
    }

    private void init() {
        tvTitleName.setText("我的");
        PhotoUtils.loadRoundImage(userEntity.getHeadImageUrl(), iv_mine_head, 200, 1);
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (99 == resultCode) {
//            userEntity = FileManagement.getLoginUserEntity();
//            tv_name.setText(userEntity.getNickName());
//            PhotoUtils.loadRoundImage(userEntity.getHeadImageUrl(), iv_mine_head, 200, 1);
//        }
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_mine_head:
//                openActivityResult(SetMineActivity.class);
                break;
            case R.id.tv_mine_gongdan:
                openActivity(MyRepairsActivity.class);
                break;
            case R.id.tv_mine_tousu:
                openActivity(MyComplainActivity.class);
                break;
            case R.id.tv_mine_zhangdan:
                break;
            case R.id.tv_mine_gonggaoyutongzhi:
                break;
            case R.id.tv_mine_qianbao:
                break;
            case R.id.tv_logout:
                new AlertView("温馨提示", "确定退出登录？",
                        "取消", new String[]{"确认"}, null, getActivity(), AlertView.Style.Alert, new OnItemClickListener() {
                    @Override
                    public void onItemClick(Object o, int position) {
                        if (position == 0) {
                            logout();
                        }
                    }
                }).setCancelable(false).show();
                break;
            case R.id.tv_my_order:
                openActivity(MyOrderActivity.class);
                break;
            case R.id.tv_my_shop_address:
                openActivity(MyReceivingAddressActivity.class);
                break;
            default:
                break;
        }
    }

    /**
     * @author TanYong
     * create at 2017/4/21 15:57
     * TODO：退出登录
     */
    private void logout() {
        FileManagement.saveTokenInfo("");
        FileManagement.saveJpushAlias("");
        FileManagement.saveJpushTags("");
        JPushInterface.setAliasAndTags(getApplicationContext(), "", null, null);
        openActivity(LoginActivity.class);
        AndroidApplication.getInstance().exitApp();
    }

}
