package com.xiandao.android.httptask;


import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.xiandao.android.entity.BaseParser;
import com.xiandao.android.entity.OverallSituationEntity;
import com.xiandao.android.net.HttpUtils;
import com.xiandao.android.net.RequestVo;
import com.xiandao.android.utils.Tools;

import org.json.JSONException;


/**
 * 此类描述的是:任务基础类
 *
 * @author TanYong
 *         create at 2017/4/21 10:41
 */
public class BaseTask extends BaseParser<Object> {
    protected Gson gson = null;
    protected OverallSituationEntity osEntity = null;
    protected String jsonInfo = "";
    public static final String ACCESSCERTIFICATE = "-60000012";// 访问证书已经过期
    public static final String REFRESHCERTIFICATE = "-60000013";// 刷新证书已经过期
    public static final String code = "-60000014";//证书不存在
    private Context context;

    private RequestVo vo;
//    private static Handler refreshHanlder = new Handler(Looper.getMainLooper()) {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//                case Constants.SUCCESS:
//                    if (msg.obj != null) {
//                        AccessToken atData = (AccessToken) msg.obj;
//                        if (atData == null) return;
//                        if (atData.getOverallSituationEntity().getResultCode().equals(code)) {
//                            platLoginResult();
//                        }
//                        if (FileManagement.getTokenEntity() != null) {
//                            TokenEntity toe = FileManagement.getTokenEntity();
//                            toe.setAccessToken(atData.getAccessToken());
//                            toe.setRefreshToken(atData.getRefreshToken());
//                            toe.setAccessExpires(atData.getAccessExpires());
//                            toe.setAuthorizationCode(atData.getAuthorizationCode());
//                            toe.setRefreshExpires(atData.getRefreshExpires());
//                            toe.setOpenId(atData.getOpenid());
//                            FileManagement.setUser(toe);
//                        }
//                    }
//                    break;
//            }
//        }
//    };

//    /**
//     * 登录
//     */
//    public static void platLoginResult() {
//        if (!Tools.isFastDoubleClick(10000)) {
//            if (!Tools.isUserLogin()) return;
//            ApiHttpResult.platLoginResult(AndroidApplication.getAppContext(),
//                    new String[]{"loginname", "password"}, new Object[]{FileManagement.getUserEntity().getUserName(),
//                            SHA1.SHA1Digest(FileManagement.getUserEntity().getPassWord())}, new HttpUtils.DataCallBack<Object>() {
//
//                @Override
//                public void callBack(Object o) {
//                    if (o != null) {
//                        TokenEntity userEntity = (TokenEntity) o;
//                        if (userEntity.getResultCode().equals("0")) {
//                            FileManagement.setUser(userEntity);
//                        } else {
//                            Tools.showPrompt(userEntity.getResultDes());
//                        }
//                    } else {
//                        Tools.showPrompt("网络异常");
//                    }
//                }
//            });
//        }
//    }

    public OverallSituationEntity getOsEntity() {
        return osEntity;
    }

    public void setOsEntity(OverallSituationEntity osEntity) {
        this.osEntity = osEntity;
    }

    @Override
    public Object parseJSON(final Context context, String str) throws JSONException {
        jsonInfo = str;
        this.context = context;
        if (Tools.isEmpty(str)) {
            return null;
        }
        gson = new Gson();
        setOsEntity(gson.fromJson(str, OverallSituationEntity.class));
//        if (osEntity != null && ACCESSCERTIFICATE.equals(osEntity.getResultCode()) && !Tools.isFastDoubleClick(5000)) {
//            if (FileManagement.getTokenEntity() == null) return null;
//            loadRefreshToken(context);
//        } else if (REFRESHCERTIFICATE.equals(getOsEntity().getResultCode())) {
//            if (LynActivityManager.getInstance().getActivityByClass(LoginActivity.class) == null && !Tools.isFastDoubleClick(5000)) {
//                new Handler(Looper.getMainLooper()).post(new Runnable() {
//                    @Override
//                    public void run() {
//                        Tools.showPrompt("登陆身份已失效，请重新登陆");
//                        FileManagement.removeObj(context);
//                    }
//                });
//                context.startActivity(new Intent(context, LoginActivity.class));
//            }
//            return null;
//        } else if (osEntity != null && code.equals(osEntity.getResultCode())) {
//            if (LynActivityManager.getInstance().getActivityByClass(LoginActivity.class) == null && !Tools.isFastDoubleClick(5000)) {
//                if (FileManagement.getTokenEntity() == null) return null;
//                loadRefreshToken(context);
//                if (vo != null)
//                    loadReloadData(vo);
//            }
//            return null;
//        } else if (osEntity != null && osEntity.getResultCode().equals("-7")) {
//            //请求超时
//            ApiHttpResult.serverTimeResult(context, null, null, new HttpUtils.DataCallBack() {
//                @Override
//                public void callBack(Object o) {
//                    if (o != null) {
//                        FileManagement.saveServerTime(Long.parseLong((String) o));
//                    }
//                }
//            });
//        }
        return str;
    }

    @Override
    public void reloadTask(RequestVo vo) throws Exception {
        this.vo = vo;
    }

//    public static void loadRefreshToken(Context mContext) {
//        if (NetUtil.hasConnectedNetwork(mContext.getApplicationContext()) && Tools.isUserLogin()) {
//            try {
//                // 请求的url地址
//                RequestVo voLogin = new RequestVo(Constants.ACCESSTOKEN,
//                        mContext, ParametBean.getAccesTokenParameter(new String[]{
//                        "grantType", "refreshToken"}, new Object[]{"refresh_token", FileManagement.getTokenEntity().getRefreshToken()}),
//                        new AccessTokenTask(), 9);
//                Message msg = Message.obtain();
//                msg.what = Constants.SUCCESS;
//                msg.obj = NetUtil.post(voLogin);
//                // 把结果返回给主线程
//                refreshHanlder.sendMessage(msg);
//            } catch (Exception e) {
//                e.printStackTrace();
//                refreshHanlder.sendEmptyMessage(Constants.FAILED);
//            }
//        } else {
//            refreshHanlder.sendEmptyMessage(Constants.NET_FAILED);
//        }
//    }

    public void loadReloadData(final RequestVo revo) {
        try {
            if (revo == null) return;
            if (revo.getCallback() == null) return;
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    new HttpUtils(context).getServerForResult(revo.getCallback(), revo);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
