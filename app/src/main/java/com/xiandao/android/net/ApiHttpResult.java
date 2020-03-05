package com.xiandao.android.net;

import android.content.Context;

import com.xiandao.android.bean.ParametBean;
import com.xiandao.android.httptask.AcceptPriceTask;
import com.xiandao.android.httptask.ComplainInfoConfirmResultTask;
import com.xiandao.android.httptask.CreateOrderResultTask;
import com.xiandao.android.httptask.CreatePayInfoTask;
import com.xiandao.android.httptask.DoThumbsUpTask;
import com.xiandao.android.httptask.GetPointNumberTask;
import com.xiandao.android.httptask.GetReadNumberTask;
import com.xiandao.android.httptask.GetRepairsAddressForScanTask;
import com.xiandao.android.httptask.GetThemeDetailsTask;
import com.xiandao.android.httptask.GetThemeListTask;
import com.xiandao.android.httptask.GetWXPayParamsTask;
import com.xiandao.android.httptask.HikAlarmAdditionTask;
import com.xiandao.android.httptask.HikAlarmCarTask;
import com.xiandao.android.httptask.HikBaseTask;
import com.xiandao.android.httptask.HikCarChargeInfoTask;
import com.xiandao.android.httptask.HikCarCrossRecordTask;
import com.xiandao.android.httptask.HikCarListTask;
import com.xiandao.android.httptask.HikCardListTask;
import com.xiandao.android.httptask.HikNullTask;
import com.xiandao.android.httptask.HikParkListTask;
import com.xiandao.android.httptask.HikParkingPaymentTask;
import com.xiandao.android.httptask.HikUserTask;
import com.xiandao.android.httptask.IsAcceptSolutionComplainTask;
import com.xiandao.android.httptask.CommentWorkOrderTask;
import com.xiandao.android.httptask.ComplainInfoCommitTask;
import com.xiandao.android.httptask.ConfirmWorkOrderFinishTask;
import com.xiandao.android.httptask.CreateCommonAddressTask;
import com.xiandao.android.httptask.CreateLinkmanTask;
import com.xiandao.android.httptask.EditPersonalInfomationTask;
import com.xiandao.android.httptask.GetCommonAddressListTask;
import com.xiandao.android.httptask.GetComplainDetailTask;
import com.xiandao.android.httptask.GetComplainTypeTask;
import com.xiandao.android.httptask.GetHomeInformTask;
import com.xiandao.android.httptask.GetLinkmanListTask;
import com.xiandao.android.httptask.GetRepairsAddressTask;
import com.xiandao.android.httptask.GetRepairsDetailTask;
import com.xiandao.android.httptask.GetRepairsTypeTask;
import com.xiandao.android.httptask.LoginTask;
import com.xiandao.android.httptask.LogoutTask;
import com.xiandao.android.httptask.NewVisitorTask;
import com.xiandao.android.httptask.QueryGoodsListTask;
import com.xiandao.android.httptask.QueryInformListTask;
import com.xiandao.android.httptask.QueryLifePayTask;
import com.xiandao.android.httptask.QueryMyComplainListTask;
import com.xiandao.android.httptask.QueryMyComplainPlanTask;
import com.xiandao.android.httptask.QueryMyOrderTask;
import com.xiandao.android.httptask.QueryMyRepairsPlanTask;
import com.xiandao.android.httptask.QueryNoticeListTask;
import com.xiandao.android.httptask.QueryRepairsListTask;
import com.xiandao.android.httptask.QueryRoomsTask;
import com.xiandao.android.httptask.QueryShopCommentListTask;
import com.xiandao.android.httptask.QueryShopHomeTask;
import com.xiandao.android.httptask.QueryShopcarListTask;
import com.xiandao.android.httptask.QueryStoreListTask;
import com.xiandao.android.httptask.RepairsInforCommitTask;
import com.xiandao.android.httptask.SendSMSTask;
import com.xiandao.android.httptask.ServerTimeTask;
import com.xiandao.android.utils.Constants;

import java.util.Map;


/**
 * 此类描述的是:请求接口公共类
 *
 * @author TanYong
 *         create at 2017/4/21 9:19
 */
public class ApiHttpResult {

    /**
     * 获取服务器时间
     */
    public static void serverTimeResult(Context context, String[] parm, Object[] objects, HttpUtils.DataCallBack callback) {
        /**
         * 初始化请求参数 Parameters: requestUrl 请求根url context 上下文 requestDataMap 请求参数
         * BaseParamsMapUtil jsonParser 解析工具类 BaseParser.java 继承自这个类
         */
        RequestVo commentRequest = new RequestVo(Constants.SERVICETIME, context, ParametBean.getParameter(parm, objects), new ServerTimeTask());
        new HttpUtils(context).getServerForResult(callback, commentRequest);
    }

    /**
     * 获取验证码
     */
    public static void sendSMS(Context context, String[] parm, Object[] objects, HttpUtils.DataCallBack callback) {
        /**
         * 初始化请求参数 Parameters: requestUrl 请求根url context 上下文 requestDataMap 请求参数
         * BaseParamsMapUtil jsonParser 解析工具类 BaseParser.java 继承自这个类
         */
        RequestVo voLogin = new RequestVo(Constants.SMSSEND, context, ParametBean.getParameter(parm, objects), new SendSMSTask());
        new HttpUtils(context).getServerForResult(callback, voLogin);
    }

    /**
     * 登录
     */
    public static void platLoginResult(Context context, String[] parm, Object[] objects, HttpUtils.DataCallBack callback) {
        /**
         * 初始化请求参数 Parameters: requestUrl 请求根url context 上下文 requestDataMap 请求参数
         * BaseParamsMapUtil jsonParser 解析工具类 BaseParser.java 继承自这个类
         */
        RequestVo voLogin = new RequestVo(Constants.USERLOGIN, context, ParametBean.getParameter(parm, objects), new LoginTask());
        new HttpUtils(context).getServerForResult(callback, voLogin);
    }

    public static void platCheckOwner(Context context, String[] parm, Object[] objects, HttpUtils.DataCallBack callback){
        RequestVo voLogin = new RequestVo(Constants.CHECKOWNER, context, ParametBean.getParameter(parm, objects), new LoginTask());
        new HttpUtils(context).getServerForResult(callback, voLogin);
    }

    /**
     * 退出登录
     */
    public static void platLogout(Context context, String[] parm, Object[] objects, HttpUtils.DataCallBack callback) {
        /**
         * 初始化请求参数 Parameters: requestUrl 请求根url context 上下文 requestDataMap 请求参数
         * BaseParamsMapUtil jsonParser 解析工具类 BaseParser.java 继承自这个类
         */
        RequestVo voLogin = new RequestVo(Constants.LOGOUT, context, ParametBean.getParameter(parm, objects), new LogoutTask());
        new HttpUtils(context).getServerForResult(callback, voLogin);
    }

    /**
     * 报修信息提交
     */
    public static void repairsInfoCommit(Context context, String[] parm, Object[] objects, HttpUtils.DataCallBack callback) {
        /**
         * 初始化请求参数 Parameters: requestUrl 请求根url context 上下文 requestDataMap 请求参数
         * BaseParamsMapUtil jsonParser 解析工具类 BaseParser.java 继承自这个类
         */
        RequestVo voLogin = new RequestVo(Constants.REPAIRSINFOCOMMIT, context, ParametBean.getParameter(parm, objects), new RepairsInforCommitTask());
        new HttpUtils(context).getServerForResult(callback, voLogin);
    }

    /**
     * 获取报修地址
     */
    public static void getRepairsAddress(Context context, String[] parm, Object[] objects, HttpUtils.DataCallBack callback) {
        /**
         * 初始化请求参数 Parameters: requestUrl 请求根url context 上下文 requestDataMap 请求参数
         * BaseParamsMapUtil jsonParser 解析工具类 BaseParser.java 继承自这个类
         */
        RequestVo voLogin = new RequestVo(Constants.GETREPAIRSADDRESS, context, ParametBean.getParameter(parm, objects), new GetRepairsAddressTask());
        new HttpUtils(context).getServerForResult(callback, voLogin);
    }

    /**
     * 通过二维码获取维修地址
     */
    public static void getRepairsAddressForScan(Context context, String[] parm, Object[] objects, HttpUtils.DataCallBack callback) {
        /**
         * 初始化请求参数 Parameters: requestUrl 请求根url context 上下文 requestDataMap 请求参数
         * BaseParamsMapUtil jsonParser 解析工具类 BaseParser.java 继承自这个类
         */
        RequestVo voLogin = new RequestVo(Constants.REPAIRSCODE, context, ParametBean.getParameter(parm, objects), new GetRepairsAddressForScanTask());
        new HttpUtils(context).getServerForResult(callback, voLogin);
    }

    /**
     * 获取报修类别
     */
    public static void getRepairsType(Context context, String[] parm, Object[] objects, HttpUtils.DataCallBack callback) {
        /**
         * 初始化请求参数 Parameters: requestUrl 请求根url context 上下文 requestDataMap 请求参数
         * BaseParamsMapUtil jsonParser 解析工具类 BaseParser.java 继承自这个类
         */
        RequestVo voLogin = new RequestVo(Constants.GETREPAIRSTYPE, context, ParametBean.getParameter(parm, objects), new GetRepairsTypeTask());
        new HttpUtils(context).getServerForResult(callback, voLogin);
    }

    /**
     * 获取首页通知
     */
    public static void getHomeInform(Context context, String[] parm, Object[] objects, HttpUtils.DataCallBack callback) {
        /**
         * 初始化请求参数 Parameters: requestUrl 请求根url context 上下文 requestDataMap 请求参数
         * BaseParamsMapUtil jsonParser 解析工具类 BaseParser.java 继承自这个类
         */
        RequestVo voLogin = new RequestVo(Constants.GETHOMEINFORM, context, ParametBean.getParameter(parm, objects), new GetHomeInformTask());
        new HttpUtils(context).getServerForResult(callback, voLogin);
    }

    /**
     * 获取投诉类型
     */
    public static void getComplainType(Context context, String[] parm, Object[] objects, HttpUtils.DataCallBack callback) {
        /**
         * 初始化请求参数 Parameters: requestUrl 请求根url context 上下文 requestDataMap 请求参数
         * BaseParamsMapUtil jsonParser 解析工具类 BaseParser.java 继承自这个类
         */
        RequestVo voLogin = new RequestVo(Constants.GETCOMPLAINTYPE, context, ParametBean.getParameter(parm, objects), new GetComplainTypeTask());
        new HttpUtils(context).getServerForResult(callback, voLogin);
    }

    /**
     * 投诉信息提交
     */
    public static void complainInfoCommit(Context context, String[] parm, Object[] objects, HttpUtils.DataCallBack callback) {
        /**
         * 初始化请求参数 Parameters: requestUrl 请求根url context 上下文 requestDataMap 请求参数
         * BaseParamsMapUtil jsonParser 解析工具类 BaseParser.java 继承自这个类
         */
        RequestVo voLogin = new RequestVo(Constants.COMPLAININFOCOMMIT, context, ParametBean.getParameter(parm, objects), new ComplainInfoCommitTask());
        new HttpUtils(context).getServerForResult(callback, voLogin);
    }

    /**
     * 获取公告列表
     */
    public static void queryNoticeList(Context context, String[] parm, Object[] objects, HttpUtils.DataCallBack callback) {
        /**
         * 初始化请求参数 Parameters: requestUrl 请求根url context 上下文 requestDataMap 请求参数
         * BaseParamsMapUtil jsonParser 解析工具类 BaseParser.java 继承自这个类
         */
        RequestVo voLogin = new RequestVo(Constants.QUERYNOTICELIST, context, ParametBean.getParameter(parm, objects), new QueryNoticeListTask());
        new HttpUtils(context).getServerForResult(callback, voLogin);
    }

    /**
     * 获取公告列表
     */
    public static void queryTurnsAndHottopics(Context context, String[] parm, Object[] objects, HttpUtils.DataCallBack callback) {
        /**
         * 初始化请求参数 Parameters: requestUrl 请求根url context 上下文 requestDataMap 请求参数
         * BaseParamsMapUtil jsonParser 解析工具类 BaseParser.java 继承自这个类
         */
        RequestVo voLogin = new RequestVo(Constants.QUERYTURNSANDHOTTOPICS, context, ParametBean.getParameter(parm, objects), new QueryNoticeListTask());
        new HttpUtils(context).getServerForResult(callback, voLogin);
    }

    /**
     * 获取通知列表
     */
    public static void queryInformList(Context context, String[] parm, Object[] objects, HttpUtils.DataCallBack callback) {
        /**
         * 初始化请求参数 Parameters: requestUrl 请求根url context 上下文 requestDataMap 请求参数
         * BaseParamsMapUtil jsonParser 解析工具类 BaseParser.java 继承自这个类
         */
        RequestVo voLogin = new RequestVo(Constants.QUERYINFORMLIST, context, ParametBean.getParameter(parm, objects), new QueryInformListTask());
        new HttpUtils(context).getServerForResult(callback, voLogin);
    }

    /**
     * 获取商店列表
     */
    public static void queryStoreList(Context context, String[] parm, Object[] objects, HttpUtils.DataCallBack callback) {
        /**
         * 初始化请求参数 Parameters: requestUrl 请求根url context 上下文 requestDataMap 请求参数
         * BaseParamsMapUtil jsonParser 解析工具类 BaseParser.java 继承自这个类
         */
        RequestVo voLogin = new RequestVo(Constants.QUERYSTORELIST, context, ParametBean.getParameter(parm, objects), new QueryStoreListTask());
        new HttpUtils(context).getServerForResult(callback, voLogin);
    }

    /**
     * 获取购物车列表
     */
    public static void queryShopcarList(Context context, String[] parm, Object[] objects, HttpUtils.DataCallBack callback) {
        /**
         * 初始化请求参数 Parameters: requestUrl 请求根url context 上下文 requestDataMap 请求参数
         * BaseParamsMapUtil jsonParser 解析工具类 BaseParser.java 继承自这个类
         */
        RequestVo voLogin = new RequestVo(Constants.QUERYSHOPCARLIST, context, ParametBean.getParameter(parm, objects), new QueryShopcarListTask());
        new HttpUtils(context).getServerForResult(callback, voLogin);
    }

    /**
     * 获取商品分类
     */
    public static void queryShopHome(Context context, String[] parm, Object[] objects, HttpUtils.DataCallBack callback) {
        /**
         * 初始化请求参数 Parameters: requestUrl 请求根url context 上下文 requestDataMap 请求参数
         * BaseParamsMapUtil jsonParser 解析工具类 BaseParser.java 继承自这个类
         */
        RequestVo voLogin = new RequestVo(Constants.QUERYSHOPHOME, context, ParametBean.getParameter(parm, objects), new QueryShopHomeTask());
        new HttpUtils(context).getServerForResult(callback, voLogin);
    }

    /**
     * 获取商品列表
     */
    public static void queryGoodsList(Context context, String[] parm, Object[] objects, HttpUtils.DataCallBack callback) {
        /**
         * 初始化请求参数 Parameters: requestUrl 请求根url context 上下文 requestDataMap 请求参数
         * BaseParamsMapUtil jsonParser 解析工具类 BaseParser.java 继承自这个类
         */
        RequestVo voLogin = new RequestVo(Constants.QUERYGOODSLIST, context, ParametBean.getParameter(parm, objects), new QueryGoodsListTask());
        new HttpUtils(context).getServerForResult(callback, voLogin);
    }

    /**
     * 添加到购物车
     */
    public static void addBuyCar(Context context, String[] parm, Object[] objects, HttpUtils.DataCallBack callback) {
        /**
         * 初始化请求参数 Parameters: requestUrl 请求根url context 上下文 requestDataMap 请求参数
         * BaseParamsMapUtil jsonParser 解析工具类 BaseParser.java 继承自这个类
         */
        RequestVo voLogin = new RequestVo(Constants.ADDBUYCAR, context, ParametBean.getParameter(parm, objects), new AcceptPriceTask());
        new HttpUtils(context).getServerForResult(callback, voLogin);
    }

    /**
     * 修改购物车数量
     */
    public static void updateCarNum(Context context, String[] parm, Object[] objects, HttpUtils.DataCallBack callback) {
        /**
         * 初始化请求参数 Parameters: requestUrl 请求根url context 上下文 requestDataMap 请求参数
         * BaseParamsMapUtil jsonParser 解析工具类 BaseParser.java 继承自这个类
         */
        RequestVo voLogin = new RequestVo(Constants.UPDATECARNUM, context, ParametBean.getParameter(parm, objects), new AcceptPriceTask());
        new HttpUtils(context).getServerForResult(callback, voLogin);
    }

    /**
     * 删除购物车
     */
    public static void delCartDetail(Context context, String[] parm, Object[] objects, HttpUtils.DataCallBack callback) {
        /**
         * 初始化请求参数 Parameters: requestUrl 请求根url context 上下文 requestDataMap 请求参数
         * BaseParamsMapUtil jsonParser 解析工具类 BaseParser.java 继承自这个类
         */
        RequestVo voLogin = new RequestVo(Constants.DELETECART, context, ParametBean.getParameter(parm, objects), new AcceptPriceTask());
        new HttpUtils(context).getServerForResult(callback, voLogin);
    }

    /**
     * 提交订单
     */
    public static void commitOrder(Context context, String[] parm, Object[] objects, HttpUtils.DataCallBack callback) {
        /**
         * 初始化请求参数 Parameters: requestUrl 请求根url context 上下文 requestDataMap 请求参数
         * BaseParamsMapUtil jsonParser 解析工具类 BaseParser.java 继承自这个类
         */
        RequestVo voLogin = new RequestVo(Constants.COMMITORDER, context, ParametBean.getParameter(parm, objects), new CreateOrderResultTask());
        new HttpUtils(context).getServerForResult(callback, voLogin);
    }

    /**
     * 查询我的订单
     */
    public static void queryMyOrder(Context context, String[] parm, Object[] objects, HttpUtils.DataCallBack callback) {
        /**
         * 初始化请求参数 Parameters: requestUrl 请求根url context 上下文 requestDataMap 请求参数
         * BaseParamsMapUtil jsonParser 解析工具类 BaseParser.java 继承自这个类
         */
        RequestVo voLogin = new RequestVo(Constants.QUERYMYORDER, context, ParametBean.getParameter(parm, objects), new QueryMyOrderTask());
        new HttpUtils(context).getServerForResult(callback, voLogin);
    }
    /**
     * 获取微信支付参数
     */
    public static void getWXPayParams(Context context, String[] parm, Object[] objects, HttpUtils.DataCallBack callback) {
        /**
         * 初始化请求参数 Parameters: requestUrl 请求根url context 上下文 requestDataMap 请求参数
         * BaseParamsMapUtil jsonParser 解析工具类 BaseParser.java 继承自这个类
         */
        RequestVo voLogin = new RequestVo(Constants.GETWXPAYPARAMS, context, ParametBean.getParameter(parm, objects), new GetWXPayParamsTask());
        new HttpUtils(context).getServerForResult(callback, voLogin);
    }

    /**
     * 取消订单
     */
    public static void updateOrderStatus(Context context, String[] parm, Object[] objects, HttpUtils.DataCallBack callback) {
        /**
         * 初始化请求参数 Parameters: requestUrl 请求根url context 上下文 requestDataMap 请求参数
         * BaseParamsMapUtil jsonParser 解析工具类 BaseParser.java 继承自这个类
         */
        RequestVo voLogin = new RequestVo(Constants.UPDATEORDERSTATUS, context, ParametBean.getParameter(parm, objects), new AcceptPriceTask());
        new HttpUtils(context).getServerForResult(callback, voLogin);
    }

    /**
     * 提交我的收货地址
     */
    public static void commitMyReceivingAddress(Context context, String[] parm, Object[] objects, HttpUtils.DataCallBack callback) {
        /**
         * 初始化请求参数 Parameters: requestUrl 请求根url context 上下文 requestDataMap 请求参数
         * BaseParamsMapUtil jsonParser 解析工具类 BaseParser.java 继承自这个类
         */
        RequestVo voLogin = new RequestVo(Constants.COMMITMYRECEIVINGADDRESS, context, ParametBean.getParameter(parm, objects), new AcceptPriceTask());
        new HttpUtils(context).getServerForResult(callback, voLogin);
    }

    /**
     * 查询商品评价列表
     */
    public static void queryShopCommentList(Context context, String[] parm, Object[] objects, HttpUtils.DataCallBack callback) {
        /**
         * 初始化请求参数 Parameters: requestUrl 请求根url context 上下文 requestDataMap 请求参数
         * BaseParamsMapUtil jsonParser 解析工具类 BaseParser.java 继承自这个类
         */
        RequestVo voLogin = new RequestVo(Constants.QUERYSHOPCOMMENTLIST, context, ParametBean.getParameter(parm, objects), new QueryShopCommentListTask());
        new HttpUtils(context).getServerForResult(callback, voLogin);
    }

    /**
     * 获取缴费信息
     */
    public static void queryLifePay(Context context, String[] parm, Object[] objects, HttpUtils.DataCallBack callback) {
        /**
         * 初始化请求参数 Parameters: requestUrl 请求根url context 上下文 requestDataMap 请求参数
         * BaseParamsMapUtil jsonParser 解析工具类 BaseParser.java 继承自这个类
         */
        RequestVo voLogin = new RequestVo(Constants.LIFEPAYQUERY, context, ParametBean.getParameter(parm, objects), new QueryLifePayTask());
        new HttpUtils(context).getServerForResult(callback, voLogin);
    }

    /**
     * 获取订单信息
     */
    public static void createPayInfo(Context context, String[] parm, Object[] objects, HttpUtils.DataCallBack callback) {
        /**
         * 初始化请求参数 Parameters: requestUrl 请求根url context 上下文 requestDataMap 请求参数
         * BaseParamsMapUtil jsonParser 解析工具类 BaseParser.java 继承自这个类
         */
        RequestVo voLogin = new RequestVo(Constants.CREATEPAYINFO, context, ParametBean.getParameter(parm, objects), new CreatePayInfoTask());
        new HttpUtils(context).getServerForResult(callback, voLogin);
    }

    /**
     * 获取我的投诉列表
     */
    public static void queryMyComplainList(Context context, String[] parm, Object[] objects, HttpUtils.DataCallBack callback) {
        /**
         * 初始化请求参数 Parameters: requestUrl 请求根url context 上下文 requestDataMap 请求参数
         * BaseParamsMapUtil jsonParser 解析工具类 BaseParser.java 继承自这个类
         */
        RequestVo voLogin = new RequestVo(Constants.QUERYMYCOMPLAINLIST, context, ParametBean.getParameter(parm, objects), new QueryMyComplainListTask());
        new HttpUtils(context).getServerForResult(callback, voLogin);
    }

    /**
     * 获取我的投诉详情信息
     */
    public static void getComplainDetail(Context context, String[] parm, Object[] objects, HttpUtils.DataCallBack callback) {
        /**
         * 初始化请求参数 Parameters: requestUrl 请求根url context 上下文 requestDataMap 请求参数
         * BaseParamsMapUtil jsonParser 解析工具类 BaseParser.java 继承自这个类
         */
        RequestVo voLogin = new RequestVo(Constants.GETCOMLAINDETAIL, context, ParametBean.getParameter(parm, objects), new GetComplainDetailTask());
        new HttpUtils(context).getServerForResult(callback, voLogin);
    }

    /**
     * 业主是否接受填写方案
     */
    public static void isAcceptSolutionComplain(Context context, String[] parm, Object[] objects, HttpUtils.DataCallBack callback) {
        /**
         * 初始化请求参数 Parameters: requestUrl 请求根url context 上下文 requestDataMap 请求参数
         * BaseParamsMapUtil jsonParser 解析工具类 BaseParser.java 继承自这个类
         */
        RequestVo voLogin = new RequestVo(Constants.ISACCEPTSOLUTIONCOMPLAIN, context, ParametBean.getParameter(parm, objects), new IsAcceptSolutionComplainTask());
        new HttpUtils(context).getServerForResult(callback, voLogin);
    }

    /**
     * 业主是否满意整改
     */
    public static void confirmResult(Context context, String[] parm, Object[] objects, HttpUtils.DataCallBack callback) {
        /**
         * 初始化请求参数 Parameters: requestUrl 请求根url context 上下文 requestDataMap 请求参数
         * BaseParamsMapUtil jsonParser 解析工具类 BaseParser.java 继承自这个类
         */
        RequestVo voLogin = new RequestVo(Constants.CONFIRMRESULT, context, ParametBean.getParameter(parm, objects), new ComplainInfoConfirmResultTask());
        new HttpUtils(context).getServerForResult(callback, voLogin);
    }

    /**
     * 业主是否接受整改
     */
    public static void customAcceptRefirm(Context context, String[] parm, Object[] objects, HttpUtils.DataCallBack callback) {
        /**
         * 初始化请求参数 Parameters: requestUrl 请求根url context 上下文 requestDataMap 请求参数
         * BaseParamsMapUtil jsonParser 解析工具类 BaseParser.java 继承自这个类
         */
        RequestVo voLogin = new RequestVo(Constants.CUSTOMACCEPTREFIRM, context, ParametBean.getParameter(parm, objects), new ComplainInfoConfirmResultTask());
        new HttpUtils(context).getServerForResult(callback, voLogin);
    }

    /**
     * 获取我的投诉进度
     */
    public static void queryMyComplainPlan(Context context, String[] parm, Object[] objects, HttpUtils.DataCallBack callback) {
        /**
         * 初始化请求参数 Parameters: requestUrl 请求根url context 上下文 requestDataMap 请求参数
         * BaseParamsMapUtil jsonParser 解析工具类 BaseParser.java 继承自这个类
         */
        RequestVo voLogin = new RequestVo(Constants.QUERYMYCOMLAINPLAN, context, ParametBean.getParameter(parm, objects), new QueryMyComplainPlanTask());
        new HttpUtils(context).getServerForResult(callback, voLogin);
    }

    /**
     * 获取我的报修进度
     */
    public static void queryMyRepairsPlan(Context context, String[] parm, Object[] objects, HttpUtils.DataCallBack callback) {
        /**
         * 初始化请求参数 Parameters: requestUrl 请求根url context 上下文 requestDataMap 请求参数
         * BaseParamsMapUtil jsonParser 解析工具类 BaseParser.java 继承自这个类
         */
        RequestVo voLogin = new RequestVo(Constants.QUERYMYREPAIRSPLAN, context, ParametBean.getParameter(parm, objects), new QueryMyRepairsPlanTask());
        new HttpUtils(context).getServerForResult(callback, voLogin);
    }

    /**
     * 业主是否接受费用
     */
    public static void acceptPrice(Context context, String[] parm, Object[] objects, HttpUtils.DataCallBack callback) {
        /**
         * 初始化请求参数 Parameters: requestUrl 请求根url context 上下文 requestDataMap 请求参数
         * BaseParamsMapUtil jsonParser 解析工具类 BaseParser.java 继承自这个类
         */
        RequestVo voLogin = new RequestVo(Constants.ACCEPTPRICE, context, ParametBean.getParameter(parm, objects), new AcceptPriceTask());
        new HttpUtils(context).getServerForResult(callback, voLogin);
    }

    /**
     * 用来判断是去跳转到评价还是支付
     */
    public static void confirmWorkOrderFinish(Context context, String[] parm, Object[] objects, HttpUtils.DataCallBack callback) {
        /**
         * 初始化请求参数 Parameters: requestUrl 请求根url context 上下文 requestDataMap 请求参数
         * BaseParamsMapUtil jsonParser 解析工具类 BaseParser.java 继承自这个类
         */
        RequestVo voLogin = new RequestVo(Constants.CONFIRMWORKORDERFINISH, context, ParametBean.getParameter(parm, objects), new ConfirmWorkOrderFinishTask());
        new HttpUtils(context).getServerForResult(callback, voLogin);
    }

    /**
     * 评价工单
     */
    public static void commentWorkOrder(Context context, String[] parm, Object[] objects, HttpUtils.DataCallBack callback) {
        /**
         * 初始化请求参数 Parameters: requestUrl 请求根url context 上下文 requestDataMap 请求参数
         * BaseParamsMapUtil jsonParser 解析工具类 BaseParser.java 继承自这个类
         */
        RequestVo voLogin = new RequestVo(Constants.COMMENTWORKORDER, context, ParametBean.getParameter(parm, objects), new CommentWorkOrderTask());
        new HttpUtils(context).getServerForResult(callback, voLogin);
    }

    /**
     * 评价投诉
     */
    public static void commentComplain(Context context, String[] parm, Object[] objects, HttpUtils.DataCallBack callback) {
        /**
         * 初始化请求参数 Parameters: requestUrl 请求根url context 上下文 requestDataMap 请求参数
         * BaseParamsMapUtil jsonParser 解析工具类 BaseParser.java 继承自这个类
         */
        RequestVo voLogin = new RequestVo(Constants.COMMENTCOMPLAIN, context, ParametBean.getParameter(parm, objects), new CommentWorkOrderTask());
        new HttpUtils(context).getServerForResult(callback, voLogin);
    }

    /**
     * 评价商品
     */
    public static void commitShopComment(Context context, String[] parm, Object[] objects, HttpUtils.DataCallBack callback) {
        /**
         * 初始化请求参数 Parameters: requestUrl 请求根url context 上下文 requestDataMap 请求参数
         * BaseParamsMapUtil jsonParser 解析工具类 BaseParser.java 继承自这个类
         */
        RequestVo voLogin = new RequestVo(Constants.COMMITCOMMENT, context, ParametBean.getParameter(parm, objects), new CommentWorkOrderTask());
        new HttpUtils(context).getServerForResult(callback, voLogin);
    }

    /**
     * 获取报修列表
     */
    public static void queryRepairsList(Context context, String[] parm, Object[] objects, HttpUtils.DataCallBack callback) {
        /**
         * 初始化请求参数 Parameters: requestUrl 请求根url context 上下文 requestDataMap 请求参数
         * BaseParamsMapUtil jsonParser 解析工具类 BaseParser.java 继承自这个类
         */
        RequestVo voLogin = new RequestVo(Constants.QUERYREPAIRSLIST, context, ParametBean.getParameter(parm, objects), new QueryRepairsListTask());
        new HttpUtils(context).getServerForResult(callback, voLogin);
    }

    /**
     * 获取报修详情
     */
    public static void getRepairsDetail(Context context, String[] parm, Object[] objects, HttpUtils.DataCallBack callback) {
        /**
         * 初始化请求参数 Parameters: requestUrl 请求根url context 上下文 requestDataMap 请求参数
         * BaseParamsMapUtil jsonParser 解析工具类 BaseParser.java 继承自这个类
         */
        RequestVo voLogin = new RequestVo(Constants.GETREPAIRSDETAIL, context, ParametBean.getParameter(parm, objects), new GetRepairsDetailTask());
        new HttpUtils(context).getServerForResult(callback, voLogin);
    }

    /**
     * 新建联系人
     */
    public static void createLinkman(Context context, String[] parm, Object[] objects, HttpUtils.DataCallBack callback) {
        /**
         * 初始化请求参数 Parameters: requestUrl 请求根url context 上下文 requestDataMap 请求参数
         * BaseParamsMapUtil jsonParser 解析工具类 BaseParser.java 继承自这个类
         */
        RequestVo voLogin = new RequestVo(Constants.CREATELINKMAN, context, ParametBean.getParameter(parm, objects), new CreateLinkmanTask());
        new HttpUtils(context).getServerForResult(callback, voLogin);
    }

    /**
     * 编辑个人资料
     */
    public static void editPersonalInformation(Context context, String[] parm, Object[] objects, HttpUtils.DataCallBack callback) {
        /**
         * 初始化请求参数 Parameters: requestUrl 请求根url context 上下文 requestDataMap 请求参数
         * BaseParamsMapUtil jsonParser 解析工具类 BaseParser.java 继承自这个类
         */
        RequestVo voLogin = new RequestVo(Constants.EDITPERSONALINFORMATION, context, ParametBean.getParameter(parm, objects), new EditPersonalInfomationTask());
        new HttpUtils(context).getServerForResult(callback, voLogin);
    }

    /**
     * 获取公告详情阅读数
     */
    public static void getReadNumber(Context context, String[] parm, Object[] objects, HttpUtils.DataCallBack callback) {
        /**
         * 初始化请求参数 Parameters: requestUrl 请求根url context 上下文 requestDataMap 请求参数
         * BaseParamsMapUtil jsonParser 解析工具类 BaseParser.java 继承自这个类
         */
        RequestVo voLogin = new RequestVo(Constants.GETREADNUMBER, context, ParametBean.getParameter(parm, objects), new GetReadNumberTask());
        new HttpUtils(context).getServerForResult(callback, voLogin);
    }

    /**
     * 获取公告详情点赞数
     */
    public static void getPointNumber(Context context, String[] parm, Object[] objects, HttpUtils.DataCallBack callback) {
        /**
         * 初始化请求参数 Parameters: requestUrl 请求根url context 上下文 requestDataMap 请求参数
         * BaseParamsMapUtil jsonParser 解析工具类 BaseParser.java 继承自这个类
         */
        RequestVo voLogin = new RequestVo(Constants.GETPOINTNUMBER, context, ParametBean.getParameter(parm, objects), new GetPointNumberTask());
        new HttpUtils(context).getServerForResult(callback, voLogin);
    }

    /**
     * 获取联系人列表
     */
    public static void getLinkmanList(Context context, String[] parm, Object[] objects, HttpUtils.DataCallBack callback) {
        /**
         * 初始化请求参数 Parameters: requestUrl 请求根url context 上下文 requestDataMap 请求参数
         * BaseParamsMapUtil jsonParser 解析工具类 BaseParser.java 继承自这个类
         */
        RequestVo voLogin = new RequestVo(Constants.GETLINKMANLIST, context, ParametBean.getParameter(parm, objects), new GetLinkmanListTask());
        new HttpUtils(context).getServerForResult(callback, voLogin);
    }

    /**
     * 查询楼盘
     */
    public static void queryProjects(Context context, String[] parm, Object[] objects, HttpUtils.DataCallBack callback) {
        /**
         * 初始化请求参数 Parameters: requestUrl 请求根url context 上下文 requestDataMap 请求参数
         * BaseParamsMapUtil jsonParser 解析工具类 BaseParser.java 继承自这个类
         */
        RequestVo voLogin = new RequestVo(Constants.QUERYPROJECTS, context, ParametBean.getParameter(parm, objects), new QueryRoomsTask());
        new HttpUtils(context).getServerForResult(callback, voLogin);
    }

    /**
     * 查询楼栋
     */
    public static void queryBuildings(Context context, String[] parm, Object[] objects, HttpUtils.DataCallBack callback) {
        /**
         * 初始化请求参数 Parameters: requestUrl 请求根url context 上下文 requestDataMap 请求参数
         * BaseParamsMapUtil jsonParser 解析工具类 BaseParser.java 继承自这个类
         */
        RequestVo voLogin = new RequestVo(Constants.QUERYBUILDINGS, context, ParametBean.getParameter(parm, objects), new QueryRoomsTask());
        new HttpUtils(context).getServerForResult(callback, voLogin);
    }

    /**
     * 查询单元
     */
    public static void queryCells(Context context, String[] parm, Object[] objects, HttpUtils.DataCallBack callback) {
        /**
         * 初始化请求参数 Parameters: requestUrl 请求根url context 上下文 requestDataMap 请求参数
         * BaseParamsMapUtil jsonParser 解析工具类 BaseParser.java 继承自这个类
         */
        RequestVo voLogin = new RequestVo(Constants.QUERYCELLS, context, ParametBean.getParameter(parm, objects), new QueryRoomsTask());
        new HttpUtils(context).getServerForResult(callback, voLogin);
    }

    /**
     * 查询房间
     */
    public static void queryRooms(Context context, String[] parm, Object[] objects, HttpUtils.DataCallBack callback) {
        /**
         * 初始化请求参数 Parameters: requestUrl 请求根url context 上下文 requestDataMap 请求参数
         * BaseParamsMapUtil jsonParser 解析工具类 BaseParser.java 继承自这个类
         */
        RequestVo voLogin = new RequestVo(Constants.QUERYROOMS, context, ParametBean.getParameter(parm, objects), new QueryRoomsTask());
        new HttpUtils(context).getServerForResult(callback, voLogin);
    }

    /**
     * 创建常用地址
     */
    public static void createCommonAddress(Context context, String[] parm, Object[] objects, HttpUtils.DataCallBack callback) {
        /**
         * 初始化请求参数 Parameters: requestUrl 请求根url context 上下文 requestDataMap 请求参数
         * BaseParamsMapUtil jsonParser 解析工具类 BaseParser.java 继承自这个类
         */
        RequestVo voLogin = new RequestVo(Constants.CREATECOMMONADDRESS, context, ParametBean.getParameter(parm, objects), new CreateCommonAddressTask());
        new HttpUtils(context).getServerForResult(callback, voLogin);
    }

    /**
     * 获取常用地址
     */
    public static void getCommonAddressList(Context context, String[] parm, Object[] objects, HttpUtils.DataCallBack callback) {
        /**
         * 初始化请求参数 Parameters: requestUrl 请求根url context 上下文 requestDataMap 请求参数
         * BaseParamsMapUtil jsonParser 解析工具类 BaseParser.java 继承自这个类
         */
        RequestVo voLogin = new RequestVo(Constants.GETCOMMONADDRESSLIST, context, ParametBean.getParameter(parm, objects), new GetCommonAddressListTask());
        new HttpUtils(context).getServerForResult(callback, voLogin);
    }

    /**
     * 获取所有帖子
     */
    public static void getAllFindList(Context context, String[] parm, Object[] objects, HttpUtils.DataCallBack callback) {
        /**
         * 初始化请求参数 Parameters: requestUrl 请求根url context 上下文 requestDataMap 请求参数
         * BaseParamsMapUtil jsonParser 解析工具类 BaseParser.java 继承自这个类
         */
        RequestVo voLogin = new RequestVo(Constants.ALLFIND, context, ParametBean.getParameter(parm, objects), new GetThemeListTask());
        new HttpUtils(context).getServerForResult(callback, voLogin);
    }

    /**
     * 获取我关注的帖子
     */
    public static void getMyFindList(Context context, String[] parm, Object[] objects, HttpUtils.DataCallBack callback) {
        /**
         * 初始化请求参数 Parameters: requestUrl 请求根url context 上下文 requestDataMap 请求参数
         * BaseParamsMapUtil jsonParser 解析工具类 BaseParser.java 继承自这个类
         */
        RequestVo voLogin = new RequestVo(Constants.MYFIND, context, ParametBean.getParameter(parm, objects), new GetThemeListTask());
        new HttpUtils(context).getServerForResult(callback, voLogin);
    }

    /**
     * 获取我发的帖子
     */
    public static void getFollowFindList(Context context, String[] parm, Object[] objects, HttpUtils.DataCallBack callback) {
        /**
         * 初始化请求参数 Parameters: requestUrl 请求根url context 上下文 requestDataMap 请求参数
         * BaseParamsMapUtil jsonParser 解析工具类 BaseParser.java 继承自这个类
         */
        RequestVo voLogin = new RequestVo(Constants.FOLLOWFIND, context, ParametBean.getParameter(parm, objects), new GetThemeListTask());
        new HttpUtils(context).getServerForResult(callback, voLogin);
    }

    /**
     * 获取帖子详情
     */
    public static void getThemeDetails(Context context, String[] parm, Object[] objects, HttpUtils.DataCallBack callback) {
        /**
         * 初始化请求参数 Parameters: requestUrl 请求根url context 上下文 requestDataMap 请求参数
         * BaseParamsMapUtil jsonParser 解析工具类 BaseParser.java 继承自这个类
         */
        RequestVo voLogin = new RequestVo(Constants.FINDDETAILS, context, ParametBean.getParameter(parm, objects), new GetThemeDetailsTask());
        new HttpUtils(context).getServerForResult(callback, voLogin);
    }

    /**
     * 点赞
     */
    public static void doThumbsUp(Context context, String[] parm, Object[] objects, HttpUtils.DataCallBack callback) {
        /**
         * 初始化请求参数 Parameters: requestUrl 请求根url context 上下文 requestDataMap 请求参数
         * BaseParamsMapUtil jsonParser 解析工具类 BaseParser.java 继承自这个类
         */
        RequestVo voLogin = new RequestVo(Constants.THUMBSUP, context, ParametBean.getParameter(parm, objects), new DoThumbsUpTask());
        new HttpUtils(context).getServerForResult(callback, voLogin);
    }

    /**
     * 关注
     */
    public static void doFollow(Context context, String[] parm, Object[] objects, HttpUtils.DataCallBack callback) {
        /**
         * 初始化请求参数 Parameters: requestUrl 请求根url context 上下文 requestDataMap 请求参数
         * BaseParamsMapUtil jsonParser 解析工具类 BaseParser.java 继承自这个类
         */
        RequestVo voLogin = new RequestVo(Constants.FOLLOW, context, ParametBean.getParameter(parm, objects), new DoThumbsUpTask());
        new HttpUtils(context).getServerForResult(callback, voLogin);
    }

    /**
     * 取消关注
     */
    public static void doCancelFollow(Context context, String[] parm, Object[] objects, HttpUtils.DataCallBack callback) {
        /**
         * 初始化请求参数 Parameters: requestUrl 请求根url context 上下文 requestDataMap 请求参数
         * BaseParamsMapUtil jsonParser 解析工具类 BaseParser.java 继承自这个类
         */
        RequestVo voLogin = new RequestVo(Constants.CANCELFOLLOW, context, ParametBean.getParameter(parm, objects), new DoThumbsUpTask());
        new HttpUtils(context).getServerForResult(callback, voLogin);
    }

    /**
     * 海康服务测试
     */
    public static void subOrgList(Context context, Map<String, Object> requestDataMap, HttpUtils.DataCallBack callback) {
        /**
         * 初始化请求参数 Parameters: requestUrl 请求根url context 上下文 requestDataMap 请求参数
         * BaseParamsMapUtil jsonParser 解析工具类 BaseParser.java 继承自这个类
         */
        RequestVo voLogin = new RequestVo("POST",Constants.SUBORGLIST, context, requestDataMap, new HikBaseTask());
        new HttpUtils(context).getServerForResult(callback, voLogin);
    }

    /**
     * 获取用户信息
     * @param context
     * @param requestDataMap
     * @param callback
     */
    public static void personInfoHik(Context context, Map<String, Object> requestDataMap, HttpUtils.DataCallBack callback){
        RequestVo voLogin = new RequestVo("POST",Constants.PERSONINFO, context, requestDataMap, new HikUserTask());
        new HttpUtils(context).getServerForResult(callback, voLogin);
    }

    /**
     * 获取用户车辆信息
     * @param context
     * @param requestDataMap
     * @param callback
     */
    public static void hikCarList(Context context, Map<String, Object> requestDataMap, HttpUtils.DataCallBack callback){
        RequestVo voLogin = new RequestVo("POST",Constants.CARLIST, context, requestDataMap, new HikCarListTask());
        new HttpUtils(context).getServerForResult(callback, voLogin);
    }

    /**
     * 获取车辆布控信息
     * @param context
     * @param requestDataMap
     * @param callback
     */
    public static void hikAlarmCar(Context context, Map<String, Object> requestDataMap, HttpUtils.DataCallBack callback){
        RequestVo voLogin = new RequestVo("POST",Constants.ALARMCAR, context, requestDataMap, new HikAlarmCarTask());
        new HttpUtils(context).getServerForResult(callback, voLogin);
    }

    /**
     * 车辆布控
     * @param context
     * @param requestDataMap
     * @param callback
     */
    public static void hikAlarmCarAddition(Context context, Map<String, Object> requestDataMap, HttpUtils.DataCallBack callback){
        RequestVo voLogin = new RequestVo("POST",Constants.ALARMCARADDITION, context, requestDataMap, new HikAlarmAdditionTask());
        new HttpUtils(context).getServerForResult(callback, voLogin);
    }

    /**
     * 车辆取消布控
     * @param context
     * @param requestDataMap
     * @param callback
     */
    public static void hikAlarmCarDeletion(Context context, Map<String, Object> requestDataMap, HttpUtils.DataCallBack callback){
        RequestVo voLogin = new RequestVo("POST",Constants.ALARMCARDELETION, context, requestDataMap, new HikNullTask());
        new HttpUtils(context).getServerForResult(callback, voLogin);
    }

    /**
     * 获取车辆出入信息
     * @param context
     * @param requestDataMap
     * @param callback
     */
    public static void hikCarCrossRecord(Context context, Map<String, Object> requestDataMap, HttpUtils.DataCallBack callback){
        RequestVo voLogin = new RequestVo("POST",Constants.CARCROSSRECORD, context, requestDataMap, new HikCarCrossRecordTask());
        new HttpUtils(context).getServerForResult(callback, voLogin);
    }

    /**
     * 获取卡片信息
     * @param context
     * @param requestDataMap
     * @param callback
     */
    public static void hikCardList(Context context, Map<String, Object> requestDataMap, HttpUtils.DataCallBack callback){
        RequestVo voLogin = new RequestVo("POST",Constants.CARDLIST, context, requestDataMap, new HikCardListTask());
        new HttpUtils(context).getServerForResult(callback, voLogin);
    }

    /**
     * 新增访客
     * @param context
     * @param body
     * @param callback
     */
    public static void hikNewVisitor(Context context, String body, HttpUtils.DataCallBack callback){
        RequestVo voLogin = new RequestVo("POST",Constants.NEWVISITOR, context, body, new HikNullTask());
        new HttpUtils(context).getServerForResult(callback, voLogin);
    }

    /**
     *
     * @param context
     * @param requestDataMap
     * @param callback
     */
    public static void hikVisitorId(Context context, Map<String, Object> requestDataMap, HttpUtils.DataCallBack callback){
        RequestVo voLogin = new RequestVo("POST",Constants.VISITORID, context, requestDataMap, new HikUserTask());
        new HttpUtils(context).getServerForResult(callback, voLogin);
    }

    /**
     * 获取停车账单
     * @param context
     * @param requestDataMap
     * @param callback
     */
    public static void hikParkingPayment(Context context, Map<String, Object> requestDataMap, HttpUtils.DataCallBack callback){
        RequestVo voLogin = new RequestVo("POST",Constants.PARKINGPAYMENT, context, requestDataMap, new HikParkingPaymentTask());
        new HttpUtils(context).getServerForResult(callback, voLogin);
    }

    /**
     * 账单支付确认
     * @param context
     * @param requestDataMap
     * @param callback
     */
    public static void hikFinishPayment(Context context, Map<String, Object> requestDataMap, HttpUtils.DataCallBack callback){
        RequestVo voLogin = new RequestVo("POST",Constants.RECEIPT, context, requestDataMap, new HikNullTask());
        new HttpUtils(context).getServerForResult(callback, voLogin);
    }

    public static void newVisitor(Context context,String[] parm, Object[] objects,HttpUtils.DataCallBack callback){
        RequestVo voLogin = new RequestVo(Constants.NEWVISITOR, context, ParametBean.getParameter(parm, objects), new NewVisitorTask());
        new HttpUtils(context).getServerForResult(callback, voLogin);
    }


    public static void carCharge(Context context, Map<String, Object> requestDataMap, HttpUtils.DataCallBack callback){
        RequestVo voLogin = new RequestVo("POST",Constants.CARCHARGE, context, requestDataMap, new HikNullTask());
        new HttpUtils(context).getServerForResult(callback, voLogin);
    }

    public static void carChargeDeletion(Context context, Map<String, Object> requestDataMap, HttpUtils.DataCallBack callback){
        RequestVo voLogin = new RequestVo("POST",Constants.CARCHARGEDELETION, context, requestDataMap, new HikNullTask());
        new HttpUtils(context).getServerForResult(callback, voLogin);
    }

    public static void parkList(Context context, Map<String, Object> requestDataMap, HttpUtils.DataCallBack callback){
        RequestVo voLogin = new RequestVo("POST",Constants.PARKLIST, context, requestDataMap, new HikParkListTask());
        new HttpUtils(context).getServerForResult(callback, voLogin);
    }
    public static void carChargeInfo(Context context, Map<String, Object> requestDataMap, HttpUtils.DataCallBack callback){
        RequestVo voLogin = new RequestVo("POST",Constants.CARCHARGEINFO, context, requestDataMap, new HikCarChargeInfoTask());
        new HttpUtils(context).getServerForResult(callback, voLogin);
    }


    /**
     * 获取人脸分组
     * @param context
     * @param requestDataMap
     * @param callback
     */
    public static void hikFaceGroup(Context context, Map<String, Object> requestDataMap, HttpUtils.DataCallBack callback){
        RequestVo voLogin = new RequestVo("POST",Constants.FACEGROUP, context, requestDataMap, new HikNullTask());
        new HttpUtils(context).getServerForResult(callback, voLogin);
    }

    /**
     * 添加人脸
     * @param context
     * @param requestDataMap
     * @param callback
     */
    public static void hikAddFace(Context context, Map<String, Object> requestDataMap, HttpUtils.DataCallBack callback){
        RequestVo voLogin = new RequestVo("POST",Constants.ADDFACE, context, requestDataMap, new HikNullTask());
        new HttpUtils(context).getServerForResult(callback, voLogin);
    }

    /**
     * 查询人脸
     * @param context
     * @param requestDataMap
     * @param callback
     */
    public static void hikQueryFace(Context context, Map<String, Object> requestDataMap, HttpUtils.DataCallBack callback){
        RequestVo voLogin = new RequestVo("POST",Constants.QUERYFACE, context, requestDataMap, new HikNullTask());
        new HttpUtils(context).getServerForResult(callback, voLogin);
    }

    /**
     * 修改人脸
     * @param context
     * @param requestDataMap
     * @param callback
     */
    public static void hikUpdateFace(Context context, Map<String, Object> requestDataMap, HttpUtils.DataCallBack callback){
        RequestVo voLogin = new RequestVo("POST",Constants.UPDATEFACE, context, requestDataMap, new HikNullTask());
        new HttpUtils(context).getServerForResult(callback, voLogin);
    }

    /**
     * 删除人脸
     * @param context
     * @param requestDataMap
     * @param callback
     */
    public static void hikDeleteFace(Context context, Map<String, Object> requestDataMap, HttpUtils.DataCallBack callback){
        RequestVo voLogin = new RequestVo("POST",Constants.DELETEFACE, context, requestDataMap, new HikNullTask());
        new HttpUtils(context).getServerForResult(callback, voLogin);
    }
}



