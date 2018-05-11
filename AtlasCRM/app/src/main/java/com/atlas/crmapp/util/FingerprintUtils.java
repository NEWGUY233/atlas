package com.atlas.crmapp.util;

/**
 * Created by hoda on 2017/9/20.
 */

public class FingerprintUtils {

//    public static  void startIdentify(FingerprintIdentify fingerprintIdentify, final OnFigerPrintListener onFigerPrintListener){
//        if(fingerprintIdentify ==null){
//            onFigerPrintListener.onFingerFail();
//            return;
//        }
//        fingerprintIdentify.startIdentify(Constants.ORDER_FINGER_PAY.MAX_AVAILABLE_TIMES, new BaseFingerprint.FingerprintIdentifyListener() {
//            @Override
//            public void onSucceed() {
//                onFigerPrintListener.onFingerSuccess();
//
//
//                Logger.d(getString(R.string.succeed));
//                checkPopup.dismiss();
//                GlobalParams.getInstance().getPersonInfoJson().setOpenfingerprint(Constants.ORDER_FINGER_PAY.AGREE_FINGER_PAY);
//                requestUpdateUserInfo();
//            }
//
//            @Override
//            public void onNotMatch(int availableTimes) {
//                if (availableTimes == 0) {
//                    scFingerPay.setChecked(false);
//                    checkPopup.setFingerTip(PaySecurityActivity.this.getString(R.string.try_again));
//                    Logger.d(getString(R.string.not_match, availableTimes));
//                }
//            }
//
//            @Override
//            public void onFailed(boolean isDeviceLocked) {
//                Logger.d(getString(R.string.failed) + " " + isDeviceLocked);
//            }
//
//            @Override
//            public void onStartFailedByDeviceLocked() {
//                Logger.d(getString(R.string.start_failed));
//            }
//        });
//    }


    public interface OnFigerPrintListener{
        void onFingerSuccess();
        void onFingerFail();
    }
}
