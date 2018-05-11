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
import com.atlas.crmapp.common.GlobalParams;
import com.atlas.crmapp.model.PrintJson;
import com.atlas.crmapp.model.PrintLogin;
import com.atlas.crmapp.model.ResourceJson;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.atlas.crmapp.usercenter.ActivitiesVerificationActivity;
import com.atlas.crmapp.usercenter.MyDynamicActivity;
import com.atlas.crmapp.util.AppUtil;
import com.orhanobut.logger.Logger;

import java.util.List;

/**
 * Created by Administrator on 2018/3/20.
 */

public class MyDynamicActivityPresenter {
    MyDynamicActivity fragment;
    Context context;

    public MyDynamicActivityPresenter(MyDynamicActivity fragment){
        this.fragment = fragment;
        context = fragment.getContext();
    }


    public void getMoment(final long createTime){
        BizDataRequest.getMyIndexList(context, createTime, new BizDataRequest.IndexMomentBeanRequestRescult() {
            @Override
            public void onSuccess(IndexMomentBean bean) {
                if (createTime == 0)
                    fragment.setList(bean);
                else
                    fragment.addList(bean);

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
