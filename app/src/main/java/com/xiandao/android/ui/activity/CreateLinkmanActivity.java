package com.xiandao.android.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiandao.android.R;
import com.xiandao.android.entity.LoginUserEntity;
import com.xiandao.android.entity.OverallSituationEntity;
import com.xiandao.android.net.ApiHttpResult;
import com.xiandao.android.net.HttpUtils;
import com.xiandao.android.net.NetUtil;
import com.xiandao.android.ui.BaseActivity;
import com.xiandao.android.utils.FileManagement;
import com.xiandao.android.utils.Tools;
import com.xiandao.android.view.MySpiner;

import org.xutils.event.annotation.ContentView;
import org.xutils.event.annotation.ViewInject;

import java.util.ArrayList;

/**
 * 此类描述的是:创建常用联系人activity
 *
 * @author TanYong
 *         create at 2017/5/18 14:37
 */
@ContentView(R.layout.activity_create_linkman)
public class CreateLinkmanActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.iv_title_left)
    private ImageView ivTitleLeft;
    @ViewInject(R.id.tv_title_name)
    private TextView tvTitleName;

    @ViewInject(R.id.et_create_linkman_name)
    private EditText et_create_linkman_name;
    @ViewInject(R.id.et_create_linkman_phone)
    private EditText et_create_linkman_phone;
    @ViewInject(R.id.ms_create_linkman_relationship)
    private MySpiner ms_create_linkman_relationship;
    @ViewInject(R.id.btn_create_linkman_ok)
    private TextView btn_create_linkman_ok;

    private ArrayList<String> relationshipData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        init();
    }

    private void init() {
        tvTitleName.setText("新建联系人");
        ivTitleLeft.setOnClickListener(this);
        btn_create_linkman_ok.setOnClickListener(this);
        relationshipData.add("业主");
        relationshipData.add("家属");
        ms_create_linkman_relationship.setdata(relationshipData);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_title_left:
                finish();
                break;
            case R.id.btn_create_linkman_ok:
                String phone = et_create_linkman_phone.getText().toString();
                if (phone.trim().equals("")) {
                    Tools.showPrompt("请输入手机号码");
//                } else if (!(Tools.isMobile(phone))) {
//                    Tools.showPrompt("请输入正确的手机号");
                } else {
                    String name = et_create_linkman_name.getText().toString();
                    String relationship = ms_create_linkman_relationship.getValue();
                    createLinkman(name, phone, relationship);
                }
                break;
            default:
                break;
        }
    }

    /**
     * @author TanYong
     * create at 2017/5/18 14:47
     * TODO：创建联系人
     */
    private void createLinkman(String name, String phone, String relationship) {
        startProgressDialog("");
        ApiHttpResult.createLinkman(CreateLinkmanActivity.this, new String[]{"name", "phone", "relaionShip", "userId", "appMobile"},
                new Object[]{name, phone, relationship, loginUserEntity.getUserId(), loginUserEntity.getMobileNumber()}, new HttpUtils.DataCallBack<Object>() {
                    @Override
                    public void callBack(Object o) {
                        stopProgressDialog();
                        if (o != null) {
                            OverallSituationEntity osEntity = (OverallSituationEntity) o;
                            if ("0".equals(osEntity.getResultCode())) {
                                CreateLinkmanActivity.this.setResult(1);
                                CreateLinkmanActivity.this.finish();
                            }
                            Tools.showPrompt(osEntity.getMsg());
                        } else {
                            NetUtil.toNetworkSetting(CreateLinkmanActivity.this);
                        }
                    }
                });
    }
}
