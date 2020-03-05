package com.xiandao.android;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.multidex.MultiDex;
import android.util.DisplayMetrics;
import android.util.Log;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.pgyersdk.crash.PgyCrashManager;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.tencent.smtt.sdk.QbSdk;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;
import com.xiandao.android.net.ThreadPoolManager;

import org.xutils.app.LynActivityManager;
import org.xutils.x;

import java.io.File;

import androidx.annotation.NonNull;
import cn.jpush.android.api.JPushInterface;

public class AndroidApplication extends Application {

    public static AndroidApplication getInstance = null;
    private Context mContext;
    public ThreadPoolManager threadPoolManager = null;
    public File cacheDir;

    public static int H;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        getInstance = this;
        mContext = getApplicationContext();
        threadPoolManager = ThreadPoolManager.getInstance();
//        initApplication(false);
//        cacheDir = StorageUtils.getOwnCacheDirectory(getApplicationContext(), "cfl/Cache");
        initImageLoader();
//        LocalImageHelper.init(this);
//        handler.postDelayed(runnable, TIME); //每隔1s执行 开始执行定时器
//        handler.removeCallbacks(runnable); //
//        isTimeRefrsh = false;
        JPushInterface.setDebugMode(true);    // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);            // 初始化 JPush
        PgyCrashManager.register(getApplicationContext());
//        }
        Fresco.initialize(this);
        H = getScreenH(this);

        x.Ext.init(this);
        x.Ext.setDebug(true); // 是否输出debug日志

        UMConfigure.init(this,"5cb6d69f0cafb29742001079","umeng",UMConfigure.DEVICE_TYPE_PHONE,"");
    }

    {
//        PlatformConfig.setWeixin("wxd52bfe3bee133c41", "8293160e1559234c9a21ec2aaf22f67e");
        PlatformConfig.setWeixin("wx9e26096ae63f011d", "22ba1246fa64314bb6cc97a7c10ac25c");
        PlatformConfig.setQQZone("101569547", "00261965102559b4d8732e9a747c771a");
    }


    static {//static 代码段可以防止内存泄露
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @NonNull
            @Override
            public RefreshHeader createRefreshHeader(@NonNull Context context, @NonNull RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.main_background, android.R.color.white);//全局设置主题颜色
                return new ClassicsHeader(context).setSpinnerStyle(SpinnerStyle.Translate);//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @NonNull
            @Override
            public RefreshFooter createRefreshFooter(@NonNull Context context, @NonNull RefreshLayout layout) {
                return new ClassicsFooter(context).setSpinnerStyle(SpinnerStyle.Translate);
            }

        });
    }
    /**
     * 获取屏幕高度
     */
    public int getScreenH(Context aty) {
        DisplayMetrics dm = aty.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    public static AndroidApplication getInstance() {
        return getInstance;
    }

    public static Context getAppContext() {
        return AndroidApplication.getInstance.mContext;
    }

    /**
     * @方法说明:完成ImageLoaderConfiguration的配置
     * @方法名称:initImageLoader
     * @返回值:void
     */
    public void initImageLoader() {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(
                this);
        config.memoryCacheExtraOptions(480, 800);// max width, max
        // height，即保存的每个缓存文件的最大长宽
        config.threadPoolSize(3);// 线程池内加载的数量
        config.threadPriority(Thread.NORM_PRIORITY - 1);
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.denyCacheImageMultipleSizesInMemory();
        config.memoryCache(new WeakMemoryCache());
        // config.memoryCache(new UsingFreqLimitemoryCache(2 * 1024 *
        // 1024));// 你可以通过自己的内存缓存实现
        config.memoryCacheSize(24 * 1024 * 1024);// 内存缓存的最大值
        config.memoryCacheSizePercentage(13);
//        config.diskCache(new UnlimitedDiskCache(cacheDir));// 自定义缓存路径
        config.diskCacheSize(50 * 1024 * 1024);// 50 Mb sd卡(本地)缓存的最大值
        config.diskCacheFileCount(1000);// sd卡缓存文件最大数量
//        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());// 将保存的时候的URI名称用MD5
        // 加密
        config.imageDownloader(new BaseImageDownloader(mContext, 5 * 1000,
                15 * 1000));// connectTimeout (5 s), readTimeout (15 s)超时时间
        config.imageDecoder(new BaseImageDecoder(true));
        config.defaultDisplayImageOptions(DisplayImageOptions.createSimple());
        // config.writeDebugLogs();// Remove for release app
        ImageLoader.getInstance().init(config.build());// 全局初始化配置
    }

    /**
     * @return
     * @方法说明:设置相册图片Options参数配置
     * @方法名称:getPhotoOption
     * @返回值:DisplayImageOptions
     */
    public static DisplayImageOptions getPhotoOption() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.tongyong_pic_mr_xiao) // 设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.tongyong_pic_mr_xiao) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.tongyong_pic_mr_xiao) // 设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
                // .considerExifParams(false) // 是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.EXACTLY) // 设置图片以如何的编码方式显示
                // EXACTLY
                // :图像将完全按比例缩小的目标大小
                // EXACTLY_STRETCHED:图片会缩放到目标大小完全
                // IN_SAMPLE_INT:图像将被二次采样的整数倍
                // IN_SAMPLE_POWER_OF_2:图片将降低2倍，直到下一减少步骤，使图像更小的目标大小
                // NONE:图片不会调整
                .bitmapConfig(Bitmap.Config.RGB_565) // 设置图片的解码类型
                .resetViewBeforeLoading(false) // 设置图片下载前是否重置 复位
//                .displayer(new FadeInBitmapDisplayer(300))//是否图片加载好后渐入的动画时间
                // .displayer(new FadeInBitmapDisplayer(300))
                // .displayer(new RoundedBitmapDisplayer(roundPixels))
                // 是否设置为圆角，弧度为多少
                // .displayer(new FadeInBitmapDisplayer(100)) // 是否图片加载好后渐入的动画时间
                .build(); // 创建配置过得DisplayImageOption对象
        return options;
    }

    /**
     * @return
     * @方法说明:设置相册图片Options参数配置
     * @方法名称:getPhotoOption
     * @返回值:DisplayImageOptions
     */
    public static DisplayImageOptions getDefautOption() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.tongyong_pic_mr_xiao) // 设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.tongyong_pic_mr_xiao) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.tongyong_pic_mr_xiao) // 设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
                // .considerExifParams(false) // 是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.EXACTLY) // 设置图片以如何的编码方式显示
                // EXACTLY
                // :图像将完全按比例缩小的目标大小
                // EXACTLY_STRETCHED:图片会缩放到目标大小完全
                // IN_SAMPLE_INT:图像将被二次采样的整数倍
                // IN_SAMPLE_POWER_OF_2:图片将降低2倍，直到下一减少步骤，使图像更小的目标大小
                // NONE:图片不会调整
                .bitmapConfig(Bitmap.Config.RGB_565) // 设置图片的解码类型
                .resetViewBeforeLoading(false) // 设置图片下载前是否重置 复位
//                .displayer(new FadeInBitmapDisplayer(500))//是否图片加载好后渐入的动画时间
                // .displayer(new FadeInBitmapDisplayer(300))
                // .displayer(new RoundedBitmapDisplayer(roundPixels))
                // 是否设置为圆角，弧度为多少
                // .displayer(new FadeInBitmapDisplayer(100)) // 是否图片加载好后渐入的动画时间
                .build(); // 创建配置过得DisplayImageOption对象
        return options;
    }

    /**
     * @return
     * @方法说明:设置相册图片Options参数配置
     * @方法名称:getPhotoOption
     * @返回值:DisplayImageOptions
     */
    public static DisplayImageOptions getDefautOption(boolean isLoadAnim) {
        if (isLoadAnim) {
            return getDefautOption();
        }
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.tongyong_pic_mr_xiao) // 设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.tongyong_pic_mr_xiao) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.tongyong_pic_mr_xiao) // 设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
                // .considerExifParams(false) // 是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.EXACTLY) // 设置图片以如何的编码方式显示
                // EXACTLY
                // :图像将完全按比例缩小的目标大小
                // EXACTLY_STRETCHED:图片会缩放到目标大小完全
                // IN_SAMPLE_INT:图像将被二次采样的整数倍
                // IN_SAMPLE_POWER_OF_2:图片将降低2倍，直到下一减少步骤，使图像更小的目标大小
                // NONE:图片不会调整
                .bitmapConfig(Bitmap.Config.RGB_565) // 设置图片的解码类型
                .resetViewBeforeLoading(false) // 设置图片下载前是否重置 复位
//                .displayer(new FadeInBitmapDisplayer(500))//是否图片加载好后渐入的动画时间
                // .displayer(new FadeInBitmapDisplayer(300))
                // .displayer(new RoundedBitmapDisplayer(roundPixels))
                // 是否设置为圆角，弧度为多少
                // .displayer(new FadeInBitmapDisplayer(100)) // 是否图片加载好后渐入的动画时间
                .build(); // 创建配置过得DisplayImageOption对象
        return options;
    }

    /**
     * @return
     * @方法说明:设置相册图片Options参数配置 sizeType:1、大头像；2、小头像
     * @方法名称:getPhotoOption
     * @返回值:DisplayImageOptions
     */
    public static DisplayImageOptions getPhotoRoundOption(int roundPixels, int sizeType) {
        int imageMipmap = R.mipmap.moren_tx_hui;
        if (sizeType == 2)
            imageMipmap = R.mipmap.moren_tx_xiao;

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(imageMipmap) // 设置图片在下载期间显示的图片
                .showImageForEmptyUri(imageMipmap) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(imageMipmap) // 设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
                // .considerExifParams(false) // 是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.EXACTLY) // 设置图片以如何的编码方式显示
                // EXACTLY
                // :图像将完全按比例缩小的目标大小
                // EXACTLY_STRETCHED:图片会缩放到目标大小完全
                // IN_SAMPLE_INT:图像将被二次采样的整数倍
                // IN_SAMPLE_POWER_OF_2:图片将降低2倍，直到下一减少步骤，使图像更小的目标大小
                // NONE:图片不会调整
                .bitmapConfig(Bitmap.Config.RGB_565) // 设置图片的解码类型
                .resetViewBeforeLoading(false) // 设置图片下载前是否重置 复位
//                    .displayer(new FadeInBitmapDisplayer(300))//是否图片加载好后渐入的动画时间
                .displayer(new RoundedBitmapDisplayer(roundPixels))
                // 是否设置为圆角，弧度为多少
                // .displayer(new FadeInBitmapDisplayer(100)) // 是否图片加载好后渐入的动画时间
                .build(); // 创建配置过得DisplayImageOption对象
        return options;
    }

    /**
     * @return
     * @方法说明:设置相册图片Options参数配置
     * @方法名称:getPhotoOption
     * @返回值:DisplayImageOptions
     */
    public static DisplayImageOptions getPhotoRoundOption(int roundPixels) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.moren_tx_hui) // 设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.moren_tx_hui) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.moren_tx_hui) // 设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
                // .considerExifParams(false) // 是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.EXACTLY) // 设置图片以如何的编码方式显示
                // EXACTLY
                // :图像将完全按比例缩小的目标大小
                // EXACTLY_STRETCHED:图片会缩放到目标大小完全
                // IN_SAMPLE_INT:图像将被二次采样的整数倍
                // IN_SAMPLE_POWER_OF_2:图片将降低2倍，直到下一减少步骤，使图像更小的目标大小
                // NONE:图片不会调整
                .bitmapConfig(Bitmap.Config.RGB_565) // 设置图片的解码类型
                .resetViewBeforeLoading(false) // 设置图片下载前是否重置 复位
//                    .displayer(new FadeInBitmapDisplayer(300))//是否图片加载好后渐入的动画时间
                .displayer(new RoundedBitmapDisplayer(roundPixels))
                // 是否设置为圆角，弧度为多少
                // .displayer(new FadeInBitmapDisplayer(100)) // 是否图片加载好后渐入的动画时间
                .build(); // 创建配置过得DisplayImageOption对象
        return options;
    }

//    Handler handler = new Handler();
//    Runnable runnable = new Runnable() {
//        @Override
//        public void run() {
//            // handler自带方法实现定时器
//            try {
//                handler.postDelayed(this, TIME);
////                updateToken();
//                ApiHttpResult.serverTimeResult(AndroidApplication.getAppContext(), null, null, new HttpUtils.DataCallBack() {
//                    @Override
//                    public void callBack(Object o) {
//                        if (o != null) {
//                            FileManagement.saveServerTime(Long.parseLong((String) o));
//                        }
//                    }
//                });
//                isTimeRefrsh = false;
//            } catch (Exception e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
//    };

    /**
     * 完全退出APP
     */
    public void exitApp() {
        // 杀死进程,推出整个应用
        threadPoolManager.stopAllTask();
        LynActivityManager.getScreenManager().removeAllActivity();
        int sdk = Integer.parseInt(android.os.Build.VERSION.SDK);
        if (sdk <= 10) {
            System.exit(0);
        } else {
            android.os.Process.killProcess(android.os.Process.myPid());
        }
//        if (handler != null)
//            handler.removeCallbacks(runnable);
    }

    public String getCachePath() {
        File cacheDir;
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            cacheDir = getExternalCacheDir();
        } else {
            cacheDir = getCacheDir();
        }
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
        return cacheDir.getAbsolutePath();
    }

    /**
     * 获得当前手机系统版本
     *
     * @return
     */
    public static int getPhoneSdk() {
        return Integer.parseInt(android.os.Build.VERSION.SDK);
    }

}
