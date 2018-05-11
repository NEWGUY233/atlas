package com.atlas.crmapp.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Administrator on 2018/3/22.
 */

public class NetChangeBroadCaster extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobNetInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifiNetInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {
            // unconnect network
            if (onNetChanged != null)
                onNetChanged.onNetChanged(false);

        }else {
            // connect network
            if (onNetChanged != null)
                onNetChanged.onNetChanged(true);
        }
    }

    private OnNetChanged onNetChanged;

    public void setOnNetChanged(OnNetChanged onNetChanged) {
        this.onNetChanged = onNetChanged;
    }

    public interface OnNetChanged{
        void onNetChanged(boolean isConnected);
    }
}
