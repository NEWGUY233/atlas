package com.atlas.crmapp.presenter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.atlas.crmapp.R;
import com.atlas.crmapp.activity.index.IndexActivity;
import com.atlas.crmapp.common.Constants;
import com.atlas.crmapp.common.GlobalParams;
import com.atlas.crmapp.common.Utils;
import com.atlas.crmapp.eventbus.EventBusFactory;
import com.atlas.crmapp.model.PersonInfoJson;
import com.atlas.crmapp.model.VisibleCompanysJson;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.atlas.crmapp.network.NetworkUtil;
import com.atlas.crmapp.register.RecordLoginActivity;
import com.atlas.crmapp.register.RegInfoActivity_;
import com.atlas.crmapp.register.RegisterActivity;
import com.atlas.crmapp.tim.model.FriendshipInfo;
import com.atlas.crmapp.util.ClickUtil;
import com.atlas.crmapp.util.ContextUtil;
import com.atlas.crmapp.util.RegisterCommonUtils;
import com.atlas.crmapp.util.SpUtil;
import com.atlas.crmapp.util.StringUtils;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMManager;

/**
 * Created by Administrator on 2018/3/20.
 */

public class RecordLoginActivityPresenter extends BasePresenter {
    RecordLoginActivity activity;
    Context context;
    String zipCode = "";

    public RecordLoginActivityPresenter(RecordLoginActivity fragment){
        this.activity = fragment;
        context = fragment;
    }

    public void postCode(String zipCode,String mPhoneNo){
        Log.i("postCodeVoice","zipCode = " + zipCode);
        if(StringUtils.isEmpty(mPhoneNo)){
            showToast(context,context.getString(R.string.input_phone_error));
            return;
        }
//        if ()
        this.zipCode = StringUtils.getBizCode(mPhoneNo);
        BizDataRequest.requestSendCode(context,zipCode, mPhoneNo, new BizDataRequest.OnRequestResult() {
            @Override
            public void onSuccess() {
                activity.timer.start();
                showToast(context,context.getString(R.string.t32));
                activity.showVoice();
            }

            @Override
            public void onError(DcnException error) {
                activity.btnCode.setEnabled(true);
                if(error.getCode() == Constants.NetWorkCode.NO_NET_WORK){
                    showToast(context, context.getString(R.string.t33));
                }else{
                    showToast(context, context.getString(R.string.t34));
                }

            }
        });
    }


    public void postCodeVoice(String zipCode,String mPhoneNo){
        Log.i("postCodeVoice","zipCode = " + zipCode);
        if(StringUtils.isEmpty(mPhoneNo) || mPhoneNo.length() != 11){
            showToast(context,context.getString(R.string.t31));
            return;
        }
        this.zipCode = StringUtils.getBizCode(mPhoneNo);
        BizDataRequest.requestSendCode(context,zipCode, mPhoneNo, new BizDataRequest.OnRequestResult() {
            @Override
            public void onSuccess() {
                activity.timer.start();
                showToast(context,context.getString(R.string.t32));
            }

            @Override
            public void onError(DcnException error) {
                activity.btnCode.setEnabled(true);
                if(error.getCode() == Constants.NetWorkCode.NO_NET_WORK){
                    showToast(context, context.getString(R.string.t33));
                }else{
                    showToast(context, context.getString(R.string.t34));
                }

            }
        },"VOICE");
    }

    public void reg(final String mMobile, final String mCode){

        BizDataRequest.requestValidCode(context,zipCode, mMobile, mCode, new BizDataRequest.OnRequestValidCode() {
            @Override
            public void onSuccess(PersonInfoJson personInfoJson) {
                activity.mPersonInfoJson = personInfoJson;
                auth(mMobile,mCode,personInfoJson.nick);

            }

            @Override
            public void onError(DcnException error) {
                if(error.getCode() == Constants.NetWorkCode.NO_NET_WORK){
                    showToast(context,context.getString(R.string.t33));
                }else{
                    showToast(context,error.getDescription());
                }
            }
        });
    }

    private void auth(final String account, String pwd, final String nickName){
        NetworkUtil.authUserForAccessToken(context, account, pwd, new NetworkUtil.OnAuthForToken() {
            @Override
            public void onSuccess(String accessToken, String refreshToken) {
                GlobalParams.getInstance().setAccessToken(accessToken);
                activity.mPersonInfoJson.setMobile(account);
                activity.mPersonInfoJson.setNick(nickName);
                Utils.storeAccountToken(context, accessToken, refreshToken, activity.mPersonInfoJson);
                GlobalParams.getInstance().setIsLogin(true);
                SpUtil.putString(context,SpUtil.PHONE,activity.mPersonInfoJson.mobile);
                SpUtil.putString(context,SpUtil.NICK,activity.mPersonInfoJson.nick);
                SpUtil.putString(context,SpUtil.ICON,activity.mPersonInfoJson.avatar);
                SpUtil.putString(context,SpUtil.AREA,activity.mPersonInfoJson.getZipCode());
                SpUtil.putLong(context,SpUtil.ID,activity.mPersonInfoJson.getId());
                SpUtil.putString(context,SpUtil.COMPANY,activity.mPersonInfoJson.getCompany());
                BizDataRequest.requestGetVisibleCompanys(context, true, null,new BizDataRequest.OnVisibleCompanysRequestResult() {
                    @Override
                    public void onSuccess(VisibleCompanysJson visibleCompanysJson) {
                        if (visibleCompanysJson.rows.size() > 0) {
                            activity.getGlobalParams().setHasContract(true);
                        } else {
                            activity.getGlobalParams().setHasContract(false);
                        }
                        initData(nickName);
                    }

                    @Override
                    public void onError(DcnException error) {
                        activity.getGlobalParams().setHasContract(false);
                        initData(nickName);
//                        SharedPreferences
                    }
                });

            }

            @Override
            public void onError(DcnException error) {
                GlobalParams.getInstance().setIsLogin(false);
            }
        });
    }

    public void initData(String nickName) {
        EventBusFactory.getBus().post(activity.mPersonInfoJson);
        if (TextUtils.isEmpty(nickName)) {
            if (ClickUtil.isFastDoubleClick()) {
                return;
            }
//            Intent intent = new Intent(RegisterActivity.this, RegInfoActivity.class);
            Intent intent = new Intent(context, RegInfoActivity_.class);
            intent.putExtra("id",activity.mPersonInfoJson.id);
            context.startActivity(intent);
            activity.finish();
        } else {
            //HuanXinManager.login(getGlobalParams().getPersonInfoJson().getId()+"",getGlobalParams().getPersonInfoJson().getUid());
            getTimSig();
//            loginTim("");
        }

    }

    private void getTimSig(){
        BizDataRequest.getTimSig(activity, String.valueOf(activity.mPersonInfoJson.getId()), new BizDataRequest.OnRequestTimSig() {
            @Override
            public void onSuccess(String string) {
                loginTim(string);
            }

            @Override
            public void onError(DcnException error) {

            }
        });
    }

    private void loginTim(String sig){
        String count = String.valueOf(activity.mPersonInfoJson.getId());
        String psw = sig.replaceAll("\"","");
//        String count = "86-18223175029";
//        String psw = Constant.SINGE;
        Log.i("TIMManager_login","count = " + count + " ; psw = " + psw);
        SpUtil.putString(ContextUtil.getUtil().getContext(),SpUtil.TIM,count);
        TIMManager.getInstance().login(count,psw,new TIMCallBack(){

            @Override
            public void onError(int i, String s) {
                Log.i("TIMManager_login","s = " + s + " ; i = " + i );
            }

            @Override
            public void onSuccess() {
//                FriendshipInfo.getInstance().refresh();
                SpUtil.putBoolean(context, "isLogin", true);
                RegisterCommonUtils.loginRegister(context, activity.mPersonInfoJson);
                GlobalParams.getInstance().setPersonInfoJson(activity.mPersonInfoJson);
//            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                Intent intent = new Intent(context, IndexActivity.class);
                context.startActivity(intent);
                activity.finish();
            }
        });
    }
}
