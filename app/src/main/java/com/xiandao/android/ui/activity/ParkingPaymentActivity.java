package com.xiandao.android.ui.activity;

import android.content.Intent;
import android.graphics.Rect;
import android.inputmethodservice.KeyboardView;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xiandao.android.R;
import com.xiandao.android.entity.HikBaseEntity;
import com.xiandao.android.entity.HikParkingPayment;
import com.xiandao.android.entity.eventbus.EventBusMessage;
import com.xiandao.android.entity.hikisc.HikIscEntity;
import com.xiandao.android.http.HikJsonParse;
import com.xiandao.android.http.MyCallBack;
import com.xiandao.android.http.XUtils;
import com.xiandao.android.net.ApiHttpResult;
import com.xiandao.android.net.HttpUtils;
import com.xiandao.android.ui.BaseActivity;
import com.xiandao.android.utils.Constants;
import com.xiandao.android.utils.FileManagement;
import com.xiandao.android.utils.IOSTimeTrans;
import com.xiandao.android.view.platenumberview.CarPlateNumberEditView;
import com.xiandao.android.view.platenumberview.PlateNumberKeyboardUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.event.annotation.ContentView;
import org.xutils.event.annotation.Event;
import org.xutils.event.annotation.ViewInject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ContentView(R.layout.activity_parking_payment)
public class ParkingPaymentActivity extends BaseActivity {
    @ViewInject(R.id.tv_title_name)
    private TextView tv_title_name;
    @ViewInject(R.id.tv_parking_car_in_time)
    private TextView tv_parking_car_in_time;
    @ViewInject(R.id.tv_parking_car_park_time)
    private TextView tv_parking_car_park_time;
    @ViewInject(R.id.tv_parking_car_payment)
    private TextView tv_parking_car_payment;
    @ViewInject(R.id.tv_parking_car_pay_amount)
    private TextView tv_parking_car_pay_amount;
    @ViewInject(R.id.btn_parking_payment)
    private Button btn_parking_payment;
    @ViewInject(R.id.cpn_edit)
    private CarPlateNumberEditView cpn_edit;
    @ViewInject(R.id.keyboard_view)
    private KeyboardView keyboard_view;
    private PlateNumberKeyboardUtil plateNumberKeyboardUtil;
    @ViewInject(R.id.btn_parking_payment_search_mask)
    private TextView btn_parking_payment_search_mask;

    private String billSyscode;
    private String supposeCost;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tv_title_name.setText("停车交费");
        cpn_edit.setOnPlateNumberValid(new CarPlateNumberEditView.OnPlateNumberValid() {
            @Override
            public void plateNumberValid(boolean valid) {
                if (valid){
                    btn_parking_payment_search_mask.setVisibility(View.GONE);
                }else{
                    btn_parking_payment_search_mask.setVisibility(View.VISIBLE);
                }
            }
        });
        EventBus.getDefault().register(this);
//        getFaceGroup();
//        addFace();
//        queryFace();
//        updateFace();
//        deleteFace();

    }

    //获取人脸分组
    private void getFaceGroup(){
        Map<String,Object> requestDataMap=new HashMap<>();
        //requestDataMap.put("name","长房云西府");
        ApiHttpResult.hikFaceGroup(this, requestDataMap, new HttpUtils.DataCallBack() {
            @Override
            public void callBack(Object o) {
                HikBaseEntity hikBaseEntity= (HikBaseEntity) o;
                if(hikBaseEntity.getCode().equals("0")){

                }else{
                    //btn_parking_payment.setVisibility(View.GONE);
                    showShortToast("没有查到人脸分组");
                }
            }
        });

    }
    //新增人脸 长房云西府 ---- 04e0ce07-23e5-4dcf-96cb-e67181423096
    private void addFace(){
        Map<String,Object> requestDataMap=new HashMap<>();
        requestDataMap.put("faceGroupIndexCode","04e0ce07-23e5-4dcf-96cb-e67181423096");
        Map<String,Object> faceInfo=new HashMap<>();
        faceInfo.put("name",FileManagement.getLoginUserEntity().getNickName());
        Map<String,Object> facePic=new HashMap<>();
        facePic.put("faceUrl","http://10.10.222.110:9081/smartxd/api/getFaceImageProcess.action?id=201905110215078405");
        requestDataMap.put("faceInfo",faceInfo);
        requestDataMap.put("facePic",facePic);
        ApiHttpResult.hikAddFace(this, requestDataMap, new HttpUtils.DataCallBack() {
            @Override
            public void callBack(Object o) {
                HikBaseEntity hikBaseEntity= (HikBaseEntity) o;
                if(hikBaseEntity.getCode().equals("0")){

                }else{
                    //btn_parking_payment.setVisibility(View.GONE);
                    showShortToast("新增人脸失败:"+((HikBaseEntity) o).getMsg());
                }
            }
        });
    }
    //查询人脸 --45fe9a9c-f2e3-4ea7-be45-110b96a034d3 , 207fdf3b-c0ec-4374-aef6-b5062a0b6b29
    private void queryFace(){
        Map<String,Object> requestDataMap=new HashMap<>();
        requestDataMap.put("faceGroupIndexCode","04e0ce07-23e5-4dcf-96cb-e67181423096");
        ApiHttpResult.hikQueryFace(this, requestDataMap, new HttpUtils.DataCallBack() {
            @Override
            public void callBack(Object o) {
                HikBaseEntity hikBaseEntity= (HikBaseEntity) o;
                if(hikBaseEntity.getCode().equals("0")){

                }else{
                    showShortToast("没有查到人脸");
                }
            }
        });
    }
    //更新人脸
    private void updateFace(){
        Map<String,Object> requestDataMap=new HashMap<>();
        requestDataMap.put("indexCode","45fe9a9c-f2e3-4ea7-be45-110b96a034d3");
        Map<String,Object> faceInfo=new HashMap<>();
        faceInfo.put("name",FileManagement.getLoginUserEntity().getNickName());
        Map<String,Object> facePic=new HashMap<>();
        facePic.put("faceUrl","http://10.10.222.110:9081/smartxd/api/getFaceImageProcess.action?id=201905151103183047");
        requestDataMap.put("faceInfo",faceInfo);
        requestDataMap.put("facePic",facePic);
        ApiHttpResult.hikUpdateFace(this, requestDataMap, new HttpUtils.DataCallBack() {
            @Override
            public void callBack(Object o) {
                HikBaseEntity hikBaseEntity= (HikBaseEntity) o;
                if(hikBaseEntity.getCode().equals("0")){

                }else{
                    showShortToast("更新人脸失败");
                }
            }
        });
    }
    //删除人脸
    private void deleteFace(){
        Map<String,Object> requestDataMap=new HashMap<>();
        requestDataMap.put("faceGroupIndexCode","04e0ce07-23e5-4dcf-96cb-e67181423096");
        List<String> indexCodes=new ArrayList<>();
        indexCodes.add("45fe9a9c-f2e3-4ea7-be45-110b96a034d3");
        indexCodes.add("207fdf3b-c0ec-4374-aef6-b5062a0b6b29");
        requestDataMap.put("indexCodes",indexCodes);
        ApiHttpResult.hikDeleteFace(this, requestDataMap, new HttpUtils.DataCallBack() {
            @Override
            public void callBack(Object o) {
                HikBaseEntity hikBaseEntity= (HikBaseEntity) o;
                if(hikBaseEntity.getCode().equals("0")){

                }else{
                    showShortToast("删除人脸失败");
                }
            }
        });

    }


    private void getParkingPayment_bak(String plateNo){
        Map<String, Object> requestDataMap=new HashMap<>();
        requestDataMap.put("plateNo",plateNo);
        ApiHttpResult.hikParkingPayment(this, requestDataMap, new HttpUtils.DataCallBack() {
            @Override
            public void callBack(Object o) {
                HikBaseEntity hikBaseEntity= (HikBaseEntity) o;
                if(hikBaseEntity.getCode().equals("0")){
                    btn_parking_payment.setVisibility(View.VISIBLE);
                    HikParkingPayment hikParkingPayment= (HikParkingPayment) hikBaseEntity.getData();
                    tv_parking_car_in_time.setText(IOSTimeTrans.trans(hikParkingPayment.getEnterTime()));
                    tv_parking_car_park_time.setText(hikParkingPayment.getParkTime()+"分钟");
                    tv_parking_car_payment.setText(hikParkingPayment.getSupposeCost()+"元");
                    supposeCost=hikParkingPayment.getSupposeCost();
                    tv_parking_car_pay_amount.setText(hikParkingPayment.getPaidCost()+"元");
                    billSyscode=hikParkingPayment.getBillSyscode();
                }else{
                    btn_parking_payment.setVisibility(View.GONE);
                    showShortToast("没有查到该车牌下账单信息");
                }
            }
        });
    }

    private void getParkingPayment(String plateNo){
        Map<String, Object> requestMap=new HashMap<>();
        requestMap.put("plateNo",plateNo);
        XUtils.Post(Constants.HOST+"/isc/pay/quickPreBill.action",requestMap,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                Log.e("result",result);
                HikIscEntity<HikParkingPayment> hikIscEntity= HikJsonParse.parse(result,HikParkingPayment.class);
                if(hikIscEntity.getCode().equals("0")){
                    btn_parking_payment.setVisibility(View.VISIBLE);
                    tv_parking_car_in_time.setText(IOSTimeTrans.trans(hikIscEntity.getData().getEnterTime()));
                    tv_parking_car_park_time.setText(hikIscEntity.getData().getParkTime()+"分钟");
                    tv_parking_car_payment.setText(hikIscEntity.getData().getSupposeCost()+"元");
                    supposeCost=hikIscEntity.getData().getSupposeCost();
                    tv_parking_car_pay_amount.setText(hikIscEntity.getData().getPaidCost()+"元");
                    billSyscode=hikIscEntity.getData().getBillSyscode();
                }else{
                    btn_parking_payment.setVisibility(View.GONE);
                    showShortToast("没有查到该车牌下账单信息");
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                showShortToast(ex.getMessage());
            }

            @Override
            public void onFinished() {
                super.onFinished();
            }
        });
    }

    private void finishPayment_bak(final String actualCost){
        Map<String, Object> requestDataMap=new HashMap<>();
        requestDataMap.put("billSyscode",billSyscode);
        requestDataMap.put("actualCost",actualCost);
        ApiHttpResult.hikFinishPayment(this, requestDataMap, new HttpUtils.DataCallBack() {
            @Override
            public void callBack(Object o) {
                HikBaseEntity hikBaseEntity= (HikBaseEntity) o;
                if(hikBaseEntity.getCode().equals("0")){
                    showShortToast("支付成功");
                    tv_parking_car_pay_amount.setText(actualCost+"元");
                    btn_parking_payment.setVisibility(View.GONE);
                }else{
                    showShortToast("支付失败");
                }
            }
        });
    }

    /**
     * 完成收费
     * @param actualCost
     */
    private void finishPayment(final String actualCost){
        Map<String, Object> requestDataMap=new HashMap<>();
        requestDataMap.put("billSyscode",billSyscode);
        requestDataMap.put("actualCost",actualCost);
        XUtils.Post(Constants.HOST+"/isc/pay/receipt.action",requestDataMap,new MyCallBack<HikIscEntity>(){
            @Override
            public void onSuccess(HikIscEntity result) {
                super.onSuccess(result);
                Log.e("result",result.toString());
                if(result.getCode().equals("0")){
                    showShortToast("支付成功");
                    tv_parking_car_pay_amount.setText(actualCost+"元");
                    btn_parking_payment.setVisibility(View.GONE);
                }else{
                    showShortToast("支付失败");
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                showShortToast(ex.getMessage());
            }

            @Override
            public void onFinished() {
                super.onFinished();
            }
        });
    }


    @Event({R.id.iv_title_left,R.id.btn_parking_payment,R.id.cpn_edit,R.id.btn_parking_payment_search})
    private void onClickEvent(View v){
        switch (v.getId()){
            case R.id.iv_title_left:
                finish();
                break;
            case R.id.btn_parking_payment:
                Bundle bundle=new Bundle();
                bundle.putString("no",billSyscode);
                bundle.putString("count",supposeCost);
                openActivityResult(PaymentTestActivity.class,bundle,100);
                break;
            case R.id.cpn_edit:
                if (plateNumberKeyboardUtil == null) {
                    plateNumberKeyboardUtil = new PlateNumberKeyboardUtil(ParkingPaymentActivity.this, cpn_edit);
                    plateNumberKeyboardUtil.showKeyboard();
                } else {
                    plateNumberKeyboardUtil.showKeyboard();
                }
                break;
            case R.id.btn_parking_payment_search:
                if(btn_parking_payment_search_mask.getVisibility()==View.GONE){
                    getParkingPayment(cpn_edit.getPlateNumberText());
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100&&resultCode==RESULT_OK){
            finishPayment(supposeCost);
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(EventBusMessage message){
        if("paymentOk".equals(message.getMessage())){
            finishPayment(supposeCost);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (plateNumberKeyboardUtil!=null&&plateNumberKeyboardUtil.isShow()) {
                plateNumberKeyboardUtil.hideKeyboard();
            } else {
                finish();
            }
        }
        return false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(plateNumberKeyboardUtil!=null&&plateNumberKeyboardUtil.isShow()){
            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                Rect viewRect = new Rect();
                btn_parking_payment_search_mask.getGlobalVisibleRect(viewRect);
                if (viewRect.contains((int) ev.getRawX(), (int) ev.getRawY())) {
                    if(btn_parking_payment_search_mask.getVisibility()==View.GONE){
                        getParkingPayment(cpn_edit.getPlateNumberText());
                    }
                    plateNumberKeyboardUtil.hideKeyboard();
                    return true;
                }else{
                    keyboard_view.getGlobalVisibleRect(viewRect);
                    if (!viewRect.contains((int) ev.getRawX(), (int) ev.getRawY())) {
                        plateNumberKeyboardUtil.hideKeyboard();
                        return true;
                    }
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
