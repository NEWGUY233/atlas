package com.atlas.crmapp.activity.index.fragment.index.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.activity.index.fragment.index.activity.TagCentreActivity;
import com.atlas.crmapp.activity.index.fragment.index.activity.TagDetailActivity;
import com.atlas.crmapp.model.VThreadsJson;
import com.atlas.crmapp.util.GlideUtils;
import com.atlas.crmapp.util.StringUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

/**
 * Created by Leo on 2018/3/20.
 */

public class AATagBodyAdapter extends RecyclerView.Adapter<AATagBodyAdapter.Holder> {
    Context c;
    private List<VThreadsJson> list;
    public AATagBodyAdapter(Context c){
        this.c = c;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(c).inflate(R.layout.item_aat_tag_body,parent,false));
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        final VThreadsJson bean = list.get(position);
        holder.tv_from.setText(c.getString(R.string.from_circle) + bean.getForumName());
        if (StringUtils.isEmpty(bean.getImage())){
            holder.iv_bg.setVisibility(View.GONE);
        }else {
            holder.iv_bg.setVisibility(View.VISIBLE);
//            GlideUtils.loadImageView(c,bean.getImage(),holder.iv_bg);
            Glide.with(c).load(bean.getImage()).apply(new RequestOptions().placeholder(R.mipmap.icon_noimage).centerCrop()).into(holder.iv_bg);
        }

        holder.tv_title.setText(bean.getTitle());
        holder.tv_follow.setText(StringUtils.numberCheck(bean.getFoucsCnt()) + c.getString(R.string.follow));
        holder.tv_answer.setText(StringUtils.numberCheck(bean.getCommentCnt()) + c.getString(R.string.answer_));
        holder.tv_thumb.setText(StringUtils.numberCheck(bean.getLikeCnt()) + c.getString(R.string.thumb));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c.startActivity(new Intent(c, TagDetailActivity.class).putExtra("data",bean));
            }
        });
        holder.tv_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c.startActivity(new Intent(c,TagCentreActivity.class).putExtra("id",bean.getForumId()));
            }
        });

    }

    @Override
    public int getItemCount() {
        return this.list == null ? 0 : list.size();
    }

    public List<VThreadsJson> getVthre() {
        return list;
    }

    public void setVthre(List<VThreadsJson> vthre) {
        this.list = vthre;
        notifyDataSetChanged();
    }

    protected class Holder extends RecyclerView.ViewHolder{
        TextView tv_from;
        TextView tv_title;
        TextView tv_follow;
        TextView tv_thumb;
        TextView tv_answer;
        ImageView iv_bg;
        public Holder(View itemView) {
            super(itemView);
            tv_from = (TextView) itemView.findViewById(R.id.tv_from);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_follow = (TextView) itemView.findViewById(R.id.tv_follow);
            tv_thumb = (TextView) itemView.findViewById(R.id.tv_thumb);
            tv_answer = (TextView) itemView.findViewById(R.id.tv_answer);
            iv_bg = (ImageView) itemView.findViewById(R.id.iv_bg);
        }
    }
}
