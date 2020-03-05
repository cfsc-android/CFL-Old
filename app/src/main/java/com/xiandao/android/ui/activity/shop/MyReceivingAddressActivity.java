package com.xiandao.android.ui.activity.shop;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiandao.android.R;
import com.xiandao.android.entity.OverallSituationEntity;
import com.xiandao.android.entity.ShopCar1Entity;
import com.xiandao.android.entity.StoreInfo;
import com.xiandao.android.net.ApiHttpResult;
import com.xiandao.android.net.HttpUtils;
import com.xiandao.android.net.NetUtil;
import com.xiandao.android.ui.BaseActivity;
import com.xiandao.android.utils.FileManagement;
import com.xiandao.android.utils.Tools;
import com.xiandao.android.view.EditTextDelView;
import com.xiandao.android.view.alertview.AlertView;
import com.xiandao.android.view.alertview.OnItemClickListener;

import org.xutils.event.annotation.ContentView;
import org.xutils.event.annotation.ViewInject;

import java.util.List;


/**
 * 此类描述的是:我的收货地址列表activity
 *
 * @author TanYong
 *         create at 2017/6/14 17:09
 */
@ContentView(R.layout.activity_my_receiving_address)
public class MyReceivingAddressActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.iv_title_left)
    private ImageView ivTitleLeft;
    @ViewInject(R.id.tv_title_name)
    private TextView tvTitleName;
    @ViewInject(R.id.etd_my_receiving_address)
    private EditTextDelView etd_my_receiving_address;
    @ViewInject(R.id.btn_commit_my_receiving_address)
    private Button btn_commit_my_receiving_address;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        tvTitleName.setText("我的收货地址");
        ivTitleLeft.setOnClickListener(this);
        btn_commit_my_receiving_address.setOnClickListener(this);
        if (!Tools.isEmpty(loginUserEntity.getReceiptAddress())) {
            String receiptAddress = loginUserEntity.getReceiptAddress().trim();
            if (!Tools.isEmpty(receiptAddress)) {
                etd_my_receiving_address.setText(receiptAddress);
                etd_my_receiving_address.setSelection(receiptAddress.length());//将光标移至文字末尾
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_title_left:
                finish();
                break;
            case R.id.btn_commit_my_receiving_address:
                String address = etd_my_receiving_address.getText().toString().trim();
                if (Tools.isEmpty(address)) {
                    Tools.showPrompt("请输入收货地址再提交");
                }
                if (address.length() > 150) {
                    Tools.showPrompt("收货地址过长");
                } else {
                    new AlertView("温馨提示", "确定修改收货地址？",
                            "取消", new String[]{"确认"}, null, MyReceivingAddressActivity.this, AlertView.Style.Alert, new OnItemClickListener() {
                        @Override
                        public void onItemClick(Object o, int position) {
                            if (position == 0) {
                                commitMyReceivingAddress(etd_my_receiving_address.getText().toString());
                            }
                        }
                    }).setCancelable(false).show();
                }
                break;
            default:
                break;
        }

    }

    /**
     * @author TanYong
     * create at 2018/3/1 09:53
     * TODO：提交我的收货地址
     */
    private void commitMyReceivingAddress(final String address) {
        startProgressDialog("");
        ApiHttpResult.commitMyReceivingAddress(this, new String[]{"ownerId", "Address"},
                new Object[]{Tools.getStringValue(FileManagement.getLoginUserEntity().getUserId()), address},
                new HttpUtils.DataCallBack() {
                    @Override
                    public void callBack(Object o) {
                        stopProgressDialog();
                        if (o != null) {
                            OverallSituationEntity resultEntity = (OverallSituationEntity) o;
                            if (resultEntity != null && resultEntity.getResultCode().equals("0")) {
                                Tools.showPrompt(resultEntity.getMsg());
                                loginUserEntity.setReceiptAddress(address);
                                FileManagement.setBaseUser(loginUserEntity);
                                MyReceivingAddressActivity.this.finish();
                            } else {
                                Tools.showPrompt(resultEntity.getMsg());
                            }
                        } else {
                            NetUtil.toNetworkSetting(MyReceivingAddressActivity.this);
                        }
                    }
                });
    }
}
