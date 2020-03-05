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
import com.xiandao.android.ui.activity.ComplainCommentActivity;
import com.xiandao.android.ui.activity.ComplainConfirmProgrammeActivity;
import com.xiandao.android.ui.activity.ComplainConfirmReformActivity;
import com.xiandao.android.ui.activity.ComplainConfirmResultActivity;
import com.xiandao.android.ui.activity.ComplainFinishActivity;
import com.xiandao.android.ui.activity.LoginActivity;
import com.xiandao.android.ui.activity.ComplainDetailActivity;
import com.xiandao.android.utils.FileManagement;
import com.xiandao.android.utils.Tools;

import org.xutils.x;

import java.util.ArrayList;

/**
 * 此类描述的是:投诉待处理fragment
 *
 * @author TanYong
 *         create at 2017/5/6 14:03
 */
public class MyComplainPendingFragment extends BaseLazyFragment {

    PullToRefreshListView ptrlv_my_tousu_list;
    MyComplainListAdapter myComplainListAdapter;
    ArrayList<MyComplainEntity> myComplainListEntities = new ArrayList<MyComplainEntity>();

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

    @Override
    protected void onFragmentStartLazy() {
        super.onFragmentStartLazy();
        page = 1;
        isFirstLoading = true;
        startProgressDialog("");
        queryMyComplainList();
    }

    private void initView(View view) {
        ptrlv_my_tousu_list = (PullToRefreshListView) view.findViewById(R.id.ptrlv_my_tousu_list);

        myComplainListAdapter = new MyComplainListAdapter(getActivity(), myComplainListEntities);
        ptrlv_my_tousu_list.setAdapter(myComplainListAdapter);

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

        ptrlv_my_tousu_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyComplainEntity complainEntity = myComplainListEntities.get(position - 1);
                ArrayList<String> jbpmOutcomes = complainEntity.getJbpmOutcomes();
                String selectComplainId = Tools.getStringValue(complainEntity.getComplainId());
                String selectTaskId = Tools.getStringValue(complainEntity.getTaskid());
                String selectPiid = Tools.getStringValue(complainEntity.getPiid());
                String selectStatus = Tools.getStringValue(complainEntity.getComplainStatus());
                start(jbpmOutcomes, selectComplainId, selectTaskId, selectPiid, selectStatus);
            }
        });

        startProgressDialog("");
        queryMyComplainList();
    }

    /**
     * @author TanYong
     * create at 2017/6/14 9:37
     * TODO：启动不同的界面
     */
    private void start(ArrayList<String> jbpmOutcomes, String selectComplainId, String selectTaskId, String selectPiid, String selectStatus) {
        if (jbpmOutcomes.indexOf("是否接受") != -1) {
            Bundle bundle = new Bundle();
            bundle.putString("complainId", selectComplainId);
            bundle.putString("taskId", selectTaskId);
            bundle.putString("piid", selectPiid);
            bundle.putBoolean("isDetail", false);
            openActivity(ComplainConfirmProgrammeActivity.class, bundle);
        } else if (jbpmOutcomes.indexOf("是否满意") != -1) {
            Bundle bundle = new Bundle();
            bundle.putString("complainId", selectComplainId);
            bundle.putString("taskId", selectTaskId);
            bundle.putString("piid", selectPiid);
            bundle.putBoolean("isDetail", false);
            openActivity(ComplainConfirmResultActivity.class, bundle);
        } else if (jbpmOutcomes.indexOf("评价") != -1) {
            Bundle bundle = new Bundle();
            bundle.putString("complainId", selectComplainId);
            bundle.putString("taskId", selectTaskId);
            openActivity(ComplainCommentActivity.class, bundle);
        } else if (jbpmOutcomes.indexOf("业主是否接受整改") != -1) {
            Bundle bundle = new Bundle();
            bundle.putString("complainId", selectComplainId);
            bundle.putString("taskId", selectTaskId);
            bundle.putBoolean("isDetail", false);
            openActivity(ComplainConfirmReformActivity.class, bundle);
        } else {
            Intent intent = new Intent(getActivity(), ComplainFinishActivity.class);
            intent.putExtra("complainId", selectComplainId);
            startActivity(intent);
//            if ("用户不接受整改措施,无上级领导处理，关闭投诉".equals(selectStatus)
//                    || "解决方案实施中".equals(selectStatus)
//                    || "员工完成解决方案".equals(selectStatus)) {
//                Bundle bundle = new Bundle();
//                bundle.putString("complainId", selectComplainId);
//                bundle.putString("taskId", selectTaskId);
//                bundle.putString("piid", selectPiid);
//                bundle.putBoolean("isDetail", true);
//                openActivity(ComplainConfirmProgrammeActivity.class, bundle);
//            } else if ("投诉处理不满意".equals(selectStatus)) {
//                Bundle bundle = new Bundle();
//                bundle.putString("complainId", selectComplainId);
//                bundle.putBoolean("isDetail", true);
//                openActivity(ComplainConfirmResultActivity.class, bundle);
//            } else if ("用户接受整改措施".equals(selectStatus)) {
//                Bundle bundle = new Bundle();
//                bundle.putString("complainId", selectComplainId);
//                bundle.putBoolean("isDetail", true);
//                openActivity(ComplainConfirmReformActivity.class, bundle);
//            } else if ("未受理".equals(selectStatus)) {
//                Bundle bundle = new Bundle();
//                bundle.putString("complainId", selectComplainId);
//                openActivity(ComplainDetailActivity.class, bundle);
//            }
        }
    }

    /**
     * @author TanYong
     * create at 2017/5/6 14:05
     * userId=7&disposeStatus=1&page=1&pageSize=10&appTokenInfo=123&appMobile=123
     * disposeStatus=1 未处理投诉
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
                        startTime, endTime, complainType, ""},
                new HttpUtils.DataCallBack<Object>() {
                    @Override
                    public void callBack(Object o) {
                        stopProgressDialog();
                        if (ptrlv_my_tousu_list != null) {
                            ptrlv_my_tousu_list.onRefreshComplete();
                        }
                        if (o != null) {
                            MyComplainListEntity myComplainListEntity = (MyComplainListEntity) o;
                            if (myComplainListEntity != null) {
                                if (myComplainListEntity.getResultCode().equals("0")) {
                                    ArrayList<MyComplainEntity> myComplainEntities = myComplainListEntity.getComplainEntityList();
                                    if (myComplainEntities != null && myComplainEntities.size() > 0) {
                                        totalPages = (int) Math.ceil(Tools.isEmpty(myComplainListEntity.getPagination().getTotalResults() + "")
                                                ? 1 : myComplainListEntity.getPagination().getTotalResults() / pageSize);
                                        if (isFirstLoading) {
                                            myComplainListEntities.clear();
                                        }
                                        myComplainListEntities.addAll(myComplainEntities);
                                        myComplainListAdapter.notifyDataSetChanged();
                                    } else {
                                        Tools.showPrompt("没有待处理投诉数据");
                                    }
                                } else if (myComplainListEntity.getResultCode().equals("-100")) {
                                    openActivity(LoginActivity.class);
                                    FileManagement.saveTokenInfo("");
                                    Tools.showPrompt(myComplainListEntity.getMsg());
                                    getActivity().finish();
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
