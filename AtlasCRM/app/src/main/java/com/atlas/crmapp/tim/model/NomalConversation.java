package com.atlas.crmapp.tim.model;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.atlas.crmapp.Atlas;
import com.atlas.crmapp.R;
import com.atlas.crmapp.activity.index.fragment.communication.ChatActivity;
import com.atlas.crmapp.activity.index.fragment.communication.ChatDynamicActivity;
import com.atlas.crmapp.activity.index.fragment.communication.ChatNoticeActivity;
import com.atlas.crmapp.db.hepler.ChatCountHelper;
import com.atlas.crmapp.db.hepler.PushMsgHepler;
import com.atlas.crmapp.util.ContextUtil;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.ext.message.TIMConversationExt;

/**
 * Created by Administrator on 2018/4/16.
 */

public class NomalConversation extends Conversation {

    TIMConversation conversation;
    public NomalConversation(TIMConversation item){
        this.conversation = item;
        type = conversation.getType();
        identify = conversation.getPeer();
    }

    //最后一条消息
    private Message lastMessage;

    @Override
    public long getLastMessageTime() {
        TIMConversationExt ext = new TIMConversationExt(conversation);
        if (ext.hasDraft()){
            if (lastMessage == null || lastMessage.getMessage().timestamp() < ext.getDraft().getTimestamp()){
                return ext.getDraft().getTimestamp();
            }else{
                return lastMessage.getMessage().timestamp();
            }
        }
        if (lastMessage == null) return 0;
        return lastMessage.getMessage().timestamp();
    }

    @Override
    public long getUnreadNum() {
        if (conversation == null) return 0;
//        TIMConversationExt ext = new TIMConversationExt(conversation);
//        return  ext.getUnreadMessageNum();

        return ChatCountHelper.getUnread(conversation.getPeer());
    }

    @Override
    public void readAllMessage() {
        if (conversation != null){
            TIMConversationExt ext = new TIMConversationExt(conversation);
            ext.setReadMessage(null, null);
            ChatCountHelper.remove(conversation.getPeer());
        }
    }

    @Override
    public int getAvatar() {
        if ("admin".equals(identify))
            return R.mipmap.im_icon_mes_friend_notice;
        if ("sport_admin".equals(identify))
            return R.mipmap.im_icon_mes_fitness_notice;
        if ("workplace_admin".equals(identify))
            return R.mipmap.im_icon_mes_office_notice;
        if ("kitchen_admin".equals(identify))
            return R.mipmap.im_icon_mes_kitchen_notice;
        if ("notice_admin".equals(identify))
            return R.mipmap.im_icon_mes_notice;


        return R.mipmap.icon_informationheard;
    }

    @Override
    public void navToDetail(Context context) {
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

        ChatActivity.navToChat(context,identify,type);
    }

    @Override
    public String getLastMessageSummary() {

        TIMConversationExt ext = new TIMConversationExt(conversation);
        if (ext.hasDraft()){
            TextMessage textMessage = new TextMessage(ext.getDraft());
            if (lastMessage == null || lastMessage.getMessage().timestamp() < ext.getDraft().getTimestamp()){
                return "" + textMessage.getSummary();
            }else{
                return lastMessage.getSummary();
            }
        }else{
            if (lastMessage == null) return "";
            return lastMessage.getSummary();
        }
    }

    @Override
    public String getName() {
        if ("admin".equals(identify))
            return ContextUtil.getUtil().getContext().getString(R.string.chat_dynamic);
        if ("sport_admin".equals(identify))
            return ContextUtil.getUtil().getContext().getString(R.string.chat_sports);
        if ("workplace_admin".equals(identify))
            return ContextUtil.getUtil().getContext().getString(R.string.chat_workplace);
        if ("kitchen_admin".equals(identify))
            return ContextUtil.getUtil().getContext().getString(R.string.chat_kitchen);
        if ("notice_admin".equals(identify))
            return ContextUtil.getUtil().getContext().getString(R.string.chat_notice);



        if (type == TIMConversationType.Group){
        }else{
            FriendProfile profile = FriendshipInfo.getInstance().getProfile(identify);
            name=profile == null?identify:profile.getName();
        }
        return name;
    }

    @Override
    public String getIcon() {
        FriendProfile profile = FriendshipInfo.getInstance().getProfile(identify);
        return name=profile == null?identify:profile.getAvatarUrl();
    }

    public void setLastMessage(Message lastMessage) {
        this.lastMessage = lastMessage;
    }
}
