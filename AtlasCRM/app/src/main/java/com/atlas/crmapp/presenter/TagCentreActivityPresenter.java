package com.atlas.crmapp.presenter;

import android.content.Context;
import android.widget.Toast;

import com.atlas.crmapp.activity.index.fragment.index.activity.TagCentreActivity;
import com.atlas.crmapp.activity.index.fragment.index.activity.TagDetailActivity;
import com.atlas.crmapp.bean.CircleDetailBean;
import com.atlas.crmapp.common.GlobalParams;
import com.atlas.crmapp.model.VThreadsJson;
import com.atlas.crmapp.model.VisibleThreadsJson;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;

/**
 * Created by Administrator on 2018/3/20.
 */

public class TagCentreActivityPresenter {
    TagCentreActivity fragment;
    Context context;

    public TagCentreActivityPresenter(TagCentreActivity fragment){
        this.fragment = fragment;
        context = fragment.getContext();
    }

    public void getList(long id){
        BizDataRequest.getCircleDetail(context, id, new BizDataRequest.CircleDetail() {
            @Override
            public void onSuccess(CircleDetailBean bean) {
                fragment.setData(bean);
            }

            @Override
            public void onError(DcnException error) {

            }
        });
    }

    public void getBodyList(final long maxID,long id){
        BizDataRequest.requestVisibleThreads(context,id, GlobalParams.getInstance().getAtlasId(), maxID
                , false, null, new BizDataRequest.OnVisibleThreads() {
                    @Override
                    public void onSuccess(VisibleThreadsJson visibleThreadsJson) {
                        if (maxID != 0){
                            fragment.addData(visibleThreadsJson.rows);
                        }else
                            fragment.setData(visibleThreadsJson.rows);
                    }

                    @Override
                    public void onError(DcnException error) {
                        Toast.makeText(context,error.getDescription(),Toast.LENGTH_SHORT).show();
//                        fragment.onNetError();
                    }
                });
    }

}
