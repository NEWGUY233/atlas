package com.atlas.crmapp.alipay;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.alipay.sdk.app.PayTask;
import com.atlas.crmapp.model.PayInfoJson;
import com.atlas.crmapp.model.ResponseOpenOrderJson;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;

import java.lang.ref.WeakReference;
import java.util.Map;

/**
 * Created by Alex on 2017/4/18.
 */

public class Alipay {

    /** 支付宝支付业务：入参app_id */
    public String APPID = "2017022005772755";
    public String SELLERID = "sea.liang@byron.com.hk";
    public String NOTIFY_URL ="";
    /** 商户私钥，pkcs8格式 */
    /** 如下私钥，RSA2_PRIVATE 或者 RSA_PRIVATE 只需要填入一个 */
    /** 如果商户两个都设置了，优先使用 RSA2_PRIVATE */
    /** RSA2_PRIVATE 可以保证商户交易在更加安全的环境下进行，建议使用 RSA2_PRIVATE */
    /** 获取 RSA2_PRIVATE，建议使用支付宝提供的公私钥生成工具生成， */
    /** 工具地址：https://doc.open.alipay.com/docs/doc.htm?treeId=291&articleId=106097&docType=1 */
    public String RSA2_PRIVATE = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCWZv04byKMLWDoxXjwR/vQoxeQ1VZZO3vuLDQSuMEPJDs8sZkCcEqP8ZwZc9sxgSzLV3iZLKLNkDEzCjvyi2dVnUu3NLrt42TGC3Xqmr3R+otiYZtoPydVBE91SBl/zoW5mCzymK/pUxl9nGmmwvuuaYrAWhxSa83QK8b0VGWkebMa7JXs0jpN9EW/qHsXleMy1aG/2Np/Gyzg0b4995QqE/VAbFBeG/osFbCC3xRr7HMFTa0gfsDMLRm+KEVznwIal53Li7H1Ji9S5tZZGiGo9WZLf/bYdAR4tUJShAnr0GnrkldWMQxz6S0vmtNEePdg3IP+156AidIBA/zm8xHBAgMBAAECggEBAIcqk5PuPOwhtCA9qEy5RJPinsEpGWskTGJmE35n0AldlGcdbNeGN1xZ1OIC/xDkeG9ecyGlvcJkPfMS1YFKy1lj/bHuj7hPTje2AQkQfFuDgVSvR81ORHjhYM+UNmZoIkIDgxGbKyzT4+rBcSnPvgSnEW2BDkb8X4EgdrMKTjabTHp9OFpknFYh5Fpk8pm8aB7SB/AMOVUfuKvkTH24lYDXDUHpWlup4hd2kGiwzhopz6rHw/BbsQm2EHAcnt3LM5B4EdTuGwwnJyVTQThOs8ZA4WGy85SjiKgn5TRmoKkAVTQyljfMvLBM2DQNAkag1BeI8YDd5aN7m48emslps4ECgYEA4r7xNrlOwEqX4yX9MmFqi0fGHcHJrrHwFIgph0GJwHSuTvL3xquO4R2r6Wy/CcuFD7oXsM+Oo7ECsoC8bkrDIySbn+OwnmjqIA47q/LWmZm35WBG/higTMDCUYrWI+fMAkgnce/SA6TPVvbD465hJJS7xWtvIAMwtF6nQc/NjkkCgYEAqc6KHaqaeaCdvKrJ40LpyA+qiNNMN0vhrMohl+mw4SIQ1yB+Dje+goo5g8VnykAwrv+eeyEITxWxZWnj189NqAXH9oMlz2TCQ+BMLMZJdbIeOBYe+5XD9dcM1MCjgXlgg9GYfTH95ZJfqlpxAlR5Oa1pOxYeYBw979qPzXVpR7kCgYBy4FAu7HVUgLYw88cyph4nkGZ0HwECC85ZaJ65kt+qbLL+9qPHuJU7wh+dtGWZHc49KIfmIVHf5sc6DnTo3+G7RUm/GMkbiIXNVcivlelZioKbqEpvY6SbHjGUxGU3bbFzGoE5fUoGZJJTOdQG3wsCUIRNQd/b9SHQz/+nHRPEWQKBgDxrcDuEE7kJTHbwMiu6CGMWJ/DNdkYKqv8HYRKjpTtGGsXhGtlGPnRGaJaZbAfL9UafT62yIDm7l0Zlehu0+IW7oN325kI4MONj8NbRqxvEx/ne7SFJtdNItJkS/lq4bH4a2u5ZFmVUQtPjoGha1FOQf2v1v9Mk2Wj51McfatmJAoGAI+XJ9Z9zRyFfGaw+EHFzZaapW4sziJK38kGy40eikPpxiICmvdjhPLdJwYdignDmScMbAjTfye6uyAD7fno2H/auVFjh33hN8TvOgqTLKyPFHMFN/et07MrY7BzFiqvfxW1O7NyTK2vaX8zLaKQwfMB9WEzH3VPQCMc5ypa7w8w=";

    public static final String RSA_PRIVATE = "";

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;


    private WeakReference<Activity> mActivity;
    private Context context;
    private ResponseOpenOrderJson confirmOrder;

    private OnAlipayListener mListener;

    public void setListener(OnAlipayListener l) {
        mListener = l;
    }

    /**
     * 支付回调接口
     *
     * @author lenovo
     *
     */
    public interface OnAlipayListener {
        /**
         * 支付成功
         */
         void onSuccess();
        /**
         * 支付取消
         */
         void onCancel();
        /**
         * 等待确认
         */
         void onWait();
    }

/*    public Alipay(Activity activity,String appID,String sellerID,String notifyUrl,String rsa2) {
        mActivity = new WeakReference<Activity>(activity);
        this.context = activity;
        APPID = appID;
        SELLERID = sellerID;
        NOTIFY_URL = notifyUrl;
        RSA2_PRIVATE = rsa2;
    }*/

    public Alipay(Activity activity, ResponseOpenOrderJson confirmOrder){
        this.context = activity;
        this.mActivity = new WeakReference<Activity>(activity);
        this.confirmOrder = confirmOrder;
    }

    public void startPay(){
        getPayInfo();
    }

    private void getPayInfo(){
        BizDataRequest.requestPayInfo(context, new BizDataRequest.OnResponsePayInfoJson() {
            @Override
            public void onSuccess(PayInfoJson payInfoJson) {
                APPID = payInfoJson.getAlipayPartner();
                RSA2_PRIVATE= payInfoJson.getAlipayRsaPrivate();
                SELLERID = payInfoJson.getAlipaySeller();
                NOTIFY_URL = payInfoJson.getAlipayNotifyUrl();
                pay(confirmOrder);
            }

            @Override
            public void onError(DcnException error) {

            }
        });
    }




    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        if (mListener != null) mListener.onSuccess();
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，
                        // 最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            if (mListener != null) mListener.onWait();
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            if (mListener != null) mListener.onCancel();
                        }
                    }
                    break;
                }

                default:
                    break;
            }
        };
    };

    public void pay(ResponseOpenOrderJson confirmOrder){
        String order = getOrderStr(confirmOrder.getActualAmount(), confirmOrder.getBriefing(), confirmOrder.getBriefing(), confirmOrder.getId());
        pay(order);
    }

    /**
     * 支付宝支付业务
     *
     * @param
     */
    public void pay(String order) {

        /**
         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
         *
         * orderInfo的获取必须来自服务端；
         */
        boolean rsa2 = (RSA2_PRIVATE.length() > 0);
        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID, rsa2,SELLERID,order,NOTIFY_URL);
        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);

        String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
        String sign = OrderInfoUtil2_0.getSign(params, privateKey, rsa2);
        final String orderInfo = orderParam + "&" + sign;

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(mActivity.get());
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Log.i("msp", result.toString());

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * get the sdk version. 获取SDK版本号
     *
     */
    public void getSDKVersion() {
        PayTask payTask = new PayTask(mActivity.get());
        String version = payTask.getVersion();
    }

    private String getOrderStr(double price, String subject, String body, long OrderId) {
        String order = "{\"timeout_express\":\"30m\",\"product_code\":\"QUICK_MSECURITY_PAY\",\"total_amount\":\"" +price
                +"\",\"subject\":\""+subject
                +"\",\"body\":\""+body
                +"\",\"out_trade_no\":\"" + OrderId
                + "\"}";
        return order;
    }
}
