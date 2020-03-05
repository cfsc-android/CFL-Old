package com.xiandao.android.http;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.xiandao.android.utils.Tools;

import org.xutils.http.oldhttp.LynSyncHttpClient;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class HttpDataConnet {

    /**
     * @说明 HTTP GET请求
     * @名称：GET_HTTPCONNET_TYPE
     * @类型：int
     */
    public static final int GET_HTTPCONNET_TYPE = 1;

    /**
     * @说明 HTTP POST请求
     * @名称：POST_HTTPCONNET_TYPE
     * @类型：int
     */
    public static final int POST_HTTPCONNET_TYPE = 2;

    /**
     * @说明 HTTP POST批量上传
     * @名称：UPLOAD_PHOTO_HTTPCONNET_TYPE
     * @类型：int
     */
    public static final int UPLOAD_PHOTO_HTTPCONNET_TYPE = 3;


    /**
     * @说明 HTTP POST下载图片
     * @名称：UPLOAD_PHOTO_HTTPCONNET_TYPE
     * @类型：int
     */
    public static final int DOWNINGPHOTO = 4;

    public static final String EXCEPTION_HTTP_NET = "http_net_exception";// 网络异常
    public static final String EXCEPTION_HTTP_DEADLY = "http_deadly_exception";// 致命异常
    LynSyncHttpClient lynHttpClient;
    //	private RequestParams paramsData = new RequestParams();// 参数
    private Map<String, Object> fileParams = new HashMap<String, Object>();
    private LinkedHashMap<String, Object> userParams = new LinkedHashMap<String, Object>();
    private Context mContext;
    private ArrayList<Handler> handList = new ArrayList<Handler>();
    private Object[] parserData;
    private Handler handler = null;
    private int httpCmd;
    private HttpDataConnet hd;
    private int isDataExist = 0;// 0正常,-1无数据，-2 网络异常 3致命异常
    private Object[] responParam = null;

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public void addHandler(Handler handList) {
        this.handList.add(handList);
    }

    /**
     * @param handler 异步任务
     * @param httpCmd 请求类型
     * @构造函数
     * @说明：无
     */

    public HttpDataConnet(Handler handler, int httpCmd) {
        this.handler = handler;
        this.httpCmd = httpCmd;
        this.hd = this;
        lynHttpClient = new LynSyncHttpClient();
    }

    public int isDataExist() {
        return isDataExist;
    }

    public void setDataExist(int isDataExist) {
        this.isDataExist = isDataExist;
    }

    public Object[] getParserData() {
        return parserData;
    }

    public void setParserData(Object[] parserData) {
        this.parserData = parserData;
    }


//    /**
//     * 方法说明：发送请求 方法名称：sendRequest
//     *
//     * @param url
//     * @param type
//     * @param paramKey
//     * @param paramValue
//     * @throws java.io.FileNotFoundException 返回值：void
//     */
//    public void sendUserRequest(String url, final int type,
//                                final String[] paramKey, final Object[] paramValue,
//                                final File fList, boolean isPublic) {
//        final String path = Constants.getHost(type) + url;
//        this.responParam = null;
//        if (isPublic) {
//            httPutParam(paramKey, paramValue);
//        } else {
//            httPutAliParam(paramKey, paramValue);
//        }
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                String responStr = ApiClient.http_getPost(path, fileParams, fList);
//                if (Tools.isEmpty(responStr)) {
//                    setDataExist(-1);
//                } else if (responStr.equals("[]")) {
//                    setDataExist(-1);
//                } else if (responStr.equals(EXCEPTION_HTTP_NET)) {
//                    setDataExist(-2);
//                } else if (responStr.equals(EXCEPTION_HTTP_DEADLY)) {
//                    setDataExist(-3);
//                } else {
//                    setDataExist(0);
//                    responParam = new Object[]{httpCmd, responStr};
//                }
//                requestHandler();
//            }
//        }).start();
//    }

    /**
     * @方法说明：http请求结果处理
     * @方法名称：requestHandler
     * @返回值：void
     */
    public void requestHandler() {
        if (isDataExist == 0) {
            responDataParse(responParam);
        }

        if (handler != null) {
            Message msg = handler.obtainMessage();
            msg.what = httpCmd;
            msg.obj = hd;
            handler.sendMessage(msg);

        }

        for (int i = 0; i < handList.size(); i++) {
            Handler handler = handList.get(i);
            Message msg = handler.obtainMessage();
            msg.what = httpCmd;
            msg.obj = hd;
            handler.sendMessage(msg);
        }
    }

    /**
     * 方法说明：保存Url参数 方法名称：httPutParam
     *
     * @param paramKey
     * @param paramValue 返回值：void
     */
    public void httPutParam(String[] paramKey, Object[] paramValue) {
        if (paramKey == null || paramValue == null) {
            return;
        }
        for (int i = 0; i < paramKey.length; i++) {
            String key = paramKey[i];
            String value = String.valueOf(paramValue[i]);
            if (!Tools.isEmpty(value)) {
                fileParams.put(key, value);
            }
        }
//        long time = Tools.getLocationTime();
//        if (time <= 0) {
//            time = Tools.getUnixTimestamp();
//        }
//        String apkKey = "";
//        if (FileManagement.getTokenEntity() != null) {
//            fileParams.put("accessToken", FileManagement.getTokenEntity().getAccessToken());
//        }
//        apkKey = Constants.APPKEY;
//        fileParams.put("appId", Constants.clientId);
//        fileParams.put("channel", Constants.channel);
//        fileParams.put("terminalType", Constants.terminalType);
//        fileParams.put("t", time + "");
//        String md5Data = BaseParameter.orderParamsMapAndReturnParamsString(fileParams);
//        md5Data = md5Data + "&appKey=" + apkKey;
//        fileParams.put("h", MD5.md5(md5Data));
    }

    /**
     * 方法说明：保存Url参数 方法名称：httPutParam
     *
     * @param paramKey
     * @param paramValue 返回值：void
     */
    public void httPutAliParam(String[] paramKey, Object[] paramValue) {
        if (paramKey == null || paramValue == null) {
            return;
        }
        for (int i = 0; i < paramKey.length; i++) {
            String key = paramKey[i];
            String value = String.valueOf(paramValue[i]);
            if (!Tools.isEmpty(value)) {
                fileParams.put(key, value);
            }
        }
    }

    /**
     * 方法说明：保存文件Url参数 方法名称：httPutParam
     *
     * @param paramKey
     * @param paramValue 返回值：void
     */
    public void httpFilePutParam(String[] paramKey, Object[] paramValue) {
        if (paramKey == null || paramValue == null) {
            return;
        }
        for (int i = 0; i < paramKey.length; i++) {
            String key = paramKey[i];
            String value = String.valueOf(paramValue[i]);
            userParams.put(key, value);
        }
    }

    /**
     * 方法说明：终止HTTP请求 方法名称：httpCancelRequests
     *
     * @param mayInterruptIfRunning 指定如果主动请求应该取消挂起的请求 返回值：void
     */
    public void httpCancelRequests(boolean mayInterruptIfRunning) {
        if (lynHttpClient != null && mContext != null) {
            lynHttpClient.cancelRequests(mContext, mayInterruptIfRunning);
        }
    }

    /**
     * 方法说明：解析数 方法名称：responDataParse
     *
     * @param param 返回值：void
     */
    public abstract void responDataParse(Object... param);


    /**
     * 方法说明：上传多张文件请求
     *
     * @param url
     * @param paramKey
     * @param paramValue
     * @throws java.io.FileNotFoundException 返回值：void
     */
    public void sendRequest(final String url, final String[] paramKey, final Object[] paramValue, final ArrayList<File> files) {
        this.responParam = null;
        httPutParam(paramKey, paramValue);
        new Thread(new Runnable() {
            @Override
            public void run() {
                String responStr = ApiClient.http_getBatchUploadPost(url, fileParams, files);
                if (Tools.isEmpty(responStr)) {
                    setDataExist(-1);
                } else if (responStr.equals("[]")) {
                    setDataExist(-1);
                } else if (responStr.equals(EXCEPTION_HTTP_NET)) {
                    setDataExist(-2);
                } else if (responStr.equals(EXCEPTION_HTTP_DEADLY)) {
                    setDataExist(-3);
                } else {
                    setDataExist(0);
                    responParam = new Object[]{httpCmd, responStr};
                }
                requestHandler();
            }
        }).start();
    }
}
