package com.xiandao.android.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xiandao.android.R;
import com.xiandao.android.entity.ThemeDetailsEntity;
import com.xiandao.android.entity.ThemeEntity;
import com.xiandao.android.http.MyCallBack;
import com.xiandao.android.http.XUtils;
import com.xiandao.android.net.ApiHttpResult;
import com.xiandao.android.net.HttpUtils;
import com.xiandao.android.net.NetUtil;
import com.xiandao.android.ui.BaseActivity;
import com.xiandao.android.utils.Constants;
import com.xiandao.android.utils.FileManagement;
import com.xiandao.android.utils.Tools;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.event.annotation.ContentView;
import org.xutils.event.annotation.ViewInject;

import java.util.HashMap;
import java.util.Map;

@ContentView(R.layout.activity_find_details)
public class FindDetailsActivity extends BaseActivity implements View.OnClickListener {
    @ViewInject(R.id.iv_title_left)
    private ImageView iv_title_left;
    @ViewInject(R.id.iv_title_right)
    private ImageView iv_title_right;
    @ViewInject(R.id.tv_title_right)
    private TextView tv_title_right;
    @ViewInject(R.id.tv_title_name)
    private TextView tv_title_name;
    @ViewInject(R.id.tv_find_details_nick_name)
    private TextView tv_find_details_nick_name;
    @ViewInject(R.id.tv_find_details_time)
    private TextView tv_find_details_time;
//    @ViewInject(R.id.iv_find_details_pic)
//    private ImageView iv_find_details_pic;
    @ViewInject(R.id.tv_find_details_content)
    private TextView tv_find_details_content;
    @ViewInject(R.id.tv_find_details_follow)
    private TextView tv_find_details_follow;
    @ViewInject(R.id.iv_find_details_zan)
    private ImageView iv_find_details_zan;
    @ViewInject(R.id.rl_find_details_zan)
    private RelativeLayout rl_find_details_zan;
    @ViewInject(R.id.ll_find_details_follow)
    private LinearLayout ll_find_details_follow;
    @ViewInject(R.id.ll_pic_list)
    private LinearLayout ll_pic_list;

    private ThemeEntity tansThemeRntitie;
    private ThemeEntity themeEntitie;
    private int id;
    private int ownerid;
    private boolean isFollow = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        getThemeDetails();
    }

    private void init() {
        tansThemeRntitie= (ThemeEntity) getIntent().getSerializableExtra("themeEntity");
        id = tansThemeRntitie.getId();
        ownerid=Integer.parseInt(FileManagement.getLoginUserEntity().getUserId());
        tv_title_name.setText("新鲜事");
        iv_title_left.setOnClickListener(this);
        rl_find_details_zan.setOnClickListener(this);
        ll_find_details_follow.setOnClickListener(this);
//        initView();
    }

    /**
     * TODO：获取帖子详情
     */
    private void getThemeDetails() {
        startProgressDialog("");
        ApiHttpResult.getThemeDetails(this, new String[]{"id", "ownerid"},
                new Object[]{id, ownerid},
                new HttpUtils.DataCallBack() {
                    @Override
                    public void callBack(Object o) {
                        stopProgressDialog();
                        if (o != null) {
                            ThemeDetailsEntity themeDetailsEntity = (ThemeDetailsEntity) o;
                            if (themeDetailsEntity.getResultCode().equals("0")) {
                                themeEntitie = themeDetailsEntity.getData();
                                if (themeEntitie != null) {
                                    initView();
                                }
                            } else {
                                Tools.showPrompt(themeDetailsEntity.getMsg());
                            }
                        } else {
                            NetUtil.toNetworkSetting(FindDetailsActivity.this);
                        }
                    }
                });
    }

    /**
     * TODO：点赞
     */
    private void doThumbsUp() {
        startProgressDialog("");
        ApiHttpResult.doThumbsUp(this, new String[]{"themeid", "ownersid"},
                new Object[]{id, ownerid},
                new HttpUtils.DataCallBack() {
                    @Override
                    public void callBack(Object o) {
                        stopProgressDialog();
                        if (o != null) {
                            String[] status = (String[]) o;
                            Tools.showPrompt(status[1]);
                            if ("0".equals(status[0])) {
                                rl_find_details_zan.setEnabled(false);
                                iv_find_details_zan.setImageResource(R.drawable.btn_find_like_sel);
                            }
                        } else {
                            NetUtil.toNetworkSetting(FindDetailsActivity.this);
                        }
                    }
                });
    }

    /**
     * TODO：关注
     */
    private void doFollow() {
        startProgressDialog("");
        ApiHttpResult.doFollow(this, new String[]{"themeid", "ownersid"},
                new Object[]{id, ownerid},
                new HttpUtils.DataCallBack() {
                    @Override
                    public void callBack(Object o) {
                        stopProgressDialog();
                        if (o != null) {
                            String[] status = (String[]) o;
                            Tools.showPrompt(status[1]);
                            if ("0".equals(status[0])) {
                                tv_find_details_follow.setText("已关注");
                                Drawable drawable=getResources().getDrawable(R.drawable.icon_public_sel);
                                drawable.setBounds(0, 0, 68, 68);
                                tv_find_details_follow.setCompoundDrawables(drawable,null,null,null);
                                isFollow = true;
                            }
                        } else {
                            NetUtil.toNetworkSetting(FindDetailsActivity.this);
                        }
                    }
                });
    }

    /**
     * TODO：取消关注
     */
    private void doCancelFollow() {
        startProgressDialog("");
        ApiHttpResult.doCancelFollow(this, new String[]{"themeid", "ownersid"},
                new Object[]{id, ownerid},
                new HttpUtils.DataCallBack() {
                    @Override
                    public void callBack(Object o) {
                        stopProgressDialog();
                        if (o != null) {
                            String[] status = (String[]) o;
                            Tools.showPrompt(status[1]);
                            if ("0".equals(status[0])) {
                                tv_find_details_follow.setText("关注");
                                Drawable drawable=getResources().getDrawable(R.drawable.btn_home_add);
                                drawable.setBounds(0, 0, 68, 68);
                                tv_find_details_follow.setCompoundDrawables(drawable,null,null,null);
                                isFollow = false;
                            }
                        } else {
                            NetUtil.toNetworkSetting(FindDetailsActivity.this);
                        }
                    }
                });
    }

    /**
     * TODO: 删除贴子
     */
    private void deleteTheme(){
        final Map<String,Object> requestMap=new HashMap<>();
        requestMap.put("id",id);
        XUtils.Post(Constants.HOST+"/theme/delByThemeId.action",requestMap,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                Log.e("result",result);
                try {
                    JSONObject jsonObject=new JSONObject(result);
                    if("0".equals(jsonObject.getString("resultCode"))){
                        finish();
                    }else{
                        showShortToast(jsonObject.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                showShortToast(ex.getMessage());
            }

            @Override
            public void onFinished() {
                super.onFinished();
            }
        });
    }

    private void initView() {
        tv_find_details_nick_name.setText("来源：" + themeEntitie.getOwnerName());
        tv_find_details_time.setText(themeEntitie.getTime());
        if (themeEntitie.getPicList() != null && themeEntitie.getPicList().size() != 0) {
            for (final ThemeEntity.PicListBean picListBean : themeEntitie.getPicList()) {
                ImageView imageView=new ImageView(this);
                LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,480);
                lp.leftMargin=100;
                lp.rightMargin=100;
                lp.topMargin=30;
                imageView.setLayoutParams(lp);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("path", picListBean.getUrl());
                        openActivity(ShowLargeImageActivity.class, bundle);
                    }
                });
                Glide.with(this)
                        .load(Constants.BASEHOST + "/" + picListBean.getUrl())
                        .placeholder(R.drawable.pic_find_2)
                        .error(R.drawable.pic_find_2)
                        .into(imageView);
                ll_pic_list.addView(imageView);
            }
        }

        tv_find_details_content.setText(themeEntitie.getContent());
        if (themeEntitie.getIfUp() != 0) {
            rl_find_details_zan.setEnabled(false);
            iv_find_details_zan.setImageResource(R.drawable.btn_find_like_sel);
        }
        if (themeEntitie.getIfFollow() != 0) {
            isFollow = true;
            tv_find_details_follow.setText("已关注");
            Drawable drawable=getResources().getDrawable(R.drawable.icon_public_sel);
            drawable.setBounds(0, 0, 38, 38);
            tv_find_details_follow.setCompoundDrawables(drawable,null,null,null);
        }
        if(themeEntitie.getOwnerid() ==Integer.parseInt(FileManagement.getLoginUserEntity().getUserId())){
            iv_title_right.setVisibility(View.GONE);
            tv_title_right.setText("删除");
            tv_title_right.setVisibility(View.VISIBLE);
            tv_title_right.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_title_left:
                finish();
                break;
            case R.id.rl_find_details_zan://点赞
                doThumbsUp();
                break;
            case R.id.ll_find_details_follow:
                if (isFollow) {
                    doCancelFollow();//取消关注
                } else {
                    doFollow();//关注
                }
                break;
            case R.id.tv_title_right:
                new AlertDialog.Builder(FindDetailsActivity.this)
                        .setTitle("删除提示")
                        .setMessage("确认要删除该信息？")
                        .setCancelable(true)
                        .setNegativeButton(
                                "确认删除",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        deleteTheme();
                                    }
                                }).show();

                break;
        }
    }
}
