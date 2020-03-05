package com.xiandao.android.http;


import android.util.Base64;

import com.xiandao.android.utils.Base64Helper;
import com.xiandao.android.utils.HmacSHA256Helper;
import com.xiandao.android.utils.MD5Helper;

import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NetParams extends RequestParams {
    private String nonce;
    private String timestamp;
    public NetParams(String uri,Map<String, Object> map) {
        super(uri);
        nonce=UUID.randomUUID().toString();
        timestamp=new Date().getTime()+"";
        addHeader("Accept","*/*");
        addHeader(HTTP.CONTENT_TYPE,"application/json");
        addHeader("x-ca-timestamp",timestamp);
        addHeader("x-ca-nonce",nonce);
        addHeader("x-ca-Key","26670594");
        addHeader("x-ca-signature",getXCaSignature("POST",map,uri));
        addHeader("x-ca-Signature-Headers","x-ca-Key,x-ca-nonce,x-ca-timestamp");
        if (null != map) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                addParameter(entry.getKey(), entry.getValue());
            }
        }
    }

    private String getXCaSignature(String httpMethod,Map<String, Object> map,String url){
        StringBuilder sb=new StringBuilder();
        sb.append(httpMethod.toUpperCase()).append("\n");
        HashMap<String, String> headers=getHeaders();
        sb.append(headers.get("Accept")).append("\n");
        sb.append(headers.get("Content-Type")).append("\n");
        sb.append(headers.get("x-ca-Key")).append("\n");
        sb.append(headers.get("x-ca-nonce")).append("\n");
        sb.append(headers.get("x-ca-timestamp")).append("\n");
        sb.append("/artemis/api/eventService/v1/eventSubscriptionView");
        System.out.println(sb);
        return HmacSHA256Helper.sha256_HMAC(sb.toString(),"GAeDdt5GpIfLGNPLR5wp");
    }
}
