package com.atlas.crmapp.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Handler;
import android.os.Message;

import com.atlas.crmapp.R;
import com.atlas.crmapp.network.AliOSS;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.tencent.imsdk.TIMImageElem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/23.
 */

public class ImageUtil {
    public static final int CROP_PHOTO = 2;
    public static final int REQUEST_CODE_PICK_IMAGE = 3;
    private static final int MSG_UPLOAD_SUCCRSS = 100;
    Context context;
    public ImageUtil(Context context){
        this.context = context;
    }

    Handler myHandler  = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == MSG_UPLOAD_SUCCRSS){
                String imageUrl = (String) msg.obj;
                if(iUploadSuccess!= null){
                    fileUrl.add(imageUrl);
                    iUploadSuccess.getDownloadUrl(imageUrl);
                    if (filePath.size() == position + 1){
                        iUploadSuccess.getDownloadUrlList(fileUrl);
                        position = 0;
                        return;
                    }
                    position ++;
                    send();
                }
            }
        }
    };

    int position = 0;
    List<String> filePath;
    List<String> fileUrl;
    public void uploadPhotoToServer(List<String> filePath, final OnPostImg iUploadSuccess ){
        this.position = 0;
        this.filePath = filePath;
        setiUploadSuccess(iUploadSuccess);
        fileUrl = new ArrayList<>();
        send();
    }

    private void send(){
        final KProgressHUD progressHUD = KProgressHUDUtils.showLoading(context, context.getString(R.string.s77));
        BizDataRequest.asyncUploadAvata(context, filePath.get(position), new AliOSS.OnUpload() {
            @Override
            public void onSuccess(String downloadUrl) {
                KProgressHUDUtils.dismissLoading(context, progressHUD , 0);
                Message msg = myHandler.obtainMessage();
                msg.what = MSG_UPLOAD_SUCCRSS;
                msg.obj = downloadUrl;
                myHandler.sendMessage(msg);
            }

            @Override
            public void onError(DcnException error) {
                if (iUploadSuccess != null)
                    iUploadSuccess.onFailed();
                position = 0;
            }

            @Override
            public void onProgress(long currentSize, long totalSize) {

            }
        });
    }

    private OnPostImg iUploadSuccess;

    public OnPostImg getiUploadSuccess() {
        return iUploadSuccess;
    }

    public void setiUploadSuccess(OnPostImg iUploadSuccess) {
        this.iUploadSuccess = iUploadSuccess;
    }

    public interface OnPostImg {
        //获得拍照后相片的地址
        void getDownloadUrl(String serverImagePath);
        void getDownloadUrlList(List<String> serverImagePath);
        void onFailed();
    }

    /**
     * 生成缩略图
     * 缩略图是将原图等比压缩，压缩后宽、高中较小的一个等于198像素
     * 详细信息参见文档
     */
    public static Bitmap getThumb(String path){
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        int reqWidth, reqHeight, width=options.outWidth, height=options.outHeight;
        if (width == 0 || height == 0) {
            return null;
        }

        if (width > height){
            reqWidth = 1080;
            reqHeight = (reqWidth * height)/width;
        }else{
            reqHeight = 1080;
            reqWidth = (width * reqHeight)/height;
        }
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        try{
            options.inSampleSize = inSampleSize;
            options.inJustDecodeBounds = false;
            Matrix mat = new Matrix();
            Bitmap bitmap = BitmapFactory.decodeFile(path, options);
            ExifInterface ei =  new ExifInterface(path);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch(orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    mat.postRotate(90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    mat.postRotate(180);
                    break;
            }
            if (bitmap == null)
                return null;
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), mat, true);
        }catch (IOException e){
            return null;
        }
    }
}
