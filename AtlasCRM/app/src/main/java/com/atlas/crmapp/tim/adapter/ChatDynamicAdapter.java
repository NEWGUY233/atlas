package com.atlas.crmapp.tim.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.bean.ChatDynamicBean;
import com.atlas.crmapp.util.ActionUriUtils;
import com.atlas.crmapp.util.DateUtil;
import com.atlas.crmapp.util.StringUtils;
import com.atlas.crmapp.widget.CircleImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/5/2.
 */

public class ChatDynamicAdapter extends RecyclerView.Adapter<ChatDynamicAdapter.Holder> {
    Context context;
    private List<ChatDynamicBean.RowsBean> list;

    public ChatDynamicAdapter(Context context) {
        this.context = context;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(context).inflate(R.layout.item_chat_dynamic, parent, false));
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        final ChatDynamicBean.RowsBean bean = list.get(position);

        if ("COMMENT".equals(bean.getMsgType())){
            holder.ivContent.setVisibility(View.GONE);
            holder.tvContent.setVisibility(View.VISIBLE);
            holder.tvContent.setText(bean.getCommentContent());
        }else {
            holder.ivContent.setVisibility(View.VISIBLE);
            holder.tvContent.setVisibility(View.GONE);
            holder.tvContent.setText("");
        }

        if (bean.getUser() == null)
            return;
        Glide.with(context).load(bean.getUser().getAvatar())
                .apply(new RequestOptions().dontAnimate().error(R.mipmap.icon_informationheard))
                .into(holder.ivIcon);
        holder.tvName.setText(bean.getUser().getNick());
        holder.tvTime.setText(DateUtil.formatTime(bean.getCreateTime(), context.getString(R.string.time_chat_dynamic)));

        if (bean.getSubDetail() == null){
            holder.rlRight.setVisibility(View.GONE);
            return;
        }
        holder.rlRight.setVisibility(View.VISIBLE);

        if (!StringUtils.isEmpty(bean.getSubDetail().getImg())) {
            Glide.with(context).load(bean.getSubDetail().getImg()).into(holder.ivRight);
            holder.ivRight.setVisibility(View.VISIBLE);
            holder.tvRight.setVisibility(View.GONE);
        }else if (!StringUtils.isEmpty(bean.getSubDetail().getContent())){
            holder.ivRight.setVisibility(View.GONE);
            holder.tvRight.setVisibility(View.VISIBLE);
            holder.tvRight.setText(bean.getSubDetail().getContent());
        }else {
            holder.ivRight.setVisibility(View.GONE);
            holder.tvRight.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = ActionUriUtils.getIntent(context,bean.getSubDetail().getActionUri(),"","");
                if (i != null)
                   context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public List<ChatDynamicBean.RowsBean> getList() {
        return list;
    }

    public void setList(List<ChatDynamicBean.RowsBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void addList(List<ChatDynamicBean.RowsBean> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    class Holder extends RecyclerView.ViewHolder {
        CircleImageView ivIcon;
        TextView tvName;
        TextView tvContent;
        ImageView ivContent;
        TextView tvTime;
        ImageView ivRight;
        TextView tvRight;
        RelativeLayout rlRight;


        public Holder(View itemView) {
            super(itemView);
            ivIcon = (CircleImageView) itemView.findViewById(R.id.iv_icon);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvContent = (TextView) itemView.findViewById(R.id.tv_content);
            ivContent = (ImageView) itemView.findViewById(R.id.iv_content);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            ivRight = (ImageView) itemView.findViewById(R.id.iv_right);
            tvRight = (TextView) itemView.findViewById(R.id.tv_right);
            rlRight = (RelativeLayout) itemView.findViewById(R.id.rl_right);
        }
    }
}
