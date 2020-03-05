package com.xiandao.android.utils;


import android.content.Context;

import com.xiandao.android.AndroidApplication;
import com.xiandao.android.bean.SharedPreferencesSave;
import com.xiandao.android.entity.DeviceInfoEntity;
import com.xiandao.android.entity.HikUser;
import com.xiandao.android.entity.LoginUserEntity;
import com.xiandao.android.entity.smart.OrderStatusEntity;
import com.xiandao.android.entity.smart.OrderTypeEntity;
import com.xiandao.android.entity.ProjectInfo;
import com.xiandao.android.entity.QQLoginEntity;
import com.xiandao.android.entity.RoomInfoEntity;
import com.xiandao.android.entity.ThirdInfoEntity;
import com.xiandao.android.entity.WeiXinLoginEntity;
import com.xiandao.android.entity.hikcloud.AccessToken;
import com.xiandao.android.entity.smart.TokenEntity;
import com.xiandao.android.entity.smart.UserInfoEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 此类描述的是:文件存储管理
 *
 * @author TanYong
 *         create at 2017/4/21 9:44
 */
public class FileManagement {

    public static void setCustomerProject(ProjectInfo projectInfo){
        ArrayList<ProjectInfo> list=getCustomerProjects();
        if(list==null){
            list=new ArrayList<>();
        }
        list.add(projectInfo);
        SharedPreferencesSave.getInstance().saveObject(AndroidApplication.getAppContext(),
                "customerProject", "customerProject", list);
    }

    public static ArrayList<ProjectInfo> getCustomerProjects(){
        return (ArrayList<ProjectInfo>) SharedPreferencesSave.getInstance().getObject(AndroidApplication.getAppContext(),
                "customerProject", "customerProject");
    }

    public static void removeCustomerProject(ArrayList<Integer> deleteList){
        ArrayList<ProjectInfo> temp=new ArrayList<>();
        ArrayList<ProjectInfo> list=getCustomerProjects();
        for (int i = 0; i < list.size(); i++) {
            if(!deleteList.contains(i+2)){
                temp.add(list.get(i));
            }
        }
        SharedPreferencesSave.getInstance().saveObject(AndroidApplication.getAppContext(),
                "customerProject", "customerProject", temp);
    }

    public static void setProjectInfo(ProjectInfo projectInfo){
        SharedPreferencesSave.getInstance().saveObject(AndroidApplication.getAppContext(),
                "projectInfo", "projectInfo", projectInfo);
    }

    public static ProjectInfo getProjectInfo(){
        return (ProjectInfo) SharedPreferencesSave.getInstance().getObject(AndroidApplication.getAppContext(),
                "projectInfo", "projectInfo");
    }

    public static void setThirdInfo(ArrayList<ThirdInfoEntity> thirdInfo){
        SharedPreferencesSave.getInstance().saveObject(AndroidApplication.getAppContext(),
                "thirdInfo", "thirdInfo", thirdInfo);
    }

    public static ArrayList<ThirdInfoEntity> getThirdInfo(){
        return (ArrayList<ThirdInfoEntity>) SharedPreferencesSave.getInstance().getObject(AndroidApplication.getAppContext(),
                "thirdInfo", "thirdInfo");
    }


    public static void setLoginType(String type){
        SharedPreferencesSave.getInstance().saveStringValue(AndroidApplication.getAppContext(),
                "login_type", "loginType", type);
    }

    public static String getLoginType(){
        return SharedPreferencesSave.getInstance().getStringValue(AndroidApplication.getAppContext(),
                "login_type", "loginType");
    }

    public static void setWXLogin(WeiXinLoginEntity wxLogin){
        SharedPreferencesSave.getInstance().saveObject(AndroidApplication.getAppContext(),
                "wx_login", "wxLogin", wxLogin);
    }

    public static WeiXinLoginEntity getWXLogin(){
        return (WeiXinLoginEntity) SharedPreferencesSave.getInstance().getObject(AndroidApplication.getAppContext(),
                "wx_login", "wxLogin");
    }

    public static void setQQLogin(QQLoginEntity qqLogin){
        SharedPreferencesSave.getInstance().saveObject(AndroidApplication.getAppContext(),
                "qq_login", "qqLogin", qqLogin);
    }

    public static QQLoginEntity getQQLogin(){
        return (QQLoginEntity) SharedPreferencesSave.getInstance().getObject(AndroidApplication.getAppContext(),
                "qq_login", "qqLogin");
    }

    public static void setParkIndexCode(String parkIndexCode){
        SharedPreferencesSave.getInstance().saveStringValue(AndroidApplication.getAppContext(),
                "park_index_code", "parkIndexCode", parkIndexCode);
    }

    public static String getParkIndexCode(){
        return SharedPreferencesSave.getInstance().getStringValue(AndroidApplication.getAppContext(),
                "park_index_code", "parkIndexCode");
    }

    public static void setDeviceInfo(ArrayList<DeviceInfoEntity> deviceInfo){
        SharedPreferencesSave.getInstance().saveObject(AndroidApplication.getAppContext(),
                "deviceInfo", "deviceInfo", deviceInfo);
    }

    public static ArrayList<DeviceInfoEntity> getDeviceInfo(){
        return (ArrayList<DeviceInfoEntity>) SharedPreferencesSave.getInstance().getObject(AndroidApplication.getAppContext(),
                "deviceInfo", "deviceInfo");
    }

    public static void setNoticeFlag(String flag){
        SharedPreferencesSave.getInstance().saveStringValue(
                AndroidApplication.getAppContext(), "notice_flag",
                "noticeFlag", flag);
    }

    public static String getNoticeFlag(){
        String noticeFlag= SharedPreferencesSave.getInstance().getStringValue(
                AndroidApplication.getAppContext(),"notice_flag","noticeFlag"
        );

        return noticeFlag;
    }

    public static void setCurrentIp(String ip){
        SharedPreferencesSave.getInstance().saveStringValue(
                AndroidApplication.getAppContext(), "ip",
                "IP", ip);
    }

    public static String getCurrentIp(){
        String ip= SharedPreferencesSave.getInstance().getStringValue(
                AndroidApplication.getAppContext(),"ip","IP"
        );

        return ip;
    }

    public static void setHikToken(String token){
        SharedPreferencesSave.getInstance().saveStringValue(
                AndroidApplication.getAppContext(), "hik_token",
                "HikToken", token);
    }
    public static String getHikToken(){
        String token= SharedPreferencesSave.getInstance().getStringValue(
                AndroidApplication.getAppContext(),"hik_token","HikToken"
        );

        return token;
    }

    public static void setAccessToken(AccessToken accessToken){
        SharedPreferencesSave.getInstance().saveObject(
                AndroidApplication.getAppContext(), "access_token",
                "AccessToken", accessToken);
    }

    public static AccessToken getAccessToken(){
        AccessToken accessToken= (AccessToken) SharedPreferencesSave.getInstance().getObject(
                AndroidApplication.getAppContext(),"access_token","AccessToken"
        );

        return accessToken;
    }

    public static void setBaseUser(LoginUserEntity loginUserEntity) {
        SharedPreferencesSave.getInstance().saveObject(
                AndroidApplication.getAppContext(), "loginUserEntity_key",
                "LoginUserEntity", loginUserEntity);
    }

    public static void setHikUser(HikUser hikUser){
        SharedPreferencesSave.getInstance().saveObject(
                AndroidApplication.getAppContext(), "hik_user", "HikUser",hikUser
        );
    }

    public static HikUser getHikUser(){
        HikUser u= (HikUser) SharedPreferencesSave.getInstance().getObject(
                AndroidApplication.getAppContext(),"hik_user","HikUser"
        );
        return u;
    }

    /**
     * @return 获取文件用户数据
     */
    public static LoginUserEntity getLoginUserEntity() {
        LoginUserEntity u = (LoginUserEntity) SharedPreferencesSave.getInstance().getObject(
                AndroidApplication.getAppContext(), "loginUserEntity_key",
                "LoginUserEntity");
        return u;
    }

    public static void removeObj(Context context) {
        SharedPreferencesSave.getInstance().removeAct(context, "loginUserEntity_key", "getLoginUserEntity");
    }

    public static void saveServerTime(long time) {
        long lTime = time - System.currentTimeMillis();
        SharedPreferencesSave.getInstance().saveLongValue(AndroidApplication.getAppContext(), "ConfigurationVariable", "getServerTime", lTime);
    }

    public static long getServerTime() {
        return SharedPreferencesSave.getInstance().getLongValue(AndroidApplication.getAppContext(), "ConfigurationVariable",
                "getServerTime");
    }

    public static void saveTokenInfo(String tokenInfo) {
        SharedPreferencesSave.getInstance().saveStringValue(AndroidApplication.getAppContext(), "tokenInfo", "getTokenInfo", tokenInfo);
    }

    public static String getTokenInfo() {
        return SharedPreferencesSave.getInstance().getStringValue(AndroidApplication.getAppContext(), "tokenInfo", "getTokenInfo");
    }
    public static void saveIsFromShop(boolean isFromShop) {
        SharedPreferencesSave.getInstance().saveBooleanValue(AndroidApplication.getAppContext(), "isFromShop", "isFromShop", isFromShop);
    }

    public static boolean getIsFromShop() {
        return SharedPreferencesSave.getInstance().getBooleanValue(AndroidApplication.getAppContext(), "isFromShop", "isFromShop");
    }

    public static void saveRoomInfo(ArrayList<RoomInfoEntity> roomInfo) {
        SharedPreferencesSave.getInstance().saveObject(AndroidApplication.getAppContext(), "roomInfo", "roomInfo", roomInfo);
    }

    public static ArrayList<RoomInfoEntity> getRoomInfo() {
        return (ArrayList<RoomInfoEntity>) SharedPreferencesSave.getInstance().getObject(AndroidApplication.getAppContext(), "roomInfo", "roomInfo");
    }

    /**
     * @author TanYong
     * create at 2017/6/14 21:19
     * TODO：保存极光别名
     */
    public static void saveJpushAlias(String alias) {
        SharedPreferencesSave.getInstance().saveStringValue(AndroidApplication.getAppContext(), "jpushAlias", "jpushAlias", alias);
    }

    /**
     * @author TanYong
     * create at 2017/6/14 21:20
     * TODO：获取极光别名
     */
    public static String getJpushAlias() {
        return SharedPreferencesSave.getInstance().getStringValue(AndroidApplication.getAppContext(), "jpushAlias", "jpushAlias");
    }

    /**
     * @author TanYong
     * create at 2017/6/14 21:19
     * TODO：保存极光标签
     */
    public static void saveJpushTags(String tags) {
        SharedPreferencesSave.getInstance().saveStringValue(AndroidApplication.getAppContext(), "jpushTags", "jpushTags", tags);
    }

    /**
     * @author TanYong
     * create at 2017/6/14 21:20
     * TODO：获取极光标签
     */
    public static String getJpushTags() {
        return SharedPreferencesSave.getInstance().getStringValue(AndroidApplication.getAppContext(), "jpushTags", "jpushTags");
    }


    public static void setComplainType(List<OrderTypeEntity> list){
        SharedPreferencesSave.getInstance().saveObject(AndroidApplication.getAppContext(),"cfl","complainType",list);
    }

    public static List<OrderTypeEntity> getComplainType(){
        return (List<OrderTypeEntity>) SharedPreferencesSave.getInstance().getObject(AndroidApplication.getAppContext(),"cfl","complainType");
    }

    public static void setOrderType(List<OrderTypeEntity> list){
        SharedPreferencesSave.getInstance().saveObject(AndroidApplication.getAppContext(),"cfl","orderType",list);
    }


    public static void setComplainStatus(List<OrderStatusEntity> list){
        SharedPreferencesSave.getInstance().saveObject(AndroidApplication.getAppContext(),"cfl","complainStatus",list);
    }

    public static List<OrderStatusEntity> getComplainStatus(){
        return (List<OrderStatusEntity>) SharedPreferencesSave.getInstance().getObject(AndroidApplication.getAppContext(),"cfl","complainStatus");
    }

    public static List<OrderTypeEntity> getOrderType(){
        return (List<OrderTypeEntity>) SharedPreferencesSave.getInstance().getObject(AndroidApplication.getAppContext(),"cfl","orderType");
    }
    public static void setOrderStatus(List<OrderStatusEntity> list){
        SharedPreferencesSave.getInstance().saveObject(AndroidApplication.getAppContext(),"cfl","orderStatus",list);
    }

    public static List<OrderStatusEntity> getOrderStatus(){
        return (List<OrderStatusEntity>) SharedPreferencesSave.getInstance().getObject(AndroidApplication.getAppContext(),"cfl","orderStatus");
    }

    public static void setUserInfo(UserInfoEntity userInfo){
        SharedPreferencesSave.getInstance().saveObject(AndroidApplication.getAppContext(),"cfl","userInfo",userInfo);
    }

    public static UserInfoEntity getUserInfoEntity(){
        return (UserInfoEntity) SharedPreferencesSave.getInstance().getObject(AndroidApplication.getAppContext(),"cfl","userInfo");
    }

    public static void setTokenEntity(TokenEntity token){
        SharedPreferencesSave.getInstance().saveObject(AndroidApplication.getAppContext(),"cfl","token",token);
    }
    public static TokenEntity getTokenEntity(){
        return (TokenEntity) SharedPreferencesSave.getInstance().getObject(AndroidApplication.getAppContext(),"cfl","token");
    }

    public static void setPhone(String phone){
        SharedPreferencesSave.getInstance().saveStringValue(AndroidApplication.getAppContext(),"cfl","phone",phone);
    }
    public static String getPhone(){
        return SharedPreferencesSave.getInstance().getStringValue(AndroidApplication.getAppContext(),"cfl","phone");
    }
}
