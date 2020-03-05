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
import android.widget.Button;
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
import com.xiandao.android.ui.activity.ComplainCommentActivity;
import com.xiandao.android.ui.activity.LoginActivity;
import com.xiandao.android.ui.activity.ShowUrlPhotoActivity;
import com.xiandao.android.utils.FileManagement;
import com.xiandao.android.utils.PhoneUtils;
import com.xiandao.android.utils.PhotoUtils;
import com.xiandao.android.utils.Tools;

import org.xutils.x;

import java.util.ArrayList;

/**
 * 此类描述的是:投诉整改结果fragment
 *
 * @author TanYong
 *         create at 2017/6/2 10:18
 */
public class ComplainToCommentFragment extends BaseLazyFragment implements View.OnClickListener {
    private ImageView iv_user_hear_image;//业主头像
    private TextView tv_name;//业主姓名
    private TextView tv_at_property;//业主所属物业
    private ImageView iv_user_mobile;//业主拨打电话图标

    private TextView tv_complain_type;//投诉类型
    private TextView tv_complain_status;//投诉状态
    private TextView tv_complain_detail;//投诉详情
    private TextView tv_complain_datetime;//投诉时间

    private GridView gv_complain_image;//投诉图片
    private ArrayList<String> complainImageList = new ArrayList<>();// 显示投诉图片URL集合
    private DisplayImageGridViewAdapter complainImageGridViewAdapter;

    private TextView tv_complain_screen_des;//现场检视说明
    private TextView tv_complain_screen_datetime;//现场检视时间
    private GridView gv_complain_screen_image;//现场检视图片
    private ArrayList<String> screenImageList = new ArrayList<>();// 显示现场检视图片URL集合
    private DisplayImageGridViewAdapter screenImageGridViewAdapter;

    private TextView tv_complain_programe_des;
    private TextView tv_complain_programe_datetime;

    private TextView tv_complain_dispose_person;
    private TextView tv_complain_dispose_des;
    private TextView tv_complain_dispose_datetime;
    private GridView gv_complain_dispose_images;
    private ArrayList<String> disposeImageList = new ArrayList<>();// 显示现场检视图片URL集合
    private DisplayImageGridViewAdapter disposeImageGridViewAdapter;

    private Button btn_complain_to_comment;

    private LoginUserEntity loginUserEntity;
    private String complainId;//投诉ID
    private String taskId;


    @Override
    protected void injectView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_complain_to_commennt, null);
        x.view().inject(this, view);
        setContentView(view);

        loginUserEntity = FileManagement.getLoginUserEntity();
        Bundle bundle = getArguments();
        if (bundle != null) {
            complainId = bundle.getString("complainId");
            taskId = bundle.getString("taskId");
            initView(view);
            queryComplainDetail();
        }
    }

    private void initView(View view) {
        iv_user_hear_image = (ImageView) view.findViewById(R.id.iv_user_hear_image);
        tv_name = (TextView) view.findViewById(R.id.tv_name);
        tv_at_property = (TextView) view.findViewById(R.id.tv_at_property);
        iv_user_mobile = (ImageView) view.findViewById(R.id.iv_user_mobile);

        tv_complain_type = (TextView) view.findViewById(R.id.tv_complain_type);
        tv_complain_status = (TextView) view.findViewById(R.id.tv_complain_status);
        tv_complain_detail = (TextView) view.findViewById(R.id.tv_complain_detail);
        tv_complain_datetime = (TextView) view.findViewById(R.id.tv_complain_datetime);

        gv_complain_image = (GridView) view.findViewById(R.id.gv_complain_image);
        complainImageGridViewAdapter = new DisplayImageGridViewAdapter(getActivity(), complainImageList);
        gv_complain_image.setAdapter(complainImageGridViewAdapter);

        gv_complain_image.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), ShowUrlPhotoActivity.class);
                intent.putStringArrayListExtra("list", complainImageList);
                intent.putExtra("position", i);
                getActivity().startActivity(intent);
            }
        });

        tv_complain_screen_des = (TextView) view.findViewById(R.id.tv_complain_screen_des);
        tv_complain_screen_datetime = (TextView) view.findViewById(R.id.tv_complain_screen_datetime);
        gv_complain_screen_image = (GridView) view.findViewById(R.id.gv_complain_screen_image);
        screenImageGridViewAdapter = new DisplayImageGridViewAdapter(getActivity(), screenImageList);
        gv_complain_screen_image.setAdapter(screenImageGridViewAdapter);

        gv_complain_screen_image.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), ShowUrlPhotoActivity.class);
                intent.putStringArrayListExtra("list", screenImageList);
                intent.putExtra("position", i);
                getActivity().startActivity(intent);
            }
        });

        tv_complain_programe_des = (TextView) view.findViewById(R.id.tv_complain_programe_des);
        tv_complain_programe_datetime = (TextView) view.findViewById(R.id.tv_complain_programe_datetime);

        tv_complain_dispose_person = (TextView) view.findViewById(R.id.tv_complain_dispose_person);
        tv_complain_dispose_des = (TextView) view.findViewById(R.id.tv_complain_dispose_des);
        tv_complain_dispose_datetime = (TextView) view.findViewById(R.id.tv_complain_dispose_datetime);
        gv_complain_dispose_images = (GridView) view.findViewById(R.id.gv_complain_dispose_images);
        disposeImageGridViewAdapter = new DisplayImageGridViewAdapter(getActivity(), disposeImageList);
        gv_complain_dispose_images.setAdapter(disposeImageGridViewAdapter);

        gv_complain_dispose_images.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), ShowUrlPhotoActivity.class);
                intent.putStringArrayListExtra("list", disposeImageList);
                intent.putExtra("position", i);
                getActivity().startActivity(intent);
            }
        });

        btn_complain_to_comment = (Button) view.findViewById(R.id.btn_complain_to_comment);
        btn_complain_to_comment.setOnClickListener(this);

    }


    /**
     * @author TanYong
     * create at 2017/6/11 23:25
     * TODO：查看投诉详情
     */
    private void queryComplainDetail() {
        startProgressDialog("");
        ApiHttpResult.getComplainDetail(getActivity(), new String[]{"appMobile", "complainId"},
                new Object[]{loginUserEntity.getMobileNumber(), complainId}, new HttpUtils.DataCallBack<Object>() {

                    @Override
                    public void callBack(Object o) {
                        stopProgressDialog();
                        if (o != null) {
                            ComplainDetailEntity complainDetailEntity = (ComplainDetailEntity) o;
                            if (complainDetailEntity.getResultCode() != null && complainDetailEntity.getResultCode().equals("0")) {
                                final DetailUserInfoEntity userInfo = complainDetailEntity.getUserInfo();
                                PhotoUtils.loadRoundImage(userInfo.getUserHearImageUrl(), iv_user_hear_image, 200, 2);
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
                                tv_complain_detail.setText(Tools.getStringValue(complainDetailEntity.getComplainDes()));
                                tv_complain_datetime.setText(Tools.getStringValue(complainDetailEntity.getComplainDateTime()));
                                tv_complain_status.setText(Tools.getStringValue(complainDetailEntity.getComplainStatus()));

                                ArrayList<ImageUrlEntity> complainImageUrlList = complainDetailEntity.getComplainImageUrlList();
                                if (null != complainImageUrlList && complainImageUrlList.size() > 0) {
                                    for (ImageUrlEntity imageUrlEntity : complainImageUrlList) {
                                        complainImageList.add(imageUrlEntity.getUrl());
                                    }
                                    gv_complain_image.setVisibility(View.VISIBLE);
                                    complainImageGridViewAdapter.notifyDataSetChanged();
                                }

                                tv_complain_screen_des.setText(Tools.getStringValue(complainDetailEntity.getCheckContent()));
                                tv_complain_screen_datetime.setText(Tools.getStringValue(complainDetailEntity.getDeparmenthandertime()));

                                ArrayList<ImageUrlEntity> managerCheckPic = complainDetailEntity.getManagercheckpic();
                                if (null != managerCheckPic && managerCheckPic.size() > 0) {
                                    for (ImageUrlEntity imageUrlEntity : managerCheckPic) {
                                        screenImageList.add(imageUrlEntity.getUrl());
                                    }
                                    gv_complain_screen_image.setVisibility(View.VISIBLE);
                                    screenImageGridViewAdapter.notifyDataSetChanged();
                                }

                                tv_complain_programe_des.setText(Tools.getStringValue(complainDetailEntity.getSolutionContent()));
                                tv_complain_programe_datetime.setText(Tools.getStringValue(complainDetailEntity.getSolutionDate()));

                                tv_complain_dispose_person.setText("处 理 人：" + Tools.getStringValue(complainDetailEntity.getEmpHandler()));

                                tv_complain_dispose_des.setText(Tools.getStringValue(complainDetailEntity.getEmpSolutionContent()));
                                tv_complain_dispose_datetime.setText(Tools.getStringValue(complainDetailEntity.getEmpSolutionDate()));

                                ArrayList<ImageUrlEntity> disposePic = complainDetailEntity.getEmpSolutionPic();
                                if (null != disposePic && disposePic.size() > 0) {
                                    for (ImageUrlEntity imageUrlEntity : disposePic) {
                                        disposeImageList.add(imageUrlEntity.getUrl());
                                    }
                                    gv_complain_dispose_images.setVisibility(View.VISIBLE);
                                    disposeImageGridViewAdapter.notifyDataSetChanged();
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_complain_to_comment:
                Bundle bundle = new Bundle();
                bundle.putString("complainId", complainId);
                bundle.putString("taskId", taskId);
                openActivity(ComplainCommentActivity.class, bundle);
                getActivity().finish();
                break;
            default:
                break;
        }
    }
}