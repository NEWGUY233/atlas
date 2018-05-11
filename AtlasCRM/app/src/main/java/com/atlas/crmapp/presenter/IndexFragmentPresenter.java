package com.atlas.crmapp.presenter;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.atlas.crmapp.PrintDetailActivity;
import com.atlas.crmapp.R;
import com.atlas.crmapp.activity.index.fragment.index.IndexFragment;
import com.atlas.crmapp.bean.DynamicSuccessBean;
import com.atlas.crmapp.bean.IndexMomentBean;
import com.atlas.crmapp.bean.LocationBean;
import com.atlas.crmapp.bean.TagBean;
import com.atlas.crmapp.common.GlobalParams;
import com.atlas.crmapp.model.PrintJson;
import com.atlas.crmapp.model.PrintLogin;
import com.atlas.crmapp.model.ResourceJson;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.atlas.crmapp.usercenter.ActivitiesVerificationActivity;
import com.atlas.crmapp.util.AppUtil;
import com.orhanobut.logger.Logger;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Administrator on 2018/3/20.
 */

public class IndexFragmentPresenter {
    IndexFragment fragment;
    Context context;

    public IndexFragmentPresenter(IndexFragment fragment){
        this.fragment = fragment;
        context = fragment.getContext();
    }

    public void printLog(PrintJson json){
        BizDataRequest.requestPrintLogin(context, json, new BizDataRequest.PrintLoginInfo() {
            @Override
            public void onSuccess(PrintLogin bean) {
                Toast.makeText(context, R.string.t30,Toast.LENGTH_LONG).show();
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

    String banner = "app/v2/home/banner";
    String recommend = "app/v2/home/recommend";
    public void getBannerRequestResource(){
        BizDataRequest.requestResource(context, GlobalParams.getInstance().getAtlasId() + "", banner, new BizDataRequest.OnRequestResource() {
            @Override
            public void onSuccess(ResourceJson resourceJson) {
                fragment.setBanner(resourceJson);
            }

            @Override
            public void onError(DcnException error) {

            }
        });
    }


    public void getRecommendRequestResource(){
        BizDataRequest.requestResource(context, GlobalParams.getInstance().getAtlasId() + "", recommend, new BizDataRequest.OnRequestResource() {
            @Override
            public void onSuccess(ResourceJson resourceJson) {
                if (resourceJson == null || resourceJson.rows == null || resourceJson.rows.size() == 0)
                    return;
                fragment.setActiveAdapter(resourceJson.rows.get(0).resourceMedias);
            }

            @Override
            public void onError(DcnException error) {

            }
        });
    }

    public void getMoment(String uid, final long createTime){
        BizDataRequest.getIndexList(context,uid, createTime, new BizDataRequest.IndexMomentBeanRequestRescult() {
            @Override
            public void onSuccess(IndexMomentBean bean) {
                if (createTime == 0)
                    fragment.setRecyclerViewIndex(bean);
                else
                    fragment.addRecyclerViewIndex(bean);

            }

            @Override
            public void onError(DcnException error) {

            }
        });
    }

    public void getLocation(){
        BizDataRequest.getIndexLocationList(context, new BizDataRequest.LocationBeanRequestRescult() {
            @Override
            public void onSuccess(List<LocationBean> list) {
                fragment.setPop(list);
            }

            @Override
            public void onError(DcnException error) {

            }
        });
    }

    public void postDynamic(List<String> path,String content){
        BizDataRequest.postDynamic(context, content, path, "", "", new BizDataRequest.DynamicSuccessBeanRequestRescult() {
            @Override
            public void onSuccess(DynamicSuccessBean bean) {
                fragment.onSuccess(bean);
            }

            @Override
            public void onError(DcnException error) {
                fragment.postFailed();
            }
        });
    }
}
