package com.xiandao.android.ui.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.xiandao.android.R;
import com.xiandao.android.adapter.HouseManageListAdapter;
import com.xiandao.android.entity.smart.BaseEntity;
import com.xiandao.android.entity.smart.RoomEntity;
import com.xiandao.android.entity.smart.UserInfoEntity;
import com.xiandao.android.http.JsonParse;
import com.xiandao.android.http.MyCallBack;
import com.xiandao.android.http.XUtils;
import com.xiandao.android.ui.BaseLazyFragment;
import com.xiandao.android.utils.Tools;

import org.xutils.common.util.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.xiandao.android.base.Config.BASE_URL;
import static com.xiandao.android.base.Config.BASIC;

/**
 * Created by Loong on 2020/2/12.
 * Version: 1.0
 * Describe: 首页
 */
public class RoomDelectFragment extends BaseLazyFragment {
    //@ViewInject(R.id.lv_house_manage_list)
    private SwipeMenuListView lv_house_manage_list;
    private HouseManageListAdapter houseManageListAdapter;
    private ArrayList<RoomEntity> roomManageList=new ArrayList<>();

    @Override
    protected void injectView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_house_others, null);
        setContentView(view);
        initListView(view);
    }

    private void initListView(View view){
        lv_house_manage_list = view.findViewById(R.id.lv_room_delect);
        UserInfoEntity userInfoEntity= new UserInfoEntity();

        houseManageListAdapter = new HouseManageListAdapter(getContext(), roomManageList);
        lv_house_manage_list.setAdapter(houseManageListAdapter);

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getContext());
                openItem.setBackground(new ColorDrawable(Color.rgb(0xF9,0x3F, 0x25)));
                //openItem.setWidth(dp2px(90));
                openItem.setTitle("删除");
                openItem.setTitleSize(18);
                openItem.setTitleColor(Color.WHITE);
                menu.addMenuItem(openItem);
            }
        };
//        // 为ListView设置创建器
//        lv_house_manage_list.setMenuCreator(creator);
//
//        // 第2步：为ListView设置菜单项点击监听器，来监听菜单项的点击事件
//        lv_house_manage_list.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
//                new AlertDialog.Builder(getContext())
//                        .setTitle("删除车辆")
//                        .setMessage("确认要删除车辆？")
//                        .setCancelable(true)
//                        .setNegativeButton(
//                                "确认删除",
//                                new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//
//                                    }
//                                }).show();
//                return false;
//            }
//        });

//        lv_house_manage_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Bundle bundle=new Bundle();
//                bundle.putSerializable("car",carManageList.get(position));
//                openActivity(CarManageAddActivity.class,bundle);
//            }
//        });
        getHouseManageList();
    }

    private void getHouseManageList(){
        startProgressDialog("");
        Map<String,String> requestMap=new HashMap<>();
        requestMap.put("pageNo","1");
        requestMap.put("pageSize","10");
        requestMap.put("phoneNumber","18073667979");//---------------------先写死做测试
        XUtils.Get(BASE_URL+BASIC+"basic/householdInfo/phone",requestMap,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtil.d(result);

                ;                BaseEntity<UserInfoEntity> baseEntity = JsonParse.parse(result,UserInfoEntity.class);
                if(baseEntity.isSuccess()){
                    roomManageList.clear();
                    roomManageList.addAll(baseEntity.getResult().getRoomList());
                    houseManageListAdapter.notifyDataSetChanged();
                }else{
                    Tools.showPrompt(baseEntity.getMessage());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                Tools.showPrompt(ex.getMessage());
            }

            @Override
            public void onFinished() {
                super.onFinished();
                stopProgressDialog();
            }
        });

    }

}
