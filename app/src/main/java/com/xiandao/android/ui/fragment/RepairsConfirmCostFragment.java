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
import com.xiandao.android.entity.ConfirmWorkOrderFinishEntity;
import com.xiandao.android.entity.ConfirmWorkOrderFinishResultEntity;
import com.xiandao.android.entity.ImageUrlEntity;
import com.xiandao.android.entity.LoginUserEntity;
import com.xiandao.android.entity.OverallSituationEntity;
import com.xiandao.android.entity.RepairsDetailEntity;
import com.xiandao.android.entity.RepairsUserInfoEntity;
import com.xiandao.android.net.ApiHttpResult;
import com.xiandao.android.net.HttpUtils;
import com.xiandao.android.net.NetUtil;
import com.xiandao.android.ui.BaseLazyFragment;
import com.xiandao.android.ui.activity.LoginActivity;
import com.xiandao.android.ui.activity.RepairsCommentActivity;
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
 * 此类描述的是:报修详情--确认费用fragment
 *
 * @author TanYong
 *         create at 2017/5/9 16:32
 */
public class RepairsConfirmCostFragment extends BaseLazyFragment implements View.OnClickListener {

    private String repairsId;//报修ID
    private LoginUserEntity loginUserEntity;

    private ImageView iv_repairs_user_hear_image;//报修人头像
    private TextView tv_repairs_name;//报修人姓名
    private TextView tv_repairs_at_property;//报修人所在物业
    private ImageView iv_repairs_user_mobile;//拨打电话
    private TextView tv_work_order_detail_address;//地址
    private TextView tv_repairs_status;//报修类型：房屋漏水
    private TextView tv_repairs_des;//报修内容
    private TextView tv_repairs_date_time;//报修时间
    private GridView gv_repairs_detail_images;//报修图片
    private TextView tv_repairs_screen_des;//现场情况描述
    private TextView tv_repairs_screen_date_time;//现场检视时间
    private GridView gv_repairs_detail_screen_images;//现场检视图片
    private TextView tv_dispose_person_cost;//人工费用
    private TextView tv_dispose_material_cost;//物料费用
    private TextView tv_dispose_total_cost;//费用总计
    private TextView tv_repairs_type;//报修类型:环境卫生
    private TextView tv_dispose_person;//处 理 人  :张三
    private TextView tv_repairs_dispose_status;//处理状态

    private LinearLayout ll_refuse_or_receiving;
    private Button btn_work_order_refuse;//拒绝费用
    private Button btn_work_order_receiving;//接受费用
    private Button btn_comment_or_payment;//评价或者支付

    private DisplayImageGridViewAdapter displayImageGridViewAdapter;
    private ArrayList<String> imageUrlList = new ArrayList<>();
    private DisplayImageGridViewAdapter displayScreenImageGridViewAdapter;
    private ArrayList<String> screenImageUrlList = new ArrayList<>();

    private String taskId;
    private int repairsStatus;//工单状态：0、确认费用，1、确认是评价还是支付，2、支付，3、评价
    private boolean isToPay = false;//判断是不是去支付，不是的话就是去评价

    @Override
    protected void injectView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_repairs_confirm_cost, null);
        x.view().inject(this, view);
        setContentView(view);
        init(view);
    }

    private void init(View view) {
        loginUserEntity = FileManagement.getLoginUserEntity();
        if (loginUserEntity == null) {
            openActivity(LoginActivity.class);
            FileManagement.saveTokenInfo("");
            getActivity().finish();
        } else {
            initView(view);
            Bundle bundle = getArguments();
            if (null != bundle) {
                repairsId = bundle.getString("repairsId");
                taskId = bundle.getString("taskId");
                repairsStatus = bundle.getInt("repairsStatus");
                switch (repairsStatus) {
                    case 1:
                        confirmWorkOrderFinish();
                        break;
                    case 2:
                        btn_comment_or_payment.setText("支付");
                        isToPay = true;
                        break;
                    case 3:
                        btn_comment_or_payment.setText("评价");
                        isToPay = false;
                        break;
                    default:
                        break;
                }

                if (repairsStatus == 0) {
                    ll_refuse_or_receiving.setVisibility(View.VISIBLE);
                    btn_comment_or_payment.setVisibility(View.GONE);
                } else {
                    ll_refuse_or_receiving.setVisibility(View.GONE);
                    btn_comment_or_payment.setVisibility(View.VISIBLE);
                }
            }
            getRepairsDetail();
        }
    }

    private void initView(View view) {
        iv_repairs_user_hear_image = (ImageView) view.findViewById(R.id.iv_repairs_user_hear_image);
        tv_repairs_name = (TextView) view.findViewById(R.id.tv_repairs_name);
        tv_repairs_at_property = (TextView) view.findViewById(R.id.tv_repairs_at_property);
        iv_repairs_user_mobile = (ImageView) view.findViewById(R.id.iv_repairs_user_mobile);
        tv_repairs_status = (TextView) view.findViewById(R.id.tv_repairs_status);
        tv_work_order_detail_address = (TextView) view.findViewById(R.id.tv_work_order_detail_address);
        tv_repairs_des = (TextView) view.findViewById(R.id.tv_repairs_des);
        tv_repairs_date_time = (TextView) view.findViewById(R.id.tv_repairs_date_time);
        gv_repairs_detail_images = (GridView) view.findViewById(R.id.gv_repairs_detail_images);
        tv_repairs_screen_des = (TextView) view.findViewById(R.id.tv_repairs_screen_des);
        tv_repairs_screen_date_time = (TextView) view.findViewById(R.id.tv_repairs_screen_date_time);
        gv_repairs_detail_screen_images = (GridView) view.findViewById(R.id.gv_repairs_detail_screen_images);
        tv_dispose_person_cost = (TextView) view.findViewById(R.id.tv_dispose_person_cost);
        tv_dispose_material_cost = (TextView) view.findViewById(R.id.tv_dispose_material_cost);
        tv_dispose_total_cost = (TextView) view.findViewById(R.id.tv_dispose_total_cost);
        tv_repairs_type = (TextView) view.findViewById(R.id.tv_repairs_type);
        tv_repairs_dispose_status = (TextView) view.findViewById(R.id.tv_repairs_dispose_status);
        tv_dispose_person = (TextView) view.findViewById(R.id.tv_dispose_person);

        ll_refuse_or_receiving = (LinearLayout) view.findViewById(R.id.ll_refuse_or_receiving);
        btn_work_order_refuse = (Button) view.findViewById(R.id.btn_work_order_refuse);
        btn_work_order_receiving = (Button) view.findViewById(R.id.btn_work_order_receiving);
        btn_comment_or_payment = (Button) view.findViewById(R.id.btn_comment_or_payment);

        btn_work_order_refuse.setOnClickListener(this);
        btn_work_order_receiving.setOnClickListener(this);
        btn_comment_or_payment.setOnClickListener(this);

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

        displayScreenImageGridViewAdapter = new DisplayImageGridViewAdapter(getActivity(), screenImageUrlList);
        gv_repairs_detail_screen_images.setAdapter(displayScreenImageGridViewAdapter);

        gv_repairs_detail_screen_images.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), ShowUrlPhotoActivity.class);
                intent.putStringArrayListExtra("list", screenImageUrlList);
                intent.putExtra("position", i);
                getActivity().startActivity(intent);
            }
        });
    }

    /**
     * @author TanYong
     * create at 2017/5/26 20:06
     * TODO：调用接口来判断工单是否有费用支出，是跳转到支付界面还是评价界面
     */
    private void confirmWorkOrderFinish() {
        startProgressDialog("");
        ApiHttpResult.confirmWorkOrderFinish(getActivity(), new String[]{"appMobile", "workOrderId", "userId", "taskId", "outcome"},
                new Object[]{loginUserEntity.getMobileNumber(), repairsId, loginUserEntity.getUserId(),
                        taskId, "客户确认"},
                new HttpUtils.DataCallBack<Object>() {
                    @Override
                    public void callBack(Object o) {
                        stopProgressDialog();
                        if (o != null) {
                            ConfirmWorkOrderFinishResultEntity resultEntity = (ConfirmWorkOrderFinishResultEntity) o;
                            if ("0".equals(resultEntity.getResultCode())) {
                                ConfirmWorkOrderFinishEntity data = resultEntity.getData();
                                ArrayList<String> jbpmOutcomes = data.getJbpmOutcomes();
                                taskId = data.getTaskId();
                                if (jbpmOutcomes.indexOf("客户付费") != -1) {
                                    btn_comment_or_payment.setText("支付");
                                    isToPay = true;
                                } else if (jbpmOutcomes.indexOf("评价") != -1) {
                                    btn_comment_or_payment.setText("评价");
                                    isToPay = false;
                                }
                            }
                        } else {
                            Tools.showPrompt("网络异常");
                        }
                    }
                });
    }

    /**
     * @author TanYong
     * create at 2017/5/26 19:59
     * TODO：获取工单详情
     */
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
                            if (null != repairsUserInfo) {
                                PhotoUtils.loadRoundImage(Tools.getStringValue(repairsUserInfo.getUserHearImageUrl()), iv_repairs_user_hear_image, 200, 2);
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
                            }
                            tv_repairs_des.setText(repairsDetailEntity.getLiveContentDesc());
                            tv_repairs_date_time.setText(repairsDetailEntity.getRepairsDateTime());
                            tv_dispose_person_cost.setText(repairsDetailEntity.getManualCost());
                            tv_dispose_material_cost.setText(repairsDetailEntity.getMaterialCost());
                            tv_dispose_total_cost.setText(repairsDetailEntity.getTotalCost());
                            if (repairsDetailEntity.getTotalCost().equals("0")) {
                                btn_work_order_refuse.setVisibility(View.GONE);
                            }
                            tv_repairs_type.setText(repairsDetailEntity.getRepairsType());
                            tv_repairs_dispose_status.setText(repairsDetailEntity.getRepairsStatus());
                            tv_dispose_person.setText(repairsDetailEntity.getDisposePerson());
                            if (!Tools.isEmpty(repairsDetailEntity.getAddress())) {
                                tv_work_order_detail_address.setText("地址：" + repairsDetailEntity.getAddress());
                            }
                            tv_repairs_status.setText(repairsDetailEntity.getRepairsType());
                            tv_repairs_screen_des.setText(repairsDetailEntity.getLiveContentDesc());
                            tv_repairs_screen_date_time.setText(repairsDetailEntity.getLivedate());

                            ArrayList<ImageUrlEntity> imageUrls = repairsDetailEntity.getPiclist();
                            if (imageUrls != null && imageUrls.size() > 0) {
                                gv_repairs_detail_images.setVisibility(View.VISIBLE);
                                if (imageUrlList.size() > 0) {
                                    imageUrlList.clear();
                                }
                                for (ImageUrlEntity imageUrl : imageUrls) {
                                    imageUrlList.add(imageUrl.getUrl());
                                }
                                displayImageGridViewAdapter.notifyDataSetChanged();
                            }
                            imageUrls = repairsDetailEntity.getNewpiclist();
                            if (imageUrls != null && imageUrls.size() > 0) {
                                gv_repairs_detail_screen_images.setVisibility(View.VISIBLE);
                                if (screenImageUrlList.size() > 0) {
                                    screenImageUrlList.clear();
                                }
                                for (ImageUrlEntity imageUrl : imageUrls) {
                                    screenImageUrlList.add(imageUrl.getUrl());
                                }
                                displayScreenImageGridViewAdapter.notifyDataSetChanged();
                            }
                        } else {
                            NetUtil.toNetworkSetting(getActivity());
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_work_order_refuse://拒单
                new AlertView("温馨提示", "确认不接受费用？",
                        "取消", new String[]{"确认"}, null, getActivity(), AlertView.Style.Alert, new OnItemClickListener() {
                    @Override
                    public void onItemClick(Object o, int position) {
                        if (position == 0) {
                            workOrderAcceptCost(0);
                        }
                    }
                }).setCancelable(false).show();
                break;
            case R.id.btn_work_order_receiving://接单
                new AlertView("温馨提示", "确认接受费用？",
                        "取消", new String[]{"确认"}, null, getActivity(), AlertView.Style.Alert, new OnItemClickListener() {
                    @Override
                    public void onItemClick(Object o, int position) {
                        if (position == 0) {
                            workOrderAcceptCost(1);
                        }
                    }
                }).setCancelable(false).show();
                break;
            case R.id.btn_comment_or_payment://去评价或者去支付
                if (isToPay) {
                    Intent intent = new Intent(getActivity(), RepairsCommentActivity.class);
                    intent.putExtra("workOrderId", repairsId);
                    intent.putExtra("taskId", taskId);
                    startActivity(intent);
                    getActivity().finish();
                } else {
                    Intent intent = new Intent(getActivity(), RepairsCommentActivity.class);
                    intent.putExtra("workOrderId", repairsId);
                    intent.putExtra("taskId", taskId);
                    startActivity(intent);
                    getActivity().finish();
                }
                break;
            default:
                break;
        }
    }

    /**
     * @author TanYong
     * create at 2017/5/26 9:21
     * TODO：是否接受费用
     * accept 1接受 0不接受
     */
    private void workOrderAcceptCost(int accept) {
        startProgressDialog("");
        ApiHttpResult.acceptPrice(getActivity(), new String[]{"appMobile", "accept", "workOrderId", "userId", "taskId", "outcome"},
                new Object[]{loginUserEntity.getMobileNumber(), accept, repairsId, loginUserEntity.getUserId(),
                        taskId, "是否接受价格"},
                new HttpUtils.DataCallBack<Object>() {
                    @Override
                    public void callBack(Object o) {
                        stopProgressDialog();
                        if (o != null) {
                            OverallSituationEntity osEntity = (OverallSituationEntity) o;
                            if ("0".equals(osEntity.getResultCode())) {
                                new AlertView("温馨提示", "工单处理成功",
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
