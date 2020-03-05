package com.xiandao.android.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.pgyersdk.update.DownloadFileListener;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;
import com.pgyersdk.update.javabean.AppBean;
import com.xiandao.android.AndroidApplication;
import com.xiandao.android.entity.LoginUserEntity;
import com.xiandao.android.utils.Constants;
import com.xiandao.android.utils.ExitAppUtils;
import com.xiandao.android.utils.FileManagement;
import com.xiandao.android.view.ProgressDialogUtil;

import org.xutils.app.LynActivityManager;
import org.xutils.x;


/**
 * 此类描述的是:activity基类
 *
 * @author TanYong
 * create at 2017/4/21 14:04
 */
public abstract class BaseActivity extends FragmentActivity {
//    @ViewInject(R.id.title_back_button)
//    public RelativeLayout title_back_button;
//    @ViewInject(R.id.title_name)
//    public TextView title_name;
//    @ViewInject(R.id.title_back)
//    public Button title_back;
//    @ViewInject(R.id.title_search)
//    public Button title_search;
//    @ViewInject(R.id.main_title_view)
//    public View main_title_view;
//    @ViewInject(R.id.title_img)
//    public ImageView title_img;

    //应用是否销毁标志
    protected boolean isDestroy;
    //防止重复点击设置的标志，涉及到点击打开其他Activity时，将该标志设置为false，在onResume事件中设置为true
    private boolean clickable = true;
    /**
     * @说明: 屏幕的宽度
     * @名称:mScreenWidth
     * @类型:int
     */
    protected int mScreenWidth;

    /**
     * @说明: 屏幕的高度
     * @名称:mScreenHeight
     * @类型:int
     */
    protected int mScreenHeight;

    /**
     * @说明: 屏幕的密度
     * @名称:mDensity
     * @类型:float
     */
    protected float mDensity;


    private ProgressDialogUtil progressDialogUtil = null;

    public LoginUserEntity loginUserEntity = FileManagement.getLoginUserEntity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isDestroy = false;
        //设置无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //垂直显示
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        LynActivityManager.getScreenManager().pushActivity(this);
        mScreenWidth = metric.widthPixels;
        mScreenHeight = metric.heightPixels;
        Constants.SWidth = mScreenWidth;
        Constants.SHeight = mScreenHeight;
        mDensity = metric.density;
        //设置状态栏颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            //取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#2C3447"));
        }
        x.view().inject(this);

//        EventBus.getDefault().register(this);
//        initView();
//        initEvent();
//        initRequestVo();
//        initCallBack();
//        initData();
//        setTitleView();
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        updateApp();
    }

    /**
     * @author TanYong
     * create at 2017/7/20 9:17
     * TODO：使用蒲公英进行版本更新
     */
    private void updateApp() {

//        PgyUpdateManager.register(this, "${applicationId}.fileprovider",
//                new UpdateManagerListener() {
//                    @Override
//                    public void onUpdateAvailable(final String result) {
//                        // 将新版本信息封装到AppBean中
//                        final AppBean appBean = getAppBeanFromString(result);
//                        new AlertDialog.Builder(BaseActivity.this)
//                                .setTitle("更新")
//                                .setMessage(appBean.getReleaseNote()).setCancelable(false)
//                                .setNegativeButton(
//                                        "确定",
//                                        new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which) {
//                                                startDownloadTask(BaseActivity.this, appBean.getDownloadURL());
//                                            }
//                                        }).show();
//                    }
//
//                    @Override
//                    public void onNoUpdateAvailable() {
//                    }
//                });
    }


    private OnBooleanListener onPermissionListener;

    public interface OnBooleanListener {
        void onClick(boolean c);
    }

    /**
     * 权限请求
     *
     * @param permission        Manifest.permission.CAMERA
     * @param onBooleanListener 权限请求结果回调，true-通过  false-拒绝
     */
    public void permissionRequests(String permission, OnBooleanListener onBooleanListener) {
        onPermissionListener = onBooleanListener;
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {
                //权限已有
                onPermissionListener.onClick(true);
            } else {
                //没有权限，申请一下
                ActivityCompat.requestPermissions(this, new String[]{permission}, 1);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //权限通过
                if (onPermissionListener != null) {
                    onPermissionListener.onClick(true);
                }
            } else {
                //权限拒绝
                if (onPermissionListener != null) {
                    onPermissionListener.onClick(false);
                }
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public boolean shouldShowRequestPermissionRationale(@NonNull String permission) {
        return super.shouldShowRequestPermissionRationale(permission);
    }

    //    /**
//     * 初始化布局
//     */
//    protected abstract void initView();

//    /**
//     * 初始化监听事件
//     */
//    protected void initEvent() {
//
//    }

//    /**
//     * 初始化RequestVo
//     */
//    protected abstract void initRequestVo();

//    /**
//     * 初始化回调函数
//     */
//    protected abstract void initCallBack();

    /**
     * @param type 网络切换连接成功
     */
    public void onConnect(org.xutils.NetUtil.netType type) {

    }

    /**
     * 网络断开
     */
    public void onDisConnect() {

    }

//    /**
//     * 初始化数据
//     */
//    protected abstract void initData();


//    /**
//     * 设置标题栏
//     */
//    protected abstract void setTitleView();

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
        Intent intent = new Intent(this, pClass);
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
        Intent intent = new Intent(this, pClass);
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
        Toast.makeText(this, pMsg, Toast.LENGTH_LONG).show();
    }

    /**
     * @param pMsg
     * @方法说明:短时间土司
     * @方法名称:showShortToast
     * @返回值:void
     */
    public void showShortToast(String pMsg) {
        Toast.makeText(this, pMsg, Toast.LENGTH_SHORT).show();
    }

    @SuppressWarnings("static-access")
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finishBase();
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * @方法说明:退出栈中所有Activity
     * @方法名称:finishBase
     * @返回值:void
     */
    public void finishBase() {
        if (AndroidApplication.getInstance().threadPoolManager != null) {
            AndroidApplication.getInstance().threadPoolManager.stopAllTask();
        }
        LynActivityManager.getScreenManager().popActivity(this);
    }

    /*
     * @重写方法名:onDestroy
     * @父类:@see android.support.v4.app.FragmentActivity#onDestroy()
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
//        EventBus.getDefault().unregister(this);
        isDestroy = true;
        if (progressDialogUtil != null) {
            progressDialogUtil.stopLoad();
            progressDialogUtil = null;
        }
//        releaseMemory();
        finishBase();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

//    /**
//     * @方法说明:手动释放内存
//     * @方法名称:releaseMemory
//     * @返回值:void
//     */
//    public abstract void releaseMemory();

    /*
     * @重写方法名:onResume
     * @父类:@see android.support.v4.app.FragmentActivity#onResume()
     */
    @Override
    protected void onResume() {
        super.onResume();
        clickable = true;
        uiRefresh();
    }

    public void uiRefresh() {
    }

    /**
     * 当前是否可以点击
     *
     * @return
     */
    protected boolean isClickable() {
        return clickable;
    }

    /**
     * 锁定点击
     */
    protected void lockClick() {
        clickable = false;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
        if (isClickable()) {
            lockClick();
            super.startActivityForResult(intent, requestCode, options);
        }
    }

    protected void startProgressDialog(String msg) {
        if (progressDialogUtil == null) {
            progressDialogUtil = new ProgressDialogUtil();
        }
        progressDialogUtil.startLoad(this, msg);
    }

    protected void stopProgressDialog() {
        if (progressDialogUtil != null) {
            progressDialogUtil.stopLoad();
        }
    }

//    public void onEvent(AnyEventType event) {
//        //接收消息
//    }

    //显示Toast
    protected void showToast(String content){
        Toast.makeText(this,content, Toast.LENGTH_SHORT).show();
    }
}
