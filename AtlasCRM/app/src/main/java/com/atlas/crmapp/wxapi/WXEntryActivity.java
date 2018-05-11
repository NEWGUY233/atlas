package com.atlas.crmapp.wxapi;

import android.app.Activity;
import android.util.Log;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.umeng.weixin.callback.WXCallbackActivity;

/**
 * Created by Administrator on 2017/6/13.
 */

public class WXEntryActivity extends WXCallbackActivity implements IWXAPIEventHandler {


    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        Log.i("onResp","WXEntryActivity");
    }
}
