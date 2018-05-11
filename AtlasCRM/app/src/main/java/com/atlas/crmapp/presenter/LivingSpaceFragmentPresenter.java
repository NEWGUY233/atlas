package com.atlas.crmapp.presenter;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.atlas.crmapp.PrintDetailActivity;
import com.atlas.crmapp.R;
import com.atlas.crmapp.activity.index.fragment.index.IndexFragment;
import com.atlas.crmapp.activity.index.fragment.livingspace.LivingSpaceFragment;
import com.atlas.crmapp.bean.LivingBizBean;
import com.atlas.crmapp.common.GlobalParams;
import com.atlas.crmapp.model.PrintJson;
import com.atlas.crmapp.model.PrintLogin;
import com.atlas.crmapp.model.ResourceJson;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.atlas.crmapp.usercenter.ActivitiesVerificationActivity;
import com.atlas.crmapp.util.AppUtil;
import com.orhanobut.logger.Logger;

/**
 * Created by Administrator on 2018/3/20.
 */

public class LivingSpaceFragmentPresenter {
    LivingSpaceFragment fragment;
    Context context;

    public LivingSpaceFragmentPresenter(LivingSpaceFragment fragment){
        this.fragment = fragment;
        context = fragment.getContext();
    }

    public void printLog(PrintJson json){
        BizDataRequest.requestPrintLogin(context, json, new BizDataRequest.PrintLoginInfo() {
            @Override
            public void onSuccess(PrintLogin bean) {
                Toast.makeText(context,context.getString(R.string.t30),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(DcnException error) {

            }
        });

    }

    public void unlockPrint(String stationId){
//        Log.i("unlockPrint","stationId = " + stationId);
        BizDataRequest.requestPrintUnlock(context,  stationId, AppUtil.getIPAddress(context), new BizDataRequest.PrintUnlockInfo() {
            @Override
            public void onSuccess() {
//                startServiceForPrint();
                context.startActivity(new Intent(context,PrintDetailActivity.class));
            }

            @Override
            public void onError(DcnException error) {

            }
        });
    }

    public void requestAppUserGeteam(final String code){
        BizDataRequest.requestAppUserGeteam(context, new BizDataRequest.OnResponseAppUserGeteam() {
            @Override
            public void onSuccess(boolean isGeteam) {
                if(isGeteam){
                    ActivitiesVerificationActivity.newInstance(fragment.getActivity(), code);
                }else{
                    Logger.e(" is not csr-----");
                }
            }

            @Override
            public void onError(DcnException error) {

            }
        });
    }

    String sports = "app/v2/liveplace/sports";
    String kac = "app/v2/liveplace/recommend";
    String stu = "app/business/main/ad/studio";
    public void getKitchenAndCoffee(){
        BizDataRequest.requestResource(context, GlobalParams.getInstance().getAtlasId() + "", kac, new BizDataRequest.OnRequestResource() {
            @Override
            public void onSuccess(ResourceJson resourceJson) {
                fragment.setKAC(resourceJson);
            }

            @Override
            public void onError(DcnException error) {

            }
        });
    }

    public void getSports(){
        BizDataRequest.requestResource(context, GlobalParams.getInstance().getAtlasId() + "", sports, new BizDataRequest.OnRequestResource() {
            @Override
            public void onSuccess(ResourceJson resourceJson) {
                fragment.setSports(resourceJson);
            }

            @Override
            public void onError(DcnException error) {

            }
        });
    }

    public void getStudio(){
        BizDataRequest.requestResource(context, GlobalParams.getInstance().getAtlasId() + "", stu, new BizDataRequest.OnRequestResource() {
            @Override
            public void onSuccess(ResourceJson resourceJson) {
                fragment.setStudio(resourceJson);
            }

            @Override
            public void onError(DcnException error) {

            }
        });
    }

    public String bizCF = "coffee";
    public String bizKT = "kitchen";
    public String bizSP = "fitness";
    public String bizGF = "gogreen";
    public void checkBiz(final String biz){
        BizDataRequest.checkBiz(context, biz, new BizDataRequest.LivingBizBeanInfo() {
            @Override
            public void onSuccess(LivingBizBean bizBean) {
                if (bizCF.equals(biz)){
                    fragment.initCoffee(bizBean);
                }else  if (bizKT.equals(biz)){
                    fragment.initKitchen(bizBean);
                }else  if (bizSP.equals(biz)){
                    fragment.initSports(bizBean);
                }else  if (bizGF.equals(biz)){
                    fragment.initGolf(bizBean);
                }
            }

            @Override
            public void onError(DcnException error) {

            }
        });
    }
}
