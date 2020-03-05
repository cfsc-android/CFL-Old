package com.xiandao.android.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.StatFs;
import android.provider.MediaStore;
import android.text.ClipboardManager;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.xutils.image.ImageDecoder.calculateInSampleSize;

/**
 * @类名:Utils
 * @类描
 * @作 Administrator
 * @创建时间:2014 上 1:11:42
 * @修改
 * @修改时间:
 * @修改备注:
 * @版本:
 */
public class Utils {

    /**
     * @param loginId
     * @param loginPassword
     * @param timeStamp
     * @return
     * @方法说明:
     * @方法名称:makeAuthenticator
     * @返回 byte[]
     */
    public static byte[] makeAuthenticator(String loginId,
                                           String loginPassword, int timeStamp) {
        java.security.MessageDigest digest = null;
        byte[] result = null;
        StringBuffer sb = new StringBuffer(64);
        String tmp = String.valueOf(timeStamp);
        String timeStampStr = tmp.length() == 9 ? "0" + tmp : tmp;
        sb.append(loginId).append("\0\0\0\0\0\0\0\0\0").append(loginPassword)
                .append(timeStampStr);
        try {
            digest = java.security.MessageDigest.getInstance("MD5");
            digest.reset();
            digest.update(sb.toString().getBytes());
            result = digest.digest();
            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * @param mobileNums
     * @return
     * @方法说明:Test whether is Mobile phone number
     * @方法名称:isMobileNO
     * @返回 boolean
     */
    public static boolean isMobileNO(String mobileNums) {
        /**
         * 判断字符串是否符合手机号码格式
         * 移动号段: 134,135,136,137,138,139,147,150,151,152,157,158,159,170,178,182,183,184,187,188
         * 联通号段: 130,131,132,145,155,156,170,171,175,176,185,186
         * 电信号段: 133,149,153,170,173,177,180,181,189
         * @param str
         * @return 待检测的字符串
         */
        String telRegex = "^((13[0-9])|(14[5,7,9])|(15[^4])|(18[0-9])|(17[0,1,3,5,6,7,8]))\\d{8}$";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobileNums))
            return false;
        else
            return mobileNums.matches(telRegex);
    }

    private static final String STATUS_BAR_HEIGHT_RES_NAME = "status_bar_height";
    /**
     * 计算状态栏高度高度 getStatusBarHeight
     *
     * @return
     */
    public static int getStatusBarHeight() {
        return getInternalDimensionSize(Resources.getSystem(),
                STATUS_BAR_HEIGHT_RES_NAME);
    }

    private static int getInternalDimensionSize(Resources res, String key) {
        int result = 0;
        int resourceId = res.getIdentifier(key, "dimen", "android");
        if (resourceId > 0) {
            result = res.getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * @param str
     * @return
     * @方法说明:验证字符串为 * @方法名称:isNotEmpty
     * @返回 Boolean
     */
    public static Boolean isNotEmpty(String str) {
        if (null != str && !"".equals(str.trim())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param path
     * @return
     * @方法说明:read the file
     * @方法名称:readFile
     * @返回 byte[]
     */
    public static byte[] readFile(String path) {
        FileInputStream fis = null;
        File file = new File(path);
        try {
            fis = new FileInputStream(file);
            byte[] bb = new byte[fis.available()];
            fis.read(bb);
            return bb;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param data
     * @param fileName
     * @方法说明:存文 * @方法名称:writeFile
     * @返回 void
     */
    public static void writeFile(byte[] data, String fileName) {

        try {

            FileOutputStream output = new FileOutputStream(fileName);
            output.write(data);
            output.close();

        } catch (FileNotFoundException e) {

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    /**
     * @return
     * @方法说明:Test whether the SD card presence
     * @方法名称:hasSdcard
     * @返回 boolean
     */
    public static boolean hasSdcard() {
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @return
     * @方法说明:get the file's path
     * @方法名称:getFilePath
     * @返回 String
     */
    public static String getFilePath() {
        if (hasSdcard()) {
            return sdPath;

        } else {
            return dataPath;
        }
    }

    /* To create file's path */
    public final static String sdPath = Environment
            .getExternalStorageDirectory().toString();
    public final static String dataPath = "/data/data/com.zhangchao.xiandao/";
    public final static String projectResoucePath = "/xiandao/";
    public final static String projectImageResoucePath = "/xiandao/";

    /**
     * @param Path
     * @param filename
     * @return
     * @方法说明:To create the file by the path and filename
     * @方法名称:creatFilePath
     * @返回 File
     */
    public static File creatFilePath(String Path, String filename) {

        File path = new File(Path);
        if (!path.exists()) {
            path.mkdirs();
        }
        File file = new File(path.getAbsolutePath() + File.separator + filename);
        if (file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    /**
     * @param Path
     * @方法说明:To create file folder
     * @方法名称:creatFilePath
     * @返回 void
     */
    public static void creatFilePath(String Path) {

        File path = new File(Path);
        if (!path.exists()) {
            path.mkdirs();
        }
    }

    /**
     * @param path
     * @return
     * @方法说明:创建文件及目 * @方法名称:creatFile
     * @返回 File
     */
    public static File creatFile(String path) {
        File file = new File(path);
        try {
            int lastIndex = path.lastIndexOf(File.separator);
            if (lastIndex != -1) {
                String md = path.substring(0, lastIndex);
                File mdFile = new File(md);
                if (!mdFile.exists()) {
                    mdFile.mkdirs();
                }
            }
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * @param con
     * @param resouce_Id
     * @return
     * @方法说明:加载布局文件
     * @方法名称:LoadXmlView
     * @返回 View
     */
    public static View LoadXmlView(Context con, int resouce_Id) {
        LayoutInflater flat = LayoutInflater.from(con);
        View view = flat.inflate(resouce_Id, null);
        return view;
    }

    /**
     * @param view
     * @方法说明:remove all child's view
     * @方法名称:removeAllView
     * @返回 void
     */
    public static void removeAllView(LinearLayout view) {
        int count = view.getChildCount();
        for (int i = 0; i < count; i++) {
            view.removeViewAt(0);
        }
    }

    /**
     * @param srcPath
     * @return
     * @方法说明:Get a picture of bytes after compression flow
     * @方法名称:getImage
     * @返回 byte[]
     */
    public static Bitmap getImage(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空
        // if(newOpts.outWidth<=0||newOpts.outHeight<=0){
        //
        // return new byte[]{0};
        // }
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置
        float hh = 800f;// 这里设置高度00f
        float ww = 480f;// 这里设置宽度80f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即
        int be = 1;// be=1表示不缩
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        Bitmap createbt = matrixBitmap(bitmap, srcPath);
        return createbt;// 压缩好比例大小后再进行质量压
    }

    /**
     * @param image
     * @return
     * @方法说明:图片质量压缩
     * @方法名称:compressImage
     * @返回 byte[]
     */
    public static byte[] compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(CompressFormat.JPEG, 100, baos);// 质量压缩方法，这00表示不压缩，把压缩后的数据存放到baos
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大00kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            image.compress(CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos
            options -= 10;// 每次都减0
            if (options == 0) {
                break;
            }
        }
        byte[] b = baos.toByteArray();
        try {
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return b;
    }

    /**
     * @param image
     * @param file
     * @方法说明:再度压缩，图片大 0kb, 就继续压  * @方法名称:uploadFileComp
     * @返回 void
     */
    public static void uploadFileComp(Bitmap image, File file) {
        if (image == null) {
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            image.compress(CompressFormat.JPEG, 100, stream);
//			int options = 90;
//			while (stream.toByteArray().length / 1024 > 200) { // 循环判断如果压缩后图片是否大00kb,大于继续压缩
//				stream.reset();// 重置baos即清空baos
//				image.compress(CompressFormat.JPEG, options, stream);// 这里压缩options%，把压缩后的数据存放到baos
//																			// options
//																			// -=
//																			// 10;//
//																			// 每次都减0
//				if (options == 0) {
//					break;
//				}
//			}
            byte[] bytes = stream.toByteArray();
            fos.write(bytes);
//			ByteArrayInputStream isBm = new ByteArrayInputStream(bytes);// 把压缩后的数据baos存放到ByteArrayInputStream
//																		// image
//																		// =
//																		// BitmapFactory.decodeStream(isBm,
//																		// null,
//																		// null);//
//																		// 把ByteArrayInputStream数据生成图片
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Bitmap uploadCompressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(CompressFormat.JPEG, 100, baos);// 质量压缩方法，这00表示不压缩，把压缩后的数据存放到baos
        int options = 90;
        while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大00kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            image.compress(CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos
            options -= 10;// 每次都减0
            if (options == 0) {
                break;
            }
        }
        byte[] b = baos.toByteArray();
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        try {
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * @return
     * @方法说明:获取sd卡剩余容 * @方法名称:getSDFreeSize
     * @返回 long
     */
    public static long getSDFreeSize() {
        // 取得SD卡文件路
        File path = Environment.getExternalStorageDirectory();
        StatFs sf = new StatFs(path.getPath());
        // 获取单个数据块的大小(Byte)
        long blockSize = sf.getBlockSize();
        // 空闲的数据块的数
        long freeBlocks = sf.getAvailableBlocks();
        // 返回SD卡空闲大
        return freeBlocks * blockSize; // 单位Byte
        // return (freeBlocks * blockSize)/1024; //单位KB
        // return (freeBlocks * blockSize); // 单位MB
        // return (freeBlocks * blockSize)/1024; //单位KB
        // return (freeBlocks * blockSize) / 1024 / 1024; // 单位MB
    }

    /**
     * @return
     * @方法说明:获取SD卡总容 * @方法名称:getSDAllSize
     * @返回 long
     */
    public static long getSDAllSize() {
        // 取得SD卡文件路
        File path = Environment.getExternalStorageDirectory();
        StatFs sf = new StatFs(path.getPath());
        // 获取单个数据块的大小(Byte)
        long blockSize = sf.getBlockSize();
        // 获取所有数据块
        long allBlocks = sf.getBlockCount();
        // 返回SD卡大
        return allBlocks * blockSize; // 单位Byte
        // return (allBlocks * blockSize)/1024; //单位KB
        // return (allBlocks * blockSize)/1024; // 单位MB
    }

    /**
     * @param picPhotoPath
     * @param sw
     * @param sh
     * @param isBreviary
     * @return
     * @方法说明:创建位图的缩略图
     * @方法名称:decodeBitmap
     * @返回 Bitmap
     */
    public static Bitmap decodeBitmap(String picPhotoPath, float sw, float sh,
                                      boolean isBreviary) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 通过这个bitmap获取图片的宽和高
        Bitmap bitmap = BitmapFactory.decodeFile(picPhotoPath, options);

        options.inJustDecodeBounds = false;
        if (sw == 0 || sh == 0) {
            options.inSampleSize = 1;
        } else {
            float souceSize = options.outWidth >= options.outHeight ? options.outWidth
                    : options.outHeight;
            if (souceSize <= 0) {
                return null;
            }
            float scaleSize = sw >= sh ? sw : sh;

            int scale = (int) (souceSize / scaleSize);

            if (scale <= 0) {
                scale = 1;
            }
            options.inSampleSize = scale;
        }
        // 注意这次要把options.inJustDecodeBounds 设为 false,这次图片是要读取出来
        Bitmap bitmapResult = null;
        try {
            bitmapResult = BitmapFactory.decodeFile(picPhotoPath, options);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmapResult;

    }

    /**
     * @param context
     * @param resouceId
     * @param sw
     * @param sh
     * @return
     * @方法说明:从资源包中创建位图缩略图
     * @方法名称:decodeBitmap
     * @返回 Bitmap
     */
    public static Bitmap decodeBitmap(Context context, int resouceId, float sw,
                                      float sh) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 通过这个bitmap获取图片的宽和高
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
                resouceId, options);
        options.inJustDecodeBounds = false;
        int scale;
        if (sw == 0 || sh == 0) {
            scale = 1;
        } else {
            float souceSize = options.outWidth >= options.outHeight ? options.outWidth
                    : options.outHeight;
            float scaleSize = sw >= sh ? sw : sh;
            scale = (int) (souceSize / scaleSize);
            if (scale <= 0) {
                scale = 1;
            }
        }
        options.inSampleSize = scale;
        // 注意这次要把options.inJustDecodeBounds 设为 false,这次图片是要读取出来
        bitmap = BitmapFactory.decodeResource(context.getResources(),
                resouceId, options);
        if (sw > 0 && sh > 0) {
            bitmap = ThumbnailUtils.extractThumbnail(bitmap, (int) sw,
                    (int) sh, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        }
        return bitmap;
    }

    /**
     * @param filepath
     * @return
     * @方法说明:获得图片的旋转角 * @方法名称:getExifOrientation
     * @返回 int
     */
    private static int getExifOrientation(String filepath) {
        int degree = 0;
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(filepath);
        } catch (IOException ex) {
            // Log.e("test", "cannot read exif", ex);
            ex.printStackTrace();
        }
        if (exif != null) {
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, -1);
            if (orientation != -1) {
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = 270;
                        break;
                }
            }
        }
        return degree;
    }

    /**
     * @param bitmap
     * @param path
     * @return
     * @方法说明:还原图片的角 * @方法名称:matrixBitmap
     * @返回 Bitmap
     */
    public static Bitmap matrixBitmap(Bitmap bitmap, String path) {
        if (bitmap == null)
            return null;
        int angle = getExifOrientation(path);
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        if (angle != 0) { // 如果照片出现旋转 那么 就更改旋转度
            Matrix matrix = new Matrix();
            matrix.postRotate(angle);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
        }
        return bitmap;
    }

    /**
     * @param bitmap bitmap图片
     * @param fixedw 宽度压缩比例
     * @param fixedh 高度压缩比例
     * @return
     * @方法说明:图片压缩的方 * @方法名称:matrixresuBitmap
     * @返回 Bitmap 压缩后的图片
     */
    public static Bitmap matrixresuBitmap(Bitmap bitmap, float fixedw,
                                          float fixedh) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        float scalew = fixedw / w;
        float scaleh = fixedh / h;
        Matrix matrix = new Matrix();
        matrix.postScale(scalew, scaleh);// 产生缩放后的Bitmap对象
        Bitmap resizeBitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix,
                false);
        return resizeBitmap;
    }


    public static boolean play(File file, boolean isEmpty) {
        // Get the file we want to playback.
        if (!file.exists()) {
            return false;
        }
        int musicLength = (int) (file.length() / 2);
        short[] music = new short[musicLength];

        try {
            // Create a DataInputStream to read the audio data back from the
            // saved file.
            InputStream is = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            DataInputStream dis = new DataInputStream(bis);

            // Read the file into the music array.
            int i = 0;
            if (isEmpty) {
                if (dis.available() > 0) {
                    return true;
                } else {
                    return false;
                }
            } else {
                while (dis.available() > 0) {
                    music[i] = dis.readShort();
                    i++;
                }
            }

            // Close the input streams.
            dis.close();

            // Create a new AudioTrack object using the same parameters as the
            // AudioRecord
            // object used to create the file.
            AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                    44100, AudioFormat.CHANNEL_CONFIGURATION_MONO,
                    AudioFormat.ENCODING_PCM_16BIT, musicLength * 2,
                    AudioTrack.MODE_STREAM);

            // Start playback
            audioTrack.play();

            // Write the music buffer to the AudioTrack object
            audioTrack.write(music, 0, musicLength);

            audioTrack.stop();

        } catch (Throwable t) {
            Log.e("AudioTrack", "Playback Failed");
        }
        return true;
    }

    public static int chatState = 0;// 聊天类型
    public static final int PERSONALCHAT = 0;
    public static final int ACTCHAT = 1;
    public static final int INTERESTCHAT = 2;

    /* 改变聊天类型 */
    public static void changeChaType(int state) {
        chatState = state;
    }

    /**
     * @param con
     * @param rid
     * @return
     * @方法说明:获得XML字符
     * @方法名称:getXmlStr
     * @返回 String
     */
    public static String getXmlStr(Context con, int rid) {
        return con.getString(rid);
    }

    public static Bitmap readBitMap(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        // 获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }

    /**
     * @param data
     * @param index
     * @return
     * @方法说明:活动map中下标指定的
     * @方法名称:getMapIndexObject
     * @返回 Object
     */
    public static Object getMapIndexObject(Map data, int index) {
        int count = 0;
        Iterator it = data.keySet().iterator();
        while (it.hasNext()) {
            String key = (String) it.next();
            if (count == index) {
                Object ob = data.get(key);
                return ob;
            }
            count++;

        }
        return null;

    }

    /**
     * @param data
     * @param index
     * @return
     * @方法说明:获得一个map的指定下标的key * @方法名称:getMapIndexObjectAndKey
     * @返回 Object[]
     */
    public static Object[] getMapIndexObjectAndKey(Map data, int index) {
        int count = 0;
        Iterator it = data.keySet().iterator();
        while (it.hasNext()) {
            String key = (String) it.next();
            if (count == index) {
                Object ob = data.get(key);
                return new Object[]{key, ob};
            }
            count++;

        }
        return null;
    }

    /**
     * @param sPath
     * @return
     * @方法说明:根据路径删除指定的目录或文件，无论存在与 * @方法名称:DeleteFolder
     * @返回 boolean
     */
    public static boolean DeleteFolder(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        // 判断目录或文件是否存
        if (!file.exists()) { // 不存在返false
            return flag;
        } else {
            // 判断是否为文
            if (file.isFile()) { // 为文件时调用删除文件方法
                return deleteFile(sPath);
            } else { // 为目录时调用删除目录方法
                return deleteDirectory(sPath);
            }
        }
    }

    /**
     * @param sPath
     * @return
     * @方法说明:删除单个 * @方法名称:deleteFile
     * @返回 boolean
     */
    public static boolean deleteFile(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }

    /**
     * @param sPath
     * @return
     * @方法说明:删除目录（文件夹）以及目录下的文 * @方法名称:deleteDirectory
     * @返回 boolean
     */
    public static boolean deleteDirectory(String sPath) {
        // 如果sPath不以文件分隔符结尾，自动添加文件分隔
        if (!sPath.endsWith(File.separator)) {
            sPath = sPath + File.separator;
        }
        File dirFile = new File(sPath);
        // 如果dir对应的文件不存在，或者不是一个目录，则退
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        boolean flag = true;
        // 删除文件夹下的所有文包括子目
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            // 删除子文
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag)
                    break;
            } // 删除子目
            else {
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag)
                    break;
            }
        }
        if (!flag)
            return false;
        // 删除当前目录
        if (dirFile.delete()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param data
     * @param key
     * @return
     * @方法说明:判断一个map的key值是否存
     * @方法名称:isKey
     * @返回 boolean
     */
    public static boolean isKey(Map data, String key) {
        Iterator it = data.keySet().iterator();
        while (it.hasNext()) {
            String mapKey = (String) it.next();
            if (mapKey.equals(key)) {
                return true;
            }

        }
        return false;
    }

    /**
     * @param data
     * @return
     * @方法说明:获得一个map对象的数
     * @方法名称:getMapObjectNum
     * @返回 int
     */
    public static int getMapObjectNum(Map data) {
        int count = 0;
        Iterator it = data.keySet().iterator();

        while (it.hasNext()) {
            it.next();
            count++;
        }
        return count;

    }

    /**
     * @return
     * @方法说明:获得当前系统的时 * @方法名称:getCutimeIsShow
     * @返回 String
     */
    public static String getCutimeIsShow() {
        return String.valueOf(System.currentTimeMillis());
    }

    /**
     * @param showname
     * @param bin
     * @方法说明:下载数据 * @方法名称:writeFile
     * @返回 void
     */
    public static synchronized void writeFile(String showname, byte[] bin) {
        File file = Utils.creatFilePath(Utils.getFilePath()
                + projectResoucePath, showname);
        Utils.writeFile(bin, file.getAbsolutePath());
    }

    /**
     * @param parten
     * @param date
     * @return
     * @方法说明:获得指定格式的日 * @方法名称:getDate
     * @返回 String
     */
    public static String getDate(String parten, Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(parten);
        if (date != null)
            return sdf.format(date);
        return "";
    }

    /**
     * @param data
     * @param ac
     * @return
     * @方法说明:获得相册图片的路 * @方法名称:getPicPath
     * @返回 String
     */
    public static String getPicPath(Intent data, Activity ac) {

        Uri originalUri = data.getData();

        String[] proj = {MediaStore.Images.Media.DATA};

        if (originalUri != null && proj != null) {
            Cursor cursor = ac.getContentResolver().query(originalUri, null,
                    null, null, null);
            if (cursor == null) {
                String path = originalUri.getPath();
                if (!Utils.isEmpty(path)) {

                    String type = ".jpg";
                    String type1 = ".png";
                    if (path.endsWith(type) || path.endsWith(type1)) {

                        return path;
                    } else {

                        return "";
                    }
                } else {
                    return "";
                }
            } else
                // 将光标移至开，这个很重要，不小心很容易引起越
                cursor.moveToFirst();
            // 按我个人理解 这个是获得用户选择的图片的索引
            int column_index = cursor.getColumnIndex(proj[0]);

            // 最后根据索引值获取图片路
            String path = cursor.getString(column_index);
            cursor.close();
            return path;
        } else {
            Bundle bundle = data.getExtras();
            String path = "";
            if (bundle != null) {
                Bitmap photo = (Bitmap) bundle.get("data");
                try {
                    File file = Utils.creatFilePath(Utils.getFilePath()
                            + Utils.projectResoucePath, Utils.getCutimeIsShow()
                            + ".jpg");
                    path = file.getAbsolutePath();
                    BufferedOutputStream bos = new BufferedOutputStream(
                            new FileOutputStream(path, false));
                    photo.compress(CompressFormat.JPEG, 100, bos);
                    bos.flush();
                    bos.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            return path;
        }
    }

    /**
     * @return
     * @方法说明:使用系统当前日期加以调整作为照片的名 * @方法名称:getPhotoFileName
     * @返回 String
     */
    public static String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "'IMG'_yyyyMMdd_HHmmss");
        return dateFormat.format(date) + ".jpg";
    }

    public static void albumCorp(Activity act, int index) {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
        act.startActivityForResult(intent, index);
    }

    public static void picSelect(Activity ac, int respIndex) {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        ac.startActivityForResult(intent, respIndex);
    }

    /**
     * @param ac
     * @param respIndex
     * @方法说明:此方法没有被使用 * @方法名称:photoPic
     * @返回 void
     */
    public static void photoPic(Activity ac, int respIndex) {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        ac.startActivityForResult(intent, respIndex);
    }

    /**
     * @param ac
     * @param file
     * @param respIndex
     * @方法说明:照相机功 * @方法名称:photoPic
     * @返回 void
     */
    public static void photoPic(Activity ac, File file, int respIndex) {
        // 跳转至相机界
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // 指定相机拍照后相片的存储位置
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        ac.startActivityForResult(intent, respIndex);
    }

    /**
     * @param ac
     * @param tempFile
     * @param respIndex
     * @方法说明:
     * @方法名称:photoCut
     * @返回 void
     */
    public static void photoCut(Activity ac, File tempFile, int respIndex) {
        Intent cameraintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 指定调用相机拍照后照片的储存路径
        cameraintent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
        ac.startActivityForResult(cameraintent, respIndex);
    }

    public static String getStartsWith(String souceStr, String startStr,
                                       String str) {
        int index = souceStr.indexOf(startStr);
        if (index != -1) {
            String leftStr = souceStr.substring(0, index);
            String right = souceStr.substring(index + startStr.length());
            return leftStr + str + getStartsWith(right, startStr, str);
        } else {
            return souceStr;
        }
    }

    /**
     * @param et
     * @param imageName
     * @param image
     * @方法说明:插图图片
     * @方法名称:insetImage
     * @返回 void
     */
    public static void insetImage(EditText et, String imageName, Bitmap image) {
        Editable eb = et.getEditableText();
        SpannableString ss = new SpannableString("[" + imageName + "]");
        ;
        // 定义插入图片
        Drawable drawable = new BitmapDrawable(image);
        ss.setSpan(new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE), 0, ("["
                + imageName + "]").length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        drawable.setBounds(2, 0, drawable.getIntrinsicWidth() + 20,
                drawable.getIntrinsicHeight() + 20);
        // 获得光标所在位
        int qqPosition = et.getSelectionStart();
        // 插入图片
        eb.insert(qqPosition, "\n");
        eb.insert(qqPosition, ss);
        eb.insert(qqPosition, "\n");
    }

    /*
     * @param sourceStr
     *
     * @return
     *
     * @返回 String
     */
    public static String parserDiscussContext(String sourceStr) {
        String parrer = "[\\[\\]]";
        String[] ss = sourceStr.split(parrer);
        String endStr = "";
        for (int i = 0; i < ss.length; i++) {
            String indexStr = ss[i];
            if (indexStr.startsWith("/")) {

                endStr += "[/img]";
            } else {
                endStr += indexStr;
            }
        }
        return endStr;
    }

    /**
     * @param view
     * @return
     * @方法说明:测量一个控件的大小
     * @方法名称:getViewSize
     * @返回 int[]
     */
    public static int[] getViewSize(View view) {

        int w = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);

        view.measure(w, h);
        int vw = view.getMeasuredWidth();
        int vh = view.getMeasuredHeight();
        return new int[]{vw, vh};
    }

    public static void setViewisvible(View view, Boolean is) {
        if (is) {
            view.setVisibility(View.INVISIBLE);
        } else {
            view.setVisibility(View.VISIBLE);
        }
    }

    public static void setViewVisibilityAgainst(View view, boolean is) {
        int visible = view.getVisibility();
        switch (visible) {
            case View.GONE:
            case View.INVISIBLE:
                view.setVisibility(View.VISIBLE);
                break;

            case View.VISIBLE:
                if (is) {
                    view.setVisibility(View.INVISIBLE);
                } else {
                    view.setVisibility(View.GONE);
                }

                break;
        }
    }

    /**
     * @param path
     * @return
     * @方法说明:计算图片的大 * @方法名称:calculatePicBig
     * @返回 int
     */
    public static int calculatePicBig(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 通过这个bitmap获取图片的宽和高
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        int[] size = Utils.getBitmapWorH(path);
        int w = options.outWidth;
        int h = options.outHeight;
        int sizeByte = 4;
        /**
         * 这一段代码不理解
         */
        if (options.inPreferredConfig == Config.ALPHA_8) {
            sizeByte = 1;
        } else if (options.inPreferredConfig == Config.ARGB_4444) {
            sizeByte = 2;

        } else if (options.inPreferredConfig == Config.ARGB_8888) {
            sizeByte = 4;

        } else if (options.inPreferredConfig == Config.RGB_565) {
            sizeByte = 2;
        }
        return w * h * sizeByte;
    }

    /**
     * @param path
     * @return
     * @方法说明:获取图片的宽和高
     * @方法名称:getBitmapWorH
     * @返回 int[]
     */
    public static int[] getBitmapWorH(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 通过这个bitmap获取图片的宽和高
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        int w = options.outWidth;
        int h = options.outHeight;
        return new int[]{w, h};
    }

    /**
     * @param context
     * @param resId
     * @return
     * @方法说明:获取图片的宽和高
     * @方法名称:getDrawbleWorH
     * @返回 int[]
     */
    public static int[] getDrawbleWorH(Context context, int resId) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 通过这个bitmap获取图片的宽和高
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
                resId);
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        return new int[]{w, h};
    }

    private static int y, m, d;
    private static DatePickerDialog showdata;

    /**
     * @param con
     * @param c
     * @param isTime
     * @param showText
     * @方法说明:显示日期
     * @方法名称:showData
     * @返回 void
     */
    public static void showData(Context con, Calendar c, final boolean isTime,
                                final TextView showText) {
        showdata = new DatePickerDialog(con,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year,
                                          int month, int dayOfMonth) {
                        y = year;
                        m = month + 1;
                        d = dayOfMonth;
                        String sm = String.valueOf(m);
                        String sd = String.valueOf(d);
                        if (m < 0) {
                            sm = 0 + String.valueOf(m);
                        }
                        if (d < 0) {
                            sd = 0 + String.valueOf(d);
                        }
                        if (!isTime) {
                            showText.setText(y + "-" + sm + "-" + sd);
                        }
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH)) {

        };
        showdata.setCancelable(false);
        showdata.show();
    }

    /**
     * @方法说明:关闭时间选择 * @方法名称:closeDataSelect
     * @返回 void
     */
    public static void closeDataSelect() {
        if (showdata != null) {
            showdata.dismiss();
        }
        if (showtime != null) {
            showtime.dismiss();
        }
    }

    private static int h, f;
    private static TimePickerDialog showtime;


    /**
     * @param context
     * @param layoutR
     * @return
     * @方法说明:加载一组布局文件
     * @方法名称:loadViewArray
     * @返回 ArrayList<View>
     */
    public static ArrayList<View> loadViewArray(Context context, int[] layoutR) {
        ArrayList<View> list = new ArrayList<View>();
        for (int i = 0; i < layoutR.length; i++) {
            View view = Utils.LoadXmlView(context, layoutR[i]);
            list.add(view);
        }
        return list;
    }

    /**
     * @param list
     * @方法说明:移除数组中的所有元 * @方法名称:removeAll
     * @返回 void
     */
    public static void removeAll(ArrayList list) {
        int count = list.size();
        for (int i = 0; i < count; i++) {
            list.remove(0);
        }
    }

    /**
     * @param listView
     * @方法说明:获得listView的高 * @方法名称:setListViewHeightBasedOnChildren
     * @返回 void
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        BaseAdapter adpter = (BaseAdapter) listView.getAdapter();
        if (adpter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0, len = adpter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = adpter.getView(i, null, listView);
            listItem.measure(0, 0); // 计算子项View 的宽
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (adpter.getCount() - 1));

        listView.setLayoutParams(params);
    }

    /**
     * @param gridView
     * @param cols
     * @param hozHight
     * @param height
     * @方法说明:获取gridView的高 * @方法名称:setListViewHeightBasedOnChildren
     * @返回 void
     */
    public static void setListViewHeightBasedOnChildren(GridView gridView,
                                                        int cols, int hozHight, int height) {
        // 获取GridView对应的Adapter
        BaseAdapter adpter = (BaseAdapter) gridView.getAdapter();
        if (adpter == null) {
            return;
        }
        // 每列放多少个item
        int cls = adpter.getCount() % cols == 0 ? adpter.getCount() / cols
                : adpter.getCount() / cols + 1;

        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = cls * height + hozHight * (cls - 1);

        gridView.setLayoutParams(params);

    }

    /**
     * @param str
     * @param defValue
     * @return
     * @方法说明:字符串转换成整数
     * @方法名称:toInt
     * @返回 int
     */
    public static int toInt(String str, int defValue) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
        }
        return defValue;
    }

    /**
     * @param obj
     * @return
     * @方法说明:对象转整 * @方法名称:toInt
     * @返回 int
     */
    public static int toInt(Object obj) {
        if (obj == null)
            return 0;
        return toInt(obj.toString(), 0);
    }

    /**
     * @param str
     * @return
     * @方法说明:判断字符串是否为 * @方法名称:isEmpty
     * @返回 boolean
     */
    public static boolean isEmpty(String str) {
        if (str == null || str.equals("") || str.equals("null")) {
            return true;
        }
        return false;
    }

    /**
     * @param str
     * @return
     * @方法说明:判断str是否为数 * @方法名称:isNumeric
     * @返回 boolean
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * @param filePath
     * @return
     * @方法说明:判断一个文件是否存 * @方法名称:fileIsExists
     * @返回 boolean
     */
    public boolean fileIsExists(String filePath) {
        File f = new File(filePath);
        if (f.exists()) {
            return true;
        }
        return false;
    }

    public static Bitmap getRoundGray(Bitmap bt, int pix, boolean isGray) {
        int width = bt.getWidth();
        int height = bt.getHeight();
        Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawColor(0x0);
        if (pix > 0) {
            Rect rect = new Rect(0, 0, bt.getWidth(), bt.getHeight());
            RectF rectf = new RectF(rect);
            paint.setAntiAlias(true);
            canvas.drawRoundRect(rectf, pix, pix, paint);
            paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        }
        if (isGray) {
            paint.setAlpha(0x60);
        }
        canvas.drawBitmap(bt, 0, 0, paint);

        if (bt.isRecycled()) {
            bt.recycle();
        }
        return output;
    }

    /**
     * @param v
     * @param event
     * @return
     * @方法说明:判断触摸点是否在控件 * @方法名称:isEvenAboveView
     * @返回 boolean
     */
    public static boolean isEvenAboveView(View v, MotionEvent event) {
        if (v != null) {
            int[] leftTop = {0, 0};
            v.getLocationOnScreen(leftTop);
            int left = leftTop[0];

            int top = leftTop[1];
            int bottom = leftTop[1] + v.getHeight();
            int right = leftTop[0] + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 保留点击EditText的事 return true;
            }
        }
        return false;
    }

    /**
     * @param content
     * @param context
     * @方法说明:实现文本复制功能
     * @方法名称:copy
     * @返回 void
     */
    public static void copy(SpannableString content, Context context) {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) context
                .getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(content);
    }

    private static int verifyCount;
    private static Button verifyButton = null;
    private static String verifyContent = "向我发送验证码";

    /**
     * @说明: 验证码发送检  * @名称:handler
     * @类型:Handler
     */
    static Handler handler = null;
    static Timer timer = null;
    private static boolean isCheckMessage = false;

    public static void startTimeMessage(int second) {
        verifyCount = second;
        new Thread(new Runnable() {

            @Override
            public void run() {
                while (verifyCount > 0) {
                    verifyCount--;
                    setPlaySound(true);
                    setMessageState(true);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                new Handler(Looper.getMainLooper()).post(new Runnable() {

                    @Override
                    public void run() {
                        verifyCount = 0;
                        setMessageState(false);
                        setPlaySound(false);
                    }
                });
            }
        }).start();

    }

    private static boolean isplaySound = false;

    public static boolean getPlaySound() {
        return isplaySound;
    }

    public static void setPlaySound(boolean isPSound) {
        isplaySound = isPSound;
    }

    public static boolean getMessageState() {
        return isCheckMessage;
    }

    public static void setMessageState(boolean iso) {
        isCheckMessage = iso;
    }

    /**
     * @方法说明:重置音效状 * @方法名称:stopMessageVerfy
     * @返回 void
     */
    public static void stopMessageVerfy() {
        verifyCount = 0;
        setMessageState(false);
        setPlaySound(false);
    }

    /**
     * @方法说明:停止验证检  * @方法名称:stopVerfy
     * @返回 void
     */
    // public static void stopVerfy() {
    // if (verifyButton != null) {
    // verifyCount = 0;
    // verifyButton.setTag("true");
    // verifyButton.setText(verifyContent);
    // verifyButton.setBackgroundResource(R.drawable.normal_btn);
    // }
    // }

    /**
     * @param context
     * @param dpValue
     * @return
     * @方法说明:根据手机的分辨率从dp的单位转成px(像素)
     * @方法名称:diptopx
     * @返回 int
     */
    public static int diptopx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * @param context
     * @param pxValue
     * @return
     * @方法说明:根据手机的分辨率从px(像素)转换成dp
     * @方法名称:pxtodip
     * @返回 int
     */
    public static int pxtodip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * @param title
     * @param array
     * @param text
     * @param con
     * @方法说明:带单选按钮的Dialog
     * @方法名称:selectcondition
     * @返回 void
     */
    public static void selectcondition(String title, final String[] array,
                                       final TextView text, Context con) {
        AlertDialog.Builder malertdialog = new AlertDialog.Builder(con);
        malertdialog.setTitle(title);
        malertdialog.setIcon(android.R.drawable.ic_dialog_info);
        malertdialog.setItems(array, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int whichButton) {

                text.setText(array[whichButton]);
                dialog.dismiss();
            }
        }).create().show();

    }

    /**
     * @param tag1
     * @param tag2
     * @param value
     * @param context
     * @方法说明:保存参数，此方法没有被使 * @方法名称:savaValue
     * @返回 void
     */
    public static void savaValue(String tag1, String tag2, Object value,
                                 Context context) {
        SharedPreferences sp = context.getSharedPreferences(tag1 + "value",
                context.MODE_PRIVATE);
        if (value instanceof Boolean) {
            sp.edit().putBoolean(tag1 + "value" + tag2, (Boolean) value)
                    .commit();

        } else if (value instanceof Integer || value instanceof Byte) {
            sp.edit().putInt(tag1 + "value" + tag2, (Integer) value).commit();

        } else if (value instanceof Long) {
            sp.edit().putLong(tag1 + "value" + tag2, (Long) value).commit();
        } else if (value instanceof Float) {
            sp.edit().putFloat(tag1 + "value" + tag2, (Float) value).commit();

        } else if (value instanceof String) {
            sp.edit().putString(tag1 + "value" + tag2, (String) value).commit();

        }

    }

    /**
     * @param tag1
     * @param tag2
     * @param T
     * @param context
     * @param defaultValue
     * @return
     * @方法说明:返回共享首选项中的数据，此方法没有被使 * @方法名称:getValue
     * @返回 Object
     */
    public static Object getValue(String tag1, String tag2, Class<?> T,
                                  Context context, Object defaultValue) {
        SharedPreferences sp = context.getSharedPreferences(tag1 + "value",
                context.MODE_PRIVATE);
        if (T == Boolean.class) {
            return sp.getBoolean(tag1 + "value" + tag2, (Boolean) defaultValue);

        } else if (T == Integer.class || T == Byte.class) {
            return sp.getInt(tag1 + "value" + tag2, (Integer) defaultValue);
        } else if (T == Long.class) {
            return sp.getLong(tag1 + "value" + tag2, (Long) defaultValue);
        } else if (T == Float.class) {
            return sp.getFloat(tag1 + "value" + tag2, (Float) defaultValue);
        } else if (T == String.class) {
            return sp.getString(tag1 + "value" + tag2, (String) defaultValue);
        }
        return null;
    }

    /**
     * @param c
     * @return
     * @方法说明:计算分享内容的字数，一个汉字两个英文字母，一个中文标点两个英文标 注意：该函数的不适用于对单个字符进行计算，因为单个字符四舍五入后都是1
     * @方法名称:calculateLength
     * @返回 long
     */
    public static long calculateLength(CharSequence c) {
        double len = 0;
        for (int i = 0; i < c.length(); i++) {
            int tmp = (int) c.charAt(i);
            if (tmp > 0 && tmp < 127) {
                len += 0.5;
            } else {
                len++;
            }
        }
        return Math.round(len);
    }

    /**
     * @param editText
     * @param length
     * @方法说明:限制EditText的输入字 * @方法名称:limitEdit
     * @返回 void
     */
    public static void limitEdit(final Context context,
                                 final EditText editText, final int length) {
        editText.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;
            private boolean isEdit = true;

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (isEdit == false) {
                    Toast.makeText(context, "输入的字数超过限制", Toast.LENGTH_SHORT)
                            .show();
                    Editable etext = editText.getText();
                    int pos = etext.length();
                    Selection.setSelection(etext, pos);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                temp = s;
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (Utils.calculateLength(temp) > length) {
                    isEdit = false;
                    Toast.makeText(context, "输入的字数超过限制", Toast.LENGTH_SHORT)
                            .show();
                    s.delete(temp.length() - 1, temp.length());
                    editText.setText(s);
                }
            }
        });
    }

    /**
     * @param mTextView
     * @param num
     * @param mEditText
     * @方法说明:刷新剩余输入字数最大值新浪微博是140个字，人人网200个字
     * @方法名称:setLeftCount
     * @返回 void
     */
    public static void setLeftCount(TextView mTextView, int num,
                                    EditText mEditText) {
        mTextView.setText(String.valueOf((num - getInputCount(mEditText))));
    }

    /**
     * @param mEditText
     * @return
     * @方法说明:获取用户输入的分享内容字 * @方法名称:getInputCount
     * @返回 long
     */
    private static long getInputCount(EditText mEditText) {
        return calculateLength(mEditText.getText().toString());
    }

    /**
     * @param str
     * @param minNum
     * @param maxNum
     * @return
     * @方法说明:截取字符 * @方法名称:stringSubStringfInfo
     * @返回 String
     */
    public static String stringSubStringfInfo(String str, int minNum, int maxNum) {
        if (Utils.isEmpty(str)) {
            return null;
        }
        String s = str.substring(minNum, maxNum);
        return s;
    }

    /**
     * @return
     * @方法说明:判断sd卡存 * @方法名称:hasSDCard
     * @返回 boolean
     */
    public static boolean hasSDCard() {
        String status = Environment.getExternalStorageState();
        if (!status.equals(Environment.MEDIA_MOUNTED)) {
            return false;
        }
        return true;
    }

    public static String getRootFilePath() {
        if (hasSDCard()) {
            return Environment.getExternalStorageDirectory().getAbsolutePath()
                    + "/";// filePath:/sdcard/
        } else {
            return Environment.getDataDirectory().getAbsolutePath() + "/data/"; // filePath:
            // /data/data/
        }
    }

	/*
   * public static void countdown(int second, final Button button, final
	 * String content) { verifyButton = button; verifyCount = second;
	 * button.setTag("false"); button.setBackgroundColor(R.color.gray); new
	 * Thread(new Runnable() {
	 * 
	 * @Override public void run() { while (verifyCount > 0) {
	 * 
	 * new Handler(Looper.getMainLooper()).post(new Runnable() {
	 * 
	 * @Override public void run() { button.setText(String.valueOf(verifyCount)
	 * + "); // button.setOnClickListener(null); } }); verifyCount--; try {
	 * Thread.sleep(1000); } catch (InterruptedException e) {
	 * e.printStackTrace(); }
	 * 
	 * } new Handler(Looper.getMainLooper()).post(new Runnable() {
	 * 
	 * @Override public void run() { verifyCount = 0; button.setTag("true");
	 * button.setText(content);
	 * button.setBackgroundResource(R.drawable.my_btn_blue_backgroup); } });
	 * 
	 * } }).start();
	 * 
	 * }
	 */

    public static OnFocusChangeListener onFocusAutoClearHintListener = new OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            EditText textView = (EditText) v;
            String hint;
            if (hasFocus) {
                hint = textView.getHint().toString();
                textView.setTag(hint);
                textView.setHint("");
            } else {
                hint = textView.getTag().toString();
                textView.setHint(hint);
            }
        }
    };

    private static long lastClickTime;

    /**
     * @param times
     * @return
     * @方法说明:防止控件被重复点击，如果点击间隔时间小于指定时间就点击无 * @方法名称:isFastDoubleClick
     * @返回 boolean
     */
    public static boolean isFastDoubleClick(long times) {
        times = 1000;
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < times) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    /**
     * @param mContext
     * @return
     * @方法说明:获取当前客户端版本信息versionName + versionCode
     * @方法名称:getCurrentVersion
     * @返回 String
     */
    public static String getCurrentVersion(Context mContext) {
        try {
            PackageInfo info = mContext.getPackageManager().getPackageInfo(
                    mContext.getPackageName(), 0);
            return info.versionName.trim();
//            return info.versionName.trim() + String.valueOf(info.versionCode);
        } catch (NameNotFoundException e) {
            e.printStackTrace(System.err);
        }
        return "";
    }

    /**
     * @param filePath
     * @return
     * @方法说明:获取文件的名 * @方法名称:getFileName
     * @返回 String
     */
    public static String getFileName(String filePath) {
        if (isEmpty(filePath))
            return "";
        int index0 = filePath.lastIndexOf(".");
        if (index0 != -1) {
            filePath = filePath.substring(0, index0);
        }
        filePath = filePath.replaceAll(":", "");
        filePath = filePath.replaceAll(File.separator, "");
        return filePath;
    }

    /**
     * @param context
     * @return
     * @方法说明:
     * @方法名称:getDisPlayMetrics
     * @返回 DisplayMetrics
     */
    public static DisplayMetrics getDisPlayMetrics(Context context) {
        DisplayMetrics metric = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay()
                .getMetrics(metric);
        return metric;
    }

    /**
     * @param context
     * @return
     * @方法说明:获取屏幕的宽度（像素 * @方法名称:getScreenWidth
     * @返回 int
     */
    public static int getScreenWidth(Context context) {
        int width = getDisPlayMetrics(context).widthPixels;
        return width;
    }

    /**
     * @param context
     * @return
     * @方法说明:获取屏幕的高 * @方法名称:getScreenHeight
     * @返回 int
     */
    public static int getScreenHeight(Context context) {
        int height = getDisPlayMetrics(context).heightPixels;
        return height;
    }

    /**
     * @param context
     * @return
     * @方法说明:屏幕密度(0.75 / 1.0 / 1.5)
     * @方法名称:getDensity
     * @返回 float
     */
    public static float getDensity(Context context) {
        float density = getDisPlayMetrics(context).density;
        return density;
    }

    /**
     * @param context
     * @return
     * @方法说明:屏幕密度DPI(120 / 160 / 240)
     * @方法名称:getDensityDpi
     * @返回 int
     */
    public static int getDensityDpi(Context context) {
        int densityDpi = getDisPlayMetrics(context).densityDpi;
        return densityDpi;
    }

    /**
     * @param input
     * @return
     * @方法说明:半角转换为全 * @方法名称:ToDBC
     * @返回 String
     */
    public static String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    /**
     * @param str
     * @return
     * @方法说明:去除特殊字符或将所有中文标号替换为英文标号
     * @方法名称:stringFilter
     * @返回值:String
     */
    public static String stringFilter(String str) {
        str = str.replaceAll("【", "[").replaceAll("】", "]")
                .replaceAll("！", "!").replaceAll("：", ":");// 替换中文标号
        String regEx = "[『』]"; // 清除掉特殊字符
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    /**
     * @author TanYong
     * create at 2017/5/9 10:48
     * TODO：Uri获取真实路径
     */
    public static String getPathFromUri(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    public static void setMyListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

    public static boolean checkCameraHardware(Context context) {
        if (context != null && context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }
    public static DisplayMetrics getScreenWH(Context context) {
        DisplayMetrics dMetrics = new DisplayMetrics();
        dMetrics = context.getResources().getDisplayMetrics();
        return dMetrics;
    }

    /**
     * 将图片按照某个角度进行旋转
     *
     * @param bm
     *            需要旋转的图片
     * @param degree
     *            旋转角度
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;

        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
                    bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }

    /** 从给定的路径加载图片，并指定是否自动旋转方向 */
    public static Bitmap loadBitmap(String imgpath, boolean adjustOritation) {
        if (!adjustOritation) {
            return BitmapFactory.decodeFile(imgpath);
        } else {
            Bitmap bm = BitmapFactory.decodeFile(imgpath);
            int digree = 0;
            ExifInterface exif = null;
            try {
                exif = new ExifInterface(imgpath);
            } catch (IOException e) {
                e.printStackTrace();
                exif = null;
            }
            if (exif != null) {
                // 读取图片中相机方向信息
                // int ori = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                // ExifInterface.ORIENTATION_NORMAL);
                int ori = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_FLIP_VERTICAL);
                // 计算旋转角度
                switch (ori) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        digree = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        digree = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        digree = 270;
                        break;
                    default:
                        digree = 0;
                        break;
                }
            }
            if (digree != 0) {
                // 旋转图片
                Matrix m = new Matrix();
                m.postRotate(digree);
                bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
                        bm.getHeight(), m, true);
            }
            return bm;
        }
    }

    /**
     * 根据路径获取图片并压缩返回bitmap用于显示
     *
     * @param context
     * @param id
     * @return
     */

    public static Bitmap getSmallBitmap(Context context, int id,int width,int height) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(), id, options);
        // 计算 缩略图大小为原始图片大小的几分之一 inSampleSize:缩略图大小为原始图片大小的几分之一
        options.inSampleSize = calculateInSampleSize(options, width, height);
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeResource(context.getResources(), id, options);
    }

    /**
     * 读取照片exif信息中的旋转角度
     * @param path 照片路径
     * @return角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    public static boolean isActivityTop(Class cls, Context context){
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        String name = manager.getRunningTasks(1).get(0).topActivity.getClassName();
        return name.equals(cls.getName());
    }

    /**
     * 将Drawable转化为Bitmap
     *
     * @param drawable Drawable
     * @return Bitmap
     */
    public static Bitmap getBitmapFromDrawable(Drawable drawable) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, drawable
                .getOpacity() != PixelFormat.OPAQUE ? Config.ARGB_8888
                : Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;

    }

}
