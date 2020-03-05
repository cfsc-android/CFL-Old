package com.xiandao.android.ui.activity.shop;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.xiandao.android.R;
import com.xiandao.android.adapter.GoodsListAdapter;
import com.xiandao.android.entity.GoodsEntity;
import com.xiandao.android.entity.GoodsListResultEntity;
import com.xiandao.android.entity.OverallSituationEntity;
import com.xiandao.android.net.ApiHttpResult;
import com.xiandao.android.net.HttpUtils;
import com.xiandao.android.net.NetUtil;
import com.xiandao.android.ui.BaseActivity;
import com.xiandao.android.utils.Tools;

import org.xutils.event.annotation.ContentView;
import org.xutils.event.annotation.ViewInject;

import java.util.ArrayList;


/**
 * 此类描述的是:商品列表activity
 *
 * @author TanYong
 *         create at 2017/6/14 17:09
 */
@ContentView(R.layout.activity_goods_list)
public class GoodsListActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.iv_title_left)
    private ImageView ivTitleLeft;
    @ViewInject(R.id.tv_title_name)
    private TextView tvTitleName;
    @ViewInject(R.id.ptrgv_goods_list)
    private PullToRefreshGridView ptrgv_goods_list;

    private ArrayList<GoodsEntity> goodsEntityArrayList;
    private GoodsListAdapter goodsListAdapter;
    private int page = 1;
    private int rows = 10;
    private int totalPages = 0;//总的页面数
    private String typeid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        tvTitleName.setText("商品列表");
        ivTitleLeft.setOnClickListener(this);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        typeid = bundle.getString("typeid");

        goodsEntityArrayList = new ArrayList<>();
        goodsListAdapter = new GoodsListAdapter(GoodsListActivity.this, goodsEntityArrayList, new GoodsListAdapter.ForBuyCar() {
            //商品id（商品id=数量&商品id=数量）
            @Override
            public void add(String goodses, String homeid) {
                addBuyCar(goodses + "=1", homeid);
            }
        });
        ptrgv_goods_list.setAdapter(goodsListAdapter);
        ptrgv_goods_list.setMode(PullToRefreshBase.Mode.BOTH);
        ptrgv_goods_list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        goodsEntityArrayList.clear();
                        page = 1;
                        queryGoodsList();
                    }
                });
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
                if (page > totalPages) {
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ptrgv_goods_list.onRefreshComplete();
                        }
                    }, 1000);
                    Tools.showPrompt("已加载完所有内容");
                } else {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            page++;
                            queryGoodsList();
                        }
                    });
                }
            }
        });

        ptrgv_goods_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle1 = new Bundle();
                bundle1.putSerializable("GoodsEntity", goodsEntityArrayList.get(position));
                openActivity(GoodsDetailActivity.class, bundle1);
            }
        });
        setPullToRefreshScrollViewInfo();
        queryGoodsList();
    }

    public void setPullToRefreshScrollViewInfo() {
        // 设置刷新文本说明(展开刷新栏前)为false,true返回设置上拉的。
        ptrgv_goods_list.getLoadingLayoutProxy(false, true).setLastUpdatedLabel("");
        ptrgv_goods_list.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在加载...");
        ptrgv_goods_list.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载更多");
        ptrgv_goods_list.getLoadingLayoutProxy(false, true).setReleaseLabel("释放开始加载");
        // true,false返回设置下拉
        ptrgv_goods_list.getLoadingLayoutProxy(true, false).setRefreshingLabel("正在刷新...");
        ptrgv_goods_list.getLoadingLayoutProxy(true, false).setPullLabel("下拉刷新");
        ptrgv_goods_list.getLoadingLayoutProxy(true, false).setReleaseLabel("释放开始刷新");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_title_left:
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * 查询商品列表
     */
    private void queryGoodsList() {
        startProgressDialog("");
        ApiHttpResult.queryGoodsList(this, new String[]{"typeid", "page", "pagesize"},
                new Object[]{typeid, page, rows},
                new HttpUtils.DataCallBack() {
                    @Override
                    public void callBack(Object o) {
                        if (ptrgv_goods_list != null) {
                            ptrgv_goods_list.onRefreshComplete();
                        }
                        stopProgressDialog();
                        if (o != null) {
                            GoodsListResultEntity goodsListResultEntity = (GoodsListResultEntity) o;
                            if (goodsListResultEntity != null && goodsListResultEntity.getResultCode().equals("0")) {
                                GoodsListResultEntity.GoodsListData data = goodsListResultEntity.getData();
                                totalPages = data.getPagination().getTotalPages();
                                ArrayList<GoodsEntity> list = data.getList();
                                if (list != null && list.size() > 0) {
                                    goodsEntityArrayList.addAll(list);
                                    goodsListAdapter.notifyDataSetChanged();
                                } else {
                                    Tools.showPrompt("没有更多数据");
                                }
                            } else {
                                Tools.showPrompt(goodsListResultEntity.getMsg());
                            }
                        } else {
                            NetUtil.toNetworkSetting(GoodsListActivity.this);
                        }
                    }
                });
    }

    /**
     * 添加到购物车
     */
    private void addBuyCar(String goodses, String homeid) {
        startProgressDialog("");
        ApiHttpResult.addBuyCar(this, new String[]{"ownerid", "goodses", "homeid"},
                new Object[]{loginUserEntity.getUserId(), goodses, homeid},
                new HttpUtils.DataCallBack() {
                    @Override
                    public void callBack(Object o) {
                        stopProgressDialog();
                        if (o != null) {
                            OverallSituationEntity entity = (OverallSituationEntity) o;
                            if (entity != null) {
                                Tools.showPrompt(entity.getMsg());
                            }
                        } else {
                            NetUtil.toNetworkSetting(GoodsListActivity.this);
                        }
                    }
                });
    }
}

