package com.atlas.crmapp.presenter;

import android.content.Context;
import android.widget.Toast;

import com.atlas.crmapp.R;
import com.atlas.crmapp.activity.index.fragment.index.activity.TagDetailActivity;
import com.atlas.crmapp.activity.index.fragment.index.activity.fragment.AATagFragment;
import com.atlas.crmapp.bean.TagBean;
import com.atlas.crmapp.common.GlobalParams;
import com.atlas.crmapp.model.VThreadsJson;
import com.atlas.crmapp.model.VisibleThreadsJson;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;

import java.util.List;

/**
 * Created by Administrator on 2018/3/20.
 */

public class TagDetailActivityPresenter {
    TagDetailActivity fragment;
    Context context;

    public TagDetailActivityPresenter(TagDetailActivity fragment){
        this.fragment = fragment;
        context = fragment.getContext();
    }

    public void getDetail(long id){
        BizDataRequest.requestGetThreadForVisitor(context, id, null, new BizDataRequest.OnVThreads() {
            @Override
            public void onSuccess(VThreadsJson vThreadsJson) {
                fragment.setDetail(vThreadsJson);
            }

            @Override
            public void onError(DcnException error) {

            }
        });
    }

    public void getCommentList(long id, final long page,String[] type){
        BizDataRequest.requestGetReplyThreadsForVisitor_V2(context, id, page,type, new BizDataRequest.OnVisibleThreads() {
            @Override
            public void onSuccess(VisibleThreadsJson visibleThreadsJson) {
                if (page == 0)
                    fragment.setCommentList(visibleThreadsJson.rows);
                else
                    fragment.addCommentList(visibleThreadsJson.rows);
            }

            @Override
            public void onError(DcnException error) {

            }
        });
    }

    public void commentTag(long id,String comment){
        BizDataRequest.requestReplyThread(context, id, comment, new BizDataRequest.OnRequestResult() {
            @Override
            public void onSuccess() {
                fragment.commentSuccess();
            }

            @Override
            public void onError(DcnException error) {

            }
        });
    }

    public void collected(boolean collected,long id){
        BizDataRequest.collectTag(context,collected,id,new BizDataRequest.OnRequestResult(){

            @Override
            public void onSuccess() {
                fragment.onCollected();
            }

            @Override
            public void onError(DcnException error) {
                fragment.showToast(error.getDescription());
            }
        });
    }

    public void postDynamic(final String content, String type, String id){
        BizDataRequest.postDynamic(context, content, null, type, id, new BizDataRequest.OnRequestResult() {
            @Override
            public void onSuccess() {
                Toast.makeText(context,context.getString(R.string.tag_share),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(DcnException error) {

            }
        });
    }

    public void delete(final String id){
        BizDataRequest.deleteTagComment(context, id, new BizDataRequest.OnRequestResult() {
            @Override
            public void onSuccess() {
                fragment.onDelete(id);
            }

            @Override
            public void onError(DcnException error) {

            }
        });
    }

}
