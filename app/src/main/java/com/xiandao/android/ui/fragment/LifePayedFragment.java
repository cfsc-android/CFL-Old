package com.xiandao.android.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.xiandao.android.R;
import com.xiandao.android.adapter.LifePayListAdapter;
import com.xiandao.android.entity.LifePayEntity;
import com.xiandao.android.entity.LoginUserEntity;
import com.xiandao.android.entity.LifePayDataEntity;
import com.xiandao.android.entity.MyComplainListEntity;
import com.xiandao.android.net.ApiHttpResult;
import com.xiandao.android.net.HttpUtils;
import com.xiandao.android.net.NetUtil;
import com.xiandao.android.ui.BaseLazyFragment;
import com.xiandao.android.ui.activity.ComplainFinishActivity;
import com.xiandao.android.ui.activity.LifePayActivity;
import com.xiandao.android.ui.activity.LifePayDetailActivity;
import com.xiandao.android.ui.activity.LoginActivity;
import com.xiandao.android.utils.FileManagement;
import com.xiandao.android.utils.Tools;

import org.xutils.x;

import java.util.ArrayList;

/**
 * 此类描述的是:生活缴费已支付fragment
 *
 * @author TanYong
 *         create at 2017/5/6 14:15
 */
public class LifePayedFragment extends BaseLazyFragment {


    PullToRefreshListView ptrlv_life_pay_list;
    LifePayListAdapter lifePayListAdapter;
    ArrayList<LifePayDataEntity> lifePayDataEntities = new ArrayList<>();
    private LoginUserEntity loginUserEntity;

    private int page = 1;//第几页
    private int pageSize = 10;//每页显示多少条
    private int totalPages = 0;//总的页面数

    //===========筛选条件=========
    private String payTime;//支付时间
    private String lastTerm;//最后期限

    private boolean isFirstLoading = true;//判断是否首次加载

    private String roomid;

    @Override
    protected void injectView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_life_pay, null);
        x.view().inject(this, view);
        setContentView(view);
        loginUserEntity = FileManagement.getLoginUserEntity();
        roomid = FileManagement.getRoomInfo().get(0).getRoomId();
        Bundle bundle = getArguments();
        if (null != bundle) {
            payTime = bundle.getString("payTime");
            lastTerm = bundle.getString("lastTerm");
        }
        initView(view);
    }

    private void initView(View view) {
        ptrlv_life_pay_list = (PullToRefreshListView) view.findViewById(R.id.ptrlv_life_pay_list);

        //下拉刷新与上拉加载
        ptrlv_life_pay_list.setMode(PullToRefreshBase.Mode.BOTH);
        // 设置刷新文本说明(展开刷新栏前)为false,true返回设置上拉的。
        ptrlv_life_pay_list.getLoadingLayoutProxy(false, true).setLastUpdatedLabel("");
        ptrlv_life_pay_list.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在加载...");
        ptrlv_life_pay_list.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载更多");
        ptrlv_life_pay_list.getLoadingLayoutProxy(false, true).setReleaseLabel("释放开始加载");
        // true,false返回设置下拉
        ptrlv_life_pay_list.getLoadingLayoutProxy(true, false).setRefreshingLabel("正在刷新...");
        ptrlv_life_pay_list.getLoadingLayoutProxy(true, false).setPullLabel("下拉刷新");
        ptrlv_life_pay_list.getLoadingLayoutProxy(true, false).setReleaseLabel("释放开始刷新");


        ptrlv_life_pay_list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                payTime = "";
                lastTerm = "";
                page = 1;
                isFirstLoading = true;
                queryLifePay();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (page > totalPages) {
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ptrlv_life_pay_list.onRefreshComplete();
                        }
                    }, 1000);
                    Tools.showPrompt("已加载完所有内容");
                } else {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            page++;
                            isFirstLoading = false;
                            queryLifePay();
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onFragmentStartLazy() {
        super.onFragmentStartLazy();
        page = 1;
        isFirstLoading = true;
        init();
    }


    private void init() {
        lifePayListAdapter = new LifePayListAdapter(getActivity(), lifePayDataEntities);
        ptrlv_life_pay_list.setAdapter(lifePayListAdapter);
        startProgressDialog("");
        queryLifePay();
        ptrlv_life_pay_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LifePayDataEntity entity = lifePayDataEntities.get(position - 1);
                Bundle bundle = new Bundle();
                bundle.putSerializable("lifePay", entity);
                bundle.putBoolean("isPayed", true);
                openActivity(LifePayDetailActivity.class, bundle);
            }
        });
    }

    /**
     * @author TanYong
     * create at 2017/5/6 14:05
     * TODO：查询生活缴费
     */
    private void queryLifePay() {
        if (!Tools.isEmpty(payTime)) {
            payTime = payTime + ":00";
        }
        if (!Tools.isEmpty(lastTerm)) {
            lastTerm = lastTerm + ":59";
        }
        ApiHttpResult.queryLifePay(getActivity(), new String[]{
                        "roomid", "appMobile", "page", "pageSize", "ifpay", "paydate", "shouldpaydate"},
                new Object[]{
                        roomid, loginUserEntity.getMobileNumber(), page, pageSize, "1", payTime, lastTerm},
                new HttpUtils.DataCallBack<Object>() {
                    @Override
                    public void callBack(Object o) {
                        stopProgressDialog();
                        if (o != null) {
                            LifePayEntity lifePayEntity = (LifePayEntity) o;
                            if (lifePayEntity != null) {
                                if (lifePayEntity.getResultCode().equals("0")) {
                                    ArrayList<LifePayDataEntity> data = lifePayEntity.getData();
                                    if (data != null && data.size() > 0) {
                                        if (isFirstLoading) {
                                            lifePayDataEntities.clear();
                                        }
                                        lifePayDataEntities.addAll(data);
                                        lifePayListAdapter.notifyDataSetChanged();
                                        if (ptrlv_life_pay_list != null) {
                                            ptrlv_life_pay_list.onRefreshComplete();
                                        }
                                    } else {
                                        Tools.showPrompt("没有待支付数据");
                                    }
                                } else if (lifePayEntity.getResultCode().equals("-100")) {
                                    openActivity(LoginActivity.class);
                                    FileManagement.saveTokenInfo("");
                                    Tools.showPrompt(lifePayEntity.getMsg());
                                    getActivity().finish();
                                }
                            } else {
                                Tools.showPrompt(lifePayEntity.getMsg());
                            }
                        } else {
                            NetUtil.toNetworkSetting(getActivity());
                        }
                    }
                });
    }
}
