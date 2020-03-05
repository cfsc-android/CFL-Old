package com.xiandao.android.ui.activity.shop;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiandao.android.R;
import com.xiandao.android.adapter.ShopcatAdapter;
import com.xiandao.android.entity.OverallSituationEntity;
import com.xiandao.android.entity.ShopCar1Entity;
import com.xiandao.android.entity.ShopCar1ResultEntity;
import com.xiandao.android.entity.StoreInfo;
import com.xiandao.android.net.ApiHttpResult;
import com.xiandao.android.net.HttpUtils;
import com.xiandao.android.net.NetUtil;
import com.xiandao.android.ui.BaseLazyFragment;
import com.xiandao.android.utils.FileManagement;
import com.xiandao.android.utils.Tools;
import com.xiandao.android.view.alertview.AlertView;
import com.xiandao.android.view.alertview.OnItemClickListener;

import org.xutils.event.annotation.ViewInject;
import org.xutils.x;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.srain.cube.views.ptr.PtrClassicDefaultHeader;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

import static in.srain.cube.views.ptr.util.PtrLocalDisplay.dp2px;


/**
 * 此类描述的是:购物车fragment1
 *
 * @author TanYong
 *         create at 2017/5/18 9:53
 */
public class ShopCarFragment1 extends BaseLazyFragment implements View.OnClickListener, ShopcatAdapter.CheckInterface, ShopcatAdapter.ModifyCountInterface, ShopcatAdapter.GroupEditorListener {
    @ViewInject(R.id.iv_title_left)
    private ImageView ivTitleLeft;
    @ViewInject(R.id.tv_title_name)
    private TextView tvTitleName;
    @ViewInject(R.id.tv_title_right)
    private TextView tv_title_right;
    @ViewInject(R.id.elv_shop_car_1)
    private ExpandableListView elv_shop_car_1;

    @ViewInject(R.id.mPtrframe)
    private PtrFrameLayout mPtrFrame;

    @ViewInject(R.id.ll_cart)
    private LinearLayout ll_cart;
    @ViewInject(R.id.ll_order_info)
    private LinearLayout ll_order_info;
    @ViewInject(R.id.cb_all_shop_car)
    private CheckBox cb_all_shop_car;
    @ViewInject(R.id.tv_total_price)
    private TextView tv_total_price;
    @ViewInject(R.id.tv_go_pay)
    private TextView tv_go_pay;
    @ViewInject(R.id.del_shop_car)
    private TextView del_shop_car;
    @ViewInject(R.id.ll_share_info)
    private LinearLayout ll_share_info;
    @ViewInject(R.id.layout_empty_shopcart)
    private LinearLayout layout_empty_shopcart;

    private int shopCarNum = 0;
    //    private ArrayList<ShopCar1Entity> shopCar1EntityArrayList;
    private Context mcontext;
    private double mtotalPrice = 0.00;
    private int mtotalCount = 0;
    //false就是编辑，ture就是完成
    private boolean flag = false;
    private ShopcatAdapter adapter;
    private List<StoreInfo> groups; //组元素的列表
    private Map<String, List<ShopCar1Entity>> childs; //子元素的列表

    @Override
    protected void injectView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_shop_car_1, null);
        x.view().inject(this, view);
        setContentView(view);
        init();
    }

    private void init() {
        ivTitleLeft.setOnClickListener(this);
        tvTitleName.setText("购物车");
        tv_title_right.setText("编辑");
        tv_title_right.setVisibility(View.VISIBLE);
        tv_title_right.setOnClickListener(this);
        cb_all_shop_car.setOnClickListener(this);
        tv_go_pay.setOnClickListener(this);

//        shopCar1EntityArrayList = new ArrayList<>();
        mcontext = getActivity();
        groups = new ArrayList<StoreInfo>();
        childs = new HashMap<String, List<ShopCar1Entity>>();

        final PtrClassicDefaultHeader header = new PtrClassicDefaultHeader(getActivity());
        header.setPadding(dp2px(20), dp2px(20), 0, 0);
        mPtrFrame.setHeaderView(header);
        mPtrFrame.addPtrUIHandler(header);
        mPtrFrame.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mPtrFrame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPtrFrame.refreshComplete();
                        queryShopCarList();
                    }
                }, 2000);
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }
        });

        adapter = new ShopcatAdapter(groups, childs, mcontext);
        adapter.setCheckInterface(this);//关键步骤1：设置复选框的接口
        adapter.setModifyCountInterface(this); //关键步骤2:设置增删减的接口
        adapter.setGroupEditorListener(this);//关键步骤3:监听组列表的编辑状态
        elv_shop_car_1.setAdapter(adapter);

        elv_shop_car_1.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int firstVisiablePostion = view.getFirstVisiblePosition();
                int top = -1;
                View firstView = view.getChildAt(firstVisibleItem);
                Log.i("childCount=", view.getChildCount() + "");//返回的是显示层面上的所包含的子view的个数
                if (firstView != null) {
                    top = firstView.getTop();
                }
                Log.i("", "firstVisiableItem=" + firstVisibleItem + ",fistVisiablePosition=" + firstVisiablePostion + ",firstView=" + firstView + ",top=" + top);
                if (firstVisibleItem == 0 && top == 0) {
                    mPtrFrame.setEnabled(true);
                } else {
                    mPtrFrame.setEnabled(false);
                }
            }
        });
    }

    @Override
    protected void onResumeLazy() {
        super.onResumeLazy();
        queryShopCarList();
    }

    /**
     * @author TanYong
     * create at 2018/3/1 09:53
     * TODO：获取购物车列表
     */
    private void queryShopCarList() {
        startProgressDialog("");
//        shopCar1EntityArrayList.clear();
        groups.clear();
        childs.clear();
        ApiHttpResult.queryShopcarList(getActivity(), new String[]{"userId"},
                new Object[]{Tools.getStringValue(FileManagement.getLoginUserEntity().getUserId())},
                new HttpUtils.DataCallBack() {
                    @Override
                    public void callBack(Object o) {
                        stopProgressDialog();
                        if (o != null) {
                            ShopCar1ResultEntity resultEntity = (ShopCar1ResultEntity) o;
                            if (resultEntity != null && resultEntity.getResultCode().equals("0")) {
                                tv_title_right.setText("编辑");
                                ArrayList<ShopCar1Entity> shopCar1Entities = resultEntity.getData();
                                if (shopCar1Entities != null && shopCar1Entities.size() > 0) {
                                    groups.add(new StoreInfo(0 + "", shopCar1Entities.get(0).getHomename()));
                                    childs.put(groups.get(0).getId(), shopCar1Entities);
                                    adapter.notifyDataSetChanged();
                                    calulate();
                                    elv_shop_car_1.setGroupIndicator(null); //设置属性 GroupIndicator 去掉向下箭头

                                    for (int i = 0; i < adapter.getGroupCount(); i++) {
                                        elv_shop_car_1.expandGroup(i); //关键步骤4:初始化，将ExpandableListView以展开的方式显示
                                    }

                                    layout_empty_shopcart.setVisibility(View.GONE);
                                    ll_cart.setVisibility(View.VISIBLE);
                                } else {
                                    layout_empty_shopcart.setVisibility(View.VISIBLE);
                                    ll_cart.setVisibility(View.GONE);
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

    /**
     * 删除操作
     * 1.不要边遍历边删除,容易出现数组越界的情况
     * 2.把将要删除的对象放进相应的容器中，待遍历完，用removeAll的方式进行删除
     */
    private void doDelete() {
        List<StoreInfo> toBeDeleteGroups = new ArrayList<StoreInfo>(); //待删除的组元素
        for (int i = 0; i < groups.size(); i++) {
            StoreInfo group = groups.get(i);
            if (group.isChoosed()) {
                toBeDeleteGroups.add(group);
            }
            List<ShopCar1Entity> toBeDeleteChilds = new ArrayList<ShopCar1Entity>();//待删除的子元素
//            List<ShopCar1Entity> child = childs.get(group.getId());
//            for (int j = 0; j < child.size(); j++) {
//                if (child.get(j).isChoosed()) {
//                    toBeDeleteChilds.add(child.get(j));
//                }
//            }
//            child.removeAll(toBeDeleteChilds);
        }
        groups.removeAll(toBeDeleteGroups);
        //重新设置购物车
        setCartNum();
        adapter.notifyDataSetChanged();
    }

    private void setCartNum() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_left:
                getActivity().finish();
                break;
            case R.id.tv_title_right:
                flag = !flag;
                for (StoreInfo group : groups) {
                    group.setActionBarEditor(!group.isActionBarEditor());
                }
                adapter.notifyDataSetChanged();
                setVisiable();
                break;
            case R.id.cb_all_shop_car:
                doCheckAll();
                break;
            case R.id.tv_go_pay:
                if (childs.size() > 0 && groups.size() > 0) {
                    List<ShopCar1Entity> shopCar1Entities = childs.get(groups.get(0).getId());
                    List<ShopCar1Entity> shopCar1EntitiesChoose = new ArrayList<>();
                    if (null != shopCar1Entities) {
                        for (ShopCar1Entity entity : shopCar1Entities) {
                            if (entity.isChoosed()) {
                                shopCar1EntitiesChoose.add(entity);
                            }
                        }
                        if (shopCar1EntitiesChoose.size() > 0) {
                            Bundle bundle = new Bundle();
                            bundle.putString("mtotalPrice", mtotalPrice + "");
                            bundle.putSerializable("shopCar1Entities", (Serializable) shopCar1EntitiesChoose);
                            openActivity(CommitOrderListActivity.class, bundle);
                        } else {
                            Tools.showPrompt("请选择购物车商品");
                        }
                    }
                } else {
                    Tools.showPrompt("暂时没有商品");
                }
                break;
            default:
                break;
        }
    }

    /**
     * 全选和反选
     * 错误标记：在这里出现过错误
     */

    private void doCheckAll() {
        for (int i = 0; i < groups.size(); i++) {
            StoreInfo group = groups.get(i);
            group.setChoosed(cb_all_shop_car.isChecked());
            List<ShopCar1Entity> child = childs.get(group.getId());
            for (int j = 0; j < child.size(); j++) {
                child.get(j).setChoosed(cb_all_shop_car.isChecked());//这里出现过错误
            }
        }
        adapter.notifyDataSetChanged();
        calulate();
    }

    @Override
    public void checkGroup(int groupPosition, boolean isChecked) {
        StoreInfo group = groups.get(groupPosition);
        List<ShopCar1Entity> child = childs.get(group.getId());
        for (int i = 0; i < child.size(); i++) {
            child.get(i).setChoosed(isChecked);
        }
        if (isCheckAll()) {
            cb_all_shop_car.setChecked(true);//全选
        } else {
            cb_all_shop_car.setChecked(false);//反选
        }
        adapter.notifyDataSetChanged();
        calulate();
    }

    /**
     * @return 判断组元素是否全选
     */
    private boolean isCheckAll() {
        for (StoreInfo group : groups) {
            if (!group.isChoosed()) {
                return false;
            }
        }
        return true;
    }

    /**
     * 计算商品总价格，操作步骤
     * 1.先清空全局计价,计数
     * 2.遍历所有的子元素，只要是被选中的，就进行相关的计算操作
     * 3.给textView填充数据
     */
    private void calulate() {
        mtotalPrice = 0.00;
        mtotalCount = 0;
        for (int i = 0; i < groups.size(); i++) {
            StoreInfo group = groups.get(i);
            List<ShopCar1Entity> child = childs.get(group.getId());
            for (int j = 0; j < child.size(); j++) {
                ShopCar1Entity good = child.get(j);
                if (good.isChoosed()) {
                    mtotalCount++;
                    mtotalPrice += Double.valueOf(good.getDiscountprice()) * Double.valueOf(good.getCount());
                }
            }
        }
        tv_total_price.setText("￥" + mtotalPrice + "");
        tv_go_pay.setText("去下单（" + mtotalCount + "）");
        if (mtotalCount == 0) {
            setCartNum();
        }
    }

    private void setVisiable() {
        if (flag) {
//            ll_order_info.setVisibility(View.GONE);
//            ll_share_info.setVisibility(View.VISIBLE);
            tv_title_right.setText("完成");
        } else {
//            ll_order_info.setVisibility(View.VISIBLE);
//            ll_share_info.setVisibility(View.GONE);
            tv_title_right.setText("编辑");
        }
    }

    /**
     * @param groupPosition 组元素的位置
     * @param childPosition 子元素的位置
     * @param isChecked     子元素的选中与否
     */
    @Override
    public void checkChild(int groupPosition, int childPosition, boolean isChecked) {
        boolean allChildSameState = true; //判断该组下面的所有子元素是否处于同一状态
        StoreInfo group = groups.get(groupPosition);
        List<ShopCar1Entity> child = childs.get(group.getId());
        for (int i = 0; i < child.size(); i++) {
            //不选全中
            if (child.get(i).isChoosed() != isChecked) {
                allChildSameState = false;
                break;
            }
        }

        if (allChildSameState) {
            group.setChoosed(isChecked);//如果子元素状态相同，那么对应的组元素也设置成这一种的同一状态
        } else {
            group.setChoosed(false);//否则一律视为未选中
        }

        if (isCheckAll()) {
            cb_all_shop_car.setChecked(true);//全选
        } else {
            cb_all_shop_car.setChecked(false);//反选
        }

        adapter.notifyDataSetChanged();
        calulate();

    }

    /**
     * 商品数量加一
     *
     * @param groupPosition 组元素的位置
     * @param childPosition 子元素的位置
     * @param showCountView 用于展示变化后数量的View
     * @param isChecked     子元素选中与否
     */
    @Override
    public void doIncrease(int groupPosition, int childPosition, View showCountView, boolean isChecked) {
        ShopCar1Entity good = (ShopCar1Entity) adapter.getChild(groupPosition, childPosition);
        int count = Integer.valueOf(good.getCount());
        count++;
        String cartid = good.getCartid();
        String goodsid = good.getId();
        updateCarNum(good, cartid, goodsid, count + "");
    }

    /**
     * 商品数量减一
     *
     * @param groupPosition
     * @param childPosition
     * @param showCountView
     * @param isChecked
     */
    @Override
    public void doDecrease(int groupPosition, int childPosition, View showCountView, boolean isChecked) {
        ShopCar1Entity good = (ShopCar1Entity) adapter.getChild(groupPosition, childPosition);
        int count = Integer.valueOf(good.getCount());
        if (count == 1) {
            return;
        }
        count--;
        String cartid = good.getCartid();
        String goodsid = good.getId();
        updateCarNum(good, cartid, goodsid, count + "");
    }

    /**
     * @param groupPosition
     * @param childPosition 思路:当子元素=0，那么组元素也要删除
     */
    @Override
    public void childDelete(final int groupPosition, final int childPosition) {
        new AlertView("温馨提示", "确认删除此商品？",
                "取消", new String[]{"确认"}, null, getActivity(), AlertView.Style.Alert, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                if (position == 0) {
                    StoreInfo group = groups.get(groupPosition);
                    List<ShopCar1Entity> child = childs.get(group.getId());
                    ShopCar1Entity entity = child.get(childPosition);
                    delCartDetail(child, groupPosition, childPosition, entity.getCartid(), entity.getId());
                }
            }
        }).setCancelable(false).show();
    }

    /**
     * @author TanYong
     * create at 2018/3/1 09:53
     * TODO：修改商品数量
     */
    private void updateCarNum(final ShopCar1Entity good, String cartid, String goodsid, final String number) {
        startProgressDialog("");
        ApiHttpResult.updateCarNum(getActivity(), new String[]{"cartid", "goodsid", "number"},
                new Object[]{cartid, goodsid, number},
                new HttpUtils.DataCallBack() {
                    @Override
                    public void callBack(Object o) {
                        stopProgressDialog();
                        if (o != null) {
                            OverallSituationEntity entity = (OverallSituationEntity) o;
                            if (entity != null && entity.getResultCode().equals("0")) {
                                good.setCount(number);
                                adapter.notifyDataSetChanged();
                                calulate();
                            } else {
                                Tools.showPrompt(entity.getMsg());
                            }
                        } else {
                            NetUtil.toNetworkSetting(getActivity());
                        }
                    }
                });
    }

    /**
     * @author TanYong
     * create at 2018/3/1 09:53
     * TODO：删除商品
     */
    private void delCartDetail(final List<ShopCar1Entity> child, final int groupPosition, final int childPosition, String cartid, String goodsid) {
        startProgressDialog("");
        ApiHttpResult.delCartDetail(getActivity(), new String[]{"cartid", "goodsid"},
                new Object[]{cartid, goodsid},
                new HttpUtils.DataCallBack() {
                    @Override
                    public void callBack(Object o) {
                        stopProgressDialog();
                        if (o != null) {
                            OverallSituationEntity entity = (OverallSituationEntity) o;
                            if (entity != null && entity.getResultCode().equals("0")) {
                                child.remove(childPosition);
                                if (child.size() == 0) {
                                    groups.remove(groupPosition);
                                }

                                if (child.size() == 0 && groups.size() == 0) {
                                    layout_empty_shopcart.setVisibility(View.VISIBLE);
                                    ll_cart.setVisibility(View.GONE);
                                } else {
                                    adapter.notifyDataSetChanged();
                                    calulate();
                                }
                            } else {
                                Tools.showPrompt(entity.getMsg());
                            }
                        } else {
                            NetUtil.toNetworkSetting(getActivity());
                        }
                    }
                });
    }

    @Override
    public void doUpdate(int groupPosition, int childPosition, View showCountView, boolean isChecked) {
        ShopCar1Entity good = (ShopCar1Entity) adapter.getChild(groupPosition, childPosition);
        int count = Integer.valueOf(good.getCount());
        Log.i("进行更新数据，数量", count + "");
        ((TextView) showCountView).setText(String.valueOf(count));
        adapter.notifyDataSetChanged();
        calulate();
    }

    @Override
    public void groupEditor(int groupPosition) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        adapter = null;
        if (null != childs && childs.size() > 0) {
            childs.clear();
        }
        if (null != groups && groups.size() > 0) {
            groups.clear();
        }
        mtotalPrice = 0.00;
        mtotalCount = 0;
    }
}
