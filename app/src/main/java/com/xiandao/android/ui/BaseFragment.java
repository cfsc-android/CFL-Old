package com.xiandao.android.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.xiandao.android.view.ProgressDialogUtil;

import org.xutils.x;

/**
 * Created by Loong on 2020/3/6.
 * Version: 1.0
 * Describe:
 */
public class BaseFragment extends Fragment {

    private Context context;// 上下文
    private boolean injected = false;
    ProgressDialogUtil progressDialogUtil = null;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        injected = true;
        return x.view().inject(this, inflater, container);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!injected) {
            x.view().inject(this, this.getView());
        }
    }

    //显示Toast
    protected void showToast(String content){
        Toast.makeText(context,content,Toast.LENGTH_SHORT).show();
    }

    /**
     * @return
     * @方法说明:获得上下文环境
     * @方法名称:getContext
     * @返回值:Context
     */
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
    public void onDestroy() {
        super.onDestroy();
        stopProgressDialog();
    }

}
