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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiandao.android.R;
import com.xiandao.android.adapter.DisplayImageGridViewAdapter;
import com.xiandao.android.entity.ComplainDetailEntity;
import com.xiandao.android.entity.DetailUserInfoEntity;
import com.xiandao.android.entity.ImageUrlEntity;
import com.xiandao.android.entity.LoginUserEntity;
import com.xiandao.android.entity.OverallSituationEntity;
import com.xiandao.android.net.ApiHttpResult;
import com.xiandao.android.net.HttpUtils;
import com.xiandao.android.net.NetUtil;
import com.xiandao.android.ui.BaseLazyFragment;
import com.xiandao.android.ui.activity.LoginActivity;
import com.xiandao.android.ui.activity.SelectCommonAddressActivity;
import com.xiandao.android.ui.activity.ShowUrlPhotoActivity;
import com.xiandao.android.utils.FileManagement;
import com.xiandao.android.utils.PhoneUtils;
import com.xiandao.android.utils.PhotoUtils;
import com.xiandao.android.utils.Tools;
import com.xiandao.android.view.alertview.AlertView;
import com.xiandao.android.view.alertview.OnItemClickListener;

import org.xutils.x;

import java.util.ArrayList;

/**
 * 此类描述的是:投诉确认方案fragment
 *
 * @author TanYong
 *         create at 2017/6/2 10:18
 */
public class ComplainComfirmProgrammeFragment extends BaseLazyFragment implements View.OnClickListener {

    private ImageView iv_user_hear_image;
    private TextView tv_name;
    private TextView tv_at_property;
    private ImageView iv_user_mobile;

    private TextView tv_complain_type;
    private TextView tv_complain_status;
    private TextView tv_complain_des;
    private TextView tv_complain_date_time;
    private GridView gv_complain_detail_images;//投诉详情图片

    private TextView tv_complain_screen_des;
    private TextView tv_complain_screen_date_time;
    private GridView gv_complain_detail_screen_images;//投诉详情图片

    private TextView tv_complain_programme_des;
    private TextView tv_complain_programme_datetime;

    private LinearLayout ll_complain_refuse_or_receiving;
    private Button btn_complain_refuse;
    private Button btn_complain_receiving;

    private DisplayImageGridViewAdapter complainDetailImagesAdapter;
    private ArrayList<String> complainDetailImageUrlList = new ArrayList<>();
    private DisplayImageGridViewAdapter complainScreenImagesAdapter;
    private ArrayList<String> complainScreenImageUrlList = new ArrayList<>();

    private LoginUserEntity loginUserEntity;
    private String complainId;//投诉ID
    private String taskId;
    private String piid;
    private boolean isDetail;

    @Override
    protected void injectView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_complain_comfirm_programme, null);
        x.view().inject(this, view);
        setContentView(view);
        loginUserEntity = FileManagement.getLoginUserEntity();
        Bundle bundle = getArguments();
        if (null != bundle) {
            complainId = bundle.getString("complainId");
            taskId = bundle.getString("taskId");
            piid = bundle.getString("piid");
            isDetail = bundle.getBoolean("isDetail");
            initView(view);
            getComplainDetail();
        }
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

        tv_complain_screen_des = (TextView) view.findViewById(R.id.tv_complain_screen_des);
        tv_complain_screen_date_time = (TextView) view.findViewById(R.id.tv_complain_screen_date_time);
        gv_complain_detail_screen_images = (GridView) view.findViewById(R.id.gv_complain_detail_screen_images);
        complainScreenImagesAdapter = new DisplayImageGridViewAdapter(getActivity(), complainScreenImageUrlList);
        gv_complain_detail_screen_images.setAdapter(complainScreenImagesAdapter);

        gv_complain_detail_screen_images.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), ShowUrlPhotoActivity.class);
                intent.putStringArrayListExtra("list", complainScreenImageUrlList);
                intent.putExtra("position", i);
                getActivity().startActivity(intent);
            }
        });

        tv_complain_programme_des = (TextView) view.findViewById(R.id.tv_complain_programme_des);
        tv_complain_programme_datetime = (TextView) view.findViewById(R.id.tv_complain_programme_datetime);

        ll_complain_refuse_or_receiving = (LinearLayout) view.findViewById(R.id.ll_complain_refuse_or_receiving);
        btn_complain_refuse = (Button) view.findViewById(R.id.btn_complain_refuse);
        btn_complain_receiving = (Button) view.findViewById(R.id.btn_complain_receiving);
        btn_complain_refuse.setOnClickListener(this);
        btn_complain_receiving.setOnClickListener(this);

        if (isDetail)
            ll_complain_refuse_or_receiving.setVisibility(View.GONE);
    }


    /**
     * @author TanYong
     * create at 2017/6/2 13:30
     * TODO：获取投诉详情
     */
    private void getComplainDetail() {
        startProgressDialog("");
        ApiHttpResult.getComplainDetail(getActivity(), new String[]{"appMobile", "complainId", "userId"},
                new Object[]{loginUserEntity.getMobileNumber(), complainId, loginUserEntity.getUserId()},
                new HttpUtils.DataCallBack<Object>() {
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
                                tv_complain_status.setText(Tools.getStringValue(complainDetailEntity.getComplainStatus()));
                                tv_complain_des.setText(Tools.getStringValue(complainDetailEntity.getComplainDes()));
                                tv_complain_date_time.setText(Tools.getStringValue(complainDetailEntity.getComplainDateTime()));

                                ArrayList<ImageUrlEntity> complainImageUrlList = complainDetailEntity.getComplainImageUrlList();
                                if (null != complainImageUrlList && complainImageUrlList.size() > 0) {
                                    for (ImageUrlEntity imageUrlEntity : complainImageUrlList) {
                                        complainDetailImageUrlList.add(imageUrlEntity.getUrl());
                                    }
                                    gv_complain_detail_images.setVisibility(View.VISIBLE);
                                    complainDetailImagesAdapter.notifyDataSetChanged();
                                }

                                tv_complain_screen_des.setText(Tools.getStringValue(complainDetailEntity.getCheckContent()));
                                tv_complain_screen_date_time.setText(Tools.getStringValue(complainDetailEntity.getDeparmenthandertime()));

                                ArrayList<ImageUrlEntity> managerCheckPic = complainDetailEntity.getManagercheckpic();
                                if (null != managerCheckPic && managerCheckPic.size() > 0) {
                                    for (ImageUrlEntity imageUrlEntity : managerCheckPic) {
                                        complainScreenImageUrlList.add(imageUrlEntity.getUrl());
                                    }
                                    gv_complain_detail_screen_images.setVisibility(View.VISIBLE);
                                    complainScreenImagesAdapter.notifyDataSetChanged();
                                }

                                tv_complain_programme_des.setText(Tools.getStringValue(complainDetailEntity.getSolutionContent()));
                                tv_complain_programme_datetime.setText(Tools.getStringValue(complainDetailEntity.getSolutionDate()));

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
            case R.id.btn_complain_refuse:
                new AlertView("温馨提示", "确认不接受解决方案？",
                        "取消", new String[]{"确认"}, null, getActivity(), AlertView.Style.Alert, new OnItemClickListener() {
                    @Override
                    public void onItemClick(Object o, int position) {
                        if (position == 0) {
                            isAcceptSolutionComplain(0);
                        }
                    }
                }).setCancelable(false).show();
                break;
            case R.id.btn_complain_receiving:
                new AlertView("温馨提示", "确认接受解决方案？",
                        "取消", new String[]{"确认"}, null, getActivity(), AlertView.Style.Alert, new OnItemClickListener() {
                    @Override
                    public void onItemClick(Object o, int position) {
                        if (position == 0) {
                            isAcceptSolutionComplain(1);
                        }
                    }
                }).setCancelable(false).show();
                break;
            default:
                break;
        }
    }

    /**
     * @author TanYong
     * create at 2017/6/10 19:23
     * accept 1 接受 0不接受
     * TODO：业主是否接受解决方案
     */
    private void isAcceptSolutionComplain(int isAccept) {
        startProgressDialog("");
        ApiHttpResult.isAcceptSolutionComplain(getActivity(), new String[]{"appMobile", "complainid", "ownerId", "taskId", "outcome", "accept", "piid"},
                new Object[]{loginUserEntity.getMobileNumber(), complainId, loginUserEntity.getUserId(), taskId, "是否接受", isAccept, piid},
                new HttpUtils.DataCallBack<Object>() {
                    @Override
                    public void callBack(Object o) {
                        stopProgressDialog();
                        if (o != null) {
                            OverallSituationEntity osEntity = (OverallSituationEntity) o;
                            if (osEntity.getResultCode() != null && osEntity.getResultCode().equals("0")) {
                                new AlertView("温馨提示", "投诉处理成功",
                                        null, new String[]{"知道了"}, null, getActivity(), AlertView.Style.Alert, new OnItemClickListener() {
                                    @Override
                                    public void onItemClick(Object o, int position) {
                                        getActivity().finish();
                                    }
                                }).setCancelable(false).show();
                            } else {
                                Tools.showPrompt(osEntity.getMsg());
                            }
                        } else {
                            NetUtil.toNetworkSetting(getActivity());
                        }
                    }
                });
    }
}
