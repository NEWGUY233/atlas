package com.atlas.crmapp.commonactivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.atlas.crmapp.R;
import com.atlas.crmapp.alipay.Alipay;
import com.atlas.crmapp.alipay.WxPay;
import com.atlas.crmapp.coffee.PaySucceedActivity;
import com.atlas.crmapp.common.BroadcastKeys;
import com.atlas.crmapp.common.Constants;
import com.atlas.crmapp.common.GlobalParams;
import com.atlas.crmapp.common.activity.BaseStatusActivity;
import com.atlas.crmapp.eventbus.Event;
import com.atlas.crmapp.eventbus.EventBusFactory;
import com.atlas.crmapp.model.PersonInfoJson;
import com.atlas.crmapp.model.ResponseMyInfoJson;
import com.atlas.crmapp.model.ResponseOpenOrderJson;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.atlas.crmapp.usercenter.PayPasswordActivity;
import com.atlas.crmapp.util.StringUtils;
import com.atlas.crmapp.view.popupwindow.FingerprintCheckPopup;
import com.atlas.crmapp.view.popupwindow.InputPwdPopup;
import com.atlas.crmapp.view.popupwindow.OrderPayChannelPopup;
import com.atlas.crmapp.workplace.RentWorkPlaceDetailActivity;
import com.orhanobut.logger.Logger;
import com.wei.android.lib.fingerprintidentify.FingerprintIdentify;
import com.wei.android.lib.fingerprintidentify.base.BaseFingerprint;

import razerdp.basepopup.BasePopupWindow;


/**
 * Created by hoda on 2017/9/13.
 */

public class OrderPayActivity extends BaseStatusActivity {
    private static final String KEY_COFIRM = "KEY_COFIRM";
    private static final String KEY_CONTRACT_ID = "KEY_CONTRACT_ID";
    private static final String KEY_DISCOUNT = "KEY_DISCOUNT";
    private static final String KEY_DEDUCTION = "KEY_DEDUCTION";
    private static final String KEY_PROMO_ID = "KEY_PROMO_ID";
    private static final String KEY_PROMO_TYPE = "KEY_PROMO_TYPE";

    private long contractId;// 等于0时使用企业支付
    private ResponseOpenOrderJson confirmOrder;
    private double discount;
    private long deduction;
    private long promoId;
    private String promoType;

    private BasePopupWindow basePopup ;

    private double balance;


    private FingerprintIdentify mFingerprintIdentify;
    private Throwable exception ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_pay);
        Intent intent = getIntent();
        if(intent != null){
            confirmOrder = (ResponseOpenOrderJson) intent.getSerializableExtra(KEY_COFIRM);
            contractId = intent.getLongExtra(KEY_CONTRACT_ID, 0);
            discount = confirmOrder.getDiscount();
            deduction = intent.getLongExtra(KEY_DEDUCTION, 0);
            promoId = intent.getLongExtra(KEY_PROMO_ID, 0);
            promoType = intent.getStringExtra(KEY_PROMO_TYPE);
        }

        initFingerprint();
        IntentFilter filter_dynamic = new IntentFilter();
        filter_dynamic.addAction("com.atlas.crmapp.paysuccess");
        registerReceiver(dynamicReceiver, filter_dynamic);
        //showOrderPayChannelPopup();
        if (contractId > 0) {
            payConfirm(confirmOrder.getId(),confirmOrder.getAmount(),confirmOrder.getDiscount(),confirmOrder.getActualAmount(),promoId, promoType,"ACCOUNT", contractId);
        }

    }

    @Override
    protected void prepareActivityData() {
        super.prepareActivityData();

        //获取余额
        BizDataRequest.requestMyInfo(OrderPayActivity.this, false, statusLayout, new BizDataRequest.OnResponseMyInfoJson() {
            @Override
            public void onSuccess(ResponseMyInfoJson responseMyInfoJson) {
                balance = responseMyInfoJson.getAmount();
                showOrderPayChannelPopup();
            }
            @Override
            public void onError(DcnException error) {
                balance = 0;
                showOrderPayChannelPopup();
            }
        });
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if(contractId == 0){
            prepareActivityData();
        }
    }


    public static void newInstance(Context context, ResponseOpenOrderJson confirmOrder, long contractId, long deduction, long promoId, String promoType){
        Intent intent = new Intent(context, OrderPayActivity.class);
        intent.putExtra(KEY_CONTRACT_ID , contractId);
        intent.putExtra(KEY_COFIRM, confirmOrder);
        intent.putExtra(KEY_DEDUCTION, deduction);
        intent.putExtra(KEY_PROMO_ID, promoId);
        intent.putExtra(KEY_PROMO_TYPE, promoType);
        context.startActivity(intent);
    }

    private boolean isStrat ;

    @Override
    protected void onPause() {
        super.onPause();
        isStrat = false;
        /*if(mFingerprintIdentify != null)
            mFingerprintIdentify.cancelIdentify();*/
    }


    @Override
    protected void onResume() {
        super.onResume();
        isStrat = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(dynamicReceiver);
        if(mFingerprintIdentify != null)
            mFingerprintIdentify.cancelIdentify();
    }

    private BroadcastReceiver dynamicReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("com.atlas.crmapp.paysuccess")) {
                int code = intent.getIntExtra("err_code", -2);
                String msg = intent.getStringExtra("err_msg");
                if(code == 0){
                    paySuccessed();
                } else {
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                }

            }

        }
    };

    private void initFingerprint(){
        mFingerprintIdentify = new FingerprintIdentify(getApplicationContext(), new BaseFingerprint.FingerprintIdentifyExceptionListener() {
            @Override
            public void onCatchException(Throwable exception) {
                Logger.d("\nException：" + exception.getLocalizedMessage());
                OrderPayActivity.this.exception = exception;
            }
        });
    }

    @Override
    protected int setTopBar() {
        return NO_SHOW_TOP_BAR;
    }

    private void startShowPayFingerOrInputPopup(){
        PersonInfoJson personInfoJson = GlobalParams.getInstance().getPersonInfoJson();

       /* if(confirmOrder.getActualAmount() <= personInfoJson.getNoCountPassword()){//免密支付
            accountPay(confirmOrder.getId(), "", contractId, Constants.PAY_PASSWORD_STATUS.NOPASSWORD);
            return;
        }*/

        if(Constants.ORDER_FINGER_PAY.NO_AGREE_FINGER_PAY.equals(personInfoJson.getOpenfingerprint()) ){//如果 不支持指纹
            if(confirmOrder.getActualAmount() <= personInfoJson.getNoCountPassword()){//免密支付
                accountPay(confirmOrder.getId(), "", contractId, Constants.PAY_PASSWORD_STATUS.NOPASSWORD);
                return;
            }
            showInputPwdPopup();
            return;
        }

        if(exception != null || mFingerprintIdentify == null ||!mFingerprintIdentify.isFingerprintEnable()  ){
            Logger.d("\n" + getString(R.string.not_support));
            showInputPwdPopup();
            return;
        }

        Logger.d("\n" + "开始验证");
        showFingerprintCheckPopup(true);
        mFingerprintIdentify.startIdentify(Constants.ORDER_FINGER_PAY.MAX_AVAILABLE_TIMES, new BaseFingerprint.FingerprintIdentifyListener() {
            @Override
            public void onSucceed() {
                showFingerprintCheckPopup(false);
                Logger.d("验证通过");
                accountPay(confirmOrder.getId(), "", contractId, Constants.PAY_PASSWORD_STATUS.FINGER);
                Logger.d("\n" + getString(R.string.succeed));
            }

            @Override
            public void onNotMatch(int availableTimes) {
                if(availableTimes == 0){
                    showInputPwdPopup();
                    Logger.d("\n" + getString(R.string.not_match, availableTimes));
                }else{
                    showToast("指纹验证失败");
                }
            }

            @Override
            public void onFailed(boolean isDeviceLocked) {
                showInputPwdPopup();
                Logger.d("\n" + getString(R.string.failed) + " " + isDeviceLocked);
            }

            @Override
            public void onStartFailedByDeviceLocked() {
                showInputPwdPopup();
                Logger.d("\n" + getString(R.string.start_failed));
            }
        });
    }

    @Override
    protected void onRefresh(View view, int id) {
        super.onRefresh(view, id);
        requestPayConfirm();
    }

    private void requestPayConfirm(){
        payConfirm(confirmOrder.getId(),confirmOrder.getAmount(),confirmOrder.getDiscount(),confirmOrder.getActualAmount(),promoId ,promoType, channelType, contractId);
    }

    private String channelType;
    //支付渠道 popu
    private void showOrderPayChannelPopup() {
        OrderPayChannelPopup payChannelPopup = new OrderPayChannelPopup(this, confirmOrder.getDiscount(), confirmOrder, balance);
        payChannelPopup.setOnEnterPayConfirmListener(new OrderPayChannelPopup.OnEnterPayConfirmListener() {
            @Override
            public void onEnterPayConfirm(String channelType) {
                OrderPayActivity.this.channelType = channelType;
                requestPayConfirm();
            }
        });
        payChannelPopup.setPopupWindowFullScreen(true);
        payChannelPopup.setOnOutSideClick(onOutSideClick);
        payChannelPopup.showPopupWindow();
        basePopup = payChannelPopup;
        basePopup.setOnDismissListener(new BasePopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(isStrat && basePopup !=null && !basePopup.isShowing()){
                            OrderPayActivity.this.finish();
                        }
                    }
                }, 20000);
            }
        });

       /* basePopup.getClickToDismissView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                OrderPayActivity.this.finish();
                return false;
            }
        });*/

    }



    private View.OnClickListener onOutSideClick =  new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(basePopup != null){
                basePopup.dismiss();
            }
            finish();
        }
    };

    /**
     * 密码输入框
     */
    private void showInputPwdPopup(){
        showFingerprintCheckPopup(false);
        InputPwdPopup inputPwdPopup = new InputPwdPopup(this, confirmOrder);
        inputPwdPopup.setOnPwdInputCompleteListener(new InputPwdPopup.OnPwdInputCompleteListener() {
            @Override
            public void onPwdInputComplete(String pwd) {
                accountPay(confirmOrder.getId(), pwd, contractId, Constants.PAY_PASSWORD_STATUS.PASSWORD);
            }
        });
        inputPwdPopup.setPopupWindowFullScreen(true);
        inputPwdPopup.setOnOutSideClick(onOutSideClick);
        inputPwdPopup.showPopupWindow();
        basePopup = inputPwdPopup;


    }
    private FingerprintCheckPopup fingerprintCheckPopup = null;
    //是否显示指纹 popu
    private void showFingerprintCheckPopup(boolean isShow){
        if(fingerprintCheckPopup == null){
            fingerprintCheckPopup = new FingerprintCheckPopup(this);
            basePopup = fingerprintCheckPopup;
        }
        if(isShow){
            fingerprintCheckPopup.setOnOutSideClick(onOutSideClick);
            fingerprintCheckPopup.setPopupWindowFullScreen(true);
            fingerprintCheckPopup.showPopupWindow();
            fingerprintCheckPopup.updateView(false, onClickUsedPassword);
            basePopup = fingerprintCheckPopup;
        }else{
            if(fingerprintCheckPopup != null){
                fingerprintCheckPopup.dismiss();
            }
        }
    }

    private View.OnClickListener onClickUsedPassword = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            switch (id){
                case R.id.tv_used_password:
                    showInputPwdPopup();
                    break;
                case R.id.tv_cancel:
                    fingerprintCheckPopup.dismiss();
                    break;
            }

        }
    };



    /**
     * 支付宝支付业务
     * @param
     */
    private void aliPay(ResponseOpenOrderJson confirmOrder){
        Alipay alipay = new Alipay(this, confirmOrder);
        alipay.setListener(onAlipayListener);
        alipay.startPay();
    }

    Alipay.OnAlipayListener onAlipayListener = new Alipay.OnAlipayListener() {
        @Override
        public void onSuccess() {
            paySuccessed();
        }

        @Override
        public void onCancel() {
            OrderPayActivity.this.finish();
        }

        @Override
        public void onWait() {

        }
    };


    //支付确认
    private void payConfirm(long orderId, double amount, double discount, final double actualAmount, long promoId, String promoType, final String paymentMedthod,final long contractId){
        if(actualAmount == 0 && !Constants.PayChannel.ACCOUNT.equals(paymentMedthod)){
            showToast(getString(R.string.text_92));
            return;
        }
        BizDataRequest.requestConfirmOrder(this, orderId, amount, discount, actualAmount, promoId, promoType, paymentMedthod, contractId, deduction, new BizDataRequest.OnPayOrderRequestResult() {
            @Override
            public void onSuccess(String jsonData) {
                if(paymentMedthod.equals(Constants.PayChannel.ALIPAY)) {
                    aliPay(confirmOrder);
                    //payV2(getOrderStr(confirmOrder.getActualAmount(), confirmOrder.getBriefing(), confirmOrder.getBriefing(), confirmOrder.getId()));
                }else if(paymentMedthod.equals(Constants.PayChannel.WXPAY)){
                    WxPay.wxPay(OrderPayActivity.this , jsonData);
                }else if(paymentMedthod.equals(Constants.PayChannel.ACCOUNT)){

                    BizDataRequest.requestMyInfo(OrderPayActivity.this , false, null, new BizDataRequest.OnResponseMyInfoJson() {
                        @Override
                        public void onSuccess(ResponseMyInfoJson responseMyInfoJson) {
                            if(StringUtils.isEmpty(responseMyInfoJson.getPassword())){
                                startActivity(new Intent(OrderPayActivity.this , PayPasswordActivity.class));//无设置密码时跳转至 设置密码页面
                            }else{
                                //showPwdWindows(this.getWindow().getDecorView());
                                if(confirmOrder!= null){
                                    startShowPayFingerOrInputPopup();
                                }else{
                                    Logger.d("confirmOrder  is null");
                                }
                            }
                        }

                        @Override
                        public void onError(DcnException error) {

                        }
                    });

                }
            }
            @Override
            public void onError(DcnException error) {
                if(error!= null && Constants.NetWorkCode.NO_NET_WORK== error.getCode()){
                    showToast(getString(R.string.ble_net_error_msg));
                }
            }
        });
    }


    private void accountPay(final long orderId, final String pwd, final long contractId, final String payPasswordStatus){
        BizDataRequest.requestPaymentCheckCode(this, orderId, new BizDataRequest.OnResponsePaymentCheckCode() {
            @Override
            public void onSuccess(String checkCode) {

                BizDataRequest.requestAccountPayV2(OrderPayActivity.this, orderId, pwd, contractId, payPasswordStatus , checkCode, new BizDataRequest.OnRequestResult() {
                    @Override
                    public void onSuccess() {
                        statusLayout.showContent();
                        if(Constants.PAY_PASSWORD_STATUS.PASSWORD.equals(payPasswordStatus)){
                            showToast(getString(R.string.text_93));
                        }else if(Constants.PAY_PASSWORD_STATUS.FINGER.equals(payPasswordStatus)){
                            showToast(getString(R.string.text_94));
                        }else if(Constants.PAY_PASSWORD_STATUS.NOPASSWORD.equals(payPasswordStatus)){
                            showToast(getString(R.string.text_95));
                        }
                        paySuccessed();
                    }

                    @Override
                    public void onError(DcnException error) {
                        if(error!= null && Constants.NetWorkCode.NO_NET_WORK== error.getCode()){
                            showToast(getString(R.string.ble_net_error_msg));
                        }
                    }
                });
            }

            @Override
            public void onError(DcnException error) {
                //showMsgDialog(error);
                if(error!= null && Constants.NetWorkCode.NO_NET_WORK== error.getCode()){
                    showToast(getString(R.string.ble_net_error_msg));
                }
            }
        });

    }


    public void onClickDismiss(View view){
        finish();
    }

    private void paySuccessed(){
        EventBusFactory.getBus().post(new Event.EventObject(Constants.EventType.ORDER_COMPLETE, ""));
        Intent intent = new Intent(this, PaySucceedActivity.class);
        intent.putExtra("amount",confirmOrder.getActualAmount());
        Intent paySuccessIntent = new Intent(BroadcastKeys.INTENT_FILTER_PAY_SUCCESS);
        sendBroadcast(paySuccessIntent);
        this.startActivityForResult(intent, 999);
        setResult(RentWorkPlaceDetailActivity.RESULT_CODE);
        this.finish();
    }

}
