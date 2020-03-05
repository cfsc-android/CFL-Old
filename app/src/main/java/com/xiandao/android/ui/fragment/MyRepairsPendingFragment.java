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
import com.xiandao.android.adapter.MyRepairsListAdapter;
import com.xiandao.android.entity.LoginUserEntity;
import com.xiandao.android.entity.MyRepairsEntity;
import com.xiandao.android.entity.MyRepairsListEntity;
import com.xiandao.android.net.ApiHttpResult;
import com.xiandao.android.net.HttpUtils;
import com.xiandao.android.net.NetUtil;
import com.xiandao.android.ui.BaseLazyFragment;
import com.xiandao.android.ui.activity.RepairsCommentActivity;
import com.xiandao.android.ui.activity.RepairsConfirmCostActivity;
import com.xiandao.android.ui.activity.RepairsDetailActivity;
import com.xiandao.android.ui.activity.LoginActivity;
import com.xiandao.android.utils.FileManagement;
import com.xiandao.android.utils.Tools;

import org.xutils.x;

import java.util.ArrayList;

/**
 * 此类描述的是:我的工单待处理fragment
 *
 * @author TanYong
 *         create at 2017/5/6 14:03
 */
public class MyRepairsPendingFragment extends BaseLazyFragment {

    PullToRefreshListView ptrlv_my_repairs_list;
    MyRepairsListAdapter myRepairsListAdapter;
    ArrayList<MyRepairsEntity> myRepairsListEntities = new ArrayList<>();

    private LoginUserEntity loginUserEntity;

    private int page = 1;//第几页
    private int pageSize = 10;//每页显示多少条
    private int totalPages = 0;//总的页面数

    //===========筛选条件=========
    private String startTime;//开始时间
    private String endTime;//结束时间
    private String workOrderType;//工单类别

    private boolean isFirstLoading = true;//判断是否首次加载

    @Override
    protected void injectView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_my_complain_list, null);
        x.view().inject(this, view);
        setContentView(view);

        loginUserEntity = FileManagement.getLoginUserEntity();
        startProgressDialog("");
        Bundle bundle = getArguments();
        if (null != bundle) {
            startTime = bundle.getString("startTime");
            endTime = bundle.getString("endTime");
            workOrderType = bundle.getString("workOrderType");
        }
        initView(view);
    }

    @Override
    protected void onFragmentStartLazy() {
        super.onFragmentStartLazy();
        page = 1;
        isFirstLoading = true;
        init();
    }

    private void initView(View view) {
        ptrlv_my_repairs_list = (PullToRefreshListView) view.findViewById(R.id.ptrlv_my_tousu_list);

        //下拉刷新与上拉加载
        ptrlv_my_repairs_list.setMode(PullToRefreshBase.Mode.BOTH);
        // 设置刷新文本说明(展开刷新栏前)为false,true返回设置上拉的。
        ptrlv_my_repairs_list.getLoadingLayoutProxy(false, true).setLastUpdatedLabel("");
        ptrlv_my_repairs_list.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在加载...");
        ptrlv_my_repairs_list.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载更多");
        ptrlv_my_repairs_list.getLoadingLayoutProxy(false, true).setReleaseLabel("释放开始加载");
        // true,false返回设置下拉
        ptrlv_my_repairs_list.getLoadingLayoutProxy(true, false).setRefreshingLabel("正在刷新...");
        ptrlv_my_repairs_list.getLoadingLayoutProxy(true, false).setPullLabel("下拉刷新");
        ptrlv_my_repairs_list.getLoadingLayoutProxy(true, false).setReleaseLabel("释放开始刷新");

        ptrlv_my_repairs_list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                startTime = "";
                endTime = "";
                workOrderType = "";
                page = 1;
                isFirstLoading = true;
                queryRepairsLis();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (page > totalPages) {
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ptrlv_my_repairs_list.onRefreshComplete();
                        }
                    }, 1000);
                    Tools.showPrompt("已加载完所有内容");
                } else {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            page++;
                            isFirstLoading = false;
                            queryRepairsLis();
                        }
                    });
                }
            }
        });
    }

    private void init() {
        myRepairsListAdapter = new MyRepairsListAdapter(getActivity(), myRepairsListEntities);
        ptrlv_my_repairs_list.setAdapter(myRepairsListAdapter);
        queryRepairsLis();
        ptrlv_my_repairs_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyRepairsEntity myRepairsEntity = myRepairsListEntities.get(position - 1);
                ArrayList<String> jbpmOutcomes = myRepairsEntity.getJbpmOutcomes();
                String selectRepairsId = Tools.getStringValue(myRepairsEntity.getRepairsId());
                String selectTaskId = Tools.getStringValue(myRepairsEntity.getTaskid());
                start(jbpmOutcomes, selectRepairsId, selectTaskId);
            }
        });
    }

    /**
     * @author TanYong
     * create at 2017/6/14 9:57
     * TODO：跳转到不同的界面
     */
    private void start(ArrayList<String> jbpmOutcomes, String selectRepairsId, String selectTaskId) {
        Intent intent;
        if (jbpmOutcomes != null && jbpmOutcomes.size() > 0) {
            if (jbpmOutcomes.indexOf("是否接受价格") != -1) {
                intent = new Intent(getActivity(), RepairsConfirmCostActivity.class);
                intent.putExtra("repairsId", selectRepairsId);
                intent.putExtra("taskId", selectTaskId);
                intent.putExtra("repairsStatus", 0);//确认费用
                startActivity(intent);
            } else if (jbpmOutcomes.indexOf("客户确认") != -1) {
                intent = new Intent(getActivity(), RepairsConfirmCostActivity.class);
                intent.putExtra("repairsId", selectRepairsId);
                intent.putExtra("taskId", selectTaskId);
                intent.putExtra("repairsStatus", 1);//客户确认是评价还是付费
                startActivity(intent);
            } else if (jbpmOutcomes.indexOf("客户付费") != -1) {
                intent = new Intent(getActivity(), RepairsConfirmCostActivity.class);
                intent.putExtra("repairsId", selectRepairsId);
                intent.putExtra("taskId", selectTaskId);
                intent.putExtra("repairsStatus", 2);//付费
                startActivity(intent);
            } else if (jbpmOutcomes.indexOf("评价") != -1) {
                intent = new Intent(getActivity(), RepairsCommentActivity.class);
                intent.putExtra("workOrderId", selectRepairsId);
                intent.putExtra("taskId", selectTaskId);
//                intent.putExtra("repairsStatus", 3);//评价
                startActivity(intent);
//                intent = new Intent(getActivity(), RepairsConfirmCostActivity.class);
//                intent.putExtra("repairsId", selectRepairsId);
//                intent.putExtra("taskId", selectTaskId);
//                intent.putExtra("repairsStatus", 3);//评价
//                startActivity(intent);
            } else {
                intent = new Intent(getActivity(), RepairsDetailActivity.class);
                intent.putExtra("repairsId", selectRepairsId);
                startActivity(intent);
            }
        } else {
            intent = new Intent(getActivity(), RepairsDetailActivity.class);
            intent.putExtra("repairsId", selectRepairsId);
            startActivity(intent);
        }
    }

    /**
     * @author TanYong
     * create at 2017/5/6 14:05
     * handerstatu=2 未处理工单
     * TODO：查询工单列表
     */
    private void queryRepairsLis() {
        if (!Tools.isEmpty(startTime)) {
            startTime = startTime + ":00";
        }
        if (!Tools.isEmpty(endTime)) {
            endTime = endTime + ":59";
        }
        ApiHttpResult.queryRepairsList(getActivity(), new String[]{
                        "handerstatu", "userId", "appMobile", "page", "pageSize", "startTime", "endTime", "workOrderType"},
                new Object[]{
                        "0", loginUserEntity.getUserId(), loginUserEntity.getMobileNumber(), page, pageSize,
                        startTime, endTime, workOrderType},
                new HttpUtils.DataCallBack<Object>() {
                    @Override
                    public void callBack(Object o) {
                        stopProgressDialog();
                        if (o != null) {
                            MyRepairsListEntity myRepairsListEntity = (MyRepairsListEntity) o;
                            if (myRepairsListEntity != null && myRepairsListEntity.getResultCode().equals("0")) {
                                ArrayList<MyRepairsEntity> myRepairsEntities = myRepairsListEntity.getRepairsEntityList();
                                if (myRepairsEntities != null && myRepairsEntities.size() > 0) {
                                    totalPages = (int) Math.ceil(Tools.isEmpty(myRepairsListEntity.getPagination().getTotalResults() + "")
                                            ? 1 : myRepairsListEntity.getPagination().getTotalResults() / pageSize);

                                    if (isFirstLoading) {
                                        myRepairsListEntities.clear();
                                    }
                                    myRepairsListEntities.addAll(myRepairsEntities);
                                    myRepairsListAdapter.notifyDataSetChanged();
                                    if (ptrlv_my_repairs_list != null) {
                                        ptrlv_my_repairs_list.onRefreshComplete();
                                    }
                                } else {
                                    Tools.showPrompt("没有待处理工单数据");
                                }
                            } else {
                                Tools.showPrompt(myRepairsListEntity.getMsg());
                            }
                        } else {
                            NetUtil.toNetworkSetting(getActivity());
                        }
                    }
                });
    }

}
