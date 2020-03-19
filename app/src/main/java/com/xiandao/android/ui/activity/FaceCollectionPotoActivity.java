package com.xiandao.android.ui.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.media.ExifInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.andview.refreshview.utils.LogUtils;
import com.xiandao.android.R;
import com.xiandao.android.entity.LoginUserEntity;
import com.xiandao.android.entity.eventbus.EventBusMessage;
import com.xiandao.android.entity.eventbus.FaceCollectionEventBusData;
import com.xiandao.android.entity.smart.BaseEntity;
import com.xiandao.android.entity.smart.FaceEntity;
import com.xiandao.android.entity.smart.FileEntity;
import com.xiandao.android.entity.smart.RoomEntity;
import com.xiandao.android.http.JsonParse;
import com.xiandao.android.http.MyCallBack;
import com.xiandao.android.http.XUtils;
import com.xiandao.android.ui.BaseActivity;
import com.xiandao.android.utils.Constants;
import com.xiandao.android.utils.FileManagement;
import com.xiandao.android.view.CameraPreview;
import com.xiandao.android.view.imagepreview.ImageViewInfo;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.event.annotation.ContentView;
import org.xutils.event.annotation.Event;
import org.xutils.event.annotation.ViewInject;
import org.xutils.http.RequestParams;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.xiandao.android.base.Config.BASE_URL;
import static com.xiandao.android.base.Config.FILE;
import static com.xiandao.android.base.Config.IOT;

@ContentView(R.layout.activity_face_collection_poto)
public class FaceCollectionPotoActivity extends BaseActivity{


    @ViewInject(R.id.tv_title_name)
    private TextView tv_title_name;
    @ViewInject(R.id.cp_face_collection_photo)
    private CameraPreview cp_face_collection_photo;
    @ViewInject(R.id.btn_take_photo)
    private ImageButton btn_take_photo;
    @ViewInject(R.id.btn_take_photo_cancel)
    private ImageButton btn_take_photo_cancel;
    @ViewInject(R.id.btn_take_photo_sure)
    private ImageButton btn_take_photo_sure;
    @ViewInject(R.id.iv_face_collection_photo)
    private ImageView iv_face_collection_photo;

    private static final String PATH_IMAGES = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "xiandao";

    private File photoFile;
    private String userId="",name="";

    private int flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle=getIntent().getExtras();
        userId=bundle.getString("userId");
        name=bundle.getString("name");
        tv_title_name.setVisibility(GONE);
        if(Build.VERSION.SDK_INT>=23){
            int hasPermission = checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if(hasPermission!= PackageManager.PERMISSION_GRANTED){
                String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
                ActivityCompat.requestPermissions(this,mPermissionList,123);
            }
        }

    }

    @Event({R.id.iv_title_left,R.id.btn_take_photo,R.id.btn_take_photo_cancel,R.id.btn_take_photo_sure})
    private void onClickEvent(View v){
        switch (v.getId()){
            case R.id.iv_title_left:
                finish();
                break;
            case R.id.btn_take_photo:
                cp_face_collection_photo.takePicture(mShutterCallback, rawPictureCallback, jpegPictureCallback);
                break;
            case R.id.btn_take_photo_cancel:
                btn_take_photo.setVisibility(VISIBLE);
                iv_face_collection_photo.setVisibility(VISIBLE);
                btn_take_photo_cancel.setVisibility(GONE);
                btn_take_photo_sure.setVisibility(GONE);
                cp_face_collection_photo.startPreview();
                break;
            case R.id.btn_take_photo_sure:
                uploadFace();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        btn_take_photo.setVisibility(VISIBLE);
        iv_face_collection_photo.setVisibility(VISIBLE);
        btn_take_photo_cancel.setVisibility(GONE);
        btn_take_photo_sure.setVisibility(GONE);
    }

    private Camera.ShutterCallback mShutterCallback = new Camera.ShutterCallback() {
        @Override
        public void onShutter() {

        }
    };
    private Camera.PictureCallback rawPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

        }
    };
    private Camera.PictureCallback jpegPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            camera.stopPreview();
            Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
            bmp=rotateBitmapByDegree(bmp,270);
            String fileName = "test_" + System.currentTimeMillis() + ".jpg";
            photoFile=saveBitmapFile(bmp, PATH_IMAGES + File.separator + fileName);
            btn_take_photo.setVisibility(GONE);
            iv_face_collection_photo.setVisibility(GONE);
            btn_take_photo_cancel.setVisibility(VISIBLE);
            btn_take_photo_sure.setVisibility(VISIBLE);
        }
    };

    /**
     * 图片旋转
     * @param bm
     * @param degree
     * @return
     */
    private Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
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

    /**
     * 保存照片返回文件
     * @param bitmap
     * @param filePath
     * @return
     */
    private File saveBitmapFile(Bitmap bitmap, String filePath) {
        File file = new File(filePath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs(); // 创建文件夹
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while ( baos.toByteArray().length / 1024>200) { //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        try {
            BufferedOutputStream bos = new BufferedOutputStream( new FileOutputStream(file));
            baos.writeTo(bos);
            bos.flush();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * base64
     * @param bitmap
     * @return
     */
    private String encodeImage(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //读取图片到ByteArrayOutputStream
        bitmap.compress(Bitmap.CompressFormat.PNG, 40, baos); //参数如果为100那么就不压缩
        byte[] bytes = baos.toByteArray();
        return Base64.encodeToString(bytes,Base64.DEFAULT);
    }

    @Override
    protected boolean isClickable() {
        return super.isClickable();
    }

    private void faceAccess(final FileEntity file){
        flag=0;
//        final List<RoomEntity> roomList=FileManagement.getUserInfoEntity().getRoomList();
//        for (int i = 0; i < roomList.size(); i++) {
//            Map<String,Object> requestMap=new HashMap<>();
//            requestMap.put("id",userId);
//            requestMap.put("name",name);
//            requestMap.put("phaseId",roomList.get(i).getPhaseId());
//            requestMap.put("unitIds",roomList.get(i).getUnitId());
//            XUtils.PostJson(BASE_URL+IOT+"community/api/access/v1/face/"+file.getId(),requestMap,new MyCallBack<String>(){
//                @Override
//                public void onSuccess(String result) {
//                    super.onSuccess(result);
//                    LogUtils.d(result);
//                    BaseEntity baseEntity= JsonParse.parse(result);
//                    if(baseEntity.isSuccess()){
//                        flag++;
//                    }else{
//                        showToast(baseEntity.getMessage());
//                        stopProgressDialog();
//                    }
//                }
//
//                @Override
//                public void onError(Throwable ex, boolean isOnCallback) {
//                    super.onError(ex, isOnCallback);
//                    showToast(ex.getMessage());
//                    stopProgressDialog();
//                }
//
//                @Override
//                public void onFinished() {
//                    super.onFinished();
//                    if(flag==roomList.size()){
//                        stopProgressDialog();
//                        EventBusMessage<FaceCollectionEventBusData> eventBusMessage=new EventBusMessage<>("faceCollection");
//                        eventBusMessage.setData(new FaceCollectionEventBusData(file.getCreateTime(),file.getDomain()+file.getUrl()));
//                        EventBus.getDefault().post(eventBusMessage);
//                        finish();
//                    }
//                }
//            });
//        }
    }

    /**
     * 上传
     */
     private void uploadFace(){
        startProgressDialog("");
        Map<String,String> requestMap=new HashMap<>();
        requestMap.put("resourceKey",userId);
        Map<String,File> fileMap=new HashMap<>();
        fileMap.put("UploadFile",photoFile);
        XUtils.UpLoadFile(BASE_URL+FILE+"files-anon", requestMap,fileMap,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtils.d(result);
                BaseEntity<FileEntity> baseEntity= JsonParse.parse(result,FileEntity.class);
                if(baseEntity.isSuccess()){
                    faceAccess(baseEntity.getResult());
                }else{
                    showToast(baseEntity.getMessage());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                showToast(ex.getMessage());
            }


        });
    }

    /**
     * 检测人脸
     * @param image
     */
    private void checkFace(String image){
        Map<String,Object> requestMap=new HashMap<>();
        requestMap.put("deviceSerial","231857475");
        requestMap.put("dataType",0);
        requestMap.put("image",image);
        requestMap.put("operation","none");
        XUtils.HikCloudPost("https://api2.hik-cloud.com/api/v1/ai/intelligence/face/analysis/detect",requestMap,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                Log.e("人脸检测",result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
            }

            @Override
            public void onFinished() {
                super.onFinished();
            }
        });
    }


    public void saveFile(byte[] data) {
        String fileName = UUID.randomUUID().toString() + ".jpg";
        FileOutputStream outputStream = null;
        try {
            File file = new File(PATH_IMAGES);
            if (!file.exists()) {
                file.mkdirs();
            }
            outputStream = new FileOutputStream(PATH_IMAGES + File.separator + fileName);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
            bufferedOutputStream.write(data, 0, data.length);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
