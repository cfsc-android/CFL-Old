package com.xiandao.android.bean;

import android.content.Context;
import android.content.SharedPreferences;

import com.xiandao.android.AndroidApplication;

import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @类名:SharedPreferencesSava
 * @类描保存共享参数
 * @作Administrator
 * @创建时间:20142下午3:32:13
 * @修改
 * @修改时间:
 * @修改备注:
 * @版本:
 */
public class SharedPreferencesSave {
    private static SharedPreferencesSave instance = null;

    public static SharedPreferencesSave getInstance() {
        if (instance == null) {
            instance = new SharedPreferencesSave();
        }
        return instance;
    }

    /**
     * @param context
     * @param spName
     * @param key
     * @param ob
     * @方法说明:保存对象
     */
    @SuppressWarnings("finally")
    public boolean saveObject(Context context, String spName, String key,
                              Object ob) {
        if (ob == null) {
            return false;
        }
        boolean falg = false;
        SharedPreferences preferences = AndroidApplication.getAppContext()
                .getSharedPreferences(spName, context.MODE_PRIVATE);
        // 创建字节输出
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try {
            // 创建对象输出流，并封装字节流
            oos = new ObjectOutputStream(baos);
            // 将对象写入字节流
            oos.writeObject(ob);
            // 将字节流编码成base64的字符窜

            String oAuth_Base64 = new String(Base64.encodeBase64(baos
                    .toByteArray()));
            falg = preferences.edit().putString(key, oAuth_Base64).commit();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (oos != null) {
                    oos.close();
                }

                if (baos != null) {
                    baos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return falg;
        }
    }

    /**
     * 调用apache的解码方法
     */
    public static byte[] getdeBASE64inCodec(String s) {
        if (s == null)
            return null;
        return new Base64().decode(s.getBytes());
    }

    /**
     * @param context
     * @param spName
     * @param key
     * @return
     * @方法说明:获取存储的对 * @方法名称:getObject
     * @返回Object
     */
    public Object getObject(Context context, String spName, String key) {
        Object ob = null;
        SharedPreferences preferences = context.getSharedPreferences(spName,
                context.MODE_PRIVATE);
        String productBase64 = preferences.getString(key, "");
        // 读取字节
        byte[] base64 = Base64.decodeBase64(productBase64.getBytes());
        // 封装到字节流
        ByteArrayInputStream bais = new ByteArrayInputStream(base64);
        ObjectInputStream bis = null;
        try {
            // 再次封装
            bis = new ObjectInputStream(bais);
            // 读取对象
            ob = bis.readObject();
        } catch (Exception e) {
            // e.printStackTrace();
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }

                if (bais != null) {
                    bais.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ob;
    }

    public void removeAct(Context context, String spName, String key) {
        SharedPreferences preferences = context.getSharedPreferences(spName,
                context.MODE_PRIVATE);
        preferences.edit().remove(key);
        preferences.edit().clear().commit();
    }

    /**
     * @param context
     * @param spName
     * @param key
     * @param value
     * @方法说明:存储int数据
     */
    public void saveIntValue(Context context, String spName, String key,
                             int value) {
        SharedPreferences preferences = context.getSharedPreferences(spName,
                context.MODE_PRIVATE);
        preferences.edit().putInt(key, value).commit();

    }

    /**
     * @param context
     * @param spName
     * @param key
     * @return
     * @方法说明:获取int数据
     * @方法名称:getIntValue
     * @返回int
     */
    public int getIntValue(Context context, String spName, String key) {
        return getIntValue(context, spName, key, 0);
    }

    /**
     * @param context
     * @param spName
     * @param key
     * @param defaultValue
     * @return
     * @方法说明:获取int数据
     * @方法名称:getIntValue
     * @返回int
     */
    public int getIntValue(Context context, String spName, String key,
                           int defaultValue) {
        SharedPreferences preferences = context.getSharedPreferences(spName,
                context.MODE_PRIVATE);
        return preferences.getInt(key, defaultValue);
    }

    /**
     * @param context
     * @param spName
     * @param key
     * @param value
     * @方法说明:存储float数据
     */
    public void saveFloatValue(Context context, String spName, String key,
                               float value) {
        SharedPreferences preferences = context.getSharedPreferences(spName,
                context.MODE_PRIVATE);
        preferences.edit().putFloat(key, value).commit();

    }

    /**
     * @param context
     * @param spName
     * @param key
     * @return
     * @方法说明:获取float数据
     * @方法名称:getFloatValue
     * @返回float
     */
    public float getFloatValue(Context context, String spName, String key) {
        SharedPreferences preferences = context.getSharedPreferences(spName,
                context.MODE_PRIVATE);
        return preferences.getFloat(key, 0);
    }

    /**
     * @param context
     * @param spName
     * @param key
     * @param value
     * @方法说明:存储boolean数据
     * @方法名称:savaBooleanValue
     * @返回void
     */
    public void saveBooleanValue(Context context, String spName, String key,
                                 boolean value) {

        SharedPreferences preferences = context.getSharedPreferences(spName,
                context.MODE_PRIVATE);
        preferences.edit().putBoolean(key, value).commit();

    }

    /**
     * @param context
     * @param spName
     * @param key
     * @return
     * @方法说明:获取boolean数据
     * @方法名称:getBooleanValue
     * @返回boolean
     */
    public boolean getBooleanValue(Context context, String spName, String key) {
        SharedPreferences preferences = context.getSharedPreferences(spName,
                context.MODE_PRIVATE);
        return preferences.getBoolean(key, false);
    }

    /**
     * @param context
     * @param spName
     * @param key
     * @param isDefault
     * @return
     * @方法说明:获取boolean数据
     * @方法名称:getBooleanValue
     * @返回boolean
     */
    public boolean getBooleanValue(Context context, String spName, String key,
                                   boolean isDefault) {
        SharedPreferences preferences = context.getSharedPreferences(spName,
                context.MODE_PRIVATE);
        return preferences.getBoolean(key, isDefault);
    }

    /**
     * @param context
     * @param spName
     * @param key
     * @param value
     * @方法说明:存储long数据
     * @方法名称:savaLongValue
     * @返回void
     */
    public void saveLongValue(Context context, String spName, String key,
                              long value) {

        SharedPreferences preferences = context.getSharedPreferences(spName,
                context.MODE_PRIVATE);

        preferences.edit().putLong(key, value).commit();
    }

    /**
     * @param context
     * @param spName
     * @param key
     * @return
     * @方法说明:获取long数据
     * @方法名称:getLongValue
     * @返回long
     */
    public long getLongValue(Context context, String spName, String key) {
        SharedPreferences preferences = context.getSharedPreferences(spName,
                context.MODE_PRIVATE);
        return preferences.getLong(key, 0);
    }

    /**
     * @param context
     * @param spName
     * @param key
     * @param value
     * @方法说明:存储String数据
     * @方法名称:savaStringValue
     * @返回void
     */
    public void saveStringValue(Context context, String spName, String key,
                                String value) {
        SharedPreferences preferences = context.getSharedPreferences(spName,
                context.MODE_PRIVATE);
        preferences.edit().putString(key, value).commit();
    }

    /**
     * @param context
     * @param spName
     * @param key
     * @return
     * @方法说明:获取String数据
     * @方法名称:getStringValue
     * @返回String
     */
    public String getStringValue(Context context, String spName, String key) {
        SharedPreferences preferences = context.getSharedPreferences(spName,
                context.MODE_PRIVATE);
        return preferences.getString(key, "");
    }

    /**
     * 保存是否是第一次进入应用
     *
     * @param context
     * @param value
     */
    public void saveFirstInFlag(Context context, boolean value) {
        SharedPreferences preferences = context.getSharedPreferences("firstInFlag",
                context.MODE_PRIVATE);
        preferences.edit().putBoolean("flag", value).commit();
    }

    /**
     * 获取是否是第一次进入应用
     *
     * @param context
     * @return
     */
    public boolean getFirstInFlag(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("firstInFlag",
                context.MODE_PRIVATE);
        return preferences.getBoolean("flag", true);
    }

}
