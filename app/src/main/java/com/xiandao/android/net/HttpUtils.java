package com.xiandao.android.net;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.xiandao.android.ui.activity.NetworkErrorActivity;
import com.xiandao.android.utils.Constants;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtils {

    Context mContext;

    public HttpUtils(Context con) {
        this.mContext = con;
    }

    // 定义回调函数
    public interface DataCallBack<T> {
        public abstract void callBack(T t);
    }

    /**
     * 从服务器获取数据
     */
    public void getServerForResult(DataCallBack dataCallBack, RequestVo vo) {
        vo.setHttpDataCallBack(dataCallBack);
        ThreadPoolManager threadPoolManager = ThreadPoolManager.getInstance();
        threadPoolManager.addTask(new BaseRunnable(new BaseHandler(dataCallBack), vo));
    }

    /**
     * 子线程中开启网络,获取数据
     */
    public class BaseRunnable implements Runnable {

        private BaseHandler handler;
        private RequestVo vo;

        public BaseRunnable(BaseHandler handler, RequestVo vo) {
            this.handler = handler;
            this.vo = vo;
        }

        @Override
        public void run() {
            if (NetUtil.hasConnectedNetwork(mContext.getApplicationContext())) {
                try {
                    // 请求的url地址
                    Message msg = Message.obtain();
                    msg.what = Constants.SUCCESS;
                    msg.obj = NetUtil.post(vo);
                    // 把结果返回给主线程
                    handler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(Constants.FAILED);
                }
            } else {
                handler.sendEmptyMessage(Constants.NET_FAILED);
            }
        }
    }


    /**
     * 自定义handler
     */
    public class BaseHandler extends Handler {

        private DataCallBack dataCallBack;

        public BaseHandler(DataCallBack dataCallBack) {
            this.dataCallBack = dataCallBack;
        }


        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.SUCCESS:
                    if (msg.obj != null) {
                        dataCallBack.callBack(msg.obj);
//                        dataCallBack.callBackCode(msg == null ? Constants.FAILED : msg.what);
                    } else {
                        dataCallBack.callBack(null);
//                        dataCallBack.callBackCode(msg == null ? Constants.FAILED : msg.what);
//                        LogUtils.toast(mContext.getApplicationContext(), "获取数据为空..");
                    }
                    break;
                case Constants.FAILED:
                    dataCallBack.callBack(null);
//                    dataCallBack.callBackCode(msg == null ? Constants.FAILED : msg.what);
//                    LogUtils.toast(mContext.getApplicationContext(), "网络异常，请重新请求...");
                    break;
                case Constants.NET_FAILED:
                    dataCallBack.callBack(null);
//                    dataCallBack.callBackCode(msg == null ? Constants.NET_FAILED : msg.what);
//                    Toast.makeText(mContext, "请求网络失败...", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }


    private static final int READ_TIMEOUT = 5000;
    private static final int CONNECT_TIMEOUT = 5000;

    public static String executeGet(String urlString) throws Exception {
        InputStream inputStream = executeGetInputStream(urlString);
        ByteArrayOutputStream outputStream = null;
        try {
            outputStream = new ByteArrayOutputStream(1024);
            int readLength = 0;
            byte[] buffer = new byte[4 * 1024];
            while ((readLength = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, readLength);
            }
            String json = outputStream.toString("utf-8");
            return json;
        } finally {
            if (inputStream != null)
                inputStream.close();
            if (outputStream != null) {
                outputStream.close();
            }
        }
    }

    public static InputStream executeGetInputStream(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        // 指的是与请求网址的服务器建立连接的超时时间。
        connection.setConnectTimeout(CONNECT_TIMEOUT);
        // 指的是建立连接后如果指定时间内服务器没有返回数据的后超时。
        connection.setReadTimeout(READ_TIMEOUT);
        connection.setRequestMethod("GET");
        int code = connection.getResponseCode();
        if (code >= 200 && code <= 299)// 连接成功,返回结果
        {
            return connection.getInputStream();
        } else {
            String info = "executeGet network error urlString:" + urlString + " code:" + code;
            throw new Exception(info);
        }
    }
}
