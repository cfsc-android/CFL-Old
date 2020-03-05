package com.xiandao.android.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.model.TakePhotoOptions;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;
import com.xiandao.android.utils.Tools;
import com.xiandao.android.view.ProgressDialogUtil;

import org.xutils.x;

/**
 * 获取图片fragment基础类
 */
public abstract class BaseTakePhotoFragment extends Fragment implements TakePhoto.TakeResultListener, InvokeListener {
    //    private Context context;// 上下文
    //    private ProgressWheel progressWheel = null;
    ProgressDialogUtil progressDialogUtil = null;

    public TakePhoto takePhoto;
    public InvokeParam invokeParam;
    private boolean injected = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getTakePhoto().onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 获取TakePhoto实例
     *
     * @return
     */
    public TakePhoto getTakePhoto() {
        if (takePhoto == null) {
            takePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));
        }
        return takePhoto;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.TPermissionType type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handlePermissionsResult(getActivity(), type, invokeParam, this);
    }

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }
        return type;
    }

    /**
     * @author TanYong
     * create at 2017/7/4 11:51
     * TODO：设置拍照参数
     */
    public void configTakePhotoOption(TakePhoto takePhoto) {
        TakePhotoOptions.Builder builder = new TakePhotoOptions.Builder();
        builder.setWithOwnGallery(true);//使用TakePhoto自带相册
        builder.setCorrectImage(true);//纠正拍照的照片旋转角度
        takePhoto.setTakePhotoOptions(builder.create());
    }

    /**
     * @author TanYong
     * create at 2017/7/4 11:51
     * TODO：配置图片压缩参数
     */
    public void configCompress(TakePhoto takePhoto) {
        int maxSize = 512000;
        int width = 529;
        int height = 960;
        boolean showProgressBar = true;//显示压缩进度条
        boolean enableRawFile = true;
        CompressConfig config = new CompressConfig.Builder()
                .setMaxSize(maxSize)
                .setMaxPixel(width >= height ? width : height)
                .enableReserveRaw(enableRawFile)//保留原图
                .create();
//        takePhoto.onEnableCompress(config, showProgressBar);
        takePhoto.onEnableCompress(null, false);
    }

    @Override
    public void takeFail(TResult result, String msg) {
        Tools.showPrompt(msg);
    }

    @Override
    public void takeCancel() {
        Tools.showPrompt("取消");
    }

//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        context = activity;
//    }

    /**
     * @return
     * @方法说明:获得上下文环境
     * @方法名称:getContext
     * @返回值:Context
     */
//    public Context getContext() {
//        return context;
//    }

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
}
