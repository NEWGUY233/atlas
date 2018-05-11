package com.atlas.crmapp.service;

/**
 * Created by Administrator on 2017/6/8.
 */


/**
 * Created by Kodulf on 2017/5/8.
 */
import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.orhanobut.logger.Logger;

import java.io.File;

/**
 * http://blog.csdn.net/rodulf/article/details/51706788
 */

public class UpdateService extends Service {
    private String apkFilePath;

    public UpdateService() {

    }

    /** 安卓系统下载类 **/
    DownloadManager manager;

    /** 接收下载完的广播 **/
    DownloadCompleteReceiver receiver;

    /** 初始化下载器 **/
    private void initDownManager() {

        manager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);

        receiver = new DownloadCompleteReceiver();

        //设置下载地址
        String urlPath = "http://testassets.crm.atlasoffice.cn/atlas/apk/atlasCRM.apk";
        Uri parse = Uri.parse(urlPath);
        DownloadManager.Request down = new DownloadManager.Request(parse);

//     百度音乐           Uri.parse("http://gdown.baidu.com/data/wisegame/fd84b7f6746f0b18/baiduyinyue_4802.apk"));
//    乐视体育
        //Uri.parse("http://122.228.237.132/apk.r1.market.hiapk.com/data/upload/apkres/2016/6_12/16/com.lesports.glivesports_040405.apk"));


        // 设置允许使用的网络类型，这里是移动网络和wifi都可以
        down.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);

        // 下载时，通知栏显示途中
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            down.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        }

        // 显示下载界面
        down.setVisibleInDownloadsUi(true);

        // 设置下载后文件存放的位置


        String apkName = parse.getLastPathSegment();
        down.setDestinationInExternalFilesDir(this, Environment.DIRECTORY_DOWNLOADS, apkName);
        apkFilePath = Environment.getExternalStorageDirectory() + File.separator + Environment.DIRECTORY_DOWNLOADS + File.separator + apkName;

        // 将下载请求放入队列
        manager.enqueue(down);

        //注册下载广播
        registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // 调用下载
        initDownManager();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onDestroy() {

        // 注销下载广播
        if (receiver != null)
            unregisterReceiver(receiver);

        super.onDestroy();
    }

    // 接受下载完成后的intent
    class DownloadCompleteReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            //判断是否下载完成的广播
            if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {



                //获取下载的文件id
                long downId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                Log.d("kodulf","id="+downId);

                //自动安装apk
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    Uri uriForDownloadedFile = manager.getUriForDownloadedFile(downId);
                    Log.d("kodulf","uri="+uriForDownloadedFile);

                    installApkNew(uriForDownloadedFile);
                }

                //停止服务并关闭广播
                UpdateService.this.stopSelf();

            }
        }

        //安装apk
        protected void installApkNew(Uri uri) {
            /**
             * 通过隐式意图调用系统安装程序安装APK
             */
        /*File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "myApp.apk");*/
            Intent intent = new Intent(Intent.ACTION_VIEW);
            // 由于没有在Activity环境下启动Activity,设置下面的标签
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if(Build.VERSION.SDK_INT>=24) { //判读版本是否在7.0以上
                //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
                Logger.d("uri.getPath()--" + apkFilePath);
                Uri apkUri = FileProvider.getUriForFile(getApplicationContext(), "com.atlas.crmapp.app.install", new File(apkFilePath));
                //添加这一句表示对目标应用临时授权该Uri所代表的文件
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
            }else{
                intent.setDataAndType(uri, "application/vnd.android.package-archive");
            }
            startActivity(intent);
        }
    }
}