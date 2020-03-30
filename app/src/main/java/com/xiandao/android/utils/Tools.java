package com.xiandao.android.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.devspark.progressfragment.ProgressWheel;
import com.xiandao.android.R;
import com.xiandao.android.ui.BaseActivity;

import org.xutils.app.LynActivityManager;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Pattern;

public class Tools {

    // 将字符串时间转化为秒数(yyyyMMddHHmmss)
    static public long getUnixTimestamp() {
        Calendar calendar = Calendar.getInstance();
        TimeZone tztz = TimeZone.getTimeZone("GMT+8");
        calendar.setTimeZone(tztz);
        long result_time = 0;
        try {
            // 返回的是毫秒数故除以1000
            result_time = calendar.getTimeInMillis() / 1000;
        } catch (Exception e) {
            // 出现异常时间赋值为20000101000000
            result_time = 946684800;
        }
        return result_time;
    }

    // 将字符串时间转化为秒数(yyyyMMddHHmmss)
    static public long getUnixTimestampms() {
        Calendar calendar = Calendar.getInstance();
        TimeZone tztz = TimeZone.getTimeZone("GMT+8");
        calendar.setTimeZone(tztz);
        long result_time = 0;
        try {
            // 返回的是毫秒数故除以1000
            result_time = calendar.getTimeInMillis();
        } catch (Exception e) {
            // 出现异常时间赋值为20000101000000
            result_time = 946684800;
        }
        return result_time;
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


    public static void styleRandom(ProgressWheel wheel, Context ctx) {
        wheel.setRimShader(null);
        wheel.setRimColor(0xFFFFFFFF);
        wheel.setCircleColor(0x00000000);
        wheel.setBarColor(0xFF8a2d98);
        wheel.setContourColor(0xFFFFFFFF);
        wheel.setBarWidth(pxFromDp(ctx, 1));
        wheel.setBarLength(pxFromDp(ctx, 100));
        wheel.setSpinSpeed(4f);
        wheel.setDelayMillis(3);
        wheel.setText("加载中...");
        wheel.setTextColor(ctx.getResources().getColor(R.color.exist_login_color));
    }

    public static int pxFromDp(final Context context, final float dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
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


    private static long lastClickTime;

    /**
     * @param times
     * @return
     * @方法说明:防止控件被重复点击，如果点击间隔时间小于指定时间就点击无 * @方法名称:isFastDoubleClick
     * @返回 boolean
     */
    public static boolean isFastDoubleClick(long times) {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < times) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    /**
     * @return
     * @方法说明:获取各大UI的activity的上下文
     * @方法名称:getUiContext
     * @返回值:Context
     */
    public static Context getUiContext() {
        Context context = null;
        if (LynActivityManager.getInstance().currentActivity() != null) {
            if (LynActivityManager.getInstance().currentActivity() instanceof BaseActivity) {
                BaseActivity activity = (BaseActivity) LynActivityManager.getInstance().currentActivity();
                context = (Context) activity;
            }
        }
        return context;
    }

    /**
     * 保留两位小数
     */
    public static double getTwoNumber(Double money) {
        if (Tools.isEmpty(money + "")) {
            return 0;
        } else {
            DecimalFormat df = new DecimalFormat("######0.00");
            Double number = Double.valueOf(df.format(money));
            return number;
        }
    }

    /**
     * 设置商品价格样式
     *
     * @param price
     * @param tv_money
     */
    public static void setPriceStyle(String price, TextView tv_money) {
        //保留小数点两位小数
        if (Tools.isEmpty(price)) {
            tv_money.setText("0.00");
            return;
        }
        DecimalFormat df = new DecimalFormat("0.00");
        String bPrice = df.format(Double.parseDouble(price));
        String money = "¥ " + bPrice;
        SpannableStringBuilder ss = new SpannableStringBuilder(money);
        ss.setSpan(new AbsoluteSizeSpan(10, true), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_money.setText(ss);
    }

    /**
     * 设置价格样式
     *
     * @param price
     * @param tv_money
     */
    public static void setSCPriceStyle(Double price, TextView tv_money) {
        //保留小数点两位小数
        if (price == null) {
            tv_money.setText("0.00");
            return;
        }
        DecimalFormat df = new DecimalFormat("0.00");
        String bPrice = df.format(price);
        String ssj = "";
        String money = ssj + "¥ " + bPrice;
        SpannableStringBuilder ss = new SpannableStringBuilder(money);
        ss.setSpan(new AbsoluteSizeSpan(10, true), ssj.length(), ssj.length() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_money.setText(ss);
        tv_money.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
    }

    /**
     * 正则表达式：验证昵称允许输入2-12位字符，支持中英文、数字及“-” “_” “@”组合。
     */
    public static final String REGEX_NICK = "[\\u4e00-\\u9fa5_a-zA-Z0-9-@-_]{2,12}";
    /**
     * 正则表达式：验证用户名
     */
    public static final String REGEX_USERNAME = "^[a-zA-Z]\\w{5,17}$";

    /**
     * 正则表达式：验证密码
     */
    public static final String REGEX_PASSWORD = "^[a-zA-Z0-9]{6,16}$";

    /**
     * 正则表达式：验证手机号
     */
    public static final String REGEX_MOBILE = "^((13[0-9])|(15[^4,\\D])|(18[0,1-9]))\\d{8}$";

    /**
     * 正则表达式：验证邮编
     */
    public static final String REGEX_CODE = "^[1-9]\\d{5}(?!\\d)";

    /**
     * 正则表达式：验证邮箱
     */
    public static final String REGEX_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

    /**
     * 正则表达式：验证汉字
     */
    public static final String REGEX_CHINESE = "^[\u4e00-\u9fa5],{0,}$";

    /**
     * 正则表达式：验证身份证
     */
    public static final String REGEX_ID_CARD = "(^\\d{18}$)|(^\\d{15}$)";

    /**
     * 正则表达式：验证URL
     */
    public static final String REGEX_URL = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";

    /**
     * 校验用户名
     *
     * @param username
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isUsername(String username) {
        return Pattern.matches(REGEX_USERNAME, username);
    }

    public static boolean isNick(String nick) {
        return Pattern.matches(REGEX_NICK, nick);
    }

    /**
     * 校验密码
     *
     * @param password
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isPassword(String password) {
        return Pattern.matches(REGEX_PASSWORD, password);
    }

//    /**
//     * 校验手机号
//     *
//     * @param mobile
//     * @return 校验通过返回true，否则返回false
//     */
//    public static boolean isMobile(String mobile) {
//        return Pattern.matches(REGEX_MOBILE, mobile);
//    }

    /**
     * 校验邮编
     *
     * @return 校验通过返回true，否则返回false
     * @paramCODE
     */
    public static boolean isCode(String code) {
        return Pattern.matches(REGEX_CODE, code);
    }

    /**
     * 校验邮箱
     *
     * @param email
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isEmail(String email) {
        return Pattern.matches(REGEX_EMAIL, email);
    }

    /**
     * 校验汉字
     *
     * @param chinese
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isChinese(String chinese) {
        return Pattern.matches(REGEX_CHINESE, chinese);
    }

    /**
     * 校验身份证
     *
     * @param idCard
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isIDCard(String idCard) {
        return Pattern.matches(REGEX_ID_CARD, idCard);
    }

    public static void picSelect(Activity ac, int respIndex) {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        ac.startActivityForResult(intent, respIndex);
    }


    /**
     * 根据Uri获取图片绝对路径，解决Android4.4以上版本Uri转换
     *
     * @param imageUri
     */
    @TargetApi(19)
    public static String getImageAbsolutePath(Activity context, Uri imageUri) {
        if (context == null || imageUri == null)
            return null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, imageUri)) {
            if (isExternalStorageDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(imageUri)) {
                String id = DocumentsContract.getDocumentId(imageUri);
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } // MediaStore (and general)
        else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(imageUri))
                return imageUri.getLastPathSegment();
            return getDataColumn(context, imageUri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
            return imageUri.getPath();
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String column = MediaStore.Images.Media.DATA;
        String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

//    public static String starUserName(String userName) {
//        if (userName == null) return "匿名用户";
//        if (userName.length() != 12) return "匿名用户";
//        String telephoneNum = userName.substring(1);
//        if (isMobile(telephoneNum)) {
//            String replaceUserName = userName.substring(4, 8);
//            return userName.replace(replaceUserName, "****");
//        } else {
//            return "匿名用户";
//        }
//    }

    /**
     * 在ScrollView中嵌套一个listview，通过listview的条目来计算listview的高度
     *
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        BaseAdapter baseAdapter = (BaseAdapter) listView.getAdapter();
        if (baseAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < baseAdapter.getCount(); i++) {
            View listItem = baseAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (baseAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    /**
     * 设置GridView的高度
     */
    public static void setGridViewHeightBasedOnChildren(GridView gridView) {
        BaseAdapter baseAdapter = (BaseAdapter) gridView.getAdapter();
        if (baseAdapter == null) {
            return;
        }
        // 固定列宽，有多少列
        int col = 3;
        int totalHeight = 0;
        for (int i = 0; i < baseAdapter.getCount(); i += col) {
            View listItem = baseAdapter.getView(i, null, gridView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        // 设置高度
        params.height = totalHeight + 30;
        // 设置margin
        ((ViewGroup.MarginLayoutParams) params).setMargins(2, 2, 2, 2);
        // 设置参数
        gridView.setLayoutParams(params);
    }

    /**
     * 获取本机时间戳
     */
    public static long getLocationTime() {
        long locationTime = System.currentTimeMillis();
        long servertime = FileManagement.getServerTime();
        if (servertime > 0) {
            locationTime += servertime;
        }
        return locationTime / 1000;
    }

    public static String getNowTime() {
        Calendar c = Calendar.getInstance();//可以对每个时间域单独修改
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int date = c.get(Calendar.DATE);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        return year + "-" + month + "-" + date + " " + hour + ":" + minute;
    }

    private static String content;
    private static long oneTime;
    private static long twoTime;
    private static Toast toast;

    /**
     * 弹出Toast
     *
     * @param str
     */
    public static void showPrompt(String str) {
        if ((getUiContext() == null)) {
            return;
        } else if (Tools.isEmpty(str)) {
            return;
        }
        if (toast == null) {
            content = str;
            toast = Toast.makeText(getUiContext(), str, Toast.LENGTH_SHORT);
            toast.show();
            oneTime = System.currentTimeMillis();
        } else {
            if (Tools.isEmpty(content)) {
                return;
            }
            twoTime = System.currentTimeMillis();
            if (str.equals(content) && twoTime - oneTime > Toast.LENGTH_SHORT) {
                toast.show();
            } else {
                content = str;
                toast.setText(content);
                toast.show();
            }
            oneTime = twoTime;
        }
    }

    public static boolean isUserLogin() {
        if (isEmpty(FileManagement.getTokenInfo())) {
            return false;
        }
        return true;
    }

    public static String getStringValue(String value) {
        if (null == value) {
            value = "";
        }
        return value;
    }

    /**
     * @author TanYong
     * create at 2017/6/6 15:04
     * TODO：判断比较的时间是否大于当前时间
     */
    public static boolean compareDateTime(String dateTimeValue) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        try {
            Date dateTime = df.parse(dateTimeValue);
            long systemDateTime = System.currentTimeMillis();
            if (dateTime.getTime() > systemDateTime) {
                return true;
            } else {
                return false;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return false;
    }

    /**
     * @author TanYong
     * create at 2017/6/6 15:04
     * TODO：判断比较的时间是否大于期望时间
     */
    public static boolean compareDateTime(String dateTimeValue, String time) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        try {
            Date dateTime = df.parse(dateTimeValue);
            Date timeDate = df.parse(time);
            if (dateTime.getTime() > timeDate.getTime()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return false;
    }

    public static void hideKeyboard(Context mcontext, ViewGroup view) {
        view.requestFocus();
        InputMethodManager im = (InputMethodManager) mcontext.getSystemService(Context.INPUT_METHOD_SERVICE);
        try {
            im.hideSoftInputFromWindow(view.getWindowToken(), 0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showKeyboard(Context mcontext, View view) {
        InputMethodManager im = (InputMethodManager) mcontext.getSystemService(Context.INPUT_METHOD_SERVICE);
        im.showSoftInput(view, 0);
    }

    public static void toast(Context mcontext, String text) {
        Toast.makeText(mcontext, text, Toast.LENGTH_SHORT).show();
    }
}
