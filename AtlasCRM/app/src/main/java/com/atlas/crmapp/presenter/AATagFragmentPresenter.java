package com.atlas.crmapp.presenter;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.atlas.crmapp.PrintDetailActivity;
import com.atlas.crmapp.R;
import com.atlas.crmapp.activity.index.fragment.index.IndexFragment;
import com.atlas.crmapp.activity.index.fragment.index.activity.fragment.AATagFragment;
import com.atlas.crmapp.bean.TagBean;
import com.atlas.crmapp.common.GlobalParams;
import com.atlas.crmapp.model.PrintJson;
import com.atlas.crmapp.model.PrintLogin;
import com.atlas.crmapp.model.VThreadsJson;
import com.atlas.crmapp.model.VisibleThreadsJson;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.atlas.crmapp.usercenter.ActivitiesVerificationActivity;
import com.atlas.crmapp.util.AppUtil;
import com.orhanobut.logger.Logger;

import java.util.List;

/**
 * Created by Administrator on 2018/3/20.
 */

public class AATagFragmentPresenter {
    AATagFragment fragment;
    Context context;

    public AATagFragmentPresenter(AATagFragment fragment){
        this.fragment = fragment;
        context = fragment.getContext();
    }

    public void getTagList(){
        BizDataRequest.getTagList(context, "", new BizDataRequest.TagListRequestRescult() {
            @Override
            public void onSuccess(List<TagBean> printJason) {
                fragment.setList(printJason);
            }

            @Override
            public void onError(DcnException error) {
                Toast.makeText(context,error.getDescription(),Toast.LENGTH_SHORT).show();
                fragment.onNetError();
            }
        });
    }

    public void getBodyList(final long maxID){
        BizDataRequest.requestVisibleThreads(context, GlobalParams.getInstance().getAtlasId(), maxID
                , false, null, new BizDataRequest.OnVisibleThreads() {
                    @Override
                    public void onSuccess(VisibleThreadsJson visibleThreadsJson) {
                        if (maxID != 0){
                            fragment.addBodyList(visibleThreadsJson.rows);
                        }else
                            fragment.setBodyList(visibleThreadsJson.rows);
                    }

                    @Override
                    public void onError(DcnException error) {
                        Toast.makeText(context,error.getDescription(),Toast.LENGTH_SHORT).show();
                        fragment.onNetError();
                    }
                });
    }

}
