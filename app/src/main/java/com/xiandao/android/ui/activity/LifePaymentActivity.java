package com.xiandao.android.ui.activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.xiandao.android.R;
import com.xiandao.android.ui.BaseActivity;

import org.xutils.event.annotation.ContentView;
import org.xutils.event.annotation.Event;
import org.xutils.event.annotation.ViewInject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ContentView(R.layout.activity_life_payment)
public class LifePaymentActivity extends BaseActivity {
    @ViewInject(R.id.tv_title_name)
    private TextView tv_title_name;
    @ViewInject(R.id.gv_life_payment_list)
    private GridView gv_life_payment_list;

    private List<Map<String, Object>> data_list;
    private SimpleAdapter sim_adapter;
    // 图片封装为一个数组
    private int[] icon = { R.drawable.pay_property,R.drawable.pay_parking,R.drawable.pay_water,R.drawable.pay_power,
    R.drawable.pay_inter,R.drawable.pay_tel,R.drawable.pay_gas,R.drawable.pay_cable};
    private String[] iconName = { "物业费", "停车缴费", "水费", "电费", "宽带", "固话", "燃气费","有线电视"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tv_title_name.setText("生活缴费");
        data_list=initData();
        String [] from ={"image","text"};
        int [] to = {R.id.iv_item_life_payment_list_icon,R.id.tv_item_life_payment_list_title};
        sim_adapter = new SimpleAdapter(this, data_list, R.layout.item_life_payment_list, from, to);
        //配置适配器
        gv_life_payment_list.setAdapter(sim_adapter);
        gv_life_payment_list.setSelector(new ColorDrawable(Color.TRANSPARENT));
        gv_life_payment_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle a_bundle=new Bundle();
                if("水费".equals(data_list.get(position).get("text").toString())){
                    a_bundle.putString("title","水费查询");
                    a_bundle.putString("url","https://billcloud.unionpay.com/ccfront/loc/CH5512/search?category=D4");
                    openActivity(NewsInfoActivity.class,a_bundle);
                }else if("电费".equals(data_list.get(position).get("text").toString())){
                    a_bundle.putString("title","电费查询");
                    a_bundle.putString("url","https://billcloud.unionpay.com/ccfront/loc/CH5512/search?category=D1");
                    openActivity(NewsInfoActivity.class,a_bundle);
                }else if("停车缴费".equals(data_list.get(position).get("text").toString())){
                    openActivity(ParkingPaymentActivity.class);
                }else{
                    showShortToast("暂未集成，敬请期待");
                }
            }
        });
    }
    @Event({R.id.iv_title_left})
    private void onClickEvent(View v){
        switch (v.getId()){
            case R.id.iv_title_left:
                finish();
                break;
        }
    }

    private List<Map<String, Object>> initData(){
        List<Map<String, Object>> data=new ArrayList<>();
        for(int i=0;i<icon.length;i++){
            Map<String, Object> map = new HashMap<>();
            map.put("image", icon[i]);
            map.put("text", iconName[i]);
            data.add(map);
        }
        return data;
    }
}
