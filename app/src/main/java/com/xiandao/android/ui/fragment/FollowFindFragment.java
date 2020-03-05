package com.xiandao.android.ui.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;

import com.xiandao.android.R;
import com.xiandao.android.adapter.StaggeredGridAdapter;
import com.xiandao.android.entity.ThemeEntity;
import com.xiandao.android.entity.ThemeListEntity;
import com.xiandao.android.net.ApiHttpResult;
import com.xiandao.android.net.HttpUtils;
import com.xiandao.android.net.NetUtil;
import com.xiandao.android.ui.BaseLazyFragment;
import com.xiandao.android.utils.FileManagement;
import com.xiandao.android.utils.Tools;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zengx on 2019/5/27.
 * Describe:
 */
public class FollowFindFragment extends BaseLazyFragment {
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView rlv_find;
    private StaggeredGridAdapter adapter;
    private List<ThemeEntity> themeEntities;

    @Override
    protected void injectView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_find_all, null);
        setContentView(view);
        initView(view);
        getAllThemeList();
    }

    private void initView(View view) {
        rlv_find = (RecyclerView) view.findViewById(R.id.rlv_find);
        //设置布局方式,2列，垂直
        rlv_find.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        themeEntities = new ArrayList<>();
        adapter = new StaggeredGridAdapter(getActivity(), themeEntities);
        rlv_find.setAdapter(adapter);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        // 下拉刷新颜色控制
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                getAllThemeList();
            }

        });

    }

    /**
     * TODO：获取所有帖子
     */
    private void getAllThemeList() {
        startProgressDialog("");
        ApiHttpResult.getFollowFindList(getActivity(), new String[]{"ownersid"},
                new Object[]{FileManagement.getLoginUserEntity().getUserId()},
                new HttpUtils.DataCallBack() {
                    @Override
                    public void callBack(Object o) {
                        stopProgressDialog();
                        //停止刷新
                        swipeRefreshLayout.setRefreshing(false);
                        if (o != null) {
                            ThemeListEntity themeListEntity = (ThemeListEntity) o;
                            if (themeListEntity != null && themeListEntity.getResultCode().equals("0")) {
                                if (themeEntities.size() != 0) {
                                    themeEntities.clear();
                                }
                                themeEntities.addAll(themeListEntity.getData());
                                adapter.notifyDataSetChanged();
                            } else {
                                Tools.showPrompt(themeListEntity.getMsg());
                            }
                        } else {
                            NetUtil.toNetworkSetting(getActivity());
                        }
                    }
                });
    }

    @Override
    protected void onFragmentStartLazy() {
        super.onFragmentStartLazy();
        getAllThemeList();
    }
}
