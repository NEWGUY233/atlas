package com.atlas.crmapp.usercenter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.common.Constants;
import com.atlas.crmapp.common.GlobalParams;
import com.atlas.crmapp.common.activity.BaseStatusActivity;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.atlas.crmapp.util.StringUtils;
import com.atlas.crmapp.view.popupwindow.FingerprintCheckPopup;
import com.atlas.crmapp.view.popupwindow.InputPwdPopup;
import com.orhanobut.logger.Logger;
import com.wei.android.lib.fingerprintidentify.FingerprintIdentify;
import com.wei.android.lib.fingerprintidentify.base.BaseFingerprint;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import razerdp.basepopup.BasePopupWindow;


/**
 * Created by hoda on 2017/9/5.
 */

public class PaySecurityActivity extends BaseStatusActivity {
    @BindView(R.id.item_set_pws)
    LinearLayout itemSetPws;
    @BindView(R.id.sc_finger_pay)
    SwitchCompat scFingerPay;
    @BindView(R.id.item_set_finger)
    LinearLayout itemSetFinger;
    @BindView(R.id.tv_is_opent_free_pay)
    TextView tvIsOpentFreePay;

    private FingerprintIdentify mFingerprintIdentify;
    private Throwable exception;

    private static final String KEY_IS_SETTRING_PSW = "KEY_IS_SETTRING_PSW";
    private boolean isSettingedPsw;
    private AlertDialog pswDialog ;


    @OnClick(R.id.item_set_free_pws)
    void onClickToFreePws(){
        showInputPwdPopup();
    }

    @OnClick(R.id.item_set_pws)
    void onClickSetPws(){
        startActivity(new Intent(this, PayPasswordActivity.class));
    }


    public static void newInstance (Context context, boolean isSettingedPsw){

        Intent intent = new Intent(context, PaySecurityActivity.class);
        intent.putExtra(KEY_IS_SETTRING_PSW, isSettingedPsw);
        context.startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_security);
        ButterKnife.bind(this);
        setTitle(getString(R.string.t96));
        if(getIntent() != null){
            isSettingedPsw = getIntent().getBooleanExtra(KEY_IS_SETTRING_PSW, false);
        }
        initFingerprint();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateViews();
    }

    private void updateViews(){
        int freeCount = GlobalParams.getInstance().getPersonInfoJson().getNoCountPassword();
        if(freeCount >= 0){
            tvIsOpentFreePay.setText(freeCount + getString(R.string.t97));
        }else{
            tvIsOpentFreePay.setText(R.string.t98);
        }

    }

    private void initFingerprint() {
        mFingerprintIdentify = new FingerprintIdentify(getApplicationContext(), new BaseFingerprint.FingerprintIdentifyExceptionListener() {
            @Override
            public void onCatchException(Throwable exception) {
                Logger.d("\nException：" + exception.getLocalizedMessage());
                PaySecurityActivity.this.exception = exception;
            }
        });
        updateActivityViews();

    }




    @Override
    protected void updateActivityViews() {
        super.updateActivityViews();
        if (mFingerprintIdentify == null || exception != null || !mFingerprintIdentify.isFingerprintEnable()) {
            Logger.d(getResources().getString(R.string.not_support));
            itemSetFinger.setVisibility(View.GONE);
            return;
        } else {
            itemSetFinger.setVisibility(View.VISIBLE);
            if (Constants.ORDER_FINGER_PAY.NO_AGREE_FINGER_PAY.equals(GlobalParams.getInstance().getPersonInfoJson().getOpenfingerprint())) {
                scFingerPay.setChecked(false);
            } else {
                scFingerPay.setChecked(true);
            }
            scFingerPay.setOnCheckedChangeListener(onCheckedChangeListener);
        }

        if (Constants.ORDER_FINGER_PAY.NO_AGREE_FINGER_PAY.equals(GlobalParams.getInstance().getPersonInfoJson().getOpenfingerprint())) {
            scFingerPay.setChecked(false);
        } else {
            scFingerPay.setChecked(true);
        }

    }


    CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if(!isSettingedPsw){

                if(pswDialog == null){
                    pswDialog = new AlertDialog.Builder(PaySecurityActivity.this)
                            .setTitle(R.string.t90)
                            .setMessage(R.string.t91)
                            .setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialoginterface, int i) {

                                    PaySecurityActivity.this.startActivity(new Intent(PaySecurityActivity.this, PayPasswordActivity.class));
                                    scFingerPay.setOnCheckedChangeListener(null);
                                    scFingerPay.setChecked(false);
                                    scFingerPay.setOnCheckedChangeListener(onCheckedChangeListener);
                                }
                            })
                            .setNegativeButton(R.string.cancel,new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    pswDialog.dismiss();
                                    scFingerPay.setOnCheckedChangeListener(null);
                                    scFingerPay.setChecked(false);
                                    scFingerPay.setOnCheckedChangeListener(onCheckedChangeListener);
                                }
                            })
                            .setCancelable(false)
                            .show();
                }else{
                    pswDialog.show();
                }

                return;
            }
            if (b) {
                if(Constants.ORDER_FINGER_PAY.NO_AGREE_FINGER_PAY .equals(GlobalParams.getInstance().getPersonInfoJson().getOpenfingerprint())){
                    startFingerIdentify();
                }
            } else {
                GlobalParams.getInstance().getPersonInfoJson().setOpenfingerprint(Constants.ORDER_FINGER_PAY.NO_AGREE_FINGER_PAY);
                requestUpdateUserInfo();
            }
        }
    };

    private void startFingerIdentify() {
        if (mFingerprintIdentify == null) {
            return;
        }

        final FingerprintCheckPopup checkPopup = new FingerprintCheckPopup(this);
        checkPopup.showPopupWindow();
        checkPopup.updateView(true, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPopup.dismiss();
            }
        });


        checkPopup.setOnDismissListener(new BasePopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if(Constants.ORDER_FINGER_PAY.NO_AGREE_FINGER_PAY.equals(GlobalParams.getInstance().getPersonInfoJson().getOpenfingerprint())){
                    updateActivityViews();
                }
            }
        });
        Logger.d("开始验证");
        mFingerprintIdentify.startIdentify(Constants.ORDER_FINGER_PAY.MAX_AVAILABLE_TIMES, new BaseFingerprint.FingerprintIdentifyListener() {
            @Override
            public void onSucceed() {
                Logger.d(getString(R.string.succeed));
                GlobalParams.getInstance().getPersonInfoJson().setOpenfingerprint(Constants.ORDER_FINGER_PAY.AGREE_FINGER_PAY);
                requestUpdateUserInfo();
                showToast(getString(R.string.open_success));
                checkPopup.dismiss();
            }

            @Override
            public void onNotMatch(int availableTimes) {
                if (availableTimes == 0) {
                    scFingerPay.setChecked(false);
                    checkPopup.dismiss();
                    Logger.d(getString(R.string.not_match, availableTimes));
                    showToast(getString(R.string.device_lock_again));
                }else{
                    checkPopup.setFingerTip(PaySecurityActivity.this.getString(R.string.try_again));
                    showToast(getString(R.string.try_again));
                }
            }

            @Override
            public void onFailed(boolean isDeviceLocked) {
                checkPopup.dismiss();
                scFingerPay.setChecked(false);
                showToast(getString(R.string.device_lock_again));
                Logger.d(getString(R.string.device_lock_again) + " " + isDeviceLocked);
            }

            @Override
            public void onStartFailedByDeviceLocked() {
                checkPopup.dismiss();
                scFingerPay.setChecked(false);
                showToast(getString(R.string.device_lock_again));
            }
        });
    }


    private void requestUpdateUserInfo() {

        BizDataRequest.requestModifyUserInfo(PaySecurityActivity.this, GlobalParams.getInstance().getPersonInfoJson(), new BizDataRequest.OnRequestResult() {
            @Override
            public void onSuccess() {
                updateActivityViews();
            }

            @Override
            public void onError(DcnException error) {

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mFingerprintIdentify != null)
            mFingerprintIdentify.cancelIdentify();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mFingerprintIdentify != null)
            mFingerprintIdentify.cancelIdentify();
    }



    private void showInputPwdPopup(){
        InputPwdPopup inputPwdPopup = new InputPwdPopup(this, null);
        inputPwdPopup.setOnPwdInputCompleteListener(new InputPwdPopup.OnPwdInputCompleteListener() {
            @Override
            public void onPwdInputComplete(String pwd) {
                requestCheckUserAccountPassword(pwd);
            }
        });
        inputPwdPopup.setPopupWindowFullScreen(true);
        inputPwdPopup.showPopupWindow();
    }


    private void requestCheckUserAccountPassword(String pwd){

        BizDataRequest.requestCheckUserAccountPassword(this, StringUtils.md5(pwd), new BizDataRequest.OnResponseCheckUserAccountPassword() {
            @Override
            public void onSuccess(int result) {
                if(result == 1){
                    startActivity(new Intent(PaySecurityActivity.this, FreePswAcitivity.class));
                }else{
                    showToast(getString(R.string.t99));
                }
            }

            @Override
            public void onError(DcnException error) {

            }
        });
    }





}
