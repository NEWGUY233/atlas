package com.atlas.crmapp.presenter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.atlas.crmapp.R;
import com.atlas.crmapp.activity.index.IndexActivity;
import com.atlas.crmapp.bean.RegionCodeBean;
import com.atlas.crmapp.common.Constants;
import com.atlas.crmapp.common.GlobalParams;
import com.atlas.crmapp.common.Utils;
import com.atlas.crmapp.model.PersonInfoJson;
import com.atlas.crmapp.model.VisibleCompanysJson;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.atlas.crmapp.network.NetworkUtil;
import com.atlas.crmapp.register.RegInfoActivity_;
import com.atlas.crmapp.register.RegisterActivity;
import com.atlas.crmapp.tim.model.FriendshipInfo;
import com.atlas.crmapp.util.ContextUtil;
import com.atlas.crmapp.util.RegisterCommonUtils;
import com.atlas.crmapp.util.SpUtil;
import com.atlas.crmapp.util.StringUtils;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMManager;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2018/3/20.
 */

public class RegInfoActivity_Presenter extends BasePresenter {
    RegInfoActivity_ activity;
    Context context;
    String zipCode = "";

    public RegInfoActivity_Presenter(RegInfoActivity_ fragment){
        this.activity = fragment;
        context = fragment;
    }

    String id ;
    public void submit(final HashMap params,String id){
        this.id = id;
        BizDataRequest.requestModifyUserInfo(context, params, new BizDataRequest.OnRequestResult() {
            @Override
            public void onSuccess() {
//                HuanXinManager.login(getGlobalParams().getPersonInfoJson().getId() + "", getGlobalParams().getPersonInfoJson().getUid());
                PersonInfoJson personInfoJson = activity.getGlobalParams().getPersonInfoJson();
                personInfoJson.setNick((String) params.get("nick"));
                personInfoJson.setCompany((String) params.get("company"));
                personInfoJson.setGender((String) params.get("gender"));
                personInfoJson.setAvatar((String) params.get("avatar"));

                Utils.storeAccountToken(context, activity.getGlobalParams().getAccessToken(), activity.getGlobalParams().getRefreshToken(), personInfoJson);
//                Intent intent = new Intent(RegInfoActivity.this, MainActivity.class);
                getTimSig();
            }

            @Override
            public void onError(DcnException error) {

            }
        });
    }

    private void getTimSig(){
        SpUtil.putLong(context,SpUtil.ID,Long.valueOf(id));
        BizDataRequest.getTimSig(activity, String.valueOf(id), new BizDataRequest.OnRequestTimSig() {
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
        String count = id;
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
//                SpUtil.putBoolean(context, "isLogin", true);
//                RegisterCommonUtils.loginRegister(context, activity.mPersonInfoJson);
//                GlobalParams.getInstance().setPersonInfoJson(activity.mPersonInfoJson);
////            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
//                Intent intent = new Intent(context, IndexActivity.class);
//                context.startActivity(intent);
//                activity.finish();
                GlobalParams.getInstance().setIsLogin(true);
                FriendshipInfo.getInstance().refresh();
                Intent intent = new Intent(context, IndexActivity.class);
                activity.startActivity(intent);
                activity.finish();
            }
        });
    }

    public void getRegionCode(){
        BizDataRequest.getRegionCode(activity, new BizDataRequest.OnRequestRegionCode() {
            @Override
            public void onSuccess(List<RegionCodeBean> regionCodeBeanList) {
                activity.setAreaCode(regionCodeBeanList);
            }

            @Override
            public void onError(DcnException error) {

            }
        });
    }
}
