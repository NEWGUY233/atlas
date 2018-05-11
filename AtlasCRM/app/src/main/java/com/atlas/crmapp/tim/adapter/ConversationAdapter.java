package com.atlas.crmapp.tim.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.activity.base.BaseActivity;
import com.atlas.crmapp.activity.index.fragment.communication.ChatActivity;
import com.atlas.crmapp.common.GlobalParams;
import com.atlas.crmapp.tim.model.Conversation;
import com.atlas.crmapp.tim.model.NomalConversation;
import com.atlas.crmapp.tim.model.SystemConversation;
import com.atlas.crmapp.util.StringUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

/**
 * Created by Administrator on 2018/4/16.
 */

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.Holder>{
    Context c;
    private List<Conversation> conversationList;
    public ConversationAdapter(Context c){
        this.c = c;
    }


    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(c).inflate(R.layout.tim_item_conversaton,parent,false));
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {
        final Conversation data = conversationList.get(position);

        holder.name.setText(data.getName());

//        if (data instanceof SystemConversation || StringUtils.isEmpty(((NomalConversation) data).getIcon())){
//            holder.icon.setImageResource(data.getAvatar());
//        }else {
//            Glide.with(c).load(((NomalConversation) data).getIcon()).apply(new RequestOptions().dontAnimate())
//                    .into(holder.icon);
//        }
        if (c.getString(R.string.contact).equals(data.getName()) ||
                c.getString(R.string.chat_sports).equals(data.getName()) ||
                c.getString(R.string.chat_workplace).equals(data.getName()) ||
                c.getString(R.string.chat_kitchen).equals(data.getName()) ||
                c.getString(R.string.chat_notice).equals(data.getName()) ||
                c.getString(R.string.chat_dynamic).equals(data.getName()) )
//            holder.icon.setImageResource(data.getAvatar());
        Glide.with(c).load( data.getAvatar()).apply(new RequestOptions().dontAnimate().placeholder(R.mipmap.icon_informationheard).error(R.mipmap.icon_informationheard))
                .into(holder.icon);
        else {
            Glide.with(c).load( data.getIcon()).apply(new RequestOptions().dontAnimate().placeholder(R.mipmap.icon_informationheard).error(R.mipmap.icon_informationheard))
                    .into(holder.icon);
        }

//        if (!StringUtils.isEmpty(data.getIcon())){
//
//
//        }else
//            holder.icon.setImageResource(data.getAvatar());

        holder.content.setText(data.getLastMessageSummary());
        holder.unread.setText(data.getUnreadNum() > 99 ?  "99+" : String.valueOf(data.getUnreadNum()));
        if (data.getUnreadNum() == 0){
            holder.unread.setVisibility(View.GONE);
        }else {
            holder.unread.setVisibility(View.VISIBLE);
        }


        if (c.getString(R.string.contact).equals(data.getName())){
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.addRule(RelativeLayout.RIGHT_OF,holder.icon.getId());
            lp.addRule(RelativeLayout.CENTER_VERTICAL);
            holder.name.setLayoutParams(lp);
//                    (RelativeLayout.LayoutParams) holder.name.getLayoutParams();
        }else {
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.addRule(RelativeLayout.RIGHT_OF,holder.icon.getId());
            lp.addRule(RelativeLayout.ALIGN_TOP,holder.icon.getId());
            holder.name.setLayoutParams(lp);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!GlobalParams.getInstance().isLogin()){
                    if (c instanceof BaseActivity)
                        ((BaseActivity) c).showAskLoginDialog();
                    return;
                }

                data.navToDetail(c);
                conversationList.get(position).readAllMessage();
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return conversationList == null ? 0 : conversationList.size();
    }

    public void setConversationList(List<Conversation> conversationList) {
        this.conversationList = conversationList;
        notifyDataSetChanged();
    }

    class Holder extends RecyclerView.ViewHolder{
        ImageView icon;
        TextView name;
        TextView content;
        TextView unread;

        public Holder(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.icon);
            name = (TextView) itemView.findViewById(R.id.name);
            content = (TextView) itemView.findViewById(R.id.content);
            unread = (TextView) itemView.findViewById(R.id.unread_text);
        }
    }
}
