package com.xiandao.android.ui.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.xiandao.android.R;
import com.xiandao.android.entity.HikBaseEntity;
import com.xiandao.android.entity.HikCardList;
import com.xiandao.android.entity.HikUser;
import com.xiandao.android.entity.LoginUserEntity;
import com.xiandao.android.entity.smart.BaseEntity;
import com.xiandao.android.entity.smart.QrCodeEntity;
import com.xiandao.android.entity.smart.UserInfoEntity;
import com.xiandao.android.http.JsonParse;
import com.xiandao.android.http.MyCallBack;
import com.xiandao.android.http.XUtils;
import com.xiandao.android.http.hikhttp.HikHttpUtil;
import com.xiandao.android.http.hikhttp.HikParams;
import com.xiandao.android.http.hikhttp.config.ArtemisConfig;
import com.xiandao.android.net.ApiHttpResult;
import com.xiandao.android.net.HttpUtils;
import com.xiandao.android.ui.BaseActivity;
import com.xiandao.android.utils.Constants;
import com.xiandao.android.utils.FileManagement;
import com.xiandao.android.utils.XUtilsImageUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.LogUtils;
import org.xutils.event.annotation.ContentView;
import org.xutils.event.annotation.Event;
import org.xutils.event.annotation.ViewInject;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.xiandao.android.base.Config.BASE_URL;
import static com.xiandao.android.base.Config.IOT;

/**
 * Created by ZXL on 2019/4/1.
 * Describe:
 */
@ContentView(R.layout.activity_un_lock)
public class UnLock extends BaseActivity {
    @ViewInject(R.id.tv_title_name)
    private TextView tv_title_name;

    @ViewInject(R.id.iv_qr_code)
    private ImageView iv_qr_code;

    private HikUser hikUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hikUser=FileManagement.getHikUser();
        tv_title_name.setText("门禁开锁");
//        getCardInfo();
//        getQrCode();
        getOpenDoorQrCode();
    }

    @Event({R.id.iv_title_left,R.id.tv_refresh_qr})
    private void onClickEvent(View v){
        switch (v.getId()){
            case R.id.iv_title_left:
                finish();
                break;
            case R.id.tv_refresh_qr:
 //               initQRCode("https://dev.chanfine.com:8888/app/download.html",2000,2000,null);
//                getQrCode();
                break;
        }
    }

    private void getOpenDoorQrCode(){
        startProgressDialog("");
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
        Map<String,Object> requestMap=new HashMap<>();
        requestMap.put("cardNo",FileManagement.getUserInfoEntity().getDefaultCardNo());
        requestMap.put("effectTime",sdf.format(new Date()));
        requestMap.put("expireTime",sdf.format(new Date(new Date().getTime()+5*60*1000)));
        requestMap.put("openTimes",4);
        requestMap.put("phaseId",FileManagement.getUserInfoEntity().getRoomList().get(0).getPhaseId());
        XUtils.PostJson(BASE_URL+IOT+"community/api/access/v1/qrcode/owner",requestMap,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtils.d(result);
                BaseEntity<QrCodeEntity> baseEntity= JsonParse.parse(result,QrCodeEntity.class);
                if(baseEntity.isSuccess()){
                    XUtilsImageUtils.display(iv_qr_code,baseEntity.getResult().getQrCodeUrl());
                }else{
                    showToast(baseEntity.getMessage());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                showToast(ex.getMessage());
            }

            @Override
            public void onFinished() {
                super.onFinished();
                stopProgressDialog();
            }
        });
    }

    /**
     * 获取二维码
     */
    private void getQrCode(){
        startProgressDialog("");
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
        Map<String,Object> requestMap=new HashMap<>();
        requestMap.put("userId",FileManagement.getLoginUserEntity().getUserId());
        requestMap.put("cardNo",FileManagement.getLoginUserEntity().getCardNo());
        requestMap.put("effectTime",sdf.format(new Date()));
        requestMap.put("expireTime",sdf.format(new Date(new Date().getTime()+5*60*1000)));
        requestMap.put("openTimes",4);
        XUtils.Post(Constants.HOST+Constants.NEWVISITOR,requestMap,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                Log.e("result",result);
                stopProgressDialog();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if(jsonObject.getInt("code")==200) {
                        JSONObject object = jsonObject.getJSONObject("data");
                        XUtilsImageUtils.display(iv_qr_code,object.getString("qrCodeUrl"));
                    }else{
                        showShortToast(jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
            }

            @Override
            public void onFinished() {
                super.onFinished();
                stopProgressDialog();
            }
        });
    }



    private void getCardInfo(){
//        initQRCode("19041916",1000,1000,null);


//        Map<String, Object> requestDataMap=new HashMap<>();
//        requestDataMap.put("personName",hikUser.getPersonName());
//        requestDataMap.put("useStatus",1);
//        requestDataMap.put("personIds",hikUser.getPersonId());
//        requestDataMap.put("pageSize",100);
//        requestDataMap.put("pageNo",1);
//        ApiHttpResult.hikCardList(this, requestDataMap, new HttpUtils.DataCallBack<Object>() {
//            @Override
//            public void callBack(Object o) {
//                HikBaseEntity hikBaseEntity= (HikBaseEntity) o;
//                if(hikBaseEntity.getCode().equals("0")){
//                    HikCardList hikCardList= (HikCardList) hikBaseEntity.getData();
//                    if(hikCardList.getList().size()>0){
//                        //创建一个bitmap对象用于作为其图标
//                        Bitmap bm_logo = BitmapFactory.decodeResource(getResources(), R.mipmap.ll_cfl);
//                        initQRCode("19041916",1000,1000,bm_logo);
////                        initQRCode(hikCardList.getList().get(0).getCardNo(),1000,1000,bm_logo);
//                    }
//                }else{
//                    showShortToast(hikBaseEntity.getMsg());
//                }
//            }
//        });
    }

    private void initQRCode(String content, int width, int height,Bitmap logo){

        try {
            //1,创建实例化对象
            QRCodeWriter writer = new QRCodeWriter() ;
            //2,设置字符集
            Map<EncodeHintType, String> map = new HashMap<EncodeHintType, String>();
            map.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            //3，通过encode方法将内容写入矩阵对象
            BitMatrix matrix = writer.encode(content, BarcodeFormat.QR_CODE, width, height,map);
            matrix = setBorder(matrix,20);//设置白边
            width = matrix.getWidth();
            height = matrix.getHeight();
            //4，定义一个二维码像素点的数组，向每个像素点中填充颜色
            int[] pixels = new int[width*height];
            //5,往每一像素点中填充颜色（像素没数据则用黑色填充，没有则用彩色填充，不过一般用白色）
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (matrix.get(j, i)) {
                        pixels[i*width+j] = 0xff000000;
                    }else {
                        pixels[i*width+j] = 0xffffffff;
                    }
                }
            }
            //6,创建一个指定高度和宽度的空白bitmap对象
            Bitmap bm_QR = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            //7，将每个像素的颜色填充到bitmap对象
            bm_QR.setPixels(pixels, 0, width, 0, 0, width, height);
            //添加logo
            if(logo!=null){
                //9，创建一个方法在二维码上添加一张图片
                if (addLogin(bm_QR,logo) != null) {
                    iv_qr_code.setImageBitmap(addLogin(bm_QR,logo));
                }
            }else{
                iv_qr_code.setImageBitmap(bm_QR);
            }
        } catch (WriterException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    /**
     * 用于向创建的二维码中添加一个logn
     * @param bm_QR
     * @param bm_login
     * @return
     */
    private Bitmap addLogin(Bitmap bm_QR, Bitmap bm_login) {
        if (bm_QR == null) {
            return null;
        }
        if (bm_login == null) {
            return bm_QR ;
        }

        //获取图片的宽高
        int bm_QR_Width = bm_QR.getWidth() ;
        int bm_QR_Height = bm_QR.getHeight();
        int bm_login_Width = bm_login.getWidth() ;
        int bm_login_Height = bm_login.getHeight();

        //设置logn的大小为二维码整体大小的1/5
        float scale_login = bm_QR_Width*1.0f /6/bm_login_Width ;
        Bitmap bitmap = Bitmap.createBitmap(bm_QR_Width, bm_QR_Height, Bitmap.Config.ARGB_8888);

        try {
            Canvas canvas = new Canvas(bitmap);
            canvas.drawBitmap(bm_QR, 0, 0, null);
            canvas.scale(scale_login, scale_login, bm_QR_Width / 2, bm_QR_Height / 2);
            canvas.drawBitmap(bm_login, (bm_QR_Width - bm_login_Width) / 2, (bm_QR_Height - bm_login_Height) / 2, null);

            canvas.save(Canvas.ALL_SAVE_FLAG);
            canvas.restore();
        } catch (Exception e) {
            bitmap = null;
            e.getStackTrace();
        }

        return bitmap;

    }

    /**
     * 设置二维码边框
     * @param matrix
     * @param borderWidth
     * @return
     */
    private BitMatrix setBorder(BitMatrix matrix,int borderWidth) {
        int[] rec = matrix.getEnclosingRectangle();
        int resWidth = rec[2]+borderWidth*2;
        int resHeight = rec[3]+borderWidth*2;
        BitMatrix resMatrix = new BitMatrix(resWidth, resHeight);
        resMatrix.clear();
        for (int i = 0; i < resWidth; i++) {
            for (int j = 0; j < resHeight; j++) {
                if (matrix.get(i-borderWidth + rec[0], j-borderWidth + rec[1]))
                    resMatrix.set(i, j);
            }
        }
        return resMatrix;
    }

}


