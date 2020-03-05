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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiandao.android.R;
import com.xiandao.android.adapter.DisplayImageGridViewAdapter;
import com.xiandao.android.entity.ImageUrlEntity;
import com.xiandao.android.entity.LoginUserEntity;
import com.xiandao.android.entity.RepairsDetailEntity;
import com.xiandao.android.entity.RepairsUserInfoEntity;
import com.xiandao.android.net.ApiHttpResult;
import com.xiandao.android.net.HttpUtils;
import com.xiandao.android.ui.BaseLazyFragment;
import com.xiandao.android.ui.activity.ShowUrlPhotoActivity;
import com.xiandao.android.utils.FileManagement;
import com.xiandao.android.utils.PhoneUtils;
import com.xiandao.android.utils.PhotoUtils;
import com.xiandao.android.utils.Tools;

import org.xutils.x;

import java.util.ArrayList;

/**
 * 此类描述的是:工单处理完成显示详情fragment
 *
 * @author TanYong
 *         create at 2017/5/22 21:32
 */
public class RepairsDetailFragment extends BaseLazyFragment {

    private ImageView iv_head;//业主头像
    private TextView tv_owner_name;//业主姓名
    private TextView tv_owner_address;//业主住址
    private TextView tv_owner_at_property;//业主所属物业
    private ImageView iv_owner_mobile;//业主拨打电话图标
    private TextView tv_work_order_title;//工单标题
    private TextView tv_work_order_status;//工单状态
    private TextView tv_work_order_detail_address;//地址
    private TextView tv_work_order_detail_plan_datetime;//上门时间
    private TextView tv_work_order_des;//工单描述
    private TextView tv_work_order_datetime;//工单时间
    private GridView gv_work_order_dispaly_image;//工单显示图片
    private DisplayImageGridViewAdapter displayImageGridViewAdapter;
    private ArrayList<String> displayImageUrlList = new ArrayList<>();// 显示图片URL集合

    private TextView tv_work_order_ago_dispose_screen;//现场情况
    private TextView tv_work_order_ago_dispose_des;//员工处理前，检查时对工单的描述
    private TextView tv_ago_dispose_datetime;//员工检查的时间
    private GridView gv_work_order_ago_dispose_image;
    private DisplayImageGridViewAdapter agoDisposeImageGridViewAdapter;
    private ArrayList<String> agoDisposeImageUrlList = new ArrayList<>();// 显示处理前图片URL集合

    private TextView tv_work_order_after_dispose_screen;//现场情况
    private TextView tv_work_order_after_dispose_des;//员工处理完工单的结果描述
    private TextView tv_work_order_after_dispose_datetime;//员工处理完的时间
    private GridView gv_work_order_after_dispose_image;
    private DisplayImageGridViewAdapter afterDisposeImageGridViewAdapter;
    private ArrayList<String> afterDisposeImageUrlList = new ArrayList<>();// 显示处理后图片URL集合

    private LinearLayout ll_work_order_comment;
    private ImageView iv_star_1;
    private ImageView iv_star_2;
    private ImageView iv_star_3;
    private ImageView iv_star_4;
    private ImageView iv_star_5;
    private TextView tv_work_order_comment_des;

    private LoginUserEntity loginUserEntity;
    private String workOrderId;//工单ID

    @Override
    protected void injectView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_repairs_detail, null);
        x.view().inject(this, view);
        setContentView(view);
        loginUserEntity = FileManagement.getLoginUserEntity();
        Bundle bundle = getArguments();
        if (bundle != null) {
            workOrderId = bundle.getString("repairsId");
            init(view);
        }
    }

    private void init(View view) {
        iv_head = (ImageView) view.findViewById(R.id.iv_head);
        tv_owner_name = (TextView) view.findViewById(R.id.tv_owner_name);
        tv_owner_address = (TextView) view.findViewById(R.id.tv_owner_address);
        tv_owner_at_property = (TextView) view.findViewById(R.id.tv_owner_at_property);
        iv_owner_mobile = (ImageView) view.findViewById(R.id.iv_owner_mobile);
        tv_work_order_title = (TextView) view.findViewById(R.id.tv_work_order_title);
        tv_work_order_status = (TextView) view.findViewById(R.id.tv_work_order_status);
        tv_work_order_detail_address = (TextView) view.findViewById(R.id.tv_work_order_detail_address);
        tv_work_order_detail_plan_datetime = (TextView) view.findViewById(R.id.tv_work_order_detail_plan_datetime);
        tv_work_order_status.setText("已结单");
        tv_work_order_des = (TextView) view.findViewById(R.id.tv_work_order_des);
        tv_work_order_datetime = (TextView) view.findViewById(R.id.tv_work_order_datetime);
        gv_work_order_dispaly_image = (GridView) view.findViewById(R.id.gv_work_order_dispaly_image);

        gv_work_order_dispaly_image.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), ShowUrlPhotoActivity.class);
                intent.putStringArrayListExtra("list", displayImageUrlList);
                intent.putExtra("position", i);
                getActivity().startActivity(intent);
            }
        });

        displayImageGridViewAdapter = new DisplayImageGridViewAdapter(getActivity(), displayImageUrlList);
        gv_work_order_dispaly_image.setAdapter(displayImageGridViewAdapter);

        tv_work_order_ago_dispose_screen = (TextView) view.findViewById(R.id.tv_work_order_ago_dispose_screen);
        tv_work_order_ago_dispose_des = (TextView) view.findViewById(R.id.tv_work_order_ago_dispose_des);
        tv_ago_dispose_datetime = (TextView) view.findViewById(R.id.tv_ago_dispose_datetime);
        gv_work_order_ago_dispose_image = (GridView) view.findViewById(R.id.gv_work_order_ago_dispose_image);

        gv_work_order_ago_dispose_image.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), ShowUrlPhotoActivity.class);
                intent.putStringArrayListExtra("list", agoDisposeImageUrlList);
                intent.putExtra("position", i);
                getActivity().startActivity(intent);
            }
        });

        agoDisposeImageGridViewAdapter = new DisplayImageGridViewAdapter(getActivity(), agoDisposeImageUrlList);
        gv_work_order_ago_dispose_image.setAdapter(agoDisposeImageGridViewAdapter);

        tv_work_order_after_dispose_screen = (TextView) view.findViewById(R.id.tv_work_order_after_dispose_screen);
        tv_work_order_after_dispose_des = (TextView) view.findViewById(R.id.tv_work_order_after_dispose_des);
        tv_work_order_after_dispose_datetime = (TextView) view.findViewById(R.id.tv_work_order_after_dispose_datetime);
        gv_work_order_after_dispose_image = (GridView) view.findViewById(R.id.gv_work_order_after_dispose_image);

        gv_work_order_after_dispose_image.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), ShowUrlPhotoActivity.class);
                intent.putStringArrayListExtra("list", afterDisposeImageUrlList);
                intent.putExtra("position", i);
                getActivity().startActivity(intent);
            }
        });

        afterDisposeImageGridViewAdapter = new DisplayImageGridViewAdapter(getActivity(), afterDisposeImageUrlList);
        gv_work_order_after_dispose_image.setAdapter(afterDisposeImageGridViewAdapter);

        ll_work_order_comment = (LinearLayout) view.findViewById(R.id.ll_work_order_comment);
        iv_star_1 = (ImageView) view.findViewById(R.id.iv_star_1);
        iv_star_2 = (ImageView) view.findViewById(R.id.iv_star_2);
        iv_star_3 = (ImageView) view.findViewById(R.id.iv_star_3);
        iv_star_4 = (ImageView) view.findViewById(R.id.iv_star_4);
        iv_star_5 = (ImageView) view.findViewById(R.id.iv_star_5);
        tv_work_order_comment_des = (TextView) view.findViewById(R.id.tv_work_order_comment_des);
        queryWorkOrderDetail();
    }

    /**
     * @author TanYong
     * create at 2017/5/22 10:22
     * TODO：查询工单详情
     */
    private void queryWorkOrderDetail() {
        startProgressDialog("");
        ApiHttpResult.getRepairsDetail(getActivity(), new String[]{"appMobile", "repairsId", "userId"},
                new Object[]{loginUserEntity.getMobileNumber(), workOrderId, loginUserEntity.getUserId()}, new HttpUtils.DataCallBack<Object>() {

                    @Override
                    public void callBack(Object o) {
                        stopProgressDialog();
                        if (o != null) {
                            RepairsDetailEntity repairsDetailEntity = (RepairsDetailEntity) o;
                            final RepairsUserInfoEntity repairsUserInfo = repairsDetailEntity.getUserInfo();
                            if (repairsUserInfo != null) {
                                PhotoUtils.loadRoundImage(repairsUserInfo.getUserHearImageUrl(), iv_head, 200, 2);
                                tv_owner_name.setText(Tools.getStringValue(repairsUserInfo.getUserName()));
                                tv_owner_address.setText(Tools.getStringValue(repairsUserInfo.getUserAddress()));
                                tv_owner_at_property.setText(Tools.getStringValue(repairsUserInfo.getAtProperty()));
                                iv_owner_mobile.setOnClickListener(new View.OnClickListener() {
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
                            }
                            tv_work_order_title.setText(Tools.getStringValue(repairsDetailEntity.getRepairsType()));
                            tv_work_order_status.setText(Tools.getStringValue(repairsDetailEntity.getRepairsStatus()));
                            if (!Tools.isEmpty(repairsDetailEntity.getAddress())) {
                                tv_work_order_detail_address.setText("地址：" + repairsDetailEntity.getAddress());
                            }
                            tv_work_order_detail_plan_datetime.setText("上门时间：" + repairsDetailEntity.getPlandate());
                            tv_work_order_des.setText(Tools.getStringValue(repairsDetailEntity.getRepairsDes()));
                            tv_work_order_datetime.setText(Tools.getStringValue(repairsDetailEntity.getRepairsDateTime()));

                            ArrayList<ImageUrlEntity> piclist = repairsDetailEntity.getPiclist();
                            if (null != piclist && piclist.size() > 0) {
                                gv_work_order_dispaly_image.setVisibility(View.VISIBLE);
                                for (ImageUrlEntity imageUrlEntity : piclist) {
                                    displayImageUrlList.add(imageUrlEntity.getUrl());
                                }
                                displayImageGridViewAdapter.notifyDataSetChanged();
                            }
                            if (!Tools.isEmpty(Tools.getStringValue(repairsDetailEntity.getLiveContentDesc()))) {
                                tv_work_order_ago_dispose_screen.setVisibility(View.VISIBLE);
                            }
                            tv_work_order_ago_dispose_des.setText(Tools.getStringValue(repairsDetailEntity.getLiveContentDesc()));
                            tv_ago_dispose_datetime.setText(repairsDetailEntity.getLivedate());
                            ArrayList<ImageUrlEntity> newPiclist = repairsDetailEntity.getNewpiclist();
                            if (null != newPiclist && newPiclist.size() > 0) {
                                gv_work_order_ago_dispose_image.setVisibility(View.VISIBLE);
                                for (ImageUrlEntity imageUrlEntity : newPiclist) {
                                    agoDisposeImageUrlList.add(imageUrlEntity.getUrl());
                                }
                                agoDisposeImageGridViewAdapter.notifyDataSetChanged();
                            }
                            if (!Tools.isEmpty(Tools.getStringValue(repairsDetailEntity.getDisposeDes()))) {
                                tv_work_order_after_dispose_screen.setVisibility(View.VISIBLE);
                            }
                            tv_work_order_after_dispose_des.setText(Tools.getStringValue(repairsDetailEntity.getDisposeDes()));
                            tv_work_order_after_dispose_datetime.setText(Tools.getStringValue(repairsDetailEntity.getDisposeTime()));
                            ArrayList<ImageUrlEntity> handerpiclist = repairsDetailEntity.getHanderpiclist();
                            if (null != handerpiclist && handerpiclist.size() > 0) {
                                gv_work_order_after_dispose_image.setVisibility(View.VISIBLE);
                                for (ImageUrlEntity imageUrlEntity : handerpiclist) {
                                    afterDisposeImageUrlList.add(imageUrlEntity.getUrl());
                                }
                                afterDisposeImageGridViewAdapter.notifyDataSetChanged();
                            }
                            if (repairsDetailEntity.getCommentLevel() != null && !repairsDetailEntity.getCommentLevel().equals("")) {
                                ll_work_order_comment.setVisibility(View.VISIBLE);
                                switch (Integer.valueOf(repairsDetailEntity.getCommentLevel())) {
                                    case 1:
                                        iv_star_1.setVisibility(View.VISIBLE);
                                        iv_star_2.setVisibility(View.GONE);
                                        iv_star_3.setVisibility(View.GONE);
                                        iv_star_4.setVisibility(View.GONE);
                                        iv_star_5.setVisibility(View.GONE);
                                        break;
                                    case 2:
                                        iv_star_1.setVisibility(View.VISIBLE);
                                        iv_star_2.setVisibility(View.VISIBLE);
                                        iv_star_3.setVisibility(View.GONE);
                                        iv_star_4.setVisibility(View.GONE);
                                        iv_star_5.setVisibility(View.GONE);
                                        break;
                                    case 3:
                                        iv_star_1.setVisibility(View.VISIBLE);
                                        iv_star_2.setVisibility(View.VISIBLE);
                                        iv_star_3.setVisibility(View.VISIBLE);
                                        iv_star_4.setVisibility(View.GONE);
                                        iv_star_5.setVisibility(View.GONE);
                                        break;
                                    case 4:
                                        iv_star_1.setVisibility(View.VISIBLE);
                                        iv_star_2.setVisibility(View.VISIBLE);
                                        iv_star_3.setVisibility(View.VISIBLE);
                                        iv_star_4.setVisibility(View.VISIBLE);
                                        iv_star_5.setVisibility(View.GONE);
                                        break;
                                    case 5:
                                        iv_star_1.setVisibility(View.VISIBLE);
                                        iv_star_2.setVisibility(View.VISIBLE);
                                        iv_star_3.setVisibility(View.VISIBLE);
                                        iv_star_4.setVisibility(View.VISIBLE);
                                        iv_star_5.setVisibility(View.VISIBLE);
                                        break;
                                    default:
                                        break;
                                }
                                tv_work_order_comment_des.setVisibility(View.VISIBLE);
                                tv_work_order_comment_des.setText(Tools.getStringValue(repairsDetailEntity.getCommentContent()));
                            }
                        } else {
                            Tools.showPrompt("网络异常");
                        }
                    }
                });
    }
}
