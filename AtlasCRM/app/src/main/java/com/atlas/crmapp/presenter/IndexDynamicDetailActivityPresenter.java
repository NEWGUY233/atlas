package com.atlas.crmapp.presenter;

import android.content.Context;

import com.atlas.crmapp.activity.index.fragment.index.activity.IndexDynamicDetailActivity;
import com.atlas.crmapp.activity.index.fragment.index.activity.TagDetailActivity;
import com.atlas.crmapp.bean.DynamicCommentBean;
import com.atlas.crmapp.bean.IndexMomentBean;
import com.atlas.crmapp.model.VThreadsJson;
import com.atlas.crmapp.model.VisibleThreadsJson;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;

import java.util.List;

/**
 * Created by Administrator on 2018/3/20.
 */

public class IndexDynamicDetailActivityPresenter {
    IndexDynamicDetailActivity fragment;
    Context context;

    public IndexDynamicDetailActivityPresenter(IndexDynamicDetailActivity fragment){
        this.fragment = fragment;
        context = fragment.getContext();
    }

    public void getDetail(String id){
        BizDataRequest.getDynamicDetail(context, id, new BizDataRequest.DynamicDetailBeanRequestRescult() {
            @Override
            public void onSuccess(IndexMomentBean.RowsBean bean) {
                fragment.setDetail(bean);
            }

            @Override
            public void onError(DcnException error) {
                fragment.showToast(error.getDescription());
                fragment.noneDynamic();
            }
        });
    }

    public void getComment(String id, String[] type, final long page){
        BizDataRequest.getDynamicDetailComment(context, id,type,page, new BizDataRequest.DynamicCommentBeanRequestRescult() {
            @Override
            public void onSuccess(DynamicCommentBean list) {
                if ( page == 0)
                    fragment.setAdapter(list);
                else
                    fragment.addAdapter(list);
            }

            @Override
            public void onError(DcnException error) {

            }
        });
    }

    public void setComment(String id,String comment){
        BizDataRequest.setDynamicDetailComment(context, id, comment, new BizDataRequest.OnRequestResult() {
            @Override
            public void onSuccess() {
                fragment.onCommentSuccess();
            }

            @Override
            public void onError(DcnException error) {

            }
        });
    }
    public void deleteDynamic(String id){
        BizDataRequest.deleteDynamic(context, id, new BizDataRequest.OnRequestResult() {
            @Override
            public void onSuccess() {
                fragment.delete();
                fragment.finish();
            }

            @Override
            public void onError(DcnException error) {

            }
        });
    }


    public void deleteComment(final int position, final String id){
        BizDataRequest.deleteDynamicComment(context, id, new BizDataRequest.OnRequestResult() {
            @Override
            public void onSuccess() {
                fragment.onCommentDelete(position,id);
            }

            @Override
            public void onError(DcnException error) {

            }
        });
    }
}
