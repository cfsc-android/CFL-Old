package com.xiandao.android.ui.activity.shop;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.xiandao.android.R;
import com.xiandao.android.adapter.MyOrderAdapter;
import com.xiandao.android.entity.MyOrderResultDataEntity;
import com.xiandao.android.entity.MyOrderResultEntity;
import com.xiandao.android.entity.MyRepairsEntity;
import com.xiandao.android.entity.OverallSituationEntity;
import com.xiandao.android.entity.Pagination;
import com.xiandao.android.entity.ShopCar1Entity;
import com.xiandao.android.entity.ShopCar1ResultEntity;
import com.xiandao.android.entity.ShopOrderDetailEntity;
import com.xiandao.android.entity.ShopOrderEntity;
import com.xiandao.android.entity.StoreInfo;
import com.xiandao.android.net.ApiHttpResult;
import com.xiandao.android.net.HttpUtils;
import com.xiandao.android.net.NetUtil;
import com.xiandao.android.ui.BaseLazyFragment;
import com.xiandao.android.ui.activity.ComplainCommentActivity;
import com.xiandao.android.ui.activity.SetMineActivity;
import com.xiandao.android.utils.FileManagement;
import com.xiandao.android.utils.Tools;

import org.xutils.event.annotation.ViewInject;
import org.xutils.x;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 此类描述的是:我的订单fragment
 *
 * @author TanYong
 *         create at 2017/5/18 9:53
 */
public class MyOrderFragment extends BaseLazyFragment implements View.OnClickListener {

    @ViewInject(R.id.ptrlv_my_order)
    private PullToRefreshListView ptrlv_my_order;
    @ViewInject(R.id.ll_no_order)
    private LinearLayout ll_no_order;
    private MyOrderAdapter adapter;

    private int ident = -1;
    private int page = 1;//第几页
    private int pageSize = 10;//每页显示多少条
    private int totalPages = 0;//总的页面数

    private List<ShopOrderEntity> orderDetailList = new ArrayList<>();


    @Override
    protected void injectView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_my_order, null);
        x.view().inject(this, view);
        setContentView(view);
    }

    @Override
    protected void onFragmentStartLazy() {
        super.onFragmentStartLazy();
        queryMyOrderList(true);
    }

    @Override
    protected void initView() {
        Bundle bundle = getArguments();
        ident = bundle.getInt("ident");

        //下拉刷新与上拉加载
        ptrlv_my_order.setMode(PullToRefreshBase.Mode.BOTH);
        // 设置刷新文本说明(展开刷新栏前)为false,true返回设置上拉的。
        ptrlv_my_order.getLoadingLayoutProxy(false, true).setLastUpdatedLabel("");
        ptrlv_my_order.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在加载...");
        ptrlv_my_order.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载更多");
        ptrlv_my_order.getLoadingLayoutProxy(false, true).setReleaseLabel("释放开始加载");
        // true,false返回设置下拉
        ptrlv_my_order.getLoadingLayoutProxy(true, false).setRefreshingLabel("正在刷新...");
        ptrlv_my_order.getLoadingLayoutProxy(true, false).setPullLabel("下拉刷新");
        ptrlv_my_order.getLoadingLayoutProxy(true, false).setReleaseLabel("释放开始刷新");

        adapter = new MyOrderAdapter(getActivity(), orderDetailList, ident, new MyOrderAdapter.ForMyOrderListener() {
            @Override
            public void cancelMyOrder(String id, int position) {
                cancelOrder(id, position);
            }

            @Override
            public void doGoPay(String orderId, String price) {
                Bundle bundle = new Bundle();
                bundle.putInt("fromWhere",1);//从哪个界面跳转到支付界面：1、商城
                bundle.putString("orderId", orderId);
                bundle.putString("price", price);
                openActivity(ShopPayActivity.class, bundle);
                getActivity().finish();
            }

            @Override
            public void goShopComment(String goodsid, String orderid) {
                Bundle bundle1 = new Bundle();
                bundle1.putString("goodsId", goodsid);
                bundle1.putString("orderid", orderid);
                bundle1.putBoolean("fromShop", true);
                openActivity(ComplainCommentActivity.class, bundle1);
            }
        });
        ptrlv_my_order.setAdapter(adapter);

        ptrlv_my_order.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                page = 1;
                queryMyOrderList(true);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (page > totalPages) {
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ptrlv_my_order.onRefreshComplete();
                        }
                    }, 1000);
                    Tools.showPrompt("已加载完所有内容");
                } else {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            page++;
                            queryMyOrderList(false);
                        }
                    });
                }
            }
        });

        ptrlv_my_order.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle1 = new Bundle();
                ShopOrderEntity shopOrderEntity = orderDetailList.get(position - 1);
                List<ShopOrderDetailEntity> shopOrderDetail = shopOrderEntity.getShopOrderDetail();
                List<ShopCar1Entity> entityList = new ArrayList<>();
                for (ShopOrderDetailEntity detailEntity : shopOrderDetail) {
                    ShopCar1Entity shopGoods = detailEntity.getShopGoods();
                    shopGoods.setHomename(Tools.getStringValue(detailEntity.getHomeName()));
                    shopGoods.setCount(Tools.getStringValue(detailEntity.getNumber()));
                    shopGoods.setOrderid(Tools.getStringValue(detailEntity.getOrderid()));
                    shopGoods.setGoodsid(Tools.getStringValue(detailEntity.getGoodsid()));
                    entityList.add(shopGoods);
                }

                bundle1.putSerializable("shopOrderEntity", (Serializable) shopOrderEntity);
                bundle1.putSerializable("shopOrderDetail", (Serializable) entityList);
                bundle1.putInt("ident", ident);
                openActivity(OrderDetailActivity.class, bundle1);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_mine_head:
                openActivityResult(SetMineActivity.class);
                break;
            default:
                break;
        }
    }

    /**
     * @author TanYong
     * create at 2018/3/1 09:53
     * ident:（0已下单/1已支付/2已取消/3已完成）
     * TODO：获取我的订单
     */
    private void queryMyOrderList(final boolean isRefresh) {
        startProgressDialog("");
        ApiHttpResult.queryMyOrder(getActivity(), new String[]{"ownerid", "ident", "page", "pagesize"},
                new Object[]{Tools.getStringValue(FileManagement.getLoginUserEntity().getUserId()), ident, page, pageSize},
                new HttpUtils.DataCallBack() {
                    @Override
                    public void callBack(Object o) {
                        stopProgressDialog();
                        if (o != null) {
                            MyOrderResultEntity resultEntity = (MyOrderResultEntity) o;
                            if (resultEntity != null && resultEntity.getResultCode().equals("0")) {
                                MyOrderResultDataEntity data = resultEntity.getData();
                                if (null != data) {
                                    List<ShopOrderEntity> orderDetail = data.getOrderDetail();
                                    Pagination pagination = data.getPagination();
                                    if (orderDetail != null && orderDetail.size() > 0) {
                                        ll_no_order.setVisibility(View.GONE);
                                        ptrlv_my_order.setVisibility(View.VISIBLE);
                                        totalPages = (int) Math.ceil(Tools.isEmpty(pagination.getTotalResults() + "")
                                                ? 1 : pagination.getTotalResults() / pageSize);
                                        if (isRefresh) {
                                            orderDetailList.clear();
                                        }
                                        orderDetailList.addAll(orderDetail);
                                    } else {
                                        if (null != orderDetailList) {
                                            orderDetailList.clear();
                                        }
                                        ptrlv_my_order.setVisibility(View.GONE);
                                        ll_no_order.setVisibility(View.VISIBLE);
                                    }
                                }
                            } else {
                                Tools.showPrompt(resultEntity.getMsg());
                            }
                        } else {
                            NetUtil.toNetworkSetting(getActivity());
                        }
                        if (ptrlv_my_order != null) {
                            ptrlv_my_order.onRefreshComplete();
                        }
                        if (adapter != null) {
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    /**
     * @author TanYong
     * create at 2018/3/1 09:53
     * TODO：取消订单
     */
    private void cancelOrder(String id, final int position) {
        startProgressDialog("");
        ApiHttpResult.updateOrderStatus(getActivity(), new String[]{"id", "status"},
                new Object[]{id, "2"},
                new HttpUtils.DataCallBack() {
                    @Override
                    public void callBack(Object o) {
                        stopProgressDialog();
                        if (o != null) {
                            OverallSituationEntity resultEntity = (OverallSituationEntity) o;
                            if (resultEntity != null && resultEntity.getResultCode().equals("0")) {
                                Tools.showPrompt(resultEntity.getMsg());
                                orderDetailList.remove(position);
                                adapter.notifyDataSetChanged();
                                if (orderDetailList.size() == 0) {
                                    ll_no_order.setVisibility(View.VISIBLE);
                                    ptrlv_my_order.setVisibility(View.GONE);
                                }
                            } else {
                                Tools.showPrompt(resultEntity.getMsg());
                            }
                        } else {
                            NetUtil.toNetworkSetting(getActivity());
                        }
                    }
                });
    }

}
