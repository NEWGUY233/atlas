package com.atlas.crmapp.presenter;

import android.content.Context;
import android.widget.Toast;

import com.atlas.crmapp.R;
import com.atlas.crmapp.activity.index.fragment.communication.ChatDynamicActivity;
import com.atlas.crmapp.activity.index.fragment.index.activity.TagDetailActivity;
import com.atlas.crmapp.bean.ChatDynamicBean;
import com.atlas.crmapp.model.VThreadsJson;
import com.atlas.crmapp.model.VisibleThreadsJson;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;

/**
 * Created by Administrator on 2018/3/20.
 */

public class ChatDynamicActivityPresenter {
    ChatDynamicActivity fragment;
    Context context;

    public ChatDynamicActivityPresenter(ChatDynamicActivity fragment){
        this.fragment = fragment;
        context = fragment.getContext();
    }


    public void getDynamicList(final long time){
        BizDataRequest.getChatDynamic(context, time, new BizDataRequest.ChatDynamicBeanRequestRescult() {
            @Override
            public void onSuccess(ChatDynamicBean bean) {
                if (time == 0)
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
