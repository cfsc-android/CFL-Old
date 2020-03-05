package com.xiandao.android.base;


import com.xiandao.android.BuildConfig;

/**
 * Created by Loong on 2020/2/6.
 * Version: 1.0
 * Describe: 基础配置
 */
public class Config {

    public static final String BASE_URL= BuildConfig.BASE_URL;//后台服务地址

    public static final String ENV= BuildConfig.ENV;//当前运行环境

    public static final String SD_APP_DIR_NAME = "CFL"; //存储程序在外部SD卡上的根目录的名字
    public static final String PHOTO_DIR_NAME = "photo";    //存储照片在根目录下的文件夹名字

    public static final String AUTH=ENV.equals("debug")?"":"api-auth/";//鉴权
    public static final String USER=ENV.equals("debug")?"":"api-user/";//用户
    public static final String FILE=ENV.equals("debug")?"":"file-manager-ms/";//文件
    public static final String SMS=ENV.equals("debug")?"":"sms-manager-ms/";//验证
    public static final String ARTICLE=ENV.equals("debug")?"":"smart-content-ms/";//文章
    public static final String IOT=ENV.equals("debug")?"":"smart-iot-ms/";//物联
    public static final String WORKORDER=ENV.equals("debug")?"":"smart-workorder-ms/";//流程
    public static final String BASIC=ENV.equals("debug")?"":"smart-basic-ms/";//小区信息

}