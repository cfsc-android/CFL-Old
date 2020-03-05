package com.xiandao.android.http.hikhttp;


import com.xiandao.android.http.hikhttp.config.ArtemisConfig;
import com.xiandao.android.http.hikhttp.enums.Method;
import com.xiandao.android.http.hikhttp.util.MessageDigestUtil;
import com.xiandao.android.utils.Tools;

import org.apache.http.HttpResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HikHttpUtil {
    private static final List<String> CUSTOM_HEADERS_TO_SIGN_PREFIX = new ArrayList();
    private static final String SUCC_PRE = "2";
    private static final String REDIRECT_PRE = "3";

    public HikHttpUtil() {
    }
    public static String doGetArtemis(Map<String, String> path, Map<String, String> querys, String accept, String contentType, Map<String, String> header) {
        String httpSchema = (String)path.keySet().toArray()[0];
        if (httpSchema != null && !Tools.isEmpty(httpSchema)) {
            String responseStr = null;

            try {
                Map<String, String> headers = new HashMap();
                headers.put("Accept", "*/*");
                headers.put("Content-Type", "application/text;charset=UTF-8");

                if (header != null) {
                    headers.putAll(header);
                }

                System.out.println((String)path.get(httpSchema));
                CUSTOM_HEADERS_TO_SIGN_PREFIX.clear();
                Request request = new Request(Method.GET, httpSchema + ArtemisConfig.host, (String)path.get(httpSchema), ArtemisConfig.appKey, ArtemisConfig.appSecret, 100);
                request.setHeaders(headers);
                request.setSignHeaderPrefixList(CUSTOM_HEADERS_TO_SIGN_PREFIX);
                request.setQuerys(querys);
                Response response = Client.execute(request);
                responseStr = response.getBody();
            } catch (Exception var10) {
                System.out.println("the Artemis GET Request is failed[doGetArtemis]"+ var10);
            }

            return responseStr;
        } else {
            throw new RuntimeException("http鍜宧ttps鍙傛暟閿欒\ue1e4httpSchema: " + httpSchema);
        }
    }

    public static HttpResponse doGetResponse(Map<String, String> path, Map<String, String> querys, String accept, String contentType, Map<String, String> header) {
        String httpSchema = (String)path.keySet().toArray()[0];
        if (httpSchema != null && !Tools.isEmpty(httpSchema)) {
            HttpResponse httpResponse = null;

            try {
                Map<String, String> headers = new HashMap();
                headers.put("Accept", "*/*");
                headers.put("Content-Type", "application/text;charset=UTF-8");
                if (header != null) {
                    headers.putAll(header);
                }

                CUSTOM_HEADERS_TO_SIGN_PREFIX.clear();
                Request request = new Request(Method.GET_RESPONSE, httpSchema + ArtemisConfig.host, (String)path.get(httpSchema), ArtemisConfig.appKey, ArtemisConfig.appSecret, 100);
                request.setHeaders(headers);
                request.setSignHeaderPrefixList(CUSTOM_HEADERS_TO_SIGN_PREFIX);
                request.setQuerys(querys);
                Response response = Client.execute(request);
                httpResponse = response.getResponse();
            } catch (Exception var10) {
                System.out.println("the Artemis GET Request is failed[doGetArtemis]"+var10);
            }

            return httpResponse;
        } else {
            throw new RuntimeException("http鍜宧ttps鍙傛暟閿欒\ue1e4httpSchema: " + httpSchema);
        }
    }

    public static String doPostFormArtemis(Map<String, String> path, Map<String, String> paramMap, Map<String, String> querys, String accept, String contentType, Map<String, String> header) {
        String httpSchema = (String)path.keySet().toArray()[0];
        if (httpSchema != null && !Tools.isEmpty(httpSchema)) {
            String responseStr = null;

            try {
                Map<String, String> headers = new HashMap();
                headers.put("Accept", "*/*");
                headers.put("Content-Type", "application/json");
//                headers.put("Content-Type", "application/text;charset=UTF-8");

                if (header != null) {
                    headers.putAll(header);
                }

                CUSTOM_HEADERS_TO_SIGN_PREFIX.clear();
                Request request = new Request(Method.POST_FORM, httpSchema + ArtemisConfig.host, (String)path.get(httpSchema), ArtemisConfig.appKey, ArtemisConfig.appSecret, 100);
                request.setHeaders(headers);
                request.setSignHeaderPrefixList(CUSTOM_HEADERS_TO_SIGN_PREFIX);
                request.setQuerys(querys);
                request.setBodys(paramMap);

                Response response = Client.execute(request);
                responseStr = getResponseResult(response);
            } catch (Exception var11) {
                System.out.println("the Artemis PostForm Request is failed[doPostFormArtemis]"+ var11);
            }

            return responseStr;
        } else {
            throw new RuntimeException("http鍜宧ttps鍙傛暟閿欒\ue1e4httpSchema: " + httpSchema);
        }
    }

    public static HttpResponse doPostFormImgArtemis(Map<String, String> path, Map<String, String> paramMap, Map<String, String> querys, String accept, String contentType, Map<String, String> header) {
        String httpSchema = (String)path.keySet().toArray()[0];
        if (httpSchema != null && !Tools.isEmpty(httpSchema)) {
            HttpResponse response = null;

            try {
                Map<String, String> headers = new HashMap();
                headers.put("Accept", "*/*");
                headers.put("Content-Type", "application/text;charset=UTF-8");

                if (header != null) {
                    headers.putAll(header);
                }

                CUSTOM_HEADERS_TO_SIGN_PREFIX.clear();
                Request request = new Request(Method.POST_FORM_RESPONSE, httpSchema + ArtemisConfig.host, (String)path.get(httpSchema), ArtemisConfig.appKey, ArtemisConfig.appSecret, 100);
                request.setHeaders(headers);
                request.setSignHeaderPrefixList(CUSTOM_HEADERS_TO_SIGN_PREFIX);
                request.setQuerys(querys);
                request.setBodys(paramMap);
                Response response1 = Client.execute(request);
                response = response1.getResponse();
            } catch (Exception var11) {
                System.out.println("the Artemis PostForm Request is failed[doPostFormImgArtemis]"+var11);
            }

            return response;
        } else {
            throw new RuntimeException("http鍜宧ttps鍙傛暟閿欒\ue1e4httpSchema: " + httpSchema);
        }
    }

    public static String doPostStringArtemis(Map<String, String> path, String body, Map<String, String> querys, String accept, String contentType, Map<String, String> header) {
        String httpSchema = (String)path.keySet().toArray()[0];
        if (httpSchema != null && !Tools.isEmpty(httpSchema)) {
            String responseStr = null;

            try {
                Map<String, String> headers = new HashMap();
//                headers.put("Accept", "*/*");
////                headers.put("Content-Type", "application/text;charset=UTF-8");
                headers.put("Accept", accept);
                headers.put("Content-Type", contentType);
                if (header != null) {
                    headers.putAll(header);
                }

                CUSTOM_HEADERS_TO_SIGN_PREFIX.clear();
                Request request = new Request(Method.POST_STRING, httpSchema + ArtemisConfig.host, (String)path.get(httpSchema), ArtemisConfig.appKey, ArtemisConfig.appSecret, 100);
                request.setHeaders(headers);
                request.setSignHeaderPrefixList(CUSTOM_HEADERS_TO_SIGN_PREFIX);
                request.setQuerys(querys);
                request.setStringBody(body);
                Response response = Client.execute(request);
                responseStr = getResponseResult(response);
            } catch (Exception var11) {
                System.out.println("the Artemis PostString Request is failed[doPostStringArtemis]"+var11);
            }

            return responseStr;
        } else {
            throw new RuntimeException("http鍜宧ttps鍙傛暟閿欒\ue1e4httpSchema: " + httpSchema);
        }
    }

    public static HttpResponse doPostStringImgArtemis(Map<String, String> path, String body, Map<String, String> querys, String accept, String contentType, Map<String, String> header) {
        String httpSchema = (String)path.keySet().toArray()[0];
        if (httpSchema != null && !Tools.isEmpty(httpSchema)) {
            HttpResponse responseStr = null;

            try {
                Map<String, String> headers = new HashMap();
                headers.put("Accept", "*/*");
                headers.put("Content-Type", "application/text;charset=UTF-8");

                if (header != null) {
                    headers.putAll(header);
                }

                CUSTOM_HEADERS_TO_SIGN_PREFIX.clear();
                Request request = new Request(Method.POST_STRING_RESPONSE, httpSchema + ArtemisConfig.host, (String)path.get(httpSchema), ArtemisConfig.appKey, ArtemisConfig.appSecret, 100);
                request.setHeaders(headers);
                request.setSignHeaderPrefixList(CUSTOM_HEADERS_TO_SIGN_PREFIX);
                request.setQuerys(querys);
                request.setStringBody(body);
                Response response = Client.execute(request);
                responseStr = response.getResponse();
            } catch (Exception var11) {
                System.out.println("the Artemis PostString Request is failed[doPostStringArtemis]"+ var11);
            }

            return responseStr;
        } else {
            throw new RuntimeException("http鍜宧ttps鍙傛暟閿欒\ue1e4httpSchema: " + httpSchema);
        }
    }

    public static String doPostBytesArtemis(Map<String, String> path, byte[] bytesBody, Map<String, String> querys, String accept, String contentType, Map<String, String> header) {
        String httpSchema = (String)path.keySet().toArray()[0];
        if (httpSchema != null && !Tools.isEmpty(httpSchema)) {
            String responseStr = null;

            try {
                Map<String, String> headers = new HashMap();
                headers.put("Accept", "*/*");
                if (bytesBody != null) {
                    headers.put("Content-MD5", MessageDigestUtil.base64AndMD5(bytesBody));
                }
                headers.put("Content-Type", "application/text;charset=UTF-8");



                if (header != null) {
                    headers.putAll(header);
                }

                CUSTOM_HEADERS_TO_SIGN_PREFIX.clear();
                Request request = new Request(Method.POST_BYTES, httpSchema + ArtemisConfig.host, (String)path.get(httpSchema), ArtemisConfig.appKey, ArtemisConfig.appSecret, 100);
                request.setHeaders(headers);
                request.setSignHeaderPrefixList(CUSTOM_HEADERS_TO_SIGN_PREFIX);
                request.setQuerys(querys);
                request.setBytesBody(bytesBody);
                Response response = Client.execute(request);
                responseStr = getResponseResult(response);
            } catch (Exception var11) {
                System.out.println("the Artemis PostBytes Request is failed[doPostBytesArtemis]"+ var11);
            }

            return responseStr;
        } else {
            throw new RuntimeException("http鍜宧ttps鍙傛暟閿欒\ue1e4httpSchema: " + httpSchema);
        }
    }

    public static String doPutStringArtemis(Map<String, String> path, String body, String accept, String contentType) {
        String httpSchema = (String)path.keySet().toArray()[0];
        if (httpSchema != null && !Tools.isEmpty(httpSchema)) {
            String responseStr = null;

            try {
                Map<String, String> headers = new HashMap();
                headers.put("Accept", "*/*");
                headers.put("Content-MD5", MessageDigestUtil.base64AndMD5(body));
                headers.put("Content-Type", "application/text;charset=UTF-8");


                CUSTOM_HEADERS_TO_SIGN_PREFIX.clear();
                Request request = new Request(Method.PUT_STRING, httpSchema + ArtemisConfig.host, (String)path.get(httpSchema), ArtemisConfig.appKey, ArtemisConfig.appSecret, 100);
                request.setHeaders(headers);
                request.setSignHeaderPrefixList(CUSTOM_HEADERS_TO_SIGN_PREFIX);
                request.setStringBody(body);
                Response response = Client.execute(request);
                responseStr = getResponseResult(response);
            } catch (Exception var9) {
                System.out.println("the Artemis PutString Request is failed[doPutStringArtemis]"+ var9);
            }

            return responseStr;
        } else {
            throw new RuntimeException("http鍜宧ttps鍙傛暟閿欒\ue1e4httpSchema: " + httpSchema);
        }
    }

    public static String doPutBytesArtemis(Map<String, String> path, byte[] bytesBody, String accept, String contentType) {
        String httpSchema = (String)path.keySet().toArray()[0];
        if (httpSchema != null && !Tools.isEmpty(httpSchema)) {
            String responseStr = null;

            try {
                Map<String, String> headers = new HashMap();
                headers.put("Accept", "*/*");
                if (bytesBody != null) {
                    headers.put("Content-MD5", MessageDigestUtil.base64AndMD5(bytesBody));
                }
                headers.put("Content-Type", "application/text;charset=UTF-8");



                CUSTOM_HEADERS_TO_SIGN_PREFIX.clear();
                Request request = new Request(Method.PUT_BYTES, httpSchema + ArtemisConfig.host, (String)path.get(httpSchema), ArtemisConfig.appKey, ArtemisConfig.appSecret, 100);
                request.setHeaders(headers);
                request.setSignHeaderPrefixList(CUSTOM_HEADERS_TO_SIGN_PREFIX);
                request.setBytesBody(bytesBody);
                Response response = Client.execute(request);
                responseStr = getResponseResult(response);
            } catch (Exception var9) {
                System.out.println("the Artemis PutBytes Request is failed[doPutBytesArtemis]"+ var9);
            }

            return responseStr;
        } else {
            throw new RuntimeException("http鍜宧ttps鍙傛暟閿欒\ue1e4httpSchema: " + httpSchema);
        }
    }

    public static String doDeleteArtemis(Map<String, String> path, Map<String, String> querys, String accept, String contentType) {
        String httpSchema = (String)path.keySet().toArray()[0];
        if (httpSchema != null && !Tools.isEmpty(httpSchema)) {
            String responseStr = null;

            try {
                Map<String, String> headers = new HashMap();
                headers.put("Accept", "*/*");
                headers.put("Content-Type", "application/text;charset=UTF-8");

                Request request = new Request(Method.DELETE, httpSchema + ArtemisConfig.host, (String)path.get(httpSchema), ArtemisConfig.appKey, ArtemisConfig.appSecret, 100);
                request.setHeaders(headers);
                request.setSignHeaderPrefixList(CUSTOM_HEADERS_TO_SIGN_PREFIX);
                request.setQuerys(querys);
                Response response = Client.execute(request);
                responseStr = getResponseResult(response);
            } catch (Exception var9) {
                System.out.println("the Artemis DELETE Request is failed[doDeleteArtemis]"+ var9);
            }

            return responseStr;
        } else {
            throw new RuntimeException("http鍜宧ttps鍙傛暟閿欒\ue1e4httpSchema: " + httpSchema);
        }
    }

    private static String getResponseResult(Response response) {
        String responseStr = null;
        int statusCode = response.getStatusCode();
        if (!String.valueOf(statusCode).startsWith("2") && !String.valueOf(statusCode).startsWith("3")) {
            String msg = response.getErrorMessage();
            responseStr = response.getBody();
            System.out.println("the Artemis Request is Failed,statusCode:" + statusCode + " errorMsg:" + msg);
        } else {
            responseStr = response.getBody();
            System.out.println("the Artemis Request is Success,statusCode:" + statusCode + " SuccessMsg:" + response.getBody());
        }

        return responseStr;
    }
}
