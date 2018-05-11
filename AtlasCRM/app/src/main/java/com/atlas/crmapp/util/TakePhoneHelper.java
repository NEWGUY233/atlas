package com.atlas.crmapp.util;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;

import com.atlas.crmapp.R;
import com.atlas.crmapp.network.AliOSS;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hoda on 2017/7/26.
 */

public class TakePhoneHelper {
    public static final int CROP_PHOTO = 2;
    public static final int REQUEST_CODE_PICK_IMAGE = 3;
    private static final int MSG_UPLOAD_SUCCRSS = 100;

    private static Uri imageUri;

    public static void takePhotoIntent(Activity activity, File output) {

        /**
         * 隐式打开拍照的Activity，并且传入CROP_PHOTO常量作为拍照结束后回调的标志
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            imageUri = Uri.fromFile(output);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            ContentValues contentValues = new ContentValues(1);
            contentValues.put(MediaStore.Images.Media.DATA, output.getAbsolutePath());
            Uri uri = activity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

            activity.startActivityForResult(intent, CROP_PHOTO);
        } else {
            imageUri = Uri.fromFile(output);
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            activity.startActivityForResult(intent, CROP_PHOTO);
        }
    }

    public static  void alertStudentDialog(Activity activity, final OnClickItemListener onClickItemListener) {
        final List<String> lst1 = new ArrayList<String>();
        lst1.add(activity.getString(R.string.s75));
        lst1.add(activity.getString(R.string.s76));
        String[] ss1 = lst1.toArray(new String[lst1.size()]);
        new AlertDialog.Builder(activity).setItems(ss1,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            if(onClickItemListener != null){
                                onClickItemListener.onClickItemListener(which);
                            }
                            dialog.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).show();
    }

    /**
     * 从相册选取图片
     */
    public static void choosePhotoIntent(Activity activity) {
        /**
         * 打开选择图片的界面
         */
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");//相片类型
        activity.startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
    }

    public static void getChoosePhoto(Context context, Intent data, IPhotoPath takePhotoPath){
        Uri originalUri = data.getData();  //获得图片的uri
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(originalUri, proj, null, null, null);
        if (cursor != null){
            if(cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                String path = cursor.getString(column_index);
                if (path != null && path.length()>0){
                    takePhotoPath.getTakePhotoPath(path);
                }
            }
        }else
        {
            String path =data.getData().getPath();
            if (path != null && path.length()>0){
                takePhotoPath.getTakePhotoPath(path);
            }
        }
        if (cursor != null){
            cursor.close();
        }
    }

    public static  void getTakePhotoPath(File output , IPhotoPath photoPath){
        if (output != null){
            photoPath.getTakePhotoPath(output.getAbsolutePath());
        }
    }

    public static File getNewOnlyImageFile(){
        return  FileUtils.getNewImageFile("");
    }

    public static File getNewImageFile(String imageName){
        return  FileUtils.getNewImageFile(imageName);
    }

    public static String getCompressFilePath (Context context, String compressFileName, String uploadFilePath){
        Logger.d("compressFileName  " + compressFileName + "  uploadFilePath "+ uploadFilePath);
        File outFile = getNewImageFile(compressFileName);
        String outFilePath = outFile.getAbsolutePath();
        int degree = BitmapUtils.getBitmapDegree(uploadFilePath);//检测图是否被旋转
        String path = BitmapUtils.compress(uploadFilePath, context, 512, 512, outFilePath, degree);
        Logger.d("degree----"+ degree);
        return path;
    }



    public static void uploadPhotoToServer(final Context context, String filePath, final IUploadSuccess iUploadSuccess ){
        final Handler myHandler  = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what == MSG_UPLOAD_SUCCRSS){
                    String imageUrl = (String) msg.obj;
                    if(iUploadSuccess!= null){
                        iUploadSuccess.getDownloadUrl(imageUrl);
                    }
                }
            }
        };
        final KProgressHUD progressHUD = KProgressHUDUtils.showLoading(context, context.getString(R.string.s77));
        BizDataRequest.asyncUploadAvata(context, filePath, new AliOSS.OnUpload() {
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

            }

            @Override
            public void onProgress(long currentSize, long totalSize) {

            }
        });
    }

    public static int position = 0;
    public static void uploadPhotoToServer(final Context context, List<String> filePath, final OnPostImg iUploadSuccess ){

        final Handler myHandler  = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what == MSG_UPLOAD_SUCCRSS){
                    String imageUrl = (String) msg.obj;
                    position ++;
                    if(iUploadSuccess!= null){
                        iUploadSuccess.getDownloadUrl(imageUrl);
                    }
                }
            }
        };
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
            }

            @Override
            public void onProgress(long currentSize, long totalSize) {

            }
        });
    }

    public interface IPhotoPath {
        //获得拍照后相片的地址
        void getTakePhotoPath(String path);
    }

    public interface IUploadSuccess {
        //获得拍照后相片的地址
        void getDownloadUrl(String serverImagePath);
    }

    public interface OnPostImg {
        //获得拍照后相片的地址
        void getDownloadUrl(String serverImagePath);
        void onFailed();
    }

    public interface OnClickItemListener{
        void onClickItemListener(int position);
    }

}
