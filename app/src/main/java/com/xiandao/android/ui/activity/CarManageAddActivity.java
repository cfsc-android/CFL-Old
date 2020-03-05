package com.xiandao.android.ui.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.KeyboardView;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;
import com.xiandao.android.R;
import com.xiandao.android.adapter.AbstractSpinerAdapter;
import com.xiandao.android.entity.CarChargeInfo;
import com.xiandao.android.entity.CarManageEntity;
import com.xiandao.android.entity.HikBaseEntity;
import com.xiandao.android.entity.HikParkInfo;
import com.xiandao.android.entity.PersonalInfomation;
import com.xiandao.android.entity.enumtype.CarColor;
import com.xiandao.android.entity.enumtype.CarType;
import com.xiandao.android.entity.enumtype.PlateColor;
import com.xiandao.android.entity.enumtype.PlateType;
import com.xiandao.android.entity.eventbus.EventBusMessage;
import com.xiandao.android.entity.smart.BaseEntity;
import com.xiandao.android.entity.smart.CarEntity;
import com.xiandao.android.http.JsonParse;
import com.xiandao.android.http.MyCallBack;
import com.xiandao.android.http.XUtils;
import com.xiandao.android.net.ApiHttpResult;
import com.xiandao.android.net.HttpUtils;
import com.xiandao.android.ui.BaseTakePhotoActivity;
import com.xiandao.android.utils.Constants;
import com.xiandao.android.utils.FileManagement;
import com.xiandao.android.utils.PhotoUtils;
import com.xiandao.android.utils.XUtilsImageUtils;
import com.xiandao.android.view.SpinerPopWindow;
import com.xiandao.android.view.alertview.AlertView;
import com.xiandao.android.view.alertview.OnItemClickListener;
import com.xiandao.android.view.platenumberview.CarPlateNumberEditView;
import com.xiandao.android.view.platenumberview.PlateNumberKeyboardUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.app.LynActivityManager;
import org.xutils.common.util.LogUtil;
import org.xutils.event.annotation.ContentView;
import org.xutils.event.annotation.Event;
import org.xutils.event.annotation.ViewInject;
import org.xutils.http.RequestParams;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.xiandao.android.base.Config.BASE_URL;
import static com.xiandao.android.base.Config.BASIC;
import static com.xiandao.android.utils.EnumUtils.getCarColorString;
import static com.xiandao.android.utils.EnumUtils.getCarTypeString;
import static com.xiandao.android.utils.EnumUtils.getPlateColorString;
import static com.xiandao.android.utils.EnumUtils.getPlateTypeString;

@ContentView(R.layout.activity_car_manage_add)
public class CarManageAddActivity extends BaseTakePhotoActivity {
    @ViewInject(R.id.tv_title_name)
    private TextView tv_title_name;
    @ViewInject(R.id.tv_title_right)
    private TextView tv_title_right;
    @ViewInject(R.id.iv_title_right)
    private ImageView iv_title_right;
    @ViewInject(R.id.cpn_edit)
    private CarPlateNumberEditView cpn_edit;
    @ViewInject(R.id.keyboard_view)
    private KeyboardView keyboard_view;
    private PlateNumberKeyboardUtil plateNumberKeyboardUtil;
    @ViewInject(R.id.btn_parking_payment_search_mask)
    private TextView btn_parking_payment_search_mask;
    @ViewInject(R.id.tv_car_manage_add_plate_color)
    private TextView tv_car_manage_add_plate_color;
    @ViewInject(R.id.tv_car_manage_add_plate_type)
    private TextView tv_car_manage_add_plate_type;
    @ViewInject(R.id.tv_car_manage_add_car_color)
    private TextView tv_car_manage_add_car_color;
    @ViewInject(R.id.tv_car_manage_add_car_type)
    private TextView tv_car_manage_add_car_type;
    @ViewInject(R.id.tv_car_manage_add_car_photo)
    private ImageView tv_car_manage_add_car_photo;
    private ArrayList<TImage> tImages = new ArrayList<>();// 添加图片集合
    private String carImagePath="";

    private SpinerPopWindow plateColorPop;
    private ArrayList<String> plateColorList = new ArrayList<>();
    private ArrayList<String> plateTypeList = new ArrayList<>();
    private ArrayList<String> carColorList = new ArrayList<>();
    private ArrayList<String> carTypeList = new ArrayList<>();

    private String plateColor,plateType,carColor,carType;
    private CarEntity carEntity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tv_title_name.setText("新增车辆");
        tv_title_right.setText("确定");
        iv_title_right.setVisibility(View.GONE);
        initData();
        cpn_edit.setOnPlateNumberValid(new CarPlateNumberEditView.OnPlateNumberValid() {
            @Override
            public void plateNumberValid(boolean valid) {
                if (valid){
                    tv_title_right.setVisibility(View.VISIBLE);
//                    btn_parking_payment_search_mask.setVisibility(View.GONE);
                }else{
                    tv_title_right.setVisibility(View.INVISIBLE);
//                    btn_parking_payment_search_mask.setVisibility(View.VISIBLE);
                }
            }
        });
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
            tv_title_name.setText("编辑车辆");
            carEntity= (CarEntity) bundle.getSerializable("car");
            cpn_edit.setPlateNumber(carEntity.getPlateNO());
            initEditData();
        }
    }

    private void initEditData(){
//        if(carEntity.getCarPhoto()!=null){
//            XUtilsImageUtils.display(tv_car_manage_add_car_photo,Constants.BASEHOST+carManageEntity.getCarPhoto(),ImageView.ScaleType.CENTER_INSIDE);
//        }
        if(carEntity.getPlateColor()!=null){
            tv_car_manage_add_plate_color.setText(getPlateColorString(carEntity.getPlateColor()));
            plateColor=carEntity.getPlateColor();
        }
        if(carEntity.getPlateType()!=null){
            tv_car_manage_add_plate_type.setText(getPlateTypeString(carEntity.getPlateType()));
            plateType=carEntity.getPlateType();
        }
        if(carEntity.getVehicleColor()!=null){
            tv_car_manage_add_car_color.setText(getCarColorString(carEntity.getVehicleColor()));
            carColor=carEntity.getVehicleColor();
        }
        if(carEntity.getVehicleType()!=null){
            tv_car_manage_add_car_type.setText(getCarTypeString(carEntity.getVehicleType()));
            carType=carEntity.getVehicleType();
        }
    }

    private void initData(){

        for(PlateColor color:PlateColor.values()){
            plateColorList.add(color.getColor());
        }
        for(PlateType type:PlateType.values()){
            plateTypeList.add(type.getType());
        }
        for(CarColor color:CarColor.values()){
            carColorList.add(color.getColor());
        }
        for(CarType type:CarType.values()){
            carTypeList.add(type.getType());
        }

    }

    @Event({R.id.iv_title_left,R.id.tv_title_right,R.id.btn_car_manage_submit,R.id.tv_car_manage_add_plate_color,R.id.cpn_edit,
            R.id.tv_car_manage_add_plate_type,R.id.tv_car_manage_add_car_color,R.id.tv_car_manage_add_car_type,
            R.id.tv_car_manage_add_car_photo})
    private void onClickEvent(View v){
        switch (v.getId()){
            case R.id.iv_title_left:
                finish();
                break;
            case R.id.tv_title_right:
//                getCarChargeInfo();
//                carCharge();
                saveCar();
                break;
            case R.id.btn_car_manage_submit:
                saveCar();
                break;
            case R.id.cpn_edit:
                if(carEntity==null){
                    if (plateNumberKeyboardUtil == null) {
                        plateNumberKeyboardUtil = new PlateNumberKeyboardUtil(CarManageAddActivity.this, cpn_edit);
                        plateNumberKeyboardUtil.showKeyboard();
                    } else {
                        plateNumberKeyboardUtil.showKeyboard();
                    }
                }
                break;
            case R.id.tv_car_manage_add_plate_color:
                initPop(plateColorList,tv_car_manage_add_plate_color,0);
                break;
            case R.id.tv_car_manage_add_plate_type:
                initPop(plateTypeList,tv_car_manage_add_plate_type,1);
                break;
            case R.id.tv_car_manage_add_car_color:
                initPop(carColorList,tv_car_manage_add_car_color,2);
                break;
            case R.id.tv_car_manage_add_car_type:
                initPop(carTypeList,tv_car_manage_add_car_type,3);
                break;
            case R.id.tv_car_manage_add_car_photo:
                getTakePhoto();
                File file = new File(Environment.getExternalStorageDirectory(), "/xiandao/" + System.currentTimeMillis() + ".jpg");
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                final Uri imageUri = Uri.fromFile(file);
                configCompress(takePhoto);
                configTakePhotoOption(takePhoto);

                new AlertView("选择获取图片方式", null, "取消", null,
                        new String[]{"拍照", "从相册中选择"}, CarManageAddActivity.this,
                        AlertView.Style.ActionSheet, new OnItemClickListener() {
                    @Override
                    public void onItemClick(Object o, int position) {
                        if (position == 0) {
                            takePhoto.onPickFromCapture(imageUri);
                        } else if (position == 1) {
                            takePhoto.onPickMultiple(1 - tImages.size());
                        }
                    }
                }).show();
                break;
        }
    }

    private void initPop(ArrayList<String> data,final TextView view, final int result){
        plateColorPop=new SpinerPopWindow(this);
        plateColorPop.setItemListener(new AbstractSpinerAdapter.IOnItemSelectListener() {
            @Override
            public void onItemClick(int pos) {
                switch (result){
                    case 0:
                        String _color=plateColorList.get(pos);
                        for(PlateColor color:PlateColor.values()){
                            if(_color.equals(color.getColor())){
                                view.setText(_color);
                                plateColor=color.getValue();
                            }
                        }
                        break;
                    case 1:
                        String _type=plateTypeList.get(pos);
                        for(PlateType type:PlateType.values()){
                            if(_type.equals(type.getType())){
                                view.setText(_type);
                                plateType=type.getValue();
                            }
                        }
                        break;
                    case 2:
                        String k_color=carColorList.get(pos);
                        for(CarColor color:CarColor.values()){
                            if(k_color.equals(color.getColor())){
                                view.setText(k_color);
                                carColor=color.getValue();
                            }
                        }
                        break;
                    case 3:
                        String k_type=carTypeList.get(pos);
                        for(CarType type:CarType.values()){
                            if(k_type.equals(type.getType())){
                                view.setText(k_type);
                                carType=type.getValue();
                            }
                        }
                        break;
                }
            }
        });
        plateColorPop.refreshData(data, 0);
        plateColorPop.setWidth(view.getWidth());
        if (data.size() < 6) {
            plateColorPop.setHeight(view.getHeight() * data.size());
        } else {
            plateColorPop.setHeight(view.getHeight() * 4);
        }
        plateColorPop.showAsDropDown(view);
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
                keyboard_view.getGlobalVisibleRect(viewRect);
                if (!viewRect.contains((int) ev.getRawX(), (int) ev.getRawY())) {
                    plateNumberKeyboardUtil.hideKeyboard();
                    return true;
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void takeSuccess(TResult result) {
        TImage image = result.getImages().get(0);
        if (image != null) {
            tImages.add(image);
            String imagePath = image.getOriginalPath().replace("/external_storage_root", "");
            if (imagePath.indexOf(Environment.getExternalStorageDirectory() + "") == -1) {
                imagePath = Environment.getExternalStorageDirectory() + imagePath;
            }
            int old_orientation = getRotateDegree(imagePath);
            PhotoUtils.compressPicture(imagePath, imagePath);
            int new_orientation = getRotateDegree(imagePath);
            if(new_orientation!=old_orientation){
                setRotateDegree(imagePath,old_orientation);
            }
            carImagePath=imagePath;
            XUtilsImageUtils.display(tv_car_manage_add_car_photo,imagePath,ImageView.ScaleType.CENTER_INSIDE);
        }

    }
    /** 从给定的路径加载图片，并指定是否自动旋转方向*/
    private void setRotateDegree(String imgpath, int orientation) {
        Bitmap bitmap=BitmapFactory.decodeFile(imgpath);
        // 旋转图片
        Matrix m = new Matrix();
        m.postRotate(orientation);
        Bitmap return_bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
        FileOutputStream out;
        try {
            out = new FileOutputStream(imgpath);
            return_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    /** 获取照片旋转方向*/
    private static int getRotateDegree(String path) {
        int result = 0;
        try {
            ExifInterface exif = new ExifInterface(path);
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    result = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    result = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    result = 270;
                    break;
            }
        } catch (IOException ignore) {
            return 0;
        }
        return result;
    }

    private void saveCar(){
        startProgressDialog("");
        Map<String,Object> map=new HashMap<>();
        String action="basic/vehicleInfo/add";
        if(carEntity!=null){
            action="basic/vehicleInfo/update";
            map.put("id",carEntity.getId());
        }
        map.put("vehicleColor",carColor);
        map.put("vehicleType",carType);
        map.put("plateColor",plateColor);
        map.put("plateType",plateType);
        map.put("plateNO",cpn_edit.getPlateNumberText());
        map.put("householdId",FileManagement.getUserInfoEntity().getId());
        map.put("ownerPhone",FileManagement.getUserInfoEntity().getMobile());
        map.put("roomId",FileManagement.getUserInfoEntity().getRoomList().get(0).getId());
        XUtils.PostJson(BASE_URL+BASIC+action,map,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtil.d(result);
                BaseEntity baseEntity= JsonParse.parse(result);
                if(baseEntity.isSuccess()){
                    EventBus.getDefault().post(new EventBusMessage<>("carAdd"));
                    finish();
                }else{
                    showShortToast(baseEntity.getMessage());
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
                stopProgressDialog();
            }
        });
    }

//    private void saveCar(){
//        startProgressDialog("");
//        String action="/vehicleinfo/add.action";
//        if(carManageEntity!=null){
//            action="/vehicleinfo/update.action";
//        }
//        RequestParams params = new RequestParams(Constants.HOST+action);
//        params.addBodyParameter("ownerid",FileManagement.getLoginUserEntity().getUserId());
//        params.addBodyParameter("plateType",plateType+"");
//        params.addBodyParameter("plateColor",plateColor+"");
//        params.addBodyParameter("carColor",carColor+"");
//        params.addBodyParameter("carType",carType+"");
//        params.addBodyParameter("vehiclecode",cpn_edit.getPlateNumberText());
//        if(!"".endsWith(carImagePath)){
//            params.addBodyParameter("pic",new File(carImagePath));
//        }
//        XUtils.UploadFile(params,new MyCallBack<String>(){
//            @Override
//            public void onSuccess(String result) {
//                super.onSuccess(result);
//                Log.e("result",result);
//                try {
//                    JSONObject jsonObject = new JSONObject(result);
//                    if(jsonObject.getInt("resultCode")==200) {
//                        EventBus.getDefault().post(new EventBusMessage<>("carAdd"));
//                        finish();
//                    }else{
//                        showShortToast(jsonObject.getString("msg"));
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onError(Throwable ex, boolean isOnCallback) {
//                super.onError(ex, isOnCallback);
//                showShortToast(ex.getMessage());
//            }
//
//            @Override
//            public void onFinished() {
//                super.onFinished();
//                stopProgressDialog();
//            }
//        });
//    }

    private void getParkList(){
        Map<String, Object> requestDataMap=new HashMap<>();
        requestDataMap.put("parkIndexCodes","");
        ApiHttpResult.parkList(this,requestDataMap,new HttpUtils.DataCallBack(){
            @Override
            public void callBack(Object o) {
                HikBaseEntity hikBaseEntity= (HikBaseEntity) o;
                if(hikBaseEntity.getCode().equals("0")){
                    List<HikParkInfo> list= (List<HikParkInfo>) hikBaseEntity.getData();
                }else{
                    showShortToast(hikBaseEntity.getMsg());
                }
            }
        });
    }

    private void carCharge(){
        Map<String, Object> requestDataMap=new HashMap<>();
        requestDataMap.put("parkSyscode","1223399961e4475c81ae6ba4038341f7");
        requestDataMap.put("plateNo","湘AG169E");
        requestDataMap.put("fee",500.00);
        requestDataMap.put("startTime","2019-07-13");
        requestDataMap.put("endTime","2019-08-13");
        ApiHttpResult.carCharge(this,requestDataMap,new HttpUtils.DataCallBack(){
            @Override
            public void callBack(Object o) {
                HikBaseEntity hikBaseEntity= (HikBaseEntity) o;
                if(hikBaseEntity.getCode().equals("0")){
                    Log.e("carCharge","车辆充值成功！");
                }else{
                    showShortToast(hikBaseEntity.getMsg());
                }
            }
        });
    }

    private void carChargeDeletion(){
        Map<String, Object> requestDataMap=new HashMap<>();
        requestDataMap.put("parkSyscode","1223399961e4475c81ae6ba4038341f7");
        requestDataMap.put("plateNo","湘AG169E");
        ApiHttpResult.carChargeDeletion(this,requestDataMap,new HttpUtils.DataCallBack(){
            @Override
            public void callBack(Object o) {
                HikBaseEntity hikBaseEntity= (HikBaseEntity) o;
                if(hikBaseEntity.getCode().equals("0")){
                    Log.e("carChargeDeletion","取消车辆包期成功！");
                }else{
                    showShortToast(hikBaseEntity.getMsg());
                }
            }
        });

    }

    private void getCarChargeInfo(){
        Map<String, Object> requestDataMap=new HashMap<>();
        requestDataMap.put("plateNo","湘AG169E");
        requestDataMap.put("pageNo",1);
        requestDataMap.put("pageSize",100);
        ApiHttpResult.carChargeInfo(this,requestDataMap,new HttpUtils.DataCallBack(){
            @Override
            public void callBack(Object o) {
                HikBaseEntity hikBaseEntity= (HikBaseEntity) o;
                if(hikBaseEntity.getCode().equals("0")){
                    CarChargeInfo info = ((List<CarChargeInfo>) hikBaseEntity.getData()).get(0);
                    Log.e("info",info.getValidity().get(0).getFunctionTime().get(0).getStartTime()+"-"+info.getValidity().get(0).getFunctionTime().get(0).getEndTime());
                }else{
                    showShortToast(hikBaseEntity.getMsg());
                }
            }
        });
    }
}
