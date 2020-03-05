package com.xiandao.android.ui.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiandao.android.R;
import com.xiandao.android.adapter.DisplayImageGridViewAdapter;
import com.xiandao.android.entity.LoginUserEntity;
import com.xiandao.android.entity.RepairsDetailEntity;
import com.xiandao.android.entity.RepairsUserInfoEntity;
import com.xiandao.android.net.ApiHttpResult;
import com.xiandao.android.net.HttpUtils;
import com.xiandao.android.net.NetUtil;
import com.xiandao.android.ui.BaseLazyFragment;
import com.xiandao.android.ui.activity.LoginActivity;
import com.xiandao.android.ui.activity.ShowUrlPhotoActivity;
import com.xiandao.android.utils.FileManagement;
import com.xiandao.android.utils.PhoneUtils;
import com.xiandao.android.utils.PhotoUtils;
import com.xiandao.android.utils.Tools;

import org.xutils.x;

import java.util.ArrayList;

/**
 * 此类描述的是:我的报修详情--确认费用fragment
 *
 * @author TanYong
 *         create at 2017/5/9 16:32
 */
public class RepairsRecognitionOfExpenseFragment extends BaseLazyFragment implements View.OnClickListener {

    private String repairsId;//报修ID
    private LoginUserEntity loginUserEntity;

    private ImageView iv_repairs_user_hear_image;//报修人头像
    private TextView tv_repairs_name;//报修人姓名
    private TextView tv_repairs_at_property;//报修人所在物业
    private ImageView iv_repairs_user_mobile;//拨打电话
    private TextView tv_repairs_des;//报修内容
    private TextView tv_repairs_date_time;//报修时间
    private GridView gv_repairs_detail_images;//报修图片
    private TextView tv_manual_cost;//人工费用:（0）元
    private TextView tv_material_cost;//物料费用:（0）元
    private TextView tv_total_cost;//费用总计:（0）元
    private TextView tv_repairs_type;//报修类型:环境卫生
    private TextView tv_repairs_status;//处理状态
    private TextView tv_dispose_person;//处 理 人  :张三
    private TextView tv_dispose_time;//处理时间:2017-3-31 15:11:24
    private TextView tv_dispose_des;//处理内容:清扫C座楼梯沉积

    private DisplayImageGridViewAdapter displayImageGridViewAdapter;
    private ArrayList<String> imageUrlList = new ArrayList<>();

    @Override
    protected void injectView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_repairs_recognition_of_expense, null);
        x.view().inject(this, view);
        setContentView(view);
        init(view);
    }

    private void init(View view) {
        loginUserEntity = FileManagement.getLoginUserEntity();
        initView(view);
        Bundle bundle = getArguments();
        if (null != bundle) {
            repairsId = bundle.getString("repairsId");
        }
        getRepairsDetail();
    }

    private void initView(View view) {
        iv_repairs_user_hear_image = (ImageView) view.findViewById(R.id.iv_repairs_user_hear_image);
        tv_repairs_name = (TextView) view.findViewById(R.id.tv_repairs_name);
        tv_repairs_at_property = (TextView) view.findViewById(R.id.tv_repairs_at_property);
        iv_repairs_user_mobile = (ImageView) view.findViewById(R.id.iv_repairs_user_mobile);
        tv_repairs_des = (TextView) view.findViewById(R.id.tv_repairs_des);
        tv_repairs_date_time = (TextView) view.findViewById(R.id.tv_repairs_date_time);
        gv_repairs_detail_images = (GridView) view.findViewById(R.id.gv_repairs_detail_images);
        tv_manual_cost = (TextView) view.findViewById(R.id.tv_manual_cost);
        tv_material_cost = (TextView) view.findViewById(R.id.tv_material_cost);
        tv_total_cost = (TextView) view.findViewById(R.id.tv_total_cost);
        tv_repairs_type = (TextView) view.findViewById(R.id.tv_repairs_type);
        tv_repairs_status = (TextView) view.findViewById(R.id.tv_repairs_status);
        tv_dispose_person = (TextView) view.findViewById(R.id.tv_dispose_person);
        tv_dispose_time = (TextView) view.findViewById(R.id.tv_dispose_time);
        tv_dispose_des = (TextView) view.findViewById(R.id.tv_dispose_des);

        displayImageGridViewAdapter = new DisplayImageGridViewAdapter(getActivity(), imageUrlList);
        gv_repairs_detail_images.setAdapter(displayImageGridViewAdapter);

        gv_repairs_detail_images.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), ShowUrlPhotoActivity.class);
                intent.putStringArrayListExtra("list", imageUrlList);
                intent.putExtra("position", i);
                getActivity().startActivity(intent);
            }
        });
    }

    private void getRepairsDetail() {
        startProgressDialog("");
        ApiHttpResult.getRepairsDetail(getActivity(), new String[]{"appMobile", "repairsId", "userId"},
                new Object[]{loginUserEntity.getMobileNumber(), repairsId, loginUserEntity.getUserId()},
                new HttpUtils.DataCallBack<Object>() {
                    @Override
                    public void callBack(Object o) {
                        stopProgressDialog();
                        if (o != null) {
                            RepairsDetailEntity repairsDetailEntity = (RepairsDetailEntity) o;
                            final RepairsUserInfoEntity repairsUserInfo = repairsDetailEntity.getUserInfo();
                            PhotoUtils.loadRoundImage(repairsUserInfo.getUserHearImageUrl(), iv_repairs_user_hear_image, 200, 2);
                            tv_repairs_name.setText(repairsUserInfo.getUserName());
                            tv_repairs_at_property.setText(repairsUserInfo.getAtProperty());
                            iv_repairs_user_mobile.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String mobileNo = repairsUserInfo.getUserMobileNo();

                                    int checkCallPhonePermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE);
                                    if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 222);
                                        return;
                                    } else {
                                        PhoneUtils.callPhone(getActivity(), mobileNo);
                                    }
                                }
                            });
                            tv_repairs_des.setText(repairsDetailEntity.getRepairsDes());
                            tv_repairs_date_time.setText(repairsDetailEntity.getRepairsDateTime());
                            tv_manual_cost.setText("人工费用:（" + repairsDetailEntity.getManualCost() + "）元");
                            tv_material_cost.setText("物料费用:（" + repairsDetailEntity.getMaterialCost() + "）元");
                            tv_total_cost.setText("费用总计:（" + repairsDetailEntity.getTotalCost() + "）元");
                            tv_repairs_type.setText("报修类型:" + repairsDetailEntity.getRepairsType());
                            tv_repairs_status.setText(repairsDetailEntity.getRepairsStatus());
                            tv_dispose_person.setText("处 理 人  :" + repairsDetailEntity.getDisposePerson());
                            tv_dispose_time.setText("处理时间:" + repairsDetailEntity.getDisposeTime());
                            tv_dispose_des.setText("处理内容:" + repairsDetailEntity.getDisposeDes());

//                            if (imageUrlList.size() > 0)
//                                imageUrlList.clear();
//                            imageUrlList.addAll(repairsDetailEntity.getPiclist());
//                            displayImageGridViewAdapter.notifyDataSetChanged();
                        } else {
                            NetUtil.toNetworkSetting(getActivity());
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {

    }

}
