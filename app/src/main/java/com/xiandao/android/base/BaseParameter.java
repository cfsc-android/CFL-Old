package com.xiandao.android.base;

import android.content.Context;
import android.util.Log;

import com.xiandao.android.utils.Constants;
import com.xiandao.android.utils.FileManagement;
import com.xiandao.android.utils.Tools;

import org.xutils.common.util.MD5;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author TanYong
 *         create at 2017/4/21 9:42
 */
public abstract class BaseParameter {
    private Context mContext;

    /**
     * 未登录全局参数（只支持服务平台接口）
     *
     * @return
     */
    public static Map<String, Object> getOverallSituationParameter(Map<String, Object> map, String[] keyenty, Object[] val) {
        Map<String, Object> paramsMap;
        paramsMap = httPutParam(keyenty, val, map);
        if (paramsMap == null) {
            paramsMap = new HashMap<>();
        }
//        long time = Tools.getLocationTime();
//        if (time <= 0) {
//            time = Tools.getUnixTimestamp();
//        }
        if (!Tools.isEmpty(FileManagement.getTokenInfo())) {
//            paramsMap.put("appType", "0");//type 0业主1员工
            paramsMap.put("appTokenInfo", FileManagement.getTokenInfo());
        }
//        paramsMap.put("terminalType", Constants.terminalType);
//        paramsMap.put("t", time + "");
//        paramsMap.put("h", MD5.md5(orderParamsMapAndReturnParamsString(paramsMap)));
        return paramsMap;
    }

    /**
     * 未登录全局参数（只支持服务平台接口）
     *
     * @return
     */
    public static Map<String, Object> getOverallNoLoginParameter(Map<String, Object> map, String[] keyenty, Object[] val) {
        Map<String, Object> paramsMap;
        paramsMap = httPutParam(keyenty, val, map);
        if (paramsMap == null) {
            paramsMap = new HashMap<>();
        }
        long time = Tools.getLocationTime();
        if (time <= 0) {
            time = Tools.getUnixTimestamp();
        }
        paramsMap.put("appId", Constants.clientId);
        paramsMap.put("channel", Constants.channel);
        paramsMap.put("terminalType", Constants.terminalType);
        paramsMap.put("t", time + "");
        String md5Data = orderParamsMapAndReturnParamsString(paramsMap);
        md5Data = md5Data + "&appKey=" + Constants.APPKEY;
        paramsMap.put("h", MD5.md5(md5Data));
        return paramsMap;
    }

    /**
     * 登录全局参数（只支持服务平台接口）
     *
     * @return
     */
    public static Map<String, Object> getAccesTokennParameter(Map<String, Object> map, String[] keyenty, Object[] val) {
        Map<String, Object> paramsMap;
        paramsMap = httPutParam(keyenty, val, map);
        if (paramsMap == null) {
            paramsMap = new HashMap<>();
        }
        long time = Tools.getLocationTime();
        if (time <= 0) {
            time = Tools.getUnixTimestamp();
        }
        String apkKey = Constants.APPKEY;
        paramsMap.put("clientId", Constants.clientId);
        paramsMap.put("channel", Constants.channel);
        paramsMap.put("terminalType", Constants.terminalType);
        paramsMap.put("t", time + "");
        String md5Data = orderParamsMapAndReturnParamsString(paramsMap);
        md5Data = md5Data + "&appKey=" + apkKey;
        Log.e("md5data=", md5Data);
        paramsMap.put("h", MD5.md5(md5Data));
        return paramsMap;
    }

    /**
     * 登录全局参数
     *
     * @return
     */
    public static Map<String, Object> getLoginOverallSituationParameter(Map<String, Object> map, String[] keyenty, Object[] val) {
        Map<String, Object> paramsMap;
        paramsMap = httPutParam(keyenty, val, map);
        if (paramsMap == null) {
            paramsMap = new HashMap<>();
        }
        long time = Tools.getLocationTime();
        if (time <= 0) {
            time = Tools.getUnixTimestamp();
        }
        String apkKey = Constants.APPKEY;
        paramsMap.put("appId", Constants.clientId);
        paramsMap.put("channel", Constants.channel);
        paramsMap.put("terminalType", Constants.terminalType);
        paramsMap.put("t", time + "");
        String md5Data = orderParamsMapAndReturnParamsString(paramsMap);
        md5Data = md5Data + "&appKey=" + apkKey;
        paramsMap.put("h", MD5.md5(md5Data));
        return paramsMap;
    }


    /**
     *
     * postUrlStr(加密Post请求URL,返回String)
     *
     * @Title: postUrlStr
     * @Description: TODO
     * @param @param beanMap 参数map
     * @param @param url
     * @param @return    设定文件
     * @return String    返回类型
     * @throws
     */
//    public static String postUrlStr(Map<String, String> beanMap,String url,String hostStr,String appId,String appKey){
//        try{
//            beanMap.put("appId", appId);
//            url=hostStr+url;
//            TimeZone zone = TimeZone.getTimeZone("Etc/Greenwich");
//            Long t= Calendar.getInstance(zone).getTime().getTime() / 1000;
//            beanMap.put("t", String.valueOf(t));
//            // 将请求参数Bean排序，并转为String,以便生成md5摘要
//            String requestString = URLDecoder.decode(RequestUtil.orderParamsMapAndReturnParamsString(beanMap), "UTF-8");
//            logger.info("请求地址"+url+"加密前请求数据参数："+requestString);
//            // 生成md5摘要
//            requestString = requestString + "&appKey=" + appKey;
//            String sgin = DigestUtils.md5DigestAsHex(requestString.toString().getBytes());
//            // 将摘要(sgin)添加到beanMap中
//            beanMap.put("h", sgin);
//            // 创建httpClient
//            httpClient = new DefaultHttpClient();
//            HttpPost postMethod = new HttpPost(url);
//            // 将Bean转为HttpPost所能接收的数据类型.
//            List<NameValuePair> pairList = new ArrayList<NameValuePair>();
//            for (Map.Entry<String, String> entry : beanMap.entrySet()) {
//                pairList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
//            }
//            // 指定字符集
//            UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(pairList, "UTF-8");
//            // 设置post参数
//            postMethod.setEntity(uefEntity);
//            // 提交请求
//            HttpResponse response = httpClient.execute(postMethod);
//            HttpEntity result=response.getEntity();
//            String rsultStr=EntityUtils.toString(result);
//            logger.info("接口返回结果:"+rsultStr);
//            return rsultStr;
//        } catch (Exception e){
//            logger.error("发送请求失败，原因："+e.getMessage());
//        }
//        return null;
//    }

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

    /**
     * 将参数键值Map按参数名进行排序，并按key1=value1&key2=value2&key3=value3的格式进行返回
     *
     * @param paramsMap
     * @return
     */
    public static String orderParamsMapAndReturnParamsString1(Map<String, Object> paramsMap) {
        if (paramsMap == null) return "";
        List<String> keys = new ArrayList<String>(paramsMap.keySet());
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

    /**
     * 方法说明：保存Url参数 方法名称：httPutParam
     *
     * @param paramKey
     * @param paramValue
     * @param paramsData 返回值：Map<String,Object>
     */
    public static Map<String, Object> httPutParam(String[] paramKey, Object[] paramValue,
                                                  Map<String, Object> paramsData) {
        if (paramKey == null || paramValue == null) {
            return null;
        }
        for (int i = 0; i < paramKey.length; i++) {
            String key = paramKey[i];
            String value = String.valueOf(paramValue[i]);
            if (!Tools.isEmpty(value)) {
//                try {
//                    value = URLEncoder.encode(value, "UTF-8");
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
                paramsData.put(key, value);
            }
        }
        return paramsData;
    }
}
