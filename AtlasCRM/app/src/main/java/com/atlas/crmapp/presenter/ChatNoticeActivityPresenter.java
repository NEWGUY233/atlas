package com.atlas.crmapp.presenter;

import android.content.Context;

import com.atlas.crmapp.activity.index.fragment.communication.ChatDynamicActivity;
import com.atlas.crmapp.activity.index.fragment.communication.ChatNoticeActivity;
import com.atlas.crmapp.bean.ChatDynamicBean;
import com.atlas.crmapp.bean.ChatNoticeBean;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;

/**
 * Created by Administrator on 2018/3/20.
 */

public class ChatNoticeActivityPresenter {
    ChatNoticeActivity fragment;
    Context context;

    public ChatNoticeActivityPresenter(ChatNoticeActivity fragment){
        this.fragment = fragment;
        context = fragment.getContext();
    }


    public void getNotice(final long page,final String id){
        BizDataRequest.getNotice(context, page,id, new BizDataRequest.ChatChatBeanRequestRescult() {
            @Override
            public void onSuccess(ChatNoticeBean bean) {
                if (page == 0)
                    fragment.setData(bean);
                else
                    fragment.addData(bean);
            }

            @Override
            public void onError(DcnException error) {

            }
        });
    }
}
