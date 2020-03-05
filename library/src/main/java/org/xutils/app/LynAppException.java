package org.xutils.app;

import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * 类名：LynAppException
 * 类描述：UncaughtException处理类,当程序发生Uncaught异常的时候,由该类来接管程序,并记录发送错误报告 作者：CJ
 * 创建时间：2014-10-29-上午10:44:18 修改记录： 修改人　　 修改时间　　 版本 描述
 * ----------------------------------------------------------
 * 
 * 
 * ---------------------------------------------------------- Copyright
 * (c)-2014烈焰鸟网络科技有限公司
 */

public class LynAppException implements UncaughtExceptionHandler {
	public static final String TAG = "CrashHandler";
	private static LynAppException instance;
	private Context mContext;// 程序的Context对象
	private UncaughtExceptionHandler mDefaultHandler;// 系统默认的UncaughtException处理类
	private Map<String, String> info = new HashMap<String, String>();// 用来存储设备信息和异常信息
	private SimpleDateFormat format = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");// 用于格式化日期,作为日志文件名的一部分

	private LynAppException(Context context) {
		init(context);
	}

	/**
	 * 方法说明：初始化UncaughtException处理类 方法名称：init
	 * 
	 * @param context
	 *            返回值：void
	 */
	public void init(Context context) {
		mContext = context;
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);// 设置该CrashHandler为程序的默认处理器
	}

	/**
	 * 方法说明：LynAppException单列 方法名称：getInstance
	 * 
	 * @param context
	 * @return 返回值：LynAppException
	 */
	public static LynAppException getInstance(Context context) {
		if (instance == null) {
			instance = new LynAppException(context);
		}
		return instance;
	}

	/*
	 * 重写方法名： uncaughtException 父类： @see
	 * java.lang.Thread.UncaughtExceptionHandler
	 * #uncaughtException(java.lang.Thread, java.lang.Throwable) 方法说明：异常回调处理
	 * 
	 * @param thread
	 * 
	 * @param ex Copyright (c)-2014烈焰鸟网络科技有限公司
	 */
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		Log.e("uncaughtException", ex.getMessage());
		if (!handleException(ex) && mDefaultHandler != null) {
			// 如果自定义的没有处理则让系统默认的异常处理器来处理
			mDefaultHandler.uncaughtException(thread, ex);
		} else {
			sendMessageTips(ex);
		}
	}

	/**
	 * 错误报告提示框
	 */
	public void sendMessageTips(final Throwable ex) {
		// if (ex instanceof NullPointerException) {
		// } else if (ex instanceof BaseException) {
		// } else if (ex instanceof DbException) {
		// } else if (ex instanceof HttpException) {
		// } else if (ex instanceof Exception) {
		// } else if (ex instanceof RuntimeException) {
		// } else if (ex instanceof ClassNotFoundException) {
		// } else if (ex instanceof IOException) {
		// } else if (ex instanceof CloneNotSupportedException) {
		// } else if (ex instanceof Error) {
		// new Thread(new Runnable() {
		//
		// @Override
		// public void run() {
		// Looper.prepare();
		// Toast.makeText(mContext,
		// "很抱歉,程序出现异常,即将退出."+ex.getMessage(),
		// Toast.LENGTH_LONG).show();
		// Looper.loop();
		// }
		// }).start();
		// }
		new Thread(new Runnable() {

			@Override
			public void run() {
				Looper.prepare();
				Toast.makeText(mContext, "很抱歉,程序出现异常,异常信息：" + ex.getMessage(),
						Toast.LENGTH_LONG).show();
				Looper.loop();
			}
		}).start();
//		ex.printStackTrace();
//		collectDeviceInfo(mContext);
//		saveCrashInfo2File(ex);
		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(0);
	}

	/**
	 * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
	 * 
	 * @param ex
	 *            异常信息
	 * @return true:如果处理了该异常信息;否则返回false.
	 */
	public boolean handleException(Throwable ex) {
		if (ex == null)
			return false;
		Log.e("handleException", ex.getMessage());
		return true;
	}

//	/**
//	 * 方法说明：收集设备参数信息 方法名称：collectDeviceInfo
//	 *
//	 * @param context
//	 *            返回值：void
//	 */
//	public void collectDeviceInfo(Context context) {
//		try {
//			PackageManager pm = context.getPackageManager();// 获得包管理器
//			PackageInfo pi = pm.getPackageInfo(context.getPackageName(),
//					PackageManager.GET_ACTIVITIES);// 得到该应用的信息，即主Activity
//			if (pi != null) {
//				String versionName = pi.versionName == null ? "null"
//						: pi.versionName;
//				String versionCode = pi.versionCode + "";
//				info.put("versionName", versionName);
//				info.put("versionCode", versionCode);
//			}
//		} catch (NameNotFoundException e) {
//			e.printStackTrace();
//		}
//
//		Field[] fields = Build.class.getDeclaredFields();// 反射机制
//		for (Field field : fields) {
//			try {
//				field.setAccessible(true);
//				info.put(field.getName(), field.get("").toString());
//				Log.d(TAG, field.getName() + ":" + field.get(""));
//			} catch (IllegalArgumentException e) {
//				e.printStackTrace();
//			} catch (IllegalAccessException e) {
//				e.printStackTrace();
//			}
//		}
//	}
//
//	/**
//	 * 方法说明：记录错误报告 方法名称：saveCrashInfo2File
//	 *
//	 * @param ex
//	 * @return 返回值：String
//	 */
//	private String saveCrashInfo2File(Throwable ex) {
//		StringBuffer sb = new StringBuffer();
//		for (Map.Entry<String, String> entry : info.entrySet()) {
//			String key = entry.getKey();
//			String value = entry.getValue();
//			sb.append(key + "=" + value + "\r\n");
//		}
//		Writer writer = new StringWriter();
//		PrintWriter pw = new PrintWriter(writer);
//		ex.printStackTrace(pw);
//		Throwable cause = ex.getCause();
//		// 循环着把所有的异常信息写入writer中
//		while (cause != null) {
//			cause.printStackTrace(pw);
//			cause = cause.getCause();
//		}
//		pw.close();// 记得关闭
//		String result = writer.toString();
//		sb.append(result);
//		// 保存文件
//		long timetamp = System.currentTimeMillis();
//		String time = format.format(new Date());
//		String fileName = "lynCrash-" + time + "-" + timetamp + ".log";
//		if (Environment.getExternalStorageState().equals(
//				Environment.MEDIA_MOUNTED)) {
//			try {
//				File dir = new File(Utils.getFilePath() + "/lynCrash/");
//				if (!dir.exists())
//					dir.mkdir();
//				FileOutputStream fos = new FileOutputStream(dir + fileName);
//				fos.write(sb.toString().getBytes());
//				fos.close();
//				return fileName;
//			} catch (FileNotFoundException e) {
//				e.printStackTrace();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//		return null;
//	}
}
