package com.xiandao.android.http;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.xiandao.android.net.NetUtil;
import com.xiandao.android.ui.activity.NetworkErrorActivity;
import com.xiandao.android.utils.Constants;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.httpclient.params.HttpMethodParams;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;
import java.util.zip.GZIPInputStream;

public class ApiClient {
    public static final String UTF_8 = "UTF-8";
    private final static int TIMEOUT_CONNECTION = 90000;
    private final static int TIMEOUT_SOCKET = 90000;
    private final static int RETRY_TIME = 1;
    private final static int RETRY_TIMESEND = 1;
    private final static int RETRY_FRIENDS = 1;

    private static HttpClient getHttpClient() {
        HttpClient httpClient = new HttpClient();
        // 设置 HttpClient 接收 Cookie,用与浏览器一样的策略
        httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
        // 设置 默认的超时重试处理策略
        httpClient.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
        // 设置 连接超时时间
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(TIMEOUT_CONNECTION);
        // 设置 读数据超时时间
        httpClient.getHttpConnectionManager().getParams().setSoTimeout(TIMEOUT_SOCKET);
        // 设置 字符集
        httpClient.getParams().setContentCharset(UTF_8);
        return httpClient;
    }

    private static GetMethod getHttpGet(String url) {
        try {
            // 使用 GET 方法 ，如果服务器需要通过 HTTPS 连接，那只需要将下面 URL 中的 http 换成 https
            GetMethod httpGet = new GetMethod(url);
            // 设置 请求超时时间
            httpGet.getParams().setSoTimeout(TIMEOUT_SOCKET);
            httpGet.setRequestHeader("Host", Constants.HOST);
            httpGet.addRequestHeader("accept-encoding", "gzip,deflate");
            return httpGet;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static PostMethod getHttpPost(String url) {
        // 使用 POST方法 ，如果服务器需要通过 HTTPS 连接，那只需要将下面 URL 中的 http 换成 https
        PostMethod httpPost = new PostMethod(url);
        // 设置 请求超时时间
        httpPost.getParams().setSoTimeout(TIMEOUT_SOCKET);
        httpPost.addRequestHeader("accept-encoding", "gzip,deflate");
        httpPost.setRequestHeader("Host", Constants.HOST);
        return httpPost;
    }

    /**
     * @param url
     * @return
     * @方法说明:get请求URL
     * @方法名称:http_get
     * @返回值:String
     */
    public static String http_get(String url) {
        HttpClient httpClient = null;
        GetMethod httpGet = null;
        String responseBody = "";
        int time = 0;
        do {
            try {
                httpClient = getHttpClient();
                httpGet = getHttpGet(url);
                Log.e("http_get----", url);
                int statusCode = httpClient.executeMethod(httpGet);
                if (statusCode != HttpStatus.SC_OK) {
                } else {
                }
                responseBody = getPostData(httpGet.getResponseBodyAsStream());
                break;
            } catch (HttpException e) {
                time++;
                if (time < RETRY_TIME) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e1) {
                    }
                    continue;
                }
                // 发生致命的异常，可能是协议不对或者返回的内容有问题
                responseBody = HttpDataConnet.EXCEPTION_HTTP_DEADLY;// 网络异常
                e.printStackTrace();
            } catch (IOException e) {
                time++;
                if (time < RETRY_TIME) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e1) {
                    }
                    continue;
                }
                // 发生网络异常
                responseBody = HttpDataConnet.EXCEPTION_HTTP_NET;// 网络异常
                e.printStackTrace();
            } finally {
                // 释放连接
                httpGet.releaseConnection();
                httpClient = null;
            }
        } while (time < RETRY_TIME);
        return responseBody;
    }


    // 公用Post，params是指参数的映射
    public static String http_getPost(String url, Map<String, Object> params,
                                      File[] files) {
        HttpClient httpClient = null;
        PostMethod httpGet = null;
        String responseBody = "";
        int time = 0;
        // post表单参数处理
        int length = 0;
        if (files != null) {
            length = (params == null ? 0 : params.size()) + files.length;
        } else {
            length = (params == null ? 0 : params.size());
        }
        Part[] parts = new Part[length];
        int i = 0;
        if (params != null)
            for (String name : params.keySet()) {
                parts[i++] = new StringPart(name, String.valueOf(params.get(name)), UTF_8);
            }
        if (files != null) {
            try {
                for (int j = 0; j < files.length; j++) {
                    parts[i + j] = new FilePart("pic", files[0 + j]);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        do {
            try {
                httpClient = getHttpClient();
                httpGet = getHttpPost(url);
                httpGet.setRequestEntity(new MultipartRequestEntity(parts, httpGet.getParams()));
                httpGet.getParams().setContentCharset("UTF-8");
                int statusCode = httpClient.executeMethod(httpGet);
                if (statusCode == HttpStatus.SC_OK) {
                } else {
                    Log.e("ApiClient", url.toString() + "  === http_getPost错误ApiClient statusCode is ==== " + statusCode);
                }
                responseBody = getPostData(httpGet.getResponseBodyAsStream());
                break;
            } catch (Exception e) {
                time++;
                if (time < RETRY_TIME) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e1) {
                    }
                    continue;
                }
                responseBody = HttpDataConnet.EXCEPTION_HTTP_DEADLY;// 致命异常
                // 发生致命的异常，可能是协议不对或者返回的内容有问题
            } finally {
                if (httpGet != null) {
                    // 释放连接
                    httpGet.releaseConnection();
                    httpClient = null;
                }
            }
        } while (time < RETRY_TIME);
        return responseBody;
    }


    /**
     * @param url
     * @param params
     * @return
     * @throws Exception
     * @方法说明:获取网络图片
     * @方法名称:getNetPhotoBitmap
     * @返回值:Bitmap
     */
    public static Bitmap getNetPhotoBitmap(String url, Map<String, Object> params) throws Exception {
        HttpClient httpClient = null;
        PostMethod httpGet = null;
        Bitmap bitmap = null;
        int time = 0;
        // post表单参数处理
        int length = (params == null ? 0 : params.size());
        Part[] parts = new Part[length];
        int i = 0;
        if (params != null)
            for (String name : params.keySet()) {
                parts[i++] = new StringPart(name, String.valueOf(params.get(name)), UTF_8);
            }
        do {
            try {
                httpClient = getHttpClient();
                httpGet = getHttpPost(url);
                httpGet.setRequestEntity(new MultipartRequestEntity(parts,
                        httpGet.getParams()));
                int statusCode = httpClient.executeMethod(httpGet);
                if (statusCode == HttpStatus.SC_OK) {

                } else {
                    Log.e("ApiClient", url.toString() + "  === getNetPhotoBitmap错误ApiClient statusCode is ==== " + statusCode);
                }
                InputStream inStream = httpGet.getResponseBodyAsStream();

                bitmap = getBitmapPostData(inStream);

                inStream.close();
                break;
            } catch (HttpException e) {
                time++;
                if (time < RETRY_TIME) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e1) {
                    }
                    continue;
                }
                // 发生致命的异常，可能是协议不对或者返回的内容有问题
                e.printStackTrace();
            } catch (IOException e) {
                time++;
                if (time < RETRY_TIME) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e1) {
                    }
                    continue;
                }
                // 发生网络异常
                e.printStackTrace();
            } finally {
                if (httpGet != null) {
                    // 释放连接
                    httpGet.releaseConnection();
                    httpClient = null;
                }
            }
        } while (time < RETRY_TIME);
        return bitmap;
    }

    /**
     * @param pis
     * @return
     * @方法说明：解析post请求返回的数据
     * @方法名称：getPostData
     * @返回值：String
     */
    public static String getPostData(InputStream pis) {
        String responseBody = "";
        try {
            InputStream is = pis;
            BufferedInputStream bis = new BufferedInputStream(is);
            bis.mark(2);
            // 取前两个字节
            byte[] header = new byte[2];
            int result = bis.read(header);
            // reset输入流到开始位置
            bis.reset();
            // 判断是否是GZIP格式
            int headerData = getShort(header);
            // Gzip 流 的前两个字节是 0x1f8b
            if (result != -1 && headerData == 0x1f8b) {
                System.out.println("HttpTask use GZIPInputStream");
                is = new GZIPInputStream(bis);
            } else {
                System.out.println("HttpTask not use GZIPInputStream");
                is = bis;
            }
            InputStreamReader reader = new InputStreamReader(is, "utf-8");
            char[] data = new char[100];
            int readSize;
            StringBuffer sb = new StringBuffer();
            while ((readSize = reader.read(data)) > 0) {
                sb.append(data, 0, readSize);
            }
            responseBody = sb.toString();
            bis.close();
            reader.close();
            return responseBody;
        } catch (Exception e) {
            return responseBody;
        }
    }

    /**
     * 图片下载
     *
     * @param pis
     * @return
     */
    public static Bitmap getBitmapPostData(InputStream pis) {
        Bitmap bitmap = null;
        try {
            InputStream is = pis;
            BufferedInputStream bis = new BufferedInputStream(is);
            bis.mark(2);
            // 取前两个字节
            byte[] header = new byte[2];
            int result = bis.read(header);
            // reset输入流到开始位置
            bis.reset();
            // 判断是否是GZIP格式
            int headerData = getShort(header);
            // Gzip 流 的前两个字节是 0x1f8b
            if (result != -1 && headerData == 0x1f8b) {
                System.out.println("HttpTask use GZIPInputStream");
                is = new GZIPInputStream(bis);
            } else {
                System.out.println("HttpTask not use GZIPInputStream");
                is = bis;
            }
            bitmap = BitmapFactory.decodeStream(is);
            bis.close();
            return bitmap;
        } catch (Exception e) {
            return bitmap;
        }
    }

    /**
     * 方法说明：批量上传文件 方法名称：http_getSendPost
     *
     * @param url    请求服务器地址
     * @param params 普通参数
     * @param files  文件集合
     * @return 返回值：String
     */
    public static String http_getBatchUploadPost(String url, Map<String, Object> params, ArrayList<File> files) {
        HttpClient httpClient = null;
        PostMethod httpGet = null;
        String responseBody = "";
        int time = 0;
        // post表单参数处理
        int length = 0;
        if (files != null) {
            length = (params == null ? 0 : params.size()) + files.size();
        } else {
            length = (params == null ? 0 : params.size());
        }
        Part[] parts = new Part[length];
        int i = 0;
        if (params != null)
            for (String name : params.keySet()) {
                parts[i++] = new StringPart(name, String.valueOf(params.get(name)), UTF_8);
            }
        if (files != null) {
            try {
                for (int j = 0; j < files.size(); j++) {
                    parts[i++] = new FilePart("pic", files.get(j));
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        do {
            try {
                httpClient = getHttpClient();
                httpGet = getHttpPost(url);
                httpGet.setRequestEntity(new MultipartRequestEntity(parts,
                        httpGet.getParams()));
                httpGet.getParams().setContentCharset("UTF-8");
                int statusCode = httpClient.executeMethod(httpGet);
                if (statusCode == HttpStatus.SC_OK) {

                } else {

                }
                responseBody = getPostData(httpGet.getResponseBodyAsStream());
                break;
            } catch (Exception e) {
                time++;
                if (time < RETRY_FRIENDS) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e1) {
                    }
                    continue;
                }
                responseBody = HttpDataConnet.EXCEPTION_HTTP_DEADLY;// 致命异常
                // 发生致命的异常，可能是协议不对或者返回的内容有问题
                e.printStackTrace();
            } finally {
                // 释放连接
                httpGet.releaseConnection();
                httpClient = null;
            }
        } while (time < RETRY_FRIENDS);
        return responseBody;
    }


    private static int getShort(byte[] data) {
        return (int) ((data[0] << 8) | data[1] & 0xFF);
    }
}
