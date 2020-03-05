package com.xiandao.android.ui.activity.shop;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.xiandao.android.R;
import com.xiandao.android.entity.EventEntity;
import com.xiandao.android.entity.GetWXPapParamsResultEntity;
import com.xiandao.android.entity.OverallSituationEntity;
import com.xiandao.android.entity.WeiXinPayEntity;
import com.xiandao.android.net.ApiHttpResult;
import com.xiandao.android.net.HttpUtils;
import com.xiandao.android.net.NetUtil;
import com.xiandao.android.ui.BaseActivity;
import com.xiandao.android.utils.Constants;
import com.xiandao.android.utils.Tools;

import com.xiandao.android.view.alertview.AlertView;
import com.xiandao.android.view.alertview.OnItemClickListener;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.event.annotation.ContentView;
import org.xutils.event.annotation.ViewInject;


/**
 * 此类描述的是:商城支付界面
 *
 * @author TanYong
 * create at 2017/5/18 9:53
 */
@ContentView(R.layout.activity_shop_pay)
public class ShopPayActivity extends BaseActivity implements View.OnClickListener {
    @ViewInject(R.id.iv_title_left)
    private ImageView ivTitleLeft;
    @ViewInject(R.id.tv_title_name)
    private TextView ivTitleName;
    @ViewInject(R.id.ll_weixin_pay)
    private LinearLayout ll_weixin_pay;
    @ViewInject(R.id.iv_weixin)
    private ImageView iv_weixin;
    @ViewInject(R.id.ll_zhifubao_pay)
    private LinearLayout ll_zhifubao_pay;
    @ViewInject(R.id.iv_zhifubao)
    private ImageView iv_zhifubao;
    @ViewInject(R.id.btn_pay_ok)
    private Button btn_pay_ok;

    private String orderId;
    private String price;

    //1、微信支付；2、支付宝支付。默认微信支付
    private int selectPayType = 1;

    // IWXAPI 是第三方app和微信通信的openapi接口
    private IWXAPI api;
    //微信支付测试接口，正式开发必须使用公司自己的接口
    private String url;

    private int fromWhere = 0;//从哪个界面跳转过来的，1、商城；2、生活缴费--物业费

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void init() {
        ivTitleName.setText("支付");
        ivTitleLeft.setOnClickListener(this);
        ll_weixin_pay.setOnClickListener(this);
        ll_zhifubao_pay.setOnClickListener(this);
        btn_pay_ok.setOnClickListener(this);

        Intent intent = getIntent();
        if (null != intent) {
            Bundle bundle = intent.getExtras();
            if (null != bundle) {
                fromWhere = bundle.getInt("fromWhere");
                orderId = bundle.getString("orderId");
                price = bundle.getString("price");
                btn_pay_ok.setText("确认支付：" + price + "元");
            }
        }

        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        // 将该app注册到微信
        api.registerApp(Constants.APP_ID);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_title_left:
                finish();
                break;
            case R.id.ll_weixin_pay:
                iv_weixin.setImageResource(R.mipmap.xzlxr_btn_p);
                iv_zhifubao.setImageResource(R.mipmap.xzlxr_btn_n);
                selectPayType = 1;
                break;
            case R.id.ll_zhifubao_pay:
                iv_weixin.setImageResource(R.mipmap.xzlxr_btn_n);
                iv_zhifubao.setImageResource(R.mipmap.xzlxr_btn_p);
                selectPayType = 2;
                break;
            case R.id.btn_pay_ok:
//                new AlertView("温馨提示", "确认支付？",
//                        "取消", new String[]{"确认"}, null, ShopPayActivity.this, AlertView.Style.Alert, new OnItemClickListener() {
//                    @Override
//                    public void onItemClick(Object o, int position) {
//                        if (selectPayType == 1) {
//                            getWXPayParams();
//                        } else {
//
//                        }
//                    }
//                }).setCancelable(false).show();
                if (selectPayType == 1) {
                    checkWeixinApp();
                } else {

                }
                break;
            default:
                break;
        }
    }

    /**
     *
     */
    private void getWXPayParams() {
        int wxPrice = (int) (Double.parseDouble(price) * 100);
        String type = "1";
        if (2 == fromWhere) {
            type = "0";
        }

        startProgressDialog("");
        /**
         *  * @param totalFee	总金额
         * @param spbillCreateIp    设备id
         * @param payid    订单id（缴费列表中的记录id）
         * @param ownerid    业主id
         * @param phonetype    设备类型(IOS,Android)
         * @param type    '支付项目类别（0：物业费，1是商城支付）',
         */
        ApiHttpResult.getWXPayParams(this, new String[]{"totalFee", "spbillCreateIp", "payid",
                        "ownerid", "phonetype", "type"},
                new Object[]{wxPrice, NetUtil.getLocalIpAddress(), orderId, loginUserEntity
                        .getUserId(), "Android", type},
                new HttpUtils.DataCallBack() {
                    @Override
                    public void callBack(Object o) {
                        stopProgressDialog();
                        if (o != null) {
                            GetWXPapParamsResultEntity resultEntity = (GetWXPapParamsResultEntity) o;
                            if (resultEntity != null && resultEntity.getResultCode().equals("0")) {
                                WeiXinPayEntity payEntity = resultEntity.getData();
                                wxPay(payEntity);
                            } else {
                                Tools.showPrompt(resultEntity.getMsg());
                            }
                        } else {
                            NetUtil.toNetworkSetting(ShopPayActivity.this);
                        }
                    }
                });
    }

    private void wxPay(WeiXinPayEntity payEntity) {
        PayReq req = new PayReq();
        req.appId = payEntity.getAppid();
        req.partnerId = payEntity.getPartnerid();
        req.prepayId = payEntity.getPrepayid();
        req.nonceStr = payEntity.getNoncestr();
        req.timeStamp = payEntity.getTimestamp();
        req.packageValue = "Sign=WXPay";
        req.sign = payEntity.getSign();
        // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
        //3.调用微信支付sdk支付方法
        api.sendReq(req);
    }

    private void checkWeixinApp() {
        if (!api.isWXAppInstalled()) {
            showLongToast("没有安装微信客户端");
            return;
        }

//        if (!api.isWXAppSupportAPI()) {
//            showLongToast("当前微信版本过低");
//            return;
//        }
        getWXPayParams();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(EventEntity entity) {
        int code = entity.getEventCode();
        switch (code) {
            case 0://支付成功
                new AlertView("温馨提示", Tools.getStringValue("微信支付成功"),
                        null, new String[]{"知道了"}, null, ShopPayActivity.this, AlertView.Style.Alert, new OnItemClickListener() {
                    @Override
                    public void onItemClick(Object o, int position) {
                        Bundle bundle = new Bundle();
                        if (1 == fromWhere) {
                            bundle.putInt("isFromWhere", 1);
                            openActivity(MyOrderActivity.class, bundle);
                        } else {

                        }
                        ShopPayActivity.this.finish();
                    }
                }).setCancelable(false).show();
                break;
            case -1://支付错误
                new AlertView("温馨提示", Tools.getStringValue("微信支付失败"),
                        null, new String[]{"知道了"}, null, ShopPayActivity.this, AlertView.Style.Alert, null).setCancelable(true).show();
                break;
            case -2://用户取消
                new AlertView("温馨提示", Tools.getStringValue("您已取消微信支付"),
                        null, new String[]{"知道了"}, null, ShopPayActivity.this, AlertView.Style.Alert, null).setCancelable(true).show();
                break;
            default:
                break;
        }
    }

    private void shopPay() {
        startProgressDialog("");
        ApiHttpResult.updateOrderStatus(this, new String[]{"id", "status"},
                new Object[]{orderId, "1"},
                new HttpUtils.DataCallBack() {
                    @Override
                    public void callBack(Object o) {
                        stopProgressDialog();
                        if (o != null) {
                            OverallSituationEntity resultEntity = (OverallSituationEntity) o;
                            if (resultEntity != null && resultEntity.getResultCode().equals("0")) {
                                Tools.showPrompt(resultEntity.getMsg());
                                Bundle bundle = new Bundle();
                                bundle.putInt("isFromWhere", 1);
                                openActivity(MyOrderActivity.class, bundle);
                                ShopPayActivity.this.finish();
                            } else {
                                Tools.showPrompt(resultEntity.getMsg());
                            }
                        } else {
                            NetUtil.toNetworkSetting(ShopPayActivity.this);
                        }
                    }
                });
    }
}
