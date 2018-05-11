package com.atlas.crmapp.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.atlas.crmapp.model.ResponseOpenOrderJson;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;

/**
 * Created by Administrator on 2018/2/27.
 */

public class PrintService extends Service {
    private MyBinder binder = new MyBinder();
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    long currentTime;
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("PrintService","onCreate");
        currentTime = System.currentTimeMillis();
        Toast.makeText(this,"start service " + currentTime,Toast.LENGTH_LONG).show();
    }

    public void setOnOrderCheck(OnOrderCheck onOrderCheck) {
        this.onOrderCheck = onOrderCheck;
        startLoop();
    }

    public class MyBinder extends Binder {
        public PrintService getService() {
            return PrintService.this;
        }
    }

    private OnOrderCheck onOrderCheck;
    public interface OnOrderCheck{
        void onOrderCheck(ResponseOpenOrderJson order);
    }


    private boolean isCheck = true;
    private void startLoop(){
        new Thread(){
            @Override
            public void run() {
                while (isCheck){
                    queryOrder();
                    try {
                        sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        isCheck = false;
                    }
                }

            }
        }.start();
    }


    public void queryOrder(){
        BizDataRequest.requestCodeOrder(this, currentTime, 0, new BizDataRequest.OnResponseOpenOrderJson() {
            @Override
            public void onSuccess(ResponseOpenOrderJson responseOpenOrderJson) {
                if (responseOpenOrderJson == null)
                    return;
                Bundle bundle = new Bundle();
                bundle.putSerializable("order",responseOpenOrderJson);
                Message msg = new Message();
                msg.what = 0;
                msg.setData(bundle);
                handler.sendMessage(msg);
                isCheck = false;
            }

            @Override
            public void onError(DcnException error) {

            }
        });
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    if (onOrderCheck != null){
                        onOrderCheck.onOrderCheck((ResponseOpenOrderJson) msg.getData().getSerializable("order"));

                    }
                    break;
            }
        }
    };

}
