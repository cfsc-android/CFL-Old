package com.xiandao.android.ui.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.allinpay.appayassistex.APPayAssistEx;
import com.xiandao.android.R;
import com.xiandao.android.entity.CreatePayInfoResultEntity;
import com.xiandao.android.entity.LifePayDataEntity;
import com.xiandao.android.entity.LifePayEntity;
import com.xiandao.android.entity.PayInfoEntity;
import com.xiandao.android.net.ApiHttpResult;
import com.xiandao.android.net.HttpUtils;
import com.xiandao.android.net.NetUtil;
import com.xiandao.android.ui.BaseActivity;
import com.xiandao.android.ui.activity.shop.ShopPayActivity;
import com.xiandao.android.utils.FileManagement;
import com.xiandao.android.utils.PaaCreator;
import com.xiandao.android.utils.Tools;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.event.annotation.ContentView;
import org.xutils.event.annotation.ViewInject;

import java.util.ArrayList;

/**
 * 此类描述的是:生活缴费详情activity
 *
 * @author TanYong
 * create at 2017/5/9 16:50
 */
@ContentView(R.layout.activity_life_pay_detail)
public class LifePayDetailActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.iv_title_left)
    private ImageView ivTitleLeft;
    @ViewInject(R.id.tv_title_name)
    private TextView tvTitleName;

    @ViewInject(R.id.tv_payment_detail_money)
    private TextView tv_payment_detail_money;//缴费金额
    @ViewInject(R.id.tv_payment_detail_address)
    private TextView tv_payment_detail_address;//缴费房屋
    @ViewInject(R.id.tv_payment_detail_from)
    private TextView tv_payment_detail_from;//缴费来源
    @ViewInject(R.id.tv_payment_detail_type)
    private TextView tv_payment_detail_type;//缴费类型
    @ViewInject(R.id.tv_payment_detail_date)
    private TextView tv_payment_detail_date;//缴费账期
    @ViewInject(R.id.tv_payment_detail_term)
    private TextView tv_payment_detail_term;//缴费期限
    @ViewInject(R.id.btn_pay)
    private Button btn_pay;//立即支付

    private LifePayDataEntity lifePayDataEntity;

    private boolean isPayed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        ivTitleLeft.setOnClickListener(this);
        btn_pay.setOnClickListener(this);
        tvTitleName.setText("支付详情");

        Intent intent = getIntent();
        if (null != intent) {
            Bundle bundle = intent.getExtras();
            if (null != bundle) {
                lifePayDataEntity = (LifePayDataEntity) bundle.getSerializable("lifePay");
                tv_payment_detail_money.setText(Tools.getStringValue(lifePayDataEntity.getFee()));
                tv_payment_detail_address.setText(Tools.getStringValue(lifePayDataEntity.getAdds()));
                tv_payment_detail_from.setText("物业费");
                isPayed = bundle.getBoolean("isPayed", false);

                if (!Tools.isEmpty(lifePayDataEntity.getAdvancetype())) {
                    if ("0".equals(lifePayDataEntity.getAdvancetype())) {
                        tv_payment_detail_type.setText("预缴费");
                    } else if ("1".equals(lifePayDataEntity.getAdvancetype())) {
                        tv_payment_detail_type.setText("补缴费");
                    }
                }

                if (isPayed) {
                    btn_pay.setVisibility(View.GONE);
                }

                tv_payment_detail_date.setText(Tools.getStringValue(lifePayDataEntity.getTerm()));
                tv_payment_detail_term.setText(Tools.getStringValue(lifePayDataEntity.getShouldpaydate()));
            }
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_title_left:
                finish();
                break;
            case R.id.btn_pay:
                Bundle bundle = new Bundle();
                bundle.putInt("fromWhere", 2);//从哪个界面跳转到支付界面：1、商城
                bundle.putString("orderId", lifePayDataEntity.getId());
                bundle.putString("price", lifePayDataEntity.getFee());
                openActivity(ShopPayActivity.class, bundle);
                this.finish();
//                getOrderData();
                break;
            default:
                break;
        }
    }

    private void getOrderData() {
        ApiHttpResult.createPayInfo(this, new String[]{
                        "operationuser", "paylistids", "roomid", "payamount", "paytype", "iphonetype"},
                new Object[]{
                        loginUserEntity.getUserId(), lifePayDataEntity.getId(), lifePayDataEntity.getRoomid(),
                        lifePayDataEntity.getFee(), "通联支付", "0"},
                new HttpUtils.DataCallBack<Object>() {
                    @Override
                    public void callBack(Object o) {
                        stopProgressDialog();
                        if (o != null) {
                            CreatePayInfoResultEntity data = (CreatePayInfoResultEntity) o;
                            if (data != null) {
                                if (data.getResultCode().equals("0")) {
                                    PayInfoEntity entity = data.getData();
                                    String orderNo = entity.getPayid();
                                    String amount = (int) (Double.valueOf(lifePayDataEntity.getFee()) * 100) + "";
                                    String orderDateTime = entity.getCreatedatefm();
                                    String userid = entity.getUserid();
                                    String resultUrl = "http://www.baidu.com";
                                    String productName = "物业费";
                                    JSONObject payData = PaaCreator.randomPaa(orderNo, amount, orderDateTime, resultUrl, productName, "008340142150000", userid);
                                    Log.e("payData", payData.toString());
                                    APPayAssistEx.startPay(LifePayDetailActivity.this, payData.toString(), APPayAssistEx.MODE_DEBUG);
                                }
                            } else {
                                Tools.showPrompt(data.getMsg());
                            }
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (APPayAssistEx.REQUESTCODE == requestCode) {
            if (null != data) {
                String payRes = null;
                String payAmount = null;
                String payTime = null;
                String payOrderId = null;
                try {
                    JSONObject resultJson = new JSONObject(data.getExtras().getString("result"));
                    payRes = resultJson.getString(APPayAssistEx.KEY_PAY_RES);
                    payAmount = resultJson.getString("payAmount");
                    payTime = resultJson.getString("payTime");
                    payOrderId = resultJson.getString("payOrderId");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (null != payRes && payRes.equals(APPayAssistEx.RES_SUCCESS)) {
                    showAppayRes("支付成功！");
                } else {
                    showAppayRes("支付失败！");
                }
                Log.e("payResult", "payRes: " + payRes + "  payAmount: " + payAmount + "  payTime: " + payTime + "  payOrderId: " + payOrderId);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void showAppayRes(String res) {
        new AlertDialog.Builder(this)
                .setMessage(res)
                .setPositiveButton("确定", null)
                .show();
    }
}
