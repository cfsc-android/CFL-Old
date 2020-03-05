package com.xiandao.android.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Toast;

import com.shizhefei.fragment.LazyFragment;
import com.xiandao.android.view.ProgressDialogUtil;

/**
 * fragment基础类
 */
public abstract class BaseLazyFragment extends LazyFragment {
    private Context context;// 上下文
    //    private ProgressWheel progressWheel = null;
    ProgressDialogUtil progressDialogUtil = null;

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState, LayoutInflater inflater) {
        super.onCreateViewLazy(savedInstanceState, inflater);
        injectView(inflater);
        initView();
    }

    protected abstract void injectView(LayoutInflater inflater);

    protected void initView() {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState == null) {// 数据初始化
            dataInit();
        } else {// 数据恢复
            dataRestore(savedInstanceState);
        }
        eventDispose();// 事件
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        // TODO Auto-generated method stub
        super.onHiddenChanged(hidden);
        refresh(hidden);
    }

    /**
     * 刷新页面
     */
    public void refresh(boolean hidden) {

    }

    /**
     * @方法说明:数据初始化
     * @方法名称:dataInit
     * @返回值:void
     */
    public void dataInit() {
    }

    /**
     * @param savedInstanceState
     * @方法说明:数据恢复
     * @方法名称:dataRestore
     * @返回值:void
     */
    public void dataRestore(Bundle savedInstanceState) {
    }

    /**
     * @方法说明:处理
     * @方法名称:eventDispose
     * @返回值:void
     */
    public void eventDispose() {
    }

    /**
     * @return
     * @方法说明:获得上下文环境
     * @方法名称:getContext
     * @返回值:Context
     */
    @Override
    public Context getContext() {
        return context;
    }

    /**
     * @param pClass
     * @方法说明:启动指定activity
     * @方法名称:openActivity
     * @返回值:void
     */
    public void openActivity(Class<?> pClass) {
        openActivity(pClass, null);
    }

    /**
     * @param pClass
     * @param pBundle
     * @方法说明:启动到指定activity，Bundle传递对象（作用于2个界面之间传递数据）
     * @方法名称:openActivity
     * @返回值:void
     */
    public void openActivity(Class<?> pClass, Bundle pBundle) {
        Intent intent = new Intent(this.getContext(), pClass);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        startActivity(intent);
    }

    /**
     * @param pAction
     * @方法说明:根据界面name启动到指定界面
     * @方法名称:openActivity
     * @返回值:void
     */
    public void openActivity(String pAction) {
        openActivity(pAction, null);
    }

    /**
     * @param pAction
     * @param pBundle
     * @方法说明:根据界面name启动到指定界面，Bundle传递对象（作用于2个界面之间传递数据）
     * @方法名称:openActivity
     * @返回值:void
     */
    public void openActivity(String pAction, Bundle pBundle) {
        Intent intent = new Intent(pAction);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        startActivity(intent);
    }

    /**
     * @param pClass
     * @方法说明:A-Activity需要在B-Activtiy中执行一些数据操作，而B-Activity又要将，执行操作数据的结果返回给A-Activity
     * @方法名称:openActivityResult
     * @返回值:void
     */
    public void openActivityResult(Class<?> pClass) {
        openActivityResult(pClass, null);
    }

    /**
     * @param pClass
     * @param pBundle
     * @方法说明:A-Activity需要在B-Activtiy中执行一些数据操作， 而B-Activity又要将，执行操作数据的结果返回给A-Activity
     * ， Bundle传递对象（作用于2个界面之间传递数据）
     * @方法名称:openActivityResult
     * @返回值:void
     */
    public void openActivityResult(Class<?> pClass, Bundle pBundle) {
        openActivityResult(pClass, pBundle, 0);
    }

    /**
     * @param pClass
     * @param pBundle
     * @param requestCode
     * @方法说明:A-Activity需要在B-Activtiy中执行一些数据操作， 而B-Activity又要将，执行操作数据的结果返回给A-Activity
     * ， Bundle传递对象（作用于2个界面之间传递数据）
     * @方法名称:openActivityResult
     * @返回值:void
     */
    public void openActivityResult(Class<?> pClass, Bundle pBundle,
                                   int requestCode) {
        Intent intent = new Intent(this.getContext(), pClass);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        startActivityForResult(intent, requestCode);
    }

    /**
     * @param pResId
     * @方法说明:获取string资源字段土司
     * @方法名称:showShortToast
     * @返回值:void
     */
    public void showShortToast(int pResId) {
        showShortToast(getString(pResId));
    }

    /**
     * @param pMsg
     * @方法说明:长时间土司
     * @方法名称:showLongToast
     * @返回值:void
     */
    public void showLongToast(String pMsg) {
        Toast.makeText(this.getContext(), pMsg, Toast.LENGTH_LONG).show();
    }

    /**
     * @param pMsg
     * @方法说明:短时间土司
     * @方法名称:showShortToast
     * @返回值:void
     */
    public void showShortToast(String pMsg) {
        Toast.makeText(this.getContext(), pMsg, Toast.LENGTH_SHORT).show();

    }

    //显示Toast
    protected void showToast(String content){
        Toast.makeText(this.getContext(), content, Toast.LENGTH_SHORT).show();
    }

    protected void startProgressDialog(String msg) {
        if (progressDialogUtil == null) {
            progressDialogUtil = new ProgressDialogUtil();
        }
        progressDialogUtil.startLoad(getContext(), msg);
    }

    protected void stopProgressDialog() {
        if (progressDialogUtil != null) {
            progressDialogUtil.stopLoad();
        }
    }

    @Override
    protected void onDestroyViewLazy() {
        super.onDestroyViewLazy();
        stopProgressDialog();
    }


}
