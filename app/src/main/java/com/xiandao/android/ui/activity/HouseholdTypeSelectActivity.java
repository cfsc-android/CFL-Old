package com.xiandao.android.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiandao.android.R;
import com.xiandao.android.entity.smart.HouseholdType;
import com.xiandao.android.ui.BaseActivity;

import org.xutils.event.annotation.ContentView;
import org.xutils.event.annotation.Event;
import org.xutils.event.annotation.ViewInject;

@ContentView(R.layout.activity_household_type_select)
public class HouseholdTypeSelectActivity extends BaseActivity {
    @ViewInject(R.id.tv_title_name)
    private TextView tv_title_name;

    @ViewInject(R.id.household_type_property_iv)
    private ImageView household_type_property_iv;


    @ViewInject(R.id.household_type_family_iv)
    private ImageView household_type_family_iv;


    @ViewInject(R.id.household_type_rent_iv)
    private ImageView household_type_rent_iv;

    private HouseholdType type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tv_title_name.setText("选择身份");
        type=HouseholdType.Property;
        household_type_property_iv.setImageResource(R.drawable.single_selected);
        household_type_family_iv.setImageResource(R.drawable.single_unselected);
        household_type_rent_iv.setImageResource(R.drawable.single_unselected);

    }

    @Event({R.id.iv_title_left,R.id.household_type_select_btn,R.id.household_type_property_ll,R.id.household_type_family_ll,R.id.household_type_rent_ll})
    private void onClickEvent(View v){
        switch (v.getId()){
            case R.id.iv_title_left:
                finish();
                break;
            case R.id.household_type_select_btn:
                Bundle bundle=new Bundle();
                bundle.putString("roomId",getIntent().getExtras().getString("roomId"));
                bundle.putString("type",type.getType());
                openActivity(HouseholdAuditActivity.class,bundle);
                break;
            case R.id.household_type_property_ll:
                type=HouseholdType.Property;
                household_type_property_iv.setImageResource(R.drawable.single_selected);
                household_type_family_iv.setImageResource(R.drawable.single_unselected);
                household_type_rent_iv.setImageResource(R.drawable.single_unselected);
                break;
            case R.id.household_type_family_ll:
                type=HouseholdType.Family;
                household_type_property_iv.setImageResource(R.drawable.single_unselected);
                household_type_family_iv.setImageResource(R.drawable.single_selected);
                household_type_rent_iv.setImageResource(R.drawable.single_unselected);
                break;
            case R.id.household_type_rent_ll:
                type=HouseholdType.Rent;
                household_type_property_iv.setImageResource(R.drawable.single_unselected);
                household_type_family_iv.setImageResource(R.drawable.single_unselected);
                household_type_rent_iv.setImageResource(R.drawable.single_selected);
                break;
        }
    }
}
