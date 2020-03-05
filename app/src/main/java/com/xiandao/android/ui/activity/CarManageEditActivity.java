package com.xiandao.android.ui.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.inputmethodservice.KeyboardView;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
import com.xiandao.android.entity.smart.CarEntity;
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
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.event.annotation.ContentView;
import org.xutils.event.annotation.Event;
import org.xutils.event.annotation.ViewInject;
import org.xutils.http.RequestParams;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.xiandao.android.utils.EnumUtils.getCarColorString;
import static com.xiandao.android.utils.EnumUtils.getCarTypeString;
import static com.xiandao.android.utils.EnumUtils.getPlateColorString;
import static com.xiandao.android.utils.EnumUtils.getPlateTypeString;

@ContentView(R.layout.activity_car_manage_edit)
public class CarManageEditActivity extends BaseTakePhotoActivity {
    @ViewInject(R.id.tv_title_name)
    private TextView tv_title_name;
    @ViewInject(R.id.tv_title_right)
    private TextView tv_title_right;
    @ViewInject(R.id.iv_title_right)
    private ImageView iv_title_right;

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


    @ViewInject(R.id.tv_car_manage_edit_pay_mode)
    private TextView tv_car_manage_edit_pay_mode;
    @ViewInject(R.id.tv_car_manage_edit_charge)
    private TextView tv_car_manage_edit_charge;

    @ViewInject(R.id.ll_car_manage_charge_pay_list)
    private LinearLayout ll_car_manage_charge_pay_list;

    private ArrayList<TImage> tImages = new ArrayList<>();// 添加图片集合
    private String carImagePath="";

    private SpinerPopWindow plateColorPop;
    private ArrayList<String> plateColorList = new ArrayList<>();
    private ArrayList<String> plateTypeList = new ArrayList<>();
    private ArrayList<String> carColorList = new ArrayList<>();
    private ArrayList<String> carTypeList = new ArrayList<>();

    private String plateColor,plateType,carColor,carType;
    private CarEntity carManageEntity;

    private CarChargeInfo info;
    private int addMonth;
    private String feeStr;

    private String tempStartTime;
    private String tempEndTime;
    private boolean reCharge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tv_title_name.setText("新增车辆");
        tv_title_right.setText("确定");
        tv_title_right.setVisibility(View.VISIBLE);
        iv_title_right.setVisibility(View.GONE);
        initData();
        Bundle bundle=getIntent().getExtras();
        carManageEntity= (CarEntity) bundle.getSerializable("car");
        StringBuffer sb=new StringBuffer(carManageEntity.getPlateNO());
        sb.insert(2," · ");
        tv_title_name.setText(sb);
        initEditData();
        if(carManageEntity.getAuditStatus()==0){
//            getCarChargeInfo();
        }else{
            tv_car_manage_edit_charge.setVisibility(View.GONE);
        }
        EventBus.getDefault().register(this);
        if(FileManagement.getParkIndexCode()==null||"".equals(FileManagement.getParkIndexCode())){
//            getParkList();
        }
    }

    private void initEditData(){
//        if(carManageEntity.getCarPhoto()!=null){
//            XUtilsImageUtils.display(tv_car_manage_add_car_photo,Constants.BASEHOST+carManageEntity.getCarPhoto(),ImageView.ScaleType.CENTER_INSIDE);
//        }
        if(carManageEntity.getPlateColor()!=null){
            tv_car_manage_add_plate_color.setText(getPlateColorString(carManageEntity.getPlateColor()));
            plateColor=carManageEntity.getPlateColor();
        }
        if(carManageEntity.getPlateType()!=null){
            tv_car_manage_add_plate_type.setText(getPlateTypeString(carManageEntity.getPlateType()));
            plateType=carManageEntity.getPlateType();
        }
        if(carManageEntity.getVehicleColor()!=null){
            tv_car_manage_add_car_color.setText(getCarColorString(carManageEntity.getVehicleColor()));
            carColor=carManageEntity.getVehicleColor();
        }
        if(carManageEntity.getVehicleType()!=null){
            tv_car_manage_add_car_type.setText(getCarTypeString(carManageEntity.getVehicleType()));
            carType=carManageEntity.getVehicleType();
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

    @Event({R.id.iv_title_left,R.id.tv_title_right,R.id.tv_car_manage_add_plate_color,R.id.tv_car_manage_add_plate_type,
            R.id.tv_car_manage_add_car_color,R.id.tv_car_manage_add_car_type,R.id.tv_car_manage_add_car_photo,
            R.id.tv_car_manage_edit_charge,R.id.ll_car_manage_charge_pay_1,R.id.ll_car_manage_charge_pay_3,R.id.ll_car_manage_charge_pay_6})
    private void onClickEvent(View v){
        switch (v.getId()){
            case R.id.iv_title_left:
                finish();
                break;
            case R.id.tv_title_right:
//                carChargeDeletion();
                saveCar();
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
                        new String[]{"拍照", "从相册中选择"}, CarManageEditActivity.this,
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
            case R.id.tv_car_manage_edit_charge:
                if(ll_car_manage_charge_pay_list.getVisibility()==View.GONE){
                    ll_car_manage_charge_pay_list.setVisibility(View.VISIBLE);
                }else{
                    ll_car_manage_charge_pay_list.setVisibility(View.GONE);
                }
                break;
            case R.id.ll_car_manage_charge_pay_1:
                addMonth=1;
                feeStr="218.00";
                Bundle bundle_1=new Bundle();
                bundle_1.putString("no","NCL"+new Date().getTime());
                bundle_1.putString("count","218.00");
                openActivity(PaymentTestActivity.class,bundle_1);
                break;
            case R.id.ll_car_manage_charge_pay_3:
                addMonth=3;
                feeStr="618.00";
                Bundle bundle_3=new Bundle();
                bundle_3.putString("no","NCL"+new Date().getTime());
                bundle_3.putString("count","618.00");
                openActivity(PaymentTestActivity.class,bundle_3);
                break;
            case R.id.ll_car_manage_charge_pay_6:
                addMonth=6;
                feeStr="1118.00";
                Bundle bundle_6=new Bundle();
                bundle_6.putString("no","NCL"+new Date().getTime());
                bundle_6.putString("count","1118.00");
                openActivity(PaymentTestActivity.class,bundle_6);
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
        RequestParams params = new RequestParams(Constants.HOST+"/vehicleinfo/update.action");
        params.addBodyParameter("ownerid",FileManagement.getLoginUserEntity().getUserId());
        params.addBodyParameter("plateType",plateType+"");
        params.addBodyParameter("plateColor",plateColor+"");
        params.addBodyParameter("carColor",carColor+"");
        params.addBodyParameter("carType",carType+"");
        params.addBodyParameter("vehiclecode",carManageEntity.getParkingNO());
        if(!"".endsWith(carImagePath)){
            params.addBodyParameter("pic",new File(carImagePath));
        }
        XUtils.UploadFile(params,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                Log.e("result",result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if(jsonObject.getInt("resultCode")==200) {
                        EventBus.getDefault().post(new EventBusMessage<>("carAdd"));
                        finish();
                    }else{
                        showShortToast(jsonObject.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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

    private void getParkList_bak(){
        Map<String, Object> requestDataMap=new HashMap<>();
        ApiHttpResult.parkList(this,requestDataMap,new HttpUtils.DataCallBack(){
            @Override
            public void callBack(Object o) {
                HikBaseEntity hikBaseEntity= (HikBaseEntity) o;
                if(hikBaseEntity.getCode().equals("0")){
                    List<HikParkInfo> list= (List<HikParkInfo>) hikBaseEntity.getData();
                    if(list!=null&&list.size()>0){
                        FileManagement.setParkIndexCode(list.get(0).getParkIndexCode());
                        if(reCharge){
                            carCharge(tempStartTime,tempEndTime);
                        }
                    }
                }else{
                    showShortToast(hikBaseEntity.getMsg());
                }
            }
        });
    }

    private void getParkList(){
        XUtils.Post(Constants.HOST+"/isc/park/parklist.action",null,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                Log.e("result",result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if(jsonObject.getString("code").equals("0")) {
                        Gson gson=new Gson();
                        Type type = new TypeToken<List<HikParkInfo>>() {}.getType();
                        List<HikParkInfo> list=gson.fromJson(jsonObject.getString("data"),type);
                        if(list!=null&&list.size()>0){
                            FileManagement.setParkIndexCode(list.get(0).getParkIndexCode());
                            if(reCharge){
                                carCharge(tempStartTime,tempEndTime);
                            }
                        }
                    }else{
                        showShortToast(jsonObject.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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

    private void carCharge_bak(final String startTime, final String endTime){
        Map<String, Object> requestDataMap=new HashMap<>();
        requestDataMap.put("parkSyscode",FileManagement.getParkIndexCode());
        requestDataMap.put("plateNo",carManageEntity.getPlateNO());
        requestDataMap.put("fee",feeStr);
        requestDataMap.put("startTime",startTime);
        requestDataMap.put("endTime",endTime);
        ApiHttpResult.carCharge(this,requestDataMap,new HttpUtils.DataCallBack(){
            @Override
            public void callBack(Object o) {
                HikBaseEntity hikBaseEntity= (HikBaseEntity) o;
                if(hikBaseEntity.getCode().equals("0")){
                    getCarChargeInfo();
                    Log.e("carCharge","车辆充值成功！");
                }else{
                    if(hikBaseEntity.getCode().equals("0x00072202")){
                        tempStartTime=startTime;
                        tempEndTime=endTime;
                        reCharge=true;
                        getParkList();
                    }else{
                        showShortToast(hikBaseEntity.getMsg());
                    }
                }
            }
        });
    }

    private void carCharge(final String startTime, final String endTime){
        startProgressDialog("");
        Map<String, Object> requestDataMap=new HashMap<>();
        requestDataMap.put("parkSyscode",FileManagement.getParkIndexCode());
        requestDataMap.put("plateNo",carManageEntity.getParkingNO());
        requestDataMap.put("fee",feeStr);
        requestDataMap.put("startTime",startTime);
        requestDataMap.put("endTime",endTime);
        XUtils.Post(Constants.HOST+"/isc/car/charge.action",requestDataMap,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                Log.e("result",result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if(jsonObject.getString("code").equals("0")) {
                        getCarChargeInfo();
                        EventBus.getDefault().post(new EventBusMessage<>("carCharge"));
                        Log.e("carCharge","车辆充值成功！");
                    }else{
                        if(jsonObject.getString("code").equals("0x00072202")){
                            tempStartTime=startTime;
                            tempEndTime=endTime;
                            reCharge=true;
                            getParkList();
                        }else{
                            showShortToast(jsonObject.getString("msg"));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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

    private void carChargeDeletion(){
        Map<String, Object> requestDataMap=new HashMap<>();
        requestDataMap.put("parkSyscode","1223399961e4475c81ae6ba4038341f7");
        requestDataMap.put("plateNo",carManageEntity.getPlateNO());
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

    private void getCarChargeInfo_bak(){
        Map<String, Object> requestDataMap=new HashMap<>();
        requestDataMap.put("plateNo",carManageEntity.getPlateNO());
        requestDataMap.put("pageNo",1);
        requestDataMap.put("pageSize",100);
        ApiHttpResult.carChargeInfo(this,requestDataMap,new HttpUtils.DataCallBack(){
            @Override
            public void callBack(Object o) {
                HikBaseEntity hikBaseEntity= (HikBaseEntity) o;
                if(hikBaseEntity.getCode().equals("0")){
                    info = ((List<CarChargeInfo>) hikBaseEntity.getData()).get(0);
                    if(info.getGroupName()!=null&&!"".equals(info.getGroupName())){
                        tv_car_manage_edit_pay_mode.setText(info.getGroupName());
                        tv_car_manage_edit_charge.setVisibility(View.GONE);
                    }else{
                        if(info.getValidity()!=null&&info.getValidity().size()>0){
                            tv_car_manage_edit_pay_mode.setText("固定车包期\r\n"+info.getValidity().get(0).getFunctionTime().get(0).getStartTime()+
                                    "至"+info.getValidity().get(0).getFunctionTime().get(0).getEndTime());
                            try {
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                Date end_date= sdf.parse(info.getValidity().get(0).getFunctionTime().get(0).getEndTime());
                                if(end_date.getTime()<=new Date().getTime()){
                                    tv_car_manage_edit_pay_mode.setText("固定车包期-包期已过期\r\n"+info.getValidity().get(0).getFunctionTime().get(0).getStartTime()+
                                            "至"+info.getValidity().get(0).getFunctionTime().get(0).getEndTime());
                                }else if(end_date.getTime()>new Date().getTime()&&end_date.getTime()<new Date().getTime()+7*24*60*60*1000){
                                    tv_car_manage_edit_charge.setText("即将到期，续费");
                                }else{
                                    tv_car_manage_edit_charge.setVisibility(View.GONE);
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }else{
                            tv_car_manage_edit_pay_mode.setText("临时车缴费");
                        }
                    }
                }else{
                    showShortToast(hikBaseEntity.getMsg());
                }
            }
        });
    }

    private void getCarChargeInfo(){
        Map<String, Object> requestDataMap=new HashMap<>();
        requestDataMap.put("plateNo",carManageEntity.getPlateNO());
        requestDataMap.put("pageNo",1);
        requestDataMap.put("pageSize",1000);
        XUtils.Post(Constants.HOST+"/isc/car/charge/page.action",requestDataMap,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                Log.e("result",result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if(jsonObject.getString("code").equals("0")) {
                        JSONObject data=jsonObject.getJSONObject("data");
                        Gson gson=new Gson();
                        Type type = new TypeToken<List<CarChargeInfo>>() {}.getType();
                        List<CarChargeInfo> list=gson.fromJson(data.getString("list"),type);
                        if(list.size()>0){
                            info = list.get(0);
                            if(info.getGroupName()!=null&&!"".equals(info.getGroupName())){
                                tv_car_manage_edit_pay_mode.setText(info.getGroupName());
                                tv_car_manage_edit_charge.setVisibility(View.GONE);
                            }else{
                                if(info.getValidity()!=null&&info.getValidity().size()>0){
                                    tv_car_manage_edit_pay_mode.setText("包期\r\n"+info.getValidity().get(0).getFunctionTime().get(0).getStartTime()+
                                            "至"+info.getValidity().get(0).getFunctionTime().get(0).getEndTime());
                                    try {
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                        Date end_date= sdf.parse(info.getValidity().get(0).getFunctionTime().get(0).getEndTime());
                                        if(end_date.getTime()<=new Date().getTime()){
                                            tv_car_manage_edit_pay_mode.setText("包期-已过期\r\n"+info.getValidity().get(0).getFunctionTime().get(0).getStartTime()+
                                                    "至"+info.getValidity().get(0).getFunctionTime().get(0).getEndTime());
                                        }else if(end_date.getTime()>new Date().getTime()&&end_date.getTime()<new Date().getTime()+7*24*60*60*1000){
                                            tv_car_manage_edit_charge.setText("即将到期，续费");
                                        }else{
                                            tv_car_manage_edit_charge.setVisibility(View.GONE);
                                        }
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }else{
                                    tv_car_manage_edit_pay_mode.setText("临时车缴费");
                                }
                            }
                        }

                    }else{
                        showShortToast(jsonObject.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(EventBusMessage message){
        if("paymentOk".equals(message.getMessage())){
            ll_car_manage_charge_pay_list.setVisibility(View.GONE);
            Date startDate=new Date();
            boolean flag=false;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if(info.getValidity()!=null&&info.getValidity().size()>0){
                try {
                    Date end_date= sdf.parse(info.getValidity().get(0).getFunctionTime().get(0).getEndTime());
                    if(end_date.getTime()>new Date().getTime()){
                        startDate=end_date;
                        flag=true;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            Calendar calendar=Calendar.getInstance();
            calendar.setTime(startDate);
            calendar.add(Calendar.MONTH,+addMonth);
            String endDateStr=sdf.format(calendar.getTime());
            String startDateStr=sdf.format(startDate);
            if(flag){
                startDateStr=info.getValidity().get(0).getFunctionTime().get(0).getStartTime();
            }
            carCharge(startDateStr,endDateStr);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
