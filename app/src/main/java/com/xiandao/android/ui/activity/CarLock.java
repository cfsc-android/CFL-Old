package com.xiandao.android.ui.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.xiandao.android.R;
import com.xiandao.android.adapter.AbstractSpinerAdapter;
import com.xiandao.android.adapter.CarRecordListAdapter;
import com.xiandao.android.entity.CarManageEntity;
import com.xiandao.android.entity.HikAlarmAddition;
import com.xiandao.android.entity.HikAlarmCarlist;
import com.xiandao.android.entity.HikBaseEntity;
import com.xiandao.android.entity.HikCarCrossRecord;
import com.xiandao.android.entity.HikCarCrossRecordList;
import com.xiandao.android.entity.HikUser;
import com.xiandao.android.entity.hikisc.HikIscEntity;
import com.xiandao.android.entity.smart.BaseEntity;
import com.xiandao.android.entity.smart.CarEntity;
import com.xiandao.android.entity.smart.CarListEntity;
import com.xiandao.android.http.HikJsonParse;
import com.xiandao.android.http.JsonParse;
import com.xiandao.android.http.MyCallBack;
import com.xiandao.android.http.XUtils;
import com.xiandao.android.net.ApiHttpResult;
import com.xiandao.android.net.HttpUtils;
import com.xiandao.android.ui.BaseActivity;
import com.xiandao.android.utils.Constants;
import com.xiandao.android.utils.FileManagement;
import com.xiandao.android.utils.Tools;
import com.xiandao.android.view.SpinerPopWindow;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.util.LogUtil;
import org.xutils.event.annotation.ContentView;
import org.xutils.event.annotation.Event;
import org.xutils.event.annotation.ViewInject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.xiandao.android.base.Config.BASE_URL;
import static com.xiandao.android.base.Config.BASIC;
import static com.xiandao.android.base.Config.IOT;


/**
 * Created by ZXL on 2019/4/4.
 * Describe:
 */
@ContentView(R.layout.activity_car_lock)
public class CarLock extends BaseActivity {
    @ViewInject(R.id.tv_title_name)
    private TextView tv_title_name;

    @ViewInject(R.id.ptrlv_car_record_list)
    PullToRefreshListView ptrlv_car_record_list;
    @ViewInject(R.id.s_car_lock)
    private Switch s_car_lock;
    @ViewInject(R.id.tv_car_lock_flag)
    private TextView tv_car_lock_flag;

    @ViewInject(R.id.tv_car_code_select)
    private TextView tv_car_code_select;
    @ViewInject(R.id.ll_car_lock_status)
    private LinearLayout ll_car_lock_status;
    @ViewInject(R.id.ll_car_lock_record)
    private LinearLayout ll_car_lock_record;

    private SpinerPopWindow mpopwindow;

    private ArrayList<String> carList = new ArrayList<>();

    private CarRecordListAdapter carRecordListAdapter;
    private ArrayList<HikCarCrossRecord> carRecordArrayList=new ArrayList<>();

    private int page = 1;//第几页
    private int pageSize = 10;//每页显示多少条
    private int totalPages = 0;//总的页面数

    //===========筛选条件=========
    private String startTime;//开始时间
    private String endTime;//结束时间

    private boolean isFirstLoading = true;//判断是否首次加载

    private HikUser hikUser;
    private String currentPlateNo="";
    private String currentAlarmSyscode="";
    private String phaseId="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hikUser=FileManagement.getHikUser();
        phaseId=FileManagement.getUserInfoEntity().getRoomList().get(0).getPhaseId();
        tv_title_name.setText("智能锁车");
        s_car_lock.setChecked(true);
        tv_car_lock_flag.setText("解锁");
        Drawable drawable=getResources().getDrawable(R.drawable.icon_car_unlock);
        drawable.setBounds(0, 0, 68, 68);
        tv_car_lock_flag.setCompoundDrawables(drawable,null,null,null);
        getCarList();
    }

    /**
     * 获取车辆列表
     */
    private void getCarList(){
        startProgressDialog("");
        Map<String,String> requestMap=new HashMap<>();
        requestMap.put("pageNo","1");
        requestMap.put("pageSize","10");
        XUtils.Get(BASE_URL+BASIC+"basic/vehicleInfo/vehiclePage",requestMap,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtil.d(result);
                BaseEntity<CarListEntity> baseEntity= JsonParse.parse(result,CarListEntity.class);
                if(baseEntity.isSuccess()){
                    List<String> vehicleCodes=new ArrayList<>();
                    for (CarEntity carEntity : baseEntity.getResult().getData()) {
                        if(carEntity.getAuditStatus()==1){
                            vehicleCodes.add(carEntity.getPlateNO());
                        }
                    }
                    initCarList(vehicleCodes);
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

    private void initCarList(List<String> vehicleCodes){
        if(vehicleCodes.size()>0){
            mpopwindow = new SpinerPopWindow(CarLock.this);
            currentPlateNo=vehicleCodes.get(0);
            tv_car_code_select.setText(currentPlateNo);
            getAlarmCar();
            initCarCross();
            carList.clear();
            for (int i = 0; i < vehicleCodes.size(); i++) {
                carList.add(vehicleCodes.get(i));
            }
            mpopwindow.refreshData(carList, 0);
            mpopwindow.setItemListener(new AbstractSpinerAdapter.IOnItemSelectListener() {
                @Override
                public void onItemClick(int pos) {
                    currentPlateNo=carList.get(pos);
                    tv_car_code_select.setText(carList.get(pos));
                    page=1;
                    totalPages=0;
                    s_car_lock.setOnCheckedChangeListener(null);
                    carRecordArrayList.clear();
                    carRecordListAdapter.notifyDataSetChanged();
                    getAlarmCar();
                    initCarCross();
                }
            });
        }else{
            s_car_lock.setClickable(false);
            showShortToast("没有获得车辆信息");
        }
    }

    /**
     *
     */
    private void getAlarmCar(){
        Map<String, Object> requestMap=new HashMap<>();
        requestMap.put("searchKey",currentPlateNo);
        requestMap.put("pageNo",1);
        requestMap.put("pageSize",100);
        XUtils.PostJson(BASE_URL+IOT+"community/api/car/v1/alarm/list/"+phaseId,requestMap,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtil.d(result);
                BaseEntity<HikAlarmCarlist> baseEntity= JsonParse.parse(result,HikAlarmCarlist.class);
                if(baseEntity.isSuccess()){
                    if(baseEntity.getResult().getList().size()>0){
                        currentAlarmSyscode=baseEntity.getResult().getList().get(0).getAlarmSyscode();
                        tv_car_lock_flag.setText("锁车");
                        s_car_lock.setChecked(false);
                        Drawable drawable=getResources().getDrawable(R.drawable.icon_car_lock);
                        drawable.setBounds(0, 0, 68, 68);
                        tv_car_lock_flag.setCompoundDrawables(drawable,null,null,null);
                    }else{
                        currentAlarmSyscode="";
                        tv_car_lock_flag.setText("解锁");
                        s_car_lock.setChecked(true);
                        Drawable drawable=getResources().getDrawable(R.drawable.icon_car_unlock);
                        drawable.setBounds(0, 0, 68, 68);
                        tv_car_lock_flag.setCompoundDrawables(drawable,null,null,null);
                    }
                    s_car_lock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if(isChecked){
                                alarmCarDeletion();
                            }else{
                                alarmCarAddition();
                            }
                        }
                    });
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
            }
        });
    }

    /**
     * 初始化出入记录
     */
    private void initCarCross(){
        //下拉刷新与上拉加载
        ptrlv_car_record_list.setMode(PullToRefreshBase.Mode.BOTH);
        // 设置刷新文本说明(展开刷新栏前)为false,true返回设置上拉的。
        ptrlv_car_record_list.getLoadingLayoutProxy(false, true).setLastUpdatedLabel("");
        ptrlv_car_record_list.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在加载...");
        ptrlv_car_record_list.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载更多");
        ptrlv_car_record_list.getLoadingLayoutProxy(false, true).setReleaseLabel("释放开始加载");
        // true,false返回设置下拉
        ptrlv_car_record_list.getLoadingLayoutProxy(true, false).setRefreshingLabel("正在刷新...");
        ptrlv_car_record_list.getLoadingLayoutProxy(true, false).setPullLabel("下拉刷新");
        ptrlv_car_record_list.getLoadingLayoutProxy(true, false).setReleaseLabel("释放开始刷新");

        ptrlv_car_record_list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                startTime = "";
                endTime = "";
                page = 1;
                isFirstLoading = true;
                getCarCrossRecord();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (page >= totalPages) {
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ptrlv_car_record_list.onRefreshComplete();
                        }
                    }, 1000);
                    Tools.showPrompt("已加载完所有内容");
                } else {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            page++;
                            isFirstLoading = false;
                            getCarCrossRecord();
                        }
                    });
                }
            }
        });

        carRecordListAdapter = new CarRecordListAdapter(this, carRecordArrayList);
        ptrlv_car_record_list.setAdapter(carRecordListAdapter);
        getCarCrossRecord();
    }
    /**
     * 车辆布控
     */
    private void alarmCarAddition(){
        Map<String, Object> requestMap=new HashMap<>();
        requestMap.put("plateNo",currentPlateNo);
        XUtils.PostJson(BASE_URL+IOT+"community/api/car/v1/alarm/"+phaseId,requestMap,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtil.d(result);
                BaseEntity<HikAlarmAddition> baseEntity= JsonParse.parse(result,HikAlarmAddition.class);
                if(baseEntity.isSuccess()){
                    currentAlarmSyscode=baseEntity.getResult().getAlarmSyscode();
                    tv_car_lock_flag.setText("锁车");
                    Drawable drawable=getResources().getDrawable(R.drawable.icon_car_lock);
                    drawable.setBounds(0, 0, 68, 68);
                    tv_car_lock_flag.setCompoundDrawables(drawable,null,null,null);
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
            }
        });
    }

    /**
     * 车辆取消布控
     */
    private void alarmCarDeletion(){
        Map<String, Object> requestMap=new HashMap<>();
        requestMap.put("alarmSyscodes",currentAlarmSyscode);
        XUtils.PostJson(BASE_URL+IOT+"community/api/car/v1/alarm/deletion/"+phaseId,requestMap,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtil.d(result);
                BaseEntity<CarListEntity> baseEntity= JsonParse.parse(result,CarListEntity.class);
                if(baseEntity.isSuccess()){
                    currentAlarmSyscode="";
                    tv_car_lock_flag.setText("解锁");
                    Drawable drawable=getResources().getDrawable(R.drawable.icon_car_unlock);
                    drawable.setBounds(0, 0, 68, 68);
                    tv_car_lock_flag.setCompoundDrawables(drawable,null,null,null);
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
            }
        });
    }
    /**
     * 车辆出入记录
     */
    private void getCarCrossRecord(){
        Map<String,Object> requestMap=new HashMap<>();
        requestMap.put("plateNo",currentPlateNo);
        requestMap.put("pageNo",page);
        requestMap.put("pageSize",pageSize);
        XUtils.PostJson(BASE_URL+IOT+"community/api/car/v1/cross/list/"+phaseId,requestMap,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtil.d(result);
                BaseEntity<HikCarCrossRecordList> baseEntity= JsonParse.parse(result,HikCarCrossRecordList.class);
                if (ptrlv_car_record_list != null) {
                    ptrlv_car_record_list.onRefreshComplete();
                }
                if(baseEntity.isSuccess()){
                    if (isFirstLoading) {
                        carRecordArrayList.clear();
                    }
                    totalPages=baseEntity.getResult().getTotal()/10+(baseEntity.getResult().getTotal()%10>0?1:0);
                    carRecordArrayList.addAll(baseEntity.getResult().getList());
                    carRecordListAdapter.notifyDataSetChanged();
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
            }
        });
    }

    @Event({R.id.iv_title_left,R.id.tv_car_code_select})
    private void onClickEvent(View v){
        switch (v.getId()){
            case R.id.iv_title_left:
                finish();
                break;
            case R.id.tv_car_code_select:
                if(mpopwindow==null){
                    showShortToast("没有获得车辆信息");
                }else{
                    mpopwindow.setWidth(tv_car_code_select.getWidth());
                    if (carList.size() < 6) {
                        mpopwindow.setHeight(tv_car_code_select.getHeight() * carList.size());
                    } else {
                        mpopwindow.setHeight(tv_car_code_select.getHeight() * 4);
                    }
                    mpopwindow.showAsDropDown(tv_car_code_select);
                }
                break;
        }
    }
}
