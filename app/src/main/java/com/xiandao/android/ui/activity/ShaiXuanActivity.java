package com.xiandao.android.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiandao.android.R;
import com.xiandao.android.entity.ComplainTypeEntity;
import com.xiandao.android.entity.LoginUserEntity;
import com.xiandao.android.entity.RepairsTypeEntity;
import com.xiandao.android.entity.SpinnerEntity;
import com.xiandao.android.net.ApiHttpResult;
import com.xiandao.android.net.HttpUtils;
import com.xiandao.android.ui.BaseActivity;
import com.xiandao.android.utils.Constants;
import com.xiandao.android.utils.FileManagement;
import com.xiandao.android.utils.Tools;
import com.xiandao.android.view.MySpiner;
import com.xiandao.android.view.WheelDialog;

import org.xutils.event.annotation.ContentView;
import org.xutils.event.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * 此类描述的是:筛选activity
 *
 * @author TanYong
 *         create at 2017/6/1 19:16
 */
@ContentView(R.layout.activity_shai_xuan)
public class ShaiXuanActivity extends BaseActivity implements View.OnClickListener, View.OnTouchListener {

    @ViewInject(R.id.iv_title_left)
    private ImageView ivTitleLeft;
    @ViewInject(R.id.tv_title_name)
    private TextView tvTitleName;
    @ViewInject(R.id.tv_spinner_name)
    private TextView tv_spinner_name;
    @ViewInject(R.id.ll_shaixuan_gongdan_leibie)
    private LinearLayout ll_shaixuan_gongdan_leibie;
    @ViewInject(R.id.ms_gongdan_leibie)
    private MySpiner ms_gongdan_leibie;
    @ViewInject(R.id.btn_setmine_ok)
    private Button btn_setmine_ok;
    @ViewInject(R.id.et_shaixuan_start_time)
    private EditText etStartTime;
    @ViewInject(R.id.et_shaixuan_end_time)
    private EditText etEndTime;
    /**
     * 1、报修筛选；2、投诉筛选；3、筛选
     */
    private int fromWhereFlag;//判断从哪个界面跳转过来的

    //===========筛选条件=========
    private String startTime;//开始时间
    private String endTime;//结束时间
    private String workOrderType;//工单类别
    private String complainType;//投诉类别

    private WheelDialog startwheeldialog;
    private WheelDialog endwheeldialog;

    private ArrayList<SpinnerEntity> repairsTypeList;//报修类别下拉列表集合
    private List<String> repairsType1Names = new ArrayList<>();
    private ArrayList<SpinnerEntity> complainTypeList;//投诉类别下拉列表集合
    private List<String> complainType1Names = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fromWhereFlag = getIntent().getIntExtra("fromWhereFlag", 0);
        init();
    }

    private void init() {
        ivTitleLeft.setOnClickListener(this);
        btn_setmine_ok.setOnClickListener(this);
        switch (fromWhereFlag) {
            case 1:
                tvTitleName.setText("报修筛选");
                tv_spinner_name.setText("工单类别：");
                getRepairsType();
                break;
            case 2:
                tvTitleName.setText("投诉筛选");
                tv_spinner_name.setText("投诉类别：");
                complainType();
                break;
            case 3:
                tvTitleName.setText("筛选");
                ll_shaixuan_gongdan_leibie.setVisibility(View.GONE);
                break;
            default:
                break;
        }

        ms_gongdan_leibie.setOnSelectItemValue(new MySpiner.GetSeleceValue() {
            @Override
            public void getSeleceValue() {
                String selectItemValue = ms_gongdan_leibie.getSelectItemValue();
                if (repairsType1Names != null && repairsType1Names.size() > 0) {
                    workOrderType = repairsTypeList.get(repairsType1Names.indexOf(selectItemValue)).getId();
                }
            }
        });

        etStartTime.setInputType(InputType.TYPE_NULL);
        etEndTime.setInputType(InputType.TYPE_NULL);
        etStartTime.setOnTouchListener(this);
        etEndTime.setOnTouchListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_title_left:
                finish();
                break;
            case R.id.btn_setmine_ok:
                if (fromWhereFlag == 1) {
                    Intent intent = new Intent();
                    startTime = etStartTime.getText().toString();
                    endTime = etEndTime.getText().toString();
                    workOrderType = ms_gongdan_leibie.getValue();
                    intent.putExtra("startTime", startTime);
                    intent.putExtra("endTime", endTime);
                    intent.putExtra("workOrderType", workOrderType);
                    this.setResult(Constants.MY_REPAIRS_REQUEST_CODE, intent);
                } else if (fromWhereFlag == 2) {
                    Intent intent = new Intent();
                    startTime = etStartTime.getText().toString();
                    endTime = etEndTime.getText().toString();
                    complainType = ms_gongdan_leibie.getValue();
                    intent.putExtra("startTime", startTime);
                    intent.putExtra("endTime", endTime);
                    intent.putExtra("complainType", complainType);
                    this.setResult(Constants.MY_COMPLAIN_REQUEST_CODE, intent);
                }
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            switch (view.getId()) {
                case R.id.et_shaixuan_start_time:
                    startwheeldialog = new WheelDialog(this, R.style.Dialog_Floating, new WheelDialog.Click() {
                        @Override
                        public void getvaue(String value) {
                            startwheeldialog.cancel();
                            etStartTime.setText(value);
                        }
                    });
                    startwheeldialog.show();
                    break;
                case R.id.et_shaixuan_end_time:
                    endwheeldialog = new WheelDialog(this, R.style.Dialog_Floating, new WheelDialog.Click() {
                        @Override
                        public void getvaue(String value) {
                            endwheeldialog.cancel();
                            etEndTime.setText(value);
                        }
                    });
                    endwheeldialog.show();
                    break;
                default:
                    break;
            }
        }
        return false;
    }

    /**
     * @author TanYong
     * create at 2017/4/26 14:06
     * isF:是否第一个下拉框
     * TODO：获取报修类别下拉列表
     */
    private void getRepairsType() {
        startProgressDialog("");
        ApiHttpResult.getRepairsType(ShaiXuanActivity.this, new String[]{"appMobile"},
                new Object[]{loginUserEntity.getMobileNumber()}, new HttpUtils.DataCallBack<Object>() {
                    @Override
                    public void callBack(Object o) {
                        stopProgressDialog();
                        if (o != null) {
                            RepairsTypeEntity repairsTypeEntity = (RepairsTypeEntity) o;
                            if ("0".equals(repairsTypeEntity.getResultCode())) {
                                repairsTypeList = repairsTypeEntity.getRepairsTypeList();
                                if (repairsTypeList != null && repairsTypeList.size() > 0) {
                                    for (SpinnerEntity entity : repairsTypeList) {
                                        repairsType1Names.add(entity.getName());
                                    }
                                    ms_gongdan_leibie.setdata(repairsType1Names);
                                    ms_gongdan_leibie.onItemClick(0);
                                } else {
                                    Tools.showPrompt("没有数据");
                                }
                            } else {
                                Tools.showPrompt(repairsTypeEntity.getMsg());
                            }
                        } else {
                            Tools.showPrompt("访问网络失败");
                        }
                    }
                });
    }

    /**
     * @author TanYong
     * create at 2017/6/2 15:21
     * TODO：获取投诉类型
     */
    private void complainType() {
        startProgressDialog("");
        ApiHttpResult.getComplainType(ShaiXuanActivity.this, new String[]{"appMobile"},
                new Object[]{loginUserEntity.getMobileNumber()}, new HttpUtils.DataCallBack<Object>() {
                    @Override
                    public void callBack(Object o) {
                        stopProgressDialog();
                        if (o != null) {
                            ComplainTypeEntity complainTypeEntity = (ComplainTypeEntity) o;
                            if ("0".equals(complainTypeEntity.getResultCode())) {
                                complainTypeList = complainTypeEntity.getComplainTypeList();
                                if (complainTypeList != null && complainTypeList.size() > 0) {
                                    for (SpinnerEntity entity : complainTypeList) {
                                        complainType1Names.add(entity.getName());
                                    }
                                    ms_gongdan_leibie.setdata(complainType1Names);
                                    ms_gongdan_leibie.onItemClick(0);
                                } else {
                                    Tools.showPrompt("没有数据");
                                }
                            } else {
                                Tools.showPrompt(complainTypeEntity.getMsg());
                            }
                        } else {
                            Tools.showPrompt("访问网络失败");
                        }
                    }
                });
    }

}
