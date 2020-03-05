package com.xiandao.android.net;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Proxy;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.xiandao.android.ui.activity.NetworkErrorActivity;
import com.xiandao.android.utils.Tools;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.client.HttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import static com.xiandao.android.http.hikhttp.HikConfig.APPKEY;
import static com.xiandao.android.http.hikhttp.HikConfig.APPSECRET;

/**
 * 网络请求工具类
 */
@SuppressWarnings("ALL")
public class NetUtil {
    private static int mConTimeout = 30000;
    private static int maxSize = 1048576;
    public static int NetType = 0;
    public static final int TYPE_OTHER_NET = 6;// 电信,移动,联通,wifi 等net网络
    private static final String TAG = "NetUtil";
    public static Uri PREFERRED_APN_URI = Uri
            .parse("content://telephony/carriers/preferapn");

    private static HttpClient getHttpClient(Context context) {
        int port = Proxy.getDefaultPort();
        BasicHttpParams basicHttpParams = new BasicHttpParams();
        HttpConnectionParams.setStaleCheckingEnabled(basicHttpParams, false);
        int status = checkNetworkType(context);

        if (status == TYPE_OTHER_NET) {
            // wifi net等网络
            NetType = TYPE_OTHER_NET;
            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, mConTimeout);
            HttpConnectionParams.setSoTimeout(httpParams, mConTimeout);
            HttpConnectionParams.setSocketBufferSize(httpParams, maxSize);
            return new DefaultHttpClient(httpParams);
        } else {
        }

        return new DefaultHttpClient(basicHttpParams);
    }

    public static HttpParams setHttpParams(Context context,
                                           int connectionTimeout) throws Exception {
        HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, mConTimeout);
        HttpConnectionParams.setSoTimeout(httpParams,
                connectionTimeout == 0 ? mConTimeout : connectionTimeout);
        HttpConnectionParams.setSocketBufferSize(httpParams, maxSize);
        return httpParams;
    }

    /**
     * wifi是否连接
     *
     * @param context
     * @return wifi是否连接
     */
    public static boolean isWifiConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWiFiNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWiFiNetworkInfo != null) {
                return mWiFiNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 是否能上网
     *
     * @param context
     * @return 是否能上网
     */
    public static boolean hasConnectedNetwork(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService
                ("connectivity");
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * @author TanYong
     * create at 2017/6/17 21:13
     * TODO：如果网络异常，就跳转到设置界面去
     */
    public static void toNetworkSetting(Activity mActivity) {
        if (!NetUtil.hasConnectedNetwork(mActivity)) {
            mActivity.startActivity(new Intent(mActivity, NetworkErrorActivity.class));
        } else {
            Tools.showPrompt("服务器异常");
        }
    }

    /**
     * 下载文件工具类 要放在后台
     *
     * @param context
     * @param url     下载的url
     * @param path    下载路径 为空默认为sd卡根目录
     * @return 是否下载成功
     */
    public static boolean urlDownloadToFile(Context context, String url,
                                            String path) {
        boolean bRet = false;
        FileOutputStream fos = null;
        InputStream is = null;
        try {
            HttpClient httpClient = null;
            try {
                httpClient = new DefaultHttpClient(setHttpParams(context,
                        mConTimeout));
            } catch (Exception e) {
                e.printStackTrace();
            }
            HttpGet get = new HttpGet(url);
            HttpResponse response = httpClient.execute(get);
            HttpEntity entity = response.getEntity();
            File file = null;
            if (path != null) {
                file = new File(path);
                file.createNewFile();
            } else {
                file = new File(Environment.getExternalStorageDirectory()
                        + "/xiandao/", "" + System.currentTimeMillis());
            }
            fos = new FileOutputStream(file);
            if (entity != null) {
                is = entity.getContent();
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                }
            }
            bRet = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return bRet;
    }

    /**
     * get方法
     *
     * @param vo 请求工具类
     * @return 返回对象
     * @throws Exception
     */
    public static Object get(RequestVo vo) throws Exception {
        String response = null;
        String encodedParams = "";
        if (vo.requestDataMap != null && !vo.requestDataMap.isEmpty()) {
            encodedParams = encodeParameters(vo.requestDataMap);
        }
        if (encodedParams.length() > 0) {
            if (-1 == vo.requestUrl.indexOf("?"))
                vo.requestUrl = vo.requestUrl + "?" + encodedParams;
            else {
                vo.requestUrl = vo.requestUrl + "&" + encodedParams;
            }
        }
        Log.d("get", "NetUtil:get:" + "url==" + vo.requestUrl);
        HttpGet method = new HttpGet(vo.requestUrl);
        response = httpRequest(vo.context, method, null);
        if (response != null) {
            return vo.jsonParser.parseJSON(vo.context, response);
        }
        return vo.jsonParser.parseJSON(vo.context, null);
    }

    /**
     * 简单请求工具类 可以放在主线程
     *
     * @param urlPath 请求url
     */
    public static void getSimple(final String urlPath) {
        new Thread() {
            public void run() {
                try {
                    HttpGet httpRequest = new HttpGet(urlPath);
                    HttpResponse httpResponse = new DefaultHttpClient()
                            .execute(httpRequest);
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        System.out.println("get提交成功=:" + urlPath);
                    } else {
                        System.out.println("get提交失败=:" + urlPath);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            ;
        }.start();

    }

    /**
     * 将参数键值Map按参数名进行排序，并按key1=value1&key2=value2&key3=value3的格式进行返回
     *
     * @param paramsMap
     * @return
     */
    public static String orderParamsMapAndReturnParamsString(Map<String, Object> paramsMap) {
        List<String> keys = new ArrayList<String>(paramsMap.keySet());
        Collections.sort(keys);
        StringBuffer paramStringBuffer = new StringBuffer();
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = (String) paramsMap.get(key);
            // 参数值为空的键值对，不要添加到字符串中。
            if (Tools.isEmpty(value)) {
                continue;
            }
            if (paramStringBuffer.length() > 0) {
                paramStringBuffer.append("&");
            }
            paramStringBuffer.append(key).append("=").append(value);
        }
        return paramStringBuffer.toString();
    }


    public static String getXCaSignature(String httpMethod,HttpPost method,RequestVo vo) {
        Header[] allHeaders = method.getAllHeaders();
        for (int i=0;i<allHeaders.length;i++ ){
            System.out.println(allHeaders[i].getName()+allHeaders[i].getValue());
        }
        String signature;
        StringBuilder sb=new StringBuilder();
        sb.append(httpMethod.toUpperCase()).append("\n");
        sb.append(method.getFirstHeader("Accept").getValue()).append("\n");
        sb.append(method.getFirstHeader("Content-Type").getValue()).append("\n");
        sb.append("x-ca-key:"+method.getFirstHeader("x-ca-key").getValue()).append("\n");
        sb.append("x-ca-nonce:"+method.getFirstHeader("x-ca-nonce").getValue()).append("\n");
        sb.append("x-ca-timestamp:"+method.getFirstHeader("x-ca-timestamp").getValue()).append("\n");
        sb.append(vo.getApi());
        System.out.println(sb);
        try {
            Mac hmacSha256 = Mac.getInstance("HmacSHA256");
            byte[] keyBytes = APPSECRET.getBytes("UTF-8");
            hmacSha256.init(new SecretKeySpec(keyBytes, 0, keyBytes.length, "HmacSHA256"));
            signature=new String(Base64.encodeBase64(hmacSha256.doFinal(sb.toString().getBytes("UTF-8"))), "UTF-8");
            System.out.println(signature);
        } catch (Exception var9) {
            throw new RuntimeException(var9);
        }
        return signature;
    }

    /**
     * 方法描述：post方式提交请求
     */
    public static Object post(RequestVo vo) throws Exception {
        vo.setType("post");
        HttpPost method = new HttpPost(vo.requestUrl);
        if(vo.getBody()!=null){
            method.addHeader("Accept","*/*");
            method.addHeader("Content-Type","application/json");
            method.addHeader("x-ca-timestamp",String.valueOf((new Date()).getTime()));
            method.addHeader("x-ca-nonce",UUID.randomUUID().toString());
            method.addHeader("x-ca-key",APPKEY);
            method.addHeader("x-ca-signature",getXCaSignature("POST",method,vo));
            method.addHeader("x-ca-signature-headers","x-ca-key,x-ca-nonce,x-ca-timestamp");
            method.setEntity(new StringEntity(vo.getBody(), "UTF-8"));
        }else{
            method.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
            List<NameValuePair> keyParams = new ArrayList<NameValuePair>();
            if (vo.requestDataMap != null) {
                Set<String> set = vo.requestDataMap.keySet();
                Iterator<String> iterator = set.iterator();
                while (iterator.hasNext()) {
                    String key = (String) iterator.next();
                    String value = (String) vo.requestDataMap.get(key);
                    if ((value != null) && (!"".equals(value))) {
                        keyParams.add(new BasicNameValuePair(key, value));
                    }
                }
            }
            try {
                method.setEntity(new UrlEncodedFormEntity(keyParams, HTTP.UTF_8));
            } catch (UnsupportedEncodingException e) {
            }
        }
        String response = httpRequest(vo.context, method, null);

        if (response != null) {
            vo.jsonParser.reloadTask(vo);
            return vo.jsonParser.parseJSON(vo.context, response);
        } else {
            return vo.jsonParser.parseJSON(vo.context, null);
        }
    }

    public static String post2(RequestVo vo) throws Exception {

        HttpPost method = new HttpPost(vo.requestUrl);
        Log.d("NetUtil:post:", "url==" + vo.requestUrl);
        List<BasicNameValuePair> keyParams = new ArrayList<BasicNameValuePair>();
        if (vo.requestDataMap != null) {
            Set<String> set = vo.requestDataMap.keySet();
            Iterator<String> iterator = set.iterator();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                String value = (String) vo.requestDataMap.get(key);
                if ((value != null) && (!"".equals(value))) {
                    keyParams.add(new BasicNameValuePair(key, value));
                }
            }
        }
        try {
            method.setEntity(new UrlEncodedFormEntity(keyParams, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
        }
        String response = httpRequest(vo.context, method, null);

        if (response != null) {
            return response;
        } else {
            return null;
        }
    }

    /**
     * @param method
     * @return 返回的结果字符串
     * @throws Exception
     */
    private static String httpRequest(Context context, HttpRequestBase method,
                                      String path) throws Exception {
        int code = -1;
        String result = null;
        try {
            HttpResponse httpResponse = getHttpClient(context).execute(method);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            Log.d("NetUtil:httpRequest:statusCode=", statusCode + "");
            if (200 == statusCode) {
                result = EntityUtils
                        .toString(httpResponse.getEntity(), "utf-8");
                Log.d("NetUtil:httpRequest:result=", result);
                if (result == null || result.equals("")) {
                    return null;
                }
            }
            return result;
        } catch (Exception ioe) {
            ioe.printStackTrace();
            Log.d("NetUtil:Response" + "htjx httpRequest exception:"
                    , ioe.getMessage());
            return null;
        } finally {
            method.abort();
        }

    }

    /**
     * 获取网络类型
     *
     * @param mContext
     * @return 网络类型
     */
    public static int checkNetworkType(Context mContext) {
        try {
            final ConnectivityManager connectivityManager = (ConnectivityManager) mContext
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            final NetworkInfo mobNetInfoActivity = connectivityManager
                    .getActiveNetworkInfo();
            if (mobNetInfoActivity == null || !mobNetInfoActivity.isAvailable()) {

                // 注意一：
                // NetworkInfo 为空或者不可以用的时候正常情况应该是当前没有可用网络，
                // 但是有些电信机器，仍可以正常联网，
                // 所以当成net网络处理依然尝试连接网络。
                // （然后在socket中捕捉异常，进行二次判断与用户提示）。
                Log.i("", "=====================>无网络");
                return TYPE_OTHER_NET;
            } else {
                // NetworkInfo不为null开始判断是网络类型
                int netType = mobNetInfoActivity.getType();
                if (netType == ConnectivityManager.TYPE_WIFI) {
                    // wifi net处理
                    Log.i("", "=====================>wifi网络");
                    return TYPE_OTHER_NET;
                } else if (netType == ConnectivityManager.TYPE_MOBILE) {

                    final Cursor c = mContext.getContentResolver().query(
                            PREFERRED_APN_URI, null, null, null, null);
                    if (c != null) {
                        c.moveToFirst();
                        final String user = c.getString(c
                                .getColumnIndex("user"));
                        if (!TextUtils.isEmpty(user)) {
                            Log.i("",
                                    "=====================>代理："
                                            + c.getString(c
                                            .getColumnIndex("proxy")));
                        }
                    }
                    c.close();

                    String netMode = mobNetInfoActivity.getExtraInfo();
                    Log.i("", "netMode ================== " + netMode);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return TYPE_OTHER_NET;
        }

        return TYPE_OTHER_NET;

    }

    /**
     * 方法描述：对请求参数进行编码(get方式)
     */
    private static String encodeParameters(Map<String, Object> map) {
        StringBuffer buf = new StringBuffer();
        Set<String> set = map.keySet();
        Iterator<String> iterator = set.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            String value = (String) map.get(key);

            if ((key == null) || ("".equals(key)) || (value == null)
                    || ("".equals(value))) {
                continue;
            }
            if (i != 0)
                buf.append("&");
            try {
                buf.append(URLEncoder.encode(key, "UTF-8")).append("=")
                        .append(URLEncoder.encode(value, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            i++;
        }
        return buf.toString();
    }

    /**
     * 是否是wifi网络
     *
     * @param mContext 上下文
     * @return 是否是wifi网络
     */
    public static boolean isWifi(Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

    /**
     * 获取本机IP地址
     *
     * @return IP
     */
    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e("get IpAddress fail", ex.toString());
            return "";
        }
        return "";
    }
}