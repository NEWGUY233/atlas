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
import com.atlas.crmapp.activity.index.fragment.index.activity.TagDetailActivity;
import com.atlas.crmapp.model.ResourceJson;
import com.atlas.crmapp.util.ActionUriUtils;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Administrator on 2018/3/20.
 */

public class LivingSpaceFoodAdapter extends RecyclerView.Adapter<LivingSpaceFoodAdapter.Holder> {
    Context c;
    private List<ResourceJson.ResourceMedia> mList;
    public LivingSpaceFoodAdapter(Context c){
        this.c = c;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(c).inflate(R.layout.item_living_space_food,parent,false));
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        final ResourceJson.ResourceMedia bean = mList.get(position);
        holder.tv_title.setText(bean.content);
        Glide.with(c).load(bean.url).into(holder.iv_bg);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = ActionUriUtils.getIntent(c,bean.actionUri,bean.url,bean.content);
                if (i != null)
                    c.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public List<ResourceJson.ResourceMedia> getmList() {
        return mList;
    }

    public void setmList(List<ResourceJson.ResourceMedia> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    protected class Holder extends RecyclerView.ViewHolder{
        ImageView iv_bg;
        TextView tv_title;
        public Holder(View itemView) {
            super(itemView);
            iv_bg = (ImageView) itemView.findViewById(R.id.iv_bg);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
        }
    }
}
