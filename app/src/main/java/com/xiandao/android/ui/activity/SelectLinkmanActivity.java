package com.xiandao.android.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.CursorLoader;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiandao.android.R;
import com.xiandao.android.adapter.SelectLinkmanAdapter;
import com.xiandao.android.entity.GetLinkmanListResultEntity;
import com.xiandao.android.entity.LinkmanEntity;
import com.xiandao.android.entity.LoginUserEntity;
import com.xiandao.android.net.ApiHttpResult;
import com.xiandao.android.net.HttpUtils;
import com.xiandao.android.net.NetUtil;
import com.xiandao.android.ui.BaseActivity;
import com.xiandao.android.utils.FileManagement;
import com.xiandao.android.utils.Tools;
import com.xiandao.android.view.NoScrollListView;

import org.xutils.event.annotation.ContentView;
import org.xutils.event.annotation.ViewInject;

import java.util.ArrayList;

/**
 * 此类描述的是:选择联系人fragment
 *
 * @author TanYong
 *         create at 2017/5/18 10:22
 */
@ContentView(R.layout.activity_select_linkman)
public class SelectLinkmanActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.iv_title_left)
    private ImageView ivTitleLeft;
    @ViewInject(R.id.tv_title_name)
    private TextView ivTitleName;

    @ViewInject(R.id.nslv_select_linkman)
    private NoScrollListView nslv_select_linkman;
    @ViewInject(R.id.tv_create_linkman)
    private TextView tvCreateLinkman;
    @ViewInject(R.id.tv_select_linkman_from_phone)
    private TextView tvSelectLinkmanFromPhone;
    @ViewInject(R.id.btn_select_linkman_ok)
    private Button btnSelectLinkmanOk;

    private ArrayList<LinkmanEntity> linkmanEntityList = new ArrayList<>();
    private SelectLinkmanAdapter selectLinkmanAdapter;

    private LoginUserEntity loginUserEntity;

    private LinkmanEntity selectLinkmanEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        init();
//    }

    private void init() {
        loginUserEntity = FileManagement.getLoginUserEntity();

        ivTitleName.setText("选择联系人");
        ivTitleLeft.setOnClickListener(this);
        tvCreateLinkman.setOnClickListener(this);
        tvSelectLinkmanFromPhone.setOnClickListener(this);
        btnSelectLinkmanOk.setOnClickListener(this);

        selectLinkmanAdapter = new SelectLinkmanAdapter(SelectLinkmanActivity.this, linkmanEntityList);
        nslv_select_linkman.setAdapter(selectLinkmanAdapter);

        getLinkmanList();

        nslv_select_linkman.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectLinkmanEntity = linkmanEntityList.get(position);
                if (linkmanEntityList.get(position).getCheckedFlag() == 1) {
                    linkmanEntityList.get(position).setCheckedFlag(2);
                } else if (linkmanEntityList.get(position).getCheckedFlag() == 2) {
                    linkmanEntityList.get(position).setCheckedFlag(1);
                    for (int i = 0; i < linkmanEntityList.size(); i++) {
                        if (i != position && linkmanEntityList.get(i).getCheckedFlag() == 1) {
                            linkmanEntityList.get(i).setCheckedFlag(2);
                        }
                    }
                }
                selectLinkmanAdapter.notifyDataSetChanged();
            }
        });

    }

    /**
     * @author TanYong
     * create at 2017/5/18 10:22
     * TODO：获取联系人列表
     */
    private void getLinkmanList() {
        startProgressDialog("");
        ApiHttpResult.getLinkmanList(SelectLinkmanActivity.this, new String[]{"userId", "appMobile"},
                new Object[]{loginUserEntity.getUserId(), loginUserEntity.getMobileNumber()}, new HttpUtils.DataCallBack<Object>() {
                    @Override
                    public void callBack(Object o) {
                        stopProgressDialog();
                        if (o != null) {
                            GetLinkmanListResultEntity getLinkmanListResultEntity = (GetLinkmanListResultEntity) o;
                            if ("0".equals(getLinkmanListResultEntity.getResultCode())) {
                                if (getLinkmanListResultEntity.getLinkmanEntityList() != null && getLinkmanListResultEntity.getLinkmanEntityList().size() > 0) {
                                    if (linkmanEntityList.size() > 0) {
                                        linkmanEntityList.clear();
                                    }
                                    linkmanEntityList.addAll(getLinkmanListResultEntity.getLinkmanEntityList());
                                    for (int i = 0; i < linkmanEntityList.size(); i++) {
                                        if (i == 0) {
                                            selectLinkmanEntity = linkmanEntityList.get(i);
                                            linkmanEntityList.get(i).setCheckedFlag(1);
                                        } else {
                                            linkmanEntityList.get(i).setCheckedFlag(2);
                                        }
                                    }
                                    selectLinkmanAdapter.notifyDataSetChanged();
                                } else {
                                    Tools.showPrompt("没有常用联系人，请新建");
                                }
                            } else {
                                Tools.showPrompt(getLinkmanListResultEntity.getMsg());
                            }
                        } else {
                            NetUtil.toNetworkSetting(SelectLinkmanActivity.this);
                        }
                    }
                });
    }

    /* 请求读取联系人权限 */
    private static String[] PERMISSIONS_CONTACT = {Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS};

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_title_left:
                finish();
                break;
            case R.id.tv_create_linkman://新建联系人
                startActivityForResult(new Intent(SelectLinkmanActivity.this, CreateLinkmanActivity.class), 1);
                break;
            case R.id.tv_select_linkman_from_phone:
                // 判断权限是否拥有
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                        != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS)
                        != PackageManager.PERMISSION_GRANTED) {
                    // 读写联系人权限未被授予，需要申请！
                    ActivityCompat.requestPermissions(this, PERMISSIONS_CONTACT, 1);
                } else {
                    // 权限已经被授予，显示细节页面！
                    Intent intent1 = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                    SelectLinkmanActivity.this.startActivityForResult(intent1, 2);
                }
                break;
            case R.id.btn_select_linkman_ok:
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("selectLinkmanEntity", selectLinkmanEntity);
                intent.putExtras(bundle);
                SelectLinkmanActivity.this.setResult(3, intent);
                SelectLinkmanActivity.this.finish();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 1:
                    getLinkmanList();
                    break;
                case 2: {
                    Uri contactData = data.getData();
                    if (contactData == null) {
                        return;
                    }
                    Cursor cursor;
                    if (Build.VERSION.SDK_INT < 11) {
                        cursor = managedQuery(contactData, null, null, null, null);
                    } else {
                        CursorLoader cursorLoader = new CursorLoader(this, contactData, null, null, null, null);
                        cursor = cursorLoader.loadInBackground();
                    }
                    if (cursor.moveToFirst()) {
                        name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        String hasPhone = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                        String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                        if (hasPhone.equalsIgnoreCase("1")) {
                            hasPhone = "true";
                        } else {
                            hasPhone = "false";
                        }
                        if (Boolean.parseBoolean(hasPhone)) {
                            Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                    null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id, null, null);
                            while (phones.moveToNext()) {
                                phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            }
                            phones.close();
                        }
                        for (LinkmanEntity l : linkmanEntityList) {
                            l.setCheckedFlag(2);
                        }
                        LinkmanEntity entity = new LinkmanEntity();
                        entity.setLinkmanName(name);
                        entity.setLinkmanMobileNo(phoneNumber);
                        entity.setCheckedFlag(1);
                        entity.setRelationship("家属");
                        cursor.close();

                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("selectLinkmanEntity", entity);
                        intent.putExtras(bundle);
                        SelectLinkmanActivity.this.setResult(3, intent);
                        SelectLinkmanActivity.this.finish();
                    } else {
                        Tools.showPrompt("您的手机获取联系人权限需要在设置里手动开启");
                    }
                    Log.i("info", "联系人：" + name + "--" + phoneNumber);
                    break;
                }
                default:
                    break;
            }
        }
    }

    String name;
    String phoneNumber;

}