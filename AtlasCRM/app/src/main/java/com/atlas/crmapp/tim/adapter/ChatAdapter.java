package com.atlas.crmapp.tim.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.activity.index.fragment.index.activity.PreviewPhotosActivity;
import com.atlas.crmapp.tim.model.Conversation;
import com.atlas.crmapp.tim.model.FriendshipInfo;
import com.atlas.crmapp.tim.model.ImageMessage;
import com.atlas.crmapp.tim.model.Message;
import com.atlas.crmapp.tim.model.TextMessage;
import com.atlas.crmapp.tim.model.VoiceMessage;
import com.atlas.crmapp.util.DateUtil;
import com.atlas.crmapp.util.GlideUtils;
import com.atlas.crmapp.util.SpUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.tencent.imsdk.TIMManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/16.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.Holder>{
    Context c;
    private List<Message> conversationList;
    String count;
    public ChatAdapter(Context c){
        this.c = c;
//        count = SpUtil.getString(c,SpUtil.TIM,"");
        count = TIMManager.getInstance().getLoginUser();
    }


    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(c).inflate(R.layout.tim_item_chat_activity,parent,false));
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(final Holder holder, int position) {
        final Message data = conversationList.get(position);
        if (data.getHasTime()){
            holder.time.setVisibility(View.VISIBLE);
            holder.time.setText(DateUtil.chatTime(data.getMessage().timestamp()));
        }else {
            holder.time.setVisibility(View.GONE);
            holder.time.setText("");
        }

        holder.tv_time_mine.setText("");
        holder.tv_time_cus.setText("");

        boolean isMine = count.equals(data.getMessage().getSender());
        if (!isMine){
            if (FriendshipInfo.getInstance().getProfile(data.getSender()) != null)
//                GlideUtils.loadImageView(c, FriendshipInfo.getInstance().getProfile(data.getSender()).getAvatarUrl(), holder.icon);
                Glide.with(c).load(FriendshipInfo.getInstance().getProfile(data.getSender()).getAvatarUrl())
                .apply(new RequestOptions().error(R.mipmap.icon_informationheard)).into(holder.icon);
            else
                Glide.with(c).load(R.mipmap.icon_informationheard)
                        .apply(new RequestOptions().error(R.mipmap.icon_informationheard)).into(holder.icon);
        }


        if (data instanceof TextMessage) {
            String mg = data.getSummary();
            holder.tv_cus.setText(mg);
            if (isMine) {
                holder.ll_cus.setVisibility(View.GONE);
                holder.iv_mine.setVisibility(View.GONE);
                holder.iv_voice_mine.setVisibility(View.GONE);
                holder.ll_mine.setVisibility(View.VISIBLE);
                holder.tv_mine.setVisibility(View.VISIBLE);
                holder.tv_mine.setText(((TextMessage) data).getText(c));
            } else {
                holder.ll_cus.setVisibility(View.VISIBLE);
                holder.tv_cus.setVisibility(View.VISIBLE);
                holder.ll_mine.setVisibility(View.GONE);
                holder.iv_cus.setVisibility(View.GONE);
                holder.iv_voice_cus.setVisibility(View.GONE);
                holder.tv_cus.setText(((TextMessage) data).getText(c));

            }
        }else if (data instanceof ImageMessage){
            if (isMine) {
                holder.ll_cus.setVisibility(View.GONE);
                holder.ll_mine.setVisibility(View.VISIBLE);

                holder.iv_voice_mine.setVisibility(View.GONE);
                holder.iv_mine.setVisibility(View.VISIBLE);
                holder.tv_mine.setVisibility(View.GONE);
//                holder.iv_mine.setImageBitmap(((ImageMessage) data).getThumb());
                ((ImageMessage) data).setImg( holder.iv_mine,c);
            } else {
                holder.ll_mine.setVisibility(View.GONE);
                holder.ll_cus.setVisibility(View.VISIBLE);

                holder.iv_voice_cus.setVisibility(View.GONE);
                holder.tv_cus.setVisibility(View.GONE);
                holder.iv_cus.setVisibility(View.VISIBLE);
//                holder.iv_cus.setImageBitmap(((ImageMessage) data).getThumb());
                ((ImageMessage) data).setImg( holder.iv_cus,c);
            }

            View.OnClickListener click = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArrayList<String> list = new ArrayList<String>();
                    list.add(((ImageMessage) data).getImgPath());
                    ((Activity)c).startActivityForResult(new Intent(c, PreviewPhotosActivity.class)
                                    .putExtra("position",0)
                                    .putExtra("type","just_show")
                                    .putStringArrayListExtra("images", list)
                            ,0x112);
                }
            };

            holder.iv_cus.setOnClickListener(click);
            holder.iv_mine.setOnClickListener(click);

        }else if (data instanceof VoiceMessage){

            if (isMine) {
                holder.ll_cus.setVisibility(View.GONE);
                holder.ll_mine.setVisibility(View.VISIBLE);

                holder.iv_voice_mine.setVisibility(View.VISIBLE);
                holder.iv_mine.setVisibility(View.GONE);
                holder.tv_mine.setVisibility(View.GONE);

                holder.tv_time_mine.setText(((VoiceMessage) data).getDuration());
                holder.tv_time_cus.setText("");
            } else {
                holder.ll_mine.setVisibility(View.GONE);
                holder.ll_cus.setVisibility(View.VISIBLE);

                holder.iv_voice_cus.setVisibility(View.VISIBLE);
                holder.tv_cus.setVisibility(View.GONE);
                holder.iv_cus.setVisibility(View.GONE);

                holder.tv_time_mine.setText("");
                holder.tv_time_cus.setText(((VoiceMessage) data).getDuration());
            }

            View.OnClickListener click = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v instanceof ImageView){
                        ((VoiceMessage) data).playAudio((AnimationDrawable) ((ImageView) v).getDrawable());
                    }
                }
            };
            holder.iv_voice_mine.setOnClickListener(click);
            holder.iv_voice_cus.setOnClickListener(click);



        }



    }

    @Override
    public int getItemCount() {
        return conversationList == null ? 0 : conversationList.size();
    }

    public void setConversationList(List<Message> conversationList) {
        this.conversationList = conversationList;
        notifyDataSetChanged();
    }

    public List<Message> getConversationList(){
        return this.conversationList;
    }

    class Holder extends RecyclerView.ViewHolder{
        ImageView icon;
        TextView time;
        TextView tv_mine;
        TextView tv_cus;
        LinearLayout ll_cus;
        LinearLayout ll_mine;

        ImageView iv_cus;
        ImageView iv_mine;

        ImageView iv_voice_cus;
        ImageView iv_voice_mine;

        TextView tv_time_mine;
        TextView tv_time_cus;

        public Holder(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.icon);
            iv_cus = (ImageView) itemView.findViewById(R.id.iv_cus);
            iv_mine = (ImageView) itemView.findViewById(R.id.iv_mine);
            time = (TextView) itemView.findViewById(R.id.time);
            tv_mine = (TextView) itemView.findViewById(R.id.tv_mine);
            tv_cus = (TextView) itemView.findViewById(R.id.tv_cus);
            ll_cus = (LinearLayout) itemView.findViewById(R.id.ll_cus);
            ll_mine = (LinearLayout) itemView.findViewById(R.id.ll_mine);

            iv_voice_cus = (ImageView) itemView.findViewById(R.id.iv_voice_cus);
            iv_voice_mine = (ImageView) itemView.findViewById(R.id.iv_voice_mine);

            tv_time_cus = (TextView) itemView.findViewById(R.id.tv_time_cus);
            tv_time_mine = (TextView) itemView.findViewById(R.id.tv_time_mine);
        }
    }
}
