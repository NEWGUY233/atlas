package com.atlas.crmapp.activity.index.fragment.livingspace.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.model.ResourceJson;
import com.atlas.crmapp.util.ActionUriUtils;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Administrator on 2018/3/20.
 */

public class LivingSpaceStudioAdapter extends RecyclerView.Adapter<LivingSpaceStudioAdapter.Holder> {
    Context c;
    private List<ResourceJson.ResourceMedia> list;
    public LivingSpaceStudioAdapter(Context c){
        this.c = c;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(c).inflate(R.layout.item_living_space_studio,parent,false));
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        final ResourceJson.ResourceMedia bean = list.get(position);
        Glide.with(c).load(bean.url).into(holder.iv_img);
        holder.tv_title.setText(bean.content);

        if ("VIDEO".equals(bean.actionType)){
            holder.iv_play.setVisibility(View.VISIBLE);
        }else {
            holder.iv_play.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ActionUriUtils.getIntent(c,bean.actionUri,bean.url,bean.content);
                if (intent != null)
                    c.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public List<ResourceJson.ResourceMedia> getList() {
        return list;
    }

    public void setList(List<ResourceJson.ResourceMedia> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    protected class Holder extends RecyclerView.ViewHolder{
        ImageView iv_play;
        ImageView iv_img;
        TextView tv_title;
        public Holder(View itemView) {
            super(itemView);
            iv_play = (ImageView) itemView.findViewById(R.id.iv_play);
            iv_img = (ImageView) itemView.findViewById(R.id.iv_img);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);

        }
    }
}
