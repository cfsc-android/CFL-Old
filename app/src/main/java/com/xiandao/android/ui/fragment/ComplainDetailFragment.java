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
import com.xiandao.android.entity.ComplainDetailEntity;
import com.xiandao.android.entity.DetailUserInfoEntity;
import com.xiandao.android.entity.ImageUrlEntity;
import com.xiandao.android.entity.LoginUserEntity;
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
 * 此类描述的是:投诉详情fragment
 *
 * @author TanYong
 *         create at 2017/6/2 10:18
 */
public class ComplainDetailFragment extends BaseLazyFragment {
    private String complainId;//投诉ID
    private LoginUserEntity loginUserEntity;

    private ImageView iv_user_hear_image;
    private TextView tv_name;
    private TextView tv_at_property;
    private ImageView iv_user_mobile;

    private TextView tv_complain_type;
    private TextView tv_complain_status;
    private TextView tv_complain_des;
    private TextView tv_complain_date_time;
    private GridView gv_complain_detail_images;

    private DisplayImageGridViewAdapter complainDetailImagesAdapter;
    private ArrayList<String> complainDetailImageUrlList = new ArrayList<>();


    @Override
    protected void injectView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_complain_detail, null);
        x.view().inject(this, view);
        setContentView(view);
        loginUserEntity = FileManagement.getLoginUserEntity();
        initView(view);
        Bundle bundle = getArguments();
        if (null != bundle) {
            complainId = bundle.getString("complainId");
        }
        getComplainDetail();
    }

    private void initView(View view) {
        iv_user_hear_image = (ImageView) view.findViewById(R.id.iv_user_hear_image);
        tv_name = (TextView) view.findViewById(R.id.tv_name);
        tv_at_property = (TextView) view.findViewById(R.id.tv_at_property);
        iv_user_mobile = (ImageView) view.findViewById(R.id.iv_user_mobile);

        tv_complain_type = (TextView) view.findViewById(R.id.tv_complain_type);
        tv_complain_status = (TextView) view.findViewById(R.id.tv_complain_status);
        tv_complain_des = (TextView) view.findViewById(R.id.tv_complain_des);
        tv_complain_date_time = (TextView) view.findViewById(R.id.tv_complain_date_time);
        gv_complain_detail_images = (GridView) view.findViewById(R.id.gv_complain_detail_images);

        complainDetailImagesAdapter = new DisplayImageGridViewAdapter(getActivity(), complainDetailImageUrlList);
        gv_complain_detail_images.setAdapter(complainDetailImagesAdapter);
        gv_complain_detail_images.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), ShowUrlPhotoActivity.class);
                intent.putStringArrayListExtra("list", complainDetailImageUrlList);
                intent.putExtra("position", i);
                getActivity().startActivity(intent);
            }
        });
    }


    /**
     * @author TanYong
     * create at 2017/6/2 13:30
     * TODO：获取投诉详情
     */
    private void getComplainDetail() {
        startProgressDialog("");
        ApiHttpResult.getComplainDetail(getActivity(), new String[]{"appMobile", "complainId",},
                new Object[]{loginUserEntity.getMobileNumber(), complainId},
                new HttpUtils.DataCallBack<Object>() {
                    @Override
                    public void callBack(Object o) {
                        stopProgressDialog();
                        if (o != null) {
                            ComplainDetailEntity complainDetailEntity = (ComplainDetailEntity) o;
                            if (complainDetailEntity.getResultCode() != null && complainDetailEntity.getResultCode().equals("0")) {
                                final DetailUserInfoEntity userInfo = complainDetailEntity.getUserInfo();
                                PhotoUtils.loadRoundImage(userInfo.getUserHearImageUrl(), iv_user_hear_image, 200,2);
                                tv_name.setText(Tools.getStringValue(userInfo.getUserName()));
                                tv_at_property.setText(Tools.getStringValue(userInfo.getAtProperty()));
                                iv_user_mobile.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        int checkCallPhonePermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE);
                                        if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 222);
                                            return;
                                        } else {
                                            PhoneUtils.callPhone(getActivity(), userInfo.getUserMobileNo());
                                        }
                                    }
                                });
                                tv_complain_type.setText(Tools.getStringValue(complainDetailEntity.getComplainType()));
                                tv_complain_des.setText(Tools.getStringValue(complainDetailEntity.getComplainDes()));
                                tv_complain_date_time.setText(Tools.getStringValue(complainDetailEntity.getComplainDateTime()));
                                tv_complain_status.setText(Tools.getStringValue(complainDetailEntity.getComplainStatus()));

                                ArrayList<ImageUrlEntity> complainImageUrlList = complainDetailEntity.getComplainImageUrlList();
                                if (null != complainImageUrlList && complainImageUrlList.size() > 0) {
                                    for (ImageUrlEntity imageUrlEntity : complainImageUrlList) {
                                        complainDetailImageUrlList.add(imageUrlEntity.getUrl());
                                    }
                                    gv_complain_detail_images.setVisibility(View.VISIBLE);
                                    complainDetailImagesAdapter.notifyDataSetChanged();
                                }

                            } else {
                                Tools.showPrompt(complainDetailEntity.getMsg());
                            }
                        } else {
                            NetUtil.toNetworkSetting(getActivity());
                        }
                    }
                });
    }


}
