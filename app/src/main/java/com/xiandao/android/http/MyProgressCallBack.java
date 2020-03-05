package com.xiandao.android.http;

import org.xutils.common.Callback;

/**
 * 
 * 文件下载回调
 * 
 * @author 曾晓龙
 *
 * @param <ResultType>
 */
public class MyProgressCallBack<ResultType> implements Callback.ProgressCallback<ResultType>{

	@Override
	public void onSuccess(ResultType result) {
	}

	@Override
	public void onError(Throwable ex, boolean isOnCallback) {
	}

	@Override
	public void onCancelled(CancelledException cex) {
		
	}

	@Override
	public void onFinished() {
		
	}

	@Override
	public void onWaiting() {
		
	}

	@Override
	public void onStarted() {
		
	}

	@Override
	public void onLoading(long total, long current, boolean isDownloading) {
		
	}

}
