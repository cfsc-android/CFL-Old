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
import com.xiandao.android.adapter.MyComplainListAdapter;
import com.xiandao.android.entity.LoginUserEntity;
import com.xiandao.android.entity.MyComplainEntity;
import com.xiandao.android.entity.MyComplainListEntity;
import com.xiandao.android.net.ApiHttpResult;
import com.xiandao.android.net.HttpUtils;
import com.xiandao.android.net.NetUtil;
import com.xiandao.android.ui.BaseLazyFragment;
import com.xiandao.android.ui.activity.ComplainDetailActivity;
import com.xiandao.android.ui.activity.ComplainFinishActivity;
import com.xiandao.android.ui.activity.LoginActivity;
import com.xiandao.android.utils.FileManagement;
import com.xiandao.android.utils.Tools;

import org.xutils.x;

import java.util.ArrayList;

/**
 * 此类描述的是:投诉已处理fragment
 *
 * @author TanYong
 *         create at 2017/5/6 14:15
 */
public class MyComplainProcessedFragment extends BaseLazyFragment {


    PullToRefreshListView ptrlv_my_tousu_list;
    MyComplainListAdapter myComplainListAdapter;
    ArrayList<MyComplainEntity> myComplainListEntities = new ArrayList<>();
    private LoginUserEntity loginUserEntity;

    private int page = 1;//第几页
    private int pageSize = 10;//每页显示多少条
    private int totalPages = 0;//总的页面数

    //===========筛选条件=========
    private String startTime;//开始时间
    private String endTime;//结束时间
    private String complainType;//投诉类别

    private boolean isFirstLoading = true;//判断是否首次加载

    @Override
    protected void injectView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_my_complain_list, null);
        x.view().inject(this, view);
        setContentView(view);
        loginUserEntity = FileManagement.getLoginUserEntity();
        Bundle bundle = getArguments();
        if (null != bundle) {
            startTime = bundle.getString("startTime");
            endTime = bundle.getString("endTime");
            complainType = bundle.getString("complainType");
        }
        initView(view);
    }

    private void initView(View view) {
        ptrlv_my_tousu_list = (PullToRefreshListView) view.findViewById(R.id.ptrlv_my_tousu_list);

        //下拉刷新与上拉加载
        ptrlv_my_tousu_list.setMode(PullToRefreshBase.Mode.BOTH);
        // 设置刷新文本说明(展开刷新栏前)为false,true返回设置上拉的。
        ptrlv_my_tousu_list.getLoadingLayoutProxy(false, true).setLastUpdatedLabel("");
        ptrlv_my_tousu_list.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在加载...");
        ptrlv_my_tousu_list.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载更多");
        ptrlv_my_tousu_list.getLoadingLayoutProxy(false, true).setReleaseLabel("释放开始加载");
        // true,false返回设置下拉
        ptrlv_my_tousu_list.getLoadingLayoutProxy(true, false).setRefreshingLabel("正在刷新...");
        ptrlv_my_tousu_list.getLoadingLayoutProxy(true, false).setPullLabel("下拉刷新");
        ptrlv_my_tousu_list.getLoadingLayoutProxy(true, false).setReleaseLabel("释放开始刷新");


        ptrlv_my_tousu_list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                startTime = "";
                endTime = "";
                complainType = "";
                page = 1;
                isFirstLoading = true;
                queryMyComplainList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (page > totalPages) {
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ptrlv_my_tousu_list.onRefreshComplete();
                        }
                    }, 1000);
                    Tools.showPrompt("已加载完所有内容");
                } else {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            page++;
                            isFirstLoading = false;
                            queryMyComplainList();
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
        myComplainListAdapter = new MyComplainListAdapter(getActivity(), myComplainListEntities);
        ptrlv_my_tousu_list.setAdapter(myComplainListAdapter);
        startProgressDialog("");
        queryMyComplainList();
        ptrlv_my_tousu_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectComplainId = myComplainListEntities.get(position - 1).getComplainId();
                start(selectComplainId);
            }
        });
    }

    /**
     * @author TanYong
     * create at 2017/6/14 9:53
     * TODO：跳转界面
     */
    private void start(String selectComplainId) {
        Intent intent = new Intent(getActivity(), ComplainFinishActivity.class);
        intent.putExtra("complainId", selectComplainId);
        startActivity(intent);
    }

    /**
     * @author TanYong
     * create at 2017/5/6 14:05
     * disposeStatus:=2 已处理投诉
     * TODO：查询投诉列表
     */
    private void queryMyComplainList() {
        if (!Tools.isEmpty(startTime)) {
            startTime = startTime + ":00";
        }
        if (!Tools.isEmpty(endTime)) {
            endTime = endTime + ":59";
        }
        ApiHttpResult.queryMyComplainList(getActivity(), new String[]{
                        "userId", "appMobile", "page", "pageSize", "startTime", "endTime", "complainTypeId", "disposeStatus"},
                new Object[]{
                        loginUserEntity.getUserId(), loginUserEntity.getMobileNumber(), page, pageSize,
                        startTime, endTime, complainType, "2"},
                new HttpUtils.DataCallBack<Object>() {
                    @Override
                    public void callBack(Object o) {
                        stopProgressDialog();
                        if (o != null) {
                            MyComplainListEntity myComplainListEntity = (MyComplainListEntity) o;
                            if (myComplainListEntity != null && myComplainListEntity.getResultCode().equals("0")) {
                                ArrayList<MyComplainEntity> myComplainEntities = myComplainListEntity.getComplainEntityList();
                                if (myComplainEntities != null && myComplainEntities.size() > 0) {
                                    totalPages = (int) Math.ceil(Tools.isEmpty(myComplainListEntity.getPagination().getTotalResults() + "")
                                            ? 1 : myComplainListEntity.getPagination().getTotalResults() / pageSize);
                                    if (isFirstLoading) {
                                        myComplainListEntities.clear();
                                    }
                                    myComplainListEntities.addAll(myComplainEntities);
                                    myComplainListAdapter.notifyDataSetChanged();
                                    if (ptrlv_my_tousu_list != null) {
                                        ptrlv_my_tousu_list.onRefreshComplete();
                                    }
                                } else {
                                    Tools.showPrompt("没有待处理投诉数据");
                                }
                            } else {
                                Tools.showPrompt(myComplainListEntity.getMsg());
                            }
                        } else {
                            NetUtil.toNetworkSetting(getActivity());
                        }
                    }
                });
    }
}
