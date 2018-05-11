package com.atlas.crmapp.tim.model;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.atlas.crmapp.R;
import com.atlas.crmapp.activity.index.fragment.communication.ChatDynamicActivity;
import com.atlas.crmapp.activity.index.fragment.communication.ChatNoticeActivity;
import com.atlas.crmapp.activity.index.fragment.communication.ContactActivity;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMElem;
import com.tencent.imsdk.TIMElemType;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.ext.message.TIMConversationExt;

/**
 * Created by Administrator on 2018/4/17.
 */

public class SystemConversation extends Conversation {
    SystemBean bean;
    public SystemConversation(SystemBean bean){
        this.bean = bean;
    }

    private SystemMessage message;
    private TIMConversation conversation;

    @Override
    public long getLastMessageTime() {
        return 0;
    }

    @Override
    public long getUnreadNum() {
        if (conversation == null) return 0;
        TIMConversationExt ext = new TIMConversationExt(conversation);
//        conversation.getType()
//        long i = 0;
//        for (TIMElem elem :  ext.getDraft().getElems()){
//            if (elem.getType() == TIMElemType.SNSTips)
//                elem.getType();
//        }
        return ext.getUnreadMessageNum();
    }

    @Override
    public void readAllMessage() {
        if (conversation != null){
            TIMConversationExt ext = new TIMConversationExt(conversation);
            ext.setReadMessage(null, null);
        }
    }

    @Override
    public int getAvatar() {
        return bean.getIcon();
    }

    @Override
    public String getIcon() {
        return bean.getIcon() + "";
    }

    @Override
    public void navToDetail(Context context) {
        Intent intent = null;
        if (context.getString(R.string.contact).equals(bean.getName())){
            intent = new Intent(context, ContactActivity.class);
        }
        identify = bean.getIdentify();
        if ("admin".equals(identify)){
            context.startActivity(new Intent(context,ChatDynamicActivity.class));
            return;
        }

        //消息
        if ("notice_admin".equals(identify)){
            context.startActivity(new Intent(context,ChatNoticeActivity.class)
                    .putExtra("title",context.getString(R.string.chat_notice)).putExtra("identify",identify));
            return;
        }
        if ("sport_admin".equals(identify)) {
            context.startActivity(new Intent(context, ChatNoticeActivity.class)
                    .putExtra("title", context.getString(R.string.chat_sports)).putExtra("identify",identify));
            return;
        }
        if ("workplace_admin".equals(identify)) {
            context.startActivity(new Intent(context, ChatNoticeActivity.class)
                    .putExtra("title", context.getString(R.string.chat_workplace)).putExtra("identify",identify));
            return;
        }
        if ("kitchen_admin".equals(identify)) {
            context.startActivity(new Intent(context, ChatNoticeActivity.class)
                    .putExtra("title", context.getString(R.string.chat_kitchen)).putExtra("identify",identify));
            return;
        }

        if (intent != null)
            context.startActivity(intent);
    }

    @Override
    public String getLastMessageSummary() {
        return null;
    }

    @Override
    public String getName() {
        return bean.getName();
    }

    public void setMessage(SystemMessage message) {
        this.message = message;
    }

    public void setConversation(TIMConversation conversation) {
        this.conversation = conversation;
    }
}
