
package org.xutils.http.oldhttp;

import android.content.Context;
import android.os.Message;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HttpContext;

 

 /**
 * 类名：LynSyncHttpClient
 * 类描述：LynSyncHttpClient是网络请求的一个类,可以支持异步,也可以不开启异步来处理程序
 * 作者：CJ
 * 创建时间：2014-10-24-下午3:06:16
 * 修改记录：
 * 修改人　　		修改时间　　		版本		描述
 *----------------------------------------------------------
 *
 *
 *----------------------------------------------------------	
 * Copyright (c)-2014烈焰鸟网络科技有限公司
 */
 
public class LynSyncHttpClient extends AsyncHttpClient
{
	private int responseCode;
	protected String result;
	protected AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler()
	{

		@Override
		protected void sendResponseMessage(org.apache.http.HttpResponse response)
		{
			responseCode = response.getStatusLine().getStatusCode();
			super.sendResponseMessage(response);
		}

		@Override
		protected void sendMessage(Message msg)
		{
			handleMessage(msg);
		}

		@Override
		public void onSuccess(String content)
		{
			result = content;
		}

		@Override
		public void onFailure(Throwable error, String content)
		{
			result = onRequestFailed(error, content);
		}
	};

	public int getResponseCode()
	{
		return responseCode;
	}

	@Override
	protected void sendRequest(DefaultHttpClient client,
			HttpContext httpContext, HttpUriRequest uriRequest,
			String contentType, AsyncHttpResponseHandler responseHandler,
			Context context)
	{
		if (contentType != null)
		{
			uriRequest.addHeader("Content-Type", contentType);
		}
		new AsyncHttpRequest(client, httpContext, uriRequest, responseHandler)
				.run();
	}

	public String onRequestFailed(Throwable error, String content)
	{
		return "";
	}

	public void delete(String url, RequestParams queryParams,
			AsyncHttpResponseHandler responseHandler)
	{
		delete(url, responseHandler);
	}

	public String get(String url, RequestParams params)
	{
		this.get(url, params, responseHandler);
		return result;
	}

	public String get(String url)
	{
		this.get(url, null, responseHandler);
		return result;
	}

	public String put(String url, RequestParams params)
	{
		this.put(url, params, responseHandler);
		return result;
	}

	public String put(String url)
	{
		this.put(url, null, responseHandler);
		return result;
	}

	public String post(String url, RequestParams params)
	{
		this.post(url, params, responseHandler);
		return result;
	}

	public String post(String url)
	{
		this.post(url, null, responseHandler);
		return result;
	}

	public String delete(String url, RequestParams params)
	{
		this.delete(url, params, responseHandler);
		return result;
	}

	public String delete(String url)
	{
		this.delete(url, null, responseHandler);
		return result;
	}

}
