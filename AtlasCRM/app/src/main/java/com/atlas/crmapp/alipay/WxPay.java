package com.atlas.crmapp.alipay;

import android.content.Context;
import android.widget.Toast;

import com.atlas.crmapp.R;
import com.orhanobut.logger.Logger;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONObject;

/**
 * Created by Alex on 2017/4/24.
 */

public class WxPay {
    public static void wxPay(Context context, String content){
        //IWXAPI api = WXAPIFactory.createWXAPI(context, "wx03265a3e5e061e25");

        final IWXAPI msgApi = WXAPIFactory.createWXAPI(context, null);
        msgApi.registerApp("wx03265a3e5e061e25");

        IWXAPI api = WXAPIFactory.createWXAPI(context, "wx03265a3e5e061e25");

        Toast.makeText(context, R.string.text_89, Toast.LENGTH_SHORT).show();
        try{
            Logger.d("get server pay params:" + content);
            JSONObject json = new JSONObject(content);
            if(null != json && !json.has("retcode") ){
                PayReq req = new PayReq();
                //req.appId = "wxf8b4f85f3a794e77";  // 测试用appId
                req.appId			= json.getString("appid");
                req.partnerId		= json.getString("partnerid");
                req.prepayId		= json.getString("prepayid");
                req.nonceStr		= json.getString("noncestr");
                req.timeStamp		= json.getString("timestamp");
                req.packageValue	= json.getString("package");
                req.sign			= json.getString("sign");
                req.extData			= "app data"; // optional
                //Toast.makeText(RechargeActivity.this, "正常调起支付", Toast.LENGTH_SHORT).show();
                // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                api.sendReq(req);
            }else{
                Logger.e("PAY_GET " + "返回错误"+json.getString("retmsg"));
                //Toast.makeText(RechargeActivity.this, "返回错误"+json.getString("retmsg"), Toast.LENGTH_SHORT).show();
            }

        }catch(Exception e){
            Logger.e("PAY_GET " + "异常："+e.getMessage());
            //Toast.makeText(RechargeActivity.this, "异常："+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
