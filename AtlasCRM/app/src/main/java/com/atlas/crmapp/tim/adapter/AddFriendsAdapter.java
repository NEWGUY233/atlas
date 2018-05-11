package com.atlas.crmapp.tim.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.activity.base.BaseActivity;
import com.atlas.crmapp.tim.model.FriendFuture;
import com.atlas.crmapp.tim.model.FriendshipInfo;
import com.atlas.crmapp.tim.model.Message;
import com.atlas.crmapp.tim.model.TextMessage;
import com.atlas.crmapp.util.DateUtil;
import com.atlas.crmapp.util.SpUtil;
import com.atlas.crmapp.util.StringUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.sns.TIMAddFriendRequest;
import com.tencent.imsdk.ext.sns.TIMFriendAddResponse;
import com.tencent.imsdk.ext.sns.TIMFriendFutureItem;
import com.tencent.imsdk.ext.sns.TIMFriendResponseType;
import com.tencent.imsdk.ext.sns.TIMFriendResult;
import com.tencent.imsdk.ext.sns.TIMFriendshipManagerExt;

import java.util.List;

/**
 * Created by Administrator on 2018/4/16.
 */

public class AddFriendsAdapter extends RecyclerView.Adapter<AddFriendsAdapter.Holder>{
    Context c;
    private List<FriendFuture> conversationList;
    public AddFriendsAdapter(Context c){
        this.c = c;
    }


    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(c).inflate(R.layout.tim_item_add_friends,parent,false));
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        final FriendFuture item = conversationList.get(position);

        if (!StringUtils.isEmpty(item.getIcon()))
            Glide.with(c).load(item.getIcon()).apply(new RequestOptions().error(R.mipmap.icon_informationheard)).into(holder.icon);
        else
            Glide.with(c).load(R.mipmap.icon_informationheard).into(holder.icon);
        holder.name.setText(item.getName());
        switch (item.getType()){
            case TIM_FUTURE_FRIEND_DECIDE_TYPE:
                break;
            case TIM_FUTURE_FRIEND_PENDENCY_IN_TYPE:
                break;
            case TIM_FUTURE_FRIEND_PENDENCY_OUT_TYPE:
                break;
        }

        holder.acp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TIMFriendAddResponse request = new TIMFriendAddResponse(item.getIdentify());
                request.setType(TIMFriendResponseType.AgreeAndAdd);
                TIMFriendshipManagerExt.getInstance().addFriendResponse(request, new TIMValueCallBack<TIMFriendResult>() {
                    @Override
                    public void onError(int i, String s) {

                    }

                    @Override
                    public void onSuccess(TIMFriendResult timFriendResult) {
                        ((BaseActivity)c).finish();
                        FriendshipInfo.getInstance().refresh();
                    }
                });
            }
        });

        holder.rej.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TIMFriendAddResponse request = new TIMFriendAddResponse(item.getIdentify());
                request.setType(TIMFriendResponseType.Reject);
                TIMFriendshipManagerExt.getInstance().addFriendResponse(request, new TIMValueCallBack<TIMFriendResult>() {
                    @Override
                    public void onError(int i, String s) {

                    }

                    @Override
                    public void onSuccess(TIMFriendResult timFriendResult) {
                        ((BaseActivity)c).finish();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return conversationList == null ? 0 : conversationList.size();
    }

    public void setConversationList(List<FriendFuture> conversationList) {
        this.conversationList = conversationList;
        notifyDataSetChanged();
    }

    class Holder extends RecyclerView.ViewHolder{
        ImageView icon;
        ImageView acp;
        ImageView rej;
        TextView name;

        public Holder(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.icon);
            acp = (ImageView) itemView.findViewById(R.id.iv_acp);
            rej = (ImageView) itemView.findViewById(R.id.iv_reject);
            name = (TextView) itemView.findViewById(R.id.name);
        }
    }
}
