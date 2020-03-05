package com.xiandao.android.http.hikhttp;



import org.apache.commons.codec.binary.Base64;
import org.xutils.http.RequestParams;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import static com.xiandao.android.http.hikhttp.HikConfig.APPKEY;
import static com.xiandao.android.http.hikhttp.HikConfig.APPSECRET;
import static com.xiandao.android.http.hikhttp.HikConfig.HOST;

public class HikParams extends RequestParams {
    private String api;
    public HikParams(String api){
        super(HOST+api);
        this.api=api;
        addHeader("Accept",utf8ToIso88591("*/*"));
        addHeader("Content-Type",utf8ToIso88591("application/json"));
        addHeader("x-ca-timestamp",utf8ToIso88591(String.valueOf((new Date()).getTime())));
        addHeader("x-ca-nonce",utf8ToIso88591(UUID.randomUUID().toString()));
        addHeader("x-ca-key",utf8ToIso88591(APPKEY));
        addHeader("x-ca-signature",utf8ToIso88591(getXCaSignature("POST")));
        addHeader("x-ca-signature-headers",utf8ToIso88591("x-ca-key,x-ca-nonce,x-ca-timestamp"));
        Map<String,String> map=getHeaders();
        for (String key : map.keySet()) {
            System.out.println(key + " ：" + map.get(key));
        }
    }
    private String getXCaSignature(String httpMethod) {
        String signature;
        StringBuilder sb=new StringBuilder();
        sb.append(httpMethod.toUpperCase()).append("\n");
        HashMap<String, String> headers=getHeaders();
        sb.append(headers.get("Accept")).append("\n");
        sb.append(headers.get("Content-Type")).append("\n");
        sb.append("x-ca-key:"+headers.get("x-ca-key")).append("\n");
        sb.append("x-ca-nonce:"+headers.get("x-ca-nonce")).append("\n");
        sb.append("x-ca-timestamp:"+headers.get("x-ca-timestamp")).append("\n");
        sb.append(api);
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

    private String utf8ToIso88591(String str) {
        if (str == null) {
            return str;
        } else {
            try {
                return new String(str.getBytes("UTF-8"), "ISO-8859-1");
            } catch (UnsupportedEncodingException var2) {
                throw new RuntimeException(var2.getMessage(), var2);
            }
        }
    }
}
