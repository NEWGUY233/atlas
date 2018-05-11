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
import com.atlas.crmapp.activity.index.fragment.index.activity.TagDetailActivity;
import com.atlas.crmapp.model.VThreadsJson;
import com.atlas.crmapp.util.GlideUtils;
import com.atlas.crmapp.util.StringUtils;

import java.util.List;

/**
 * Created by Administrator on 2018/3/20.
 */

public class AATagTagAdapter extends RecyclerView.Adapter<AATagTagAdapter.Holder> {
    Context c;
    private List<VThreadsJson> list;
    public AATagTagAdapter(Context c){
        this.c = c;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(c).inflate(R.layout.item_aat_tag_tag_body,parent,false));
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        final VThreadsJson bean = list.get(position);

        if (StringUtils.isEmpty(bean.getImage())){
            holder.iv_img.setVisibility(View.GONE);
        }else {
            holder.iv_img.setVisibility(View.VISIBLE);
            GlideUtils.loadImageView(c,bean.getImage(),holder.iv_img);
        }

        holder.tv_title.setText(bean.getTitle());
        holder.tv_see.setText(StringUtils.numberCheck(bean.getFoucsCnt()) + "");
        holder.tv_comment.setText(StringUtils.numberCheck(bean.getCommentCnt()) + "");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c.startActivity(new Intent(c, TagDetailActivity.class).putExtra("data",bean));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public List<VThreadsJson> getList() {
        return list;
    }

    public void setList(List<VThreadsJson> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    protected class Holder extends RecyclerView.ViewHolder{
        ImageView iv_img;
        TextView tv_title;
        TextView tv_see;
        TextView tv_comment;
        public Holder(View itemView) {
            super(itemView);
            iv_img = (ImageView) itemView.findViewById(R.id.iv_img);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_see = (TextView) itemView.findViewById(R.id.tv_see);
            tv_comment = (TextView) itemView.findViewById(R.id.tv_comment);
        }
    }
}
