package org.xutils.app;

import android.app.Application;

import org.xutils.NetUtil;
import org.xutils.netbroadcast.NetworkStateReceiver;
import org.xutils.netbroadcast.WKXNetChangeObserver;


/**
 * 类名：LynApplication
 * 类描述：全局application
 * 作者：CJ
 * 创建时间：2014-11-5-下午4:05:25
 * 修改记录：
 * 修改人　　		修改时间　　		版本		描述
 *----------------------------------------------------------
 *
 *
 *----------------------------------------------------------	
 * Copyright (c)-2014烈焰鸟网络科技有限公司
 */
 
public abstract class LynApplication extends Application{
	private WKXNetChangeObserver taNetChangeObserver;

	@Override
	public void onCreate() {
		super.onCreate();
		//获得上下文
		taNetChangeObserver = new WKXNetChangeObserver()
		{
			@Override
			public void onConnect(NetUtil.netType type)
			{
				super.onConnect(type);
				LynApplication.this.onConnect(type);

			}

			@Override
			public void onDisConnect()
			{
				super.onDisConnect();
				LynApplication.this.onDisConnect();

			}
		};
		NetworkStateReceiver.registerObserver(taNetChangeObserver);
	}

	
	 /**
	 * 方法说明：初始化Application
	 * 方法名称：initApplication
	 * 返回值：void
	*/
	public void initApplication(boolean isStart) {
		if(isStart){
		LynAppException.getInstance(getApplicationContext()).init(
				getApplicationContext());}
	}
	
	
	 /**
	 * 方法说明：有网络
	 * 方法名称：onConnect
	 * @param type
	 * 返回值：void
	*/
	protected void onConnect(NetUtil.netType type){
    }
	 /**
	 * 方法说明：无网络
	 * 方法名称：onDisConnect
	 * 返回值：void
	*/
	protected void onDisConnect(){}
}
