package com.xiandao.android.http;

import com.google.gson.Gson;

import org.xutils.http.UriRequest;
import org.xutils.http.app.ResponseParser;


/**
 * @author 曾晓龙
 */
public class JsonResponseParser implements ResponseParser {
	//检查服务器返回的响应头信息
	@Override
	public void checkResponse(UriRequest request) throws Throwable {
	}

	@Override
	public Object parse(Class<?> resultType, String result) throws Throwable {
		return new Gson().fromJson(result, resultType);
	}

}
