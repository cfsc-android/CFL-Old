package com.xiandao.android.ui.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.xiandao.android.R;
import com.xiandao.android.entity.OverallSituationEntity;
import com.xiandao.android.http.MyCallBack;
import com.xiandao.android.http.XUtils;
import com.xiandao.android.net.ApiHttpResult;
import com.xiandao.android.net.HttpUtils;
import com.xiandao.android.net.NetUtil;
import com.xiandao.android.ui.BaseActivity;
import com.xiandao.android.utils.Constants;
import com.xiandao.android.utils.FileManagement;
import com.xiandao.android.utils.Tools;
import com.xiandao.android.utils.XUtilsImageUtils;

import org.xutils.event.annotation.ContentView;
import org.xutils.event.annotation.Event;
import org.xutils.event.annotation.ViewInject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ContentView(R.layout.activity_visitor_qr_code)
public class VisitorQrCodeActivity extends BaseActivity {
    @ViewInject(R.id.tv_title_name)
    private TextView tv_title_name;

    @ViewInject(R.id.iv_visitor_qr_code)
    private ImageView iv_visitor_qr_code;
    @ViewInject(R.id.tv_visitor_name)
    private TextView tv_visitor_name;
    @ViewInject(R.id.tv_visitor_valid_time)
    private TextView tv_visitor_valid_time;
    @ViewInject(R.id.ll_visitor_qr_code)
    private LinearLayout ll_visitor_qr_code;
    @ViewInject(R.id.ll_visitor_qr_code_all)
    private LinearLayout ll_visitor_qr_code_all;
    @ViewInject(R.id.tv_visitor_valid_num)
    private TextView tv_visitor_valid_num;

    private String name;
    private String start;
    private String end;
    private int num;
    private String qrCodeUrl;
    private boolean savePermiss=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        qrCodeUrl=bundle.getString("qrCodeUrl");
        name=bundle.getString("name");
        start=bundle.getString("start");
        end=bundle.getString("end");
        num=bundle.getInt("num");
        tv_title_name.setText("邀请码");
        tv_visitor_name.setText(name);
        tv_visitor_valid_num.setText(num+"");
        tv_visitor_valid_time.setText(end);
        XUtilsImageUtils.display(iv_visitor_qr_code,qrCodeUrl);
//
//
//        Bitmap bm_logo = BitmapFactory.decodeResource(getResources(), R.mipmap.ll_cfl);
//        initQRCode(qrCodeUrl,1000,1000,bm_logo);
        getPromiss();

    }



    public void initQRCode(String content, int width, int height,Bitmap logo){
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
                    iv_visitor_qr_code.setImageBitmap(addLogin(bm_QR,logo));
                }
            }else{
                iv_visitor_qr_code.setImageBitmap(bm_QR);
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
    private static BitMatrix setBorder(BitMatrix matrix,int borderWidth) {
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

    @Event({R.id.iv_title_left,R.id.btn_visitor_qr_code_share,R.id.btn_visitor_qr_code_save})
    private void onClickEvent(View v){
        switch (v.getId()){
            case R.id.iv_title_left:
                finish();
                break;
            case R.id.btn_visitor_qr_code_share:
                share();
                break;
            case R.id.btn_visitor_qr_code_save:
                if(savePermiss){
                    try {
                        saveFile(getGeneratBitmap(),"qr_share"+new Date().getTime()+".jpg","/img");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    showShortToast("没有读写存储权限，请设置权限");
                }
                break;
        }
    }

    private Bitmap getGeneratBitmap(){
        View dView = getWindow().getDecorView();
        dView.setDrawingCacheEnabled(true);
        dView.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(dView.getDrawingCache());
        return bitmap;
    }

    /**
     * @param linearLayout 要转化为图片的布局
     */
    private Bitmap generatBitmap(LinearLayout linearLayout) {
        linearLayout.setDrawingCacheEnabled(true);
        linearLayout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        linearLayout.layout(0, 0, linearLayout.getMeasuredWidth(), linearLayout.getMeasuredHeight());
        linearLayout.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(linearLayout.getDrawingCache());
        linearLayout.setDrawingCacheEnabled(false);
        linearLayout.setGravity(Gravity.CENTER);  //因为刚刚重新测量布局一次，需要重新设置view居中
        return bitmap;

    }

    private static final String SAVE_PIC_PATH = Environment.getExternalStorageDirectory()
            + File.separator + Environment.DIRECTORY_DCIM
            +File.separator+"Camera"+File.separator;
    //保存的确切位置，根据自己的具体需要来修改
    public void saveFile(Bitmap bm, String fileName, String path) throws IOException {
        String subForder = SAVE_PIC_PATH + path;
        File foder = new File(subForder);
        if (!foder.exists()) {
            foder.mkdirs();
        }
        File myCaptureFile = new File(subForder, fileName);
        if (!myCaptureFile.exists()) {
            myCaptureFile.createNewFile();
        }
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        bos.flush();
        bos.close();
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);  // 这是刷新单个文件
        Uri uri = Uri.fromFile(myCaptureFile);
        intent.setData(uri);
        sendBroadcast(intent);
        showShortToast("保存成功");
    }
    /**
     * 分享
     */
    private void share(){
        UMImage image = new UMImage(VisitorQrCodeActivity.this, getGeneratBitmap());
        new ShareAction(this).withText("开门二维码")
                .withMedia(image)
                .setDisplayList(SHARE_MEDIA.QQ,SHARE_MEDIA.WEIXIN)
                .setCallback(shareListener).open();
    }

    private UMShareListener shareListener = new UMShareListener() {
        /**
         * @descrption 分享开始的回调
         * @param platform 平台类型
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        /**
         * @descrption 分享成功的回调
         * @param platform 平台类型
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {
            showShortToast("成功了");
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Log.d("错误日志",platform.toString());
            Log.d("错误日志",t.toString());
            showShortToast("失败了");
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
//            showShortToast("取消了");
        }
    };

    private void getPromiss(){
        if(Build.VERSION.SDK_INT>=23){
            int hasPermission = checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if(hasPermission!= PackageManager.PERMISSION_GRANTED){
                savePermiss=false;
                String[] mPermissionList = new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
                ActivityCompat.requestPermissions(this,mPermissionList,123);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        if (requestCode == 123) {
            savePermiss=true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
