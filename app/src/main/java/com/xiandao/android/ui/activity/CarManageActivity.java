package com.xiandao.android.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pgyersdk.update.PgyUpdateManager;
import com.xiandao.android.R;
import com.xiandao.android.adapter.CarManageListAdapter;
import com.xiandao.android.entity.CarChargeInfo;
import com.xiandao.android.entity.CarManageEntity;
import com.xiandao.android.entity.HikBaseEntity;
import com.xiandao.android.entity.eventbus.EventBusMessage;
import com.xiandao.android.entity.smart.BaseEntity;
import com.xiandao.android.entity.smart.CarEntity;
import com.xiandao.android.entity.smart.CarListEntity;
import com.xiandao.android.http.JsonParse;
import com.xiandao.android.http.MyCallBack;
import com.xiandao.android.http.XUtils;
import com.xiandao.android.net.ApiHttpResult;
import com.xiandao.android.net.HttpUtils;
import com.xiandao.android.ui.BaseActivity;
import com.xiandao.android.utils.Constants;
import com.xiandao.android.utils.FileManagement;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.util.LogUtil;
import org.xutils.event.annotation.ContentView;
import org.xutils.event.annotation.Event;
import org.xutils.event.annotation.ViewInject;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.xiandao.android.base.Config.BASE_URL;
import static com.xiandao.android.base.Config.BASIC;


@ContentView(R.layout.activity_car_manage)
public class CarManageActivity extends BaseActivity {
    @ViewInject(R.id.tv_title_name)
    private TextView tv_title_name;
    @ViewInject(R.id.iv_title_right)
    private ImageView iv_title_right;
    @ViewInject(R.id.lv_car_manage_list)
    private SwipeMenuListView lv_car_manage_list;

    private CarManageListAdapter carManageListAdapter;
    private ArrayList<CarEntity> carManageList=new ArrayList<>();

    private int pageNo=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tv_title_name.setText("车辆管理");
        iv_title_right.setImageResource(R.drawable.btn_home_add);
        iv_title_right.setVisibility(View.VISIBLE);
        EventBus.getDefault().register(this);
        initListView();
    }

    private void initListView(){
        carManageListAdapter = new CarManageListAdapter(this,carManageList);
        lv_car_manage_list.setAdapter(carManageListAdapter);
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getApplicationContext());
                openItem.setBackground(new ColorDrawable(Color.rgb(0xF9,0x3F, 0x25)));
                openItem.setWidth(dp2px(90));
                openItem.setTitle("删除");
                openItem.setTitleSize(18);
                openItem.setTitleColor(Color.WHITE);
                menu.addMenuItem(openItem);
            }
        };
        // 为ListView设置创建器
        lv_car_manage_list.setMenuCreator(creator);

        // 第2步：为ListView设置菜单项点击监听器，来监听菜单项的点击事件
        lv_car_manage_list.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
                new AlertDialog.Builder(CarManageActivity.this)
                        .setTitle("删除车辆")
                        .setMessage("确认要删除车辆？")
                        .setCancelable(true)
                        .setNegativeButton(
                                "确认删除",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        deleteCar(position);
                                    }
                                }).show();
                return false;
            }
        });

        lv_car_manage_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle=new Bundle();
                bundle.putSerializable("car",carManageList.get(position));
                openActivity(CarManageAddActivity.class,bundle);
            }
        });
        getCarManageList();
    }

    private void deleteCar(int position){
//        Map<String,Object> requestMap=new HashMap<>();
//        requestMap.put("id",carManageList.get(position).getId());
//        XUtils.Post(Constants.HOST+"/vehicleinfo/delete.action",requestMap,new MyCallBack<String>(){
//            @Override
//            public void onSuccess(String result) {
//                super.onSuccess(result);
//                Log.e("result",result);
//                try {
//                    JSONObject jsonObject = new JSONObject(result);
//                    if(jsonObject.getInt("resultCode")==200) {
//                        getCarManageList();
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
//            }
//        });
    }

    private void getCarManageList(){
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
                    carManageList.clear();
                    carManageList.addAll(baseEntity.getResult().getData());
                    carManageListAdapter.notifyDataSetChanged();
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
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(EventBusMessage message){
        if("carAdd".equals(message.getMessage())){
            getCarManageList();
        }
    }

    @Event({R.id.iv_title_left,R.id.iv_title_right})
    private void onClickEvent(View v){
        switch (v.getId()){
            case R.id.iv_title_left:
                finish();
                break;
            case R.id.iv_title_right:
                openActivity(CarManageAddActivity.class);
                break;
        }
    }


    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_left) {
            lv_car_manage_list.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
            return true;
        }

        if (id == R.id.action_right) {
            lv_car_manage_list.setSwipeDirection(SwipeMenuListView.DIRECTION_RIGHT);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
