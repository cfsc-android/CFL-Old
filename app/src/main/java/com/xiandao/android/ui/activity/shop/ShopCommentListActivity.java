package com.xiandao.android.ui.activity.shop;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.xiandao.android.R;
import com.xiandao.android.adapter.ShopCommentListAdapter;
import com.xiandao.android.adapter.StoreListAdapter;
import com.xiandao.android.entity.GoodsCommentOwnersEntity;
import com.xiandao.android.entity.ShopCommentDataEntity;
import com.xiandao.android.entity.ShopCommentEntity;
import com.xiandao.android.entity.ShopCommentResultEntity;
import com.xiandao.android.entity.StoreEntity;
import com.xiandao.android.entity.StoreListResultEntity;
import com.xiandao.android.net.ApiHttpResult;
import com.xiandao.android.net.HttpUtils;
import com.xiandao.android.net.NetUtil;
import com.xiandao.android.ui.BaseActivity;
import com.xiandao.android.utils.Tools;

import org.xutils.event.annotation.ContentView;
import org.xutils.event.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * 此类描述的是:商品评价列表activity
 *
 * @author TanYong
 *         create at 2017/6/14 17:09
 */
@ContentView(R.layout.activity_shop_comment_list)
public class ShopCommentListActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.iv_title_left)
    private ImageView ivTitleLeft;
    @ViewInject(R.id.tv_title_name)
    private TextView tvTitleName;

    @ViewInject(R.id.ptrlv_shop_comment)
    private PullToRefreshListView ptrlv_shop_comment;

    private ArrayList<ShopCommentEntity> shopCommentEntityArrayList = new ArrayList<>();
    private ShopCommentListAdapter shopCommentListAdapter;

    private int page = 1;//第几页
    private int pageSize = 10;//每页显示多少条
    private int totalPages = 0;//总的页面数

    private String goodsId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        tvTitleName.setText("评价内容");
        ivTitleLeft.setOnClickListener(this);

        Intent intent = getIntent();
        if (null != intent) {
            Bundle bundle = intent.getExtras();
            if (null != bundle) {
                goodsId = bundle.getString("goodsId");
            }
        }

        shopCommentListAdapter = new ShopCommentListAdapter(ShopCommentListActivity.this, shopCommentEntityArrayList);
        ptrlv_shop_comment.setAdapter(shopCommentListAdapter);

        //下拉刷新与上拉加载
        ptrlv_shop_comment.setMode(PullToRefreshBase.Mode.BOTH);
        // 设置刷新文本说明(展开刷新栏前)为false,true返回设置上拉的。
        ptrlv_shop_comment.getLoadingLayoutProxy(false, true).setLastUpdatedLabel("");
        ptrlv_shop_comment.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在加载...");
        ptrlv_shop_comment.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载更多");
        ptrlv_shop_comment.getLoadingLayoutProxy(false, true).setReleaseLabel("释放开始加载");
        // true,false返回设置下拉
        ptrlv_shop_comment.getLoadingLayoutProxy(true, false).setRefreshingLabel("正在刷新...");
        ptrlv_shop_comment.getLoadingLayoutProxy(true, false).setPullLabel("下拉刷新");
        ptrlv_shop_comment.getLoadingLayoutProxy(true, false).setReleaseLabel("释放开始刷新");

        ptrlv_shop_comment.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                page = 1;
                shopCommentEntityArrayList.clear();
                queryShopCommentList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (page > totalPages) {
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ptrlv_shop_comment.onRefreshComplete();
                        }
                    }, 1000);
                    Tools.showPrompt("已加载完所有内容");
                } else {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            page++;
                            queryShopCommentList();
                        }
                    });
                }
            }
        });

        queryShopCommentList();
    }

    /**
     * @author TanYong
     * create at 2018/3/1 09:53
     * TODO：获取商品评价列表
     */
    private void queryShopCommentList() {
        startProgressDialog("");
        ApiHttpResult.queryShopCommentList(this, new String[]{"goodsId", "page", "pagesize"},
                new Object[]{goodsId, page, pageSize},
                new HttpUtils.DataCallBack() {
                    @Override
                    public void callBack(Object o) {
                        stopProgressDialog();
                        if (null != ptrlv_shop_comment) {
                            ptrlv_shop_comment.onRefreshComplete();
                        }
                        if (o != null) {
                            ShopCommentResultEntity resultEntity = (ShopCommentResultEntity) o;
                            if (resultEntity != null && resultEntity.getResultCode().equals("0")) {
                                ShopCommentDataEntity data = resultEntity.getData();
                                List<ShopCommentEntity> entityList = data.getComments();
                                totalPages = (int) Math.ceil(Tools.isEmpty(data.getPagination().getTotalResults() + "")
                                        ? 1 : data.getPagination().getTotalResults() / pageSize);
                                if (entityList != null && entityList.size() > 0) {
                                    shopCommentEntityArrayList.addAll(entityList);
                                    shopCommentListAdapter.notifyDataSetChanged();
                                }
                            } else {
                                Tools.showPrompt(resultEntity.getMsg());
                            }
                        } else {
                            NetUtil.toNetworkSetting(ShopCommentListActivity.this);
                        }
                    }
                });
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
}
